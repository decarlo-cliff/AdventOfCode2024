package common;


public enum DirectionEnum {
  NORTH,SOUTH,EAST,WEST;
  
  public Coordinate getCoordinateForDirecton() {
    switch(this) {
      case NORTH:
        return new Coordinate(-1, 0);
      case SOUTH:
        return new Coordinate(1, 0);
      case EAST:
        return new Coordinate(0, 1);
      case WEST:
        return new Coordinate(0, -1);
      default :
        //Not sure what to do here..return null or a direction coordinate of 0,0 (might cause infinite loop)?
        return new Coordinate(0, 0);
    }
  }
  
  public DirectionEnum oppositeDirection() {
    switch(this) {
      case NORTH:
        return SOUTH;
      case SOUTH:
        return NORTH;
      case EAST:
        return WEST;
      case WEST:
        return EAST;
    }
    return this;
  }
  
  public Coordinate getCoordinateForDirection(Coordinate p_startingCoordinate) {
    Coordinate newCoordinate = new Coordinate(p_startingCoordinate.getRow(), p_startingCoordinate.getCol());
    switch(this) {
      case NORTH:
        newCoordinate.setRow(newCoordinate.getRow() - 1);
        break;
      case SOUTH:
        newCoordinate.setRow(newCoordinate.getRow() + 1);
        break;
      case EAST:
        newCoordinate.setCol(newCoordinate.getCol() + 1);
        break;
      case WEST:
        newCoordinate.setCol(newCoordinate.getCol() - 1);
        break;
    }
    return newCoordinate;
  }
}
