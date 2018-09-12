/*
 * @(#)ZoneEntity.java		Created at 2018/7/4
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package test.skava.sql.entity;

import lombok.ToString;
import lombok.experimental.Accessors;
import org.shaneking.skava.sql.annotation.SKTable;
import org.shaneking.skava.sql.entity.SKL10nEntity;

@Accessors(chain = true)
@ToString(callSuper = true, includeFieldNames = true)
@SKTable
public class ZoneEntity extends SKL10nEntity {
}
