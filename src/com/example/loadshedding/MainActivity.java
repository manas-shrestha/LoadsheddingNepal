package com.example.loadshedding;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	Button b, submit, show;
	long mInterval = 2000; // at begining
	Handler mHandler;
	EditText groupet, dayet, shiftet, timestopet, timestartet, timestartet2,
			timestopet2;
	MediaPlayer oursoung;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		b = (Button) findViewById(R.id.button1);

		submit = (Button) findViewById(R.id.button2);
		show = (Button) findViewById(R.id.button3);

		groupet = (EditText) findViewById(R.id.editText6);
		dayet = (EditText) findViewById(R.id.editText1);
		shiftet = (EditText) findViewById(R.id.editText2);
		timestartet = (EditText) findViewById(R.id.editText3);
		timestopet = (EditText) findViewById(R.id.editText4);
		timestartet2 = (EditText) findViewById(R.id.editText5);
		timestopet2 = (EditText) findViewById(R.id.editText7);

		mHandler = new Handler(); // in oncreate
		// startService(new Intent(MainActivity.this, MyServices.class));
		// startRepeatingTask();

		show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Database_Handler db = new Database_Handler(MainActivity.this);
				db.open();

				Log.e("Data", db.getData());
				db.close();
			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Database_Handler db = new Database_Handler(MainActivity.this);

				db.open();
				Log.e("Time start pathaunu bhnda agadi", timestartet.getText()
						.toString());
				db.CreateEntry(groupet.getText().toString(), dayet.getText()
						.toString(), timestartet.getText().toString(),
						timestopet.getText().toString(), timestartet2.getText()
								.toString(), timestopet2.getText().toString());

				db.close();
			}
		});

		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	Runnable mStatusChecker = new Runnable() {
		@Override
		public void run() {

			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			String date = df.format(Calendar.getInstance().getTime());

			String str1 = "10:57";

			java.util.Date date1 = null;
			java.util.Date date2 = null;
			try {
				date2 = formatter.parse(date);
				date1 = formatter.parse(str1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (date1.compareTo(date2) < 0) {
				System.out.println("date2 is Greater than my date1");
				oursoung.start();
			}

			// here should be the code to repeat in background
			mHandler.postDelayed(mStatusChecker, mInterval);
		}
	};

	void startRepeatingTask() {
		Log.e("", "Started");
		mStatusChecker.run();

	}

	void stopRepeatingTask() {
		mHandler.removeCallbacks(mStatusChecker);
		Log.e("", "Stopped");
	}

}
