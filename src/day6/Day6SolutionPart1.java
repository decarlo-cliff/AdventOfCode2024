package day6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Day6SolutionPart1 {
  public static void main(String[] args) {
    Day6SolutionPart1 solution = new Day6SolutionPart1();
    
    solution.findSolution();
  }
  
  private void findSolution() {
    try (FileReader fileReader = new FileReader("src/day6/Day6Input.txt"); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
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
      //Now I can construct my grid
      char[][] mapGrid = new char[lstLines.size()][lstLines.get(0).length()];
      int iRow = 0;
      for(String strCurrentLine : lstLines) {
        mapGrid[iRow] = strCurrentLine.toCharArray();
        iRow++;
      }
      //OK, the grid is built, so find the starting position
      DirectionEnum startingDirection = null;
      DirectionCoordinate startingCoordinate = null;
      
      outerloop:      
      for(int iCurRow=0; iCurRow < mapGrid.length; iCurRow++) {
        for(int iCurCol = 0; iCurCol < mapGrid[0].length ; iCurCol++) {
          char currentChar = mapGrid[iCurRow][iCurCol];
          switch(currentChar) {
            case '^':
              startingDirection = DirectionEnum.NORTH;
              startingCoordinate = new DirectionCoordinate(iCurRow, iCurCol);
              break;
            case 'v':
              startingDirection = DirectionEnum.SOUTH;
              startingCoordinate = new DirectionCoordinate(iCurRow, iCurCol);
              break;
            case '<':
              startingDirection = DirectionEnum.WEST;
              startingCoordinate = new DirectionCoordinate(iCurRow, iCurCol);
              break;
            case '>':
              startingDirection = DirectionEnum.EAST;
              startingCoordinate = new DirectionCoordinate(iCurRow, iCurCol);
              break;
          }
          if(startingDirection != null && startingCoordinate != null) {
            break outerloop;
          }
        }
      }
      //O.K. I've found my starting position and coordinates....so now I can implement the route rules
      walkThroughGrid(mapGrid, startingCoordinate, startingDirection);
      
      System.out.println("Location visited count = " + countXInGrid(mapGrid));
    }
    catch(Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
  }
  
  private int countXInGrid(char[][] mapGrid) {
    int iTotalCount = 0;
    for(int iRow = 0; iRow < mapGrid.length ; iRow++) {
      for(int iCol = 0; iCol < mapGrid[0].length; iCol++) {
        if(mapGrid[iRow][iCol] == 'X') {
          iTotalCount++;
        }
      }
    }
    return iTotalCount;
  }
  
  private boolean isOutOfBounds(char[][] mapGrid, DirectionCoordinate dirCoord) {
    int iNumRows = mapGrid.length;
    int iNumCols = mapGrid[0].length;
    
    if(dirCoord.getRow() < 0 || dirCoord.getCol() < 0) {
      return true;
    }
    
    if((dirCoord.getRow() > iNumRows) || (dirCoord.getCol() > iNumCols)) {
      return true;
    }
    return false;
  }
  
  private char getCharAtCoord(char[][] mapGrid, DirectionCoordinate dirCoord) {
    if(!isOutOfBounds(mapGrid, dirCoord)) {
      return mapGrid[dirCoord.getRow()][dirCoord.getCol()];
    }
    return ' ';
  }
  
  private void walkThroughGrid(char[][] mapGrid, DirectionCoordinate startingCoordinate, DirectionEnum startingDirectionEnum) {
    //Start at the staring position and go in the starting direction until I hit an obstacle or exit the grid
    DirectionCoordinate currentCoordinate = startingCoordinate;
    DirectionEnum currentDirection = startingDirectionEnum;
    
    while(!isOutOfBounds(mapGrid, currentCoordinate)) {
      //The first time through the loop, I'm at the starting location so it's safe to mark that location with an X
      //After the first time through, I will have updated the direction and row coordinates based on the direction I am headed and if there are any obstacles
      mapGrid[currentCoordinate.getRow()][currentCoordinate.getCol()] = 'X';
      //Now go in the current direction and check for an obstacle
      currentCoordinate = currentCoordinate.getNewCoordinateFromStartingAndDirection(currentCoordinate, currentDirection);
      if(!isOutOfBounds(mapGrid, currentCoordinate)) {
        //Check if this is an obstacle....
        if(getCharAtCoord(mapGrid, currentCoordinate) == '#') {
          //Obstacle detected....so move back one space in opposite direction in which I'm coming from (I cannot turn on the obstacle) then turn 90 degrees to the right and continue in that new direction
          //Back up one space..in the direction I came from
          currentCoordinate = currentCoordinate.getNewCoordinateFromStartingAndDirection(currentCoordinate, currentDirection.oppositeDirection());
          //Turn 90 degrees to the right and continue on
          currentDirection = currentDirection.getNewDirectionAfterObstacle();
        }
      }
    }
  }
}
