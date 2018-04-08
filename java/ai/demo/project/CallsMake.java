package ai.demo.project;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import ai.demo.project.R;

public class CallsMake extends Activity
	{
	private TextView phonenumber;
	private TextView makecall;
	private TextView goback;
	private String phoneValue = "";
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.callsmake);
		GlobalVars.lastActivity = CallsMake.class;
		phonenumber = (TextView) findViewById(R.id.phonenumber);
			//getButtonClick(phonenumber);
		makecall = (TextView) findViewById(R.id.makecall);
			//getButtonClick(makecall);
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
					if(v==phonenumber)
						GlobalVars.activityItemLocation=1;
					else if(v==makecall)
						GlobalVars.activityItemLocation=2;
					else if(v==goback)
						GlobalVars.activityItemLocation=3;

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
			phoneValue = GlobalVars.inputModeResult;
			GlobalVars.setText(phonenumber, false, phoneValue);
			GlobalVars.inputModeResult = null;
			}
		GlobalVars.lastActivity = CallsMake.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		GlobalVars.selectTextView(phonenumber,false);
		GlobalVars.selectTextView(makecall,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutCallsMakeCallOnResume));
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //INPUT PHONE NUMBER
			GlobalVars.selectTextView(phonenumber,true);
			GlobalVars.selectTextView(makecall,false);
			GlobalVars.selectTextView(goback,false);
			if (phoneValue=="")
				{
				GlobalVars.talk(getResources().getString(R.string.layoutCallsMakeCallPhone2));
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutCallsMakeCallPhone3) + 
								GlobalVars.divideNumbersWithBlanks(phoneValue));
				}
			break;

			case 2: //MAKE A CALL
			GlobalVars.selectTextView(makecall, true);
			GlobalVars.selectTextView(phonenumber,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutCallsMakeCallCall));
			break;

			case 3: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(phonenumber,false);
			GlobalVars.selectTextView(makecall,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //INPUT PHONE NUMBER
			GlobalVars.inputModeKeyboardOnlyNumbers = true;
			GlobalVars.startInputActivity();
			break;

			case 2: //MAKE A CALL
			if (phoneValue=="")
				{
				GlobalVars.talk(getResources().getString(R.string.layoutCallsMakeCallCallError));
				}
				else
				{
				GlobalVars.callTo("tel:" + phoneValue);
				}
			break;

			case 3: //GO BACK TO THE PREVIOUS MENU
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
