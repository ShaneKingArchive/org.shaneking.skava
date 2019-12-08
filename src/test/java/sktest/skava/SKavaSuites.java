package sktest.skava;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sktest.skava.ling.LingSuites;
import sktest.skava.sk.SKSuites;
import sktest.skava.t3.T3Suites;

@RunWith(Suite.class)
@Suite.SuiteClasses({LingSuites.class, SKSuites.class, T3Suites.class})
public class SKavaSuites {
}
