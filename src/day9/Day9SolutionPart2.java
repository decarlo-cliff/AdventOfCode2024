package day9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Day9SolutionPart2 {
  private final static String FILE_NAME = "src/day9/Day9Input.txt";
  
  public static void main(String[] args) {
    Day9SolutionPart2 solution = new Day9SolutionPart2();
    
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
      
      //ArrayList<DiskObject> lstOptimized = optimizeDisk(lstDiskObject);
      ArrayList<DiskObject> lstOptimized = optimizeDiskByFile(lstDiskObject);
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
  
  //Part 2 wants us to only move a file from the right if it will totally fit into the free space
  private ArrayList<DiskObject> optimizeDiskByFile(ArrayList<DiskObject> p_lstDiskObjects) throws Exception {
    //Clone my list (so that I can manipulate this list and not worry about a mutating exception)
    ArrayList<DiskObject> lstClone = new ArrayList<>(p_lstDiskObjects.size());
    p_lstDiskObjects.stream().forEach(c -> {
      try {
        lstClone.add((DiskObject)c.clone());
      }
      catch (CloneNotSupportedException e) {
        throw new RuntimeException(e);
      }
    });
    ArrayList<DiskObject> lstAlreadyMoved = new ArrayList<>();  //Keep track of things I've already moved
    for(int iRHS=p_lstDiskObjects.size() -1; iRHS>=0;iRHS--) {
      //Now look in the cloned list for a free space object big enough for this file object
      DiskObject doRHS = p_lstDiskObjects.get(iRHS);
      if(lstAlreadyMoved.contains(doRHS)) {
        continue;
      }
      if(doRHS.getType() == DiskObjectTypeEnum.FILE) {
        int iFileSize = doRHS.getLength();
        for(int iLHS=0; iLHS<lstClone.size(); iLHS++) {
          DiskObject doLHS = lstClone.get(iLHS);
          if(doLHS.getType() == DiskObjectTypeEnum.FREE_SPACE) {
            if(doLHS.getLength() >= iFileSize) {
              //This block is big enough to fit my file...so put it here
              int iFreeSpaceLeft = doLHS.getLength() - iFileSize;
              //Make the current LHS disk object this file and add a new free space object (if there is any space left) to the right of this object
              doLHS.setType(doRHS.getType());
              doLHS.setFileNumber(doRHS.getFileNumber());
              doLHS.setLength(doRHS.getLength());
              lstAlreadyMoved.add(doRHS);  //Keep track of the file I just moved
              if(iFreeSpaceLeft > 0) {
                //Insert a new free space object because there is space left (unless the location is already a free space, then just increase the size)
                if(lstClone.get(iLHS+1).getType() == DiskObjectTypeEnum.FREE_SPACE) {
                  lstClone.get(iLHS+1).setLength(lstClone.get(iLHS+1).getLength() + iFreeSpaceLeft);
                }
                else {
                  DiskObject doFree = new DiskObject(DiskObjectTypeEnum.FREE_SPACE, iFreeSpaceLeft);
                  lstClone.add(iLHS+1, doFree);
                }
              }
              //Set the file object that I just processed to free space in my cloned list
              for(int k=lstClone.size() - 1; k>=0; k--) {
                DiskObject doClone = lstClone.get(k);
                if(doClone.equals(doRHS)) {
                  doClone.setType(DiskObjectTypeEnum.FREE_SPACE);
                  break;
                }
              }
              break; //Stop looking because I found a spot for it to be moved
            }
          }
        }
      }
    }
    ArrayList<DiskObject> lstDoFinal = new ArrayList<DiskObject>();
    //Compress free space objects next to each other...
    for(int i=0; i<lstClone.size()-1;i++) {
      DiskObject doCompress = lstClone.get(i);
      if(doCompress.getType() == DiskObjectTypeEnum.FILE) {
        lstDoFinal.add(doCompress);
      }
      else {
        //This is a free space...keep looking to the right to see if there is a bunch of free space objects in a row...and then compress them
        int iTotalFreeSpace = doCompress.getLength();
        int iSkip = 0;
        for(int j=i+1; j<lstClone.size();j++) {
          if(lstClone.get(j).getType() == DiskObjectTypeEnum.FREE_SPACE) {
            iTotalFreeSpace += lstClone.get(j).getLength();
            iSkip++; //Skip the free space I just found in the outer loop
          }
          else {
            break; //Stop because I hit a file
          }
        }
        if(iTotalFreeSpace > 0) {
          lstDoFinal.add(new DiskObject(DiskObjectTypeEnum.FREE_SPACE, iTotalFreeSpace));
          i += iSkip;
        }
      }
    }
    return lstDoFinal;
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
