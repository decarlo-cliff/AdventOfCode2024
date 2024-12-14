package day6;

public enum DirectionEnum {
  NORTH,SOUTH,EAST,WEST;
  
  public DirectionCoordinate getCoordinateForDirecton() {
    switch(this) {
      case NORTH:
        return new DirectionCoordinate(-1, 0);
      case SOUTH:
        return new DirectionCoordinate(1, 0);
      case EAST:
        return new DirectionCoordinate(0, 1);
      case WEST:
        return new DirectionCoordinate(0, -1);
      default :
        //Not sure what to do here..return null or a direction coordinate of 0,0 (might cause infinite loop)?
        return new DirectionCoordinate(0, 0);
    }
  }
  
  public DirectionEnum getNewDirectionAfterObstacle() {
    switch(this) {
      case NORTH:
        return EAST;
      case SOUTH:
        return WEST;
      case EAST:
        return SOUTH;
      case WEST:
        return NORTH;
    }
    return this;
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
 
}
