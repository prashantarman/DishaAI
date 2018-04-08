package ai.demo.project;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import ai.demo.project.R;

public class MessagesInboxDeleteAll extends Activity
	{
	private TextView delete;
	private TextView goback;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.messagesinboxdeleteall);
		GlobalVars.lastActivity = MessagesInboxDeleteAll.class;
		delete = (TextView) findViewById(R.id.messagedelete);
			//getButtonClick(delete);
		goback = (TextView) findViewById(R.id.goback);
			//getButtonClick(goback);
		GlobalVars.messagesInboxWereDeleted = false;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=2;
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
					if(v==delete)
						GlobalVars.activityItemLocation=1;
					else if(v==goback)
						GlobalVars.activityItemLocation=2;

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
		GlobalVars.lastActivity = MessagesInboxDeleteAll.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=2;
		GlobalVars.selectTextView(delete,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutMessagesInboxDeleteAllOnResume));
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //CONFIRM DELETE
			GlobalVars.selectTextView(delete, true);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutMessagesInboxDeleteAllDelete));
			break;

			case 2: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //CONFIRM DELETE
			deleteAllReceivedMessages();
			GlobalVars.messagesInboxWereDeleted = true;
			this.finish();
			break;

			case 2: //GO BACK TO THE PREVIOUS MENU
			this.finish();
			break;
			}
		}
		


	private void deleteAllReceivedMessages()
		{
		try
			{
			Uri inboxUri = Uri.parse("content://sms/inbox");
			Cursor c = getContentResolver().query(inboxUri , null, null, null, null);
			while (c.moveToNext())
				{
			    try
			    	{
			        // Delete the SMS
			        String pid = c.getString(0); // Get id;
			        String uri = "content://sms/" + pid;
			        getContentResolver().delete(Uri.parse(uri), null, null);
			    	}
			    	catch (Exception e)
			    	{
			    	}
				}
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		}
	}