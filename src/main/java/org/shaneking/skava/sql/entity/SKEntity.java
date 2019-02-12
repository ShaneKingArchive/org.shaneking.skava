/*
 * @(#)SKEntity.java		Created at 2017/9/10
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.skava.ling.collect.Tuple;
import org.shaneking.skava.ling.lang.String0;
import org.shaneking.skava.ling.lang.String20;
import org.shaneking.skava.sql.annotation.SKColumn;
import org.shaneking.skava.sql.annotation.SKTable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Accessors(chain = true)
@Slf4j
@ToString(includeFieldNames = true)
public class SKEntity<J> {
  @JsonIgnore
  @Getter
  private final Map<String, String> dbColumnMap = Maps.newHashMap();
  @JsonIgnore
  @Getter
  private final List<String> fieldNameList = Lists.newArrayList();
  @JsonIgnore
  @Getter
  private final Map<String, SKColumn> skColumnMap = Maps.newHashMap();

  @JsonIgnore
  @Getter
  @Setter
  private String fullTableName;

  @JsonIgnore
  @Getter
  @Setter
  private SKTable skTable;

  //maybe fastjson,gson,jackson...
  @Getter
  @Setter
  private J extJson;//if extJson.createDatetime exist and it is Array, then t.create_datatime between (extJson.crateDatetime[0], extJson.crateDatetime[1])

  /**
   * !important, can't be criteria for query, no index
   */
  @Getter
  @Setter
  @SKColumn(canWhere = false, dataType = "LONGTEXT")
  private String extJsonStr;

  /**
   * @see org.shaneking.skava.ling.util.Date0#DATE_TIME
   */
  @Getter
  @Setter
  @SKColumn(length = 20, useLike = true)
  private String createDatetime;

  @Getter
  @Setter
  @SKColumn(length = 40)
  private String createUserId;

  @Getter
  @Setter
  @SKColumn(length = 40)
  private String id;

  @Getter
  @Setter
  @SKColumn(length = 10)
  private String invalid;//Y|N(default)

  /**
   * @see org.shaneking.skava.ling.util.Date0#DATE_TIME
   */
  @Getter
  @Setter
  @SKColumn(length = 20, useLike = true)
  private String invalidDatetime;

  @Getter
  @Setter
  @SKColumn(length = 40)
  private String invalidUserId;

  /**
   * @see org.shaneking.skava.ling.util.Date0#DATE_TIME
   */
  @Getter
  @Setter
  @SKColumn(length = 20, useLike = true)
  private String lastModifyDatetime;

  @Getter
  @Setter
  @SKColumn(length = 40)
  private String lastModifyUserId;

  @Getter
  @Setter
  @SKColumn(length = 20, dataType = "INT")
  private Integer version = 1;

  public SKEntity() {
    initTableInfo();
    initColumnInfo(this.getClass());
  }

  public void initColumnInfo(Class<? extends Object> skEntityClass) {
    for (Field field : skEntityClass.getDeclaredFields()) {
      SKColumn skColumn = field.getAnnotation(SKColumn.class);
      if (skColumn != null && fieldNameList.indexOf(field.getName()) == -1) {
        dbColumnMap.put(field.getName(), Strings.isNullOrEmpty(skColumn.name()) ? String0.upper2lower(field.getName()) : skColumn.name());
        fieldNameList.add(field.getName());
        skColumnMap.put(field.getName(), skColumn);
      }
    }
    if (SKEntity.class.isAssignableFrom(skEntityClass.getSuperclass())) {
      initColumnInfo(skEntityClass.getSuperclass());
    }
  }

  public void initTableInfo() {
    if (skTable == null) {
      skTable = this.getClass().getAnnotation(SKTable.class);
    }
    fullTableName = Strings.isNullOrEmpty(skTable.schema()) ? String0.EMPTY : skTable.schema() + String0.DOT;
    if (Strings.isNullOrEmpty(skTable.name())) {
      String classTableName = String0.upper2lower(Lists.reverse(Lists.newArrayList(this.getClass().getName().split(String20.BACKSLASH_DOT))).get(0));
      fullTableName = fullTableName + "t" + (classTableName.startsWith(String0.UNDERLINE) ? classTableName : String0.UNDERLINE + classTableName);
    } else {
      fullTableName = fullTableName + skTable.name();
    }
  }

  //curd
  public int delete() {
    int rtnInt = 0;
    //TODO
    return rtnInt;
  }

  public int insert() {
    int rtnInt = 0;
    //TODO
    return rtnInt;
  }

  public int insertOrUpdateById() {
    int rtnInt = 0;
    //TODO
    return rtnInt;
  }

  public Tuple.Pair<String, List<Object>> insertSql() {
    List<Object> rtnObjectList = Lists.newArrayList();

    List<String> insertList = Lists.newArrayList();
    insertStatement(insertList, rtnObjectList);
    insertStatementExt(insertList, rtnObjectList);

    List<String> sqlList = Lists.newArrayList();
    sqlList.add("insert into");
    sqlList.add(fullTableName);
    sqlList.add(String0.OPEN_PARENTHESIS + Joiner.on(String0.COMMA).join(insertList) + String0.CLOSE_PARENTHESIS);
    sqlList.add("values");
    sqlList.add(String0.OPEN_PARENTHESIS + Strings.repeat(String0.COMMA + String0.QUESTION, insertList.size()).substring(1) + String0.CLOSE_PARENTHESIS);

    return Tuple.of(Joiner.on(String0.BLACK).join(sqlList), rtnObjectList);
  }

  public void insertStatement(@NonNull List<String> insertList, @NonNull List<Object> objectList) {
    Object o;
    for (String fieldName : fieldNameList) {
      try {
        o = this.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)).invoke(this);
      } catch (Exception e) {
        o = null;
        log.warn(e.toString());
      }
      if (o != null && !Strings.isNullOrEmpty(o.toString())) {
        insertList.add(dbColumnMap.get(fieldName));
        objectList.add(o);
      }
    }
  }

  public void insertStatementExt(@NonNull List<String> insertList, @NonNull List<Object> objectList) {
  }

  public List<? extends SKEntity> select() {
    List<? extends SKEntity> rtnList = Lists.newArrayList();
    //TODO
    return rtnList;
  }

  public Tuple.Pair<String, List<Object>> selectSql() {
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
    if (whereList.size() > 0) {
      sqlList.add("where");
      sqlList.add(Joiner.on(" and ").join(whereList));
    }
    if (groupByList.size() > 0) {
      sqlList.add("group by");
      sqlList.add(Joiner.on(String0.COMMA).join(groupByList));
    }
    if (havingList.size() > 0) {
      sqlList.add("having");
      sqlList.add(Joiner.on(" and ").join(havingList));
    }
    if (orderByList.size() > 0) {
      sqlList.add("order by");
      sqlList.add(Joiner.on(String0.COMMA).join(orderByList));
    }

    return Tuple.of(Joiner.on(String0.BLACK).join(sqlList), rtnObjectList);
  }

  public void selectStatement(@NonNull List<String> selectList, @NonNull List<Object> objectList) {
    //jdk8 can't infer it
    Function<String, String> tmpFunc = (String fieldName) -> dbColumnMap.get(fieldName);
    selectList.addAll(Lists.transform(fieldNameList, tmpFunc));
  }

  public void selectStatementExt(@NonNull List<String> selectList, @NonNull List<Object> objectList) {
    //nothing
  }

  public int update() {
    int rtnInt = 0;
    //TODO
    return rtnInt;
  }

  public Tuple.Pair<String, List<Object>> updateByIdAndVersionSql() {
    List<Object> rtnObjectList = Lists.newArrayList();

    List<String> updateList = Lists.newArrayList();
    updateStatement(updateList, rtnObjectList);
    updateStatementExt(updateList, rtnObjectList);

    List<String> sqlList = Lists.newArrayList();
    sqlList.add("update");
    sqlList.add(fullTableName);
    sqlList.add("set");
    sqlList.add(Joiner.on(String0.COMMA).join(updateList));
    sqlList.add("where");
    sqlList.add("id=? and version=?");
    rtnObjectList.add(id);
    rtnObjectList.add(version);

    return Tuple.of(Joiner.on(String0.BLACK).join(sqlList), rtnObjectList);
  }

  public void updateStatement(@NonNull List<String> updateList, @NonNull List<Object> objectList) {
    Object o = null;
    for (String fieldName : fieldNameList.stream().filter(fieldName -> !"id".equals(fieldName)).collect(Collectors.toList())) {
      try {
        o = this.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)).invoke(this);
      } catch (Exception e) {
        o = null;
        log.warn(e.toString());
      }
      if (o != null && !Strings.isNullOrEmpty(o.toString())) {
        updateList.add(dbColumnMap.get(fieldName) + String20.EQUAL_QUESTION);
        if ("version".equals(fieldName)) {
          objectList.add((Integer) o + 1);
        } else {
          objectList.add(o);
        }
      }
    }
  }

  public void updateStatementExt(@NonNull List<String> updateList, @NonNull List<Object> objectList) {

  }

  //others
  public void fromStatement(@NonNull List<String> fromList, @NonNull List<Object> objectList) {
    fromList.add(fullTableName);
  }

  public void fromStatementExt(@NonNull List<String> fromList, @NonNull List<Object> objectList) {
  }

  public void groupByStatement(@NonNull List<String> groupByList, @NonNull List<Object> objectList) {
  }

  public void groupByStatementExt(@NonNull List<String> groupByList, @NonNull List<Object> objectList) {
  }

  public void havingStatement(@NonNull List<String> havingList, @NonNull List<Object> objectList) {
  }

  public void havingStatementExt(@NonNull List<String> havingList, @NonNull List<Object> objectList) {
  }

  public void orderByStatement(@NonNull List<String> orderByList, @NonNull List<Object> objectList) {
  }

  public void orderByStatementExt(@NonNull List<String> orderByList, @NonNull List<Object> objectList) {
  }

  public void whereStatement(@NonNull List<String> whereList, @NonNull List<Object> objectList) {
    Object o = null;
    for (String fieldName : fieldNameList) {
      try {
        o = this.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)).invoke(this);
      } catch (Exception e) {
        o = null;
        log.warn(e.toString());
      }
      if (o != null && !Strings.isNullOrEmpty(o.toString()) && skColumnMap.get(fieldName) != null && skColumnMap.get(fieldName).canWhere()) {
        if (skColumnMap.get(fieldName).useLike()) {
          whereList.add(dbColumnMap.get(fieldName) + " like ");
          objectList.add(String0.PERCENT + o.toString() + String0.PERCENT);
        } else {
          whereList.add(dbColumnMap.get(fieldName) + String20.EQUAL_QUESTION);
          objectList.add(o);
        }
      }
    }
  }

  public void whereStatementExt(@NonNull List<String> whereList, @NonNull List<Object> objectList) {
  }
}
