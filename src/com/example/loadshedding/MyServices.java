package com.example.loadshedding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyServices extends Service {
	long mInterval = 10000; // at begining
	Handler mHandler;
	private static final String TAG = null;
	SharedPreferences load;
	String filename = "loadshedding";
	private static final int MY_NOTIFICATION_ID = 0;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {

		Toast.makeText(this, "Congrats! MyService Created", Toast.LENGTH_LONG)
				.show();
		Log.d(TAG, "onCreate");
		mHandler = new Handler();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");
		startRepeatingTask();
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
		stopRepeatingTask();
	}

	Runnable mStatusChecker = new Runnable() {
		@Override
		public void run() {
			load = getSharedPreferences(filename, 0);
			String notificationtime = load.getString("notifyTime", "NOT GOT");
			long threshold = 0;
			Log.e("NOTIFY TIME", load.getString("notifyTime", "NOT GOT"));
			if(notificationtime.contentEquals("5 mins")){
				threshold = 300000;
			}
			if(notificationtime.contentEquals("10 mins")){
				threshold = 600000;
			}
			if(notificationtime.contentEquals("15 mins")){
				threshold = 900000;
			}
			if(notificationtime.contentEquals("30 mins")){
				threshold = 1800000;
			}
			if(notificationtime.contentEquals("1 hr")){
				threshold = 3600000;
			}
			
			Log.e("THRESHOLD",threshold+"");
			// adssdsdsdsad

			Database_Handler Schedule = new Database_Handler(MyServices.this);
			Schedule.open();
			Long TimeUpdated = Long.valueOf(Schedule.getLastUpdated(0));
			String grp = Schedule.getUserGroup(0);
			Log.e("TimeUpdated", TimeUpdated + " " + grp);
			Date cputime = null;
			Date sheduletimeStart1 = null;
			Date sheduletimeStop1 = null;
			Date sheduletimeStart2 = null;
			Date sheduletimeStop2 = null;
			SimpleDateFormat dfo = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
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

				sheduletimeStart1 = dfo
						.parse(cpuCurrentDate
								+ " "
								+ Schedule.selectbydaystart1(
										getPresentDay(Today), grp));
				sheduletimeStop1 = dfo.parse(cpuCurrentDate + " "
						+ Schedule.selectbydaystop1(getPresentDay(Today), grp));
				sheduletimeStart2 = dfo
						.parse(cpuCurrentDate
								+ " "
								+ Schedule.selectbydaystart2(
										getPresentDay(Today), grp));
				// check if stop time 2 lies on the following day
				if (Schedule.selectbydaystop2(getPresentDay(Today), grp)
						.contentEquals("12:00 AM")) {
					sheduletimeStop2 = dfo.parse(cpuPlusOneDay
							+ " "
							+ Schedule.selectbydaystop2(getPresentDay(Today),
									grp));
				} else {
					sheduletimeStop2 = dfo.parse(cpuCurrentDate
							+ " "
							+ Schedule.selectbydaystop2(getPresentDay(Today),
									grp));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String cpuTimeForWeather = dfo.format(Calendar.getInstance()
					.getTime());
			JSONObject hh;

			long oo = Calendar.getInstance().getTimeInMillis();
			long io = Calendar.getInstance().getTimeInMillis();
			Log.i("LOAD SHEDDINF STARTED", "LOAD SHEDDING STARTED " + io);
			Log.i("LOAD SHEDDINF STARTED", "LOAD SHEDDING STARTED " + oo);

			if ((((sheduletimeStart1.getTime() - Calendar.getInstance()
					.getTimeInMillis()) <= threshold) && ((sheduletimeStart1
					.getTime() - Calendar.getInstance().getTimeInMillis()) > (threshold - 11000)))
					|| (((sheduletimeStart2.getTime() - Calendar.getInstance()
							.getTimeInMillis()) <= threshold) && ((sheduletimeStart2
							.getTime() - Calendar.getInstance()
							.getTimeInMillis()) > (threshold -11000)))) {

				long o = Calendar.getInstance().getTimeInMillis();
				long i = Calendar.getInstance().getTimeInMillis();
				Log.i("LOAD SHEDDINF STARTED inside", "LOAD SHEDDING STARTED "
						+ i);
				Log.i("LOAD SHEDDINF STARTED inside", "LOAD SHEDDING STARTED "
						+ o);

				NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				@SuppressWarnings("deprecation")
				Notification myNotification = new Notification(
						R.drawable.weatherclouds, "Notification!",
						System.currentTimeMillis());
				Context context = getApplicationContext();
				String notificationTitle = ("LoadShedding");
				String notificationText = ("LoadShedding Begins in ");
				;
				String myBlog = ("message");
				Intent myIntent = new Intent(MyServices.this,
						TheMainHome.class);
				PendingIntent pendingIntent = PendingIntent.getActivity(
						context, 0, myIntent, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				myNotification.defaults |= Notification.DEFAULT_SOUND;
				myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
				myNotification.setLatestEventInfo(context, notificationTitle,
						notificationText, pendingIntent);
				notificationManager.notify(MY_NOTIFICATION_ID, myNotification);

				// remoteViews.setImageViewBitmap(R.id.imageView2, BitmapFactory
				// .decodeResource(context.getResources(),
				// R.drawable.lightoff));

				// if (cputime.getTime() < sheduletimeStop1.getTime()) {
				// try {
				// // remoteViews.setTextViewText(
				// // R.id.timeremaining,
				// // tezy(sheduletimeStop1.getTime()
				// // - cputime.getTime()));
				//
				// } catch (ParseException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// } else {
				// try {
				// // remoteViews.setTextViewText(
				// // R.id.timeremaining,
				// // tezy(sheduletimeStop2.getTime()
				// // - cputime.getTime()));
				// //
				// } catch (ParseException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }
				//
				// } else if (cputime.getTime() < sheduletimeStart1.getTime()) {
				// try {
				//
				// // remoteViews.setTextViewText(
				// // R.id.timeremaining,
				// // tezy(sheduletimeStart1.getTime()
				// // - cputime.getTime()));
				// } catch (ParseException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// } else if ((cputime.getTime() > sheduletimeStop1.getTime())
				// && cputime.getTime() < sheduletimeStart2.getTime()) {
				// try {
				// // remoteViews.setTextViewText(
				// // R.id.timeremaining,
				// // tezy(sheduletimeStart2.getTime()
				// // - cputime.getTime()));
				// } catch (ParseException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// } else if ((cputime.getTime() > sheduletimeStop2.getTime()))
				// {
				// try {
				// String NextDay = dfo5.format(c.getTime());
				// Log.e("dd-MM-yyyy", NextDay);
				// Date sheduletimeStartnextday = dfo.parse(cpuPlusOneDay
				// + " "
				// + Schedule.selectbydaystart1(getNextDay(NextDay),
				// grp));
				// // remoteViews.setTextViewText(
				// // R.id.timeremaining,
				// // tezy(sheduletimeStartnextday.getTime()
				// // - cputime.getTime()));
				// } catch (ParseException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}

			Schedule.close();

			// sdsdsdsdsds
			mHandler.postDelayed(mStatusChecker, mInterval);
		}
	};

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

	void startRepeatingTask() {
		Log.e("", "Started");
		mStatusChecker.run();

	}

	void stopRepeatingTask() {
		mHandler.removeCallbacks(mStatusChecker);
		Log.e("", "Stopped");
	}
}
