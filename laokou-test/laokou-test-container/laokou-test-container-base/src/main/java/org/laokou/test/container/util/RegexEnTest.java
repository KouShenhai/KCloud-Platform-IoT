package org.laokou.test.container.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class RegexEnTest {
	public static void main(String[] args) {
		String reg = "^[A-Za-z]+$";
		String str = "111";
		String str2 = "aaZZ";
		String str3 = "ZZ111SSss";
		log.info("{}",Pattern.matches(reg, str));
		log.info("{}",Pattern.matches(reg, str2));
		log.info("{}",Pattern.matches(reg, str3));
	}
}
