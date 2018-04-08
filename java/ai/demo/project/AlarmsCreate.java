package ai.demo.project;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import ai.demo.project.R;

public class AlarmsCreate extends Activity
	{
	private TextView message;
	private TextView day;
	private TextView hour;
	private TextView minute;
	private TextView create;
	private TextView goback;
	private String messageValue = "";
	private int dayValue = 2;
	private int hourValue = 0;
	private int minuteValue = 0;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.alarmscreate);
		GlobalVars.lastActivity = AlarmsCreate.class;
		message = (TextView) findViewById(R.id.alarmsmessage);
			//getButtonClick(message);
		day = (TextView) findViewById(R.id.alarmsday);
			//getButtonClick(day);
		hour = (TextView) findViewById(R.id.alarmshourstime);
			//getButtonClick(hour);
		minute = (TextView) findViewById(R.id.alarmsminutestime);
			//getButtonClick(minute);
		create = (TextView) findViewById(R.id.alarmscreate);
			//getButtonClick(create);
		goback = (TextView) findViewById(R.id.goback);
			//getButtonClick(goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=6;
		GlobalVars.setText(day, false, GlobalVars.getDayName(dayValue));
		GlobalVars.setText(hour, false, GlobalVars.alarmTimeHoursValues.get(hourValue));
		GlobalVars.setText(minute, false, GlobalVars.alarmTimeMinutesValues.get(minuteValue));
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
					if(v==message)
						GlobalVars.activityItemLocation=1;
					else if(v==day)
						GlobalVars.activityItemLocation=2;
					else if(v==hour)
						GlobalVars.activityItemLocation=3;
					else if(v==minute)
						GlobalVars.activityItemLocation=4;
					else if(v==create)
						GlobalVars.activityItemLocation=5;
					else if(v==goback)
						GlobalVars.activityItemLocation=6;

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
		if (GlobalVars.inputModeResult!=null)
			{
			messageValue = GlobalVars.inputModeResult;
			GlobalVars.setText(message, false, getResources().getString(R.string.layoutAlarmsCreateMessage3) + messageValue);
			GlobalVars.inputModeResult = null;
			}
		GlobalVars.lastActivity = AlarmsCreate.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=6;
		GlobalVars.selectTextView(message,false);
		GlobalVars.selectTextView(day,false);
		GlobalVars.selectTextView(hour,false);
		GlobalVars.selectTextView(minute,false);
		GlobalVars.selectTextView(create,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutAlarmsCreateOnResume));
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //ALARM MESSAGE
			GlobalVars.selectTextView(message,true);
			GlobalVars.selectTextView(day,false);
			GlobalVars.selectTextView(goback,false);
			if (messageValue=="")
				{
				GlobalVars.talk(getResources().getString(R.string.layoutAlarmsCreateMessage2));
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutAlarmsCreateMessage4) + messageValue);
				}
			break;

			case 2: //ALARM DAY
			GlobalVars.selectTextView(day, true);
			GlobalVars.selectTextView(message,false);
			GlobalVars.selectTextView(hour,false);
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsCreateDay2) + GlobalVars.getDayName(dayValue));
			break;

			case 3: //ALARM HOURS
			GlobalVars.selectTextView(hour,true);
			GlobalVars.selectTextView(day,false);
			GlobalVars.selectTextView(minute,false);
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsCreateTimeHour2) + GlobalVars.alarmTimeHoursValues.get(hourValue) + getResources().getString(R.string.layoutAlarmsCreateHours));
			break;

			case 4: //ALARM MINUTES
			GlobalVars.selectTextView(minute,true);
			GlobalVars.selectTextView(hour,false);
			GlobalVars.selectTextView(create,false);
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsCreateTimeMinute2) + GlobalVars.alarmTimeMinutesValues.get(minuteValue) + getResources().getString(R.string.layoutAlarmsCreateMinutes));
			break;
			
			case 5: //CREATE ALARM
			GlobalVars.selectTextView(create,true);
			GlobalVars.selectTextView(minute,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsCreateCreate));
			break;
			
			case 6: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(create,false);
			GlobalVars.selectTextView(message,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}

	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //ALARM MESSAGE
			GlobalVars.startInputActivity();
			break;

			case 2: //ALARM DAY
			if (dayValue+1==8)
				{
				dayValue = 1;
				}
				else
				{
				dayValue = dayValue +1;
				}
			GlobalVars.setText(day, true, GlobalVars.getDayName(dayValue));
			GlobalVars.talk(GlobalVars.getDayName(dayValue));
			break;
			
			case 3: //ALARM HOURS
			if (hourValue+1==GlobalVars.alarmTimeHoursValues.size())
				{
				hourValue = 0;
				}
				else
				{
				hourValue = hourValue +1;
				}
			GlobalVars.setText(hour, true, GlobalVars.alarmTimeHoursValues.get(hourValue));
			GlobalVars.talk(GlobalVars.alarmTimeHoursValues.get(hourValue) + getResources().getString(R.string.layoutAlarmsCreateHours));
			break;

			case 4: //ALARM MINUTES
			if (minuteValue+1==GlobalVars.alarmTimeMinutesValues.size())
				{
				minuteValue = 0;
				}
				else
				{
				minuteValue = minuteValue +1;
				}
			GlobalVars.setText(minute, true, GlobalVars.alarmTimeMinutesValues.get(minuteValue));
			GlobalVars.talk(GlobalVars.alarmTimeMinutesValues.get(minuteValue) + getResources().getString(R.string.layoutAlarmsCreateMinutes));
			break;
			
			case 5: //CREATE ALARM
			if (messageValue=="")
				{
				GlobalVars.talk(getResources().getString(R.string.layoutAlarmsCreateErrorNoMessage));
				}
				else
				{
				String dayToSave = String.valueOf(dayValue);
				String hourToSave = GlobalVars.alarmTimeHoursValues.get(hourValue) + ":" +
									GlobalVars.alarmTimeMinutesValues.get(minuteValue);
				String messageToSave = messageValue;
				//CHECKS IF ANOTHER ALARM HAS BEEN SET FOR THE SAME DAY AND TIME
				if (checkAlarmConflict(dayToSave,hourToSave)==false)
					{
					GlobalVars.createAlarm(dayToSave + "|" + hourToSave + "|" + messageToSave);
					GlobalVars.alarmWasCreated=true;
					this.finish();
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutAlarmsCreateErrorAlarmConflict));
					}
				}
			break;

			case 6: //GO BACK TO THE PREVIOUS MENU
			this.finish();
			break;
			}
		}
	
	private void previousItem()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 2: //ALARM DAY
			if (dayValue-1<1)
				{
				dayValue = 7;
				}
				else
				{
				dayValue = dayValue - 1;
				}
			GlobalVars.setText(day, true, GlobalVars.getDayName(dayValue));
			GlobalVars.talk(GlobalVars.getDayName(dayValue));
			break;
			
			case 3: //ALARM HOURS
			if (hourValue-1<0)
				{
				hourValue = GlobalVars.alarmTimeHoursValues.size() - 1;
				}
				else
				{
				hourValue = hourValue -1;
				}
			GlobalVars.setText(hour, true,  GlobalVars.alarmTimeHoursValues.get(hourValue));
			GlobalVars.talk(GlobalVars.alarmTimeHoursValues.get(hourValue) + getResources().getString(R.string.layoutAlarmsCreateHours));
			break;

			case 4: //ALARM MINUTES
			if (minuteValue-1<0)
				{
				minuteValue = GlobalVars.alarmTimeMinutesValues.size() - 1;
				}
				else
				{
				minuteValue = minuteValue - 1;
				}
			GlobalVars.setText(minute, true,  GlobalVars.alarmTimeMinutesValues.get(minuteValue));
			GlobalVars.talk(GlobalVars.alarmTimeMinutesValues.get(minuteValue) + getResources().getString(R.string.layoutAlarmsCreateMinutes));
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

			case GlobalVars.ACTION_SELECT_PREVIOUS:
			previousItem();
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

			case GlobalVars.ACTION_SELECT_PREVIOUS:
			previousItem();
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
		
	private boolean checkAlarmConflict(String dayToSave, String hourToSave)
		{
		try
			{
			for (int i=0;i<GlobalVars.alarmList.size();i++)
				{
				if (GlobalVars.alarmList.get(i).startsWith(dayToSave + "|" + hourToSave + "|"))
					{
					return true;
					}
				}
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		return false;
		}
	}