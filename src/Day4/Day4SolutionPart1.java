package Day4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Day4SolutionPart1 {
  private static String[] WORDS_TO_FIND = { "XMAS" };

  public static void main(String[] args) {
    Day4SolutionPart1 solution = new Day4SolutionPart1();

    solution.findSolution();
  }

  private void findSolution() {
    try (FileReader fileReader = new FileReader("src/Day4/Day4Input.txt"); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
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

      for (String strFindWord : WORDS_TO_FIND) {
        int[][] ans = searchWord(wordGrid, strFindWord);
        printResult(ans);
      }

      System.out.println();
    }
    catch (Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
  }

  private void printResult(int[][] ans) {
    int iCounter = 0;
    for (int[] a : ans) {
      System.out.println("{" + a[0] + "," + a[1] + "}" + " ");
      iCounter++;
    }
    System.out.println("Total Finds : " + iCounter);
  }

  private int searchInGrid(char[][] grid, int row, int col, String word) {
    int iGridLength = grid.length;
    int iGridRowLength = grid[0].length;

    // return false if the given coordinate
    // does not match with first index char.
    if (grid[row][col] != word.charAt(0))
      return 0;

    int iFoundCount = 0;

    int len = word.length();

    //These are the directions to move the x and y coordinates within the grid...should I make this an ENUM?
    int[] x = { -1, -1, -1, 0, 0, 1, 1, 1 };
    int[] y = { -1, 0, 1, -1, 1, -1, 0, 1 };

    // This loop will search in all the 8 directions
    for (int dir = 0; dir < 8; dir++) {
      int k, currX = row + x[dir], currY = col + y[dir];

      // I already checked the first character matched above, so now check the rest.
      for (k = 1; k < len; k++) {
        if (currX >= iGridLength || currX < 0 || currY >= iGridRowLength || currY < 0)
          break;  //This is out of bounds, so break out of the loop

        if (grid[currX][currY] != word.charAt(k))
          break;  //Found one that didn't match the characters in the word...so just break out of the loop

        currX += x[dir];
        currY += y[dir];
      }

      if (k == len) {
        iFoundCount++;
      }
    }

    return iFoundCount;
  }

  //Search for a word inside my grid of character
  private int[][] searchWord(char[][] grid, String word) {
    int iGridLength = grid.length;
    int iGridNumCols = grid[0].length;

    // This is the maximum number of answers possible
    int[][] ans = new int[iGridLength * iGridNumCols][2];
    int iTotalCount = 0;
    int iFoundCount = 0;

    for (int i = 0; i < iGridLength; i++) {
      for (int j = 0; j < iGridNumCols; j++) {
        iFoundCount = searchInGrid(grid, i, j, word);
        if (iFoundCount > 0) {
          ans[iTotalCount][0] = i;
          ans[iTotalCount][1] = j;
          iTotalCount += iFoundCount;
        }
      }
    }

    int[][] result = new int[iTotalCount][2];
    for (int i = 0; i < iTotalCount; i++) {
      result[i] = ans[i];
    }

    return result;
  }

}
