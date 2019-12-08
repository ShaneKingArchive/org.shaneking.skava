package org.shaneking.skava.sk.lang;

public class SKRuntimeException extends RuntimeException {
  public SKRuntimeException() {
    super();
  }

  public SKRuntimeException(String message) {
    super(message);
  }

  public SKRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public SKRuntimeException(Throwable cause) {
    super(cause);
  }
}
