package com.spicy.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JsonPointer emptyPointer = JsonPointer.compile(null);

    public static ObjectMapper objectMapper() {

        return objectMapper.copy()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setTimeZone(TimeZone.getTimeZone("GMT+8"))
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static ObjectNode objectNode() {
        return objectMapper().createObjectNode();
    }

    public static ArrayNode arrayNode() {
        return objectMapper().createArrayNode();
    }

    public static ArrayNode arrayNode(Collection<JsonNode> nodes) {
        return objectMapper().createArrayNode().addAll(nodes);
    }

    public static <T> T convertTo(Object source, Class<T> type) {

        JavaType javaType = TypeFactory.defaultInstance().constructType(type);
        return convertTo(source, javaType);
    }

    public static <T> T convertTo(Object source, Class<T> type, Class<?>... parameterTypes) {

        if (parameterTypes.length == 0) {
            return convertTo(source, type);
        }

        JavaType javaType = TypeFactory.defaultInstance().constructParametricType(type, parameterTypes);
        return convertTo(source, javaType);
    }

    public static <T> T convertTo(Object source, JavaType javaType) {

        if (source == null) {
            return null;
        }

        return objectMapper().convertValue(source, javaType);
    }

    public static JsonNode toJson(Object input) {

        if (input instanceof JsonNode) {
            return (JsonNode) input;
        }

        if (input instanceof CharSequence) {

            try {
                return objectMapper.readTree(String.valueOf(input));
            } catch (Exception e) {
            }
        }

        return objectMapper().setSerializationInclusion(Include.NON_NULL).convertValue(input, JsonNode.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> T copyOf(T input) {

        if (input == null) {
            return null;
        }

        if (input instanceof JsonNode) {
            return (T) JsonNode.class.cast(input).deepCopy();
        }

        return convertTo(toJson(input), (Class<T>) input.getClass());
    }

    public static JsonNode toJsonWithNull(Object input) {

        if (input instanceof JsonNode) {
            return (JsonNode) input;
        }

        return objectMapper().convertValue(input, JsonNode.class);
    }

    public static String toString(Object input) {

        if (input == null) {
            return null;
        }

        if (input instanceof CharSequence) {
            return input.toString();
        }

        try {
            return objectMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
        }

        return null;
    }

    public static void unionJsonNode(JsonNode node, JsonNode jsonNode) {

        if (jsonNode.isArray()) {

            writeJsonNode(node, "/", jsonNode);
            return;
        }

        jsonNode.fieldNames().forEachRemaining(name -> writeJsonNode(node, name, jsonNode.path(name)));
    }

    public static void writeJsonValue(JsonNode node, String jsonPath, Object value) {

        jsonPath = StringUtils.replace(jsonPath, ".", "/");
        jsonPath = StringUtils.prependIfMissing(jsonPath, "/");

        JsonPointer pointer = JsonPointer.compile(jsonPath);
        writeJsonNode(node, pointer, convertTo(value, JsonNode.class));
    }

    public static void writeJsonValue(JsonNode node, JsonPointer pointer, Object value) {
        writeJsonNode(node, pointer, convertTo(value, JsonNode.class));
    }

    public static void writeJsonNode(JsonNode node, String jsonPath, JsonNode jsonNode) {

        jsonPath = StringUtils.replace(jsonPath, ".", "/");
        jsonPath = StringUtils.prependIfMissing(jsonPath, "/");

        JsonPointer pointer = JsonPointer.compile(jsonPath);
        writeJsonNode(node, pointer, jsonNode);
    }

    public static void writeJsonNode(JsonNode node, JsonPointer pointer, JsonNode jsonNode) {

        JsonNode valueNode = jsonNode == null || jsonNode.isMissingNode() ? NullNode.instance : jsonNode;

        JsonPointer headPointer = pointer.head();

        if (headPointer == null) {

            if (node.isArray()) {

                ArrayNode arrayNode = (ArrayNode) node;

                if (valueNode.isArray()) {

                    arrayNode.addAll((ArrayNode) valueNode);
                } else {

                    arrayNode.add(valueNode);
                }
            } else if (node.isObject() && valueNode.isObject()) {

                ((ObjectNode) node).setAll((ObjectNode) valueNode);
            }

            return;
        }

        JsonNode parentNode = nodeAt(node, headPointer);

        String property = pointer.last().toString().substring(1);

        if (parentNode.isNull() || parentNode.isMissingNode()) {

            parentNode = "#".equals(property) || StringUtils.isNumeric(property)
                    ? arrayNode() : objectNode();

            writeJsonNode(node, headPointer, parentNode);
        }

        if (parentNode.isArray()) {

            ArrayNode parentArrNode = (ArrayNode) parentNode;

            if (StringUtils.isNumeric(property)) {

                int index = Integer.valueOf(property);

                for (int i = parentArrNode.size(); i <= index; i++) {
                    parentArrNode.addNull();
                }

                parentArrNode.set(index, valueNode);

            } else if ("#".equals(property)) {

                parentArrNode.add(valueNode);

            } else if (StringUtils.isNotEmpty(property)) {

                if (valueNode.isArray() && valueNode.elements().hasNext()) {

                    writeArray(parentArrNode, valueNode, property);
                } else {

                    Optional<JsonNode> found = Streams.stream(parentArrNode)
                            .filter(arrNode -> !arrNode.has(property)).findFirst();

                    if (found.isPresent()) {

                        writeJsonNode(found.get(), property, valueNode);
                    } else {
                        parentArrNode.add(objectNode().set(property, valueNode));
                    }
                }
            } else {

                parentArrNode.add(valueNode);
            }
        } else if (parentNode.isObject()) {

            if (valueNode.isArray() && valueNode.elements().hasNext()) {

                String[] arrayPaths = splitArrayPath(pointer);

                if (arrayPaths != null) {

                    writeArray(nodeAt(node, JsonPointer.compile(arrayPaths[0])), valueNode, arrayPaths[1]);
                } else {

                    ((ObjectNode) parentNode).set(property, valueNode);
                }
            } else {

                ((ObjectNode) parentNode).set(property, valueNode);
            }
        } else {
            throw new IllegalArgumentException("`" + property + "` can't be set for parent node `" + headPointer
                    + "` because parent is not a container but " + parentNode.getNodeType().name());
        }
    }

    private static String[] splitArrayPath(JsonPointer pointer) {

        String path = pointer.toString();

        int index = StringUtils.lastIndexOf(path, "/#");

        if (index < 0) {
            return null;
        }

        return new String[]{path.substring(0, index).replace("#", "0"), path.substring(index)};
    }

    private static void writeArray(JsonNode arrayNode, JsonNode valueNode, String path) {

        Iterator<JsonNode> arrayItr = arrayNode.elements();
        Iterator<JsonNode> valueItr = valueNode.elements();

        while (arrayItr.hasNext() && valueItr.hasNext()) {
            writeJsonNode(arrayItr.next(), path, valueItr.next());
        }

        while (valueItr.hasNext()) {
            writeJsonNode(arrayNode, path, valueItr.next());
        }
    }

    private static JsonNode nodeAt(JsonNode node, JsonPointer pointer) {

        JsonNode found = node.at(pointer);

        if (found.isNull() || found.isMissingNode()) {

            if ("#".equals(pointer.last().toString().substring(1))) {

                return nodeAt(node, pointer.head());
            }
        }

        return found;
    }

    public static void copyJsonNode(JsonNode source, JsonNode target, String path) {
        copyJsonNode(source, target, path, path);
    }

    public static void copyJsonNode(JsonNode source, JsonNode target, String srcPath, String tgtPath) {

        JsonNode node = readJsonNode(source, srcPath);

        if (node.isNull() || node.isMissingNode()) {
            return;
        }

        writeJsonNode(target, tgtPath, node);
    }

    public static <T> T readJsonValue(JsonNode node, String jsonPath, Class<T> type) {

        jsonPath = StringUtils.replace(jsonPath, ".", "/");
        jsonPath = StringUtils.prependIfMissing(jsonPath, "/");

        JsonNode valueNode = readJsonNode(node, jsonPath);

        if (valueNode.isMissingNode() || valueNode.isNull()) {
            return null;
        }

        return convertTo(valueNode, type);
    }

    public static <T> T readJsonValue(JsonNode node, JsonPointer pointer, Class<T> type) {

        JsonNode valueNode = readJsonNode(node, pointer);
        return convertTo(valueNode, type);
    }

    public static JsonNode readJsonNode(JsonNode node, String jsonPath) {

        jsonPath = StringUtils.replace(jsonPath, ".", "/");
        jsonPath = StringUtils.prependIfMissing(jsonPath, "/");

        JsonPointer pointer = JsonPointer.compile(jsonPath);
        return readJsonNode(node, pointer);
    }

    public static JsonNode readJsonNode(JsonNode node, JsonPointer pointer) {
        return readJsonNode(node, emptyPointer, pointer);
    }

    private static JsonNode readJsonNode(JsonNode node, JsonPointer parent, JsonPointer remain) {

        String property = remain.getMatchingProperty();

        if ("#".equals(property)) {

            JsonNode parentNode = node.at(parent);

            if (!parentNode.isArray()) {
                return NullNode.instance;
            }

            ArrayNode results = objectMapper().createArrayNode();

            for (int i = 0; i < parentNode.size(); i++) {

                JsonPointer pointer = parent.append(JsonPointer.compile("/" + i));
                JsonNode result = readJsonNode(node, pointer, remain.tail());

                if (result.isArray()) {

                    results.addAll((ArrayNode) result);

                } else if (!result.isMissingNode()) {

                    results.add(result);
                }
            }

            return results;
        }

        JsonPointer pointer = property.isEmpty() ? parent : parent.append(
                JsonPointer.compile(StringUtils.prependIfMissing(property, "/")));

        JsonNode pointedNode = node.at(pointer);

        if (pointedNode.isMissingNode() || pointedNode.isNull()) {
            return pointedNode;
        }

        JsonPointer nextRemain = remain.tail();

        if (nextRemain == null || nextRemain.equals(emptyPointer)) {
            return pointedNode;
        }

        return readJsonNode(node, pointer, nextRemain);
    }

    public static List<JsonPointer> exportJsonPointer(JsonNode node) {

        List<JsonPointer> pointers = new ArrayList<>();
        exportJsonPointer(pointers, emptyPointer, node);

        return pointers;
    }

    private static void exportJsonPointer(List<JsonPointer> pointers, JsonPointer parent, JsonNode node) {

        if (node.isObject()) {

            for (Iterator<Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {

                Entry<String, JsonNode> field = it.next();

                JsonPointer pointer = parent.append(
                        JsonPointer.compile(StringUtils.prependIfMissing(field.getKey(), "/")));
                exportJsonPointer(pointers, pointer, field.getValue());
            }

            return;
        }

        if (node.isArray()) {

            JsonPointer pointer = parent.append(JsonPointer.compile("/#"));
            exportJsonPointer(pointers, pointer, node.get(0));

            return;
        }

        pointers.add(parent);
    }

    public static Map<String, JsonNode> resolveJsonNode(JsonNode node) {

        Map<String, JsonNode> nodes = new LinkedHashMap<>();
        resolveJsonNode(nodes, emptyPointer, node);

        return nodes;
    }

    private static void resolveJsonNode(Map<String, JsonNode> nodes, JsonPointer parent, JsonNode node) {

        if (node.isObject()) {

            for (Iterator<Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {

                Entry<String, JsonNode> field = it.next();

                JsonPointer pointer = parent.append(
                        JsonPointer.compile(StringUtils.prependIfMissing(field.getKey(), "/")));
                resolveJsonNode(nodes, pointer, field.getValue());
            }

            return;
        }

        if (node.isArray()) {

            for (int i = 0; i < node.size(); i++) {

                JsonPointer pointer = parent.append(JsonPointer.compile("/" + i));
                resolveJsonNode(nodes, pointer, node.get(i));
            }

            return;
        }

        nodes.put(parent.toString(), node);
    }

    public static Map<String, Object> toMap(JsonNode node) {

        Map<String, Object> result = Maps.newLinkedHashMap();

        for (Iterator<String> it = node.fieldNames(); it.hasNext(); ) {

            String fieldName = it.next();
            JsonNode valueNode = node.get(fieldName);

            Map<String, Object> valueMap = toMap(valueNode);

            if (!valueMap.isEmpty()) {

                result.put(fieldName, valueMap);
                continue;
            }

            result.put(fieldName, JsonUtils.convertTo(valueNode, Object.class));
        }

        return result;
    }

}
