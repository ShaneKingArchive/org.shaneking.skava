package test.skava.sql.parser;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Test;
import org.shaneking.skava.ling.collect.Tuple;
import org.shaneking.skava.sql.parser.TablesNamesFinder;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TablesNamesFinderTest {

  private static CCJSqlParserManager pm = new CCJSqlParserManager();

  @Test
  public void testGetTableList() throws Exception {

    String sql = "SELECT * FROM MY_TABLE1, MY_TABLE2, (SELECT * FROM MY_TABLE3) LEFT OUTER JOIN MY_TABLE4 "
      + " WHERE ID = (SELECT MAX(ID) FROM MY_TABLE5) AND ID2 IN (SELECT * FROM MY_TABLE6)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    // now you should use a class that implements StatementVisitor to decide what to do
    // based on the kind of the statement, that is SELECT or INSERT etc. but here we are only
    // interested in SELECTS
    if (statement instanceof Select) {
      Select selectStatement = (Select) statement;
      TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
      List<String> tableList = Lists.transform(tablesNamesFinder.getTableList(selectStatement), new Function<Tuple.Pair<String, String>, String>() {
        @Override
        public String apply(Tuple.@Nullable Pair<String, String> objects) {
          return Tuple.getFirst(objects);
        }
      });
      assertEquals(6, tableList.size());
      int i = 1;
      for (Iterator iter = tableList.iterator(); iter.hasNext(); i++) {
        String tableName = (String) iter.next();
        assertEquals("MY_TABLE" + i, tableName);
      }
    }

  }

  @Test
  public void testGetTableListWithAlias() throws Exception {
    String sql = "SELECT * FROM MY_SCHEMA.MY_TABLE1 as ALIAS_TABLE1";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
    List<String> tableList = Lists.transform(tablesNamesFinder.getTableList(selectStatement), new Function<Tuple.Pair<String, String>, String>() {
      @Override
      public String apply(Tuple.@Nullable Pair<String, String> objects) {
        return Tuple.getFirst(objects);
      }
    });
    assertEquals(1, tableList.size());
    assertEquals("MY_SCHEMA.MY_TABLE1", (String) tableList.get(0));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetTableListWithStmt() throws Exception {
    String sql = "WITH TESTSTMT as (SELECT * FROM MY_TABLE1 as ALIAS_TABLE1) SELECT * FROM TESTSTMT";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
    List<String> tableList = Lists.transform(tablesNamesFinder.getTableList(selectStatement), new Function<Tuple.Pair<String, String>, String>() {
      @Override
      public String apply(Tuple.@Nullable Pair<String, String> objects) {
        return Tuple.getFirst(objects);
      }
    });
    assertEquals(1, tableList.size());
    assertEquals("MY_TABLE1", (String) tableList.get(0));
  }


  @Test
  public void testGetTableListWithStmtOld() throws Exception {
    String sql = "WITH TESTSTMT as (SELECT * FROM MY_TABLE1 as ALIAS_TABLE1) SELECT * FROM TESTSTMT";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    net.sf.jsqlparser.util.TablesNamesFinder tablesNamesFinder = new net.sf.jsqlparser.util.TablesNamesFinder();
    List<String> tableList = tablesNamesFinder.getTableList(selectStatement);
    assertEquals(1, tableList.size());
    assertEquals("MY_TABLE1", (String) tableList.get(0));
  }

  @Test
  public void testGetTableListWithLateral() throws Exception {
    String sql = "SELECT * FROM MY_TABLE1, LATERAL(select a from MY_TABLE2) as AL";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
    List<String> tableList = Lists.transform(tablesNamesFinder.getTableList(selectStatement), new Function<Tuple.Pair<String, String>, String>() {
      @Override
      public String apply(Tuple.@Nullable Pair<String, String> objects) {
        return Tuple.getFirst(objects);
      }
    });
    assertEquals(2, tableList.size());
    assertTrue(tableList.contains("MY_TABLE1"));
    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test
  public void testGetTableListFromDelete() throws Exception {
    String sql = "DELETE FROM MY_TABLE1 as AL WHERE a = (SELECT a from MY_TABLE2)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Delete deleteStatement = (Delete) statement;
    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
    List<String> tableList = Lists.transform(tablesNamesFinder.getTableList(deleteStatement), new Function<Tuple.Pair<String, String>, String>() {
      @Override
      public String apply(Tuple.@Nullable Pair<String, String> objects) {
        return Tuple.getFirst(objects);
      }
    });
    assertEquals(2, tableList.size());
    assertTrue(tableList.contains("MY_TABLE1"));
    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test
  public void testGetTableListFromDelete2() throws Exception {
    String sql = "DELETE FROM MY_TABLE1";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Delete deleteStatement = (Delete) statement;
    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
    List<String> tableList = Lists.transform(tablesNamesFinder.getTableList(deleteStatement), new Function<Tuple.Pair<String, String>, String>() {
      @Override
      public String apply(Tuple.@Nullable Pair<String, String> objects) {
        return Tuple.getFirst(objects);
      }
    });
    assertEquals(1, tableList.size());
    assertTrue(tableList.contains("MY_TABLE1"));
  }

  @Test
  public void testGetTableListFromDeleteWithJoin() throws Exception {
    String sql = "DELETE t1, t2 FROM MY_TABLE1 t1 JOIN MY_TABLE2 t2 ON t1.id = t2.id";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Delete deleteStatement = (Delete) statement;
    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
    List<String> tableList = Lists.transform(tablesNamesFinder.getTableList(deleteStatement), new Function<Tuple.Pair<String, String>, String>() {
      @Override
      public String apply(Tuple.@Nullable Pair<String, String> objects) {
        return Tuple.getFirst(objects);
      }
    });
    assertEquals(2, tableList.size());
    assertTrue(tableList.contains("MY_TABLE1"));
    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test
  public void testGetTableListFromInsert() throws Exception {
    String sql = "INSERT INTO MY_TABLE1 (a) VALUES ((SELECT a from MY_TABLE2 WHERE a = 1))";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Insert insertStatement = (Insert) statement;
    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
    List<String> tableList = Lists.transform(tablesNamesFinder.getTableList(insertStatement), new Function<Tuple.Pair<String, String>, String>() {
      @Override
      public String apply(Tuple.@Nullable Pair<String, String> objects) {
        return Tuple.getFirst(objects);
      }
    });
    assertEquals(2, tableList.size());
    assertTrue(tableList.contains("MY_TABLE1"));
    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test
  public void testGetTableListFromInsertValues() throws Exception {
    String sql = "INSERT INTO MY_TABLE1 (a) VALUES (5)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Insert insertStatement = (Insert) statement;
    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
    List<String> tableList = Lists.transform(tablesNamesFinder.getTableList(insertStatement), new Function<Tuple.Pair<String, String>, String>() {
      @Override
      public String apply(Tuple.@Nullable Pair<String, String> objects) {
        return Tuple.getFirst(objects);
      }
    });
    assertEquals(1, tableList.size());
    assertTrue(tableList.contains("MY_TABLE1"));
  }

  @Test
  public void testGetTableListFromReplace() throws Exception {
    String sql = "REPLACE INTO MY_TABLE1 (a) VALUES ((SELECT a from MY_TABLE2 WHERE a = 1))";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Replace replaceStatement = (Replace) statement;
    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
    List<String> tableList = Lists.transform(tablesNamesFinder.getTableList(replaceStatement), new Function<Tuple.Pair<String, String>, String>() {
      @Override
      public String apply(Tuple.@Nullable Pair<String, String> objects) {
        return Tuple.getFirst(objects);
      }
    });
    assertEquals(2, tableList.size());
    assertTrue(tableList.contains("MY_TABLE1"));
    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test
  public void testGetTableListFromUpdate() throws Exception {
    String sql = "UPDATE MY_TABLE1 SET a = (SELECT a from MY_TABLE2 WHERE a = 1)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Update updateStatement = (Update) statement;
    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
    List<String> tableList = Lists.transform(tablesNamesFinder.getTableList(updateStatement), new Function<Tuple.Pair<String, String>, String>() {
      @Override
      public String apply(Tuple.@Nullable Pair<String, String> objects) {
        return Tuple.getFirst(objects);
      }
    });
    assertEquals(2, tableList.size());
    assertTrue(tableList.contains("MY_TABLE1"));
    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test
  public void testGetTableListFromUpdate2() throws Exception {
    String sql = "UPDATE MY_TABLE1 SET a = 5 WHERE 0 < (SELECT COUNT(b) FROM MY_TABLE3)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Update updateStatement = (Update) statement;
    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
    List<String> tableList = Lists.transform(tablesNamesFinder.getTableList(updateStatement), new Function<Tuple.Pair<String, String>, String>() {
      @Override
      public String apply(Tuple.@Nullable Pair<String, String> objects) {
        return Tuple.getFirst(objects);
      }
    });
    assertEquals(2, tableList.size());
    assertTrue(tableList.contains("MY_TABLE1"));
    assertTrue(tableList.contains("MY_TABLE3"));
  }

  @Test
  public void testGetTableListFromUpdate3() throws Exception {
    String sql = "UPDATE MY_TABLE1 SET a = 5 FROM MY_TABLE1 INNER JOIN MY_TABLE2 on MY_TABLE1.C = MY_TABLE2.D WHERE 0 < (SELECT COUNT(b) FROM MY_TABLE3)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Update updateStatement = (Update) statement;
    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
    List<String> tableList = Lists.transform(tablesNamesFinder.getTableList(updateStatement), new Function<Tuple.Pair<String, String>, String>() {
      @Override
      public String apply(Tuple.@Nullable Pair<String, String> objects) {
        return Tuple.getFirst(objects);
      }
    });
    assertEquals(4, tableList.size());
    assertTrue(tableList.contains("MY_TABLE1"));
    assertTrue(tableList.contains("MY_TABLE2"));
    assertTrue(tableList.contains("MY_TABLE3"));
  }
}
