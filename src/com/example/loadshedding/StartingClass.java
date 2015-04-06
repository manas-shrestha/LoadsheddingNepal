package com.example.loadshedding;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class StartingClass extends Activity {
	String filename = "loadshedding";
	SharedPreferences LoadShedding;
	Typeface ty;
TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.starting_page_layout);
		ty = Typeface.createFromAsset(getAssets(),
				"fonts/freescript.ttf");
		tv = (TextView) findViewById(R.id.textView1);
		tv.setTypeface(ty);
		LoadShedding = getSharedPreferences(filename, 0);
		String initialization = LoadShedding.getString("Initialized", "NO");
		if (initialization.contentEquals("YES")) {
			Thread timer = new Thread(){
				public void run(){
					try{
						sleep(2000);
					}catch(InterruptedException e){
						e.printStackTrace();
					}finally{
						Intent i = new Intent(StartingClass.this, TheMainHome.class);
						finish();
						startActivity(i);
				}
			}
			
};
timer.start();

			
			
		} else {
			
				new bringouttheschedule().execute();
			
		}

	}

	public class bringouttheschedule extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog pd;
		Database_Handler dbi = new Database_Handler(getApplicationContext());
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dbi.open();
			pd = new ProgressDialog(StartingClass.this);
			pd.setCancelable(false);
			pd.setTitle("Just A Moment");
			pd.setMessage("Downloading Schedule");
			pd.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String READ_SCHEDULE = "http://binits.com.np/loadshedding/loadshedule.php";
			String READ_GROUPS = "http://binits.com.np/loadshedding/groups.php";
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = jsonParser.getJSONFromUrl(READ_SCHEDULE);
			JSONArray schedule = null;
			

			try {
				schedule = jsonObject.getJSONArray("posts");
				for (int i = 0; i < schedule.length(); i++) {
					JSONObject c = schedule.getJSONObject(i);
					String group = c.getString("group");
					String Day = c.getString("day");
					String timestart = c.getString("starttime1");
					String timestop = c.getString("stoptime1");
					String timestart2 = c.getString("starttime2");
					String timestop2 = c.getString("stoptime2");

					dbi.CreateEntry(group, Day, timestart, timestop, timestart2,
							timestop2);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			 jsonParser = new JSONParser();
			 jsonObject = jsonParser.getJSONFromUrl(READ_GROUPS);
			 schedule = null;
			

			try {
				schedule = jsonObject.getJSONArray("posts");
				for (int i = 0; i < schedule.length(); i++) {
					JSONObject c = schedule.getJSONObject(i);
					String name = c.getString("name");
					String group = c.getString("group");
					String latitude = c.getString("latitude");
					String longitude = c.getString("longitude");
					

					dbi.CreateEntryForPlacesAndGroups("place", name, group, latitude, longitude);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dbi.createEntryForEssentials("essentials", "5", Calendar.getInstance().getTimeInMillis()+"");
			dbi.close();
			LoadShedding = getSharedPreferences(filename, 0);
			SharedPreferences.Editor e = LoadShedding.edit();
			e.putString("Initialized", "YES");
			e.commit();
			Intent i = new Intent(StartingClass.this, TheMainHome.class);
			finish();
			startActivity(i);
			pd.dismiss();
		}

	}

}
