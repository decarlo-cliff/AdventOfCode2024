package day6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Day6SolutionPart2 {
  private final static String FILE_NAME = "src/day6/Day6Input.txt";
  
  public static void main(String[] args) {
    Day6SolutionPart2 solution = new Day6SolutionPart2();
    
    solution.findSolution();
  }
  
  private void findSolution() {
    try (FileReader fileReader = new FileReader(FILE_NAME); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
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
      DirectionEnum absoluteStartingDirection = null;
      DirectionCoordinate absoluteStartingCoordinate = null;
      
      outerloop:      
      for(int iCurRow=0; iCurRow < mapGrid.length; iCurRow++) {
        for(int iCurCol = 0; iCurCol < mapGrid[0].length ; iCurCol++) {
          char currentChar = mapGrid[iCurRow][iCurCol];
          switch(currentChar) {
            case '^':
              absoluteStartingDirection = DirectionEnum.NORTH;
              absoluteStartingCoordinate = new DirectionCoordinate(iCurRow, iCurCol);
              break;
            case 'v':
              absoluteStartingDirection = DirectionEnum.SOUTH;
              absoluteStartingCoordinate = new DirectionCoordinate(iCurRow, iCurCol);
              break;
            case '<':
              absoluteStartingDirection = DirectionEnum.WEST;
              absoluteStartingCoordinate = new DirectionCoordinate(iCurRow, iCurCol);
              break;
            case '>':
              absoluteStartingDirection = DirectionEnum.EAST;
              absoluteStartingCoordinate = new DirectionCoordinate(iCurRow, iCurCol);
              break;
          }
          if(absoluteStartingDirection != null && absoluteStartingCoordinate != null) {
            break outerloop;
          }
        }
      }
      //O.K. I've found my starting position and coordinates and have them saved
      //I need to see how many new obstacles I can place to make the guards get stuck in a loop
      int iObstacleLoopCount = 0;
      for(int iCurRow = 0; iCurRow < mapGrid.length; iCurRow++) {
        for(int iCurCol = 0; iCurCol < mapGrid[0].length; iCurCol++) {
          char[][] gridCopy = makeGridCopy(mapGrid);
          DirectionCoordinate currentLocation = new DirectionCoordinate(iCurRow, iCurCol);
          if(!currentLocation.equals(absoluteStartingCoordinate)) {
            //I'm not at the starting location, so check if the current position is already an obstacle
            if(getCharAtCoord(gridCopy, currentLocation) != '#') {
              //O.K. I can add an obstacle here...so now see if this causes a loop to occur
              setCharAtCoord(gridCopy, currentLocation, '#');
              if(walkThroughGrid(gridCopy, absoluteStartingCoordinate, absoluteStartingDirection)) {
                System.out.println("Obstacle at : " + currentLocation.getRow() + "," + currentLocation.getCol() + " causes loop!");
                iObstacleLoopCount++;
              }
            }
            
          }
        }
      }
      
      System.out.println("Loop obstacle count = " + iObstacleLoopCount);
    }
    catch(Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
  }
  
  private char[][] makeGridCopy(char[][] mapGrid) {
    return Arrays.stream(mapGrid).map(char[]::clone).toArray(char[][]::new);
  }
  
  private boolean isOutOfBounds(char[][] mapGrid, DirectionCoordinate dirCoord) {
    int iNumRows = mapGrid.length;
    int iNumCols = mapGrid[0].length;
    
    if(dirCoord.getRow() < 0 || dirCoord.getCol() < 0) {
      return true;
    }
    
    if((dirCoord.getRow() >= iNumRows) || (dirCoord.getCol() >= iNumCols)) {
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
  
  private void setCharAtCoord(char[][] mapGrid, DirectionCoordinate dirCoordinate, char theChar) {
    if(!isOutOfBounds(mapGrid, dirCoordinate)) {
      mapGrid[dirCoordinate.getRow()][dirCoordinate.getCol()] = theChar;
    }
  }
  
  private boolean walkThroughGrid(char[][] mapGrid, DirectionCoordinate startingCoordinate, DirectionEnum startingDirectionEnum) throws CloneNotSupportedException {
    //Start at the staring position and go in the starting direction until I hit an obstacle or exit the grid
    DirectionCoordinate currentCoordinate = (DirectionCoordinate)startingCoordinate.clone();
    DirectionEnum currentDirection = startingDirectionEnum;
    ArrayList<DirectionCoordinate> lstSeenCoords = new ArrayList<>();
    int iPrevSeenCount = 0;
    
    while(!isOutOfBounds(mapGrid, currentCoordinate)) {
      //The first time through the loop, I'm at the starting location so it's safe to mark that location with an X
      //After the first time through, I will have updated the direction and row coordinates based on the direction I am headed and if there are any obstacles
      //If I get back to my starting position...then I'm in a loop!
      //mapGrid[currentCoordinate.getRow()][currentCoordinate.getCol()] = 'X';
      if(!lstSeenCoords.contains(currentCoordinate)) {
        lstSeenCoords.add(currentCoordinate);
      }
      else {
        //System.out.println("I've been at " + currentCoordinate.getRow() + "," + currentCoordinate.getCol() + " before!");
        iPrevSeenCount++;
        if(iPrevSeenCount >= (mapGrid.length * mapGrid[0].length)) {
          //System.out.println("Infinite loop detected!");
          return true; //Poor man's infinite loop detector (I'm going in circles)!  (There must be a better way to do this!)
        }
      }
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
    return false;
  }
}
