package me.tsukanov.counter;

import java.util.LinkedHashMap;
import java.util.Map;

import me.tsukanov.counter.ui.CounterFragment;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CounterApplication extends Application {
	
	private static final String DATA_FILE_NAME = "data";
	SharedPreferences data; // Where counters are stored
	
	// Counters	(String = Name, Integer = Value)
	public LinkedHashMap<String, Integer> counters;
		
	// Active counter's name (key string in LinkedHashMap)
	public String activeKey;

	// True if theme was changed or/and data removed
	public boolean isUpdateNeeded = false;

	@Override
	public void onCreate() {
		super.onCreate();
		
		counters = new LinkedHashMap<String, Integer>();
		data = getBaseContext().getSharedPreferences(DATA_FILE_NAME, Context.MODE_PRIVATE);
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		activeKey = settings.getString("activeKey", "");
		
		loadData();
	}
	
	public void loadData() {
		Map<String, ?> dataMap = data.getAll();
		if (dataMap.isEmpty()) {
			counters.put((String) getResources().getText(R.string.default_counter_name),
					CounterFragment.getDefaultValue());
		} else {
			for (Map.Entry<String, ?> entry : dataMap.entrySet())
				counters.put(entry.getKey(), (Integer) entry.getValue());
		}
	}
	
	public void saveData() {
		SharedPreferences.Editor dataEditor = data.edit();
		dataEditor.clear();
		for (String name : counters.keySet()) {
			dataEditor.putInt(name, counters.get(name));
		}
		dataEditor.commit();
	}
	

	public void clearData() {
		counters.clear();
		counters.put((String) getResources().getText(R.string.default_counter_name),
				CounterFragment.getDefaultValue());
		isUpdateNeeded = true;
	}

}