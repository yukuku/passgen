package yuku.infinitepassgen.model;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import yuku.infinitepassgen.lib.v3.PwgenV3Options;
import yuku.infinitepassgen.storage.Table;

public class Bookmark {
	public static final String TAG = Bookmark.class.getSimpleName();
	
	public long _id;
	public int version;
	public String keyword;
	public String note;
	public PwgenV3Options options;
	public int ordering;
	public Date createdTime;
	public Date updatedTime;
	
	private Bookmark() {
	}
	
	public static Bookmark create(String keyword, String note, PwgenV3Options options, Date createdTime, Date updatedTime) {
		Bookmark res = new Bookmark();
		res.version = 3; // auto fill in
		res.keyword = keyword;
		res.note = note;
		res.options = options;
		res.ordering = 0; // auto fill in
		res.createdTime = createdTime;
		res.updatedTime = updatedTime;
		return res;
	}

	public ContentValues toContentValues(ContentValues reuse) {
		ContentValues res = reuse != null? reuse: new ContentValues();
		if (_id != 0) res.put("_id", _id);
		res.put(Table.Bookmark.version.name(), version);
		res.put(Table.Bookmark.keyword.name(), keyword);
		res.put(Table.Bookmark.note.name(), note);
		res.put(Table.Bookmark.options.name(), options.dumpToString());
		res.put(Table.Bookmark.ordering.name(), ordering);
		res.put(Table.Bookmark.createdTime.name(), createdTime.getTime());
		res.put(Table.Bookmark.updatedTime.name(), updatedTime.getTime());
		return res;
	}
	
	public static int[] getColumnMap(Cursor c) {
		return new int[] {
			c.getColumnIndexOrThrow("_id"),
			c.getColumnIndexOrThrow(Table.Bookmark.version.name()),
			c.getColumnIndexOrThrow(Table.Bookmark.keyword.name()),
			c.getColumnIndexOrThrow(Table.Bookmark.note.name()),
			c.getColumnIndexOrThrow(Table.Bookmark.options.name()),
			c.getColumnIndexOrThrow(Table.Bookmark.ordering.name()),
			c.getColumnIndexOrThrow(Table.Bookmark.createdTime.name()),
			c.getColumnIndexOrThrow(Table.Bookmark.updatedTime.name()),
		};
	}
	
	public static Bookmark fromCursor(Cursor c, int[] columnMapOptional) {
		int[] columnMap = columnMapOptional != null? columnMapOptional: getColumnMap(c);
		Bookmark res = new Bookmark();
		res._id = c.getLong(columnMap[0]);
		res.version = c.getInt(columnMap[1]);
		res.keyword = c.getString(columnMap[2]);
		res.note = c.getString(columnMap[3]);
		res.options = PwgenV3Options.loadFromString(c.getString(columnMap[4]));
		res.ordering = c.getInt(columnMap[5]);
		res.createdTime = new Date(c.getLong(columnMap[6]));
		res.updatedTime = new Date(c.getLong(columnMap[7]));
		return res;
	}
}
