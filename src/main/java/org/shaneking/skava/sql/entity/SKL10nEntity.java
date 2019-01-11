/*
 * @(#)SKL10nEntity.java		Created at 2017/9/17
 *
 * Copyright (c) ShaneKing All rights reserved.
 * ShaneKing PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.shaneking.skava.sql.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.shaneking.skava.sql.annotation.SKColumn;

@Accessors(chain = true)
@ToString(callSuper = true, includeFieldNames = true)
public class SKL10nEntity<E> extends SKEntity<E> {
  /**
   * @see org.shaneking.skava.ling.util.Date0#H_MI_S
   */
  @Getter
  @Setter
  @SKColumn(length = 10)
  private String createTimezone;

  /**
   * @see org.shaneking.skava.ling.util.Date0#H_MI_S
   */
  @Getter
  @Setter
  @SKColumn(length = 10)
  private String lastModifyTimezone;

  /**
   * @see org.shaneking.skava.ling.util.Date0#H_MI_S
   */
  @Getter
  @Setter
  @SKColumn(length = 10)
  private String invalidTimezone;
}
