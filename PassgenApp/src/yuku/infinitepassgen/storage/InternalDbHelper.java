package yuku.infinitepassgen.storage;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import yuku.infinitepassgen.storage.Table.Bookmark;

public class InternalDbHelper extends yuku.afw.storage.InternalDbHelper {
	public static final String TAG = InternalDbHelper.class.getSimpleName();
	
	@Override public void createTables(SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder(200);
		sb.append("create table " + Bookmark.tableName() + " ( _id integer primary key "); //$NON-NLS-1$ //$NON-NLS-2$
		for (Bookmark field: Bookmark.values()) {
			sb.append(',');
			sb.append(field.name());
			sb.append(' ');
			sb.append(field.type.name());
			if (field.suffix != null) {
				sb.append(' ');
				sb.append(field.suffix);
			}
		}
		sb.append(")"); //$NON-NLS-1$
		db.execSQL(sb.toString());
	}

	@Override public void createIndexes(SQLiteDatabase db) {
		db.execSQL("create unique index " + Bookmark.tableName() + "_" + Bookmark.keyword.name() + "_unique on " + Bookmark.tableName() + " (" + Bookmark.keyword.name() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		db.execSQL("create index " + Bookmark.tableName() + "_" + Bookmark.ordering.name() + "_index on " + Bookmark.tableName() + " (" + Bookmark.ordering.name() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
	
	@Override public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);

		db.execSQL("PRAGMA synchronous=OFF"); //$NON-NLS-1$
	}
	
	@Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		super.onUpgrade(db, oldVersion, newVersion);
		
		Log.d(TAG, "onUpgrade " + oldVersion + " " + newVersion);  //$NON-NLS-1$//$NON-NLS-2$
	}
}
