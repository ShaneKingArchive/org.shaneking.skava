package test.skava.sql.parser;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.collect.Tuple;
import org.shaneking.skava.sql.parser.SensitiveItemsFinder;

import java.io.StringReader;
import java.util.Map;
import java.util.Set;

public class SensitiveItemsFinderTest {

  private static CCJSqlParserManager pm = new CCJSqlParserManager();

  @Test
  public void testGetItemList() throws Exception {

    String sql = "SELECT mt1.a as b FROM MY_TABLE1 as mt1, MY_TABLE2 as mt2, (SELECT mt3i.* FROM MY_TABLE3 as mt3i) as mt3 LEFT OUTER JOIN MY_TABLE4 as mt4 "
      + " WHERE mt2.ID = (SELECT MAX(mt.ID) FROM MY_TABLE5 as mt) AND mt3.ID2 IN (SELECT mt6.* FROM MY_TABLE6 as mt6)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    // now you should use a class that implements StatementVisitor to decide what to do
    // based on the kind of the statement, that is SELECT or INSERT etc. but here we are only
    // interested in SELECTS
    if (statement instanceof Select) {
      Select selectStatement = (Select) statement;
      SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
      Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(statement);
      System.out.println(itemMap);
//      Assert.assertEquals("{MT2=([MY_TABLE2],{}), MT1=([MY_TABLE1],{A=[(MY_TABLE1,A,1,false)], B=[(MY_TABLE1,A,1,false)]}), MT4=([MY_TABLE4],{}), MT3=([MY_TABLE3],{*=[(MY_TABLE3,*,2,false)]})}", itemMap.toString());
      Assert.assertEquals("{MT2=([MY_TABLE2],{}), MT1=([MY_TABLE1],{A=[(MY_TABLE1,A,Select→SelectExpressionItem,false)], B=[(MY_TABLE1,A,Select→SelectExpressionItem,false)]}), MT4=([MY_TABLE4],{}), MT3=([MY_TABLE3],{*=[(MY_TABLE3,*,Select→SubSelect,false)]})}", itemMap.toString());
//      assertEquals(4, selectItemsList.size());
//      int i = 1;
//      for (Iterator iter = selectItemsList.iterator(); iter.hasNext(); i++) {
//        String tableName = (String) iter.next();
//        assertEquals("MY_TABLE" + i, tableName);
//      }
    }

  }

