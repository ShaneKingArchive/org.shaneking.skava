package org.shaneking.skava.sk.rr;

import lombok.Getter;
import lombok.Setter;

public class RespException extends RuntimeException {
  @Getter
  @Setter
  private Resp resp;

  public RespException(Resp resp) {
    super();
    this.resp = resp;
  }

  public RespException(Resp resp, String message) {
    super(message);
    this.resp = resp;
  }

  public RespException(Resp resp, String message, Throwable cause) {
    super(message, cause);
    this.resp = resp;
  }

  public RespException(Resp resp, Throwable cause) {
    super(cause);
    this.resp = resp;
  }
}
