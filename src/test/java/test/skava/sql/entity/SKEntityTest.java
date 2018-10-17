package test.skava.sql.entity;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.lang.String0;
import test.skava.SKUnit;

public class SKEntityTest extends SKUnit {
  private UserEntity userEntity = new UserEntity();

  @Test
  public void insertOrUpdateById() throws Exception {
    Assert.assertEquals(userEntity.insertOrUpdateById(), 0);
  }

  @Test
  public void select() throws Exception {
    Assert.assertEquals(userEntity.select().toString(), "[]");
  }

  @Test
  public void updateByIdAndVersionSql() throws Exception {
    Assert.assertEquals(userEntity.updateByIdAndVersionSql().toString(), "(update t_user_entity set version=? where id=? and version=?,[2, null, 1])");
  }

  @Test
  public void extUpdateStatement() throws Exception {
    userEntity.updateStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genUpdateStatement() throws Exception {
    userEntity.updateStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void insertSql() throws Exception {
    Assert.assertEquals(userEntity.insertSql().toString(), "(insert into t_user_entity (version) values (?),[1])");
  }

  @Test
  public void extInsertStatement() throws Exception {
    userEntity.insertStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genInsertStatement() throws Exception {
    userEntity.insertStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void selectSql() throws Exception {
    Assert.assertEquals(userEntity.selectSql().toString(), "(select birthday,id,version,ext_json,create_datetime,create_user_id,last_modify_datetime,last_modify_user_id,invalid,invalid_datetime,invalid_user_id from t_user_entity where version=?,[1])");
  }

  @Test
  public void extOrderByStatement() throws Exception {
    userEntity.orderByStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genOrderByStatement() throws Exception {
    userEntity.orderByStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void extHavingStatement() throws Exception {
    userEntity.havingStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genHavingStatement() throws Exception {
    userEntity.havingStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void extGroupByStatement() throws Exception {
    userEntity.groupByStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genGroupByStatement() throws Exception {
    userEntity.groupByStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void extWhereStatement() throws Exception {
    userEntity.whereStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genWhereStatement() throws Exception {
    userEntity.whereStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void extFromStatement() throws Exception {
    userEntity.fromStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genFromStatement() throws Exception {
    userEntity.fromStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void extSelectStatement() throws Exception {
    userEntity.selectStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genSelectStatement() throws Exception {
    userEntity.selectStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void initColumnInfo() throws Exception {
    userEntity.initColumnInfo(userEntity.getClass());
  }

  @Test
  public void initTableInfo() throws Exception {
    userEntity.initTableInfo();
  }

  @Test
  public void upper2lower() throws Exception {
    Assert.assertEquals(String0.upper2lower(this.getClass().getName()), "test_.skava_.sql_.entity_._s_k_entity_test");
  }

  @Test
  public void delete() throws Exception {
    Assert.assertEquals(userEntity.delete(), 0);
  }

  @Test
  public void insert() throws Exception {
    Assert.assertEquals(userEntity.insert(), 0);
  }

  @Test
  public void update() throws Exception {
    Assert.assertEquals(userEntity.update(), 0);
  }
}
