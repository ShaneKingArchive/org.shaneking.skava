package sktest.skava.ling.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.ling.io.AC0;
import org.shaneking.skava.ling.util.UUID0;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class AC0Test {
  @Test
  public void close() throws IOException {
    BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(File0Test.TEST_FOLDER, File0Test.TEST_TXT));
    Assert.assertTrue(AC0.close(bufferedReader));
    Assert.assertTrue(AC0.close(null));
  }

  @Test
  public void close3() {
    RandomAccessFile randomAccessFile = null;
    FileChannel fileChannel = null;
    FileLock fileLock = null;
    try {
      randomAccessFile = new RandomAccessFile(Paths.get(File0Test.TEST_FOLDER, File0Test.TEST_TXT).toFile(), "rw");
      fileChannel = randomAccessFile.getChannel();
      fileLock = fileChannel.tryLock();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      AC0.close(fileLock, 3);
      AC0.close(fileChannel);
      AC0.close(randomAccessFile);
    }
  }

  @Test
  public void closeTrue() {
    FileOutputStream fos = null;
    OutputStreamWriter osw = null;
    BufferedWriter bw = null;
    try {
      fos = new FileOutputStream(Paths.get(File0Test.TEST_FOLDER, File0Test.TEST_TXT).toFile());
      osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
      bw = new BufferedWriter(osw);

      bw.write(UUID0.l19());

      Assert.assertTrue(AC0.close(bw));//will close with flush
      Assert.assertTrue(AC0.close(osw));
      Assert.assertTrue(AC0.close(fos));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      AC0.close(bw);
      AC0.close(osw);
      AC0.close(fos);
    }
  }

  @Test
  public void closeFalse() {
    FileOutputStream fos = null;
    OutputStreamWriter osw = null;
    BufferedWriter bw = null;
    try {
      fos = new FileOutputStream(Paths.get(File0Test.TEST_FOLDER, File0Test.TEST_TXT).toFile());
      osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
      bw = new BufferedWriter(osw);

      bw.write(UUID0.l19());

      Assert.assertTrue(AC0.close(fos));
      Assert.assertTrue(AC0.close(osw));
      Assert.assertFalse(AC0.close(bw));//close exception because stream is closed
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      AC0.close(bw);
      AC0.close(osw);
      AC0.close(fos);
    }
  }

  @Test
  public void closeFalse3True() {
    FileOutputStream fos = null;
    OutputStreamWriter osw = null;
    BufferedWriter bw = null;
    try {
      fos = new FileOutputStream(Paths.get(File0Test.TEST_FOLDER, File0Test.TEST_TXT).toFile());
      osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
      bw = new BufferedWriter(osw);

      bw.write(UUID0.l19());

      Assert.assertTrue(AC0.close(fos));
      Assert.assertTrue(AC0.close(osw));
      Assert.assertTrue(AC0.close(bw, 3));//close exception because stream is closed
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      AC0.close(bw);
      AC0.close(osw);
      AC0.close(fos);
    }
  }
}
