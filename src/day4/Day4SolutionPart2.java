package day4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Day4SolutionPart2 {

  public static void main(String[] args) {
    Day4SolutionPart2 solution = new Day4SolutionPart2();

    solution.findSolution();
  }

  private void findSolution() {
    try (FileReader fileReader = new FileReader("src/day4/Day4Input.txt"); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
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
      // Create my wordGrid
      char[][] wordGrid = new char[lstLines.size()][iLineLength];
      int iRow = 0;
      for (String strCurrentLine : lstLines) {
        wordGrid[iRow] = strCurrentLine.toCharArray();
        iRow++;
      }

      // The grid is built...so look for the MAS X shape
      int iMASCount = findMAS(wordGrid);

      System.out.println("MAS Count : " + iMASCount);

    }
    catch (Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
  }

  public static boolean isOutOfBounds(int iGridLength, int iGridColLength, int row, int col) {
    //is the row/col I'm asking for beyond the bounds of the grid?
    return (row < 0 || row > iGridLength - 1 || col < 0 || col > iGridColLength - 1);
  }

  private char getCharForGridLocation(char[][] wordGrid, int iRow, int iCol, DirectionEnum dirEnum) {
    int iGridLength = wordGrid.length;
    int iGridColLength = wordGrid[0].length;
    
    if(isOutOfBounds(iGridLength, iGridColLength, iRow + dirEnum.getRowDirection(), iCol + dirEnum.getColDirection())) {
      return ' ';
    }
    else {
      return wordGrid[iRow + dirEnum.getRowDirection()][iCol + dirEnum.getColDirection()];
    }
  }
  
  private int findMAS(char[][] wordGrid) {
    int iFoundCount = 0;
    // Loop through the entire grid
    for (int col = 0; col < wordGrid[0].length; col++) {
      for (int row = 0; row < wordGrid.length; row++) {
        if(countMAS(wordGrid, row, col)) {
          iFoundCount++;
        }
      }
    }
    return iFoundCount;
  }

  public boolean countMAS(char[][] wordGrid, int row, int col) {
    // Look for 'A' since it's in the middle of the X and then check NW, SE, NE, SW for the M-S or S-M on the diagonal
    
    if (wordGrid[row][col] == 'A') {
      char topLeft = getCharForGridLocation(wordGrid, row, col, DirectionEnum.NORTHWEST);
      char bottomRight = getCharForGridLocation(wordGrid, row, col, DirectionEnum.SOUTHEAST);
      if (topLeft == ' ' || bottomRight == ' ') {
        return false;
      }
      else {
        if (topLeft == 'M') {
          if (bottomRight != 'S')
            return false;
        }
        else if (topLeft == 'S') {
          if (bottomRight != 'M')
            return false;
        }
        else {
          return false;
        }
      }
      char topRight = getCharForGridLocation(wordGrid, row, col, DirectionEnum.NORTHEAST);
      char bottomLeft = getCharForGridLocation(wordGrid, row, col, DirectionEnum.SOUTHWEST);
      if (topRight == ' ' || bottomLeft == ' ') {
        return false;
      }
      else {
        if (topRight == 'M') {
          if (bottomLeft != 'S')
            return false;
        }
        else if (topRight == 'S') {
          if (bottomLeft != 'M')
            return false;
        }
        else {
          return false;
        }
      }
      return true;
    }
    return false;
  }

}
