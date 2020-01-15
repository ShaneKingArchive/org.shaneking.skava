package sktest.skava;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sktest.skava.crypto.Cipher0Test;
import sktest.skava.io.IoSuites;
import sktest.skava.lang.LangSuites;
import sktest.skava.persistence.PersistenceSuites;
import sktest.skava.rr.RRSuites;
import sktest.skava.security.SecuritySuites;
import sktest.skava.time.TimeSuites;
import sktest.skava.util.UtilSuites;

@RunWith(Suite.class)
@Suite.SuiteClasses({Cipher0Test.class, IoSuites.class, LangSuites.class, PersistenceSuites.class, RRSuites.class, SecuritySuites.class, TimeSuites.class, UtilSuites.class})
public class SKavaSuites {
}
