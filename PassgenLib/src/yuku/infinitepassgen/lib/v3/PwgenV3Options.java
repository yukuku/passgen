package yuku.infinitepassgen.lib.v3;

import org.json.JSONException;
import org.json.JSONObject;

public class PwgenV3Options {
	public static final String TAG = PwgenV3Options.class.getSimpleName();

	private int min;
	private int max;
	private int flags;
	
	public int getMin() {
		return min;
	}
	
	public void setMin(int min) {
		this.min = min;
	}
	
	public int getMax() {
		return max;
	}
	
	public void setMax(int max) {
		this.max = max;
	}
	
	public int getFlags() {
		return flags;
	}
	
	public void setFlags(int flags) {
		this.flags = flags;
	}
	
	public boolean getUpper() {
		return (this.flags & PwgenV3.KAR_UPPER) != 0;
	}
	
	public void setUpper(boolean on) {
		this.flags = on? (this.flags | PwgenV3.KAR_UPPER): (this.flags & ~PwgenV3.KAR_UPPER);
	}
	
	public boolean getLower() {
		return (this.flags & PwgenV3.KAR_LOWER) != 0;
	}
	
	public void setLower(boolean on) {
		this.flags = on? (this.flags | PwgenV3.KAR_LOWER): (this.flags & ~PwgenV3.KAR_LOWER);
	}
	
	public boolean getNumber() {
		return (this.flags & PwgenV3.KAR_NUMBER) != 0;
	}
	
	public void setNumber(boolean on) {
		this.flags = on? (this.flags | PwgenV3.KAR_NUMBER): (this.flags & ~PwgenV3.KAR_NUMBER);
	}
	
	public boolean getSymbol() {
		return (this.flags & PwgenV3.KAR_SYMBOL) != 0;
	}
	
	public void setSymbol(boolean on) {
		this.flags = on? (this.flags | PwgenV3.KAR_SYMBOL): (this.flags & ~PwgenV3.KAR_SYMBOL);
	}
	
	@Override public boolean equals(Object o) {
		if (!(o instanceof PwgenV3Options)) return false;
		PwgenV3Options other = (PwgenV3Options) o;
		return this.min == other.min && this.max == other.max && this.flags == other.flags;
	}
	
	public String dumpToString() {
		JSONObject json = new JSONObject();
		try {
			json.put("min", min);
			json.put("max", max);
			json.put("flags", flags);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return json.toString();
	}
	
	public static PwgenV3Options loadFromString(String dump) {
		PwgenV3Options res = new PwgenV3Options();
		try {
			JSONObject json = new JSONObject(dump);
			res.min = json.optInt("min");
			res.max = json.optInt("max");
			res.flags = json.optInt("flags");
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return res;
	}
}
