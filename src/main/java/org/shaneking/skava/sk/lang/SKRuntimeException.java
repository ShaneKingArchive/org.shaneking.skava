package org.shaneking.skava.sk.lang;

public class SKRuntimeException extends RuntimeException {
  public SKRuntimeException(Throwable cause) {
    super(cause);
  }

  public SKRuntimeException(String message) {
    super(message);
  }
}
