package yuku.infinitepassgen.storage;

import static yuku.infinitepassgen.storage.Table.Type.*;

public class Table {
	public static final String TAG = Table.class.getSimpleName();

	public enum Type {
		integer,
		real,
		text,
		blob,
	}
	
	public enum Bookmark {
		version(integer),
		keyword(text, "collate nocase"),
		note(text),
		options(text),
		ordering(integer), 
		createdTime(integer),
		updatedTime(integer),
		;
		
		public final Type type;
		public final String suffix;
		
		private Bookmark(Type type) {
			this(type, null);
		}
		
		private Bookmark(Type type, String suffix) {
			this.type = type;
			this.suffix = suffix;
		}
		
		public static String tableName() {
			return Bookmark.class.getSimpleName();
		}
	}
}
