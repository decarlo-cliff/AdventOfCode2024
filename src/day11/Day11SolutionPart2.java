package day11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.IndexTreeList;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerCompressionWrapper;

//This was taking way too long and using too much memory, so I had to do it a different way that didn't store the entire list of stones every time
public class Day11SolutionPart2 {
  private final static String FILE_NAME = "src/day11/Day11Input.txt";
  
  private HashMap<Long, Integer> m_hashValues = new HashMap<>();
  private long[][] m_lValueArray = new long[10000][500];  //How big should I make this?
  private int m_iCurrentPosition = 1;
  
  public static void main(String[] args) {
    Day11SolutionPart2 solution = new Day11SolutionPart2();
    
    solution.findSolution();
  }
  
  public Day11SolutionPart2() {
  }
  
  private void findSolution() {
    try (FileReader fileReader = new FileReader(FILE_NAME); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String strLine = null;
      ArrayList<String> lstLines = new ArrayList<>();
      while ((strLine = bufferedReader.readLine()) != null) {
        lstLines.add(strLine);
      }
      //Now for each line read...(there is only 1 line for the first part) create a list of stones
      ArrayList<PlutoStone> lstStones = new ArrayList<>();
      for(String strCurrentLine : lstLines) {
        for(String strNumber : strCurrentLine.split(" ")) {
          lstStones.add(new PlutoStone(strNumber));
        }
      }
      //Now I have my list of stones....blink them!
      long lTotalCount = 0;
      for(PlutoStone plutoStone : lstStones) {
        lTotalCount += splitStones(plutoStone.getLongValueOfStone(), 75); 
      }
      
      System.out.println("New stone count = " + lTotalCount);
      
    }
    catch(Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
    finally {
    }
  }
  
  private long splitStones(long p_lNumber, int p_iBlinksLeft) {
    if(p_iBlinksLeft == 0) {
      return 1;
    }
    
    int iIDValue = getValueForLong(p_lNumber);
    if(m_lValueArray[iIDValue][p_iBlinksLeft] != 0) {
      return m_lValueArray[iIDValue][p_iBlinksLeft] - 1;
    }
    
    long lCount = 0;
    if(p_lNumber == 0) {
      lCount = splitStones(1, p_iBlinksLeft -1);
    }
    else if(PlutoStone.hasEvenDigits(p_lNumber)) {
      Long[] lDigits = PlutoStone.splitDigitsToLong(p_lNumber);
      lCount += splitStones(lDigits[0], p_iBlinksLeft -1);
      lCount += splitStones(lDigits[1], p_iBlinksLeft -1);
    }
    else {
      lCount = splitStones(p_lNumber * 2024, p_iBlinksLeft -1);
    }
    
    m_lValueArray[iIDValue][p_iBlinksLeft] = lCount + 1;
    
    return lCount;
  }
  
  //See if I have this value in my hash...if not add it, then return the value stored
  private int getValueForLong(Long p_lValue) {
    if(!m_hashValues.containsKey(p_lValue)) {
      m_hashValues.put(p_lValue, m_iCurrentPosition++);
    }
    return m_hashValues.get(p_lValue);
  }
}
 