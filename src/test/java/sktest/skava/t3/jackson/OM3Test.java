package sktest.skava.t3.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;
import org.shaneking.skava.sk.lang.SKRuntimeException;
import org.shaneking.skava.t3.jackson.OM3;
import sktest.skava.SKUnit;
import sktest.skava.t3.jackson.prepare.PrepareOM3;
import sktest.skava.t3.jackson.prepare.PrepareOM3NoGetterSetter;
import sktest.skava.t3.jackson.prepare.PrepareOM3T;

import java.util.ArrayList;

public class OM3Test extends SKUnit {

  @Test
  public void readValue1_List() {
    Assert.assertEquals("[]", OM3.writeValueAsString(new ArrayList<>()));
  }

  @Test(expected = SKRuntimeException.class)
  public void readValue1_Object() {
    Assert.assertEquals("{}", OM3.writeValueAsString(new Object()));
  }

  @Test(expected = SKRuntimeException.class)
  public void om() {
    Assert.assertEquals("", OM3.writeValueAsString(OM3.om()));
  }

  @Test
  public void createObjectNode() {
    Assert.assertEquals("{}", OM3.writeValueAsString(OM3.createObjectNode()));
  }

  @Test(expected = NullPointerException.class)
  public void createObjectNode_a1() {
    Assert.assertEquals("{}", OM3.writeValueAsString(OM3.createObjectNode(null)));
  }

  @Test
  public void readValue_a1_PrepareOM3() {
    PrepareOM3 prepare = new PrepareOM3().setS1("s1");
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(prepare));
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue(prepare)));
    Assert.assertSame(prepare, OM3.readValue(prepare));
  }

  @Test
  public void readValue_a1_PrepareOM3_noValue() {
    PrepareOM3 prepare = new PrepareOM3();
    Assert.assertEquals("{}", OM3.writeValueAsString(prepare));
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.readValue(prepare)));
    Assert.assertNull(OM3.readValue(prepare));
  }

  @Test(expected = SKRuntimeException.class)
  public void readValue_a1_PrepareOM3NoGetterSetter() {
    PrepareOM3NoGetterSetter prepare = new PrepareOM3NoGetterSetter();
    Assert.assertEquals("{}", OM3.writeValueAsString(prepare));
//    Assert.assertEquals("{}", OM3.writeValueAsString(OM3.readValue(prepare)));
//    Assert.assertSame(prepare, OM3.readValue(prepare));
  }

  @Test
  public void readValue_a2_1() {
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue("{\"s1\":\"s1\"}", PrepareOM3.class)));
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue("{\"s1\":\"s1\"}", PrepareOM3T.class)));
  }

  @Test(expected = NullPointerException.class)
  public void readValue_a3_1() {
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue(null, "{\"s1\":\"s1\"}", PrepareOM3.class)));
//    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue(null,"{\"s1\":\"s1\"}", PrepareOM3T.class)));
  }

  @Test
  public void readValue_a2_2() {
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue("{\"s1\":\"s1\"}", new TypeReference<PrepareOM3>() {
    })));
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue("{\"s1\":\"s1\"}", new TypeReference<PrepareOM3T<String>>() {
    })));
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.readValue("{\"t\":\"s1\"}", new TypeReference<PrepareOM3T<String>>() {
    })));
  }

  @Test(expected = NullPointerException.class)
  public void readValue_a3_2() {
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue(null, "{\"s1\":\"s1\"}", new TypeReference<PrepareOM3>() {
    })));
  }

  @Test(expected = SKRuntimeException.class)
  public void readValue_a4_false() {
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.readValue(OM3.om(), "{\"s1\":\"s1\"}", PrepareOM3NoGetterSetter.class, false)));
  }

  @Test
  public void readValue_a4_true() {
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.readValue(OM3.om(), "{\"s1\":\"s1\"}", PrepareOM3NoGetterSetter.class, true)));
  }

  @Test(expected = SKRuntimeException.class)
  public void readValue_a4typeRef_false() {
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.readValue(OM3.om(), "{\"s1\":\"s1\"}", new TypeReference<PrepareOM3NoGetterSetter>() {
    }, false)));
  }

  @Test
  public void readValue_a4typeRef_true() {
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.readValue(OM3.om(), "{\"s1\":\"s1\"}", new TypeReference<PrepareOM3NoGetterSetter>() {
    }, true)));
  }

  @Test
  public void treeToValue_a2() {
    ObjectNode objectNode = OM3.createObjectNode();
    objectNode.put("s1", "s1");
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.treeToValue(objectNode, PrepareOM3.class)));
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.treeToValue(objectNode, PrepareOM3T.class)));
  }

  @Test(expected = NullPointerException.class)
  public void treeToValue_a3() {
    ObjectNode objectNode = OM3.createObjectNode();
    objectNode.put("s1", "s1");
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.treeToValue(null, objectNode, PrepareOM3.class)));
//    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.treeToValue(null, objectNode, PrepareOM3T.class)));
  }

  @Test(expected = SKRuntimeException.class)
  public void treeToValue_a4_false() {
    ObjectNode objectNode = OM3.createObjectNode();
    objectNode.put("i1", "i1");
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.treeToValue(OM3.om(), objectNode, PrepareOM3.class, false)));
//    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.treeToValue(null, objectNode, PrepareOM3T.class)));
  }

  @Test
  public void treeToValue_a4_true() {
    ObjectNode objectNode = OM3.createObjectNode();
    objectNode.put("i1", "i1");
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.treeToValue(OM3.om(), objectNode, PrepareOM3.class, true)));
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.treeToValue(OM3.om(), objectNode, PrepareOM3T.class, true)));
  }

  @Test
  public void valueToTree_a1() {
    PrepareOM3 prepare = new PrepareOM3();
    Assert.assertEquals("{}", OM3.writeValueAsString(OM3.valueToTree(prepare)));

  }

  @Test(expected = NullPointerException.class)
  public void valueToTree_a2() {
    PrepareOM3 prepare = new PrepareOM3();
    Assert.assertEquals("{}", OM3.writeValueAsString(OM3.valueToTree(null, prepare)));
  }

  @Test(expected = NullPointerException.class)
  public void writeValueAsString_a2() {
    PrepareOM3 prepare = new PrepareOM3();
    Assert.assertEquals("{}", OM3.writeValueAsString(null, prepare));
  }

  @Test
  public void writeValueAsString_a3() {
    PrepareOM3NoGetterSetter prepare = new PrepareOM3NoGetterSetter();
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.writeValueAsString(OM3.om(), prepare, true)));
  }
}
