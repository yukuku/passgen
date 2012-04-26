package yuku.infinitepassgen.ac;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import yuku.infinitepassgen.ac.base.BaseActivity;
import yuku.infinitepassgen.fr.MainFragment;

public class MainActivity extends BaseActivity {
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(android.R.id.content, MainFragment.create());
		ft.commit();
	}
}
