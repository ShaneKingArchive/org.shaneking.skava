package test.skava.sql.parser;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import org.junit.Test;
import org.shaneking.skava.ling.collect.Tuple;
import org.shaneking.skava.sql.parser.SelectItemsFinder;

import java.io.StringReader;
import java.util.Map;
import java.util.Set;

public class SelectItemsFinderTest {

  private static CCJSqlParserManager pm = new CCJSqlParserManager();

  @Test
  public void testGetSelectItemList() throws Exception {

    String sql = "SELECT mt1.a as b FROM MY_TABLE1 as mt1, MY_TABLE2 as mt2, (SELECT mt3i.* FROM MY_TABLE3 as mt3i) as mt3 LEFT OUTER JOIN MY_TABLE4 as mt4 "
      + " WHERE mt2.ID = (SELECT MAX(mt.ID) FROM MY_TABLE5 as mt) AND mt3.ID2 IN (SELECT mt6.* FROM MY_TABLE6 as mt6)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    // now you should use a class that implements StatementVisitor to decide what to do
    // based on the kind of the statement, that is SELECT or INSERT etc. but here we are only
    // interested in SELECTS
    if (statement instanceof Select) {
      Select selectStatement = (Select) statement;
      SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
      Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> selectItemsMap = selectItemsFinder.getSelectItemList(selectStatement);
      System.out.println(selectItemsMap);
//      assertEquals(4, selectItemsList.size());
//      int i = 1;
//      for (Iterator iter = selectItemsList.iterator(); iter.hasNext(); i++) {
//        String tableName = (String) iter.next();
//        assertEquals("MY_TABLE" + i, tableName);
//      }
    }

  }

