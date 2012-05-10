package yuku.infinitepassgen.lib.v3;

import android.util.Log;

import yuku.androidcrypto.DigestType;
import yuku.androidcrypto.Digester;

public class PwgenV3 {
	public static final String TAG = PwgenV3.class.getSimpleName();

	public static final int KAR_UPPER = 0x08;
	public static final int KAR_LOWER = 0x04;
	public static final int KAR_NUMBER= 0x02;
	public static final int KAR_SYMBOL= 0x01;

	public static String hitung(String M, String K, PwgenV3Options options, int perbanyak) {
		StringBuilder S = new StringBuilder();
		if (options == null || options.getFlags() == 0) {
			// ga ada kar yang dipilih
			return null;
		}

		int retlen = perbanyak*16;
		byte[] G = new byte[retlen];

		for (int i = 0; i < perbanyak; i++) {
			S.append(M).append("pemisah").append(K);
			byte[] m = md5(S); 
			for (int j = 0; j < 16; j++) {
				G[i*16+j] = m[j];
			}
		}
	
		int x = options.getMax() - options.getMin() + 1;
		x = (G[retlen-1] & 0xff) % x;
		int len = options.getMin() + x;
	
		boolean[] bolehan = new boolean[256];
	
		if (options.getUpper()) {
			for (int i = 'A'; i <= 'Z'; i++) {
				bolehan[i] = true;
			}
		}
	
		if (options.getLower()) {
			for (int i = 'a'; i <= 'z'; i++) {
				bolehan[i] = true;
			}
		}
	
		if (options.getNumber()) {
			for (int i = '0'; i <= '9'; i++) {
				bolehan[i] = true;
			}
		}
	
		if (options.getSymbol()) {
			bolehan['.'] = true;
			bolehan[','] = true;
			bolehan['?'] = true;
			bolehan['!'] = true;
			bolehan['_'] = true;
			bolehan['/'] = true;
			bolehan['@'] = true;
			bolehan['#'] = true;
			bolehan['$'] = true;
			bolehan['%'] = true;
			bolehan['&'] = true;
			bolehan['*'] = true;
		}
	
		StringBuilder jadi = new StringBuilder();
	
		for (int i = 0; i < retlen; i++) {
			if (bolehan[G[i] & 0xff]) {
				jadi.append((char) (G[i] & 0xff));
				if (jadi.length() >= len) {
					break;
				}
			}
		}
	
		if (jadi.length() < len) {
			Log.w(TAG, "perbanyak " + perbanyak + " not enough, doubling");
			return hitung(M, K, options, perbanyak*2);
		} else {
			return jadi.toString();
		}
	}

	private static byte[] md5(CharSequence s) {
		return Digester.digest(DigestType.MD5, s.toString());
	};
}
