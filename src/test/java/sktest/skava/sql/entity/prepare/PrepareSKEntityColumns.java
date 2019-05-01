package sktest.skava.sql.entity.prepare;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.shaneking.skava.sql.entity.SKEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Accessors(chain = true)
@ToString(callSuper = true, includeFieldNames = true)
@Table
public class PrepareSKEntityColumns extends SKEntity {
  @Getter
  @Setter
  private String withoutAnnotation;

  @Getter
  @Setter
  @Column(length = 10)
  private String hasLength;

  @Getter
  @Setter
  @Column(name = "re_name")
  private String reName;
}
