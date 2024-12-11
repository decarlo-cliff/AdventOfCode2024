package Day1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DayOneSolutionPart2 {
  public static void main(String[] p_strArgs) {
    DayOneSolutionPart2 solution = new DayOneSolutionPart2();
    
    solution.findSolution();
  }

  private void findSolution() {
    try(FileReader fileReader = new FileReader("src/Day1/Lists.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String strLine = null;
      ArrayList<Integer> lstOne = new ArrayList<Integer>();
      ArrayList<Integer> lstTwo = new ArrayList<Integer>();
      
      //Fill my lists from the list file
      while((strLine = bufferedReader.readLine()) != null) {
        String[] strColumns = strLine.split(" ");
        if(strColumns != null) {
          boolean bFirstList = true;
          for(String str : strColumns) {
            if(str.length() > 0) {
              if(bFirstList) {
                lstOne.add(Integer.valueOf(str));
                bFirstList = false;
              }
              else {
                lstTwo.add(Integer.valueOf(str));
                bFirstList = true;
              }
            }
          }
        }
      }
      //OK, I have my lists...so sort them
      Collections.sort(lstOne);
      Collections.sort(lstTwo);
      
      //Since I need to count the number of items in the second list...turn that into a HashMap
      HashMap<Integer, Integer> hashLstTwoCounts = new HashMap<Integer, Integer>(); 
      for(Integer integer : lstTwo) {
        if(!hashLstTwoCounts.containsKey(integer)) {
          //Not yet in my list, so put it in with a count of 1
          hashLstTwoCounts.put(integer, Integer.valueOf(1));
        }
        else {
          //Already in the list, so increase the count of this by one
          Integer iCurrentCount = hashLstTwoCounts.get(integer);
          iCurrentCount++;
          hashLstTwoCounts.put(integer, iCurrentCount);
        }
      }
      //Now I can loop through my first list and see how many of that value are in the second list
      int iSimilarityScore = 0;
      for(Integer integer : lstOne) {
        Integer iNumberOfThisInteger = hashLstTwoCounts.get(integer);
        if(iNumberOfThisInteger == null) {
          iNumberOfThisInteger = Integer.valueOf(0);
        }
        iSimilarityScore += (integer.intValue() * iNumberOfThisInteger.intValue());
      }
      System.out.println("Similarity score = " + iSimilarityScore);
    }
    catch(Exception ex) {
      System.out.println("Did not find list file");
      ex.printStackTrace();
    }
    finally {
    }
  }
}
