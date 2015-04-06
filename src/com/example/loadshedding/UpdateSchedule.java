package com.example.loadshedding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class UpdateSchedule extends AsyncTask<Void, Void, Boolean> {
	String isNewScheduleAvailable = "http://binits.com.np/loadshedding/isNewScheduleAvailable.php";
	String GET_NEW_SCHEDULE = "http://binits.com.np/loadshedding/loadshedule.php";
	ProgressDialog pd;
	JSONArray newSchedule = null;
	String msg;

	Context context;

	public UpdateSchedule(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		pd = new ProgressDialog(context);
		pd.setMessage("Checking For New Schedule");
		pd.show();

		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		JSONParser parser = new JSONParser();
		JSONObject obj = parser.getJSONFromUrl(isNewScheduleAvailable);
		String message = null;

		try {
			message = obj.getString("message");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (message.contentEquals("YES")) {

			parser = new JSONParser();
			obj = parser.getJSONFromUrl(GET_NEW_SCHEDULE);

			try {
				newSchedule = obj.getJSONArray("posts");
				msg = "YES";

			} catch (JSONException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}

		} else {
			msg = "NO";
		}

		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (msg.contentEquals("YES")) {
			context.deleteDatabase("loadshedding");
			Database_Handler dbi = new Database_Handler(context);
			dbi.open();
			try {
				for (int i = 0; i < newSchedule.length(); i++) {

					JSONObject c = newSchedule.getJSONObject(i);
					String group;
					group = c.getString("group");
					String Day = c.getString("day");
					String timestart = c.getString("starttime1");
					String timestop = c.getString("stoptime1");
					String timestart2 = c.getString("starttime2");
					String timestop2 = c.getString("stoptime2");

					dbi.CreateEntry(group, Day, timestart, timestop,
							timestart2, timestop2);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Toast.makeText(context, "New Schedule Has Been Downloaded",
					Toast.LENGTH_SHORT).show();
			dbi.close();
		} else {
			Toast.makeText(context, "No New Schedule Found", Toast.LENGTH_SHORT)
					.show();
		}

		pd.dismiss();
	}

}
