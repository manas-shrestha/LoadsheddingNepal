package com.example.loadshedding;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SelectGroup extends Activity{
		SharedPreferences load;
		String grp;
		
		public SelectGroup() {
			// TODO Auto-generated constructor stub
			load = getSharedPreferences("filename",0);
			grp =	load.getString("group", "1");
			Log.i("FROM SELECTGROUP", grp);
		}
		

		public String returnShared(){
			return grp;
		}

}
