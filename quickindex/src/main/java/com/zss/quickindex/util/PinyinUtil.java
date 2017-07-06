package com.zss.quickindex.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {

	
	/**
	 * 将包含汉字的字符串转成拼音
	 * @param string 字符串
	 * @return zifuchuan
	 */
	public static String getPinyin(String string) {
		
		// 黑马
		//     黑    马    
		// 黑rt uiok  6789$%^&*IJ马
		
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE); // 去掉发音
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE); // 大写
		
		StringBuffer sb = new StringBuffer();
		// 获取字符数组
		char[] charArray = string.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			
			// 是空格
			if(Character.isWhitespace(c)){
				continue;
			}
			
			if(c >= -128 && c <= 127){
				// 非汉字
				sb.append(c);
			}else {
				try {
					// 将字符转成拼音, 黑 -> HEI, 单 -> DAN, SHAN
					String s = PinyinHelper.toHanyuPinyinStringArray(c, format)[0];
					sb.append(s);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			}
		}
		
		return sb.toString();
	}

}
