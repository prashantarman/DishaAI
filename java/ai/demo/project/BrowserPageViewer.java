package ai.demo.project;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class BrowserPageViewer extends Activity
	{
	private TextView pagetitle;
	private TextView pagetext;
	private TextView pagelinks;
	private TextView linksgoto;
	private TextView addtobookmarks;
	private TextView goback;
	public static int linkLocation = -1;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.browserpageviewer);
		GlobalVars.lastActivity = BrowserPageViewer.class;
		pagetitle = (TextView) findViewById(R.id.readpagetitle);
			//getButtonClick(pagetitle);
		pagetext = (TextView) findViewById(R.id.readpagetext);
			//getButtonClick(pagetext);
		pagelinks = (TextView) findViewById(R.id.pagelinks);
			//getButtonClick(pagelinks);
		linksgoto = (TextView) findViewById(R.id.linksgoto);
			//getButtonClick(linksgoto);
		addtobookmarks = (TextView) findViewById(R.id.addtobookmarks);
			//getButtonClick(addtobookmarks);
		goback = (TextView) findViewById(R.id.goback);
			//getButtonClick(goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=6;
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
					if(v==pagetitle)
						GlobalVars.activityItemLocation=1;
					else if(v==pagetext)
						GlobalVars.activityItemLocation=2;
					else if(v==pagelinks)
						GlobalVars.activityItemLocation=3;
					else if(v==linksgoto)
						GlobalVars.activityItemLocation=4;
					else if(v==addtobookmarks)
						GlobalVars.activityItemLocation=5;
					else if(v==goback)
						GlobalVars.activityItemLocation=6;

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
		GlobalVars.lastActivity = BrowserPageViewer.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=6;
		GlobalVars.selectTextView(pagetitle,false);
		GlobalVars.selectTextView(pagetext,false);
		GlobalVars.selectTextView(pagelinks,false);
		GlobalVars.selectTextView(linksgoto,false);
		GlobalVars.selectTextView(addtobookmarks,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerOnResume));
		}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ PAGE TITLE
			GlobalVars.selectTextView(pagetitle,true);
			GlobalVars.selectTextView(pagetext,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerReadTitle));
			break;
			
			case 2: //READ PAGE TEXT
			GlobalVars.selectTextView(pagetext,true);
			GlobalVars.selectTextView(pagetitle,false);
			GlobalVars.selectTextView(pagelinks,false);
			GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerReadText));
			break;

			case 3: //PAGE LINKS
			GlobalVars.selectTextView(pagelinks, true);
			GlobalVars.selectTextView(pagetext,false);
			GlobalVars.selectTextView(linksgoto,false);
			if (linkLocation==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerPageLinks2));
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerPageLinks3) + String.valueOf(linkLocation + 1) + getResources().getString(R.string.layoutBrowserPageViewerPageLinks3Of) + String.valueOf(GlobalVars.browserWebLinks.size()) + ". " + GlobalVars.browserWebLinks.get(linkLocation).substring(0,GlobalVars.browserWebLinks.get(linkLocation).indexOf("|")));
				}
			break;

			case 4: //GO TO LINK
			GlobalVars.selectTextView(linksgoto, true);
			GlobalVars.selectTextView(pagelinks,false);
			GlobalVars.selectTextView(addtobookmarks,false);
			GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerGoToLink));
			break;
			
			case 5: //ADD TO BOOKMARKS
			GlobalVars.selectTextView(addtobookmarks, true);
			GlobalVars.selectTextView(linksgoto,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerAddToBookmarks));
			break;
			
			case 6: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(addtobookmarks,false);
			GlobalVars.selectTextView(pagetitle,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //READ PAGE TITLE
			GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerReadTitle2) + GlobalVars.browserWebTitle);
			break;
			
			case 2: //READ PAGE TEXT
			GlobalVars.talk(GlobalVars.browserWebText);
			break;

			case 3: //PAGE LINKS
			if (GlobalVars.browserWebLinks.size()==0)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerPageLinks4));
				}
				else
				{
				if (linkLocation+1==GlobalVars.browserWebLinks.size())
					{
					linkLocation=-1;
					}
				linkLocation = linkLocation + 1;
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerPageLinks3) + String.valueOf(linkLocation + 1) + getResources().getString(R.string.layoutBrowserPageViewerPageLinks3Of) + String.valueOf(GlobalVars.browserWebLinks.size()) + ". " + GlobalVars.browserWebLinks.get(linkLocation).substring(0,GlobalVars.browserWebLinks.get(linkLocation).indexOf("|")));
				}
			break;

			case 4: //GO TO LINK
			if (linkLocation==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerPageLinkSelectError));
				}
				else
				{
				new BrowserThreadGoTo().execute(GlobalVars.browserWebLinks.get(linkLocation).substring(GlobalVars.browserWebLinks.get(linkLocation).indexOf("|") + 1, GlobalVars.browserWebLinks.get(linkLocation).length()));
				}
			break;
			
			case 5: //ADD TO BOOKMARKS
			String newBookmark = GlobalVars.browserWebTitle + "|" + GlobalVars.browserWebURL;
			boolean repeatedBookmark = false;
			for (int i=0;i<GlobalVars.browserBookmarks.size();i++)
				{
				if (GlobalVars.browserBookmarks.get(i).toLowerCase().equals(newBookmark.toLowerCase()))
					{
					repeatedBookmark = true;
					i = GlobalVars.browserBookmarks.size();
					}
				}
			if (repeatedBookmark==true)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerAddToBookmarksRepeated));
				}
				else
				{
				GlobalVars.browserBookmarks.add(newBookmark);
				GlobalVars.sortBookmarksDatabase();
				GlobalVars.saveBookmarksDatabase();
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerAddToBookmarksAdded));
				}
			break;
			
			case 6: //GO BACK TO THE PREVIOUS MENU
			this.finish();
			break;
			}
		}

	private void previousItem()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 3: //PAGE LINKS
			if (GlobalVars.browserWebLinks.size()==0)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerPageLinks4));
				}
				else
				{
				if (linkLocation-1<0)
					{
					linkLocation = GlobalVars.browserWebLinks.size();
					}
				linkLocation = linkLocation - 1;
				GlobalVars.talk(getResources().getString(R.string.layoutBrowserPageViewerPageLinks3) + String.valueOf(linkLocation + 1) + getResources().getString(R.string.layoutBrowserPageViewerPageLinks3Of) + String.valueOf(GlobalVars.browserWebLinks.size()) + ". " + GlobalVars.browserWebLinks.get(linkLocation).substring(0,GlobalVars.browserWebLinks.get(linkLocation).indexOf("|")));
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