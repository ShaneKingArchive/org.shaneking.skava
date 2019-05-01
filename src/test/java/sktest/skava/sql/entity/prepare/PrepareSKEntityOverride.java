package sktest.skava.sql.entity.prepare;

import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.shaneking.skava.ling.lang.String0;
import org.shaneking.skava.sql.OperationContent;
import org.shaneking.skava.sql.entity.SKEntity;

import javax.persistence.Table;
import java.util.List;

@Accessors(chain = true)
@ToString(callSuper = true, includeFieldNames = true)
@Table
public class PrepareSKEntityOverride extends SKEntity {
  @Override
  public void groupByStatementExt(@NonNull List groupByList, @NonNull List list) {
    groupByList.add("version");
  }

  @Override
  public String havingStatementExt(@NonNull String havingExpression, @NonNull List list) {
    list.add(this.getVersion());
    return "version > ?";
  }

  @Override
  public void orderByStatementExt(@NonNull List orderByList, @NonNull List list) {
    orderByList.add("version");
  }

  @Override
  public List<OperationContent> findOperationContentList(String fieldName) {
    if ("createDatetime".equals(fieldName)) {
      return Lists.newArrayList(new OperationContent().setBw(String0.PERCENT).setC("1949-10-01").setEw(String0.PERCENT).setO("like"));
    }
    return super.findOperationContentList(fieldName);
  }
}
