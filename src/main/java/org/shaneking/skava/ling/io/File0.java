package org.shaneking.skava.ling.io;

import com.google.common.base.Joiner;

import java.io.File;

public class File0 {
  public static final String ILLEGAL_FILENAME_REGEX = "[{/\\\\:*?\"<>|}]";

  public static final int BUFFER_SIZE = 256 * 1024;

  public static File join(String... strings) {
    return join(File.separator, strings);
  }

  public static File join(File parent, String... strings) {
    return join(File.separator, parent, strings);
  }

  public static File join(String separator, String... strings) {
    return new File(Joiner.on(separator).join(strings));
  }

  public static File join(String separator, File parent, String... strings) {
    return new File(parent, Joiner.on(separator).join(strings));
  }
}
