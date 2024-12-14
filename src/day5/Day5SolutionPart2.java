package day5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Day5SolutionPart2 {
  public static void main(String[] args) {
    Day5SolutionPart2 day5 = new Day5SolutionPart2();
    
    day5.findSolution();
  }
  
  private void findSolution() {
    try (FileReader fileOrderReader = new FileReader("src/day5/Day5OrderInput.txt"); 
         BufferedReader bufferedOrderReader = new BufferedReader(fileOrderReader);
        FileReader filePrintOrderReader = new FileReader("src/day5/Day5PrintOrderList.txt");
        BufferedReader bufferedPrintOrderReader = new BufferedReader(filePrintOrderReader)) {
      
      
      //Read in the ordering rules
      ArrayList<String> lstOrderingRules = new ArrayList<String>();
      String strLine = null;
      while ((strLine = bufferedOrderReader.readLine()) != null) {
        lstOrderingRules.add(strLine);
      }
      
      ArrayList<ArrayList<String>> lstPrintOrderDirectives = new ArrayList<ArrayList<String>>(); 
      while ((strLine = bufferedPrintOrderReader.readLine()) != null) {
        String[] strPrintOrder = strLine.split(",");
        ArrayList<String> lstPageOrder = new ArrayList<String>();
        for(String strPageNumber : strPrintOrder) {
          lstPageOrder.add(strPageNumber);
        }
        lstPrintOrderDirectives.add(lstPageOrder);
      }
      
      //Now I have my print ordering rules and the page numbers to be printed...figure out which ones are correct
      
      System.out.println("The solution is : " + getCorrectedMiddleValueSum(lstPrintOrderDirectives, lstOrderingRules));
    }
    catch (Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
  }
  
  private boolean isOrderValid(ArrayList<String> lstPageOrder, ArrayList<String> lstOrderingRules) {
    //The list order is valid if the sorted list (using our comparator) is the same order as the original list
    ArrayList<String> originalList = new ArrayList<String>();
    ArrayList<String> sortedList = new ArrayList<String>();
    originalList.addAll(lstPageOrder);
    sortedList.addAll(lstPageOrder);
    
    PrintOrderComparator comparator = new PrintOrderComparator(lstOrderingRules);
    
    //Sort our list
    Collections.sort(sortedList, comparator);
    
    //Now see if our lists are in the same order
    for(int i=0; i<sortedList.size(); i++) {
      if(!sortedList.get(i).equals(originalList.get(i))) {
        return false;  //The order of the list changed and is therefore not in a valid order
      }
    }
    
    return true;
  }
  
  private int getCorrectedMiddleValueSum(ArrayList<ArrayList<String>> lstPrintOrderDirectives, ArrayList<String> lstOrderingRules) {
    int iTotalSum = 0;
    //Loop through all of the print directives and check to see if they are valid
    for(ArrayList<String> lstPageOrder : lstPrintOrderDirectives) {
      if(!isOrderValid(lstPageOrder, lstOrderingRules)) {
        //This means we have a badly sorted list...sort it and then grab the middle number
        Collections.sort(lstPageOrder, new PrintOrderComparator(lstOrderingRules));
        
        //Our list is now sorted properly...so get the middle value and sum them up!
        iTotalSum += Integer.valueOf(lstPageOrder.get(lstPageOrder.size() / 2));
      }
    }
    
    return iTotalSum;
  }
}
