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
import org.shaneking.skava.sql.annotation.SKTransient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class SKEntity
{
  @SKTransient
  private static final Logger LOG = LoggerFactory.getLogger(SKEntity.class);

  @SKColumn(length = 36)
  private String uuid;
  @SKColumn(length = 11, dataType = "INT")
  private Integer version = 1;
  /**
   * !important, can't be criteria for query
   * <p>
   * InnoDB prefix index max 767 bytes(utf8:767/3=255char;gbk:767/2=383char)
   */
  @SKColumn(canWhere = false, dataType = "LONGTEXT")
  private String extJson;
  /**
   * @see org.shaneking.skava.ling.util.Date0#Y_M_D
   */
  @SKColumn(length = 10)
  private String insertDate;
  /**
   * @see org.shaneking.skava.ling.util.Date0#H_MI_S
   */
  @SKColumn(length = 8)
  private String insertTime;
  @SKColumn(length = 36)
  private String insertUserId;
  /**
   * @see org.shaneking.skava.ling.util.Date0#Y_M_D
   */
  @SKColumn(length = 10)
  private String lastModifyDate;
  /**
   * @see org.shaneking.skava.ling.util.Date0#H_MI_S
   */
  @SKColumn(length = 8)
  private String lastModifyTime;
  @SKColumn(length = 36)
  private String lastModifyUserId;
  @SKColumn(length = 1)
  private String invalid;//0|1
  /**
   * @see org.shaneking.skava.ling.util.Date0#Y_M_D
   */
  @SKColumn(length = 10)
  private String invalidDate;
  /**
   * @see org.shaneking.skava.ling.util.Date0#H_MI_S
   */
  @SKColumn(length = 8)
  private String invalidTime;
  @SKColumn(length = 36)
  private String invalidUserId;

  @SKTransient
  private SKTable skTable;
  @SKTransient
  private String  tableName;

  @SKTransient
  private List<String>          unTransientFieldNameList = Lists.newArrayList();
  @SKTransient
  private Map<String, SKColumn> skColumnMap              = Maps.newHashMap();
  @SKTransient
  private Map<String, String>   columnNameMap            = Maps.newHashMap();

  public SKEntity()
  {
    initTableInfo();
    initColumnInfo(this.getClass());
  }

  public int insertOrUpdateById()
  {
    int rtnInt = 0;
    //TODO
    return rtnInt;
  }

  public List<? extends SKEntity> select()
  {
    List<? extends SKEntity> rtnList = Lists.newArrayList();
    //TODO
    return rtnList;
  }

  public Tuple.Pair<String, List<Object>> updateByIdAndVersionSql()
  {
    List<Object> rtnObjectList = Lists.newArrayList();

    List<String> updateList = Lists.newArrayList();
    genUpdateStatement(updateList, rtnObjectList);
    extUpdateStatement(updateList, rtnObjectList);

    List<String> sqlList = Lists.newArrayList();
    sqlList.add("update");
    sqlList.add(tableName);
    sqlList.add("set");
    sqlList.add(Joiner.on(String0.COMMA).join(updateList));
    sqlList.add("where");
    sqlList.add("uuid=? and version=?");
    rtnObjectList.add(uuid);
    rtnObjectList.add(version);

    return Tuple.of(Joiner.on(" ").join(sqlList), rtnObjectList);
  }

  public void extUpdateStatement(@Nonnull List<String> updateList, @Nonnull List<Object> objectList)
  {

  }

  public void genUpdateStatement(@Nonnull List<String> updateList, @Nonnull List<Object> objectList)
  {
    Object o = null;
    for (String fieldName : unTransientFieldNameList)
    {
      if (!"uuid".equals(fieldName))
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
          updateList.add(columnNameMap.get(fieldName) + "=?");
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
  }

  public Tuple.Pair<String, List<Object>> insertSql()
  {
    List<Object> rtnObjectList = Lists.newArrayList();

    List<String> insertList = Lists.newArrayList();
    genInsertStatement(insertList, rtnObjectList);
    extInsertStatement(insertList, rtnObjectList);

    List<String> sqlList = Lists.newArrayList();
    sqlList.add("insert into");
    sqlList.add(tableName);
    sqlList.add("(" + Joiner.on(String0.COMMA).join(insertList) + ")");
    sqlList.add("values");
    sqlList.add("(" + Strings.repeat(",?", insertList.size()).substring(1) + ")");

    return Tuple.of(Joiner.on(" ").join(sqlList), rtnObjectList);
  }

  public void extInsertStatement(@Nonnull List<String> insertList, @Nonnull List<Object> objectList)
  {
  }

  public void genInsertStatement(@Nonnull List<String> insertList, @Nonnull List<Object> objectList)
  {
    Object o = null;
    for (String fieldName : unTransientFieldNameList)
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
        insertList.add(columnNameMap.get(fieldName));
        objectList.add(o);
      }
    }
  }


  public Tuple.Pair<String, List<Object>> selectSql()
  {
    List<Object> rtnObjectList = Lists.newArrayList();

    List<String> selectList = Lists.newArrayList();
    genSelectStatement(selectList, rtnObjectList);
    extSelectStatement(selectList, rtnObjectList);

    List<String> fromList = Lists.newArrayList();
    genFromStatement(fromList, rtnObjectList);
    extFromStatement(fromList, rtnObjectList);

    List<String> whereList = Lists.newArrayList();
    genWhereStatement(whereList, rtnObjectList);
    extWhereStatement(whereList, rtnObjectList);

    List<String> groupByList = Lists.newArrayList();
    genGroupByStatement(groupByList, rtnObjectList);
    extGroupByStatement(groupByList, rtnObjectList);

    List<String> havingList = Lists.newArrayList();
    genHavingStatement(havingList, rtnObjectList);
    extHavingStatement(havingList, rtnObjectList);

    List<String> orderByList = Lists.newArrayList();
    genOrderByStatement(orderByList, rtnObjectList);
    extOrderByStatement(orderByList, rtnObjectList);

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

  public void extOrderByStatement(@Nonnull List<String> orderByList, @Nonnull List<Object> objectList)
  {
  }

  public void genOrderByStatement(@Nonnull List<String> orderByList, @Nonnull List<Object> objectList)
  {
  }

  public void extHavingStatement(@Nonnull List<String> havingList, @Nonnull List<Object> objectList)
  {
  }

  public void genHavingStatement(@Nonnull List<String> havingList, @Nonnull List<Object> objectList)
  {
  }

  public void extGroupByStatement(@Nonnull List<String> groupByList, @Nonnull List<Object> objectList)
  {
  }

  public void genGroupByStatement(@Nonnull List<String> groupByList, @Nonnull List<Object> objectList)
  {
  }

  public void extWhereStatement(@Nonnull List<String> whereList, @Nonnull List<Object> objectList)
  {
  }

  public void genWhereStatement(@Nonnull List<String> whereList, @Nonnull List<Object> objectList)
  {
    Object o = null;
    for (String fieldName : unTransientFieldNameList)
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
        whereList.add(columnNameMap.get(fieldName) + "=?");
        objectList.add(o);
      }
    }
  }

  public void extFromStatement(@Nonnull List<String> fromList, @Nonnull List<Object> objectList)
  {
  }

  public void genFromStatement(@Nonnull List<String> fromList, @Nonnull List<Object> objectList)
  {
    fromList.add(tableName);
  }

  public void extSelectStatement(@Nonnull List<String> selectList, @Nonnull List<Object> objectList)
  {
    //nothing
  }

  public void genSelectStatement(@Nonnull List<String> selectList, @Nonnull List<Object> objectList)
  {
    //jdk8 can't infer it
    Function<String, String> tmpFunc = (String fieldName) -> columnNameMap.get(fieldName);
    selectList.addAll(Lists.transform(unTransientFieldNameList, tmpFunc));
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
      if (field.getAnnotation(SKTransient.class) == null)
      {
        unTransientFieldNameList.add(field.getName());
        if (skColumn != null)
        {
          skColumnMap.put(field.getName(), skColumn);
          if (!Strings.isNullOrEmpty(skColumn.name()))
          {
            columnNameMap.put(field.getName(), skColumn.name());
          }
        }
        if (Strings.isNullOrEmpty(columnNameMap.get(field.getName())))
        {
          columnNameMap.put(field.getName(), replaceUpperCase2UnderlineLowerCase(field.getName()));
        }
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
      tableName = replaceUpperCase2UnderlineLowerCase(Lists.reverse(Lists.newArrayList(this.getClass().getName().split("\\."))).get(0));
      tableName = "t" + (tableName.startsWith(String0.UNDERLINE) ? tableName : String0.UNDERLINE + tableName);
    }
    else
    {
      tableName = (Strings.isNullOrEmpty(skTable.schema()) ? "" : skTable.schema() + String0.DOT) + skTable.name();
    }
  }

  public String replaceUpperCase2UnderlineLowerCase(@Nonnull String hasUpperCaseString)
  {
    //jdk8 can't infer it
    Function<String, String> tmpFunc = (alphabet) -> alphabet.equals(alphabet.toUpperCase()) ? String0.UNDERLINE + alphabet.toLowerCase() : alphabet;
    return Joiner.on("").join(Lists.transform(Lists.newArrayList(hasUpperCaseString.split("")), tmpFunc));
  }

  public String getUuid()
  {
    return uuid;
  }

  public SKEntity setUuid(String uuid)
  {
    this.uuid = uuid;
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

  public String getInsertDate()
  {
    return insertDate;
  }

  public SKEntity setInsertDate(String insertDate)
  {
    this.insertDate = insertDate;
    return this;
  }

  public String getInsertTime()
  {
    return insertTime;
  }

  public SKEntity setInsertTime(String insertTime)
  {
    this.insertTime = insertTime;
    return this;
  }

  public String getInsertUserId()
  {
    return insertUserId;
  }

  public SKEntity setInsertUserId(String insertUserId)
  {
    this.insertUserId = insertUserId;
    return this;
  }

  public String getLastModifyDate()
  {
    return lastModifyDate;
  }

  public SKEntity setLastModifyDate(String lastModifyDate)
  {
    this.lastModifyDate = lastModifyDate;
    return this;
  }

  public String getLastModifyTime()
  {
    return lastModifyTime;
  }

  public SKEntity setLastModifyTime(String lastModifyTime)
  {
    this.lastModifyTime = lastModifyTime;
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

  public String getInvalidDate()
  {
    return invalidDate;
  }

  public SKEntity setInvalidDate(String invalidDate)
  {
    this.invalidDate = invalidDate;
    return this;
  }

  public String getInvalidTime()
  {
    return invalidTime;
  }

  public SKEntity setInvalidTime(String invalidTime)
  {
    this.invalidTime = invalidTime;
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
