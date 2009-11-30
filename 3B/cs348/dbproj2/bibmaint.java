import java.sql.*;
import java.util.*;
import java.io.*;

public class bibmaint {

  public static void main(String[] args) throws SQLException {
    Connection conn = Utils.connect();
    Statement s = conn.createStatement();
    Scanner lines = new Scanner(System.in);

    while (lines.hasNext()) {
      String line = lines.nextLine();
      Scanner in = new Scanner(line).useDelimiter("#|\\(|\\)");
      String type = in.next();

      if (type.equals("author")) {
        String aid = in.next();
        String name = in.next();

        boolean exists = s.executeQuery("select * from author where aid=" + aid).next();
        if (!exists) {
          s.executeUpdate("insert into author (aid, name) values ("+ aid + ",\'" + name +"\')");
        } else {
          s.executeUpdate("update author set name=\'" + name + "\' where aid=" + aid + "");
        }

        Utils.printAuthor(conn, aid);
      } else if (type.equals("authorurl")) {
        String aid = in.next();
        String url = in.next();

        s.executeUpdate("update author set url=\'" + url + "\' where aid=" + aid + "");
        Utils.printAuthor(conn, aid);

      } else if (type.equals("book")) {
        String pubid = in.next();
        String title = in.next();
        String[] aids = in.next().split(";");
        String publisher = in.next();
        String year = in.next();

        boolean exists = s.executeQuery("select * from publication where pubid=\'" + pubid + "\'").next();
        if (!exists) {
          s.executeUpdate("insert into publication (pubid, title) values " +
              "(\'" + pubid + "\', \'" + title + "\')");
          s.executeUpdate("insert into book (pubid, publisher, year) values " +
              "(\'" + pubid + "\', \'" + publisher + "\', " + year + ")");
          for (int i = 0; i < aids.length; i++) {
            s.executeUpdate("insert into wrote (aid, pubid, aorder) values"
                + "(" + aids[i] + ", \'" + pubid + "\', " + i + ")");
          }
        } else {
          s.executeUpdate("update book set publisher=\'" + publisher + "\'," 
              + "year=" + year
              + " where pubid=\'" + pubid + "\'");
          s.executeUpdate("update publication set title=\'" + title + "\'" 
              + " where pubid=\'" + pubid + "\'");
          s.executeUpdate("delete from wrote"
              + " where pubid=\'" + pubid + "\'");
          for (int i = 0; i < aids.length; i++) {
            s.executeUpdate("insert into wrote (aid, pubid, aorder) values"
                + "(" + aids[i] + ", \'" + pubid + "\', " + i + ")");
          }
        }

        Publication book = new Publication();
        book.setPubid(pubid);
        book.setTitle(Utils.getTitle(conn, pubid));
        book.setYear(Utils.getYear(conn, pubid, Utils.PubType.BOOK));
        book.setType(Utils.PubType.BOOK);

        Statement ss = conn.createStatement();
        ResultSet ars = ss.executeQuery("SELECT w.aorder, a.name "
                                    + "FROM wrote w, author a "
                                    + "WHERE w.pubid = '" + pubid
                                    + "' AND a.aid = w.aid ");
        while (ars.next()) {
          book.addAuthor(Integer.parseInt(ars.getString("aorder")), ars.getString("name").trim());
        }
        Utils.print(conn, book);

      } else if (type.equals("journal")) {
        String pubid = in.next();
        String title = in.next();
        String volume = in.next();
        String number = in.next();
        String year = in.next();

        boolean exists = s.executeQuery("select * from publication where pubid=\'" + pubid + "\'").next();
        if (!exists) {
          s.executeUpdate("insert into publication (pubid, title) values " +
              "(\'" + pubid + "\', \'" + title + "\')");
          s.executeUpdate("insert into journal (pubid, volume, number, year) values " +
              "(\'" + pubid + "\', " + volume + ", " + number + ", " + year + ")");
        } else {
          s.executeUpdate("update journal set volume=" + volume + ","
              + "number=" + number + ", "
              + "year=" + year
              + " where pubid=\'" + pubid + "\'");
          s.executeUpdate("update publication set title=\'" + title + "\'" 
              + " where pubid=\'" + pubid + "\'");
        }

        Publication jour = new Publication();
        jour.setPubid(pubid);
        jour.setTitle(Utils.getTitle(conn, pubid));
        jour.setYear(Utils.getYear(conn, pubid, Utils.PubType.JOURNAL));
        jour.setType(Utils.PubType.JOURNAL);

        Statement ss = conn.createStatement();
        ResultSet ars = ss.executeQuery("SELECT w.aorder, a.name "
                                    + "FROM wrote w, author a "
                                    + "WHERE w.pubid = '" + pubid
                                    + "' AND a.aid = w.aid ");
        while (ars.next()) {
          jour.addAuthor(Integer.parseInt(ars.getString("aorder")), ars.getString("name").trim());
        }
        Utils.print(conn, jour);

      } else if (type.equals("proceedings")) {
        String pubid = in.next();
        String title = in.next();
        String year = in.next();

        boolean exists = s.executeQuery("select * from publication where pubid=\'" + pubid + "\'").next();
        if (!exists) {
          s.executeUpdate("insert into publication (pubid, title) values " +
              "(\'" + pubid + "\', \'" + title + "\')");
          s.executeUpdate("insert into proceedings (pubid, year) values " +
              "(\'" + pubid + "\', " + year + ")");
        } else {
          s.executeUpdate("update proceedings set "
              + "year=" + year
              + " where pubid=\'" + pubid + "\'");
          s.executeUpdate("update publication set title=\'" + title + "\'" 
              + " where pubid=\'" + pubid + "\'");
        }

        Publication proc = new Publication();
        proc.setPubid(pubid);
        proc.setTitle(Utils.getTitle(conn, pubid));
        proc.setYear(Utils.getYear(conn, pubid, Utils.PubType.PROCEEDINGS));
        proc.setType(Utils.PubType.PROCEEDINGS);

        Statement ss = conn.createStatement();
        ResultSet ars = ss.executeQuery("SELECT w.aorder, a.name "
                                    + "FROM wrote w, author a "
                                    + "WHERE w.pubid = '" + pubid
                                    + "' AND a.aid = w.aid ");
        while (ars.next()) {
          proc.addAuthor(Integer.parseInt(ars.getString("aorder")), ars.getString("name").trim());
        }
        Utils.print(conn, proc);

      } else if (type.equals("article")) {
        String pubid = in.next();
        String title = in.next();
        String[] aids = in.next().split(";");
        String appearsin = in.next();
        String startpage = in.next();
        String endpage = in.next();

        boolean exists = s.executeQuery("select * from publication where pubid=\'" + pubid + "\'").next();
        if (!exists) {
          s.executeUpdate("insert into publication (pubid, title) values " +
              "(\'" + pubid + "\', \'" + title + "\')");
          s.executeUpdate("insert into article (pubid, appearsin, startpage, endpage) values " +
              "(\'" + pubid + "\', \'" + appearsin + "\', " + startpage + "," + endpage + ")");
          for (int i = 0; i < aids.length; i++) {
            s.executeUpdate("insert into wrote (aid, pubid, aorder) values"
                + "(" + aids[i] + ", \'" + pubid + "\', " + i + ")");
          }
        } else {
          s.executeUpdate("update article set appearsin=\'" + appearsin + "\'," 
              + " startpage=" + startpage + ","
              + " endpage=" + endpage
              + " where pubid=\'" + pubid + "\'");
          s.executeUpdate("update publication set title=\'" + title + "\'" 
              + " where pubid=\'" + pubid + "\'");
          s.executeUpdate("delete from wrote"
              + " where pubid=\'" + pubid + "\'");
          for (int i = 0; i < aids.length; i++) {
            s.executeUpdate("insert into wrote (aid, pubid, aorder) values"
                + "(" + aids[i] + ", \'" + pubid + "\', " + i + ")");
          }
        }

        Publication art = new Publication();
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

      }
      System.out.println();
    }
    
    s.close();
    conn.close();
  }

}

