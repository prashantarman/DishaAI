package ai.demo.project;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class Main extends Activity implements TextToSpeech.OnInitListener
	{
	private static boolean speakOnResume = false;
	public static TextView messages;
	public static TextView calls;
	private TextView contacts;
	private TextView music;
	private TextView internet;
	public static TextView alarms;
	private TextView voicerecorder;
	private TextView applications;
	private TextView settings;
	private TextView status;
	private TextView cameravision;
	private TextView news;
	private boolean okToFinish = false;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
		GlobalVars.lastActivity = Main.class;
		speakOnResume=false;
		messages = (TextView) findViewById(R.id.messages);
			//getButtonClick(messages);
		calls = (TextView) findViewById(R.id.calls);
			//getButtonClick(calls);
		contacts = (TextView) findViewById(R.id.contacts);
			//getButtonClick(contacts);
		music = (TextView) findViewById(R.id.music);
			//getButtonClick(music);
		internet = (TextView) findViewById(R.id.browser);
			//getButtonClick(internet);
		alarms = (TextView) findViewById(R.id.alarms);
			//getButtonClick(alarms);
		voicerecorder = (TextView) findViewById(R.id.voicerecorder);
			//getButtonClick(voicerecorder);
		applications = (TextView) findViewById(R.id.apps);
			//getButtonClick(applications);
		settings = (TextView) findViewById(R.id.settings);
			//getButtonClick(settings);
		//status = (TextView) findViewById(R.id.status);
			//getButtonClick(status);
		cameravision = (TextView) findViewById(R.id.describe);
			//getButtonClick(cameravision);
		news = (TextView) findViewById(R.id.news);
			//getButtonClick(news);

		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=11;
		
		GlobalVars.contextApp = getApplicationContext();
		
		GlobalVars.context = this;
		GlobalVars.startTTS(GlobalVars.tts);
		GlobalVars.tts = new TextToSpeech(this,this);
		GlobalVars.tts.setPitch((float) 1.0);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (!android.provider.Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 0);
                }
                else
					GlobalVars.startService(HeadService.class);
			}

		
		//SETS THE ALARM VIBRATOR VARIABLE
		GlobalVars.alarmVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		
		//SETS PROFILE TO NORMAL
		AudioManager audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
		audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

		GlobalVars.alarmAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		GlobalVars.openAndLoadAlarmFile();
		GlobalVars.setText(alarms,false, getResources().getString(R.string.mainAlarms) + " (" + GlobalVars.getPendingAlarmsForTodayCount() + ")");
		
		//LIST EVERY MUSIC FILE WITH THE MEDIA INFORMATION TO USE IT WITH THE MUSIC PLAYER
		new MusicPlayerThreadRefreshDatabase().execute("");




		
		//READ WEB BOOKMARKS DATABASE
		GlobalVars.readBookmarksDatabase();
		
		if (GlobalVars.deviceIsAPhone()==true)
			{
			messages.setText(GlobalVars.context.getResources().getString(R.string.mainMessages) + " (" + String.valueOf(GlobalVars.getMessagesUnreadCount()) + ")");
			}
			else
			{
			messages.setText(GlobalVars.context.getResources().getString(R.string.mainMessages) + " (0)");
			}
		
		if (GlobalVars.deviceIsAPhone()==true)
			{
			calls.setText(GlobalVars.context.getResources().getString(R.string.mainCalls) + " (" + String.valueOf(GlobalVars.getCallsMissedCount()) + ")");
			}
			else
			{
			calls.setText(GlobalVars.context.getResources().getString(R.string.mainCalls) + " (0)");
			}

		//GETS EVERY ALARM TONE
		try
			{
			RingtoneManager manager = new RingtoneManager(this);
			manager.setType(RingtoneManager.TYPE_ALARM);
			Cursor cursorAlarms = manager.getCursor();
			if (cursorAlarms!=null)
				{
				if(cursorAlarms.moveToFirst())
		    		{
					while(!cursorAlarms.isAfterLast())
		        		{
						GlobalVars.settingsToneAlarmTitle.add(cursorAlarms.getString(RingtoneManager.TITLE_COLUMN_INDEX));
						GlobalVars.settingsToneAlarmUri.add(cursorAlarms.getString(RingtoneManager.URI_COLUMN_INDEX));
						GlobalVars.settingsToneAlarmID.add(cursorAlarms.getString(RingtoneManager.ID_COLUMN_INDEX));
						cursorAlarms.moveToNext();
		        		}
		    		}
				//cursorAlarms.close();
				}
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
			
		//GETS EVERY NOTIFICATION TONE
		try
			{
			RingtoneManager manager = new RingtoneManager(this);
			manager.setType(RingtoneManager.TYPE_NOTIFICATION);
			Cursor cursorNotificationTone = manager.getCursor();
			if (cursorNotificationTone!=null)
				{
				if(cursorNotificationTone.moveToFirst())
	    			{
					while(!cursorNotificationTone.isAfterLast())
	        			{
						GlobalVars.settingsToneNotificationTitle.add(cursorNotificationTone.getString(RingtoneManager.TITLE_COLUMN_INDEX));
						GlobalVars.settingsToneNotificationUri.add(cursorNotificationTone.getString(RingtoneManager.URI_COLUMN_INDEX));
						GlobalVars.settingsToneNotificationID.add(cursorNotificationTone.getString(RingtoneManager.ID_COLUMN_INDEX));
						cursorNotificationTone.moveToNext();
	        			}
	    			}
				//cursorNotificationTone.close();
				}
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		
		//GETS EVERY CALL TONE
		try
			{
			RingtoneManager manager = new RingtoneManager(this);
			manager.setType(RingtoneManager.TYPE_RINGTONE);
			Cursor cursorCallTone = manager.getCursor();
			if (cursorCallTone!=null)
				{
				if(cursorCallTone.moveToFirst())
    				{
					while(!cursorCallTone.isAfterLast())
        				{
						GlobalVars.settingsToneCallTitle.add(cursorCallTone.getString(RingtoneManager.TITLE_COLUMN_INDEX));
						GlobalVars.settingsToneCallUri.add(cursorCallTone.getString(RingtoneManager.URI_COLUMN_INDEX));
						GlobalVars.settingsToneCallID.add(cursorCallTone.getString(RingtoneManager.ID_COLUMN_INDEX));
						cursorCallTone.moveToNext();
        				}
    				}
				//cursorCallTone.close();
				}
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
			
		//GETS READING SPEED VALUE
		String readingSpeedString = GlobalVars.readFile("readingspeed.cfg");
		if (readingSpeedString=="")
			{
			GlobalVars.settingsTTSReadingSpeed = 1;
			GlobalVars.tts.setSpeechRate(GlobalVars.settingsTTSReadingSpeed);
			GlobalVars.writeFile("readingspeed.cfg",String.valueOf(GlobalVars.settingsTTSReadingSpeed));
			}
			else
			{
			try
				{
				GlobalVars.settingsTTSReadingSpeed = Integer.valueOf(readingSpeedString);
				GlobalVars.tts.setSpeechRate(GlobalVars.settingsTTSReadingSpeed);
				}
				catch(Exception e)
				{
				GlobalVars.settingsTTSReadingSpeed = 1;
				GlobalVars.tts.setSpeechRate(GlobalVars.settingsTTSReadingSpeed);
				GlobalVars.writeFile("readingspeed.cfg",String.valueOf(GlobalVars.settingsTTSReadingSpeed));
				}
			}
		
		//GETS INPUT MODE VALUE
		String inputModeString = GlobalVars.readFile("inputmode.cfg");
		if (inputModeString=="")
			{
			GlobalVars.inputMode = GlobalVars.INPUT_KEYBOARD;
			GlobalVars.writeFile("inputmode.cfg",String.valueOf(GlobalVars.INPUT_KEYBOARD));
			}
			else
			{
			try
				{
				GlobalVars.inputMode = Integer.valueOf(inputModeString);
				}
				catch(Exception e)
				{
				GlobalVars.inputMode = GlobalVars.INPUT_KEYBOARD;
				GlobalVars.writeFile("inputmode.cfg",String.valueOf(GlobalVars.INPUT_KEYBOARD));
				}
			}
		
		//GETS SCREEN TIMEOUT POSSIBLE VALUES
		int[] arr = getResources().getIntArray(R.array.screenTimeOutSeconds);
			for(int i=0;i<arr.length;i++)
			{
			GlobalVars.settingsScreenTimeOutValues.add(String.valueOf(arr[i]));
			}

		//GETS TIME VALUES FOR ALARMS
		String[] arr2 = getResources().getStringArray(R.array.timeHourValues);
		for(int i=0;i<arr2.length;i++)
			{
			GlobalVars.alarmTimeHoursValues.add(String.valueOf(arr2[i]));
			}
		String[] arr3 = getResources().getStringArray(R.array.timeMinutesValues);
		for(int i=0;i<arr3.length;i++)
			{
			GlobalVars.alarmTimeMinutesValues.add(String.valueOf(arr3[i]));
			}
			
		//GETS SCREEN TIMEOUT VALUE
		String screenTimeOutString = GlobalVars.readFile("screentimeout.cfg");
		if (screenTimeOutString=="")
			{
			GlobalVars.settingsScreenTimeOut = Integer.valueOf(GlobalVars.settingsScreenTimeOutValues.get(GlobalVars.settingsScreenTimeOutValues.size() -1));
			GlobalVars.writeFile("screentimeout.cfg",String.valueOf(GlobalVars.settingsScreenTimeOut));
			}
			else
			{
			try
				{
				GlobalVars.settingsScreenTimeOut = Integer.valueOf(screenTimeOutString);
				}
				catch(Exception e)
				{
				GlobalVars.settingsScreenTimeOut = Integer.valueOf(GlobalVars.settingsScreenTimeOutValues.get(GlobalVars.settingsScreenTimeOutValues.size() -1));
				GlobalVars.writeFile("screentimeout.cfg",String.valueOf(GlobalVars.settingsScreenTimeOut));
				}
			}
		
		//SETS BLUETOOTH VALUE STATE
		GlobalVars.bluetoothEnabled = GlobalVars.isBluetoothEnabled();
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable()
			{
			@Override public void run()
				{
				speakOnResume = true;
				}
			}, 2000);

		GlobalVars.startService(BlindCommunicatorService.class);


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
					if(v==cameravision)
						GlobalVars.activityItemLocation=1;
					else if(v==contacts)
						GlobalVars.activityItemLocation=2;
					else if(v==messages)
						GlobalVars.activityItemLocation=3;
					else if(v==calls)
						GlobalVars.activityItemLocation=4;
					else if(v==music)
						GlobalVars.activityItemLocation=5;
					else if(v==news)
						GlobalVars.activityItemLocation=6;
					else if(v==internet)
						GlobalVars.activityItemLocation=7;
					else if(v==alarms)
						GlobalVars.activityItemLocation=8;
					else if(v==voicerecorder)
						GlobalVars.activityItemLocation=9;
					else if(v==applications)
						GlobalVars.activityItemLocation=10;
					else if(v==settings)
						GlobalVars.activityItemLocation=11;

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
		
    @Override protected void onDestroy()
    	{
		shutdownEverything();
    	super.onDestroy();
    	}
		
	@Override public void onResume()
		{
		super.onResume();
		try{GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = Main.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=11;
		GlobalVars.selectTextView(messages,false);
		GlobalVars.selectTextView(calls,false);
		GlobalVars.selectTextView(contacts,false);
		GlobalVars.selectTextView(music,false);
		GlobalVars.selectTextView(internet,false);
		GlobalVars.selectTextView(alarms,false);
		GlobalVars.selectTextView(voicerecorder,false);
		GlobalVars.selectTextView(applications,false);
		GlobalVars.selectTextView(settings,false);
		GlobalVars.selectTextView(cameravision,false);
		GlobalVars.selectTextView(news,false);
		
		//UPDATE ALARM COUNTER
		try
			{
			GlobalVars.setText(alarms,false, getResources().getString(R.string.mainAlarms) + " (" + GlobalVars.getPendingAlarmsForTodayCount() + ")");
			}
			catch(IllegalStateException e)
			{
			}
			catch(Exception e)
			{
			}
		
		try
			{
			if (GlobalVars.deviceIsAPhone()==true)
				{
				messages.setText(GlobalVars.context.getResources().getString(R.string.mainMessages) + " (" + String.valueOf(GlobalVars.getMessagesUnreadCount()) + ")");
				}
			}
			catch(IllegalStateException e)
			{
			}
			catch(Exception e)
			{
			}
		
		if (speakOnResume==true)
			{
			GlobalVars.talk(getResources().getString(R.string.layoutMainOnResume));
			}
		
		try
			{
			if (GlobalVars.deviceIsAPhone()==true)
				{
				calls.setText(GlobalVars.context.getResources().getString(R.string.mainCalls) + " (" + String.valueOf(GlobalVars.getCallsMissedCount()) + ")");
				}
				else
				{
				calls.setText(GlobalVars.context.getResources().getString(R.string.mainCalls) + " (0)");
				}
			}
			catch(NullPointerException e)
			{
			}
			catch(IllegalStateException e)
			{
			}
			catch(Exception e)
			{
			}
		}
    
    public void onInit(int status)
    	{
		if (status == TextToSpeech.SUCCESS)
			{
			GlobalVars.talk(getResources().getString(R.string.mainWelcome));
			GlobalVars.tts.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener()
				{
				@Override public void onUtteranceCompleted(String utteranceId)
					{
					try
   						{
						GlobalVars.musicPlayer.setVolume(1f,1f);
   						}
						catch(NullPointerException e)
						{
						}
						catch(Exception e)
						{
						}
					}
				});
			}
			else
			{
			new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.mainNoTTSInstalledTitle)).setMessage(getResources().getString(R.string.mainNoTTSInstalledMessage)).setPositiveButton(getResources().getString(R.string.mainNoTTSInstalledButton),new DialogInterface.OnClickListener()
				{
				public void onClick(DialogInterface dialog,int which)
					{
					try
						{
					    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=id=com.google.android.tts")));
						}
						catch (ActivityNotFoundException e)
						{
						try
							{
						    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.tts")));
							}
							catch (ActivityNotFoundException e2)
							{
							}
						}
					}
				}).show();
			}
		}

	public void shutdownEverything()
		{
		try
			{
			GlobalVars.tts.shutdown();
			}
			catch(Exception e)
			{
			}
		try
			{
			if (GlobalVars.musicPlayer!=null)
				{
				GlobalVars.musicPlayer.stop();
				GlobalVars.musicPlayer.reset();
				GlobalVars.musicPlayer.release();
				GlobalVars.musicPlayer = null;
				}
			}
			catch(Exception e)
			{
			}
		GlobalVars.musicPlayerPlayingSongIndex = -1;
		try
			{
			GlobalVars.stopService(BlindCommunicatorService.class);
			}
			catch(Exception e)
			{
			}
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{

				case 1: //CAMERA VISION
					GlobalVars.selectTextView(cameravision,true);
					GlobalVars.selectTextView(contacts,false);
					GlobalVars.selectTextView(settings,false);
					GlobalVars.talk(getResources().getString(R.string.mainCameraVision));
					break;

				case 2: //CONTACTS
					GlobalVars.selectTextView(contacts,true);
					GlobalVars.selectTextView(cameravision,false);
					GlobalVars.selectTextView(messages,false);
					GlobalVars.talk(getResources().getString(R.string.mainContacts));
					break;

			case 3: //MESSAGES
			if (GlobalVars.deviceIsAPhone()==true)
				{
				int smsUnread = GlobalVars.getMessagesUnreadCount();
				if (smsUnread==0)
					{
					GlobalVars.talk(getResources().getString(R.string.mainMessagesNoNew));
					}
				else if (smsUnread==1)
					{
					GlobalVars.talk(getResources().getString(R.string.mainMessagesOneNew));
					}
				else
					{
					GlobalVars.talk(getResources().getString(R.string.mainMessages) + ". " + smsUnread + " " + getResources().getString(R.string.mainMessagesNew));
					}
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.mainMessagesNotAvailable));
				}
			GlobalVars.selectTextView(messages,true);
			GlobalVars.selectTextView(calls,false);
			GlobalVars.selectTextView(contacts,false);
			break;
			
			case 4: //CALLS
			if (GlobalVars.deviceIsAPhone()==true)
				{
				int missedCalls = GlobalVars.getCallsMissedCount();
				if (missedCalls==0)
					{
					GlobalVars.talk(getResources().getString(R.string.mainCallsNoMissed));
					}
				else if (missedCalls==1)
					{
					GlobalVars.talk(getResources().getString(R.string.mainCallsOneMissed));
					}
				else
					{
					GlobalVars.talk(getResources().getString(R.string.mainCalls) + ". " + missedCalls + " " + getResources().getString(R.string.mainCallsMissed));
					}
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.mainCallsNotAvailable));
				}
			GlobalVars.selectTextView(calls, true);
			GlobalVars.selectTextView(messages,false);
			GlobalVars.selectTextView(music,false);
			break;


				case 5: //MUSIC
					GlobalVars.selectTextView(music,true);
					GlobalVars.selectTextView(calls,false);
					GlobalVars.selectTextView(news,false);
					GlobalVars.talk(getResources().getString(R.string.mainMusicPlayer));
					break;



				case 6: //NEWS
					GlobalVars.selectTextView(news,true);
					GlobalVars.selectTextView(music,false);
					GlobalVars.selectTextView(internet,false);
					GlobalVars.talk(getResources().getString(R.string.mainNews));
					break;

			case 7: //INTERNET
			GlobalVars.selectTextView(internet,true);
			GlobalVars.selectTextView(news,false);
			GlobalVars.selectTextView(alarms,false);
			GlobalVars.talk(getResources().getString(R.string.mainBrowser));
			break;
			
			case 8: //ALARMS
			GlobalVars.selectTextView(alarms,true);
			GlobalVars.selectTextView(internet,false);
			GlobalVars.selectTextView(voicerecorder,false);
			GlobalVars.talk(GlobalVars.getPendingAlarmsForTodayCountText());
			break;
			
			case 9: //VOICE RECORDER
			GlobalVars.selectTextView(voicerecorder,true);
			GlobalVars.selectTextView(alarms,false);
			GlobalVars.selectTextView(applications,false);
			GlobalVars.talk(getResources().getString(R.string.mainVoiceRecorder));
			break;
			
			case 10: //APPLICATIONS
			GlobalVars.selectTextView(applications,true);
			GlobalVars.selectTextView(voicerecorder,false);
			GlobalVars.selectTextView(settings,false);
			GlobalVars.talk(getResources().getString(R.string.mainApplications));
			break;
			
			case 11: //SETTINGS
			GlobalVars.selectTextView(settings,true);
			GlobalVars.selectTextView(applications,false);
			GlobalVars.selectTextView(cameravision,false);
			GlobalVars.talk(getResources().getString(R.string.mainSettings));
			break;
			






			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 3: //MESSAGES
			if (GlobalVars.deviceIsAPhone()==true)
				{
				GlobalVars.startActivity(Messages.class);
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.mainNotAvailable));
				}
			break;
			
			case 4: //CALLS
			if (GlobalVars.deviceIsAPhone()==true)
				{
				GlobalVars.startActivity(Calls.class);
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.mainNotAvailable));
				}
			break;
			
			case 2: //CONTACTS
			GlobalVars.startActivity(Contacts.class);
			break;
			
			case 5: //MUSIC
			if (GlobalVars.musicPlayerDatabaseReady==true)
				{
				GlobalVars.startActivity(MusicPlayer.class);
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.mainMusicPlayerPleaseTryAgain));
				}
			break;
			
			case 7: //INTERNET
			GlobalVars.startActivity(Browser.class);
			break;
			
			case 8: //ALARMS
			GlobalVars.startActivity(Alarms.class);
			break;
			
			case 9: //VOICE RECORDER
			GlobalVars.startActivity(VoiceRecorder.class);
			break;
			
			case 10: //APPLICATIONS
			GlobalVars.startActivity(Applications.class);
			break;
			
			case 11: //SETTINGS
			GlobalVars.startActivity(Settings.class);
			break;
			
