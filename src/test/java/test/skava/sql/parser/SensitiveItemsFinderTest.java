package test.skava.sql.parser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.OracleHint;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.upsert.Upsert;
import org.junit.Test;
import org.shaneking.skava.ling.collect.Tuple;
import org.shaneking.skava.sql.parser.SensitiveItemsFinder;

import java.io.StringReader;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SensitiveItemsFinderTest {

  private static CCJSqlParserManager pm = new CCJSqlParserManager();

  @Test
  public void skTestSetOperationList() throws Exception {
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(CCJSqlParserUtil.parse("select sub(t.a,1,3) from (select tab1.a from tab1 union select tab2.m from tab2) t"));
    assertEquals(1, itemMap.size());
    assertEquals("{T=([TAB2, TAB1],{A=[(TAB1,A,[Select→SubSelect→SelectExpressionItem, Select→SelectExpressionItem],true)], M=[(TAB2,M,[Select→SubSelect→SelectExpressionItem],false)]})}", itemMap.toString());
  }

  @Test
  public void testGetItemMap() throws Exception {

    String sql = "SELECT mt1.* FROM MY_TABLE1 mt1, MY_TABLE2, (SELECT imt3.* FROM MY_TABLE3 imt3) LEFT OUTER JOIN MY_TABLE4 "
      + " WHERE ID = (SELECT MAX(ID) FROM MY_TABLE5) AND ID2 IN (SELECT * FROM MY_TABLE6)";
    Statement statement = pm.parse(new StringReader(sql));

    // now you should use a class that implements StatementVisitor to decide what to do
    // based on the kind of the statement, that is SELECT or INSERT etc. but here we are only
    // interested in SELECTS
    if (statement instanceof Select) {
      Select selectStatement = (Select) statement;
      SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
      Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(selectStatement);
      assertEquals(4, itemMap.size());
      assertEquals("{MY_TABLE4=([MY_TABLE4],{}), *=([MY_TABLE3],{*=[(MY_TABLE3,*,[Select→SubSelect],false)]}), MT1=([MY_TABLE1],{*=[(MY_TABLE1,*,[Select],false)]}), MY_TABLE2=([MY_TABLE2],{})}", itemMap.toString());
    }

  }

  @Test
  public void testGetItemMapWithAlias() throws Exception {
    String sql = "SELECT ALIAS_TABLE1.* FROM MY_TABLE1 as ALIAS_TABLE1";
    Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(selectStatement);
    assertEquals(1, itemMap.size());
    assertEquals("{ALIAS_TABLE1=([MY_TABLE1],{*=[(MY_TABLE1,*,[Select],false)]})}", itemMap.toString());

  }

  @Test
  public void testGetItemMapWithStmt() throws Exception {
    String sql = "WITH TESTSTMT as (SELECT ALIAS_TABLE1.* FROM MY_TABLE1 as ALIAS_TABLE1) SELECT TESTSTMT.* FROM TESTSTMT";
    Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(selectStatement);
    assertEquals(1, itemMap.size());
    assertEquals("{TESTSTMT=([MY_TABLE1],{*=[(MY_TABLE1,*,[Select, Select→WithItem],false)]})}", itemMap.toString());
  }

  @Test
  public void testGetItemMapWithLateral() throws Exception {
    String sql = "SELECT al.* FROM MY_TABLE1, LATERAL(select MY_TABLE2.a from MY_TABLE2) as AL";
    Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(selectStatement);
    assertEquals(2, itemMap.size());
    assertEquals("{AL=([MY_TABLE2],{A=[(MY_TABLE2,A,[Select→FromItem→SubSelect→SelectExpressionItem],false)], *=[(MY_TABLE2,A,[Select, Select→FromItem→SubSelect→SelectExpressionItem],false)]}), MY_TABLE1=([MY_TABLE1],{})}", itemMap.toString());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemMapFromDelete() throws Exception {
    String sql = "DELETE FROM MY_TABLE1 as AL WHERE a = (SELECT a from MY_TABLE2)";
    Statement statement = pm.parse(new StringReader(sql));

    Delete deleteStatement = (Delete) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(deleteStatement);
    System.out.println(itemMap);
    assertEquals(2, itemMap.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemMapFromDelete2() throws Exception {
    String sql = "DELETE FROM MY_TABLE1";
    Statement statement = pm.parse(new StringReader(sql));

    Delete deleteStatement = (Delete) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(deleteStatement);
    System.out.println(itemMap);
    assertEquals(1, itemMap.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemMapFromDeleteWithJoin() throws Exception {
    String sql = "DELETE t1, t2 FROM MY_TABLE1 t1 JOIN MY_TABLE2 t2 ON t1.id = t2.id";
    Statement statement = pm.parse(new StringReader(sql));

    Delete deleteStatement = (Delete) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(deleteStatement);
    System.out.println(itemMap);
    assertEquals(2, itemMap.size());
  }

  @Test
  public void testGetItemMapFromInsert() throws Exception {
    String sql = "INSERT INTO MY_TABLE1 (a) VALUES ((SELECT MY_TABLE2.a from MY_TABLE2 WHERE a = 1))";
    Statement statement = pm.parse(new StringReader(sql));

    Insert insertStatement = (Insert) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(insertStatement);
    assertEquals(1, itemMap.size());
    assertEquals("{*=([MY_TABLE2],{A=[(MY_TABLE2,A,[Insert→SubSelect→SelectExpressionItem],false)]})}", itemMap.toString());
  }

  @Test
  public void testGetItemMapFromInsertValues() throws Exception {
    String sql = "INSERT INTO MY_TABLE1 (a) VALUES (5)";
    Statement statement = pm.parse(new StringReader(sql));

    Insert insertStatement = (Insert) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(insertStatement);
    assertEquals(0, itemMap.size());
    assertEquals("{}", itemMap.toString());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemMapFromReplace() throws Exception {
    String sql = "REPLACE INTO MY_TABLE1 (a) VALUES ((SELECT a from MY_TABLE2 WHERE a = 1))";
    Statement statement = pm.parse(new StringReader(sql));

    Replace replaceStatement = (Replace) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(replaceStatement);
    System.out.println(itemMap);
    assertEquals(2, itemMap.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemMapFromUpdate() throws Exception {
    String sql = "UPDATE MY_TABLE1 SET a = (SELECT a from MY_TABLE2 WHERE a = 1)";
    Statement statement = pm.parse(new StringReader(sql));

    Update updateStatement = (Update) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(updateStatement);
    System.out.println(itemMap);
    assertEquals(2, itemMap.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemMapFromUpdate2() throws Exception {
    String sql = "UPDATE MY_TABLE1 SET a = 5 WHERE 0 < (SELECT COUNT(b) FROM MY_TABLE3)";
    Statement statement = pm.parse(new StringReader(sql));

    Update updateStatement = (Update) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(updateStatement);
    System.out.println(itemMap);
    assertEquals(2, itemMap.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemMapFromUpdate3() throws Exception {
    String sql = "UPDATE MY_TABLE1 SET a = 5 FROM MY_TABLE1 INNER JOIN MY_TABLE2 on MY_TABLE1.C = MY_TABLE2.D WHERE 0 < (SELECT COUNT(b) FROM MY_TABLE3)";
    Statement statement = pm.parse(new StringReader(sql));

    Update updateStatement = (Update) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(updateStatement);
    System.out.println(itemMap);
    assertEquals(3, itemMap.size());
  }

  @Test
  public void testCmplxSelectProblem() throws Exception {
    String sql = "SELECT tbl.cid, (SELECT tbl0.name FROM tbl0 WHERE tbl0.id = cid) AS name, tbl.original_id AS bc_id FROM tbl WHERE crid = ? AND user_id is null START WITH ID = (SELECT original_id FROM tbl2 WHERE USER_ID = ?) CONNECT BY prior parent_id = id AND rownum = 1";
    Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(selectStatement);
    assertEquals(2, itemMap.size());
    assertEquals("{*=([TBL0],{NAME=[(TBL0,NAME,[Select→SelectExpressionItem→SubSelect→SelectExpressionItem],false)]}), TBL=([TBL],{ORIGINAL_ID=[(TBL,ORIGINAL_ID,[Select→SelectExpressionItem],false)], BC_ID=[(TBL,ORIGINAL_ID,[Select→SelectExpressionItem],false)], CID=[(TBL,CID,[Select→SelectExpressionItem],false)]})}", itemMap.toString());
  }

  @Test
  public void testInsertSelect() throws Exception {
    String sql = "INSERT INTO mytable (mycolumn) SELECT mytable2.mycolumn FROM mytable2";
    Statement statement = pm.parse(new StringReader(sql));

    Insert insertStatement = (Insert) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(insertStatement);
    assertEquals(1, itemMap.size());
    assertEquals("{MYTABLE2=([MYTABLE2],{MYCOLUMN=[(MYTABLE2,MYCOLUMN,[Insert→Select→SelectExpressionItem],false)]})}", itemMap.toString());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCreateSelect() throws Exception {
    String sql = "CREATE TABLE mytable AS SELECT mycolumn FROM mytable2";
    Statement statement = pm.parse(new StringReader(sql));

    CreateTable createTable = (CreateTable) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(createTable);
    System.out.println(itemMap);
    assertEquals(2, itemMap.size());
  }

  @Test
  public void testInsertSubSelect() throws JSQLParserException {
    String sql = "INSERT INTO Customers (CustomerName, Country) SELECT Suppliers.SupplierName, Suppliers.Country FROM Suppliers WHERE Country='Germany'";
    Insert insert = (Insert) pm.parse(new StringReader(sql));
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(insert);
    assertEquals(1, itemMap.size());
    assertEquals("{SUPPLIERS=([SUPPLIERS],{SUPPLIERNAME=[(SUPPLIERS,SUPPLIERNAME,[Insert→Select→SelectExpressionItem],false)], COUNTRY=[(SUPPLIERS,COUNTRY,[Insert→Select→SelectExpressionItem],false)]})}", itemMap.toString());
  }

  @Test
  public void testExpr() throws JSQLParserException {
    String sql = "table.mycol in (select mytable.col2 from mytable)";
    Expression expr = (Expression) CCJSqlParserUtil.parseCondExpression(sql);
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(expr);
    assertEquals(2, itemMap.size());
    assertEquals("{TABLE=([TABLE],{MYCOL=[(TABLE,MYCOL,[],false)]}), *=([MYTABLE],{COL2=[(MYTABLE,COL2,[SubSelect→SelectExpressionItem],false)]})}", itemMap.toString());
  }

  @Test
  public void testOracleHint() throws JSQLParserException {
    String sql = "select --+ HINT\ncol2 from mytable";
    Select select = (Select) CCJSqlParserUtil.parse(sql);
    final OracleHint[] holder = new OracleHint[1];
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder() {

      @Override
      public void visit(OracleHint hint) {
        super.visit(hint);
        holder[0] = hint;
      }

    };
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(select);
    assertNull(holder[0]);
    assertEquals("{MYTABLE=([MYTABLE],{COL2=[(MYTABLE,COL2,[Select→SelectExpressionItem],false)]})}", itemMap.toString());
  }

  @Test
  public void testGetItemMapIssue194() throws Exception {
    String sql = "SELECT 1";
    Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(selectStatement);
    assertEquals(0, itemMap.size());
    assertEquals("{}", itemMap.toString());
  }

  @Test
  public void testGetItemMapIssue284() throws Exception {
    String sql = "SELECT NVL( (SELECT 1 FROM DUAL), 1) AS A FROM TEST1";
    Select selectStatement = (Select) CCJSqlParserUtil.parse(sql);
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(selectStatement);
    assertEquals(2, itemMap.size());
    assertEquals("{*=([DUAL],{}), TEST1=([TEST1],{})}", itemMap.toString());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUpdateGetItemMapIssue295() throws JSQLParserException {
    Update statement = (Update) CCJSqlParserUtil.
      parse("UPDATE component SET col = 0 WHERE (component_id,ver_num) IN (SELECT component_id,ver_num FROM component_temp)");
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(statement);
    System.out.println(itemMap);
    assertEquals(2, itemMap.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemMapForMerge() throws Exception {
    String sql = "MERGE INTO employees e  USING hr_records h  ON (e.id = h.emp_id) WHEN MATCHED THEN  UPDATE SET e.address = h.address  WHEN NOT MATCHED THEN    INSERT (id, address) VALUES (h.emp_id, h.address);";
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();

    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap((Merge) CCJSqlParserUtil.parse(sql));
    System.out.println(itemMap);
    assertEquals(2, itemMap.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetItemMapForMergeUsingQuery() throws Exception {
    String sql = "MERGE INTO employees e USING (SELECT * FROM hr_records WHERE start_date > ADD_MONTHS(SYSDATE, -1)) h  ON (e.id = h.emp_id)  WHEN MATCHED THEN  UPDATE SET e.address = h.address WHEN NOT MATCHED THEN INSERT (id, address) VALUES (h.emp_id, h.address)";
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap((Merge) CCJSqlParserUtil.parse(sql));
    System.out.println(itemMap);
    assertEquals(2, itemMap.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUpsertValues() throws Exception {
    String sql = "UPSERT INTO MY_TABLE1 (a) VALUES (5)";
    Statement statement = pm.parse(new StringReader(sql));

    Upsert insertStatement = (Upsert) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(insertStatement);
    System.out.println(itemMap);
    assertEquals(1, itemMap.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUpsertSelect() throws Exception {
    String sql = "UPSERT INTO mytable (mycolumn) SELECT mycolumn FROM mytable2";
    Statement statement = pm.parse(new StringReader(sql));

    Upsert insertStatement = (Upsert) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(insertStatement);
    System.out.println(itemMap);
    assertEquals(2, itemMap.size());
  }

  @Test
  public void testCaseWhenSubSelect() throws JSQLParserException {
    String sql = "select case (select count(*) from mytable2) when 1 then 0 else -1 end";
    Statement stmt = CCJSqlParserUtil.parse(sql);
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(stmt);
    assertEquals(1, itemMap.size());
    assertEquals("{*=([MYTABLE2],{})}", itemMap.toString());
  }

  @Test
  public void testCaseWhenSubSelect2() throws JSQLParserException {
    String sql = "select case when (select count(*) from mytable2) = 1 then 0 else -1 end";
    Statement stmt = CCJSqlParserUtil.parse(sql);
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(stmt);
    assertEquals(1, itemMap.size());
    assertEquals("{*=([MYTABLE2],{})}", itemMap.toString());
  }

  @Test
  public void testCaseWhenSubSelect3() throws JSQLParserException {
    String sql = "select case when 1 = 2 then 0 else (select count(*) from mytable2) end";
    Statement stmt = CCJSqlParserUtil.parse(sql);
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(stmt);
    assertEquals(1, itemMap.size());
    assertEquals("{*=([MYTABLE2],{})}", itemMap.toString());
  }

  @Test
  public void testExpressionIssue515() throws JSQLParserException {
    SensitiveItemsFinder finder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = finder.findItemMap(CCJSqlParserUtil.parseCondExpression("SOME_TABLE.COLUMN = 'A'"));
    assertEquals(1, itemMap.size());
    assertEquals("{SOME_TABLE=([SOME_TABLE],{COLUMN=[(SOME_TABLE,COLUMN,[],false)]})}", itemMap.toString());
  }

  @Test
  public void testSelectHavingSubquery() throws Exception {
    String sql = "SELECT TABLE1.* FROM TABLE1 GROUP BY COL1 HAVING SUM(COL2) > (SELECT COUNT(*) FROM TABLE2)";
    Statement statement = pm.parse(new StringReader(sql));

    Select selectStmt = (Select) statement;
    SensitiveItemsFinder sensitiveItemsFinder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = sensitiveItemsFinder.findItemMap(selectStmt);
    assertEquals(1, itemMap.size());
    assertEquals("{TABLE1=([TABLE1],{*=[(TABLE1,*,[Select],false)]})}", itemMap.toString());
  }

  @Test
  public void testMySQLValueListExpression() throws JSQLParserException {
    String sql = "SELECT * FROM TABLE1 WHERE (a, b) = (c, d)";
    SensitiveItemsFinder finder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = finder.findItemMap(CCJSqlParserUtil.parse(sql));
    assertEquals(1, itemMap.size());
    assertEquals("{TABLE1=([TABLE1],{*=[(TABLE1,*,[Select],false)]})}", itemMap.toString());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSkippedSchemaIssue600() throws JSQLParserException {
    String sql = "delete from schema.table where id = 1";
    SensitiveItemsFinder finder = new SensitiveItemsFinder();
    Map<String, Tuple.Pair<Set<String>, Map<String, Set<Tuple.Quadruple<String, String, Set<String>, Boolean>>>>> itemMap = finder.findItemMap(CCJSqlParserUtil.parse(sql));
    System.out.println(itemMap);
    assertEquals(1, itemMap.size());
  }

}
