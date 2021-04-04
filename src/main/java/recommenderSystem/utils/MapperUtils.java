package recommenderSystem.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description:
 *  工具类
 * @version v1.0.0
 * @Author xiayu
 * @Date 2020/10/1 12:30
 */
public class MapperUtils {

    private static DecimalFormat df = new DecimalFormat("######0.##");

    /**
     * 对象是否为空
     * @param obj
     * @return
     */
    public static boolean isBlank(Object obj) {

        if (obj == null) {
            return true;
        }
        int strLen;
        String str = obj.toString();
        if ((strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断String是否为空
     * @param string
     * @return
     */
    public static boolean isBlankString(String string) {
        return (string == null) || (string.trim().length() < 1);
    }

    /**
     * 过滤String 中的空格
     */
    public static String trimKonggeAll(String s) {
        if(s == null){
            return null;
        }
        else {
            return s.replaceAll(" ", "");
        }
    }

    /**
     * 删除为空的
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> collectionsRemoveNull(List<T> list) {
        List<T> result = new ArrayList<>();
        if ((list != null) && (!list.isEmpty())) {
            for (T item : list) {
                if (item != null) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    /**
     * 集合是否为空
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> boolean isBlankCollection(Collection<T> obj) {
        return obj == null || obj.isEmpty();
    }

    public static byte[] objectWrite(Object obj) throws IOException {
        ByteArrayOutputStream stream = null;
        ObjectOutputStream oos = null;
        byte[] bs = null;
        try {
            stream = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(stream);
            oos.writeObject(obj);
            bs = stream.toByteArray();
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (stream != null) {
                stream.close();
            }
        }
        return bs;
    }

    /**
     * 返回List的第一个数据
     */
    public static <T> T getFirstOne(List<T> list) {
        if (isBlankCollection(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /**
     * 截取list
     */
    public static <T> List<T> listSubList(List<T> list, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (list == null || fromIndex < 0 || toIndex < 0 || fromIndex > toIndex || list.isEmpty() || fromIndex >= list.size()) {
            return new ArrayList<T>();
        }
        if (toIndex > list.size()) {
            toIndex = list.size();
        }
        return new ArrayList<T>(list.subList(fromIndex, toIndex));
    }

    /**
     * 判断一个字符的后半部分是否和另一个字符是否一样
     *
     * @param string
     * @param end
     * @return
     */
    public static boolean stringEndWithIgnoreCase(String string, String end) {
        if ((isBlankString(string)) || (isBlankString(end))) {
            return false;
        }
        if (string.length() < end.length()) {
            return false;
        }
        //substring  截取String
        String temp = string.substring(string.length() - end.length());
        return temp.equalsIgnoreCase(end);
    }


    /**
     * 判断一个字符的前半部分是否和另一个字符是否一样
     *
     * @param sourceString
     * @param startChar
     * @return
     */
    public static String stringReplaceStartChar(String sourceString, String startChar) {
        if ((!isBlankString(sourceString)) && (!isBlankString(startChar))) {
            int length = startChar.length();
            while (sourceString.startsWith(startChar)) {
                sourceString = sourceString.substring(length);
            }
        }
        return sourceString;
    }

    /**
     * 截取String的前多少位
     */
    public static String stringSubString(String str, int length) {
        if (str == null) {
            return null;
        }
        if (str.length() > length) {
            return str.substring(0, length);
        }
        return str;
    }

    /**
     * 依据一个String分割一个String返回这String[]
     *
     * @param string
     * @param regex
     * @return
     */
    public static String[] stringSplit(String string, String regex) {
        if ((isBlankString(string))) {
            return new String[0];
        }
        if (regex == null) {
            return new String[]{string};
        }
        //split 分割
        return string.split(regex);
    }

    /**
     * 一个字符串是否要包含另一个字符串
     *
     * @param string
     * @param s
     * @return
     */
    public static boolean stringContains(String string, CharSequence s) {
        if ((isBlankString(string))) {
            return false;
        }
        return string.contains(s);
    }

    /**
     * 生成一个UUID
     *
     * @return
     */
    public static String buildUUID() {
        //replace 替代
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * 将String的值转成boolean，String是否等于true
     */
    public static boolean toBoolean(String data, boolean defaultValue) {
        try {
            if (data == null) {
                return defaultValue;
            }
            //Boolean.valueOf(value)判断这个值是不是true,
            return Boolean.valueOf(data).booleanValue();
        } catch (NumberFormatException e) {
            // igonre
        }
        return defaultValue;
    }

    public static boolean toBoolean(Object data, boolean defaultValue) {
        try {
            if (data == null) {
                return defaultValue;
            }
            return Boolean.valueOf(String.valueOf(data)).booleanValue();
        } catch (NumberFormatException e) {
            // igonre
        }
        return defaultValue;
    }

    /**
     * 将String转成Int类型
     *
     * @param data
     * @param defaultValue
     * @return
     */
    public static int toInt(Object data, int defaultValue) {
        try {
            if (data == null) {
                return defaultValue;
            }
            return Integer.valueOf(String.valueOf(data)).intValue();
        } catch (NumberFormatException e) {
            // igonre
        }
        return defaultValue;
    }

    public static int toInt(String data, int defaultValue) {
        try {
            return Integer.valueOf(data).intValue();
        } catch (NumberFormatException e) {
            // igonre
        }
        return defaultValue;
    }

    /**
     * 将String转化成Long类型
     */
    public static long toLong(Object data, long defaultValue) {
        try {
            if (data == null) {
                return defaultValue;
            }
            return Long.valueOf(String.valueOf(data));
        } catch (NumberFormatException e) {
            // igonre
        }
        return defaultValue;
    }

    public static long toLong(String data, long defaultValue) {
        try {
            return Long.valueOf(data);
        } catch (NumberFormatException e) {
            // igonre
        }
        return defaultValue;
    }

    /**
     * String字符之间的排序
     */
    public static String stringSort(String text) {
        if (text == null) {
            return text;
        }
        char[] arr = text.toCharArray();
        Arrays.sort(arr);
        return new String(arr);
    }




    //***********************************List操作************************************

    /**
     * 数组转化成Lite
     */
    public static <T> List<T> arrayToList(T[] array) {
        List<T> list = new ArrayList<>();
        if (array == null) {
            return list;
        }
        for (T item : array) {
            list.add(item);
        }
        return list;
    }

    /**
     * 字符串依据分割符划分为List<String>
     */
    public static List<String> StringToList(String string, String regex) {
        String[] array = stringSplit(string, regex);
        return arrayToList(array);
    }

    /**
     * List转为String
     */
    public static <T> String listToString(List<T> list, String split) {
        if (list == null || list.isEmpty()) {
            return "";
        }//默认分隔符是 ，
        if (split == null) {
            split = ",";
        }
        String str = "";
        int size = list.size();
        for (int i = 0; i < size - 1; i++) {
            str += list.get(i) + split;
        }
        str += list.get(size - 1);
        return str;
    }

    /**
     * List转化为一个Map
     */
    public static <T> Map<String, List<T>> listToKeyList(List<T> list, String... keys) {
        Map<String, List<T>> map = new HashMap<>();
        if (list == null || list.isEmpty() || keys == null || keys.length == 0) {
            return map;
        }
        for (T obj : list) {
            if (obj == null) {
                continue;
            }
            String mapKey = getValue(obj, keys);
            if (!isBlankString(mapKey)) {
                List<T> temp = map.get(mapKey);
                if (temp == null) {
                    temp = new ArrayList<>();
                    map.put(mapKey, temp);
                }
                temp.add(obj);
            }
        }
        return map;
    }

    /**
     * list转map
     */
    public static <T> Map<String, T> listToKeyObj(List<T> list, String... keys) {
        Map<String, T> map = new HashMap<>();
        if (list == null || list.isEmpty() || keys == null || keys.length == 0) {
            return map;
        }
        for (T obj : list) {
            if (obj == null) {
                continue;
            }
            String mapKey = getValue(obj, keys);
            if (!isBlankString(mapKey)) {
                map.put(mapKey, obj);
            }
        }
        return map;
    }


    public static <T> String getKey(T obj, String... keys) {
        if (keys == null || keys.length == 0) {
            return "";
        }
        String key = "";
        for (int i = 0; i < keys.length - 1; i++) {
            key += getValue(obj, keys[i]) + "_";
        }
        return key += getValue(obj, keys[keys.length - 1]);
    }

    public static <T> String getValue(T obj, String... keys) {
        if (keys == null || keys.length == 0) {
            return "";
        }
        String mapKey = "";
        for (int i = 0; i < keys.length - 1; i++) {
            mapKey += getValue(obj, keys[i]) + "_";
        }
        mapKey += getValue(obj, keys[keys.length - 1]);
        return mapKey;
    }

    /**
     * 连接String
     *
     * @param split
     * @param args
     * @return
     */
    public static String stringConcat(String split, String... args) {
        if (args == null) {
            return null;
        }
        if (args.length == 1) {
            return args[0];
        }
        String s = "";
        for (int i = 0; i < args.length; i++) {
            String item = args[i];
            //startsWith 判断这个改字段的前缀是不是（）里面的
            if ((i == 0) || (item.startsWith(split))) {
                s = s.concat(item);
            } else {
                s = s.concat(split + item);
            }
        }
        return s;
    }
    /**
     * 判断String是否相等 不区分大小写
     */
    public static boolean stringCompare(String string1, String string2) {
        if (string1 == null) {
            if (string2 == null) {
                return true;
            }
            return false;
        }
        //不区分大小写
        return string1.equalsIgnoreCase(string2);
    }

    public static boolean equals(Object o1, Object o2) {

        if (o1 == null) {
            if (o2 == null) {
                return true;
            } else {
                return false;
            }
        } else {
            if (o1.equals(o2)) {
                return true;
            } else {
                return false;
            }
        }

    }



    /**
     * 构建一个随记数
     * @param j 随机数的位数
     */
    public static Integer builderRandomNum (int j){
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < j; i++) {
            String random = String.valueOf(r.nextInt(9)+1);
            sb.append(random);
        }
        int ii= Integer.parseInt( sb.toString() );
        return ii;
    }
    /**
     * 获取分页数量
     *
     * @param total    数据数量
     * @param pageSize 每页数据数量
     * @return
     */
    public static int getPageNo(int total, int pageSize) {
        int pageTotal = total / pageSize;
        if (total % pageSize != 0) {
            pageTotal++;
        }
        return pageTotal;
    }





    /**
     * 字符串转换为 Map<String, Object>
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    public static <T> Map<String, Object> json2map(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.readValue(jsonString, Map.class);
    }


     /*
    数值操作
    *
    * */

    /**
     * Double 转成百分比类型
     *
     * @param val
     * @return
     */
    public static String formatPercent(Double val) {
        if (val == null) {
            val = 0.0D;
        }
        //小数社区不四舍五入
        val = Math.floor(val * 10000) / 100;
        //四舍五入 num = num.toFixed(2);
        return String.format("%.2f", val) + "%";
    }

    /**
     * Double格式化保留两位小数
     *
     * @param val
     * @return
     */
    public static String formatDouble(Double val) {
        if (val == null) {
            val = 0.0D;
        }
        return df.format(Math.floor(val * 100) / 100);
    }



    /**
     * 除操作
     *
     * @param val1
     * @param val2
     * @return
     */
    public static double divisor(Integer val1, Integer val2) {
        if (val1 == null || val2 == null || val2 == 0) {
            return 0.0;
        }
        return val1 / (val2 + 0.0D);
    }

    public static double divisor(Double val1, Integer val2) {
        if (val1 == null || val2 == null || val2 == 0) {
            return 0.0;
        }
        return val1 / (val2 + 0.0D);
    }

//    public static double divisor(long val1, int val2) {
//        if (val1 == null || val2 == null || val2 == 0) {
//            return 0.0;
//        }
//        return val1 / (val2 + 0.0D);
//    }

    public static double divisor(Integer val1, Double val2) {
        if (val1 == null || val2 == null || val2 == 0) {
            return 0.0;
        }
        return val1 / (val2 + 0.0D);
    }

    /**
     * 比较
     *
     * @param val1
     * @param val2
     * @return
     */
    public static boolean compare(Double val1, Double val2) {
        if (val1 == null || val2 == null) {
            return false;
        }
        return val1.equals(val2);
    }

    /**
     * 转double
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static double toDouble(Double value, double defaultValue) {
        return value == null ? defaultValue : value.doubleValue();
    }

    public static double toDouble(Object data, double defaultValue) {
        try {
            if (data == null) {
                return defaultValue;
            }
            return Double.valueOf(String.valueOf(data)).doubleValue();
        } catch (NumberFormatException e) {
            // igonre
        }
        return defaultValue;
    }

    public static String toDateString(Date date, String format) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(date);
    }

    /**
     * 初始化PO类
     * @param source
     */
    public static void initEntityPo(Object source) {
        String uuid = MapperUtils.buildUUID();
        Date date = new Date();
        Map map = new HashMap();
        map.put("id", uuid);
        map.put("createTime", date);
        map.put("updateTime", date);
        map.put("delFlag", 0);
        com.xiayu.springboot.utils.BeanUtil.copyPropertiesIgnoreNull(source, map);
        com.xiayu.springboot.utils.BeanUtil.copyProperties(map, source);
    }

}
