package day2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class DayTwoSolutionPart1 {
  public static void main(String[] p_strArgs) {
    DayTwoSolutionPart1 solution = new DayTwoSolutionPart1();
    
    solution.findSolution();
  }
  
  private void findSolution() {
    try(FileReader fileReader = new FileReader("src/day2/Day2List.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String strLine = null;
      ArrayList<ArrayList<Integer>> lstOfNumbers = new ArrayList<ArrayList<Integer>>(); 
      while((strLine = bufferedReader.readLine()) != null) {
        ArrayList<Integer> lstIntegers = new ArrayList<Integer>();
        String[] strColumns = strLine.split(" ");
        for(String str : strColumns) {
          if(str.length() > 0) {
            lstIntegers.add(Integer.valueOf(str));
          }
        }
        lstOfNumbers.add(lstIntegers);
      }
      int iSafeCount = 0;
      //Now check the safety of each list within the list
      for(ArrayList<Integer> lstInteger : lstOfNumbers) {
        if(isListSafe(lstInteger)) {
          iSafeCount++;
        }
      }
      System.out.println("Safe Count : " + iSafeCount);
      
    }
    catch(Exception ex) {
      System.out.println("Exception happened!");
      ex.printStackTrace();
    }
    finally {
    }
  }
  
  private boolean isListSafe(ArrayList<Integer> lstInteger) {
    //Save my list
    ArrayList<Integer> originalList = new ArrayList<Integer>();
    originalList.addAll(lstInteger);
    
    boolean bIsAcending = true;
    boolean bIsDescending = true;
    
    //Sort my list ascending
    Collections.sort(lstInteger);
    //Now see if this list matches the original list
    for(int i=0; i<originalList.size(); i++) {
      if(!originalList.get(i).equals(lstInteger.get(i))) {
        bIsAcending = false;
      }
    }
    //Now do the same thing in reverse to check for descending
    Collections.reverse(lstInteger);
    for(int i=0; i<originalList.size(); i++) {
      if(!originalList.get(i).equals(lstInteger.get(i))) {
        bIsDescending = false;
      }
    }
    
    if(bIsAcending || bIsDescending) {
      //Check to make sure the difference between any two positions is 1 <= i >= 3  (at least one and at most three)
      for(int i = 0; i < originalList.size() - 1; i++) {
        int iDistance = Math.abs(originalList.get(i) - originalList.get(i+1)); 
        if(!(iDistance >= 1 && iDistance <=3)) {
          return false;
        }
      }
      //I didn't return and the list was either ascending or descending...so this is a "safe" list
      return true;
    }
    return false;
  }
}