package sktest.skava.sql.entity;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sktest.skava.SKUnit;
import sktest.skava.sql.entity.prepare.PrepareSKEntityColumnNoGetMethod;
import sktest.skava.sql.entity.prepare.PrepareSKEntityColumns;
import sktest.skava.sql.entity.prepare.PrepareSKEntityOverride;
import sktest.skava.sql.entity.prepare.prepareSKEntityTableName;

public class SKEntityTest extends SKUnit {
  private PrepareSKEntityColumnNoGetMethod prepareSKEntityColumnNoGetMethod = new PrepareSKEntityColumnNoGetMethod();
  private PrepareSKEntityColumns prepareSKEntityColumns = new PrepareSKEntityColumns();
  private PrepareSKEntityOverride prepareSKEntityOverride = new PrepareSKEntityOverride();
  private prepareSKEntityTableName prepareSKEntityTableName = new prepareSKEntityTableName();

  @Before
  public void setUp() {
    super.setUp();
    prepareSKEntityColumnNoGetMethod = new PrepareSKEntityColumnNoGetMethod();
    prepareSKEntityColumns = new PrepareSKEntityColumns();
    prepareSKEntityOverride = new PrepareSKEntityOverride();
    prepareSKEntityTableName = new prepareSKEntityTableName();
  }

  @Test
  public void setter() {
    prepareSKEntityColumnNoGetMethod.setNoGetMethod("").setCreateDatetime("").setCreateUserId("").setExtJson("").setExtJsonStr("").setInvalid("").setInvalidDatetime("").setInvalidUserId("").setLastModifyDatetime("").setLastModifyUserId("");
  }


  @Test
  public void initColumnInfo() {
    prepareSKEntityColumns.initColumnInfo(prepareSKEntityColumns.getClass());
    prepareSKEntityTableName.initColumnInfo(prepareSKEntityTableName.getClass());
  }

  @Test
  public void initTableInfo() {
    prepareSKEntityColumns.initTableInfo();
    prepareSKEntityTableName.initTableInfo();
  }

  @Test
  public void delete() {
    Assert.assertEquals(prepareSKEntityColumns.delete(), 0);
  }

  @Test
  public void insert() {
    Assert.assertEquals(prepareSKEntityColumns.insert(), 0);
  }

  @Test
  public void insertOrUpdateById() {
    Assert.assertEquals(prepareSKEntityColumns.insertOrUpdateById(), 0);
  }

  @Test
  public void insertSql() {
    Assert.assertEquals(prepareSKEntityColumns.insertSql().toString(), "(insert into t_prepare_s_k_entity_columns (version) values (?),[1])");
  }

