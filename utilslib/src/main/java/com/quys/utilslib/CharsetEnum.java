package com.quys.utilslib;

/**
 * 编码类型
 */
public enum CharsetEnum {
	UTF8("UTF-8"),
	UTF16LE("UTF-16LE"),
	UTF16BE("UTF-16BE"),
	GBK("GBK");

	private String mName;
	public static final byte BLANK = 0x0a;

	private CharsetEnum(String name) {
		mName = name;
	}
	
	public String getName() {
		return mName;
	}
}
