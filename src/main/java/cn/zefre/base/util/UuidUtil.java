package cn.zefre.base.util;

import java.util.UUID;

/**
 * @author pujian
 * @date 2022/9/21 14:07
 */
public class UuidUtil {

	/**
	 * 生成不带-的UUID
	 *
	 * @return java.lang.String
	 * @author pujian
	 * @date 2022/9/21 14:07
	 */
	public static String generatorUuid() {
		String uuid = UUID.randomUUID().toString();
		// 因为UUID本身为32位只是生成时多了“-”，所以将它们去点就可
		return uuid.replace("-", "");
	}

}
