package org.shaneking.skava.sql;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@ToString(includeFieldNames = true)
public class OperationContent {
  @Getter
  @Setter
  private String o;//between,>,like...
  @Getter
  @Setter
  private String c;
  @Getter
  @Setter
  private String bw;//beginWith
  @Getter
  @Setter
  private String ew;//endWith
}
