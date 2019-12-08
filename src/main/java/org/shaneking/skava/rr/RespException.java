package org.shaneking.skava.rr;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.shaneking.skava.lang.SkavaException;

@Accessors(chain = true)
@ToString
public class RespException extends SkavaException {
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
