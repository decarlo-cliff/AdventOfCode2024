package day10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;

import common.Coordinate;
import common.DirectionEnum;

public class Day10SolutionPart2 {
  private final static String FILE_NAME = "src/day10/Day10Input.txt";
  
  public static void main(String[] args) {
    Day10SolutionPart2 solution = new Day10SolutionPart2();
    
    solution.findSolution();
  }
  
  private void findSolution() {
    try (FileReader fileReader = new FileReader(FILE_NAME); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String strLine = null;
      ArrayList<String> lstLines = new ArrayList<>();
      // Read every line from our topographic map
      int iLineLength = 0;
      while ((strLine = bufferedReader.readLine()) != null) {
        lstLines.add(strLine);
        if (iLineLength == 0) {
          iLineLength = strLine.length();
        }
      }
      // Create my map grid
      char[][] mapGrid = new char[lstLines.size()][iLineLength];
      int iRow = 0;
      for (String strCurrentLine : lstLines) {
        mapGrid[iRow] = strCurrentLine.toCharArray();
        iRow++;
      }
      //O.K. I have my map grid array
      int iSumScore = traverseTrail(mapGrid);
      
      System.out.println("Sume of scores = " + iSumScore);
    }
    catch (Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
  }
  
  private int traverseTrail(char[][] mapGrid) {
    ArrayList<Coordinate> lstStartingLocations = new ArrayList<Coordinate>();
    int iGridLen = mapGrid.length;
    int iGridWid = mapGrid[0].length;
    for(int iRow=0; iRow<iGridLen; iRow++) {
      for(int iCol=0; iCol<iGridWid; iCol++) {
        Coordinate coord = new Coordinate(iRow, iCol);
        if(getCharAtCoord(mapGrid, coord) == '0') {
          //This is a starting location
          lstStartingLocations.add(coord);
        }
      }
    }
    int iTotalBranchCountToPeak = 0;  //This will be my total of paths that I can take to get to the peaks
    //Now I have all of my starting positions....
    HashSet<Coordinate> hashPeaks = new HashSet<Coordinate>();  //This will keep track of my peaks (when I hit a 9)
    for(Coordinate startingCoord : lstStartingLocations) {
      hashPeaks.clear();  //Clear everything in case I've been here before
      iTotalBranchCountToPeak += countPathsToPeak(mapGrid, startingCoord, hashPeaks);
    }
    return iTotalBranchCountToPeak;
  }
  
  private int countPathsToPeak(char[][] mapGrid, Coordinate p_currentCoordinate, HashSet<Coordinate> hashPeaks) {
    //Check to see if I'm already at the peak
    int iCurrentHeight = Character.getNumericValue(getCharAtCoord(mapGrid, p_currentCoordinate));
    if(iCurrentHeight == 9) {
      //I've reached a peak
      hashPeaks.add(p_currentCoordinate);
      return 1;
    }
    //O.K. I'm not at a peak so look in every direction for a path
    int iTotalPaths = 0;
    for(DirectionEnum dirEnum : DirectionEnum.values()) {
      //Traverse all 4 directions (no diagonals in this puzzle)
      Coordinate newCoordinate = dirEnum.getCoordinateForDirection(p_currentCoordinate);
      
      if(isOutOfBounds(mapGrid, newCoordinate)) {
        continue;  //I'm out of bounds!
      }
      
      //See if the new location is 1 higher than my current location...and if so, keep traversing that path until I hit a peak
      int iNewHeight = Character.getNumericValue(getCharAtCoord(mapGrid, newCoordinate));
     
      if((iNewHeight - iCurrentHeight) == 1) {
        //This new coordinate is only one higher, so I can keep going on this path
        iTotalPaths += countPathsToPeak(mapGrid, newCoordinate, hashPeaks);  //Yay!  More recursive code!!!
      }
    }
    return iTotalPaths;
  }
  
  private boolean isOutOfBounds(char[][] mapGrid, Coordinate p_coord) {
    int iNumRows = mapGrid.length;
    int iNumCols = mapGrid[0].length;
    
    if(p_coord.getRow() < 0 || p_coord.getCol() < 0) {
      return true;
    }
    
    if((p_coord.getRow() >= iNumRows) || (p_coord.getCol() >= iNumCols)) {
      return true;
    }
    return false;
  }
  
  private char getCharAtCoord(char[][] mapGrid, Coordinate p_coord) {
    if(!isOutOfBounds(mapGrid, p_coord)) {
      return mapGrid[p_coord.getRow()][p_coord.getCol()];
    }
    return ' ';
  }
  
}
