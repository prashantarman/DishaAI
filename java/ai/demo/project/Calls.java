package ai.demo.project;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class Calls extends Activity
	{
	private TextView callslist;
	private TextView deletecalls;
	private TextView makeacall;
	private TextView goback;
	   
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.calls);
		GlobalVars.lastActivity = Calls.class;
		callslist = (TextView) findViewById(R.id.callslist);
			//getButtonClick(callslist);
		deletecalls = (TextView) findViewById(R.id.deletecalllogs);
			//getButtonClick(deletecalls);
		makeacall = (TextView) findViewById(R.id.makeacall);
			//getButtonClick(makeacall);
		goback = (TextView) findViewById(R.id.goback);
			//getButtonClick(goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
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
					if(v==callslist)
						GlobalVars.activityItemLocation=1;
					else if(v==deletecalls)
						GlobalVars.activityItemLocation=2;
					else if(v==makeacall)
						GlobalVars.activityItemLocation=3;
					else if(v==goback)
						GlobalVars.activityItemLocation=4;

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
		GlobalVars.lastActivity = Calls.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		GlobalVars.selectTextView(callslist,false);
		GlobalVars.selectTextView(deletecalls,false);
		GlobalVars.selectTextView(makeacall,false);
		GlobalVars.selectTextView(goback,false);
		if (GlobalVars.callLogsDeleted==true)
			{
			GlobalVars.talk(getResources().getString(R.string.layoutCallsOnResume2));
			GlobalVars.callLogsDeleted=false;
			}
			else
			{
			GlobalVars.talk(getResources().getString(R.string.layoutCallsOnResume));
			}
		}

	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //LIST CALL LOGS
			GlobalVars.selectTextView(callslist,true);
			GlobalVars.selectTextView(deletecalls,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutCallsMissedCallsList));
			break;

			case 2: //DELETE ALL CALL LOGS
			GlobalVars.selectTextView(deletecalls,true);
			GlobalVars.selectTextView(callslist,false);
			GlobalVars.selectTextView(makeacall,false);
			GlobalVars.talk(getResources().getString(R.string.layoutCallsDeleteCallLogs2));
			break;
			
			case 3: //MAKE A CALL
			GlobalVars.selectTextView(makeacall, true);
			GlobalVars.selectTextView(deletecalls,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutCallsMakeCall));
			break;

			case 4: //GO BACK TO THE MAIN MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(callslist,false);
			GlobalVars.selectTextView(makeacall,false);
			GlobalVars.talk(getResources().getString(R.string.backToMainMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //LIST CALL LOGS
			GlobalVars.startActivity(CallsLogs.class);
			break;
			
			case 2: //DELETE ALL CALL LOGS
			GlobalVars.startActivity(CallsLogsDelete.class);
			break;

			case 3: //MAKE A CALL
			GlobalVars.startActivity(CallsMake.class);
			break;

			case 4: //GO BACK TO THE MAIN MENU
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