import java.sql.*;

public class Utils {
  private static String PROTOCOL = "jdbc:db2:";
  private static String DATABASE = "cs348";

  public enum PubType {PROCEEDINGS, JOURNAL, BOOK, ARTICLE};

  // Connection Utility function

  public static Connection connect() {
    Connection conn = null;
    try {
      DriverManager.registerDriver(new COM.ibm.db2.jdbc.app.DB2Driver());
      // establish connection to database
      String url = PROTOCOL + DATABASE;
      conn = DriverManager.getConnection(url);
      // System.out.println("Connected to DB2");
    } catch (Exception e) {
      System.err.println("Error connecting to database.");
      System.err.println(e.toString());
      System.exit(1);
    }
    return conn;
  }


  //----------------------//
  //   Utility Functions  //
  //----------------------//

  public static PubType determineType(Connection conn, String pubid)
      throws SQLException {
    Statement s = conn.createStatement();
    ResultSet r;
    // Check if article
    r = s.executeQuery("SELECT * FROM article WHERE pubid = '" + pubid + "'");
    if (r.next()) {
      return PubType.ARTICLE;
    }
    // Check if proceeding
    r = s.executeQuery("SELECT * FROM proceedings WHERE pubid = '" + pubid + "'");
    if (r.next()) {
      return PubType.PROCEEDINGS;
    }
    // Check if journal
    r = s.executeQuery("SELECT * FROM journal WHERE pubid = '" + pubid + "'");
    if (r.next()) {
      return PubType.JOURNAL;
    }
    // Check if book
    r = s.executeQuery("SELECT * FROM book WHERE pubid = '" + pubid + "'");
    if (r.next()) {
      return PubType.BOOK;
    }
    // Pubid not found
    System.out.println("Pubid not found!");
    System.exit(1);
    return null;
  }

  public static int getYear(Connection conn, String pubid, PubType type)
      throws SQLException {
    // Get the publication year given pubid
    String query = "";
    Statement s = conn.createStatement();
    switch (type) {
    case ARTICLE:
      // Recurse for containing publication
      ResultSet r = s.executeQuery("SELECT appearsin FROM article "
                                 + "WHERE pubid = '" + pubid + "'");
      r.next();
      String container = r.getString(1).trim();
      return getYear(conn, container, determineType(conn, container));
    case JOURNAL:
      query = "SELECT year FROM journal WHERE pubid = '" + pubid + "'"; break;
    case PROCEEDINGS:
      query = "SELECT year FROM proceedings WHERE pubid = '" + pubid + "'";
      break;
    case BOOK:
      query = "SELECT year FROM book WHERE pubid = '" + pubid + "'"; break;
    }
    // Return the year
    ResultSet r = s.executeQuery(query);
    r.next();
    return r.getInt(1);
  }


  public static String getTitle(Connection conn, String pubid)
      throws SQLException {
    Statement s = conn.createStatement();
    ResultSet r = s.executeQuery("SELECT title FROM publication "
                               + "WHERE pubid = '" + pubid + "'");
    r.next();
    return r.getString(1).trim();
  }

  //---------------------//
  //   Printing Methods  //
  //---------------------//

  public static void print(Connection conn, Publication pub) throws SQLException {
    // Print pubid
    System.out.println("Pubid: " + pub.getPubid());
    PubType type = determineType(conn, pub.getPubid());

    // Print type
    System.out.print("Type: ");
    switch(pub.getType()) {
    case PROCEEDINGS:
      System.out.println("proceedings"); break;
    case BOOK:
      System.out.println("book"); break;
    case JOURNAL:
      System.out.println("journal"); break;
    case ARTICLE:
      System.out.println("article"); break;
    }

    // Print Authors
    printAuthorList(conn, pub);

    // Print Title
    System.out.println("Title: " + pub.getTitle());

    // Print additional data based on type
    printAdditionalData(conn, pub);
  }

  public static void printAuthorList(Connection conn, Publication pub) {
    System.out.print("Authors: ");
    for (int i = 0; i < pub.getNumAuthors(); i++) {
      if (i != 0) {
        System.out.print(", ");
      }
      System.out.print(pub.getAuthor(i));
    }
    System.out.println();
  }

  public static void printAdditionalData(Connection conn, Publication pub)
      throws SQLException {
    Statement s = conn.createStatement();
    ResultSet r;
    switch(pub.getType()) {
    case PROCEEDINGS:
      System.out.println("Year: " + pub.getYear());
      break;
    case BOOK:
      r = s.executeQuery("SELECT publisher FROM book WHERE "
                       + "pubid = '" + pub.getPubid() + "'");
      r.next();
      System.out.println("Publisher: " + r.getString(1));
      System.out.println("Year: " + pub.getYear());
      break;
    case JOURNAL:
      r = s.executeQuery("SELECT volume, number FROM journal WHERE "
                       + "pubid = '" + pub.getPubid() + "'");
      r.next();
      System.out.println("Volume: " + r.getString(1));
      System.out.println("Number: " + r.getString(2));
      System.out.println("Year: " + pub.getYear());
      break;
    case ARTICLE:
      r = s.executeQuery("SELECT appearsin, startpage, endpage FROM article WHERE "
                       + "pubid = '" + pub.getPubid() + "'");
      r.next();
      System.out.println("In: " + r.getString(1));
      System.out.println("Pages: " + r.getInt(2) + "--" + r.getInt(3));
      break;
    }
  }

  public static void printAuthor(Connection conn, String aid) throws SQLException {
    Statement s = conn.createStatement();
    ResultSet rs = s.executeQuery("select * from author where aid=" + aid);
    if (rs.next()) {
      System.out.println("Aid: " + aid);
      System.out.println("Name: " + rs.getString("name"));
      String url = rs.getString("url");
      if (url == null) {
        url = "";
      }
      System.out.println("Url: " + url);
    } else {
      throw new RuntimeException("No such author");
    }
  }
}
