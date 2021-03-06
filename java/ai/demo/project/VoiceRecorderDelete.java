package ai.demo.project;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class VoiceRecorderDelete extends Activity
	{
	private TextView todelete;
	private TextView delete;
	private TextView goback;
	private MediaPlayer mp = new MediaPlayer();

	@Override protected void onCreate(Bundle savedInstanceState)
    	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voicerecorderdelete);
		GlobalVars.lastActivity = VoiceRecorderDelete.class;
		todelete = (TextView) findViewById(R.id.todelete);
			//getButtonClick(todelete);
		delete = (TextView) findViewById(R.id.delete);
			//getButtonClick(delete);
		goback = (TextView) findViewById(R.id.goback);
			//getButtonClick(goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		GlobalVars.setText(todelete, false, getResources().getString(R.string.layoutVoiceRecorderDeleteToDelete) + (GlobalVars.voiceRecorderToDelete + 1));
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
					if(v==todelete)
						GlobalVars.activityItemLocation=1;
					else if(v==delete)
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
		GlobalVars.lastActivity = VoiceRecorderDelete.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		GlobalVars.selectTextView(todelete,false);
		GlobalVars.selectTextView(delete,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderDeleteOnResume));
		}

	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ VOICE RECORD TO DELETE
			GlobalVars.selectTextView(todelete,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderDeleteDelete) + (GlobalVars.voiceRecorderToDelete + 1) + getResources().getString(R.string.layoutVoiceRecorderDeleteDelete2));
			break;

			case 2: //DELETE VOICE RECORD
			GlobalVars.selectTextView(delete,true);
			GlobalVars.selectTextView(todelete,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderDeleteConfirm));
			break;
			
			case 3: //GO BACK TO PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(todelete,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}

	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ VOICE RECORD TO DELETE
			try
				{
				GlobalVars.tts.stop();
				}
				catch(NullPointerException e)
				{
				}
				catch(Exception e)
				{
				}
			try
				{
				mp.stop();
				}
				catch(NullPointerException e)
				{
				}
				catch(Exception e)
				{
				}
			try
				{
				mp.release();
				}
				catch(NullPointerException e)
				{
				}
				catch(Exception e)
				{
				}
			try
				{
				mp = new MediaPlayer();
				mp.setDataSource(GlobalVars.voiceRecorderListFiles.get(GlobalVars.voiceRecorderToDelete));
				mp.prepare();
				mp.setLooping(false);
				mp.start();
				}
				catch(NullPointerException e)
				{
				}
				catch (Exception e)
				{
				}
			break;

			case 2: //DELETE VOICE RECORD
			try
				{
				File toDeleteFile = new File(GlobalVars.voiceRecorderListFiles.get(GlobalVars.voiceRecorderToDelete));
				toDeleteFile.delete();
				}
				catch(IllegalArgumentException e)
				{
				}
				catch(NullPointerException e)
				{
				}
				catch(Exception e)
				{
				}
			GlobalVars.voiceRecorderAudioWasDeleted = true;
			this.finish();
			break;

			case 3: //GO BACK TO PREVIOUS MENU
			this.finish();
			break;
			}
		}
	
	private void previousItem()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ VOICE RECORD TO DELETE
			try
				{
				mp.stop();
				}
				catch(NullPointerException e)
				{
				}
				catch(Exception e)
				{
				}
			try
				{
				mp.release();
				}
				catch(NullPointerException e)
				{
				}
				catch(Exception e)
				{
				}
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


	}
