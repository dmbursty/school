import java.sql.*;

public class bibcontent {

  public static void main(String[] args) throws SQLException {
    if (args.length < 1)  {
      System.out.println("Please specify a publication id");
      System.exit(1);
    }

    Connection conn = Utils.connect();
    Statement s = conn.createStatement();
    ResultSet rs = s.executeQuery("select * from article where appearsin=\'" + args[0] + "\' order by startpage");

    boolean hasResults = rs.next();
    if (!hasResults) {
      System.out.println("No articles for the publication id was found!");
    } else {
      do {
        Publication art = new Publication();
        String pubid = rs.getString("pubid");
        art.setPubid(pubid);
        art.setTitle(Utils.getTitle(conn, pubid));
        art.setYear(Utils.getYear(conn, pubid, Utils.PubType.ARTICLE));
        art.setType(Utils.PubType.ARTICLE);

        Statement ss = conn.createStatement();
        ResultSet ars = ss.executeQuery("SELECT w.aorder, a.name "
                                    + "FROM wrote w, author a "
                                    + "WHERE w.pubid = '" + pubid
                                    + "' AND a.aid = w.aid ");
        while (ars.next()) {
          art.addAuthor(Integer.parseInt(ars.getString("aorder")), ars.getString("name").trim());
        }

        Utils.print(conn, art);
        System.out.println();
      } while (rs.next());
    }

    s.close();
    conn.close();
  }
}

