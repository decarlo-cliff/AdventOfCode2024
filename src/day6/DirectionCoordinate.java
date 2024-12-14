package day6;

public class DirectionCoordinate {
  private int m_iRow;
  private int m_iCol;
  
  public DirectionCoordinate(int iRow, int iCol) {
    m_iRow = iRow;
    m_iCol = iCol;
  }
  
  public int getRow() {
    return m_iRow;
  }

  public int getCol() {
    return m_iCol;
  }
  
  public DirectionCoordinate getNewCoordinateFromStartingAndDirection(DirectionCoordinate startingCoordinate, DirectionEnum directionEnum) {
    return new DirectionCoordinate(startingCoordinate.getRow() + directionEnum.getCoordinateForDirecton().getRow(), startingCoordinate.getCol() + directionEnum.getCoordinateForDirecton().getCol());
  }
  
  @Override
  public boolean equals(Object obj) {
    if(obj instanceof DirectionCoordinate) {
      DirectionCoordinate coord2 = (DirectionCoordinate)obj;
      return ((getRow() == coord2.getRow() && getCol() == coord2.getCol()));
    }
    return false;
  }
  
  @Override
  protected Object clone() throws CloneNotSupportedException {
    return new DirectionCoordinate(getRow(), getCol());
  }
}
