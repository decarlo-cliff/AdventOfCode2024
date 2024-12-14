package day3;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3SolutionPart2 {
  public static void main(String[] p_strArgs) {
    Day3SolutionPart2 solution = new Day3SolutionPart2();
    
    solution.findSolution();
  }
  
  private void findSolution() {
    try {
      byte[] byData = Files.readAllBytes(Paths.get("src/day3/Day3Input.txt"));
      String strData = new String(byData);
      
      //Find all of the do() and don't()
      ArrayList<Integer> lstDo = new ArrayList<Integer>();
      ArrayList<Integer> lstDont = new ArrayList<Integer>();
      int iIndex = -1;
      do {
        iIndex = strData.indexOf("do()", iIndex + 1);
        System.out.println("Index do() = " + iIndex);
        if(iIndex > 0) {
          lstDo.add(Integer.valueOf(iIndex));
        }          
      } while(iIndex > 0);
      
      iIndex = -1;
      do {
        iIndex = strData.indexOf("don't()", iIndex + 1);
        System.out.println("Index don't() = " + iIndex);
        if(iIndex > 0) {
          lstDont.add(Integer.valueOf(iIndex));
        }
      } while(iIndex > 0);
      
      HashMap<Integer, String> hashDoDont = new HashMap<Integer, String>();
      
      for(Integer integer : lstDo) {
        hashDoDont.put(integer, "DO");
      }
      for(Integer integer : lstDont) {
        hashDoDont.put(integer, "DONT");
      }
      
      Set<Integer> setKeys = hashDoDont.keySet();
      ArrayList<Integer> lstKeys = new ArrayList<Integer>();
      setKeys.stream().forEach(k -> lstKeys.add(k));
      
      Collections.sort(lstKeys);
      
      LinkedHashMap<Integer, String> hashDoDontSorted = new LinkedHashMap<Integer, String>();
      for(Integer iKey : lstKeys) {
        hashDoDontSorted.put(iKey, hashDoDont.get(iKey));
      }
      
      //Get all of the positions for do and don't
      HashMap<Integer, String> hashDoDontPositions = new HashMap<>();
      String strLastValue = "DO";
      for(int i = 0; i < strData.length(); i++) {
        if(i == 0) {
          hashDoDontPositions.put(Integer.valueOf(i), strLastValue);
        }
        else {
          if(hashDoDontSorted.containsKey(Integer.valueOf(i))) {
            strLastValue = hashDoDontSorted.get(Integer.valueOf(i));
          }
          hashDoDontPositions.put(Integer.valueOf(i), strLastValue);
        }          
      }
      
      //Now I have my do/dont ranges, so I can figure out the new sum 
      Pattern pattern = Pattern.compile(".*?(mul\\(\\d*,\\d*\\)).*?", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
      Pattern patternNumbers = Pattern.compile(".*?(\\d*),(\\d*).*?");
      Matcher matcher = pattern.matcher(strData);
      long lTotal = 0;
      while(matcher.find()) {
        for(int i=1; i<= matcher.groupCount(); i++) {
          System.out.print(matcher.group(i) + "(" + strData.indexOf(matcher.group(i)) + ") : ");
          Matcher matcherNumbers = patternNumbers.matcher(matcher.group(i));
          ArrayList<Integer> lstIntegers = new ArrayList<>();
          while(matcherNumbers.find()) {
            for(int j=1; j<= matcherNumbers.groupCount(); j++) {
              lstIntegers.add(Integer.valueOf(matcherNumbers.group(j)));
            }
            long lProduct = 0;
            int iOne = lstIntegers.get(0);
            int iTwo = lstIntegers.get(1);
            lProduct = iOne * iTwo;
            if(hashDoDontPositions.get(strData.indexOf(matcher.group(i))).equals("DO")) {
              lTotal += lProduct;
              System.out.println(iOne + " * " + iTwo + " = " + lProduct);
            }
            else {
              System.out.println("SKIPPING: " + iOne + " * " + iTwo + " = " + lProduct);
            }
            System.out.println();
          }
        }
      }
      System.out.println("Grand Total : " + lTotal);
      
      
      
      
      System.out.println();
      
    }
    catch(Exception ex) {
      System.out.println("Exception happened!");
      ex.printStackTrace();
    }
  }

}