  @Test
  public void testGetSelectItemListWithAddExpr() throws Exception {

    String sql = "SELECT mt1.a + mt1.c as b FROM MY_TABLE1 as mt1, MY_TABLE2 as mt2, (SELECT mt3i.* FROM MY_TABLE3 as mt3i) as mt3 LEFT OUTER JOIN MY_TABLE4 as mt4 "
      + " WHERE mt2.ID = (SELECT MAX(mt.ID) FROM MY_TABLE5 as mt) AND mt3.ID2 IN (SELECT mt6.* FROM MY_TABLE6 as mt6)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    if (statement instanceof Select) {
      SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
      Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> selectItemsMap = selectItemsFinder.getSelectItemList(statement);
      System.out.println(selectItemsMap);
    }

  }

  @Test
  public void testGetSelectItemListWithoutAlias() throws Exception {
    String sql = "SELECT MY_TABLE1.* FROM MY_TABLE1";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> selectItemsMap = selectItemsFinder.getSelectItemList(selectStatement);
    System.out.println(selectItemsMap);
//    assertEquals(1, tableList.size());
//    assertEquals("MY_TABLE1", (String) tableList.get(0));
  }

  @Test
  public void testGetSelectItemListWithAlias() throws Exception {
    String sql = "SELECT ALIAS_TABLE1.* FROM MY_TABLE1 as ALIAS_TABLE1";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> selectItemsMap = selectItemsFinder.getSelectItemList(selectStatement);
    System.out.println(selectItemsMap);
//    assertEquals(1, tableList.size());
//    assertEquals("MY_TABLE1", (String) tableList.get(0));
  }

  //net.sf.jsqlparser.util.TablesNamesFinder
  @Test
  public void testGetSelectItemListWithItem() throws Exception {
    String sql = "WITH TESTWITH as (SELECT ALIAS_TABLE1.* FROM MY_TABLE1 as ALIAS_TABLE1) SELECT TESTWITH.* FROM TESTWITH";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> selectItemsMap = selectItemsFinder.getSelectItemList(selectStatement);
    System.out.println(selectItemsMap);//{TESTWITH=([MY_TABLE1],{*=[(MY_TABLE1,*,1,false)]})}
//    assertEquals(1, tableList.size());
//    assertEquals("MY_TABLE1", (String) tableList.get(0));
  }

  //net.sf.jsqlparser.util.TablesNamesFinder
  @Test
  public void testGetSelectItemListWithStmt() throws Exception {
    String sql = "WITH TESTWITH as (SELECT ALIAS_TABLE1.* FROM MY_TABLE1 as ALIAS_TABLE1) SELECT T.* FROM TESTWITH AS T";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> selectItemsMap = selectItemsFinder.getSelectItemList(selectStatement);
    System.out.println(selectItemsMap);//{TESTWITH=([MY_TABLE1],{*=[(MY_TABLE1,*,2,false)]}), T=([MY_TABLE1],{*=[(MY_TABLE1,*,1,false)]})}
//    assertEquals(1, tableList.size());
//    assertEquals("MY_TABLE1", (String) tableList.get(0));
  }

  @Test
  public void testGetSelectItemListWithLateral() throws Exception {
    String sql = "SELECT al.* FROM MY_TABLE1 as mt1, LATERAL(select mt2.a from MY_TABLE2 mt2) as AL";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> selectItemsMap = selectItemsFinder.getSelectItemList(selectStatement);
    System.out.println(selectItemsMap);
//    assertEquals(2, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
//    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetSelectItemListFromDelete() throws Exception {
    String sql = "DELETE FROM MY_TABLE1 as AL WHERE a = (SELECT a from MY_TABLE2)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Delete deleteStatement = (Delete) statement;
    SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> selectItemsMap = selectItemsFinder.getSelectItemList(statement);
    System.out.println(selectItemsMap);
//    assertEquals(2, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
//    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetSelectItemListFromDelete2() throws Exception {
    String sql = "DELETE FROM MY_TABLE1";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Delete deleteStatement = (Delete) statement;
    SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> selectItemsMap = selectItemsFinder.getSelectItemList(statement);
    System.out.println(selectItemsMap);
//    assertEquals(1, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetSelectItemListFromDeleteWithJoin() throws Exception {
    String sql = "DELETE t1, t2 FROM MY_TABLE1 t1 JOIN MY_TABLE2 t2 ON t1.id = t2.id";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Delete deleteStatement = (Delete) statement;
    SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> selectItemsMap = selectItemsFinder.getSelectItemList(statement);
    System.out.println(selectItemsMap);
//    assertEquals(2, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
//    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetSelectItemListFromInsert() throws Exception {
    String sql = "INSERT INTO MY_TABLE1 (a) VALUES ((SELECT a from MY_TABLE2 WHERE a = 1))";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Insert insertStatement = (Insert) statement;
    SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> selectItemsMap = selectItemsFinder.getSelectItemList(statement);
    System.out.println(selectItemsMap);
//    assertEquals(2, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
//    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetSelectItemListFromInsertValues() throws Exception {
    String sql = "INSERT INTO MY_TABLE1 (a) VALUES (5)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Insert insertStatement = (Insert) statement;
    SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> selectItemsMap = selectItemsFinder.getSelectItemList(statement);
    System.out.println(selectItemsMap);
//    assertEquals(1, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetSelectItemListFromReplace() throws Exception {
    String sql = "REPLACE INTO MY_TABLE1 (a) VALUES ((SELECT a from MY_TABLE2 WHERE a = 1))";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Replace replaceStatement = (Replace) statement;
    SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> selectItemsMap = selectItemsFinder.getSelectItemList(statement);
    System.out.println(selectItemsMap);
//    assertEquals(2, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
//    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetSelectItemListFromUpdate() throws Exception {
    String sql = "UPDATE MY_TABLE1 SET a = (SELECT a from MY_TABLE2 WHERE a = 1)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Update updateStatement = (Update) statement;
    SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> selectItemsMap = selectItemsFinder.getSelectItemList(statement);
    System.out.println(selectItemsMap);
//    assertEquals(2, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
//    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetSelectItemListFromUpdate2() throws Exception {
    String sql = "UPDATE MY_TABLE1 SET a = 5 WHERE 0 < (SELECT COUNT(b) FROM MY_TABLE3)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Update updateStatement = (Update) statement;
    SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> selectItemsMap = selectItemsFinder.getSelectItemList(statement);
    System.out.println(selectItemsMap);
//    assertEquals(2, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
//    assertTrue(tableList.contains("MY_TABLE3"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetSelectItemListFromUpdate3() throws Exception {
    String sql = "UPDATE MY_TABLE1 SET a = 5 FROM MY_TABLE1 INNER JOIN MY_TABLE2 on MY_TABLE1.C = MY_TABLE2.D WHERE 0 < (SELECT COUNT(b) FROM MY_TABLE3)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Update updateStatement = (Update) statement;
    SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Integer, Boolean>>>>> selectItemsMap = selectItemsFinder.getSelectItemList(statement);
    System.out.println(selectItemsMap);
//    assertEquals(4, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
//    assertTrue(tableList.contains("MY_TABLE2"));
//    assertTrue(tableList.contains("MY_TABLE3"));
  }

}
