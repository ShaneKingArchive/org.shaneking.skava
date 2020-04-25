package sktest.skava.lang;

import com.google.common.base.Joiner;
import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.lang.Runtime0;
import org.shaneking.skava.lang.String0;

public class Runtime0Test {
  @Test
  public void exec() {
    Assert.assertEquals("===B:errorStream=false,value4pause=false,pauseFlag=null===;123;===E:errorStream=false,value4pause=false,pauseFlag=null===;===B:errorStream=true,value4pause=false,pauseFlag=null===;===E:errorStream=true,value4pause=false,pauseFlag=null===;process.waitFor()=0", Joiner.on(String0.SEMICOLON).join(Runtime0.exec("echo 123")));
    Assert.assertEquals("===B:errorStream=false,value4pause=true,pauseFlag=>read -n 1 -p===;123;===E:errorStream=false,value4pause=true,pauseFlag=>read -n 1 -p===;===B:errorStream=true,value4pause=true,pauseFlag=>read -n 1 -p===;===E:errorStream=true,value4pause=true,pauseFlag=>read -n 1 -p===;process.waitFor()=0", Joiner.on(String0.SEMICOLON).join(Runtime0.exec("echo 123", true, Runtime0.PAUSE_FLAG_SHELL)));
    Assert.assertEquals("===B:errorStream=false,value4pause=false,pauseFlag=>read -n 1 -p===;123;===E:errorStream=false,value4pause=false,pauseFlag=>read -n 1 -p===;===B:errorStream=true,value4pause=false,pauseFlag=>read -n 1 -p===;===E:errorStream=true,value4pause=false,pauseFlag=>read -n 1 -p===;process.waitFor()=0", Joiner.on(String0.SEMICOLON).join(Runtime0.exec("echo 123", false, Runtime0.PAUSE_FLAG_SHELL)));
    Assert.assertEquals("java.io.IOException: Cannot run program \"skUnknownCommand\": error=2, No such file or directory", Joiner.on(String0.SEMICOLON).join(Runtime0.exec("skUnknownCommand")));
    Assert.assertEquals("java.io.IOException: Cannot run program \"skUnknownCommand\": error=2, No such file or directory", Joiner.on(String0.SEMICOLON).join(Runtime0.exec("skUnknownCommand", true, Runtime0.PAUSE_FLAG_SHELL)));
    Assert.assertEquals("java.io.IOException: Cannot run program \"skUnknownCommand\": error=2, No such file or directory", Joiner.on(String0.SEMICOLON).join(Runtime0.exec("skUnknownCommand", false, Runtime0.PAUSE_FLAG_SHELL)));
  }
}
