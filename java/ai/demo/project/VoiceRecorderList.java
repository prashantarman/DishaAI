package ai.demo.project;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import ai.demo.project.R;

public class VoiceRecorderList extends Activity
	{
	private TextView list;
	private TextView play;
	private TextView delete;
	private TextView goback;
	private MediaPlayer mp = new MediaPlayer();
	private int selectedVoiceRecord = -1;

	@Override protected void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voicerecorderlist);
		GlobalVars.lastActivity = VoiceRecorderList.class;
		list = (TextView) findViewById(R.id.voicerecorderlist);
			//getButtonClick(list);
		play = (TextView) findViewById(R.id.voicerecorderplay);
			//getButtonClick(play);
		delete = (TextView) findViewById(R.id.voicerecorderdelete);
			//getButtonClick(delete);
		goback = (TextView) findViewById(R.id.goback);
			//getButtonClick(goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		selectedVoiceRecord = -1;
		new VoiceRecorderListThread().execute("");
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
					else if(v==play)
						GlobalVars.activityItemLocation=2;
					else if(v==delete)
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
		GlobalVars.lastActivity = VoiceRecorderList.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		GlobalVars.selectTextView(list,false);
		GlobalVars.selectTextView(play,false);
		GlobalVars.selectTextView(delete,false);
		GlobalVars.selectTextView(goback,false);
		selectedVoiceRecord = -1;
		GlobalVars.setText(list, false, getResources().getString(R.string.layoutVoiceRecorderListList));
		if (GlobalVars.voiceRecorderAudioWasDeleted==true)
			{
			GlobalVars.voiceRecorderToDelete = -1;
			GlobalVars.voiceRecorderAudioWasDeleted = false;
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListOnResume2));
			new VoiceRecorderListThread().execute("");
			}
			else
			{
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListOnResume));
			}
		}

	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //LIST VOICE RECORDS
			GlobalVars.selectTextView(list,true);
			GlobalVars.selectTextView(play,false);
			GlobalVars.selectTextView(goback,false);
			if (selectedVoiceRecord==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListList2));
				}
				else
				{
				if (GlobalVars.voiceRecorderListReady==false)
					{
					GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListPleaseWait));
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListItem) + (selectedVoiceRecord + 1) + getResources().getString(R.string.layoutVoiceRecorderListItemOf) + GlobalVars.voiceRecorderListFiles.size());
					}
				}
			break;

			case 2: //PLAY VOICE RECORD
			GlobalVars.selectTextView(play, true);
			GlobalVars.selectTextView(list,false);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListPlay2));
			break;

			case 3: //DELETE VOICE RECORD
			GlobalVars.selectTextView(delete, true);
			GlobalVars.selectTextView(play,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListDelete2));
			break;
			
			case 4: //GO BACK TO PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(list,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}

	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //LIST VOICE RECORDS
			if (GlobalVars.voiceRecorderListReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListPleaseWait));
				}
				else
				{
				if (GlobalVars.voiceRecorderListFiles.size()>0)
					{
					if (selectedVoiceRecord+1==GlobalVars.voiceRecorderListFiles.size())
						{
						selectedVoiceRecord = -1;
						}
					selectedVoiceRecord = selectedVoiceRecord + 1;
					GlobalVars.setText(list, true, getResources().getString(R.string.layoutVoiceRecorderListItem) + (selectedVoiceRecord + 1) + "/" + GlobalVars.voiceRecorderListFiles.size());
					GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListItem) + (selectedVoiceRecord + 1) + getResources().getString(R.string.layoutVoiceRecorderListItemOf) + GlobalVars.voiceRecorderListFiles.size());
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListNoVoiceRecords));
					}	
				}
			break;

			case 2: //PLAY VOICE RECORD
			if (selectedVoiceRecord==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListError));
				}
				else
				{
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
					mp.setDataSource(GlobalVars.voiceRecorderListFiles.get(selectedVoiceRecord));
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
				}
			break;

			case 3: //DELETE VOICE RECORD
			if (selectedVoiceRecord==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListError));
				}
				else
				{
				GlobalVars.voiceRecorderToDelete = selectedVoiceRecord;
				GlobalVars.startActivity(VoiceRecorderDelete.class);
				}
			break;
			
			case 4: //GO BACK TO PREVIOUS MENU
			this.finish();
			break;
			}
		}
		
	private void previousItem()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //LIST VOICE RECORDS
			if (GlobalVars.voiceRecorderListReady==false)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListPleaseWait));
				}
				else
				{
				if (GlobalVars.voiceRecorderListFiles.size()>0)
					{
					if (selectedVoiceRecord-1<0)
						{
						selectedVoiceRecord = GlobalVars.voiceRecorderListFiles.size();
						}
					selectedVoiceRecord = selectedVoiceRecord - 1;
					GlobalVars.setText(list, true, getResources().getString(R.string.layoutVoiceRecorderListItem) + (selectedVoiceRecord + 1) + "/" + GlobalVars.voiceRecorderListFiles.size());
					GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListItem) + (selectedVoiceRecord + 1) + getResources().getString(R.string.layoutVoiceRecorderListItemOf) + GlobalVars.voiceRecorderListFiles.size());
					}
					else
					{
					GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListNoVoiceRecords));
					}	
				}
			break;
			
			case 2: //PLAY VOICE RECORD
			if (selectedVoiceRecord==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutVoiceRecorderListError));
				}
				else
				{
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
