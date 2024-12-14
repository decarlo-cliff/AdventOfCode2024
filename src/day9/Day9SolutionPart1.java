package day9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Day9SolutionPart1 {
  private final static String FILE_NAME = "src/day9/Day9Input.txt";
  
  public static void main(String[] args) {
    Day9SolutionPart1 solution = new Day9SolutionPart1();
    
    solution.findSolution();
  }
  
  private void findSolution() {
    try (FileReader fileReader = new FileReader(FILE_NAME); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String strLine = bufferedReader.readLine();
      //My list of disk objects
      ArrayList<DiskObject> lstDiskObject = new ArrayList<>();
      
      if(strLine != null) {
        //Read each character at a time, the even numbers are files and the odd numbers are free blocks
        int iCurrentFileNumber = 0;
        for(int iCurrentPos = 0; iCurrentPos<strLine.length(); iCurrentPos++) {
          int iVal = Character.getNumericValue(strLine.charAt(iCurrentPos));
          if(isEven(iCurrentPos)) {
            //The even locations are files
            DiskObject diskObject = new DiskObject(DiskObjectTypeEnum.FILE, iVal, iCurrentFileNumber);
            lstDiskObject.add(diskObject);
            iCurrentFileNumber++;
          }
          else {
            //The odd locations are free space objects
            DiskObject diskObject = new DiskObject(DiskObjectTypeEnum.FREE_SPACE, iVal);
            if(diskObject.getLength() > 0) {
              lstDiskObject.add(diskObject);  //Only add the free space disk object if there is actually free space!
            }
          }
        }
      }
      else {
        throw new Exception("Error reading " + FILE_NAME);
      }
      
      ArrayList<DiskObject> lstOptimized = optimizeDisk(lstDiskObject);
      ArrayList<Integer> lstFileNumber = new ArrayList<>();
      long lChecksum = 0l;
      for(DiskObject diskObject : lstOptimized) {
        for(int i=0; i<diskObject.getLength();i++) {
          lstFileNumber.add(Integer.valueOf(diskObject.getFileNumber()));
        }
      }
      for(int i=0;i<lstFileNumber.size();i++) {
        lChecksum += (i * lstFileNumber.get(i));
      }
      System.out.println("Checksum = " + lChecksum);
    }
    catch(Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
  }
  
  private ArrayList<DiskObject> optimizeDisk(ArrayList<DiskObject> p_lstDiskObjects) throws CloneNotSupportedException {
    //Calculate the total file length (this will tell me when to stop)
    long lTotalFileSize = 0l, lTotalFreeSpace = 0l;
    for(DiskObject diskObject : p_lstDiskObjects) {
      if(diskObject.getType() == DiskObjectTypeEnum.FILE) {
        lTotalFileSize += diskObject.getLength();
      }
      else {
        lTotalFreeSpace += diskObject.getLength();
      }
    }
    System.out.println("Total File Size: " + lTotalFileSize);
    System.out.println("Total Free Space: " + lTotalFreeSpace);
    //Optimize the disk by filling in the free space on the left from the objects on the right
    ArrayList<DiskObject> lstOptimized = new ArrayList<>();
    
    boolean bStop = false;
    int iCurrentLocationLeft = 0;
    int iCurrentLocationRight = p_lstDiskObjects.size() - 1;
    DiskObject doLHS = null, doRHS = null;
    while(!bStop) {
      if(doLHS == null) {
        doLHS = p_lstDiskObjects.get(iCurrentLocationLeft);
      }
      if(doLHS.equals(doRHS)) {
        //If the LHS and the RHS are the same object, then I'm done...add what's left to the end and exit
        lstOptimized.add(doLHS);
        bStop = true;
        System.out.println("RHS==LHS");
        continue;
      }
      if(iCurrentLocationLeft == 0) {
        //The 0 case is special...just move the first object in my unorganized list into the first location
        lstOptimized.add(doLHS);
        iCurrentLocationLeft++;
        doLHS = null;  //Set to null to get the next one
        continue;  //That's it for the first iteration
      }
      //Now I'm not at 0, so see if the disk object I'm looking at is free space....if it is get the object from the right hand side and start filling
      if(doLHS.getType() == DiskObjectTypeEnum.FREE_SPACE) {
        //This is free space...so start filling it from the disk objects on the right
        DiskObject doFreeSpace = doLHS;
        if(doRHS == null) {
          doRHS = p_lstDiskObjects.get(iCurrentLocationRight);
        }
        if(doRHS.getType() == DiskObjectTypeEnum.FREE_SPACE) {
          iCurrentLocationRight--;
          doRHS = null;
          continue;
        }
        else {
          //The rhs object is a file, so start filling up the free space
          if(doFreeSpace.getLength() >= doRHS.getLength()) {
            //The entire file object from the RHS will fit in the free space (so just put the whole disk object in the free space
            lstOptimized.add(doRHS);
            //How much free space is left?
            int iFreeSpaceLeft = doFreeSpace.getLength() - doRHS.getLength();
            if(iFreeSpaceLeft == 0) {
              //This means the RHS file just fit in the free space exactly
              iCurrentLocationRight--;
              iCurrentLocationLeft++;
              doLHS = doRHS = null;  //Set both to null to get the next object
              continue;
            }
            else {
              //There is still some free space..so don't get a new object from the LHS, but do get one from the RHS
              doFreeSpace.setLength(iFreeSpaceLeft);
              doRHS = null;
              iCurrentLocationRight--;
            }
          }
          else {
            //The file object from the RHS is bigger than the current free space, so I need to get more free space after filling up the current free space block
            DiskObject newDO = (DiskObject)doRHS.clone();
            newDO.setLength(doFreeSpace.getLength()); //I only wrote the amount that was left in the free space
            lstOptimized.add(newDO);
            iCurrentLocationLeft++;
            doRHS.setLength(doRHS.getLength() - doFreeSpace.getLength());  //Reduce the amount left in the RHS file
            doLHS = null; //The free space was used up, so keep going until I get another block of free space
          }
        }
      }
      else {
        //There is already a file at this location(LHS)...add it to the list
        lstOptimized.add(doLHS);
        doLHS = null;
        iCurrentLocationLeft++;
      }
      //I've hit the end of my free space, so stop looping
      if(iCurrentLocationLeft >= iCurrentLocationRight) {
        if(doRHS != null) {
          lstOptimized.add(doRHS);  //Just add what's left of the RHS file to the end
        }
        System.out.println("iCurrentLocationLeft==size of list");
        bStop = true;
      }
    }
    return lstOptimized;
  }
  
  
  private boolean isEven(int iNumber) {
    return ((iNumber %2) == 0); 
  }
}
