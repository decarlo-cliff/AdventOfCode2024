package Day1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class DayOneSolutionPart1 {
	public static void main(String[] p_strArgs) {
	  DayOneSolutionPart1 solution = new DayOneSolutionPart1();
	  
	  solution.findSolution();
	}

	private void findSolution() {
	  try(FileReader fileReader = new FileReader("src/Day1/Lists.txt");
	      BufferedReader bufferedReader = new BufferedReader(fileReader)) {
	    String strLine = null;
	    ArrayList<Integer> lstOne = new ArrayList<Integer>();
	    ArrayList<Integer> lstTwo = new ArrayList<Integer>();
	    
	    //Fill my lists from the list file
	    while((strLine = bufferedReader.readLine()) != null) {
	      String[] strColumns = strLine.split(" ");
	      if(strColumns != null) {
	        boolean bFirstList = true;
	        for(String str : strColumns) {
	          if(str.length() > 0) {
	            if(bFirstList) {
	              lstOne.add(Integer.valueOf(str));
	              bFirstList = false;
	            }
	            else {
	              lstTwo.add(Integer.valueOf(str));
	              bFirstList = true;
	            }
	          }
	        }
	      }
	    }
	    //OK, I have my lists...so sort them
	    Collections.sort(lstOne);
	    Collections.sort(lstTwo);
	    //Now get the difference between each element in the list (now that they have been sorted)
	    
	    //Just assume the lists have the same number of elements in them...
      int iTotalDistance = 0;
	    for(int i=0; i<lstOne.size();i++) {
	      Integer iOne = lstOne.get(i);
	      Integer iTwo = lstTwo.get(i);
	      //Let the autoboxing do the conversions to ints here for me...
	      int iDistance = Math.abs(iOne - iTwo);
	      iTotalDistance += iDistance;
	    }
	    System.out.println("Total distance between two lists = " + iTotalDistance);
	  }
	  catch(Exception ex) {
	    System.out.println("Did not find list file");
	    ex.printStackTrace();
	  }
	  finally {
	  }
	}
}
