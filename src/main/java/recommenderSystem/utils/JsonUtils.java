package recommenderSystem.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @version v1.0.0
 * @Author xiayu
 * @Date 2020/10/1 12:26
 */
public class JsonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    // 定义jackson对象
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        //忽略多余的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    private JsonUtils() {

    }

    /**
     * 将对象转换成json字符串。
     */
    public static String objectToJson(final Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (final JsonProcessingException e) {
            LOGGER.error("", e);
        }
        return null;
    }


    /**
     * 将json结果集转化为对象
     */
    public static <T> T jsonToPojo(final String jsonData, final Class<T> beanType) {
        try {
            return objectMapper.readValue(jsonData, beanType);
        } catch (final Exception e) {
            LOGGER.error("", e);
        }
        return null;
    }

    /**
     * 将json数据转换成pojo对象list
     */
    public static <T> List<T> jsonToList(final String jsonData, final Class<T> beanType) {
        final JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return objectMapper.readValue(jsonData, javaType);
        } catch (final Exception e) {
            LOGGER.error("", e);
        }

        return null;
    }

    /**
     * javaBean、列表数组转换为json字符串
     */
    public static String obj2json(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("", e);
        }
        return null;
    }

    /**
     * javaBean、列表数组转换为json字符串,忽略空值
     */
    public static String obj2jsonIgnoreNull(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("", e);
        }
        return null;
    }

    /**
     * json 转JavaBean
     */

    public static <T> T json2pojo(String jsonString, Class<T> clazz) {
        try {
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            return objectMapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        return null;
    }

    /**
     * json字符串转换为map
     */
    public static <T> Map<String, Object> json2map(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.readValue(jsonString, Map.class);
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        return null;
    }

    /**
     * json字符串转换为map
     */
    public static <T> Map<String, T> json2map(String jsonString, Class<T> clazz) {
        try {
            Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) objectMapper.readValue(jsonString,
                    new TypeReference<Map<String, T>>() {
                    });
            Map<String, T> result = new HashMap<String, T>();
            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
            }
            return result;
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        return null;
    }

    /**
     * 深度转换json成map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> json2mapDeeply(String json) {
        try {
            return json2MapRecursion(json, objectMapper);
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        return null;
    }

    /**
     * 把json解析成list，如果list内部的元素存在jsonString，继续解析
     *
     * @param json
     * @param mapper 解析工具
     * @return
     * @
     */
    private static List<Object> json2ListRecursion(String json, ObjectMapper mapper) throws IOException {
        if (json == null) {
            return null;
        }

        List<Object> list = mapper.readValue(json, List.class);

        for (Object obj : list) {
            if (obj != null && obj instanceof String) {
                String str = (String) obj;
                if (str.startsWith("[")) {
                    obj = json2ListRecursion(str, mapper);
                } else if (obj.toString().startsWith("{")) {
                    obj = json2MapRecursion(str, mapper);
                }
            }
        }

        return list;
    }

    /**
     * 把json解析成map，如果map内部的value存在jsonString，继续解析
     *
     * @param json
     * @param mapper
     * @return
     * @
     */
    private static Map<String, Object> json2MapRecursion(String json, ObjectMapper mapper) throws IOException {
        if (json == null) {
            return null;
        }

        Map<String, Object> map = mapper.readValue(json, Map.class);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object obj = entry.getValue();
            if (obj != null && obj instanceof String) {
                String str = ((String) obj);

                if (str.startsWith("[")) {
                    List<?> list = json2ListRecursion(str, mapper);
                    map.put(entry.getKey(), list);
                } else if (str.startsWith("{")) {
                    Map<String, Object> mapRecursion = json2MapRecursion(str, mapper);
                    map.put(entry.getKey(), mapRecursion);
                }
            }
        }

        return map;
    }

    /**
     * 与javaBean json数组字符串转换为列表
     */
    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) {
        try {
            JavaType javaType = getCollectionType(ArrayList.class, clazz);
            List<T> lst = (List<T>) objectMapper.readValue(jsonArrayStr, javaType);
            return lst;
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        return null;
    }

    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * map  转JavaBean
     */
    public static <T> T map2pojo(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    /**
     * map 转json
     *
     * @param map
     * @return
     */
    public static String mapToJson(Map map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * obj  转JavaBean
     * 注意  clazz 与 obj.getClass相同或是它的超类或接口 不会返回新的对象
     * 强制返回新对象可用 {@link #obj2newPojo(Object, Class)}
     */
    public static <T> T obj2pojo(Object obj, Class<T> clazz) {
        return objectMapper.convertValue(obj, clazz);
    }

    /**
     * obj  转JavaBean
     */
    public static <T> T obj2newPojo(Object obj, Class<T> clazz) {
        return json2pojo(obj2json(obj), clazz);
    }


    /**
     * 将指定节点的 JSON 数据转换为 JavaBean
     *
     * @param jsonString
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> T json2pojoByTree(String jsonString, String treeNode, Class<T> clazz) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        JsonNode data = jsonNode.findPath(treeNode);
        return json2pojo(data.toString(), clazz);
    }

    /**
     * 将指定节点的 JSON 数组转换为集合
     *
     * @param jsonStr  JSON 字符串
     * @param treeNode 查找 JSON 中的节点
     * @return
     * @throws Exception
     */
    public static <T> List<T> json2listByTree(String jsonStr, String treeNode, Class<T> clazz) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(jsonStr);
        JsonNode data = jsonNode.findPath(treeNode);
        return json2list(data.toString(), clazz);
    }

}

