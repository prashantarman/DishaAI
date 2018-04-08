package ai.demo.project;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Browser extends Activity
	{
	private TextView browsergoogle;
	private TextView bookmarks;
	private TextView goback;
	private TextView bookmarksdelete;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.browser);
		GlobalVars.lastActivity = Browser.class;
		browsergoogle = (TextView) findViewById(R.id.browsergoogle);
			//getButtonClick(browsergoogle);
		bookmarks = (TextView) findViewById(R.id.bookmarks);
			//getButtonClick(bookmarks);
		bookmarksdelete = (TextView) findViewById(R.id.bookmarksdelete);
			//getButtonClick(bookmarksdelete);
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
					if(v==browsergoogle)
						GlobalVars.activityItemLocation=1;
					else if(v==bookmarks)
						GlobalVars.activityItemLocation=2;
					else if(v==bookmarksdelete)
						GlobalVars.activityItemLocation=4;
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
		GlobalVars.lastActivity = Browser.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		GlobalVars.selectTextView(browsergoogle,false);
		GlobalVars.selectTextView(bookmarks,false);
		GlobalVars.selectTextView(bookmarksdelete,false);
		GlobalVars.selectTextView(goback,false);
		if (GlobalVars.inputModeResult!=null)
			{
			if (GlobalVars.browserRequestInProgress==false)
				{
				GlobalVars.browserRequestInProgress=true;
				new BrowserThreadGoTo().execute("http://www.google.com/custom?q=" + GlobalVars.inputModeResult);
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserErrorPendingRequest));
				}
			GlobalVars.inputModeResult = null;
			}
			else
			{
			GlobalVars.talk(getResources().getString(R.string.layoutBrowserOnResume));
			}
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //SEARCH IN GOOGLE
			GlobalVars.selectTextView(browsergoogle,true);
			GlobalVars.selectTextView(bookmarks,false);
			GlobalVars.selectTextView(goback,false);
			if (GlobalVars.browserRequestInProgress==true)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserSearchInGoogle) + 
								getResources().getString(R.string.layoutBrowserAWebPageItsBeenLoading));
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserSearchInGoogle));
				}
			break;

			case 2: //LIST BOOKMARKS
			GlobalVars.selectTextView(bookmarks, true);
			GlobalVars.selectTextView(browsergoogle,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutBrowserListBookmarks));
			break;





			case 3: //GO BACK TO THE MAIN MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(bookmarks,false);
			GlobalVars.selectTextView(browsergoogle,false);
			GlobalVars.talk(getResources().getString(R.string.backToMainMenu));
			break;

				case 4: //BOOKMARKS DELETE
					GlobalVars.selectTextView(bookmarksdelete,true);
					GlobalVars.selectTextView(bookmarks,false);
					GlobalVars.selectTextView(browsergoogle,false);
					GlobalVars.talk("Delete Bookmarks");
					break;



			}
		}

	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //SEARCH IN GOOGLE
			if (GlobalVars.browserRequestInProgress==false)
				{
				GlobalVars.startInputActivity();
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserErrorPendingRequest));
				}
			break;

			case 2: //LIST BOOKMARKS
			GlobalVars.startActivity(BookmarksList.class);
			break;

			case 3: //GO BACK TO THE MAIN MENU
			this.finish();
			break;


				case 4: //GO BACK TO THE MAIN MENU
					GlobalVars.startActivity(BookmarksDelete.class);
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