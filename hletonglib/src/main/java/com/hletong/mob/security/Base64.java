package com.hletong.mob.security;

/**
 * 自定义base64
 * @author Administrator
 * 
 *  注： 1.编码，解码参照的字母顺序必须一样(包括前台js加密，解密）
 *      2.编码参照的除了本代码中的字母外也可以是符号什么的都可以，但不能重复
 *      3.算法可以改变，但改编码算法的同时必须相应更改解码的算法
 */
public class Base64 {
	//数据参照，其中字母顺序与加密的参照表中的顺序必须一样，解码出来的数据才能一致
	//数据参照，其中顺序可随便打乱以实现独一无二的解密，使其通过网上工具无法解码出正确数据
	private static String keyStr = "STUVjkKLMGHIJQRhinyz01NWdeqrstopuvwx78=ABCD4lm239+/F56EXYZfgOPabc";
	/**
	 * 解码
	 */
	public static String decode(String input) {
		byte[] data = input.getBytes();
		String output = "";
		int chr1, chr2, chr3;
		int enc1, enc2, enc3, enc4;
		int i = 0;
       //检测编码合法性必须是 A-Z, a-z, 0-9, +, /, or =
		for (int j = 0; j < data.length; j++) {
			if (keyStr.indexOf(data[j]) < 0) {
				System.out.println("There were invalid base64 characters in the input text.\n"
								+ "Valid base64 characters are A-Z, a-z, 0-9, '+', '/', and '='\n"
								+ "Expect errors in decoding.");
				return "";
			}
		}
		do {
			enc1 = keyStr.indexOf(input.charAt(i++));
			enc2 = keyStr.indexOf(input.charAt(i++));
			enc3 = keyStr.indexOf(input.charAt(i++));
			enc4 = keyStr.indexOf(input.charAt(i++));

			chr1 = (enc1 << 2) | (enc2 >> 4);
			chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			chr3 = ((enc3 & 3) << 6) | enc4;

			output = output + (char) chr1;

			if (enc3 != 64) {
				output = output + (char) chr2;
			}
			if (enc4 != 64) {
				output = output + (char) chr3;
			}

			chr1 = chr2 = chr3 = 0;
			enc1 = enc2 = enc3 = enc4 = 0;

		} while (i < input.length());

		return Escape.unescape(output);
	}
	
	//加密

	public static String encode(String input) {
		
		input = Escape.escape(input);
		
		String output = "";
		int chr1, chr2, chr3;
		int enc1, enc2, enc3, enc4;
		int i = 0;
		int len = input.length();
		//算法，可自定义
		do {

			chr1 = (int) input.charAt(i++);
			enc1 = chr1 >> 2;

			if (i < len) {
				chr2 = (int) input.charAt(i++);
				enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
				if (!isNum(chr2)) {
					enc3 = enc4 = 64;
				}

				if (i < len) {
					chr3 = (int) input.charAt(i++);
					enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
					enc4 = chr3 & 63;

					if (!isNum(chr3)) {
						enc4 = 64;
					}
					output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2)
							+ keyStr.charAt(enc3) + keyStr.charAt(enc4);
				} else {
					//添加尾
					enc4 = ((chr2 & 15) << 2);
					output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2)
							+ keyStr.charAt(enc4) + "=";
				}
			} else {
				//添加尾
				enc4 = ((chr1 & 3) << 4);
				output = output + keyStr.charAt(enc1) + keyStr.charAt(enc4)
						+ "==";
			}

			chr1 = chr2 = chr3 = 0;
			enc1 = enc2 = enc3 = enc4 = 0;

		} while (i < len);
		return output;
	}

	public static boolean isNum(int str) {
		return String.valueOf(str).matches(
				"^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

}
