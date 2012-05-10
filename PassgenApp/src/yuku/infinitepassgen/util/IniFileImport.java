package yuku.infinitepassgen.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yuku.infinitepassgen.lib.v3.PwgenV3Options;
import yuku.infinitepassgen.model.Bookmark;

public class IniFileImport {
	public static final String TAG = IniFileImport.class.getSimpleName();
	
	public static List<Bookmark> parseIniFileLines(List<String> lines) {
		List<Bookmark> res = new ArrayList<Bookmark>();
		
		// create section-entry map
		Map<String, Map<String, String>> sections = new HashMap<String, Map<String, String>>();
		
		{ // parse lines and convert to sections 
			Map<String, String> section_content = null;
			String section_title = null;
			
			for (String line: lines) {
				if (line.startsWith("[") && line.endsWith("]")) {
					section_title = line.substring(1, line.length() - 1);
					section_content = new HashMap<String, String>();
					sections.put(section_title, section_content);
				} else if (line.contains("=") && !line.startsWith(";")) {
					// special: for no section
					if (section_title == null) {
						section_title = "global";
						section_content = new HashMap<String, String>();
						sections.put(section_title, section_content);
					}
					
					String[] split = line.split("=", 2);
					String key = split[0];
					key = key.replace("\\\\", "\\").replace("\\ ", " ");
					String val = split[1];
					val = val.replace("\\n", "\n").replace("\\\\", "\\");
					section_content.put(key, val);
				} else {
					Log.d(TAG, "ignored line: " + line);
				}
			}
		}
		
		// process sections
		Map<String, String> dfBuk = sections.get("dfBuk");
		if (dfBuk != null) {
			String n_s = dfBuk.get("n");
			if (n_s != null) {
				int n = Integer.parseInt(n_s);
				Date date = new Date();
				for (int i = 0; i < n; i++) {
					Map<String, String> bookmark_section = sections.get("dfBuk/" + i);
					if (bookmark_section != null) {
						String keyword = bookmark_section.get("keyword");
						String note = bookmark_section.get("note");
						String min_s = bookmark_section.get("min");
						int min = Integer.parseInt(min_s);
						String max_s = bookmark_section.get("max");
						int max = Integer.parseInt(max_s);
						String flag_s = bookmark_section.get("flag");
						int flag = Integer.parseInt(flag_s);
						PwgenV3Options options = new PwgenV3Options();
						options.setMin(min);
						options.setMax(max);
						options.setFlags(flag);
						Bookmark bookmark = Bookmark.create(keyword, note, options, date, date);
						res.add(bookmark);
					}
				}
			}
		}
		
		return res;
	}
}
