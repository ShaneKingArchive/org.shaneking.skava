package test.skava.sql.parser;

import com.google.common.collect.Maps;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.shaneking.skava.sql.parser.TableNamesStatementReplacerFactory;

import java.util.Map;

public class TableNamesSelectReplacerTest {
  Map<String, String> tableMap = Maps.newHashMap();

  @Before
  public void setUp() throws Exception {
    tableMap.put("SCHEMA.TABLE".toUpperCase(), "(SELECT * FROM SCHEMA.TABLE)");
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testPlainSelect() throws Exception {
    StringBuilder sb = new StringBuilder();
    Statement statement = CCJSqlParserUtil.parse("select t.*--comments\n from schema.table t");
    statement.accept(TableNamesStatementReplacerFactory.create(sb, tableMap));
//    System.out.println(sb);
    Assert.assertEquals("SELECT t.* FROM (SELECT * FROM SCHEMA.TABLE) t", sb.toString());
  }

}
