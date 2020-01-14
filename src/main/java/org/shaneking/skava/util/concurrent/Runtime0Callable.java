package org.shaneking.skava.util.concurrent;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.skava.io.AC0;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Locale;
import java.util.concurrent.Callable;

@Slf4j
public class Runtime0Callable implements Callable<Boolean> {
  @Getter
  private InputStream inputStream;
  @Getter
  private boolean errorStream;
  @Getter
  private boolean value4pause;//if meet pause, return true or false?
  @Getter
  private String pauseFlag;

  public Runtime0Callable(InputStream inputStream, boolean errorStream, boolean value4pause, String pauseFlag) {
    super();
    this.inputStream = inputStream;
    this.errorStream = errorStream;
    this.value4pause = value4pause;
    this.pauseFlag = pauseFlag;
  }

  @Override
  public Boolean call() throws Exception {
    boolean rtnBoolean = true;

    LineNumberReader lineNumberReader = null;
    InputStreamReader inputStreamReader = null;
    String line = null;

    try {
      inputStreamReader = new InputStreamReader(this.getInputStream());
      lineNumberReader = new LineNumberReader(inputStreamReader);

      if (Strings.isNullOrEmpty(this.getPauseFlag())) {
        while ((line = lineNumberReader.readLine()) != null) {
          if (this.isErrorStream()) {
            log.error(line);
          } else {
            log.info(line);
          }
        }
      } else {
        while ((line = lineNumberReader.readLine()) != null) {
          if (this.isErrorStream()) {
            log.error(line);
          } else {
            log.info(line);
          }
          if (line.toLowerCase(Locale.ENGLISH).contains(this.getPauseFlag())) {
            rtnBoolean = this.isValue4pause();
            break;
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      rtnBoolean = false;
    } finally {
      AC0.close(lineNumberReader);
      AC0.close(inputStreamReader);
    }

    return rtnBoolean;
  }
}

