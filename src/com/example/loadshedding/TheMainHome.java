package com.example.loadshedding;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

public class TheMainHome extends FragmentActivity implements
		ActionBar.TabListener, OnClickListener {
	private ViewPager viewPager;
	private TabPagerAdapter mAdapter;

	private android.app.ActionBar actionBar;
	private String[] tabs = { "1", "2", "3", "4", "5", "6", "7" };
	Camera camera;
	boolean isFlashOn = false;
	Parameters p;
	int Counter = 1;
	SharedPreferences load;
	String filename = "loadshedding";
	ImageButton flashlightib;
	RadioGroup rg, rg2;
	RadioButton select, select2;
	Typeface helvetica;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub

		super.onCreate(arg0);
		setContentView(R.layout.view_pager_element);

		viewPager = (ViewPager) findViewById(R.id.pager);

		actionBar = getActionBar();
		getActionBar().setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		getActionBar().setIcon(android.R.color.transparent);
		actionBar.setCustomView(R.layout.action_bar_layoutz);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#1dcaff")));
		actionBar.setDisplayUseLogoEnabled(false);

		mAdapter = new TabPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		helvetica = Typeface.createFromAsset(getAssets(),
				"fonts/freescript.ttf");
		TextView title = (TextView) findViewById(R.id.titleofapp);
		title.setTypeface(helvetica);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		LayoutInflater inflater = getLayoutInflater();

		for (String tab_name : tabs) {

			// actionBar.addTab(actionBar.newTab().setText(tab_name)
			// .setTabListener(this));

			Tab tab = actionBar.newTab().setTabListener(this);
			//
			// View tabView = inflater.inflate(R.layout.layout_for_tabs, null);
			// tabView.setBackgroundColor(Color.BLUE); // set custom color
			// tab.setCustomView(tabView);

			// tab.setCustomView(R.layout.layout_for_tabs);
			switch (Counter) {
			case 1:
				tab.setIcon(R.drawable.g1);
				break;
			case 2:
				tab.setIcon(R.drawable.g2);
				break;
			case 3:
				tab.setIcon(R.drawable.g3);
				break;
			case 4:
				tab.setIcon(R.drawable.g4);
				break;
			case 5:
				tab.setIcon(R.drawable.g5);
				break;
			case 6:
				tab.setIcon(R.drawable.g6);
				break;
			case 7:
				tab.setIcon(R.drawable.g7);
				break;
			}

			actionBar.addTab(tab);
			Counter++;

		}
		camera = Camera.open();
		p = camera.getParameters();

		ImageButton menu = (ImageButton) findViewById(R.id.imageButton1);
		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Dialog d = new Dialog(TheMainHome.this);

				d.setTitle("Menu");
				d.requestWindowFeature(Window.FEATURE_NO_TITLE);

				d.setContentView(R.layout.dialog);
				ImageButton findgroupib = (ImageButton) d
						.findViewById(R.id.imageButton3);
				ImageButton exitib = (ImageButton) d
						.findViewById(R.id.imageButton7);
				ImageButton settings = (ImageButton) d
						.findViewById(R.id.imageButton5);
				ImageButton weathericon = (ImageButton) d
						.findViewById(R.id.imageButton6);
				ImageButton updateib = (ImageButton) d
						.findViewById(R.id.imageButton1);
				flashlightib = (ImageButton) d.findViewById(R.id.imageButton2);
				ImageButton mapib = (ImageButton) d
						.findViewById(R.id.imageButton4);

				if (isFlashOn == true) {
					flashlightib.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.flashlighticonon));
				} else {
					flashlightib.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.flashlighticon));
				}

				weathericon.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(TheMainHome.this,
								WeatherClass.class);
						startActivity(i);
					}
				});

				exitib.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						AlertDialog.Builder builder = new AlertDialog.Builder(
								TheMainHome.this);
						builder.setTitle("You Are About To Exit.");
						builder.setMessage("Are you sure?");

						builder.setPositiveButton("YES",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// Do nothing but close the dialog
										dialog.dismiss();
										if (isFlashOn == true) {

											p.setFlashMode(Parameters.FLASH_MODE_OFF);
											camera.setParameters(p);
											camera.stopPreview();
											flashlightib
													.setImageBitmap(BitmapFactory
															.decodeResource(
																	getResources(),
																	R.drawable.flashlighticon));
											isFlashOn = false;
										}
										Intent intent = new Intent(
												Intent.ACTION_MAIN);
										intent.addCategory(Intent.CATEGORY_HOME);
										intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										startActivity(intent);

									}

								});

						builder.setNegativeButton("NO",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// Do nothing
										dialog.dismiss();
									}
								});

						AlertDialog alert = builder.create();
						alert.show();
					}
				});

				settings.setOnClickListener(new OnClickListener() {
					Dialog d;

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						d = new Dialog(TheMainHome.this);
						d.setContentView(R.layout.selectgroup);
						d.setTitle("Settings");
						rg = (RadioGroup) d.findViewById(R.id.radiogroup);
						rg2 = (RadioGroup) d
								.findViewById(R.id.radiogroupfornotifications);
						Button confirm = (Button) d
								.findViewById(R.id.buttonconfirm);
						Button confirm2 = (Button) d
								.findViewById(R.id.buttonconfirm2);
						rg.check(R.id.radioButton1);
						rg2.check(R.id.radioButton1n);
						load = getSharedPreferences(filename, 0);

						confirm.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								int id = rg.getCheckedRadioButtonId();
								select = (RadioButton) rg.findViewById(id);
								Database_Handler db = new Database_Handler(
										TheMainHome.this);
								db.open();
								String lastupdated = db.getLastUpdated(0);
								db.editentry("essentials", select.getText()
										.toString(), lastupdated);
								db.close();
								d.dismiss();

							}
						});

						confirm2.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String notificationstatus = load
										.getString("notificationstatus",
												"not_initialized");

								Log.e("k payo", notificationstatus);

								int id = rg2.getCheckedRadioButtonId();
								select = (RadioButton) rg2.findViewById(id);
								String checked = select.getText().toString();
								if (checked.contentEquals("5 mins")) {
									stopService(new Intent(TheMainHome.this,
											MyServices.class));
									Log.e("asdasdasdasdasda", checked);
									SharedPreferences.Editor yoloEditor = load.edit();
									yoloEditor.putString("notifyTime", checked);
									yoloEditor.commit();
									startService(new Intent(TheMainHome.this,
											MyServices.class));

								}
								if (checked.contentEquals("10 mins")) {
									stopService(new Intent(TheMainHome.this,
											MyServices.class));
									Log.e("asdasdasdasdasda", checked);
									SharedPreferences.Editor yoloEditor = load.edit();
									yoloEditor.putString("notifyTime", checked);
									yoloEditor.commit();
									startService(new Intent(TheMainHome.this,
											MyServices.class));

								}
								if (checked.contentEquals("15 mins")) {
									stopService(new Intent(TheMainHome.this,
											MyServices.class));
									Log.e("asdasdasdasdasda", checked);
									SharedPreferences.Editor yoloEditor = load.edit();
									yoloEditor.putString("notifyTime", checked);
									yoloEditor.commit();
									startService(new Intent(TheMainHome.this,
											MyServices.class));

								}
								if (checked.contentEquals("30 mins")) {
									stopService(new Intent(TheMainHome.this,
											MyServices.class));
									Log.e("asdasdasdasdasda", checked);
									SharedPreferences.Editor yoloEditor = load.edit();
									yoloEditor.putString("notifyTime", checked);
									yoloEditor.commit();
									startService(new Intent(TheMainHome.this,
											MyServices.class));

								}
								if (checked.contentEquals("1 hr")) {
									stopService(new Intent(TheMainHome.this,
											MyServices.class));
									Log.e("asdasdasdasdasda", checked);
									SharedPreferences.Editor yoloEditor = load.edit();
									yoloEditor.putString("notifyTime", checked);
									yoloEditor.commit();
									startService(new Intent(TheMainHome.this,
											MyServices.class));

								}
								if (checked.contentEquals("Off")) {

									stopService(new Intent(TheMainHome.this,
											MyServices.class));
								}

							}
						});

						d.show();
					}
				});

				findgroupib.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(TheMainHome.this,
								SearchGroup.class);
						startActivity(i);
					}
				});

				mapib.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(TheMainHome.this,
								GroupsInMaps.class);
						startActivity(i);
					}
				});

				updateib.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new UpdateSchedule(TheMainHome.this).execute();
					}
				});
				flashlightib.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (isFlashOn == false) {

							p.setFlashMode(Parameters.FLASH_MODE_TORCH);
							camera.setParameters(p);
							camera.startPreview();
							flashlightib.setImageBitmap(BitmapFactory
									.decodeResource(getResources(),
											R.drawable.flashlighticonon));
							isFlashOn = true;
						} else if (isFlashOn == true) {

							p.setFlashMode(Parameters.FLASH_MODE_OFF);
							camera.setParameters(p);
							camera.stopPreview();
							flashlightib.setImageBitmap(BitmapFactory
									.decodeResource(getResources(),
											R.drawable.flashlighticon));
							isFlashOn = false;
						}

					}
				});
				d.show();

			}
		});

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				actionBar.setSelectedNavigationItem(arg0);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// do something on back.
			AlertDialog.Builder builder = new AlertDialog.Builder(
					TheMainHome.this);
			builder.setTitle("You Are About To Exit.");
			builder.setMessage("Are you sure?");

			builder.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// Do nothing but close the dialog
							dialog.dismiss();
							if (isFlashOn == true) {

								p.setFlashMode(Parameters.FLASH_MODE_OFF);
								camera.setParameters(p);
								camera.stopPreview();
								flashlightib.setImageBitmap(BitmapFactory
										.decodeResource(getResources(),
												R.drawable.flashlighticon));
								isFlashOn = false;
							}
							Intent intent = new Intent(Intent.ACTION_MAIN);
							intent.addCategory(Intent.CATEGORY_HOME);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);

						}

					});

			builder.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Do nothing
							dialog.dismiss();
						}
					});

			AlertDialog alert = builder.create();
			alert.show();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}
