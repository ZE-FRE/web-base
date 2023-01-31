package cn.zefre.base.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author pujian
 * @date 2022/8/18 15:12
 */
public class ListUtilTest {

    @Test
    public void testRemoveDuplicates() {
        List<String> list = new ArrayList<>(Arrays.asList("张三","李四","王五","张三","李四","张三"));
        ListUtil.removeDuplicates(list);
        Assertions.assertArrayEquals(new String[]{"张三","李四","王五"}, list.toArray(new String[3]));
    }

    @Test
    public void testGetDuplicates() {
        List<String> list = new ArrayList<>(Arrays.asList("张三","李四","王五","张三","李四","张三"));
        Map<String, Integer> duplicateMap = ListUtil.getDuplicateMap(list);
        Assertions.assertEquals(2, duplicateMap.size());
        Assertions.assertEquals(3, duplicateMap.get("张三"));
        Assertions.assertEquals(2, duplicateMap.get("李四"));
    }

}
