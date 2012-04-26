package yuku.infinitepassgen.fr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.ToggleButton;

import java.util.Date;
import java.util.List;

import yuku.afw.App;
import yuku.afw.V;
import yuku.infinitepassgen.S;
import yuku.infinitepassgen.app.R;
import yuku.infinitepassgen.fr.base.BaseFragment;
import yuku.infinitepassgen.lib.v3.PwgenV3;
import yuku.infinitepassgen.lib.v3.PwgenV3Options;
import yuku.infinitepassgen.model.Bookmark;
import yuku.infinitepassgen.widget.BaseTextWatcher;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MainFragment extends BaseFragment {
	public static final String TAG = MainFragment.class.getSimpleName();
	
	public MainFragment() {
	}
	
	public static MainFragment create() {
		return new MainFragment();
	}
	
	TextView tMaster;
	TextView tKeyword;
	TextView tResult;
	Button bReveal;
	TextView lOptions;
	ToggleButton cShowOptions;
	View panelOptions;
	TextView lMin;
	TextView lMax;
	SeekBar sbMin;
	SeekBar sbMax;
	CheckBox cUpper;
	CheckBox cLower;
	CheckBox cNumber;
	CheckBox cSymbol;
	Button bDefault;
	
	String result_data;
	boolean result_revealing;

	private BaseTextWatcher tMaster_textChanged = new BaseTextWatcher() {
		@Override public void afterTextChanged(Editable s) {
			calculateResult();
			displayResultWithHiding();
		}
	};

	private BaseTextWatcher tKeyword_textChanged = new BaseTextWatcher() {
		@Override public void afterTextChanged(Editable s) {
			calculateResult();
			displayResultWithHiding();
		}
	};

	private OnTouchListener bReveal_touch = new OnTouchListener() {
		@Override public boolean onTouch(View v, MotionEvent event) {
			int action = event.getActionMasked();
			if (action == MotionEvent.ACTION_DOWN) {
				result_revealing = true;
				displayResultWithHiding();
			} else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
				result_revealing = false;
				displayResultWithHiding();
			}
			return false;
		}
	};
	
	private OnCheckedChangeListener cShowOptions_checkedChange = new OnCheckedChangeListener() {
		@Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			displayPanelOptions();
		}
	};
	
	private OnSeekBarChangeListener sbMin_seekBarChange = new OnSeekBarChangeListener() {
		@Override public void onStopTrackingTouch(SeekBar seekBar) {}
		@Override public void onStartTrackingTouch(SeekBar seekBar) {}
		@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (progress > sbMax.getProgress()) {
				sbMax.setProgress(progress);
			}
			updateMinMaxLabels();
			updateOptionsSummary();
			calculateResult();
			displayResultWithHiding();
		}
	};
	
	private OnSeekBarChangeListener sbMax_seekBarChange = new OnSeekBarChangeListener() {
		@Override public void onStopTrackingTouch(SeekBar seekBar) {}
		@Override public void onStartTrackingTouch(SeekBar seekBar) {}
		@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (progress < sbMin.getProgress()) {
				sbMin.setProgress(progress);
			}
			updateMinMaxLabels();
			updateOptionsSummary();
			calculateResult();
			displayResultWithHiding();
		}
	};
	
	private OnCheckedChangeListener cUpper_checkedChange = new OnCheckedChangeListener() {
		@Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			updateOptionsSummary();
			calculateResult();
			displayResultWithHiding();
		}
	};
	
	private OnCheckedChangeListener cLower_checkedChange = new OnCheckedChangeListener() {
		@Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			updateOptionsSummary();
			calculateResult();
			displayResultWithHiding();
		}
	};

	private OnCheckedChangeListener cNumber_checkedChange = new OnCheckedChangeListener() {
		@Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			updateOptionsSummary();
			calculateResult();
			displayResultWithHiding();
		}
	};
	
	private OnCheckedChangeListener cSymbol_checkedChange = new OnCheckedChangeListener() {
		@Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			updateOptionsSummary();
			calculateResult();
			displayResultWithHiding();
		}
	};
	
	private OnClickListener bDefault_click = new OnClickListener() {
		@Override public void onClick(View v) {
			sbMin.setProgress(5);
			sbMax.setProgress(5);
			updateMinMaxLabels();
			cUpper.setChecked(true);
			cLower.setChecked(true);
			cNumber.setChecked(true);
			cSymbol.setChecked(false);
			updateOptionsSummary();
			calculateResult();
			displayResultWithHiding();
		}
	};
	
	private void displayPanelOptions() {
		if (cShowOptions.isChecked()) {
			panelOptions.setVisibility(View.VISIBLE);
		} else {
			panelOptions.setVisibility(View.GONE);
		}
	}
	
	protected void updateMinMaxLabels() {
		lMin.setText("Minimum: " + (sbMin.getProgress() + 3));
		lMax.setText("Minimum: " + (sbMax.getProgress() + 3));
	}

	private void updateOptionsSummary() {
		SpannableStringBuilder sb = new SpannableStringBuilder();
		sb.append("Options: ");
		sb.setSpan(new StyleSpan(Typeface.BOLD), 0, sb.length(), 0);
		
		int min = sbMin.getProgress() + 3;
		int max = sbMax.getProgress() + 3;
		boolean upper = cUpper.isChecked();
		boolean lower = cLower.isChecked();
		boolean number = cNumber.isChecked();
		boolean symbol = cSymbol.isChecked();
		
		if (!upper && !lower && !number && !symbol) {
			sb.append("Please select one of the possible character sets!");
		} else {
			if (min == max) {
				sb.append(String.valueOf(min));
			} else {
				sb.append(min + "–" + max);
			}
			sb.append(" ");
			int c = 0;
			if (upper) {
				if (c != 0) sb.append("/");
				sb.append("uppercase");
				c++;
			}
			if (lower) {
				if (c != 0) sb.append("/");
				sb.append("lowercase");
				c++;
			}
			if (upper || lower) {
				sb.append(" letters");
			}
			if (number) {
				if (c != 0) sb.append("/");
				sb.append("numbers");
				c++;
			}
			if (symbol) {
				if (c != 0) sb.append("/");
				sb.append("symbols");
				c++;
			}
			
			if (upper && lower && number && !symbol && min == 8 && max == 8) {
				sb.append(" (Default)");
			}
		}
		lOptions.setText(sb, BufferType.SPANNABLE);
	}
	
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_main, menu);
	}
	
	@Override public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.menuLoad) {
			final List<Bookmark> bookmarks = S.getDb().getAllBookmarks();
			if (bookmarks.size() == 0) {
				S.msgDialog(getActivity(), "You have no saved keywords.");
			} else {
				String[] items = new String[bookmarks.size()];
				for (int i = 0; i < items.length; i++) {
					items[i] = bookmarks.get(i).keyword;
				}
				new AlertDialog.Builder(getActivity())
				.setItems(items, new DialogInterface.OnClickListener() {
					@Override public void onClick(DialogInterface dialog, int which) {
						applyBookmarkToWidgets(bookmarks.get(which));
					}
				})
				.show();
			}
			return true;
		} else if (itemId == R.id.menuSave) {
			final String keyword = tKeyword.getText().toString();
			if (keyword.trim().length() == 0) {
				S.msgDialog(getActivity(), "Keyword is blank.");
			} else {
				final Bookmark old = S.getDb().getBookmarkByKeyword(keyword);
				if (old == null) {
					Bookmark neu = Bookmark.create(keyword, null, getOptionsFromWidgets(), new Date(), new Date());
					S.getDb().putBookmark(neu);
					S.okToast("\"" + keyword + "\" saved.");
				} else {
					// TODO don't prompt if the existing data is exactly equal with this
					new AlertDialog.Builder(getActivity())
					.setMessage("\"" + keyword + "\" was previously saved. Do you want to overwrite it?")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override public void onClick(DialogInterface dialog, int which) {
							// update data
							// TODO old.note = ...
							old.updatedTime = new Date();
							old.options = getOptionsFromWidgets();
							S.getDb().putBookmark(old);
							S.okToast("\"" + keyword + "\" updated.");
						}
					})
					.setNegativeButton("Cancel", null)
					.show();
				}
			}
			return true;
		} else if (itemId == R.id.menuAbout) {
			S.msgDialog(getActivity(), "Infinite Password Generator " + App.getVersionName());
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	void applyBookmarkToWidgets(Bookmark bookmark) {
		tKeyword.setText(bookmark.keyword);
		applyOptionsToWidgets(bookmark.options);
	}
	
	void applyOptionsToWidgets(PwgenV3Options options) {
		sbMin.setProgress(options.getMin() - 3);
		sbMax.setProgress(options.getMax() - 3);
		updateMinMaxLabels();
		
		cUpper.setChecked(options.getUpper());
		cLower.setChecked(options.getLower());
		cNumber.setChecked(options.getNumber());
		cSymbol.setChecked(options.getSymbol());
		updateOptionsSummary();
	}

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View res = inflater.inflate(R.layout.fragment_main, container, false);
		tMaster = V.get(res, R.id.tMaster);
		tMaster.addTextChangedListener(tMaster_textChanged);
		tKeyword = V.get(res, R.id.tKeyword);
		tKeyword.addTextChangedListener(tKeyword_textChanged);
		tResult = V.get(res, R.id.tResult);
		bReveal = V.get(res, R.id.bReveal);
		bReveal.setOnTouchListener(bReveal_touch);
		lOptions = V.get(res, R.id.lOptions);
		cShowOptions = V.get(res, R.id.cShowOptions);
		cShowOptions.setOnCheckedChangeListener(cShowOptions_checkedChange);
		panelOptions = V.get(res, R.id.panelOptions);
		lMin = V.get(res, R.id.lMin);
		lMax = V.get(res, R.id.lMax);
		sbMin = V.get(res, R.id.sbMin);
		sbMin.setOnSeekBarChangeListener(sbMin_seekBarChange);
		sbMax = V.get(res, R.id.sbMax);
		sbMax.setOnSeekBarChangeListener(sbMax_seekBarChange);
		cUpper = V.get(res, R.id.cUpper);
		cUpper.setOnCheckedChangeListener(cUpper_checkedChange);
		cLower = V.get(res, R.id.cLower);
		cLower.setOnCheckedChangeListener(cLower_checkedChange);
		cNumber = V.get(res, R.id.cNumber);
		cNumber.setOnCheckedChangeListener(cNumber_checkedChange);
		cSymbol = V.get(res, R.id.cSymbol);
		cSymbol.setOnCheckedChangeListener(cSymbol_checkedChange);
		bDefault = V.get(res, R.id.bDefault);
		bDefault.setOnClickListener(bDefault_click);
		
		displayPanelOptions();
		updateMinMaxLabels();
		updateOptionsSummary();
		return res;
	}

	protected void calculateResult() {
		String master = tMaster.getText().toString();
		String keyword = tKeyword.getText().toString();
		if (master.length() != 0 && keyword.length() != 0) {
			PwgenV3Options options = getOptionsFromWidgets();
			
			result_data = PwgenV3.hitung(master, keyword, options, 8);
		}
	}

	private PwgenV3Options getOptionsFromWidgets() {
		int min = sbMin.getProgress() + 3;
		int max = sbMax.getProgress() + 3;
		boolean upper = cUpper.isChecked();
		boolean lower = cLower.isChecked();
		boolean number = cNumber.isChecked();
		boolean symbol = cSymbol.isChecked();
		
		PwgenV3Options res = new PwgenV3Options();
		res.setMin(min);
		res.setMax(max);
		res.setUpper(upper);
		res.setLower(lower);
		res.setNumber(number);
		res.setSymbol(symbol);
		return res;
	}

	private void displayResultWithHiding() {
		if (result_revealing) {
			tResult.setText(result_data);
		} else {
			StringBuilder sb = new StringBuilder();  
			for (int i = 0; i < (result_data != null? result_data.length(): 0); i++) {
				sb.append('\u2022');
			}
			tResult.setText(sb);
		}
	}
}
