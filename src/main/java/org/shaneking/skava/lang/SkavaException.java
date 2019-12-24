package org.shaneking.skava.lang;

public class SkavaException extends RuntimeException {
  public SkavaException() {
    super();
  }

  public SkavaException(String message) {
    super(message);
  }

  public SkavaException(String message, Throwable cause) {
    super(message, cause);
  }

  public SkavaException(Throwable cause) {
    super(cause);
  }
}
