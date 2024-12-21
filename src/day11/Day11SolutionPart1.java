package day11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.IndexTreeList;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerCompressionWrapper;

public class Day11SolutionPart1 {
  private final static String FILE_NAME = "src/day11/Day11Input.txt";
  
  public static void main(String[] args) {
    Day11SolutionPart1 solution = new Day11SolutionPart1();
    
    solution.findSolution();
  }
  
  public Day11SolutionPart1() {
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
      long lNewStoneCount = blinkStones(lstStones, 25);
      
      System.out.println("New Stone Count = " + lNewStoneCount);
      
    }
    catch(Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
    finally {
    }
  }
  
  private long blinkStones(List<PlutoStone> p_lstStones, int p_iBlinkTimes) {
    long lTotalCount = 0;
    //Blink each stone and then get the count
    for(PlutoStone plutoStone : p_lstStones) {
      lTotalCount += plutoStone.blinkStones(new ArrayList<PlutoStone>(Arrays.asList(plutoStone)), p_iBlinkTimes).size();
      plutoStone = null;
      System.gc();
    }
    return lTotalCount;
  }
}
