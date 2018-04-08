package ai.demo.project;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import ai.demo.project.R;

public class Alarms extends Activity
	{
	private TextView list;
	private TextView create;
	private TextView goback;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.alarms);
		GlobalVars.lastActivity = Alarms.class;
		list = (TextView) findViewById(R.id.alarmslist);
			//getButtonClick(list);
		create = (TextView) findViewById(R.id.alarmscreate);
			//getButtonClick(create);
		goback = (TextView) findViewById(R.id.goback);
			//getButtonClick(goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
    	}

		public void getMenuClick(View v ){


			v.setOnClickListener(new View.OnClickListener() {
				int i = 0;
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					i++;
					Handler handler = new Handler();
					Runnable r = new Runnable() {

						@Override
						public void run() {
							i = 0;
						}
					};
					//Toast.makeText(getApplicationContext(),GlobalVars.id+"...",Toast.LENGTH_SHORT).show();
					if(v==list)
						GlobalVars.activityItemLocation=1;
					else if(v==create)
						GlobalVars.activityItemLocation=2;
					else if(v==goback)
						GlobalVars.activityItemLocation=3;

					//GlobalVars.activityItemLocation=GlobalVars.id+1;
					if (i == 1) {
						select();
						handler.postDelayed(r, 250);
					} else if (i == 2) {
						execute();
						i = 0;

					}


				}
			});

		}


		@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = Alarms.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		GlobalVars.selectTextView(list,false);
		GlobalVars.selectTextView(create,false);
		GlobalVars.selectTextView(goback,false);
		if (GlobalVars.alarmWasCreated==true)
			{
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsAlarmCreated));
			GlobalVars.alarmWasCreated=false;
			}
			else
			{
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsOnResume));
			}
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //LIST ALARMS
			GlobalVars.selectTextView(list,true);
			GlobalVars.selectTextView(create,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsList));
			break;

			case 2: //CREATE ALARM
			GlobalVars.selectTextView(create, true);
			GlobalVars.selectTextView(list,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsCreate));
			break;

			case 3: //GO BACK TO MAIN MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(list,false);
			GlobalVars.selectTextView(create,false);
			GlobalVars.talk(getResources().getString(R.string.backToMainMenu));
			break;
			}
		}
    
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //LIST ALARMS
			GlobalVars.startActivity(AlarmsList.class);
			break;

			case 2: //CREATE ALARM
			GlobalVars.alarmWasCreated=false;
			GlobalVars.startActivity(AlarmsCreate.class);
			break;

			case 3: //GO BACK TO MAIN MENU
			this.finish();
			break;
			}
		}



		@Override public boolean onTouchEvent(MotionEvent event)
		{
			int result = GlobalVars.detectMovement(event);
			switch (result)
			{
				case GlobalVars.ACTION_SELECT:
					select();
					break;


				case GlobalVars.ACTION_EXECUTE:
					execute();
					break;
			}
			return super.onTouchEvent(event);
		}

		public boolean onKeyUp(int keyCode, KeyEvent event)
		{
			int result = GlobalVars.detectKeyUp(keyCode);
			switch (result)
			{
				case GlobalVars.ACTION_SELECT:
					select();
					break;



				case GlobalVars.ACTION_EXECUTE:
					execute();
					break;
			}
			return super.onKeyUp(keyCode, event);
		}

		public boolean onKeyDown(int keyCode, KeyEvent event)
		{
			return GlobalVars.detectKeyDown(keyCode);
		}
		

	}
