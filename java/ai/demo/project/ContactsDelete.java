package ai.demo.project;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class ContactsDelete extends Activity
	{
	private TextView name;
	private TextView delete;
	private TextView goback;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.contactsdelete);
		GlobalVars.lastActivity = ContactsDelete.class;
		name = (TextView) findViewById(R.id.contactsname);
			//getButtonClick(name);
		delete = (TextView) findViewById(R.id.contactsdelete);
			//getButtonClick(delete);
		goback = (TextView) findViewById(R.id.goback);
			//getButtonClick(goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		String nameValue = GlobalVars.contactToDeleteName;
		nameValue = nameValue.replace("-","");
		nameValue = nameValue.replace("  "," ");
		GlobalVars.setText(name, false, nameValue);
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
					if(v==name)
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
		GlobalVars.lastActivity = ContactsDelete.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=3;
		GlobalVars.selectTextView(name,false);
		GlobalVars.selectTextView(delete,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutContactsDeleteOnResume));
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ CONTACT NAME
			GlobalVars.selectTextView(name,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(goback,false);
			String nameValue = GlobalVars.contactToDeleteName;
			nameValue = nameValue.replace("-","");
			nameValue = nameValue.replace("  "," ");
			GlobalVars.talk(getResources().getString(R.string.layoutContactsDeleteSelected) + nameValue);
			break;

			case 2: //CONFIRM DELETE
			GlobalVars.selectTextView(delete, true);
			GlobalVars.selectTextView(name,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutContactsDeleteDelete));
			break;

			case 3: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.selectTextView(name,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
	
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ CONTACT NAME
			String nameValue = GlobalVars.contactToDeleteName;
			nameValue = nameValue.replace("-","");
			nameValue = nameValue.replace("  "," ");
			GlobalVars.talk(getResources().getString(R.string.layoutContactsDeleteSelected) + nameValue);
			break;

			case 2: //CONFIRM DELETE
			GlobalVars.contactWasDeleted = deleteContact(this, GlobalVars.contactToDeleteName,GlobalVars.contactToDeletePhone);
			this.finish();
			break;

			case 3: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.contactToDeleteName="";
			GlobalVars.contactToDeletePhone="";
			GlobalVars.contactWasDeleted=false;
			this.finish();
			break;
			}
		}
	

		
	private boolean deleteContact(Context ctx, String name, String phoneNumber)
		{
		Uri contactUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		Cursor cur = ctx.getContentResolver().query(contactUri, null, null, null, null);
		try
			{
			if (cur.moveToFirst())
				{
				do
					{
					if (cur.getString(cur.getColumnIndex(PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name))
						{
						String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
						Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
						ctx.getContentResolver().delete(uri, null, null);
						return true;
						}
					}
				while (cur.moveToNext());
				}
			}
			catch (Exception e)
			{
			}
		return false;
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