  @Test
  public void insertStatement() {
    prepareSKEntityColumns.insertStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void insertStatementColumnNoGetMethod() {
    prepareSKEntityColumnNoGetMethod.insertStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void insertStatementNull1() {
    prepareSKEntityColumns.insertStatement(null, Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void insertStatementNull2() {
    prepareSKEntityColumns.insertStatement(Lists.newArrayList(), null);
  }

  @Test
  public void insertStatementExt() {
    prepareSKEntityColumns.insertStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void insertStatementExtNull1() {
    prepareSKEntityColumns.insertStatementExt(null, Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void insertStatementExtNull2() {
    prepareSKEntityColumns.insertStatementExt(Lists.newArrayList(), null);
  }

  @Test
  public void select() {
    Assert.assertEquals(prepareSKEntityColumns.select().toString(), "[]");
  }

  @Test
  public void selectSql() {
    Assert.assertEquals(prepareSKEntityColumns.selectSql().toString(), "(select has_length,re_name,create_datetime,create_user_id,ext_json_str,id,invalid,invalid_datetime,invalid_user_id,last_modify_datetime,last_modify_user_id,version from t_prepare_s_k_entity_columns where version=?,[1])");
  }

  @Test
  public void selectSqlColumnNull() {
    prepareSKEntityColumns.setVersion(null);
    Assert.assertEquals(prepareSKEntityColumns.selectSql().toString(), "(select has_length,re_name,create_datetime,create_user_id,ext_json_str,id,invalid,invalid_datetime,invalid_user_id,last_modify_datetime,last_modify_user_id,version from t_prepare_s_k_entity_columns,[])");
  }

  @Test
  public void selectSqlOverride() {
    Assert.assertEquals(prepareSKEntityOverride.selectSql().toString(), "(select create_datetime,create_user_id,ext_json_str,id,invalid,invalid_datetime,invalid_user_id,last_modify_datetime,last_modify_user_id,version from t_prepare_s_k_entity_override where create_user_id in (?,?,?) and invalid_datetime between ? and ? and last_modify_datetime like ? and version=? group by version having version > ? order by version,[1, a, ,, 1949-10-01, 1996-07, %1949-10-01%, 1, 1])");
  }

  @Test
  public void selectStatement() {
    prepareSKEntityColumns.selectStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void selectStatementNull1() {
    prepareSKEntityColumns.selectStatement(null, Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void selectStatementNull2() {
    prepareSKEntityColumns.selectStatement(Lists.newArrayList(), null);
  }

  @Test
  public void selectStatementExt() {
    prepareSKEntityColumns.selectStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void selectStatementExtNull1() {
    prepareSKEntityColumns.selectStatementExt(null, Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void selectStatementExtNull2() {
    prepareSKEntityColumns.selectStatementExt(Lists.newArrayList(), null);
  }

  @Test
  public void update() {
    Assert.assertEquals(prepareSKEntityColumns.update(), 0);
  }

  @Test
  public void updateByIdAndVersionSql() {
    Assert.assertEquals(prepareSKEntityColumns.updateByIdAndVersionSql().toString(), "(update t_prepare_s_k_entity_columns set version=? where id=? and version=?,[2, null, 1])");
    prepareSKEntityColumns.setVersion(null);
    Assert.assertEquals(prepareSKEntityColumns.updateByIdAndVersionSql().toString(), "(update t_prepare_s_k_entity_columns set  where id=?,[null])");
  }

  @Test
  public void updateStatement() {
    prepareSKEntityColumns.updateStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void updateStatementColumnNoGetMethod() {
    prepareSKEntityColumnNoGetMethod.updateStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void updateStatementNull1() {
    prepareSKEntityColumns.updateStatement(null, Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void updateStatementNull2() {
    prepareSKEntityColumns.updateStatement(Lists.newArrayList(), null);
  }

  @Test
  public void updateStatementExt() {
    prepareSKEntityColumns.updateStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void updateStatementExtNull1() {
    prepareSKEntityColumns.updateStatementExt(null, Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void updateStatementExtNull2() {
    prepareSKEntityColumns.updateStatementExt(Lists.newArrayList(), null);
  }

  @Test
  public void fromStatement() {
    prepareSKEntityColumns.fromStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void fromStatementNull1() {
    prepareSKEntityColumns.fromStatement(null, Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void fromStatementNull2() {
    prepareSKEntityColumns.fromStatement(Lists.newArrayList(), null);
  }

  @Test
  public void fromStatementExt() {
    prepareSKEntityColumns.fromStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void fromStatementExtNull1() {
    prepareSKEntityColumns.fromStatementExt(null, Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void fromStatementExtNull2() {
    prepareSKEntityColumns.fromStatementExt(Lists.newArrayList(), null);
  }

  @Test
  public void groupByStatement() {
    prepareSKEntityColumns.groupByStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void groupByStatementNull1() {
    prepareSKEntityColumns.groupByStatement(null, Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void groupByStatementNull2() {
    prepareSKEntityColumns.groupByStatement(Lists.newArrayList(), null);
  }

  @Test
  public void groupByStatementExt() {
    prepareSKEntityColumns.groupByStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void groupByStatementExtNull1() {
    prepareSKEntityColumns.groupByStatementExt(null, Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void groupByStatementExtNull2() {
    prepareSKEntityColumns.groupByStatementExt(Lists.newArrayList(), null);
  }

  @Test
  public void havingStatement() {
    prepareSKEntityColumns.havingStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void havingStatementNull1() {
    prepareSKEntityColumns.havingStatement(null, Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void havingStatementNull2() {
    prepareSKEntityColumns.havingStatement(Lists.newArrayList(), null);
  }

  @Test
  public void havingStatementExt() {
    prepareSKEntityColumns.havingStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void havingStatementExtNull1() {
    prepareSKEntityColumns.havingStatementExt(null, Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void havingStatementExtNull2() {
    prepareSKEntityColumns.havingStatementExt(Lists.newArrayList(), null);
  }

  @Test
  public void orderByStatement() {
    prepareSKEntityColumns.orderByStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void orderByStatementNull1() {
    prepareSKEntityColumns.orderByStatement(null, Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void orderByStatementNull2() {
    prepareSKEntityColumns.orderByStatement(Lists.newArrayList(), null);
  }

  @Test
  public void orderByStatementExt() {
    prepareSKEntityColumns.orderByStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void orderByStatementExtNull1() {
    prepareSKEntityColumns.orderByStatementExt(null, Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void orderByStatementExtNull2() {
    prepareSKEntityColumns.orderByStatementExt(Lists.newArrayList(), null);
  }

  @Test
  public void whereStatement() {
    prepareSKEntityColumnNoGetMethod.whereStatement(Lists.newArrayList(), Lists.newArrayList());
    prepareSKEntityColumns.whereStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void whereStatementColumnMap() {
    prepareSKEntityColumns.setHasLength("hasLength");
    prepareSKEntityColumns.getColumnMap().remove("hasLength");
    prepareSKEntityColumns.whereStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void whereStatementOperationContent() {
    prepareSKEntityOverride.whereStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void whereStatementNull1() {
    prepareSKEntityColumns.whereStatement(null, Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void whereStatementNull2() {
    prepareSKEntityColumns.whereStatement(Lists.newArrayList(), null);
  }

  @Test
  public void whereStatementExt() {
    prepareSKEntityColumns.whereStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void whereStatementExtNull1() {
    prepareSKEntityColumns.whereStatementExt(null, Lists.newArrayList());
  }

  @Test(expected = NullPointerException.class)
  public void whereStatementExtNull2() {
    prepareSKEntityColumns.whereStatementExt(Lists.newArrayList(), null);
  }
}
