package day3;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3SolutionPart1 {
  public static void main(String[] p_strArgs) {
    Day3SolutionPart1 solution = new Day3SolutionPart1();
    
    solution.findSolution();
  }
  
  private void findSolution() {
    try {
      byte[] byData = Files.readAllBytes(Paths.get("src/day3/Day3Input.txt"));
      String strData = new String(byData);
      
      Pattern pattern = Pattern.compile(".*?(mul\\(\\d*,\\d*\\)).*?", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
      Pattern patternNumbers = Pattern.compile(".*?(\\d*),(\\d*).*?");
      Matcher matcher = pattern.matcher(strData);
      long lTotal = 0;
      while(matcher.find()) {
        for(int i=1; i<= matcher.groupCount(); i++) {
          System.out.print(matcher.group(i) + " : ");
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
            lTotal += lProduct;
            System.out.println(iOne + " * " + iTwo + " = " + lProduct);
            System.out.println();
          }
        }
      }
      System.out.println("Grand Total : " + lTotal);
    }
    catch(Exception ex) {
      System.out.println("Exception happened!");
      ex.printStackTrace();
    }
  }

}
