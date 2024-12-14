package day7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Day7SolutionPart1 {
  private final static String FILE_NAME = "src/day7/Day7Input.txt";
  
  public static void main(String[] args) {
    Day7SolutionPart1 solution = new Day7SolutionPart1();
    
    solution.findSolution();
  }
  
  private void findSolution() {
    try (FileReader fileReader = new FileReader(FILE_NAME); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String strLine = null;
      ArrayList<String> lstLines = new ArrayList<>();
      // Read every line from our word jumble file
      int iLineLength = 0;
      while ((strLine = bufferedReader.readLine()) != null) {
        lstLines.add(strLine);
        if (iLineLength == 0) {
          iLineLength = strLine.length();
        }
      }
      //I have all of my lines
      long lTotalSum = 0;
      for(String strCurrentLine : lstLines) {
        lTotalSum += getValueForLine(strCurrentLine);
      }
      
      System.out.println("Total Sum is : " + lTotalSum);
    }
    catch(Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
  }
  
  private boolean isPossible(ArrayList<Long> lstNumbers, int iCurrentIndex, long lCurrentTotal, long lRequiredTotal) {
    if(iCurrentIndex == lstNumbers.size()) {
      return lCurrentTotal == lRequiredTotal;  //I'm at the end of my list, so is the currently calculated total the same as the required total I'm looking for?
    }
    if(lCurrentTotal > lRequiredTotal) {
      //I've gone over the required total, so this cannot be a possible solution
      return false;
    }
    
    long lCurrentNumber = lstNumbers.get(iCurrentIndex);
    
    //Yay! Recursion!!!
    return isPossible(lstNumbers, iCurrentIndex + 1, lCurrentTotal + lCurrentNumber, lRequiredTotal) || isPossible(lstNumbers, iCurrentIndex + 1, lCurrentTotal * lCurrentNumber, lRequiredTotal);
  }
  
  private long getValueForLine(String strLine) {
    String[] strSplit = strLine.split("[ :]");
    int i=0;
    Long lLHS = 0l, lSum = 0l;
    ArrayList<Long> lstValues = new ArrayList<>();
    for(String str : strSplit) {
      if(str.length() > 0) {
        if(i==0) {
          lLHS = Long.valueOf(str);
          i++;
        }
        else {
          lstValues.add(Long.valueOf(str));
        }
      }
    }
    
    if(isPossible(lstValues, 1, lstValues.get(0), lLHS)) {
      lSum += lLHS;
    }
    
    return lSum;
  }

}
