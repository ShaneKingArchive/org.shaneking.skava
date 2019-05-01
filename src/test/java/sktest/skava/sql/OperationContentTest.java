package sktest.skava.sql;

import org.junit.Test;
import org.shaneking.skava.sql.OperationContent;
import sktest.skava.SKUnit;

public class OperationContentTest extends SKUnit {
  @Test
  public void setter() {
    skPrint(new OperationContent().setBw("").setCs("").setEw("").setOp(""));
  }

}
