/*
 * @(#)SKEntity.java		Created at 2017/9/10
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import org.shaneking.skava.sql.Keyword0;
import org.shaneking.skava.sql.OperationContent;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Accessors(chain = true)
@Slf4j
@ToString(includeFieldNames = true)
public class SKEntity<J> {
  @JsonIgnore
  @Getter
  private final Map<String, Column> columnMap = Maps.newHashMap();
  @JsonIgnore
  @Getter
  private final Map<String, String> dbColumnMap = Maps.newHashMap();
  @JsonIgnore
  @Getter
  private final List<String> fieldNameList = Lists.newArrayList();

  @JsonIgnore
  @Getter
  @Setter
  private String fullTableName;
  @JsonIgnore
  @Getter
  @Setter
  private Table table;

  /**
   * @see org.shaneking.skava.ling.util.Date0#DATE_TIME
   */
  @Getter
  @Setter
  @Column(length = 20, updatable = false)
  private String createDatetime;

  @Getter
  @Setter
  @Column(length = 40, updatable = false)
  private String createUserId;

  /**
   * J maybe fastjson,gson,jackson...
   * <p> extJson:
   * when result:The json object of extJsonStr
   * when table operation(insert/delete/update/select):
   * <blockquote><pre>
   *     {
   *         createDatetime:{
   *             op:'between',
   *             c:['2017-09-10','2019-04-27']
   *         },
   *         invalidDatetime:{
   *             op:'>',
   *             c:'2017-09-10'
   *         },
   *         lastModifyDatetime:{
   *             op:'like',
   *             c:'2019-04-27'
   *         }
   *     }
   * </pre></blockquote>
   */
  @Getter
  @Setter
  private J extJson;

  /**
   * !important, can't be criteria for query, no index
   */
  @Getter
  @Setter
  @Column
  private String extJsonStr;

  @Getter
  @Setter
  @Column(length = 40, updatable = false)
  private String id;

  @Getter
  @Setter
  @Column(length = 10)
  private String invalid;//Y|N(default)

  /**
   * @see org.shaneking.skava.ling.util.Date0#DATE_TIME
   */
  @Getter
  @Setter
  @Column(length = 20)
  private String invalidDatetime;

  @Getter
  @Setter
  @Column(length = 40)
  private String invalidUserId;

  /**
   * @see org.shaneking.skava.ling.util.Date0#DATE_TIME
   */
  @Getter
  @Setter
  @Column(length = 20)
  private String lastModifyDatetime;

  @Getter
  @Setter
  @Column(length = 40)
  private String lastModifyUserId;

  @Getter
  @Setter
  @Column(length = 20)
  private Integer version = 1;

  public SKEntity() {
    initTableInfo();
    initColumnInfo(this.getClass());
  }

  public List<OperationContent> findOperationContentList(String fieldName) {
    List<OperationContent> rtnList = Lists.newArrayList();
    //implements by sub entity
    return rtnList;
  }

  public void initColumnInfo(Class<? extends Object> skEntityClass) {
    for (Field field : skEntityClass.getDeclaredFields()) {
      Column column = field.getAnnotation(Column.class);
      if (column != null && this.getFieldNameList().indexOf(field.getName()) == -1) {
        this.getColumnMap().put(field.getName(), column);
        this.getDbColumnMap().put(field.getName(), Strings.isNullOrEmpty(column.name()) ? String0.upper2lower(field.getName()) : column.name());
        this.getFieldNameList().add(field.getName());
      }
    }
    if (SKEntity.class.isAssignableFrom(skEntityClass.getSuperclass())) {
      initColumnInfo(skEntityClass.getSuperclass());
    }
  }

  public void initTableInfo() {
    if (this.getTable() == null) {
      this.setTable(this.getClass().getAnnotation(Table.class));
    }
    this.setFullTableName(Strings.isNullOrEmpty(this.getTable().schema()) ? String0.EMPTY : this.getTable().schema() + String0.DOT);
    if (Strings.isNullOrEmpty(this.getTable().name())) {
      String classTableName = String0.upper2lower(Lists.reverse(Lists.newArrayList(this.getClass().getName().split(String20.BACKSLASH_DOT))).get(0));
      this.setFullTableName(this.getFullTableName() + "t" + (classTableName.startsWith(String0.UNDERLINE) ? classTableName : String0.UNDERLINE + classTableName));
    } else {
      this.setFullTableName(this.getFullTableName() + this.getTable().name());
    }
  }

  //curd
  public int delete() {
    int rtnInt = 0;
    //implements by sub entity
    return rtnInt;
  }

  public int insert() {
    int rtnInt = 0;
    //implements by sub entity
    return rtnInt;
  }

  public int insertOrUpdateById() {
    int rtnInt = 0;
    //implements by sub entity
    return rtnInt;
  }

  public Tuple.Pair<String, List<Object>> insertSql() {
    List<Object> rtnObjectList = Lists.newArrayList();

    List<String> insertList = Lists.newArrayList();
    insertStatement(insertList, rtnObjectList);
    insertStatementExt(insertList, rtnObjectList);

    List<String> sqlList = Lists.newArrayList();
    sqlList.add("insert into");
    sqlList.add(this.getFullTableName());
    sqlList.add(String0.OPEN_PARENTHESIS + Joiner.on(String0.COMMA).join(insertList) + String0.CLOSE_PARENTHESIS);
    sqlList.add("values");
    sqlList.add(String0.OPEN_PARENTHESIS + Strings.repeat(String0.COMMA + String0.QUESTION, insertList.size()).substring(1) + String0.CLOSE_PARENTHESIS);

    return Tuple.of(Joiner.on(String0.BLACK).join(sqlList), rtnObjectList);
  }

  public void insertStatement(@NonNull List<String> insertList, @NonNull List<Object> objectList) {
    Object o;
    for (String fieldName : this.getFieldNameList()) {
      try {
        o = this.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)).invoke(this);
      } catch (Exception e) {
        o = null;
        log.warn(e.toString());
      }
      if (o != null && !Strings.isNullOrEmpty(o.toString())) {
        insertList.add(this.getDbColumnMap().get(fieldName));
        objectList.add(o);
      }
    }
  }

  public void insertStatementExt(@NonNull List<String> insertList, @NonNull List<Object> objectList) {
    //implements by sub entity
  }

  public List<? extends SKEntity> select() {
    List<? extends SKEntity> rtnList = Lists.newArrayList();
    //implements by sub entity
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
    selectList.addAll(this.getFieldNameList().stream().map((String fieldName) -> this.getDbColumnMap().get(fieldName)).collect(Collectors.toList()));
  }

  public void selectStatementExt(@NonNull List<String> selectList, @NonNull List<Object> objectList) {
    //implements by sub entity
  }

  public int update() {
    int rtnInt = 0;
    //implements by sub entity
    return rtnInt;
  }

  public Tuple.Pair<String, List<Object>> updateByIdSql() {
    List<Object> rtnObjectList = Lists.newArrayList();

    List<String> updateList = Lists.newArrayList();
    updateStatement(updateList, rtnObjectList);
    updateStatementExt(updateList, rtnObjectList);

    List<String> sqlList = Lists.newArrayList();
    sqlList.add("update");
    sqlList.add(this.getFullTableName());
    sqlList.add("set");
    sqlList.add(Joiner.on(String0.COMMA).join(updateList));
    sqlList.add("where");
    sqlList.add("id=?");
    rtnObjectList.add(this.getId());

    return Tuple.of(Joiner.on(String0.BLACK).join(sqlList), rtnObjectList);
  }

  public Tuple.Pair<String, List<Object>> updateByIdAndVersionSql() {
    Tuple.Pair<String, List<Object>> rtn = this.updateByIdSql();
    if (this.getVersion() != null) {
      List<Object> rtnObjectList = Tuple.getSecond(rtn);
      rtnObjectList.add(this.getVersion());
      rtn = Tuple.of(Joiner.on(String0.BLACK).join(Tuple.getFirst(rtn), "and version=?"), rtnObjectList);
    }
    return rtn;
  }

  public void updateStatement(@NonNull List<String> updateList, @NonNull List<Object> objectList) {
    Object o = null;
    for (String fieldName : this.getFieldNameList().stream().filter(fieldName -> !"id".equals(fieldName) && this.getColumnMap().get(fieldName).updatable()).collect(Collectors.toList())) {
      try {
        o = this.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)).invoke(this);
      } catch (Exception e) {
        o = null;
        log.warn(e.toString());
      }
      if (o != null && !Strings.isNullOrEmpty(o.toString())) {
        updateList.add(this.getDbColumnMap().get(fieldName) + String20.EQUAL_QUESTION);
        if ("version".equals(fieldName)) {
          objectList.add((Integer) o + 1);
        } else {
          objectList.add(o);
        }
      }
    }
  }

  public void updateStatementExt(@NonNull List<String> updateList, @NonNull List<Object> objectList) {
    //implements by sub entity
  }

  //others
  public void fromStatement(@NonNull List<String> fromList, @NonNull List<Object> objectList) {
    fromList.add(this.getFullTableName());
  }

  public void fromStatementExt(@NonNull List<String> fromList, @NonNull List<Object> objectList) {
    //implements by sub entity
  }

  public void groupByStatement(@NonNull List<String> groupByList, @NonNull List<Object> objectList) {
    //implements by sub entity
  }

  public void groupByStatementExt(@NonNull List<String> groupByList, @NonNull List<Object> objectList) {
    //implements by sub entity
  }

  public void havingStatement(@NonNull List<String> havingByList, @NonNull List<Object> objectList) {
    //implements by sub entity
  }

  public void havingStatementExt(@NonNull List<String> havingByList, @NonNull List<Object> objectList) {
    //implements by sub entity
  }

  public void orderByStatement(@NonNull List<String> orderByList, @NonNull List<Object> objectList) {
  }

  public void orderByStatementExt(@NonNull List<String> orderByList, @NonNull List<Object> objectList) {
    //implements by sub entity
  }

  public void whereStatement(@NonNull List<String> whereList, @NonNull List<Object> objectList) {
    Object o = null;
    for (String fieldName : this.getFieldNameList()) {
      try {
        o = this.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)).invoke(this);
      } catch (Exception e) {
        o = null;
        log.warn(e.toString());
      }
      if (this.getColumnMap().get(fieldName) != null) {
        if (o != null && !Strings.isNullOrEmpty(o.toString())) {
          whereList.add(this.getDbColumnMap().get(fieldName) + String20.EQUAL_QUESTION);
          objectList.add(o);
        }
        for (OperationContent oc : this.findOperationContentList(fieldName)) {
          if (Keyword0.BETWEEN.equalsIgnoreCase(oc.getOp())) {
            if (oc.getCl().size() == 2) {
              whereList.add(this.getDbColumnMap().get(fieldName) + String0.BLACK + oc.getOp() + String0.BLACK + String0.QUESTION + String0.BLACK + Keyword0.AND + String0.BLACK + String0.QUESTION);
              objectList.addAll(oc.getCl());
            }
          } else if (Keyword0.IN.equalsIgnoreCase(oc.getOp())) {
            whereList.add(this.getDbColumnMap().get(fieldName) + String0.BLACK + oc.getOp() + String0.BLACK + String0.OPEN_PARENTHESIS + Joiner.on(String0.COMMA).join(Collections.nCopies(oc.getCl().size(), String0.QUESTION)) + String0.CLOSE_PARENTHESIS);
            objectList.addAll(oc.getCl());
          } else {
            whereList.add(this.getDbColumnMap().get(fieldName) + String0.BLACK + oc.getOp() + String0.BLACK + String0.QUESTION);
            objectList.add(Strings.nullToEmpty(oc.getBw()) + oc.getCs() + oc.getEw());
          }
        }
      }
    }
  }

  public void whereStatementExt(@NonNull List<String> whereList, @NonNull List<Object> objectList) {
    //implements by sub entity
  }
}
