package com.example.loadshedding;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class SearchGroup extends ActionBarActivity {
	ActionBar actionBar;
	ListView Lv;
	EditText searchitem;
	List<String> where;
	ArrayAdapter<String> aa;
	String[] dataforlist;
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.search_groups);
		actionBar = getActionBar();
		getActionBar().setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.action_bar_layoutz2);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#1dcaff")));
		actionBar.setIcon(android.R.color.transparent);
		ImageButton menu = (ImageButton) findViewById(R.id.searchbutton);
		
		getdataforlist();
		aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				dataforlist);
		Lv = (ListView) findViewById(R.id.searchlist);
		Lv.setAdapter(aa);
		searchitem = (EditText) findViewById(R.id.searchtext);
		
		updateWeatherData("Kathmandu");
		searchitem.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				SearchGroup.this.aa.getFilter().filter(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void getdataforlist() {
		// TODO Auto-generated method stub
		where = new ArrayList<String>();
		Database_Handler db = new Database_Handler(SearchGroup.this);
		db.open();
		int l = db.noOfRecords("place");
		Log.e("l = ", l + "");

		for (int i = 0; i < l; i++) {
			where.add(db.getnameandplace(i, "place"));
		}

		db.close();
		dataforlist = new String[where.size()];

		where.toArray(dataforlist);
	}

	private void updateWeatherData(final String city) {
		new Thread() {
			public void run() {
				final JSONObject json = RemoteFetch.getJSON(SearchGroup.this,
						city);
				if (json == null) {
					handler.post(new Runnable() {
						public void run() {
							Toast.makeText(SearchGroup.this,
									"Sorry, no weather data found",
									Toast.LENGTH_LONG).show();
						}
					});
				} else {
					handler.post(new Runnable() {
						public void run() {
							renderWeather(json);
						}
					});
				}
			}
		}.start();
	}

	private void renderWeather(JSONObject json) {
		try {

			JSONObject details = json.getJSONArray("weather").getJSONObject(0);
			JSONObject main = json.getJSONObject("main");
			Log.e("Temp try", json.getString("name"));
			Log.e("Temp try", main.getDouble("temp") + " c");

		} catch (Exception e) {
			Log.e("SimpleWeather",
					"One or more fields not found in the JSON data");
		}
	}

}
