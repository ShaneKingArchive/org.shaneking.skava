/*
 * @(#)SKEntity.java		Created at 2017/9/10
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.entity;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.shaneking.skava.ling.collect.Tuple;
import org.shaneking.skava.ling.lang.String0;
import org.shaneking.skava.sql.annotation.SKColumn;
import org.shaneking.skava.sql.annotation.SKTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SKEntity
{
  private static final Logger LOG = LoggerFactory.getLogger(SKEntity.class);
  private SKTable skTable;
  private String  tableName;

  private Map<String, String>   dbColumnMap   = Maps.newHashMap();
  private List<String>          fieldNameList = Lists.newArrayList();
  private Map<String, SKColumn> skColumnMap   = Maps.newHashMap();

  @SKColumn(length = 36)
  private String id;
  @SKColumn(length = 11, dataType = "INT")
  private Integer version = 1;
  /**
   * !important, can't be criteria for query, no index
   */
  @SKColumn(canWhere = false, dataType = "LONGTEXT")
  private String extJson;
  /**
   * @see org.shaneking.skava.ling.util.Date0#DATE_TIME
   */
  @SKColumn(length = 20)
  private String createDatetime;
  @SKColumn(length = 36)
  private String createUserId;
  /**
   * @see org.shaneking.skava.ling.util.Date0#DATE_TIME
   */
  @SKColumn(length = 20)
  private String lastModifyDatetime;
  @SKColumn(length = 36)
  private String lastModifyUserId;
  @SKColumn(length = 1)
  private String invalid;//0|1
  /**
   * @see org.shaneking.skava.ling.util.Date0#DATE_TIME
   */
  @SKColumn(length = 20)
  private String invalidDatetime;
  @SKColumn(length = 36)
  private String invalidUserId;

  public SKEntity()
  {
    initTableInfo();
    initColumnInfo(this.getClass());
  }

  public void initColumnInfo(Class<? extends Object> skEntityClass)
  {
    if (SKEntity.class.isAssignableFrom(skEntityClass.getSuperclass()))
    {
      initColumnInfo(skEntityClass.getSuperclass());
    }
    for (Field field : skEntityClass.getDeclaredFields())
    {
      SKColumn skColumn = field.getAnnotation(SKColumn.class);
      if (skColumn != null)
      {
        dbColumnMap.put(field.getName(), Strings.isNullOrEmpty(skColumn.name()) ? String0.upper2lower(field.getName()) : skColumn.name());
        fieldNameList.add(field.getName());
        skColumnMap.put(field.getName(), skColumn);
      }
    }
  }

  public void initTableInfo()
  {
    if (skTable == null)
    {
      skTable = this.getClass().getAnnotation(SKTable.class);
    }
    if (Strings.isNullOrEmpty(skTable.name()))
    {
      tableName = String0.upper2lower(Lists.reverse(Lists.newArrayList(this.getClass().getName().split("\\."))).get(0));
      tableName = "t" + (tableName.startsWith(String0.UNDERLINE) ? tableName : String0.UNDERLINE + tableName);
    }
    else
    {
      tableName = (Strings.isNullOrEmpty(skTable.schema()) ? "" : skTable.schema() + String0.DOT) + skTable.name();
    }
  }

  //curd
  public int delete()
  {
    int rtnInt = 0;
    //TODO
    return rtnInt;
  }

  public int insert()
  {
    int rtnInt = 0;
    //TODO
    return rtnInt;
  }

  public int insertOrUpdateById()
  {
    int rtnInt = 0;
    //TODO
    return rtnInt;
  }

  public Tuple.Pair<String, List<Object>> insertSql()
  {
    List<Object> rtnObjectList = Lists.newArrayList();

    List<String> insertList = Lists.newArrayList();
    insertStatement(insertList, rtnObjectList);
    insertStatementExt(insertList, rtnObjectList);

    List<String> sqlList = Lists.newArrayList();
    sqlList.add("insert into");
    sqlList.add(tableName);
    sqlList.add(String0.OPEN_PARENTHESIS + Joiner.on(String0.COMMA).join(insertList) + String0.CLOSE_PARENTHESIS);
    sqlList.add("values");
    sqlList.add(String0.OPEN_PARENTHESIS + Strings.repeat(String0.COMMA + String0.QUESTION, insertList.size()).substring(1) + String0.CLOSE_PARENTHESIS);

    return Tuple.of(Joiner.on(String0.BLACK).join(sqlList), rtnObjectList);
  }

  public void insertStatement(@Nonnull List<String> insertList, @Nonnull List<Object> objectList)
  {
    Object o;
    for (String fieldName : fieldNameList)
    {
      try
      {
        o = this.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)).invoke(this);
      }
      catch (Exception e)
      {
        o = null;
        LOG.warn(e.toString());
      }
      if (o != null && !Strings.isNullOrEmpty(o.toString()))
      {
        insertList.add(dbColumnMap.get(fieldName));
        objectList.add(o);
      }
    }
  }

  public void insertStatementExt(@Nonnull List<String> insertList, @Nonnull List<Object> objectList)
  {
  }

  public List<? extends SKEntity> select()
  {
    List<? extends SKEntity> rtnList = Lists.newArrayList();
    //TODO
    return rtnList;
  }

  public Tuple.Pair<String, List<Object>> selectSql()
  {
    List<Object> rtnObjectList = Lists.newArrayList();

    List<String> selectList = Lists.newArrayList();
    selectStatement(selectList, rtnObjectList);
    selectStatementExt(selectList, rtnObjectList);

    List<String> fromList = Lists.newArrayList();
    fromStatement(fromList, rtnObjectList);
    fromStatementExt(fromList, rtnObjectList);

    List<String> whereList = Lists.newArrayList();
    whereStatement(whereList, rtnObjectList);
    whereStatementExt(whereList, rtnObjectList);

    List<String> groupByList = Lists.newArrayList();
    groupByStatement(groupByList, rtnObjectList);
    groupByStatementExt(groupByList, rtnObjectList);

    List<String> havingList = Lists.newArrayList();
    havingStatement(havingList, rtnObjectList);
    havingStatementExt(havingList, rtnObjectList);

    List<String> orderByList = Lists.newArrayList();
    orderByStatement(orderByList, rtnObjectList);
    orderByStatementExt(orderByList, rtnObjectList);

    List<String> sqlList = Lists.newArrayList();
    sqlList.add("select");
    sqlList.add(Joiner.on(String0.COMMA).join(selectList));
    sqlList.add("from");
    sqlList.add(Joiner.on(String0.COMMA).join(fromList));
    if (whereList.size() > 0)
    {
      sqlList.add("where");
      sqlList.add(Joiner.on(" and ").join(whereList));
    }
    if (groupByList.size() > 0)
    {
      sqlList.add("group by");
      sqlList.add(Joiner.on(String0.COMMA).join(groupByList));
    }
    if (havingList.size() > 0)
    {
      sqlList.add("having");
      sqlList.add(Joiner.on(" and ").join(havingList));
    }
    if (orderByList.size() > 0)
    {
      sqlList.add("order by");
      sqlList.add(Joiner.on(String0.COMMA).join(orderByList));
    }

    return Tuple.of(Joiner.on(" ").join(sqlList), rtnObjectList);
  }

  public void selectStatement(@Nonnull List<String> selectList, @Nonnull List<Object> objectList)
  {
    //jdk8 can't infer it
    Function<String, String> tmpFunc = (String fieldName) -> dbColumnMap.get(fieldName);
    selectList.addAll(Lists.transform(fieldNameList, tmpFunc));
  }

  public void selectStatementExt(@Nonnull List<String> selectList, @Nonnull List<Object> objectList)
  {
    //nothing
  }

  public int update()
  {
    int rtnInt = 0;
    //TODO
    return rtnInt;
  }

  public Tuple.Pair<String, List<Object>> updateByIdAndVersionSql()
  {
    List<Object> rtnObjectList = Lists.newArrayList();

    List<String> updateList = Lists.newArrayList();
    updateStatement(updateList, rtnObjectList);
    updateStatementExt(updateList, rtnObjectList);

    List<String> sqlList = Lists.newArrayList();
    sqlList.add("update");
    sqlList.add(tableName);
    sqlList.add("set");
    sqlList.add(Joiner.on(String0.COMMA).join(updateList));
    sqlList.add("where");
    sqlList.add("id=? and version=?");
    rtnObjectList.add(id);
    rtnObjectList.add(version);

    return Tuple.of(Joiner.on(" ").join(sqlList), rtnObjectList);
  }

  public void updateStatement(@Nonnull List<String> updateList, @Nonnull List<Object> objectList)
  {
    Object o = null;
    for (String fieldName : fieldNameList.stream().filter(fieldName -> !"id".equals(fieldName)).collect(Collectors.toList()))
    {
      try
      {
        o = this.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)).invoke(this);
      }
      catch (Exception e)
      {
        o = null;
        LOG.warn(e.toString());
      }
      if (o != null && !Strings.isNullOrEmpty(o.toString()))
      {
        updateList.add(dbColumnMap.get(fieldName) + "=?");
        if ("version".equals(fieldName))
        {
          objectList.add((Integer) o + 1);
        }
        else
        {
          objectList.add(o);
        }
      }
    }
  }

  public void updateStatementExt(@Nonnull List<String> updateList, @Nonnull List<Object> objectList)
  {

  }

  //others
  public void fromStatement(@Nonnull List<String> fromList, @Nonnull List<Object> objectList)
  {
    fromList.add(tableName);
  }

  public void fromStatementExt(@Nonnull List<String> fromList, @Nonnull List<Object> objectList)
  {
  }

  public void groupByStatement(@Nonnull List<String> groupByList, @Nonnull List<Object> objectList)
  {
  }

  public void groupByStatementExt(@Nonnull List<String> groupByList, @Nonnull List<Object> objectList)
  {
  }

  public void havingStatement(@Nonnull List<String> havingList, @Nonnull List<Object> objectList)
  {
  }

  public void havingStatementExt(@Nonnull List<String> havingList, @Nonnull List<Object> objectList)
  {
  }

  public void orderByStatement(@Nonnull List<String> orderByList, @Nonnull List<Object> objectList)
  {
  }

  public void orderByStatementExt(@Nonnull List<String> orderByList, @Nonnull List<Object> objectList)
  {
  }

  public void whereStatement(@Nonnull List<String> whereList, @Nonnull List<Object> objectList)
  {
    Object o = null;
    for (String fieldName : fieldNameList)
    {
      try
      {
        o = this.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)).invoke(this);
      }
      catch (Exception e)
      {
        o = null;
        LOG.warn(e.toString());
      }
      if (o != null && !Strings.isNullOrEmpty(o.toString()) && (skColumnMap.get(fieldName) == null || skColumnMap.get(fieldName).canWhere()))
      {
        whereList.add(dbColumnMap.get(fieldName) + "=?");
        objectList.add(o);
      }
    }
  }

  public void whereStatementExt(@Nonnull List<String> whereList, @Nonnull List<Object> objectList)
  {
  }

  //get/set
  public String getId()
  {
    return id;
  }

  public SKEntity setId(String id)
  {
    this.id = id;
    return this;
  }

  public Integer getVersion()
  {
    return version;
  }

  public SKEntity setVersion(Integer version)
  {
    this.version = version;
    return this;
  }

  public String getExtJson()
  {
    return extJson;
  }

  public SKEntity setExtJson(String extJson)
  {
    this.extJson = extJson;
    return this;
  }

  public String getCreateDatetime()
  {
    return createDatetime;
  }

  public SKEntity setCreateDatetime(String createDatetime)
  {
    this.createDatetime = createDatetime;
    return this;
  }

  public String getCreateUserId()
  {
    return createUserId;
  }

  public SKEntity setCreateUserId(String createUserId)
  {
    this.createUserId = createUserId;
    return this;
  }

  public String getLastModifyDatetime()
  {
    return lastModifyDatetime;
  }

  public SKEntity setLastModifyDatetime(String lastModifyDatetime)
  {
    this.lastModifyDatetime = lastModifyDatetime;
    return this;
  }

  public String getLastModifyUserId()
  {
    return lastModifyUserId;
  }

  public SKEntity setLastModifyUserId(String lastModifyUserId)
  {
    this.lastModifyUserId = lastModifyUserId;
    return this;
  }

  public String getInvalid()
  {
    return invalid;
  }

  public SKEntity setInvalid(String invalid)
  {
    this.invalid = invalid;
    return this;
  }

  public String getInvalidDatetime()
  {
    return invalidDatetime;
  }

  public SKEntity setInvalidDatetime(String invalidDatetime)
  {
    this.invalidDatetime = invalidDatetime;
    return this;
  }

  public String getInvalidUserId()
  {
    return invalidUserId;
  }

  public SKEntity setInvalidUserId(String invalidUserId)
  {
    this.invalidUserId = invalidUserId;
    return this;
  }
}
