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
  public void readValueA1List() {
    Assert.assertEquals("[]", OM3.writeValueAsString(new ArrayList<>()));
  }

  @Test(expected = SKRuntimeException.class)
  public void readValueA1Object() {
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
  public void createObjectNodeA1() {
    Assert.assertEquals("{}", OM3.writeValueAsString(OM3.createObjectNode(null)));
  }

  @Test
  public void readValueA1PrepareOM3() {
    PrepareOM3 prepare = new PrepareOM3().setS1("s1");
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(prepare));
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue(prepare)));
    Assert.assertSame(prepare, OM3.readValue(prepare));
  }

  @Test
  public void readValueA1PrepareOM3NoValue() {
    PrepareOM3 prepare = new PrepareOM3();
    Assert.assertEquals("{}", OM3.writeValueAsString(prepare));
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.readValue(prepare)));
    Assert.assertNull(OM3.readValue(prepare));
  }

  @Test(expected = SKRuntimeException.class)
  public void readValueA1PrepareOM3NoGetterSetter() {
    PrepareOM3NoGetterSetter prepare = new PrepareOM3NoGetterSetter();
    Assert.assertEquals("{}", OM3.writeValueAsString(prepare));
//    Assert.assertEquals("{}", OM3.writeValueAsString(OM3.readValue(prepare)));
//    Assert.assertSame(prepare, OM3.readValue(prepare));
  }

  @Test
  public void readValueA21() {
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue("{\"s1\":\"s1\"}", PrepareOM3.class)));
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue("{\"s1\":\"s1\"}", PrepareOM3T.class)));
  }

  @Test(expected = NullPointerException.class)
  public void readValueA31() {
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue(null, "{\"s1\":\"s1\"}", PrepareOM3.class)));
//    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue(null,"{\"s1\":\"s1\"}", PrepareOM3T.class)));
  }

  @Test
  public void readValueA22JavaType() {
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue("{\"s1\":\"s1\"}", OM3.om().getTypeFactory().constructType(PrepareOM3.class))));
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue("{\"s1\":\"s1\"}", OM3.om().getTypeFactory().constructParametricType(PrepareOM3T.class, String.class))));
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.readValue("{\"t\":\"s1\"}", OM3.om().getTypeFactory().constructParametricType(PrepareOM3T.class, String.class))));
  }

  @Test
  public void readValueA22() {
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue("{\"s1\":\"s1\"}", new TypeReference<PrepareOM3>() {
    })));
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue("{\"s1\":\"s1\"}", new TypeReference<PrepareOM3T<String>>() {
    })));
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.readValue("{\"t\":\"s1\"}", new TypeReference<PrepareOM3T<String>>() {
    })));
  }

  @Test(expected = NullPointerException.class)
  public void readValueA32() {
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.readValue(null, "{\"s1\":\"s1\"}", new TypeReference<PrepareOM3>() {
    })));
  }

  @Test(expected = SKRuntimeException.class)
  public void readValueA4False() {
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.readValue(OM3.om(), "{\"s1\":\"s1\"}", PrepareOM3NoGetterSetter.class, false)));
  }

  @Test
  public void readValueA4True() {
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.readValue(OM3.om(), "{\"s1\":\"s1\"}", PrepareOM3NoGetterSetter.class, true)));
  }

  @Test(expected = SKRuntimeException.class)
  public void readValueA4TypeRefFalse() {
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.readValue(OM3.om(), "{\"s1\":\"s1\"}", new TypeReference<PrepareOM3NoGetterSetter>() {
    }, false)));
  }

  @Test
  public void readValueA4TypeRefTrue() {
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.readValue(OM3.om(), "{\"s1\":\"s1\"}", new TypeReference<PrepareOM3NoGetterSetter>() {
    }, true)));
  }

  @Test
  public void treeToValueA2() {
    ObjectNode objectNode = OM3.createObjectNode();
    objectNode.put("s1", "s1");
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.treeToValue(objectNode, PrepareOM3.class)));
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.treeToValue(objectNode, PrepareOM3T.class)));
  }

  @Test(expected = NullPointerException.class)
  public void treeToValueA3() {
    ObjectNode objectNode = OM3.createObjectNode();
    objectNode.put("s1", "s1");
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.treeToValue(null, objectNode, PrepareOM3.class)));
//    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.treeToValue(null, objectNode, PrepareOM3T.class)));
  }

  @Test(expected = SKRuntimeException.class)
  public void treeToValueA4False() {
    ObjectNode objectNode = OM3.createObjectNode();
    objectNode.put("i1", "i1");
    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.treeToValue(OM3.om(), objectNode, PrepareOM3.class, false)));
//    Assert.assertEquals("{\"s1\":\"s1\"}", OM3.writeValueAsString(OM3.treeToValue(null, objectNode, PrepareOM3T.class)));
  }

  @Test
  public void treeToValueA4Ture() {
    ObjectNode objectNode = OM3.createObjectNode();
    objectNode.put("i1", "i1");
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.treeToValue(OM3.om(), objectNode, PrepareOM3.class, true)));
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.treeToValue(OM3.om(), objectNode, PrepareOM3T.class, true)));
  }

  @Test
  public void valueToTreeA1() {
    PrepareOM3 prepare = new PrepareOM3();
    Assert.assertEquals("{}", OM3.writeValueAsString(OM3.valueToTree(prepare)));

  }

  @Test(expected = NullPointerException.class)
  public void valueToTreeA2() {
    PrepareOM3 prepare = new PrepareOM3();
    Assert.assertEquals("{}", OM3.writeValueAsString(OM3.valueToTree(null, prepare)));
  }

  @Test(expected = NullPointerException.class)
  public void writeValueAsStringA2() {
    PrepareOM3 prepare = new PrepareOM3();
    Assert.assertEquals("{}", OM3.writeValueAsString(null, prepare));
  }

  @Test
  public void writeValueAsStringA3() {
    PrepareOM3NoGetterSetter prepare = new PrepareOM3NoGetterSetter();
    Assert.assertEquals("null", OM3.writeValueAsString(OM3.writeValueAsString(OM3.om(), prepare, true)));
  }
}
