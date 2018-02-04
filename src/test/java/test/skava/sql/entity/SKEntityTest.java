package test.skava.sql.entity;

import com.google.common.collect.Lists;
import org.junit.Test;
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
    userEntity.extUpdateStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genUpdateStatement() throws Exception
  {
    userEntity.genUpdateStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void insertSql() throws Exception
  {
    skPrint(userEntity.insertSql());
  }

  @Test
  public void extInsertStatement() throws Exception
  {
    userEntity.extInsertStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genInsertStatement() throws Exception
  {
    userEntity.genInsertStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void selectSql() throws Exception
  {
    skPrint(userEntity.selectSql());
  }

  @Test
  public void extOrderByStatement() throws Exception
  {
    userEntity.extOrderByStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genOrderByStatement() throws Exception
  {
    userEntity.genOrderByStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void extHavingStatement() throws Exception
  {
    userEntity.extHavingStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genHavingStatement() throws Exception
  {
    userEntity.genHavingStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void extGroupByStatement() throws Exception
  {
    userEntity.extGroupByStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genGroupByStatement() throws Exception
  {
    userEntity.genGroupByStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void extWhereStatement() throws Exception
  {
    userEntity.extWhereStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genWhereStatement() throws Exception
  {
    userEntity.genWhereStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void extFromStatement() throws Exception
  {
    userEntity.extFromStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genFromStatement() throws Exception
  {
    userEntity.genFromStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void extSelectStatement() throws Exception
  {
    userEntity.extSelectStatement(Lists.newArrayList(), Lists.newArrayList());
  }

  @Test
  public void genSelectStatement() throws Exception
  {
    userEntity.genSelectStatement(Lists.newArrayList(), Lists.newArrayList());
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
    skPrint(userEntity.replaceUpperCase2UnderlineLowerCase(this.getClass().getName()));
  }

}
