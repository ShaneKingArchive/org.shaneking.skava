package sktest.skava.lang;

import org.junit.Test;
import org.shaneking.skava.lang.SkavaException;

public class SkavaExceptionTest {

  @Test(expected = SkavaException.class)
  public void constructor() {
    throw new SkavaException();
  }

  @Test(expected = SkavaException.class)
  public void constructorMessage() {
    throw new SkavaException("message");
  }

  @Test(expected = SkavaException.class)
  public void constructorMessageCause() {
    throw new SkavaException("massage", new SkavaException());
  }

  @Test(expected = SkavaException.class)
  public void constructorCause() {
    throw new SkavaException(new SkavaException());
  }
}
