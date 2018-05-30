package test.skava.sql.entity;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.shaneking.skava.ling.lang.String0;
import test.skava.SKUnit;

public class SKEntityTest extends SKUnit
{
  private UserEntity userEntity = new UserEntity();

  @Test
  public void insertOrUpdateById() throws Exception
  {
    skPrint(userEntity.insertOrUpdateById());
  }

  @Test
  public void select() throws Exception
  {
    skPrint(userEntity.select());
  }

  @Test
  public void updateByIdAndVersionSql() throws Exception
  {
    skPrint(userEntity.updateByIdAndVersionSql());
  }

  @Test
  public void extUpdateStatement() throws Exception
  {
    userEntity.updateStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genUpdateStatement() throws Exception
  {
    userEntity.updateStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void insertSql() throws Exception
  {
    skPrint(userEntity.insertSql());
  }

  @Test
  public void extInsertStatement() throws Exception
  {
    userEntity.insertStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genInsertStatement() throws Exception
  {
    userEntity.insertStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void selectSql() throws Exception
  {
    skPrint(userEntity.selectSql());
  }

  @Test
  public void extOrderByStatement() throws Exception
  {
    userEntity.orderByStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genOrderByStatement() throws Exception
  {
    userEntity.orderByStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void extHavingStatement() throws Exception
  {
    userEntity.havingStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genHavingStatement() throws Exception
  {
    userEntity.havingStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void extGroupByStatement() throws Exception
  {
    userEntity.groupByStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genGroupByStatement() throws Exception
  {
    userEntity.groupByStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void extWhereStatement() throws Exception
  {
    userEntity.whereStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genWhereStatement() throws Exception
  {
    userEntity.whereStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void extFromStatement() throws Exception
  {
    userEntity.fromStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genFromStatement() throws Exception
  {
    userEntity.fromStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void extSelectStatement() throws Exception
  {
    userEntity.selectStatementExt(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genSelectStatement() throws Exception
  {
    userEntity.selectStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void initColumnInfo() throws Exception
  {
    userEntity.initColumnInfo(userEntity.getClass());
  }

  @Test
  public void initTableInfo() throws Exception
  {
    userEntity.initTableInfo();
  }

  @Test
  public void replaceUpperCase2UnderlineLowerCase() throws Exception
  {
    skPrint(String0.replaceUpperCase2UnderlineLowerCase(this.getClass().getName()));
  }

}
