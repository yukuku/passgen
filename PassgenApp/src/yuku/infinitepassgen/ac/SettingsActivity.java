package yuku.infinitepassgen.ac;

import android.content.Intent;
import android.os.Bundle;

import yuku.afw.App;
import yuku.infinitepassgen.app.R;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

public class SettingsActivity extends SherlockPreferenceActivity {
	public static class Result {
	}

	public static Intent createIntent() {
		return new Intent(App.context, SettingsActivity.class);
	}
	
	@SuppressWarnings("deprecation") @Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.settings);
	}
}
