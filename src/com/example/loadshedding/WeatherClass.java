package com.example.loadshedding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherClass extends Activity implements OnClickListener{
	TextView Location, Info, Temp;
	ImageView icon;		ImageButton menu;
	Handler handler = new Handler();
	RelativeLayout rl;
	Dialog d ;
	Boolean isDialogOpen =false;
	Boolean isActionBarShown = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather);
		ActionBar actionBar = getActionBar();
		getActionBar().setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.action_bar_layoutz2);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#1dcaff")));
		
		
		getActionBar().setIcon(android.R.color.transparent);
		actionBar.hide();
		menu = (ImageButton) findViewById(R.id.search1);
		
		new getWeather().execute("Kathmandu");
		rl =(RelativeLayout) findViewById(R.id.rlweather101);
		menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				d= new Dialog(WeatherClass.this);
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);
				d.setContentView(R.layout.dialog_of_search);
				d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				ImageButton search = (ImageButton) d.findViewById(R.id.search2);
				final EditText searchitem = (EditText) d.findViewById(R.id.searchTextWeather);
				isDialogOpen = true;
				search.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new getWeather().execute(searchitem.getText().toString());
						
					}
				});
		
				d.show();
				
			}
		});
		
		Location = (TextView) findViewById(R.id.textView1);
		Info = (TextView) findViewById(R.id.textView2);
		Temp = (TextView) findViewById(R.id.textView3);
		icon = (ImageView) findViewById(R.id.imageView1);
	}
	
	/* private void updateWeatherData(final String city){
	    new Thread(){
	        public void run(){
	            final JSONObject json = RemoteFetch.getJSON(WeatherClass.this, city);
	            if(json == null){
	                handler.post(new Runnable(){
	                    public void run(){
	                        Toast.makeText(WeatherClass.this, 
	                                "NO SUCH CITY FOUND", 
	                                Toast.LENGTH_LONG).show(); 
	                    }
	                });
	            } else {
	                handler.post(new Runnable(){
	                    public void run(){
	                        renderWeather(json);
	                    }
	                });
	            }               
	        }
	    }.start();
	} */
	    private void renderWeather(JSONObject json){
	        try {
			Location.setText(json.getString("name").toUpperCase(Locale.US) + 
	                    ", " + 
	                    json.getJSONObject("sys").getString("country"));
	             
	            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
	            JSONObject main = json.getJSONObject("main");
	            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm a"); 
	            String sunrisehr = formatter.format(new Date( json.getJSONObject("sys").getLong("sunrise") * 1000));
	            String sunsethr = formatter.format(new Date( json.getJSONObject("sys").getLong("sunset") * 1000));
	            Info.setText(
	                    details.getString("description").toUpperCase(Locale.US) +
	                    "\n" + "Humidity: " + main.getString("humidity") + "%" +
	                    "\n" + "Pressure: " + main.getString("pressure") + " hPa" + "\n" + "Min Temperature: " + main.getString("temp_min")+ " " +(char) 0x00B0+"c \n" +
	                    "Max Temperature: " + main.getString("temp_max")+ " " +(char) 0x00B0+"c \n" + "Sunrise: " + sunrisehr +"\n" + "Sunset: " + sunsethr );
	             
	            Temp.setText(
	                        String.format("%.2f", main.getDouble("temp"))+ " " +(char) 0x00B0+"c");
	     
	           
	     
	            setWeatherIcon(details.getInt("id"),
	                    json.getJSONObject("sys").getLong("sunrise") * 1000,
	                    json.getJSONObject("sys").getLong("sunset") * 1000);
	             
	        }catch(Exception e){
	            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
	        }
	    }
	
	    private void setWeatherIcon(int actualId, long sunrise, long sunset){
	        int id = actualId / 100;
	 
	        if(actualId == 800){
	        	long currentTime = new Date().getTime();
	        	 if(currentTime>=sunrise && currentTime<sunset) {
	        		 rl.setBackgroundResource(R.drawable.sunnyweatherbg);
					
					    icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.weathersunny));
			        }else{
			        	 rl.setBackgroundResource(R.drawable.clearnightbg);
					
					    icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.weathermoon));
			        }
	            
	        } else {
	            switch(id) {
	            case 2 :
	                icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.weatherthunder));
	                rl.setBackgroundResource(R.drawable.thunderweatherbg);
	                     break;         
	            case 3 : 
		                icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.weatherdrizzle));
		                rl.setBackgroundResource(R.drawable.drizzleweatherbg);
	                     break;     
	            case 7 : 
		                icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.weatherfoggy));
		                rl.setBackgroundResource(R.drawable.foggyweatherbg);
	                     break;
	            case 8 :
	                icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.weatherclouds));
	                rl.setBackgroundResource(R.drawable.cloudyweatherbg);
	                     break;
	            case 6 : 
	                icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.weathersnow));
	                rl.setBackgroundResource(R.drawable.snowweatherbg);
	                     break;
	            case 5 : 
	                icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.weatherrain));
	                rl.setBackgroundResource(R.drawable.rainweatherbg);
	                     break;
	            }
	        }
	       
	    }

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	
	
	public class getWeather extends AsyncTask<String , Void, JSONObject>{
ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(WeatherClass.this);
			pd.setTitle("Just A Moment");
			pd.setMessage("Loading Weather...");
			pd.show();
		}
		
		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			   final JSONObject json = RemoteFetch.getJSON(WeatherClass.this, params[0]);
			
			
			
			return json;
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			renderWeather(result);
			if(isDialogOpen == true){
			d.cancel();
			}
			pd.dismiss();
		}
		
	}
	
	
	
	
}
