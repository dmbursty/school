import java.util.*;

public class Publication {
  // Members
  private int year;
  private String pubid, title;
  private Utils.PubType type;
  public List<Author> authorList = new ArrayList<Author>();

  private class Author {
    public int aorder;
    public String name;
    public Author(int aorder, String name) {
      this.aorder = aorder;
      this.name = name;
    }
  }

  // Setters
  public void setPubid(String pubid) {
    this.pubid = pubid;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public void setType(Utils.PubType type) {
    this.type = type;
  }

  // Getters
  public String getPubid() {
    return pubid;
  }

  public String getTitle() {
    return title;
  }

  public int getYear() {
    return year;
  }

  public Utils.PubType getType() {
    return type;
  }

  // Author Management
  public void addAuthor(int aorder, String name) {
    authorList.add(new Author(aorder, name));
  }

  public int getNumAuthors() {
    return authorList.size();
  }

  public String getAuthor(int aorder) {
    return authorList.get(aorder).name;
  }

  public void sortAuthors() {
    Collections.sort(authorList, new Comparator<Author>() {
      public int compare(Author a1, Author a2) {
        return a1.aorder - a2.aorder;
      }
    });
  }
}
