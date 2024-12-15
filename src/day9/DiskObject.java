package day9;

public class DiskObject {
  private int m_iLength;
  private int m_iFileNumber;
  private DiskObjectTypeEnum m_type;
  
  public DiskObject(DiskObjectTypeEnum p_objectType, int p_iLength, int p_iFileNumber) {
    m_type = p_objectType;
    m_iLength = p_iLength;
    m_iFileNumber = p_iFileNumber;
  }
  
  public DiskObject(DiskObjectTypeEnum p_objectType, int p_iLength) {
    if(p_objectType != DiskObjectTypeEnum.FREE_SPACE) {
      throw new RuntimeException("A FILE object must have a file number!");
    }
    m_type = p_objectType;
    m_iLength = p_iLength;
    m_iFileNumber = 0;  //This is a free space block, so there is no file number
  }

  public int getLength() {
    return m_iLength;
  }

  public void setLength(int length) {
    m_iLength = length;
  }

  public int getFileNumber() {
    if(getType() == DiskObjectTypeEnum.FREE_SPACE) {
      return 0;
    }
    return m_iFileNumber;
  }

  public void setFileNumber(int fileNumber) {
    m_iFileNumber = fileNumber;
  }

  public DiskObjectTypeEnum getType() {
    return m_type;
  }

  public void setType(DiskObjectTypeEnum type) {
    m_type = type;
  }
  
  @Override
  public String toString() {
    if(getType() == DiskObjectTypeEnum.FILE) {
      return getType().toString() + "(#" + getFileNumber() + ")[" + getLength() + "]";
    }
    else {
      return getType().toString() + "[" + getLength() + "]";
    }
  }
  
  @Override
  protected Object clone() throws CloneNotSupportedException {
    DiskObject clone = new DiskObject(getType(), getLength(), getFileNumber());
    return clone;
  }

  @Override
  public boolean equals(Object obj) {
    if(obj instanceof DiskObject) {
      DiskObject do2 = (DiskObject)obj;
      return ((do2.getFileNumber() == getFileNumber()) && (do2.getLength() == getLength()) && (do2.getType() == getType())); 
    }
    return false;
  }
 
}
