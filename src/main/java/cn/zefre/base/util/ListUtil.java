package cn.zefre.base.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * List工具
 *
 * @author pujian
 * @date 2022/8/16 14:31
 */
@UtilityClass
public class ListUtil {

    /**
     * List去重
     *
     * @param list list
     * @author pujian
     * @date 2022/8/16 18:00
     */
    public static <T> void removeDuplicates(List<T> list) {
        removeDuplicates(list, Function.identity());
    }

    /**
     * List去重，由getter获取用于判断是否重复的值
     *
     * @param list list
     * @param getter getter
     * @author pujian
     * @date 2022/8/16 18:01
     */
    public static <T, R> void removeDuplicates(List<T> list, Function<? super T, ? extends R> getter) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Assert.notNull(getter, "getter cannot be null");
        Set<R> set = new HashSet<>(list.size());
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T obj = iterator.next();
            R result = getter.apply(obj);
            if (set.contains(result)) {
                iterator.remove();
            } else {
                set.add(result);
            }
        }
    }

    /**
     * 获取List重复的元素
     *
     * @param list list
     * @return java.util.List<T>
     * @author pujian
     * @date 2022/8/16 18:05
     */
    public static <T> List<T> getDuplicates(List<T> list) {
        return getDuplicates(list, Function.identity());
    }

    /**
     * 获取List中重复的字段
     *
     * @param list list
     * @param getter getter
     * @return java.util.List<R>
     * @author pujian
     * @date 2022/8/16 18:06
     */
    public static <T, R> List<R> getDuplicates(List<T> list, Function<? super T, ? extends R> getter) {
        Map<? extends R, Integer> duplicateMap = getDuplicateMap(list, getter);
        List<R> result = new ArrayList<>();
        if (duplicateMap != null && !duplicateMap.isEmpty()) {
            result.addAll(duplicateMap.keySet());
        }
        return result;
    }

    /**
     * 获取List重复元素以及重复的次数
     *
     * @param list list
     * @return java.util.Map<T,java.lang.Integer>
     * @author pujian
     * @date 2022/8/18 10:35
     */
    public static <T> Map<T, Integer> getDuplicateMap(List<T> list) {
        return getDuplicateMap(list, Function.identity());
    }

    /**
     * 获取List中重复字段以及重复的次数
     *
     * @param list list
     * @param getter getter
     * @return java.util.Map<R,java.lang.Integer>
     * @author pujian
     * @date 2022/8/16 18:07
     */
    public static <T, R> Map<R, Integer> getDuplicateMap(List<T> list, Function<? super T, ? extends R> getter) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        Assert.notNull(getter, "getter cannot be null");
        // 等价于 <=> list.stream().collect(Collectors.groupingBy(getter, Collectors.counting()))
        Map<R, Integer> countingMap = new HashMap<>(list.size());
        for (T obj : list) {
            R result = getter.apply(obj);
            countingMap.merge(result, 1, Integer::sum);
        }
        // 收集重复的数据
        return countingMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 批量复制对象属性
     *
     * @param sourceList  源List
     * @param targetClass 目标类型
     * @return java.util.List<TAR>
     * @author pujian
     * @date 2023/1/19 11:12
     */
    @SneakyThrows
    public static <SRC, TAR> List<TAR> copyPropertiesBatch(List<SRC> sourceList, Class<TAR> targetClass) {
        Assert.notNull(sourceList, "sourceList cannot be null.");
        Assert.notNull(targetClass, "targetClass cannot be null.");
        List<TAR> targetList = new ArrayList<>(sourceList.size());
        for (SRC source : sourceList) {
            TAR target = targetClass.newInstance();
            BeanUtils.copyProperties(source, target);
            targetList.add(target);
        }
        return targetList;
    }

}
