package org.shaneking.skava.t3.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.shaneking.skava.sk.lang.SKRuntimeException;

@Slf4j
public class OM3 {
  public static final String OBJECT_ERROR_STRING = "{}";
  private static ObjectMapper OM = null;

  public static ObjectMapper om() {
    if (OM == null) {
      OM = new ObjectMapper();
      OM.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      OM.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
    return OM;
  }

  public static ObjectNode createObjectNode() {
    return createObjectNode(om());
  }

  public static ObjectNode createObjectNode(@NonNull ObjectMapper objectMapper) {
    return objectMapper.createObjectNode();
  }

  public static <T> T readValue(T t) {
    return OBJECT_ERROR_STRING.equals(writeValueAsString(t)) ? null : t;
  }

  public static <T> T readValue(String content, Class<T> valueType) {
    return readValue(om(), content, valueType);
  }

  public static <T> T readValue(@NonNull ObjectMapper objectMapper, String content, Class<T> valueType) {
    return readValue(objectMapper, content, valueType, false);
  }

  public static <T> T readValue(@NonNull ObjectMapper objectMapper, String content, Class<T> valueType, boolean rtnNullIfException) {
    try {
      return readValue(objectMapper.readValue(content, valueType));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      if (rtnNullIfException) {
        return null;
      } else {
        throw new SKRuntimeException(e);
      }
    }
  }

  public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
    return readValue(om(), content, valueTypeRef);
  }

  public static <T> T readValue(@NonNull ObjectMapper objectMapper, String content, TypeReference<T> valueTypeRef) {
    return readValue(objectMapper, content, valueTypeRef, false);
  }

  public static <T> T readValue(@NonNull ObjectMapper objectMapper, String content, TypeReference<T> valueTypeRef, boolean rtnNullIfException) {
    try {
      return readValue(objectMapper.readValue(content, valueTypeRef));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      if (rtnNullIfException) {
        return null;
      } else {
        throw new SKRuntimeException(e);
      }
    }
  }

  public static <T> T treeToValue(TreeNode n, Class<T> valueType) {
    return treeToValue(om(), n, valueType);
  }

  public static <T> T treeToValue(@NonNull ObjectMapper objectMapper, TreeNode n, Class<T> valueType) {
    return treeToValue(om(), n, valueType, false);
  }

  public static <T> T treeToValue(@NonNull ObjectMapper objectMapper, TreeNode n, Class<T> valueType, boolean rtnNullIfException) {
    try {
      return objectMapper.treeToValue(n, valueType);
    } catch (Exception e) {
      if (rtnNullIfException) {
        return null;
      } else {
        throw new SKRuntimeException(e);
      }
    }
  }

  public static <T extends JsonNode> T valueToTree(Object fromValue) {
    return valueToTree(om(), fromValue);
  }

  public static <T extends JsonNode> T valueToTree(@NonNull ObjectMapper objectMapper, Object fromValue) {
    return objectMapper.valueToTree(fromValue);
  }

  public static String writeValueAsString(Object value) {
    return writeValueAsString(om(), value);
  }

  public static String writeValueAsString(@NonNull ObjectMapper objectMapper, Object value) {
    return writeValueAsString(objectMapper, value, false);
  }

  public static String writeValueAsString(@NonNull ObjectMapper objectMapper, Object value, boolean rtnNullIfException) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (Exception e) {
      if (rtnNullIfException) {
        return null;
      } else {
        throw new SKRuntimeException(e);
      }
    }
  }
}
