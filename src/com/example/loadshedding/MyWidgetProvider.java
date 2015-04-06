package com.example.loadshedding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.loadshedding.ListManager.items;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class MyWidgetProvider extends AppWidgetProvider {
	private PendingIntent service = null;
	private static final String ACTION_CLICK = "ACTION_CLICK";
	String UPDATE_TIME_FILTER = "com.example.loadshedding.UPDATE";
	SharedPreferences load;
	String filename = "loadshedding";
	String GROUP;
	RemoteViews remoteViews;
	Handler handler = new Handler();
	JSONObject json;
	int[] widgetId;
	AppWidgetManager widgetManager;

	@Override
	public void onReceive(Context context, Intent intent) {

		load = PreferenceManager.getDefaultSharedPreferences(context);
		GROUP = load.getString("group", "1");

		widgetManager = AppWidgetManager.getInstance(context);
		ComponentName widgetComponent = new ComponentName(
				context.getPackageName(), this.getClass().getName());
		widgetId = widgetManager.getAppWidgetIds(widgetComponent);
		int widgetnum = widgetId.length;

		remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
			Intent updateIntent = new Intent(UPDATE_TIME_FILTER);
			PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(
					context, 0, updateIntent, 0);
			AlarmManager alarmManager = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			alarmManager.setRepeating(AlarmManager.RTC,
					System.currentTimeMillis() + 1000, 30000,
					pendingUpdateIntent);

		} else if (intent.getAction().equals(UPDATE_TIME_FILTER)) {
			for (int i = 0; i < widgetnum; i++) {
				Database_Handler Schedule = new Database_Handler(context);
				Schedule.open();
				Long TimeUpdated = Long.valueOf(Schedule.getLastUpdated(0));
				String grp = Schedule.getUserGroup(0);
				Log.e("TimeUpdated", TimeUpdated + " " + grp);
				Date cputime = null;
				Date sheduletimeStart1 = null;
				Date sheduletimeStop1 = null;
				Date sheduletimeStart2 = null;
				Date sheduletimeStop2 = null;
				SimpleDateFormat dfo = new SimpleDateFormat(
						"dd-MM-yyyy hh:mm a");
				SimpleDateFormat dfo4 = new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat dfo3 = new SimpleDateFormat("hh:mm a");
				SimpleDateFormat dfo5 = new SimpleDateFormat("EEE");

				Calendar c = Calendar.getInstance();

				c.add(Calendar.DATE, 1); // number of days to add
				String Today = dfo5.format(Calendar.getInstance().getTime());
				String cpuPlusOneDay = dfo4.format(c.getTime());
				Log.e("dd-MM-yyyy", cpuPlusOneDay);
				String cpuCurrentDate = dfo4.format(Calendar.getInstance()
						.getTime());
				Log.e("dd-MM-yyyy", cpuCurrentDate);
				String cpuTime = dfo3.format(Calendar.getInstance().getTime());
				try {
					cputime = dfo.parse(cpuCurrentDate + " " + cpuTime);

					sheduletimeStart1 = dfo.parse(cpuCurrentDate
							+ " "
							+ Schedule.selectbydaystart1(getPresentDay(Today),
									grp));
					sheduletimeStop1 = dfo.parse(cpuCurrentDate
							+ " "
							+ Schedule.selectbydaystop1(getPresentDay(Today),
									grp));
					sheduletimeStart2 = dfo.parse(cpuCurrentDate
							+ " "
							+ Schedule.selectbydaystart2(getPresentDay(Today),
									grp));
					// check if stop time 2 lies on the following day
					if (Schedule.selectbydaystop2(getPresentDay(Today), grp)
							.contentEquals("12:00 AM")) {
						sheduletimeStop2 = dfo.parse(cpuPlusOneDay
								+ " "
								+ Schedule.selectbydaystop2(
										getPresentDay(Today), grp));
					} else {
						sheduletimeStop2 = dfo.parse(cpuCurrentDate
								+ " "
								+ Schedule.selectbydaystop2(
										getPresentDay(Today), grp));
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Set the text
				remoteViews.setTextViewText(R.id.time,
						dfo3.format(Calendar.getInstance().getTime()));
				remoteViews
						.setTextViewText(
								R.id.dateandday,
								dfo5.format(Calendar.getInstance().getTime())
										+ ", "
										+ dfo4.format(Calendar.getInstance()
												.getTime()));
				remoteViews.setTextViewText(
						R.id.first,
						Schedule.selectbydaystart1(getPresentDay(Today), grp)
								+ " - "
								+ Schedule.selectbydaystop1(
										getPresentDay(Today), grp));
				remoteViews.setTextViewText(
						R.id.second,
						Schedule.selectbydaystart2(getPresentDay(Today), grp)
								+ " - "
								+ Schedule.selectbydaystop2(
										getPresentDay(Today), grp));
				remoteViews.setTextViewText(R.id.timeremaining, "  ON  ");

				String cpuTimeForWeather = dfo.format(Calendar.getInstance()
						.getTime());
				JSONObject hh;

				try {
					Log.e("DIff", (TimeUpdated - Calendar.getInstance()
							.getTimeInMillis()) + "");
					if ((Calendar.getInstance().getTimeInMillis() - TimeUpdated) > 40000) {
						Schedule.editentry("essentials", grp, Calendar
								.getInstance().getTimeInMillis() + "");
						Log.e("Every Time Yetae chiryo", "sdfsdf");
						updateWeatherData(context, "Kathmandu", i);

					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				remoteViews.setImageViewBitmap(R.id.imageView2, BitmapFactory
						.decodeResource(context.getResources(),
								R.drawable.lighton));

				if ((cputime.getTime() > sheduletimeStart1.getTime() && cputime
						.getTime() < sheduletimeStop1.getTime())
						|| (cputime.getTime() > sheduletimeStart2.getTime() && cputime
								.getTime() < sheduletimeStop2.getTime())) {

					remoteViews
							.setImageViewBitmap(R.id.imageView2, BitmapFactory
									.decodeResource(context.getResources(),
											R.drawable.lightoff));

					if (cputime.getTime() < sheduletimeStop1.getTime()) {
						try {
							remoteViews.setTextViewText(
									R.id.timeremaining,
									tezy(sheduletimeStop1.getTime()
											- cputime.getTime()));

						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						try {
							remoteViews.setTextViewText(
									R.id.timeremaining,
									tezy(sheduletimeStop2.getTime()
											- cputime.getTime()));

						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} else if (cputime.getTime() < sheduletimeStart1.getTime()) {
					try {

						remoteViews.setTextViewText(
								R.id.timeremaining,
								tezy(sheduletimeStart1.getTime()
										- cputime.getTime()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if ((cputime.getTime() > sheduletimeStop1.getTime())
						&& cputime.getTime() < sheduletimeStart2.getTime()) {
					try {
						remoteViews.setTextViewText(
								R.id.timeremaining,
								tezy(sheduletimeStart2.getTime()
										- cputime.getTime()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if ((cputime.getTime() > sheduletimeStop2.getTime())) {
					try {
						String NextDay = dfo5.format(c.getTime());
						Log.e("dd-MM-yyyy", NextDay);
						Date sheduletimeStartnextday = dfo.parse(cpuPlusOneDay
								+ " "
								+ Schedule.selectbydaystart1(
										getNextDay(NextDay), GROUP));
						remoteViews.setTextViewText(R.id.timeremaining,
								tezy(sheduletimeStartnextday.getTime()
										- cputime.getTime()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				Schedule.close();
				widgetManager.updateAppWidget(widgetId[i], remoteViews);
			}

		} else if (intent.getAction().equals(
				AppWidgetManager.ACTION_APPWIDGET_DISABLED)) {
			Intent updaIntent = new Intent(UPDATE_TIME_FILTER);
			PendingIntent pendingUdateIntent = PendingIntent.getBroadcast(
					context, 0, updaIntent, 0);
			AlarmManager alarmManager = (AlarmManager) context
					.getSystemService(context.ALARM_SERVICE);
			alarmManager.cancel(pendingUdateIntent);
		}

	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		load = context.getSharedPreferences(filename, 0);
		GROUP = load.getString("group", "1");
		Log.e("GROUP IN APPWIDGET", GROUP);
		Log.i("onUpdate", "onUpdate");
	}

	public String tezy(long e) throws ParseException {

		SimpleDateFormat dfo3 = new SimpleDateFormat("hh:mm a");
		String cputimesasdad = dfo3.format(Calendar.getInstance().getTime());
		SimpleDateFormat dfo = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		Date cputime = dfo3.parse(cputimesasdad);
		long millis = e;
		String hms = String.format(
				"%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
								.toHours(millis)));

		return hms;
	}

	public class MyServicey extends Service {
		@Override
		public void onCreate() {
			super.onCreate();
		}

		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			buildUpdate();

			return super.onStartCommand(intent, flags, startId);
		}

		private void buildUpdate() {
			String lastUpdated = DateFormat.format("hh:mm:ss", new Date())
					.toString();

			RemoteViews view = new RemoteViews(getPackageName(),
					R.layout.widget_layout);
			view.setTextViewText(R.id.timeremaining, lastUpdated);

			ComponentName thisWidget = new ComponentName(this,
					MyWidgetProvider.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			manager.updateAppWidget(thisWidget, view);
		}

		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}
	}

	public String getNextDay(String day) {

		if (day.contentEquals("Sun")) {
			return "Monday";
		} else if (day.contentEquals("Mon")) {
			return "Tuesday";
		} else if (day.contentEquals("Tue")) {
			return "Wednesday";
		} else if (day.contentEquals("Wed")) {
			return "Thursday";
		} else if (day.contentEquals("Thu")) {
			return "Friday";
		} else if (day.contentEquals("Fri")) {
			return "Saturday";
		} else if (day.contentEquals("Sat")) {
			return "Sunday";
		} else {
			return null;
		}

	}

	public String getPresentDay(String day) {

		if (day.contentEquals("Mon")) {
			return "Monday";
		} else if (day.contentEquals("Tue")) {
			return "Tuesday";
		} else if (day.contentEquals("Wed")) {
			return "Wednesday";
		} else if (day.contentEquals("Thu")) {
			return "Thursday";
		} else if (day.contentEquals("Fri")) {
			return "Friday";
		} else if (day.contentEquals("Sat")) {
			return "Saturday";
		} else if (day.contentEquals("Sun")) {
			return "Sunday";
		} else {
			return null;
		}

	}

	private JSONObject updateWeatherData(final Context contexts,
			final String city, final int i) {

		new Thread() {
			public void run() {
				json = RemoteFetch.getJSON(contexts, city);
				if (json == null) {
					handler.post(new Runnable() {
						public void run() {
							Toast.makeText(contexts,
									"Sorry, no weather data found",
									Toast.LENGTH_LONG).show();
						}
					});
				} else {
					handler.post(new Runnable() {
						public void run() {
							renderWeather(json, i, contexts);
						}
					});
				}
			}
		}.start();

		return json;
	}

	private void renderWeather(JSONObject json, int i, Context contexts) {
		try {

			JSONObject details = json.getJSONArray("weather").getJSONObject(0);
			JSONObject main = json.getJSONObject("main");
			long sunrise = json.getJSONObject("sys").getLong("sunrise") * 1000;
			long sunset = json.getJSONObject("sys").getLong("sunset") * 1000;
			Log.e("Temp try", json.getString("name"));
			String desc = "NA";
			int y = details.getInt("id");
			int id = y / 100;
			if (y == 800) {
				long currentTime = new Date().getTime();
				if (currentTime >= sunrise && currentTime < sunset) {
					remoteViews.setImageViewBitmap(R.id.weathericon,
							BitmapFactory.decodeResource(
									contexts.getResources(),
									R.drawable.weathersunny));
					remoteViews.setInt(R.id.layout, "setBackgroundResource",
							R.drawable.sunnyweatherbg);
				} else {
					remoteViews.setImageViewBitmap(R.id.weathericon,
							BitmapFactory.decodeResource(
									contexts.getResources(),
									R.drawable.weathermoon));
					remoteViews.setInt(R.id.layout, "setBackgroundResource",
							R.drawable.clearnightbg);
				}

				desc = details.getString("description");
			} else {
				switch (id) {
				case 2:
					remoteViews.setImageViewBitmap(R.id.weathericon,
							BitmapFactory.decodeResource(
									contexts.getResources(),
									R.drawable.weatherthunder));
					remoteViews.setInt(R.id.layout, "setBackgroundResource",
							R.drawable.thunderweatherbg);
					desc = details.getString("description");
					break;
				case 3:
					remoteViews.setImageViewBitmap(R.id.weathericon,
							BitmapFactory.decodeResource(
									contexts.getResources(),
									R.drawable.weatherdrizzle));
					remoteViews.setInt(R.id.layout, "setBackgroundResource",
							R.drawable.drizzleweatherbg);
					desc = details.getString("description");
					break;
				case 7:
					remoteViews.setImageViewBitmap(R.id.weathericon,
							BitmapFactory.decodeResource(
									contexts.getResources(),
									R.drawable.weatherfoggy));
					remoteViews.setInt(R.id.layout, "setBackgroundResource",
							R.drawable.foggyweatherbg);
					desc = details.getString("description");
					break;
				case 8:
					remoteViews.setImageViewBitmap(R.id.weathericon,
							BitmapFactory.decodeResource(
									contexts.getResources(),
									R.drawable.weatherclouds));
					remoteViews.setInt(R.id.layout, "setBackgroundResource",
							R.drawable.cloudyweatherbg);
					desc = details.getString("description");
					break;
				case 6:
					remoteViews.setImageViewBitmap(R.id.weathericon,
							BitmapFactory.decodeResource(
									contexts.getResources(),
									R.drawable.weathersnow));
					remoteViews.setInt(R.id.layout, "setBackgroundResource",
							R.drawable.snowweatherbg);
					desc = details.getString("description");
					break;
				case 5:
					remoteViews.setImageViewBitmap(R.id.weathericon,
							BitmapFactory.decodeResource(
									contexts.getResources(),
									R.drawable.weatherrain));
					remoteViews.setInt(R.id.layout, "setBackgroundResource",
							R.drawable.rainweatherbg);
					desc = details.getString("description");
					break;
				}
			}
			remoteViews.setTextViewText(R.id.weathertv,
					((int) main.getDouble("temp")) + "" + (char) 0x00B0 + "c");
			remoteViews.setTextViewText(R.id.weatherdesc, desc.toUpperCase());
			widgetManager.updateAppWidget(widgetId[i], remoteViews);
		} catch (Exception e) {
			Log.e("SimpleWeather",
					"One or more fields not found in the JSON data");
		}
	}

}