//			case 12: //STATUS
//			GlobalVars.talk(getDeviceStatus());
//			break;

			case 1: //CAMERA VISION
			GlobalVars.startActivity(CameraVison.class);
			break;

			case 6: //NEWS
			GlobalVars.startActivity(News.class);
			break;

			}
		}
		


	private String getDeviceStatus()
		{
		String year = GlobalVars.getYear();
		String month = GlobalVars.getMonthName(Integer.valueOf(GlobalVars.getMonth()));
		String day = Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		String dayname = GlobalVars.getDayName(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
		String hour = Integer.toString(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		String minutes = Integer.toString(Calendar.getInstance().get(Calendar.MINUTE));

		String textStatus = "";

		textStatus = textStatus +
					 getResources().getString(R.string.mainBatteryChargedAt) +
					 String.valueOf(batteryLevel() +
					 getResources().getString(R.string.mainPercentAndTime) +
					 hour + getResources().getString(R.string.mainHours) +
					 minutes + getResources().getString(R.string.mainMinutesAndDate) +
					 dayname + " " + day + getResources().getString(R.string.mainOf) +
					 month + getResources().getString(R.string.mainOf) + year);

		if (GlobalVars.batteryAt100==true)
			{
			textStatus = textStatus + getResources().getString(R.string.deviceChargedStatus);
			}
		else if (GlobalVars.batteryIsCharging==true)
			{
			textStatus = textStatus + getResources().getString(R.string.deviceChargingStatus);
			}

		if (GlobalVars.deviceIsAPhone()==true)
			{
			if (GlobalVars.deviceIsConnectedToMobileNetwork()==true)
				{
				textStatus = textStatus + getResources().getString(R.string.mainCarrierIs) + getCarrier();
				}
				else
				{
				textStatus = textStatus + getResources().getString(R.string.mainNoSignal);
				}
			}
		
		AudioManager audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
		switch(audioManager.getRingerMode())
			{
			case AudioManager.RINGER_MODE_NORMAL:
			textStatus = textStatus + getResources().getString(R.string.mainProfileIsNormal);
			break;

			case AudioManager.RINGER_MODE_SILENT:
			textStatus = textStatus + getResources().getString(R.string.mainProfileIsSilent);
			break;

			case AudioManager.RINGER_MODE_VIBRATE:
			textStatus = textStatus + getResources().getString(R.string.mainProfileIsVibrate);
			break;
			}
			
		if (GlobalVars.isWifiEnabled())
			{
			String name = GlobalVars.getWifiSSID();
			if (name=="")
				{
				textStatus = textStatus + getResources().getString(R.string.mainWifiOnWithoutNetwork);
				}
				else
				{
				textStatus = textStatus + getResources().getString(R.string.mainWifiOnWithNetwork) + name + ".";
				}
			}
			else
			{
			textStatus = textStatus + getResources().getString(R.string.mainWifiOff);
			}
		return textStatus;
		}
		
	private int batteryLevel()
		{
    	Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    	int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    	int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    	if(level == -1 || scale == -1)
			{
    		return (int)50.0f;
			}
    	return (int)(((float)level / (float)scale) * 100.0f); 
		}
		
	private String getCarrier()
		{
		try
			{
			TelephonyManager telephonyManager = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE));
			String carrier;
			carrier = telephonyManager.getSimOperatorName();
			if (carrier==null | carrier=="")
				{
				return getResources().getString(R.string.mainCarrierNotAvailable);
				}
				else
				{
				return carrier;
				}
			}
			catch(Exception e)
			{
			return getResources().getString(R.string.mainCarrierNotAvailable);
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

			if (keyCode== KeyEvent.KEYCODE_BACK)
			{
				if (GlobalVars.isBCTheDefaultLauncher()==false)
				{
					if (okToFinish==false)
					{
						okToFinish=true;
						Handler handler = new Handler();
						handler.postDelayed(new Runnable()
						{
							@Override public void run()
							{
								okToFinish = false;
							}
						}, 3000);
						GlobalVars.talk(getResources().getString(R.string.mainPressBack));
						return false;
					}
					else
					{
						shutdownEverything();
						this.finish();
					}
				}
				else
				{
					return false;
				}
			}
			return super.onKeyUp(keyCode, event);
		}



	}
