package yuku.infinitepassgen.lib;

import java.io.UnsupportedEncodingException;

public class U {
	public static final String TAG = U.class.getSimpleName();
	
	public static boolean equals(Object a, Object b) {
		if (a == b) return true;
		if (a == null) return false;
		return a.equals(b);
	}
	
	public static String utf8decode(byte[] b) {
		try {
			return new String(b, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e); // should not happen
		}
	}
	
	public static byte[] utf8encode(String s) {
		try {
			return s.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e); // should not happen
		}
	}
}
