package sktest.skava.sql.entity.prepare;

import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.shaneking.skava.sql.entity.SKEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Accessors(chain = true)
@ToString(callSuper = true, includeFieldNames = true)
@Table
public class PrepareSKEntityColumnNoGetMethod extends SKEntity {
  @Setter
  @Column(length = 10)
  private String noGetMethod;
}
