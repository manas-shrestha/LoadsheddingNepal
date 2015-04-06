package com.example.loadshedding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.drive.internal.OnResourceIdSetResponse;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class  ListManager extends Fragment {
	ListView loadshedding;
	List<items> ab = new ArrayList<items>();
	ImageView onoff;
	String group;
	PrepareList adapter;
	int counter = 0;
	private Handler mHandler;
	boolean isRunRunning = false;

	public ListManager() {
		// TODO Auto-generated constructor stub
		group = "2";
	}
	public ListManager(String Grp) {
		// TODO Auto-generated constructor stub
		group = Grp;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(savedInstanceState != null){
		group = savedInstanceState.getString("group");
		}
		Log.e("group uuuuuu",group);
		View v = inflater.inflate(R.layout.listview_for_main, container, false);
		loadshedding = (ListView) v.findViewById(R.id.listView1);
		adapter = new PrepareList();
		mHandler = new Handler();
		loadshedding.setAdapter(adapter);
		
	
		return v;
	}

	public static class items {
		String Day;
		String Shift;
		String Start;
		String Stop;
		String Start2;
		String Stop2;
	}

	
	
	public class PrepareList extends BaseAdapter {
		List<items> ab = getdataforthelist();

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 7;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return ab.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater.inflate(R.layout.layout_for_shedule_list, arg2,
					false);
			TextView tv = (TextView) arg1.findViewById(R.id.textView1);
			TextView tvshift1 = (TextView) arg1.findViewById(R.id.textView2);
			TextView tvshift2 = (TextView) arg1.findViewById(R.id.textView3);
			TextView count = (TextView) arg1.findViewById(R.id.counter);
			onoff = (ImageView) arg1.findViewById(R.id.imageView1);
			items jpt = ab.get(position);
			SimpleDateFormat df = new SimpleDateFormat("EEE");
			String date = df.format(Calendar.getInstance().getTime());
			count.setVisibility(View.INVISIBLE);
			String string = jpt.Day;
			String[] day = string.split("");
			int tag = 0;
			String con = "";
			for (int i = 0; i < 4; i++) {
				String word = day[i];
				word.toLowerCase();
				con = con + word;

			}
			onoff.setImageBitmap(BitmapFactory.decodeResource(getResources(),
					R.drawable.lighton));

			if (date.contentEquals(con)) {
				arg1.setBackgroundColor(Color.parseColor("#c0deed"));

				SimpleDateFormat dfo = new SimpleDateFormat(
						"dd-MM-yyyy hh:mm a");
				SimpleDateFormat dfo4 = new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat dfo3 = new SimpleDateFormat("hh:mm a");
				String cputimesasdad = dfo3.format(Calendar.getInstance()
						.getTime());
				Calendar c = Calendar.getInstance();
				
				c.add(Calendar.DATE, 1);  // number of days to add
			
				String cpuPlusOneDay = dfo4
						.format(c.getTime());
				String cputimes = dfo.format(Calendar.getInstance().getTime());
				String cpuCurrentDate = dfo4.format(Calendar.getInstance().getTime());
				Date cputime = null;
				Date sheduletimeStart1 = null;
				Date sheduletimeStop1 = null;
				Date sheduletimeStart2 = null;
				Date sheduletimeStop2 = null;
				
				try {
					cputime = dfo.parse(cpuCurrentDate+" " + cputimesasdad);
					// Log.e("add ", "21-01-2015 " + cputimesasdad);
					sheduletimeStart1 = dfo.parse(cpuCurrentDate+" "+ jpt.Start);
					sheduletimeStop1 = dfo.parse(cpuCurrentDate+" "+ jpt.Stop);
					sheduletimeStart2 = dfo.parse(cpuCurrentDate+" "+jpt.Start2);
					sheduletimeStop2 = dfo.parse(cpuCurrentDate+" "+jpt.Stop2);
					long timeInMilliseconds = sheduletimeStart1.getTime();

					
					if(jpt.Stop2.contentEquals("12:00 AM")){
						sheduletimeStop2 = dfo.parse(cpuPlusOneDay + " " + jpt.Stop2);
					}else{
					sheduletimeStop2 = dfo.parse(cpuCurrentDate + " " +jpt.Stop2);
					}
					/*
					 * Log.e("Start", sheduletimeStart2.getTime() + " " +
					 * sheduletimeStart2.getHours()); Log.e("Current",
					 * cputime.getTime() + " " + cputime.getHours());
					 * Log.e("Stop", sheduletimeStop2.getTime() + " " +
					 * sheduletimeStop2.getHours());
					 */
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

				if ((cputime.getTime() > sheduletimeStart1.getTime() && cputime
						.getTime() < sheduletimeStop1.getTime())  || (cputime.getTime() > sheduletimeStart2.getTime() && cputime
								.getTime() < sheduletimeStop2.getTime())) {
					/*
					 * Toast.makeText( getActivity(), "Current time" + cputimes
					 * + "\n Start time " + jpt.Start + "\n Stop Time " +
					 * jpt.Stop + "\n", Toast.LENGTH_SHORT).show();
					 */
					
					onoff.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.lightoff));
					count.setVisibility(View.VISIBLE);
					
					
					if(cputime
							.getTime() < sheduletimeStop1.getTime()){
					try {
						count.setText(tezy(sheduletimeStop1.getTime()
								- cputime.getTime()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					}else{
						try {
							count.setText(tezy(sheduletimeStop2.getTime()
									- cputime.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					
					
					if (isRunRunning == false) {
						startRepeatingTask();

						isRunRunning = true;
					}
				}else if (cputime.getTime() < sheduletimeStart1.getTime()) {
					try {
						count.setVisibility(View.VISIBLE);
						count.setText(
								tezy(sheduletimeStart1.getTime()
										- cputime.getTime()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if ((cputime.getTime() > sheduletimeStop1.getTime())
						&& cputime.getTime() < sheduletimeStart2.getTime()) {
					try {
						count.setVisibility(View.VISIBLE);
						count.setText(
								tezy(sheduletimeStart2.getTime()
										- cputime.getTime()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if ((cputime.getTime() > sheduletimeStop2.getTime())) {
					try {
						SimpleDateFormat dfo5 = new SimpleDateFormat("EEE");
						count.setVisibility(View.VISIBLE);
						String NextDay = dfo5.format(c.getTime());
						Log.e("dd-MM-yyyy", NextDay);
						Database_Handler Schedule = new Database_Handler(getActivity());
						Schedule.open();
						Date sheduletimeStartnextday = dfo.parse(cpuPlusOneDay
								+ " "
								+ Schedule.selectbydaystart1(
										getNextDay(NextDay), group));
						count.setText(tezy(sheduletimeStartnextday.getTime()
										- cputime.getTime()));
						Schedule.close();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
//
			}

			tv.setText(jpt.Day);
			tvshift1.setText(jpt.Start + " - " + jpt.Stop);
			tvshift2.setText(jpt.Start2 + " - " + jpt.Stop2);

			return arg1;
		}

	}

	public List<items> getdataforthelist() {
		Database_Handler Schedule = new Database_Handler(getActivity());
		Schedule.open();
		for (int i = 0; i < 7; i++) {
			items abe = new items();
			abe.Day = Schedule.getday(i, "" + group);
			abe.Start = Schedule.firststart(i, "" + group);
			abe.Stop = Schedule.firststop(i, "" + group);
			abe.Start2 = Schedule.secondstart(i, "" + group);
			abe.Stop2 = Schedule.secondstop(i, "" + group);
			ab.add(abe);

		}
		return ab;

	}

	private final Runnable mRunnable = new Runnable() {
		public void run() {
			counter++;

			// notify that data has been changed
			adapter.notifyDataSetChanged();

			// update every second
			mHandler.postDelayed(this, 10000);

		}

	};

	void startRepeatingTask() {
		Log.e("", "LFooped");
		mRunnable.run();
		Log.e("", "Looped");
	}

	void stopRepeatingTask() {
		mHandler.removeCallbacks(mRunnable);
		Log.e("", "Looped");
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
	
	
@Override
public void onSaveInstanceState(Bundle outState) {
	// TODO Auto-generated method stub
	super.onSaveInstanceState(outState);
	outState.putString("group", group);
}



}
