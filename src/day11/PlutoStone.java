package day11;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.mapdb.IndexTreeList;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerCompressionWrapper;

/**
 * If the stone is engraved with the number 0, it is replaced by a stone engraved with the number 1.
 * If the stone is engraved with a number that has an even number of digits, it is replaced by two stones. 
 *      The left half of the digits are engraved on the new left stone, and the right half of the digits are engraved on the new right stone. (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)
*  If none of the other rules apply, the stone is replaced by a new stone; the old stone's number multiplied by 2024 is engraved on the new stone.
 */

public class PlutoStone implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String m_strNumber;
  
  public PlutoStone(String p_strNumber) {
    m_strNumber = Long.valueOf(p_strNumber).toString();  //Convert to numeric and then back to string to remove leading 0's
  }
  
  public PlutoStone(long p_lNumber) {
    m_strNumber = Long.toString(p_lNumber);
  }
  
  public boolean isZero() {
    if(Long.valueOf(m_strNumber) == 0) {
      return true;
    }
    return false;
  }
  
  private boolean hasEvenDigits() {
    return !isZero() && ((m_strNumber.length() % 2) == 0);
  }
  
  public static boolean hasEvenDigits(Long p_lNumber) {
    String strNumber = Long.toString(p_lNumber);
    if((p_lNumber != 0) && (strNumber.length() % 2 == 0)) {
      return true;
    }
    return false;
  }
  
  public Long getLongValueOfStone() {
    return Long.valueOf(m_strNumber);
  }
  
  public static Long[] splitDigitsToLong(String p_strNumber) {
    if(p_strNumber.length() % 2 == 0) {
      Long[] lArray = new Long[2];
      lArray[0] = Long.valueOf(p_strNumber.substring(0, p_strNumber.length() / 2));
      lArray[1] = Long.valueOf(p_strNumber.substring(p_strNumber.length() / 2));
      return lArray;
    }
    
    return null;
  }
  
  public static Long[] splitDigitsToLong(Long p_lNumber) {
    return splitDigitsToLong(p_lNumber.toString());
  }
  
  private String[] splitDigits() {
    String[] digitArray = new String[2];
    if(hasEvenDigits()) {
      digitArray[0] = m_strNumber.substring(0, m_strNumber.length() / 2);
      digitArray[1] = m_strNumber.substring(m_strNumber.length() / 2);
      
      return digitArray;
    }
    else {
      throw new RuntimeException("This does not have an even number of digits!");
    }
  }
  
  public PlutoStone multiplyBy2024() {
    Long lNewValue = Long.valueOf(m_strNumber) * 2024;
    return new PlutoStone(lNewValue);
  }
  
  public Long multiplyStoneBy2024() {
    return  Long.valueOf(m_strNumber) * 2024;
  }
  
  public List<PlutoStone> blinkStones(List<PlutoStone> p_lstStones, int p_iBlinkTimes) {
    List<PlutoStone> lstNewStones = new ArrayList<PlutoStone>();
    System.out.println("Blink : " + p_iBlinkTimes);
    if(p_iBlinkTimes == 0) {
      return p_lstStones;
    }
    else {
      for(PlutoStone plutoStone : p_lstStones) {
        lstNewStones.addAll(plutoStone.blink());
      }
      p_lstStones.clear();
      p_lstStones = null;
      System.gc();
      lstNewStones = blinkStones(lstNewStones, (p_iBlinkTimes - 1));
    }
    return lstNewStones;
  }
  
  public Collection<PlutoStone> blink() {
    ArrayList<PlutoStone> lstStones = new ArrayList<>(2);
    
    if(isZero()) {
      lstStones.add(new PlutoStone(1));
    }
    else if(hasEvenDigits()) {
      String[] splitArray = splitDigits();
      lstStones.add(new PlutoStone(splitArray[0]));
      lstStones.add(new PlutoStone(splitArray[1]));
    }
    else {
      lstStones.add(multiplyBy2024());
    }
    
    return lstStones;
  }
  
  @Override
  public String toString() {
    return m_strNumber;
  }
  
  @Override
  public boolean equals(Object obj) {
    if(obj instanceof PlutoStone) {
      return m_strNumber.equals(((PlutoStone)obj).m_strNumber);
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(m_strNumber);
  }
}
