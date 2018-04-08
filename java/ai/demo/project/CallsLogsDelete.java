package ai.demo.project;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class CallsLogsDelete extends Activity {
	private TextView deletecalls;
	private TextView goback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callslogsdelete);
		GlobalVars.lastActivity = CallsLogsDelete.class;
		deletecalls = (TextView) findViewById(R.id.deletecalllogs);
		//getButtonClick(deletecalls);
		goback = (TextView) findViewById(R.id.goback);
		//getButtonClick(goback);
		GlobalVars.activityItemLocation = 0;
		GlobalVars.activityItemLimit = 2;
	}

	public void getButtonClick(View v) {


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
				if (v == deletecalls)
					GlobalVars.activityItemLocation = 1;
				else if (v == goback)
					GlobalVars.activityItemLocation = 2;

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


	@Override
	public void onResume() {
		super.onResume();
		try {
			GlobalVars.alarmVibrator.cancel();
		} catch (NullPointerException e) {
		} catch (Exception e) {
		}
		GlobalVars.lastActivity = CallsLogsDelete.class;
		GlobalVars.activityItemLocation = 0;
		GlobalVars.activityItemLimit = 2;
		GlobalVars.selectTextView(deletecalls, false);
		GlobalVars.selectTextView(goback, false);
		GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsDeleteOnResume));
	}

	public void select() {
		switch (GlobalVars.activityItemLocation) {
			case 1: //DELETE ALL CALL LOGS
				GlobalVars.selectTextView(deletecalls, true);
				GlobalVars.selectTextView(goback, false);
				GlobalVars.talk(getResources().getString(R.string.layoutCallsLogsDelete));
				break;

			case 2: //GO BACK TO THE PREVIOUS MENU
				GlobalVars.selectTextView(goback, true);
				GlobalVars.selectTextView(deletecalls, false);
				GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
				break;
		}
	}

	public void execute() {
		switch (GlobalVars.activityItemLocation) {
			case 1: //DELETE ALL CALL LOGS
				deleteAllCallLogs();
				GlobalVars.callLogsDeleted = true;
				this.finish();
				break;

			case 2: //GO BACK TO THE PREVIOUS MENU
				this.finish();
				break;
		}
	}


	public void deleteAllCallLogs() {
		try {
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			getContentResolver().delete(CallLog.Calls.CONTENT_URI, null, null);
			}
			catch(Exception e)
			{
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