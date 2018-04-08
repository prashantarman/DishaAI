package ai.demo.project;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactsCreate extends Activity
	{
	private TextView name;
	private TextView phonenumber;
	private TextView create;
	private TextView goback;
	private int location;
	private final int LOCATION_NAME = 1;
	private final int LOCATION_PHONE = 2;
	private String nameValue = null;
	public static String phonenumberValue = null;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.contactscreate);
		GlobalVars.lastActivity = ContactsCreate.class;
		name = (TextView) findViewById(R.id.contactsname);
			//getButtonClick(name);
		phonenumber = (TextView) findViewById(R.id.contactsphone);
			//getButtonClick(phonenumber);
		create = (TextView) findViewById(R.id.contactscreate);
			//getButtonClick(create);
		goback = (TextView) findViewById(R.id.goback);
			//getButtonClick(goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		if (phonenumberValue!=null) //ONLY WHEN 'ADD TO NEW CONTACT' FROM CALL LOGS ACTIVITY IS SELECTED
			{
			GlobalVars.setText(phonenumber, false, String.valueOf(phonenumberValue));
			}
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
					else if(v==phonenumber)
						GlobalVars.activityItemLocation=2;
					else if(v==create)
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
		if (GlobalVars.inputModeResult!=null)
			{
			if (location==LOCATION_NAME)
				{
				GlobalVars.setText(name, false, GlobalVars.inputModeResult);
				nameValue = GlobalVars.inputModeResult;
				}
			else if (location==LOCATION_PHONE)
				{
				GlobalVars.setText(phonenumber, false, GlobalVars.inputModeResult);
				phonenumberValue = GlobalVars.inputModeResult;
				}
			GlobalVars.inputModeResult = null;
			}
		GlobalVars.lastActivity = ContactsCreate.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		GlobalVars.selectTextView(name,false);
		GlobalVars.selectTextView(phonenumber,false);
		GlobalVars.selectTextView(create,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutContactsCreateOnResume));
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //INPUT NAME
			GlobalVars.selectTextView(name,true);
			GlobalVars.selectTextView(phonenumber,false);
			GlobalVars.selectTextView(goback,false);
			if (nameValue==null)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutContactsCreateName2));
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutContactsCreateName3) + nameValue);
				}
			break;

			case 2: //INPUT PHONENUMBER
			GlobalVars.selectTextView(phonenumber, true);
			GlobalVars.selectTextView(name,false);
			GlobalVars.selectTextView(create,false);
			if (phonenumberValue==null)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutContactsCreatePhone2));
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutContactsCreatePhone3) + GlobalVars.divideNumbersWithBlanks(phonenumberValue));
				}
			break;

			case 3: //CREATE CONTACT
			GlobalVars.selectTextView(create, true);
			GlobalVars.selectTextView(phonenumber,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutContactsCreateCreate));
			break;
			
			case 4: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(name,false);
			GlobalVars.selectTextView(create,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //INPUT NAME
			location = LOCATION_NAME;
			GlobalVars.startInputActivity();
			break;

			case 2: //INPUT PHONENUMBER
			location = LOCATION_PHONE;
			GlobalVars.inputModeKeyboardOnlyNumbers=true;
			GlobalVars.startInputActivity();
			break;

			case 3: //CREATE CONTACT
			if (nameValue==null | phonenumberValue==null)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutContactsCreateErrorComplete));
				}
				else
				{
				createContact(nameValue,phonenumberValue);
				GlobalVars.contactWasCreated=true;
				this.finish();
				}
			break;
			
			case 4: //GO BACK TO THE PREVIOUS MENU
			phonenumberValue = null;
			this.finish();
			break;
			}
		}


		
	private void createContact(String name, String phoneNumber)
		{
		ArrayList <ContentProviderOperation> ops = new ArrayList < ContentProviderOperation > ();
		ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,name).build());
		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber).withValue(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
		try
			{
		    getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			}
			catch (Exception e)
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
