package sktest.skava.sk;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sktest.skava.sk.crypto.CryptoSuites;
import sktest.skava.sk.lang.LangSuites;
import sktest.skava.sk.persistence.PersistenceSuites;
import sktest.skava.sk.rr.RRSuites;

@RunWith(Suite.class)
@Suite.SuiteClasses({PersistenceSuites.class, CryptoSuites.class, LangSuites.class, RRSuites.class})
public class SKSuites {
}
