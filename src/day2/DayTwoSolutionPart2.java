package day2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class DayTwoSolutionPart2 {
  public static void main(String[] p_strArgs) {
    DayTwoSolutionPart2 solution = new DayTwoSolutionPart2();
    
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
    //These lists are now allow to have one "bad" value
    //Let's just remove an element one by one and check until we either checked everything or found a good one
    ArrayList<Integer> lstToTest = new ArrayList<Integer>();
    for(int i=0; i<lstInteger.size(); i++) {
      //Clear and then clone my list
      lstToTest.clear();
      lstToTest.addAll(lstInteger);
      lstToTest.remove(i);
      if(isListOrderAndDistanceCorrect(lstToTest)) {
        return true;
      }
    }
    return false;  //None of the permutations were safe
  }

  private boolean isListOrderAndDistanceCorrect(ArrayList<Integer> originalList) {
    boolean bIsAcending;
    boolean bIsDescending;
    //Now my list should either have any "bad" elements removed (or it didn't have any to begin with)
    //Reset my list booleans
    bIsAcending = true;
    bIsDescending = true;
    
    //Clone my list again
    ArrayList<Integer> lstOriginalSorted = new ArrayList<>();
    lstOriginalSorted.addAll(originalList);
    
    //Sort my list ascending
    Collections.sort(lstOriginalSorted);
    //Now see if this list matches the original list
    for(int i=0; i<originalList.size(); i++) {
      if(!originalList.get(i).equals(lstOriginalSorted.get(i))) {
        bIsAcending = false;
        break;  //Found one that wasn't the correct ordering, so I don't need to keep looking
      }
    }
    //Now do the same thing in reverse to check for descending
    Collections.reverse(lstOriginalSorted);
    for(int i=0; i<originalList.size(); i++) {
      if(!originalList.get(i).equals(lstOriginalSorted.get(i))) {
        bIsDescending = false;
        break;  //Found one that wasn't the correct ordering, so I don't need to keep looking
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
