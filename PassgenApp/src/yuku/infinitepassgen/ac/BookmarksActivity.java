package yuku.infinitepassgen.ac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import yuku.afw.App;
import yuku.infinitepassgen.ac.base.BaseActivity;
import yuku.infinitepassgen.fr.BookmarksFragment;
import yuku.infinitepassgen.model.Bookmark;

public class BookmarksActivity extends BaseActivity {
	private static final String EXTRA_selectedKeyword = "selectedKeyword";
	
	public static class Result {
		public String selectedKeyword;
	}

	public static Intent createIntent() {
		return new Intent(App.context, BookmarksActivity.class);
	}
	
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(android.R.id.content, BookmarksFragment.create());
		ft.commit();
	}

	public static Result obtainResult(Intent data) {
		if (data == null) return null;
		Result res = new Result();
		res.selectedKeyword = data.getStringExtra(EXTRA_selectedKeyword);
		return res;
	}

	public void finishOk(Bookmark bookmark) {
		Intent data = new Intent();
		data.putExtra(EXTRA_selectedKeyword, bookmark.keyword);
		setResult(RESULT_OK, data);
		finish();
	}
}
