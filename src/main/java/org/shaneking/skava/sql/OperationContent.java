package org.shaneking.skava.sql;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@ToString(includeFieldNames = true)
public class OperationContent {
  //will trans in request and response, so abbreviation
  @Getter
  @Setter
  private String op;//between,>,like...
  @Getter
  @Setter
  private List<String> cl;//content List
  @Getter
  @Setter
  private String cs;//content String
  @Getter
  @Setter
  private String bw;//beginWith
  @Getter
  @Setter
  private String ew;//endWith
}
