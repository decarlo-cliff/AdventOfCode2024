package day10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;

import common.Coordinate;
import common.DirectionEnum;

public class Day10SolutionPart1 {
  private final static String FILE_NAME = "src/day10/Day10Input.txt";
  
  public static void main(String[] args) {
    Day10SolutionPart1 solution = new Day10SolutionPart1();
    
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
    for(Coordinate startingCoord : lstStartingLocations) {
      Deque<Coordinate> currentLocationDeck = new ArrayDeque<Coordinate>();
      currentLocationDeck.add(startingCoord);  //My current starting location
      HashSet<Coordinate> hashPeaks = new HashSet<Coordinate>();  //This will keep track of my peaks (when I hit a 9)
      int iNumOfPaths = 0;  //keep track of the number of paths that get me to a peak
      
      while(!currentLocationDeck.isEmpty()) {
        Coordinate currentCoordinate = currentLocationDeck.pop();  //Get the current location on my queue
        for(DirectionEnum dirEnum : DirectionEnum.values()) {
          //Traverse all 4 directions (no diagonals in this puzzle)
          Coordinate newCoordinate = dirEnum.getCoordinateForDirection(currentCoordinate);
          
          if(isOutOfBounds(mapGrid, newCoordinate)) {
            continue;  //I'm out of bounds!
          }
          
          //Get the height of the location I'm currently at
          int iCurrentHeight = Character.getNumericValue(getCharAtCoord(mapGrid, currentCoordinate));
          //Get the height of the new location I am traveling to
          int iNewHeight = Character.getNumericValue(getCharAtCoord(mapGrid, newCoordinate));
          
          if((iNewHeight - iCurrentHeight) != 1) {
            continue;  //I can only be one higher at the new location
          }
          
          //See if I'm at the peak (9)
          if(iNewHeight == 9) {
            //See if I've seen this before
            if(!hashPeaks.contains(newCoordinate)) {
              iNumOfPaths++;
              hashPeaks.add(newCoordinate);
            }
            continue; //Keep going
          }
          
          currentLocationDeck.add(newCoordinate);  //I'm in a new location now...push it onto the deck
        }
      }
      iTotalBranchCountToPeak += iNumOfPaths;
    }
    return iTotalBranchCountToPeak;
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
