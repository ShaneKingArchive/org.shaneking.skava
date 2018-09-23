package test.skava.sql.parser;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.shaneking.skava.ling.collect.Tuple;
import org.shaneking.skava.ling.lang.String0;
import org.shaneking.skava.sql.parser.SensitiveItemsFinder;
import org.shaneking.skava.sql.parser.SensitiveStatementReplacerFactory;

import java.util.Map;

public class SensitiveExpressionReplacerTest {
  Map<String, Tuple.Triple<String, String, String>> itemMap = Maps.newHashMap();
  Map<String, String> tableMap = Maps.newHashMap();

  @Before
  public void setUp() throws Exception {
    itemMap.put("T.A", Tuple.of(Joiner.on(String0.ARROW).join(SensitiveItemsFinder.PATH_OF_SELECT, SensitiveItemsFinder.PATH_OF_SELECT_EXPRESSION_ITEM), "hash(", ")"));
    tableMap.put("SCHEMA.TABLE", "(SELECT * FROM SCHEMA.TABLE)");
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testPlainSelect() throws Exception {
    StringBuilder sb = new StringBuilder();
    Statement statement = CCJSqlParserUtil.parse("select t.a as ta,t.* from schema.table t");
    statement.accept(SensitiveStatementReplacerFactory.create(itemMap, sb, tableMap));
//    System.out.println(sb);
    Assert.assertEquals("SELECT hash(t.a) AS ta, t.* FROM (SELECT * FROM SCHEMA.TABLE) t", sb.toString());
  }

}
