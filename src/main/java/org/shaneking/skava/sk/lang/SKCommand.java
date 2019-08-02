package org.shaneking.skava.sk.lang;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SKCommand {
  public static final long DEFAULT_TIMEOUT_MINUTES = 33;
  public static final String PAUSE_FLAG_CMD = ">pause";
  public static final String PAUSE_FLAG_SHELL = ">read -n 1 -p";

  public static boolean exec(String command) {
    return exec(command, DEFAULT_TIMEOUT_MINUTES);
  }

  public static boolean exec(String command, long timeout) {
    return exec(command, timeout, false, null);
  }

  public static boolean exec(String command, boolean value4pause, String pauseFlag) {
    return exec(command, DEFAULT_TIMEOUT_MINUTES, value4pause, pauseFlag);
  }

  public static boolean exec(String command, long timeout, boolean value4pause, String pauseFlag) {
    log.info(command);
    boolean rtnBoolean = true;

    if (!Strings.isNullOrEmpty(command)) {
      Process process = null;
      Future<Boolean> iFuture = null;
      Future<Boolean> eFuture = null;
      try {
        process = Runtime.getRuntime().exec(command);
        ExecutorService es = Executors.newFixedThreadPool(2);
        iFuture = es.submit(new SKCommandCallable(process.getInputStream(), false, value4pause, pauseFlag));
        eFuture = es.submit(new SKCommandCallable(process.getErrorStream(), true, value4pause, pauseFlag));
        rtnBoolean = iFuture.get(timeout, TimeUnit.MINUTES) && eFuture.get(timeout, TimeUnit.MINUTES) && process.waitFor() == 0;
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        rtnBoolean = false;
      } finally {
        if (null != iFuture) {
          iFuture.cancel(true);
        }
        if (null != eFuture) {
          eFuture.cancel(true);
        }
        if (null != process) {
          process.destroy();
        }
      }
    }

    return rtnBoolean;
  }
}
