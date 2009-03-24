package a3;

public class Clipboard {
  private static Clipboard instance;
  private MarkerSet item;
  
  private Clipboard() {
    item = null;
  }
  
  public static Clipboard getInstance() {
    if (instance == null) {
      instance = new Clipboard();
    }
    return instance;
  }
  
  public void setItem(MarkerSet mset) {
    item = mset;
  }
  
  public MarkerSet getItem() {
    return item;
  }
}
