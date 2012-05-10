package yuku.infinitepassgen;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;

import yuku.afw.V;
import yuku.infinitepassgen.app.R;
import yuku.infinitepassgen.storage.InternalDb;
import yuku.infinitepassgen.storage.InternalDbHelper;

public class S {
	public static final String TAG = S.class.getSimpleName();
	
	private static InternalDb db;
	public static synchronized InternalDb getDb() {
		if (db == null) {
			db = new InternalDb(new InternalDbHelper());
		}
		
		return db;
	}
	
	public static Toast okToast(CharSequence message) {
		Toast toast = Toast.makeText(App.context, message, Toast.LENGTH_SHORT);
		TextView tMessage = V.get(toast.getView(), android.R.id.message);
		if (tMessage != null) {
			tMessage.setCompoundDrawablesWithIntrinsicBounds(App.context.getResources().getDrawable(R.drawable.ic_toast_ok), null, null, null);
			tMessage.setCompoundDrawablePadding((int) (App.context.getResources().getDisplayMetrics().density * 10));
		}
		toast.show();
		return toast;
	}
	
	public static AlertDialog msgDialog(Activity activity, CharSequence message) {
		return new AlertDialog.Builder(activity)
		.setMessage(message)
		.setPositiveButton("OK", null)
		.show();
	}
}
