package day5;

import java.util.Comparator;
import java.util.List;

public class PrintOrderComparator implements Comparator<String> {
  private List<String> m_lstOfRules;
  
  public PrintOrderComparator(List<String> p_lstOrderRules) {
    m_lstOfRules = p_lstOrderRules;
  }
  
  @Override
  public int compare(String p_str1, String p_str2) {
    String strForward =  p_str1 + "|" + p_str2;
    String strBackward = p_str2 + "|" + p_str1;
    
    if(m_lstOfRules.contains(strForward)) {
      return -1;  //str1 comes before str2
    }
    else if(m_lstOfRules.contains(strBackward)) {
      return 1;  //str2 comes before str1
    }
    
    return 0;
  }

}
