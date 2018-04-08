package ai.demo.project;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import ai.demo.project.R;

public class AlarmsDelete extends Activity
	{
	private TextView alarmname;
	private TextView delete;
	private TextView goback;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.alarmsdelete);
		GlobalVars.lastActivity = AlarmsDelete.class;
		alarmname = (TextView) findViewById(R.id.alarmsname);
			//getButtonClick(alarmname);
		delete = (TextView) findViewById(R.id.alarmsdelete);
			//getButtonClick(delete);
		goback = (TextView) findViewById(R.id.goback);
			//getButtonClick(goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		
		GlobalVars.setText(alarmname, false,
							GlobalVars.getAlarmDayName(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) + " - " +
							GlobalVars.getAlarmHours(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) + ":" +
							GlobalVars.getAlarmMinutes(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) + "\n" +
							GlobalVars.getAlarmMessage(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)));
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
					if(v==alarmname)
						GlobalVars.activityItemLocation=1;
					else if(v==delete)
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
		GlobalVars.lastActivity = AlarmsDelete.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		GlobalVars.selectTextView(alarmname,false);
		GlobalVars.selectTextView(delete,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutAlarmsDeleteOnResume));
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ ALARM
			GlobalVars.selectTextView(alarmname,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsDeleteSelected) +
							GlobalVars.getAlarmDayName(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) +
							getResources().getString(R.string.layoutAlarmsListAt) +
							GlobalVars.getAlarmHours(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) +
							getResources().getString(R.string.layoutAlarmsCreateHours) + " " +
							GlobalVars.getAlarmMinutes(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) +
							getResources().getString(R.string.layoutAlarmsCreateMinutes) + ". " +
							GlobalVars.getAlarmMessage(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)));
			break;

			case 2: //DELETE ALARM
			GlobalVars.selectTextView(delete, true);
			GlobalVars.selectTextView(alarmname,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsDeleteDelete));
			break;

			case 3: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(alarmname,false);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ ALARM
			GlobalVars.talk(getResources().getString(R.string.layoutAlarmsDeleteSelected) +
							GlobalVars.getAlarmDayName(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) +
							getResources().getString(R.string.layoutAlarmsListAt) +
							GlobalVars.getAlarmHours(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) +
							getResources().getString(R.string.layoutAlarmsCreateHours) + " " +
							GlobalVars.getAlarmMinutes(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)) +
							getResources().getString(R.string.layoutAlarmsCreateMinutes) + ". " +
							GlobalVars.getAlarmMessage(GlobalVars.alarmList.get(GlobalVars.alarmToDeleteIndex)));
			break;

			case 2: //DELETE ALARM
			GlobalVars.deleteAlarm(GlobalVars.alarmToDeleteIndex);
			GlobalVars.alarmToDeleteIndex = -1;
			GlobalVars.alarmWasDeleted=true;
			this.finish();
			break;

			case 3: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.alarmToDeleteIndex = -1;
			this.finish();
			break;
			}
		}
		

	}
