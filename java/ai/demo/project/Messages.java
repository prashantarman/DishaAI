package ai.demo.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class Messages extends Activity {
	public static TextView inbox;
	private TextView sent;
	private TextView compose;
	private TextView goback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);
		GlobalVars.lastActivity = Messages.class;
		inbox = (TextView) findViewById(R.id.messagesinbox);
		//getButtonClick(inbox);
		sent = (TextView) findViewById(R.id.messagessent);
		//getButtonClick(sent);
		compose = (TextView) findViewById(R.id.messagesnew);
		//getButtonClick(compose);
		goback = (TextView) findViewById(R.id.goback);
		//getButtonClick(goback);
		GlobalVars.activityItemLocation = 0;
		GlobalVars.activityItemLimit = 4;
		GlobalVars.setText(inbox, false, GlobalVars.context.getResources().getString(R.string.layoutMessagesInbox) + " (" + String.valueOf(GlobalVars.getMessagesUnreadCount()) + ")");

	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			GlobalVars.alarmVibrator.cancel();
		} catch (NullPointerException e) {
		} catch (Exception e) {
		}
		GlobalVars.lastActivity = Messages.class;
		GlobalVars.activityItemLocation = 0;
		GlobalVars.activityItemLimit = 4;
		GlobalVars.selectTextView(inbox, false);
		GlobalVars.selectTextView(sent, false);
		GlobalVars.selectTextView(compose, false);
		GlobalVars.selectTextView(goback, false);
		inbox.setText(GlobalVars.context.getResources().getString(R.string.layoutMessagesInbox) + " (" + String.valueOf(GlobalVars.getMessagesUnreadCount()) + ")");
		if (GlobalVars.messagesWasSent == true) {
			GlobalVars.talk(getResources().getString(R.string.layoutMessagesOnResume2));
			GlobalVars.messagesWasSent = false;
		} else {
			GlobalVars.talk(getResources().getString(R.string.layoutMessagesOnResume));
		}
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
				if (v == inbox)
					GlobalVars.activityItemLocation = 1;
				else if (v == sent)
					GlobalVars.activityItemLocation = 2;
				else if (v == compose)
					GlobalVars.activityItemLocation = 3;
				else if (v == goback)
					GlobalVars.activityItemLocation = 4;

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


	public void select() {
		switch (GlobalVars.activityItemLocation) {
			case 1: //INBOX
				GlobalVars.selectTextView(inbox, true);
				GlobalVars.selectTextView(sent, false);
				GlobalVars.selectTextView(goback, false);
				int smsUnread = GlobalVars.getMessagesUnreadCount();
				if (smsUnread == 0) {
					GlobalVars.talk(getResources().getString(R.string.layoutMessagesInboxNoNew));
				} else if (smsUnread == 1) {
					GlobalVars.talk(getResources().getString(R.string.layoutMessagesInboxOneNew));
				} else {
					GlobalVars.talk(getResources().getString(R.string.layoutMessagesInbox) + ". " + smsUnread + " " + getResources().getString(R.string.layoutMessagesInboxNew));
				}
				break;

			case 2: //SENT
				GlobalVars.selectTextView(sent, true);
				GlobalVars.selectTextView(inbox, false);
				GlobalVars.selectTextView(compose, false);
				GlobalVars.talk(getResources().getString(R.string.layoutMessagesSent));
				break;

			case 3: //COMPOSE MESSAGE
				GlobalVars.selectTextView(compose, true);
				GlobalVars.selectTextView(sent, false);
				GlobalVars.selectTextView(goback, false);
				GlobalVars.talk(getResources().getString(R.string.layoutMessagesNew2));
				break;

			case 4: //GO BACK TO THE MAIN MENU
				GlobalVars.selectTextView(goback, true);
				GlobalVars.selectTextView(compose, false);
				GlobalVars.selectTextView(inbox, false);
				GlobalVars.talk(getResources().getString(R.string.backToMainMenu));
				break;
		}
	}

	public void execute() {
		switch (GlobalVars.activityItemLocation) {
			case 1: //INBOX
				GlobalVars.startActivity(MessagesInbox.class);
				break;

			case 2: //SENT
				GlobalVars.startActivity(MessagesSent.class);
				break;

			case 3: //COMPOSE MESSAGE
				GlobalVars.startActivity(MessagesCompose.class);
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