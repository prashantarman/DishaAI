package ai.demo.project;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import ai.demo.project.R;

public class MessagesDelete extends Activity
	{
	private TextView todelete;
	private TextView delete;
	private TextView goback;
	public static String messageFrom = "";
	public static String messageToDelete = "";
	public static String messageIDToDelete = "";
	public static int messageType = -1;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.messagesdelete);
		GlobalVars.lastActivity = MessagesDelete.class;
		todelete = (TextView) findViewById(R.id.message);
		delete = (TextView) findViewById(R.id.messagedelete);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		GlobalVars.messagesWasDeleted = false;
		GlobalVars.setText(todelete, false, messageFrom);
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
		GlobalVars.lastActivity = MessagesDelete.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		GlobalVars.selectTextView(todelete,false);
		GlobalVars.selectTextView(delete,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutMessagesDeleteOnResume));
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //MESSAGE TO DELETE
			GlobalVars.selectTextView(todelete,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(goback,false);
			switch (messageType)
				{
				case GlobalVars.TYPE_INBOX:
				GlobalVars.talk(getResources().getString(R.string.layoutMessagesDeleteNameFrom) + messageToDelete);
				break;
				
				case GlobalVars.TYPE_SENT:
				GlobalVars.talk(getResources().getString(R.string.layoutMessagesDeleteNameTo) + messageToDelete);
				break;
				}
			break;

			case 2: //CONFIRM DELETE
			GlobalVars.selectTextView(delete, true);
			GlobalVars.selectTextView(todelete,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutMessagesDeleteDelete));
			break;

			case 3: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(todelete,false);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //MESSAGE TO DELETE
			switch (messageType)
				{
				case GlobalVars.TYPE_INBOX:
				GlobalVars.talk(getResources().getString(R.string.layoutMessagesDeleteNameFrom) + messageToDelete);
				break;
				
				case GlobalVars.TYPE_SENT:
				GlobalVars.talk(getResources().getString(R.string.layoutMessagesDeleteNameTo) + messageToDelete);
				break;
				}
			break;

			case 2: //CONFIRM DELETE
			deleteMessage(messageIDToDelete);
			GlobalVars.messagesWasDeleted=true;
			messageFrom = "";
			messageToDelete = "";
			messageIDToDelete = "";
			messageType = -1;
			this.finish();
			break;

			case 3: //GO BACK TO THE PREVIOUS MENU
			messageFrom = "";
			messageToDelete = "";
			messageIDToDelete = "";
			messageType = -1;
			this.finish();
			break;
			}
		}
		


	private void deleteMessage(String messageID)
		{
		try
			{
			getContentResolver().delete(Uri.parse("content://sms/" + messageID), null, null);
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
