import java.util.*;
import java.sql.*;

public class bibauthor {
  public static void main(String[] args) throws SQLException {
    List<Publication> pubList = new ArrayList<Publication>();
    Connection conn = Utils.connect();
    Statement s = conn.createStatement();
    ResultSet results;
    try {
      results = s.executeQuery("SELECT w.pubid FROM author a, wrote w "
                             + "WHERE a.name = '" + args[0] + "' "
                             + "AND a.aid = w.aid");
    } catch (IndexOutOfBoundsException e) {
      System.err.println("Error: No author specified");
      System.exit(1);
      return;
    }
    if (!results.next()) {
      System.err.println("Error: No publications found for author");
      System.exit(1);
    }
    do {
      // Create Publication object for easy sorting
      Publication pub = getPublication(conn, results.getString(1).trim());

      // If it's an article, we also need to extract the containing publication
      if (pub.getType() == Utils.PubType.ARTICLE) {
        Statement ss = conn.createStatement();
        ResultSet r = ss.executeQuery("SELECT appearsin FROM article "
                                    + "WHERE pubid = '" + pub.getPubid() + "'");
        r.next();
        Publication container = getPublication(conn, r.getString(1).trim());
        pubList.add(container);
        ss.close();
      }

      // Add publication to list
      pubList.add(pub);
    } while (results.next());

    // Sort Publications
    Collections.sort(pubList, new Comparator<Publication>() {
      public int compare(Publication p1, Publication p2) {
        if (p1.getNumAuthors() == 0) {
          return -1;
        }
        if (p2.getNumAuthors() == 0) {
          return 1;
        }
        if (p1.getAuthor(0).equals(p2.getAuthor(0))) {
          return p1.getYear() - p2.getYear();
        } else {
          return p1.getAuthor(0).compareTo(p2.getAuthor(0));
        }
      }
    });

    // Print Publications
    boolean firstResult = true;
    // Make sure we don't print duplicates
    Set<String> seen = new HashSet<String>();
    for (int i = 0; i < pubList.size(); i++) {
      if (seen.contains(pubList.get(i).getPubid())) {
        continue;
      }
      seen.add(pubList.get(i).getPubid());
      if (!firstResult) {
        System.out.println("");
      }
      firstResult = false;
      Utils.print(conn, pubList.get(i));
    }

    s.close();
    conn.close();
  }

  public static Publication getPublication(Connection conn, String pubid)
      throws SQLException {
    // Create Publication object
    Publication pub = new Publication();

    // Determine type of publication
    Utils.PubType type = Utils.determineType(conn, pubid);

    // Populate Publication Object
    pub.setPubid(pubid);
    pub.setType(type);
    pub.setTitle(Utils.getTitle(conn, pubid));
    pub.setYear(Utils.getYear(conn, pubid, type));

    // Add authors
    Statement ss = conn.createStatement();
    ResultSet res = ss.executeQuery("SELECT w.aorder, a.name "
                                  + "FROM wrote w, author a "
                                  + "WHERE w.pubid = '" + pubid
                                  + "' AND a.aid = w.aid "
                                  + "ORDER BY w.aorder");
    while (res.next()) {
      pub.addAuthor(res.getInt("aorder"), res.getString("name").trim());
    }
    pub.sortAuthors();
    res.close();
    return pub;
  }
}
