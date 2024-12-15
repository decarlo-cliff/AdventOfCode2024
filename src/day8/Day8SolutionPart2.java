package day8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import common.Coordinate;

public class Day8SolutionPart2 {
  private final static String FILE_NAME = "src/day8/Day8Input.txt";
  
  public static void main(String[] args) {
    Day8SolutionPart2 solution = new Day8SolutionPart2();
    
    solution.findSolution();
  }
  
  private void findSolution() {
    try (FileReader fileReader = new FileReader(FILE_NAME); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String strLine = null;
      ArrayList<String> lstLines = new ArrayList<>();
      
      int iLineLength = 0;
      while ((strLine = bufferedReader.readLine()) != null) {
        lstLines.add(strLine);
        if (iLineLength == 0) {
          iLineLength = strLine.length();
        }
      }
      //Now I can construct my grid
      char[][] mapGrid = new char[lstLines.size()][lstLines.get(0).length()];
      int iRow = 0;
      for(String strCurrentLine : lstLines) {
        mapGrid[iRow] = strCurrentLine.toCharArray();
        iRow++;
      }
      
      //Loop through everything in my grid and extract all of the locations and store the coordinates in a hashmap
      HashMap<Character, ArrayList<Coordinate>> hashAntennaLocations = new HashMap<>();
      for(int iCurrRow = 0; iCurrRow < mapGrid.length; iCurrRow++) {
        for(int iCurrCol = 0; iCurrCol < mapGrid[0].length; iCurrCol++) {
          Coordinate coord = new Coordinate(iCurrRow, iCurrCol);
          char currentChar = getCharAtCoord(mapGrid, coord); 
          if(currentChar != '.') {
            //This is an antenna location
            if(hashAntennaLocations.containsKey(currentChar)) {
              hashAntennaLocations.get(currentChar).add(coord);
            }
            else {
              ArrayList<Coordinate> lstCoord = new ArrayList<>();
              lstCoord.add(coord);
              hashAntennaLocations.put(currentChar, lstCoord);
            }
          }
        }
      }
      //Now I have the locations of all the antennas
      ArrayList<Coordinate> lstAntiNodes = new ArrayList<>();  //List to contain all of the valid anti-node locations
      //For each different antenna, get the valid anti-node locations
      for(Entry<Character, ArrayList<Coordinate>> entry : hashAntennaLocations.entrySet()) { 
        for(int i = 0; i < entry.getValue().size(); i++) {
          for(int j = i+1; j < entry.getValue().size(); j++) {
            addValidAntiNodes(mapGrid, lstAntiNodes, entry.getValue().get(i), entry.getValue().get(j));  //Pass in the node pairs (1 node apart) and check the distance and then see if they are still in the grid
          }
        }
      }
      
      System.out.println("Number of valid anti-node locations = " + lstAntiNodes.size());
      
    }
    catch(Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
  }
  
  private void addValidAntiNodes(char[][] mapGrid, ArrayList<Coordinate> lstAntiNodes, Coordinate coord1, Coordinate coord2) {
    int iDeltaRow = coord2.getRow() - coord1.getRow();
    int iDeltaCol = coord2.getCol() - coord1.getCol();
    
    Coordinate possible1 = null;
    
    //For this case I have to traverse the entire grid in a straight line in both directions
    for(int iGridPos = (mapGrid.length * -1); iGridPos <= mapGrid.length; iGridPos++) {
      //This coordinate traverses the entire grid in a straight line
      possible1 = new Coordinate((coord1.getRow() + iGridPos * iDeltaRow), coord1.getCol()  + iGridPos * iDeltaCol);
      if(!addNodeIfNotOutOfBoundsForGridLine(mapGrid, lstAntiNodes, possible1, iGridPos)) {
        //If this is the case, then I need to stop looping through the grid line
        break;
      }
    }

  }
  
  private boolean addNodeIfNotOutOfBoundsForGridLine(char[][] mapGrid, ArrayList<Coordinate> lstAntiNodes, Coordinate p_coord, int iGridPos) {
    if(!isOutOfBounds(mapGrid, p_coord)) {
      if(!lstAntiNodes.contains(p_coord)) {
        lstAntiNodes.add(p_coord);
      }
      return true;
    }
    return iGridPos <= 0;
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
