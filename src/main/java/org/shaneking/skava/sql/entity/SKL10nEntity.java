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

import javax.persistence.Column;

@Accessors(chain = true)
@ToString(callSuper = true, includeFieldNames = true)
public class SKL10nEntity<J> extends SKEntity<J> {
  /**
   * @see org.shaneking.skava.ling.util.Date0#H_MI_S
   */
  @Getter
  @Setter
  @Column(length = 10)
  private String createTimezone;

  /**
   * @see org.shaneking.skava.ling.util.Date0#H_MI_S
   */
  @Getter
  @Setter
  @Column(length = 10)
  private String invalidTimezone;

  /**
   * @see org.shaneking.skava.ling.util.Date0#H_MI_S
   */
  @Getter
  @Setter
  @Column(length = 10)
  private String lastModifyTimezone;
}
