package day11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.IndexTreeList;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerCompressionWrapper;

public class Day11SolutionPart1 {
  private final static String FILE_NAME = "src/day11/Day11Input.txt";
  private final static String DB_FILE = "src/day11/mapdb.db";
  
  private DB DATABASE = null;
  
  public static void main(String[] args) {
    Day11SolutionPart1 solution = new Day11SolutionPart1();
    
    solution.findSolution();
  }
  
  public Day11SolutionPart1() {
    try {
      Files.deleteIfExists(Path.of(DB_FILE));
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    //DATABASE = DBMaker.fileDB(DB_FILE).allocateStartSize(2048000000).concurrencyDisable().make();
    //DATABASE = DBMaker.memoryDB().concurrencyDisable().cleanerHackEnable().make();
    DATABASE = DBMaker.heapDB().concurrencyDisable().cleanerHackEnable().make();
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
      int iNewStoneCount = blinkStones(lstStones, 75).size();
      
      System.out.println("New Stone Count = " + iNewStoneCount);
      
    }
    catch(Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
    finally {
      DATABASE.close();
    }
  }
  
  private List<PlutoStone> blinkStones(List<PlutoStone> p_lstStones, int p_iBlinkTimes) {
    System.out.println("Blink : " + p_iBlinkTimes + " : Size = " + p_lstStones.size());
    
    
    @SuppressWarnings("unchecked")
    List<PlutoStone> lstNewStones = (List<PlutoStone>) DATABASE.indexTreeList("stone-list" + p_iBlinkTimes, new SerializerCompressionWrapper<PlutoStone>(Serializer.JAVA)).createOrOpen();
    if(p_iBlinkTimes == 0) {
      return p_lstStones;
    }
    else {
      for(PlutoStone plutoStone : p_lstStones) {
        lstNewStones.addAll(plutoStone.blink());
      }
      IndexTreeList<PlutoStone> lastList = DATABASE.get("stone-list" + (p_iBlinkTimes + 1));
      if(lastList != null) {
        lastList.clear();
        DATABASE.getStore().compact();
        lastList = null;
        System.gc();
      }
      lstNewStones = blinkStones(lstNewStones, (p_iBlinkTimes - 1));
    }
    return lstNewStones;
  }
}
