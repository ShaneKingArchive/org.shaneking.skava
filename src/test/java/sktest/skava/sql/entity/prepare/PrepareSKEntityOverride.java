package sktest.skava.sql.entity.prepare;

import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.shaneking.skava.ling.lang.String0;
import org.shaneking.skava.sql.Keyword0;
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
  public void havingStatementExt(@NonNull List havingList, @NonNull List list) {
    havingList.add("version > ?");
    list.add(this.getVersion());
  }

  @Override
  public void orderByStatementExt(@NonNull List orderByList, @NonNull List list) {
    orderByList.add("version");
  }

  @Override
  public List<OperationContent> findOperationContentList(String fieldName) {
    if ("createUserId".equals(fieldName)) {
      return Lists.newArrayList(new OperationContent().setOp(Keyword0.IN).setCl(Lists.newArrayList("1", "a", ",")));
    } else if ("createDatetime".equals(fieldName)) {
      return Lists.newArrayList(new OperationContent().setOp(Keyword0.BETWEEN).setCl(Lists.newArrayList("1949-10-01")));
    } else if ("invalidDatetime".equals(fieldName)) {
      return Lists.newArrayList(new OperationContent().setOp(Keyword0.BETWEEN).setCl(Lists.newArrayList("1949-10-01", "1996-07")));
    } else if ("lastModifyDatetime".equals(fieldName)) {
      return Lists.newArrayList(new OperationContent().setBw(String0.PERCENT).setCs("1949-10-01").setEw(String0.PERCENT).setOp("like"));
    }
    return super.findOperationContentList(fieldName);
  }
}
