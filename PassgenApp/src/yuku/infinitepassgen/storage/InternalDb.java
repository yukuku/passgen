package yuku.infinitepassgen.storage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import yuku.infinitepassgen.model.Bookmark;


public class InternalDb extends yuku.afw.storage.InternalDb {
	public static final String TAG = InternalDb.class.getSimpleName();
	
	public InternalDb(InternalDbHelper helper) {
		super(helper);
	}
	
	// begin Bookmark methods
	
	public List<Bookmark> getAllBookmarks() {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<Bookmark> res = new ArrayList<Bookmark>();
		Cursor c = db.query(Table.Bookmark.tableName(), null, null, null, null, null, Table.Bookmark.ordering.name() + " asc, " + Table.Bookmark.keyword.name() + " asc");
		try {
			int[] columnMap = Bookmark.getColumnMap(c);
			while (c.moveToNext()) {
				res.add(Bookmark.fromCursor(c, columnMap));
			}
			return res;
		} finally {
			c.close();
		}
	}
	
	public Bookmark getBookmarkByKeyword(String keyword) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(Table.Bookmark.tableName(), null, Table.Bookmark.keyword.name() + "=?", new String[] {keyword}, null, null, null);
		try {
			if (c.moveToNext()) {
				return Bookmark.fromCursor(c, null);
			}
			return null;
		} finally {
			c.close();
		}
	}

	public void putBookmark(Bookmark bookmark) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if (bookmark._id == 0) {
			long _id = db.insert(Table.Bookmark.tableName(), null, bookmark.toContentValues(null));
			if (_id > 0) {
				bookmark._id = _id;
			}
		} else {
			db.update(Table.Bookmark.tableName(), bookmark.toContentValues(null), "_id=?", new String[] {String.valueOf(bookmark._id)});
		}
	}

	public void deleteBookmarkById(long _id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(Table.Bookmark.tableName(), "_id=?", new String[] {String.valueOf(_id)});
	}
	
	// end
}