  @Test
  public void testGetItemListWithAddExpr() throws Exception {

    String sql = "SELECT mt1.a + mt1.c as b,mt2.d FROM MY_TABLE1 as mt1, MY_TABLE2 as mt2, (SELECT mt3i.* FROM MY_TABLE3 as mt3i) as mt3 LEFT OUTER JOIN MY_TABLE4 as mt4 "
      + " WHERE mt2.ID = (SELECT MAX(mt.ID) FROM MY_TABLE5 as mt) AND mt3.ID2 IN (SELECT mt6.* FROM MY_TABLE6 as mt6)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    if (statement instanceof Select) {
      SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
      Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(statement);
      System.out.println(itemMap);
//      Assert.assertEquals("{MT2=([MY_TABLE2],{D=[(MY_TABLE2,D,1,false)]}), MT1=([MY_TABLE1],{A=[(MY_TABLE1,A,1,true)], B=[(MY_TABLE1,C,1,true), (MY_TABLE1,A,1,true)], C=[(MY_TABLE1,C,1,true)]}), MT4=([MY_TABLE4],{}), MT3=([MY_TABLE3],{*=[(MY_TABLE3,*,2,false)]})}", itemMap.toString());
      Assert.assertEquals("{MT2=([MY_TABLE2],{D=[(MY_TABLE2,D,Select→SelectExpressionItem,false)]}), MT1=([MY_TABLE1],{A=[(MY_TABLE1,A,Select→SelectExpressionItem,true)], B=[(MY_TABLE1,A,Select→SelectExpressionItem,true), (MY_TABLE1,C,Select→SelectExpressionItem,true)], C=[(MY_TABLE1,C,Select→SelectExpressionItem,true)]}), MT4=([MY_TABLE4],{}), MT3=([MY_TABLE3],{*=[(MY_TABLE3,*,Select→SubSelect,false)]})}", itemMap.toString());
    }

  }

  @Test
  public void testGetItemListWithoutAlias() throws Exception {
    String sql = "SELECT MY_TABLE1.* FROM MY_TABLE1";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(selectStatement);
    System.out.println(itemMap);
//    Assert.assertEquals("{MY_TABLE1=([MY_TABLE1],{*=[(MY_TABLE1,*,1,false)]})}", itemMap.toString());
    Assert.assertEquals("{MY_TABLE1=([MY_TABLE1],{*=[(MY_TABLE1,*,Select,false)]})}", itemMap.toString());
//    assertEquals(1, tableList.size());
//    assertEquals("MY_TABLE1", (String) tableList.get(0));
  }

  @Test
  public void testGetItemListWithAlias() throws Exception {
    String sql = "SELECT ALIAS_TABLE1.* FROM MY_TABLE1 as ALIAS_TABLE1";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(selectStatement);
    System.out.println(itemMap);
//    Assert.assertEquals("{ALIAS_TABLE1=([MY_TABLE1],{*=[(MY_TABLE1,*,1,false)]})}", itemMap.toString());
    Assert.assertEquals("{ALIAS_TABLE1=([MY_TABLE1],{*=[(MY_TABLE1,*,Select,false)]})}", itemMap.toString());
//    assertEquals(1, tableList.size());
//    assertEquals("MY_TABLE1", (String) tableList.get(0));
  }

  //net.sf.jsqlparser.util.TableNamesFinder
  @Test
  public void testGetItemListWithItem() throws Exception {
    String sql = "WITH TESTWITH as (SELECT ALIAS_TABLE1.* FROM MY_TABLE1 as ALIAS_TABLE1) SELECT TESTWITH.* FROM TESTWITH";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(selectStatement);
    System.out.println(itemMap);//{TESTWITH=([MY_TABLE1],{*=[(MY_TABLE1,*,1,false)]})}
//    Assert.assertEquals("{TESTWITH=([MY_TABLE1],{*=[(MY_TABLE1,*,1,false)]})}", itemMap.toString());
    Assert.assertEquals("{TESTWITH=([MY_TABLE1],{*=[(MY_TABLE1,*,Select,false)]})}", itemMap.toString());
//    assertEquals(1, tableList.size());
//    assertEquals("MY_TABLE1", (String) tableList.get(0));
  }

  //net.sf.jsqlparser.util.TableNamesFinder
  @Test
  public void testGetItemListWithStmt() throws Exception {
    String sql = "WITH TESTWITH as (SELECT ALIAS_TABLE1.* FROM MY_TABLE1 as ALIAS_TABLE1) SELECT T.* FROM TESTWITH AS T";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(selectStatement);
    System.out.println(itemMap);//{TESTWITH=([MY_TABLE1],{*=[(MY_TABLE1,*,2,false)]}), T=([MY_TABLE1],{*=[(MY_TABLE1,*,1,false)]})}
//    Assert.assertEquals("{TESTWITH=([MY_TABLE1],{*=[(MY_TABLE1,*,2,false)]}), T=([MY_TABLE1],{*=[(MY_TABLE1,*,1,false)]})}", itemMap.toString());
    Assert.assertEquals("{T=([MY_TABLE1],{*=[(MY_TABLE1,*,Select,false)]})}", itemMap.toString());
//    assertEquals(1, tableList.size());
//    assertEquals("MY_TABLE1", (String) tableList.get(0));
  }

  @Test
  public void testGetItemListWithLateral() throws Exception {
    String sql = "SELECT al.* FROM MY_TABLE1 as mt1, LATERAL(select mt2.a from MY_TABLE2 mt2) as AL";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(selectStatement);
    System.out.println(itemMap);
//    Assert.assertEquals("{MT1=([MY_TABLE1],{}), AL=([MY_TABLE2],{A=[(MY_TABLE2,A,2,false)], *=[(MY_TABLE2,A,1,false)]})}", itemMap.toString());
    Assert.assertEquals("{MT1=([MY_TABLE1],{}), AL=([MY_TABLE2],{A=[(MY_TABLE2,A,Select→SubSelect→SelectExpressionItem,false)], *=[(MY_TABLE2,A,Select→SubSelect→SelectExpressionItem,false)]})}", itemMap.toString());
//    assertEquals(2, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
//    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemListFromDelete() throws Exception {
    String sql = "DELETE FROM MY_TABLE1 as AL WHERE a = (SELECT a from MY_TABLE2)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Delete deleteStatement = (Delete) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(statement);
    System.out.println(itemMap);
//    assertEquals(2, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
//    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemListFromDelete2() throws Exception {
    String sql = "DELETE FROM MY_TABLE1";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Delete deleteStatement = (Delete) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(statement);
    System.out.println(itemMap);
//    assertEquals(1, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemListFromDeleteWithJoin() throws Exception {
    String sql = "DELETE t1, t2 FROM MY_TABLE1 t1 JOIN MY_TABLE2 t2 ON t1.id = t2.id";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Delete deleteStatement = (Delete) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(statement);
    System.out.println(itemMap);
//    assertEquals(2, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
//    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemListFromInsert() throws Exception {
    String sql = "INSERT INTO MY_TABLE1 (a) VALUES ((SELECT a from MY_TABLE2 WHERE a = 1))";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Insert insertStatement = (Insert) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(statement);
    System.out.println(itemMap);
//    assertEquals(2, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
//    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemListFromInsertValues() throws Exception {
    String sql = "INSERT INTO MY_TABLE1 (a) VALUES (5)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Insert insertStatement = (Insert) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(statement);
    System.out.println(itemMap);
//    assertEquals(1, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemListFromReplace() throws Exception {
    String sql = "REPLACE INTO MY_TABLE1 (a) VALUES ((SELECT a from MY_TABLE2 WHERE a = 1))";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Replace replaceStatement = (Replace) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(statement);
    System.out.println(itemMap);
//    assertEquals(2, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
//    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemListFromUpdate() throws Exception {
    String sql = "UPDATE MY_TABLE1 SET a = (SELECT a from MY_TABLE2 WHERE a = 1)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Update updateStatement = (Update) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(statement);
    System.out.println(itemMap);
//    assertEquals(2, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
//    assertTrue(tableList.contains("MY_TABLE2"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemListFromUpdate2() throws Exception {
    String sql = "UPDATE MY_TABLE1 SET a = 5 WHERE 0 < (SELECT COUNT(b) FROM MY_TABLE3)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Update updateStatement = (Update) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(statement);
    System.out.println(itemMap);
//    assertEquals(2, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
//    assertTrue(tableList.contains("MY_TABLE3"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemListFromUpdate3() throws Exception {
    String sql = "UPDATE MY_TABLE1 SET a = 5 FROM MY_TABLE1 INNER JOIN MY_TABLE2 on MY_TABLE1.C = MY_TABLE2.D WHERE 0 < (SELECT COUNT(b) FROM MY_TABLE3)";
    net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));

    Update updateStatement = (Update) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, String, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(statement);
    System.out.println(itemMap);
//    assertEquals(4, tableList.size());
//    assertTrue(tableList.contains("MY_TABLE1"));
//    assertTrue(tableList.contains("MY_TABLE2"));
//    assertTrue(tableList.contains("MY_TABLE3"));
  }

}
