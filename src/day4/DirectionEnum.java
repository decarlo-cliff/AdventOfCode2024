package day4;

public enum DirectionEnum {
  NORTH(-1, 0),
  SOUTH(1, 0),
  EAST(0, 1),
  WEST(0, -1),
  NORTHEAST(-1, 1),
  NORTHWEST(-1, -1),
  SOUTHEAST(1, 1),
  SOUTHWEST(1, -1);
 
  private int ROW_DIRECTION;
  private int COL_DIRECTION;
  
  private DirectionEnum(int iRowDirection, int iColDirection) {
    ROW_DIRECTION = iRowDirection;
    COL_DIRECTION = iColDirection;
  }
  
  public int getRowDirection() {
    return ROW_DIRECTION;
  }
  
  public int getColDirection() {
    return COL_DIRECTION;
  }
}
