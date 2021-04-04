package recommenderSystem.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *  对象的的拷贝操作
 * 重写 BeanUtil
 * kopy 对象
 */

public class BeanUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtil.class);

    /**
     * 获取空属性
     *
     * @param source
     * @return
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 复制对象
     *
     * @param source
     * @param target
     */
    private static void copyPropertiesMap(Object source, Map<String, Object> target) {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = source.getClass();
        PropertyDescriptor[] sourcePds = BeanUtils.getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor sourcePd : sourcePds) {
            if ("class".equals(sourcePd.getName())) {
                continue;
            }
            Method readMethod = sourcePd.getReadMethod();
            if (readMethod != null) {
                try {
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(source);
                    target.put(sourcePd.getName(), value);
                } catch (Throwable ex) {
                    throw new FatalBeanException("Could not copy property '" + sourcePd.getName() + "' from source to target",
                            ex);
                }
            }

        }

    }

    /**
     * 复制对象
     *
     * @param source
     * @param target
     */
    private static void copyPropertiesMap(Map<String, Object> source, Object target) {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor targetPd : targetPds) {
            if ("class".equals(targetPd.getName())) {
                continue;
            }
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null) {
                try {
                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                        writeMethod.setAccessible(true);
                    }
                    writeMethod.invoke(target, source.get(targetPd.getName()));
                } catch (Throwable ex) {
                    throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target",
                            ex);
                }
            }

        }

    }

    /**
     * 复制对象
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {

        if (target instanceof Map) {
            copyPropertiesMap(source, (Map) target);
        } else if (source instanceof Map) {
            copyPropertiesMap((Map) source, target);
        } else {
            BeanUtils.copyProperties(source, target);
        }
    }

    /**
     * 复制对象
     *
     * @param source
     * @param clazz
     */
    public static <T> T copyProperties(Object source, Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
            BeanUtils.copyProperties(source, t);
        } catch (Exception e) {
            LOGGER.error("复制对象失败", e);
        }
        return t;
    }

    /**
     * 复制对象 去除null
     *
     * @param source
     * @param target
     */
    private static void copyPropertiesMapIgnoreNull(Object source, Map<String, Object> target) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = source.getClass();
        PropertyDescriptor[] sourcePds = BeanUtils.getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor sourcePd : sourcePds) {
            if ("class".equals(sourcePd.getName())) {
                continue;
            }
            Method readMethod = sourcePd.getReadMethod();
            if (readMethod != null) {
                try {
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(source);
                    if (value == null) {
                        continue;
                    }
                    target.put(sourcePd.getName(), value);
                } catch (Throwable ex) {
                    throw new FatalBeanException("Could not copy property '" + sourcePd.getName() + "' from source to target",
                            ex);
                }
            }

        }
    }

    /**
     * 复制对象 去除null
     *
     * @param source
     * @param target
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        if (target instanceof Map) {
            copyPropertiesMapIgnoreNull(source, (Map) target);
        } else {
            BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        }
    }

    /**
     * 对象类型一样用这个 深拷贝
     *
     * @param object
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T clone(T object) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bout);
            oos.writeObject(object);

            ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bin);
            // 此处不需要释放资源，说明：调用ByteArrayInputStream或ByteArrayOutputStream对象的close方法没有任何意义
            // 这两个基于内存的流只要垃圾回收器清理对象就能够释放资源，这一点不同于对外部资源（如文件流）的释放
            return (T) ois.readObject();
        } catch (Exception e) {
            LOGGER.error("复制对象失败", e);
            throw new RuntimeException(e);
        }

    }

}

