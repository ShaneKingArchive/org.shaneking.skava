package sktest.skava.t3.jackson.prepare;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@ToString
public class PrepareOM3 {

  @Getter
  @Setter
  private String s1;

  @Getter
  @Setter
  private Integer i1;
}
