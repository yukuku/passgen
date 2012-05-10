package yuku.infinitepassgen.widget;

import android.text.Editable;
import android.text.TextWatcher;

public class BaseTextWatcher implements TextWatcher {
	public static final String TAG = BaseTextWatcher.class.getSimpleName();

	@Override public void afterTextChanged(Editable s) {}
	
	@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	
	@Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
