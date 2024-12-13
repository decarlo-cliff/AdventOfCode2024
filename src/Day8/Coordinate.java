package Day8;

public class Coordinate {
  private int m_iRow;
  private int m_iCol;
  
  public Coordinate(int p_iRow, int p_iCol) {
    m_iRow = p_iRow;
    m_iCol = p_iCol;
  }

  public int getRow() {
    return m_iRow;
  }

  public void setRow(int row) {
    m_iRow = row;
  }

  public int getCol() {
    return m_iCol;
  }

  public void setCol(int col) {
    m_iCol = col;
  }
  
  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Coordinate) {
      Coordinate coord = (Coordinate)obj;
      return ((getCol() == coord.getCol()) && (getRow() == coord.getRow())); 
    }
    return false;
  }
  
  @Override
  public String toString() {
    return getRow() + "," + getCol();
  }
}
