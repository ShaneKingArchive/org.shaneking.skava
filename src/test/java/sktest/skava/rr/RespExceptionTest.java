package sktest.skava.rr;

import org.junit.Test;
import org.shaneking.skava.lang.SkavaException;
import org.shaneking.skava.rr.Resp;
import org.shaneking.skava.rr.RespException;

public class RespExceptionTest {

  @Test(expected = RespException.class)
  public void constructor() {
    throw new RespException(Resp.failed());
  }

  @Test(expected = RespException.class)
  public void constructorMessage() {
    throw new RespException(Resp.failed(), "message");
  }

  @Test(expected = RespException.class)
  public void constructorMessageCause() {
    throw new RespException(Resp.failed(), "massage", new SkavaException());
  }

  @Test(expected = RespException.class)
  public void constructorCause() {
    throw new RespException(Resp.failed(), new RespException(Resp.failed()).setResp(Resp.failed()));
  }
}
