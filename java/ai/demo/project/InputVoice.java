package ai.demo.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ai.demo.project.R;

public class InputVoice extends Activity
	{
    private SpeechRecognizer sr;
	private TextView start;
	private TextView resultsTextview;
	private List<String> stringResults = new ArrayList<String>();
	private int selectedValue = -1;
	private TextView enter;
	private TextView goback;

	@Override protected void onCreate(Bundle savedInstanceState)
		{
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.inputvoice);
		GlobalVars.lastActivity = InputVoice.class;
		start = (TextView) findViewById(R.id.startrecognition);
			//getButtonClick(start);
		resultsTextview = (TextView) findViewById(R.id.possibleresults);
			//getButtonClick(resultsTextview);
		enter = (TextView) findViewById(R.id.enter);
			//getButtonClick(enter);
		goback = (TextView) findViewById(R.id.goback);
			//getButtonClick(goback);
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		stringResults.clear();
		GlobalVars.setText(resultsTextview, false, getResources().getString(R.string.layoutInputVoicePossibleResults) + stringResults.size() + ")");
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
					if(v==start)
						GlobalVars.activityItemLocation=1;
					else if(v==resultsTextview)
						GlobalVars.activityItemLocation=2;
					else if(v==enter)
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
		GlobalVars.lastActivity = InputVoice.class;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=4;
		GlobalVars.selectTextView(start,false);
		GlobalVars.selectTextView(resultsTextview,false);
		GlobalVars.selectTextView(enter,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutInputVoiceOnResume));
		}
		
    @Override protected void onDestroy()
    	{
    	super.onDestroy();
    	try
    		{
    		if(sr!=null)
    			{
    		    sr.destroy();
    			}    		
    		}
			catch(NullPointerException e)
			{
			}
    		catch(Exception e)
    		{
    		}
    	}
	
    class listener implements RecognitionListener
    	{
    	public void onReadyForSpeech(Bundle params)
            {
            }

    	public void onBeginningOfSpeech()
            {
            }

    	public void onRmsChanged(float rmsdB)
            {
            }

    	public void onBufferReceived(byte[] buffer)
            {
            }

    	public void onEndOfSpeech()
            {
            }

    	public void onError(int error)
            {
			stringResults.clear();
			selectedValue = -1;
    		if (GlobalVars.activityItemLocation==2)
    			{
    			GlobalVars.setText(resultsTextview, true, getResources().getString(R.string.layoutInputVoicePossibleResults) + stringResults.size() + ")");
    			}
				else
				{
				GlobalVars.setText(resultsTextview, false, getResources().getString(R.string.layoutInputVoicePossibleResults) + stringResults.size() + ")");
				}
    		GlobalVars.talk(getResources().getString(R.string.layoutInputVoiceNoRecognition));
            }

    	public void onResults(Bundle results)
            {
    		try
    			{
    			stringResults.clear();
        		if (GlobalVars.activityItemLocation==2)
        			{
        			GlobalVars.setText(resultsTextview, true, getResources().getString(R.string.layoutInputVoicePossibleResults) + stringResults.size() + ")");
        			}
    				else
    				{
   					GlobalVars.setText(resultsTextview, false, getResources().getString(R.string.layoutInputVoicePossibleResults) + stringResults.size() + ")");
    				}
        		ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        		for (int i=0;i<data.size();i++)
            		{
    	        	stringResults.add(data.get(i));
            		}
        		if (GlobalVars.activityItemLocation==2)
    				{
        			GlobalVars.setText(resultsTextview, true, getResources().getString(R.string.layoutInputVoicePossibleResults) + stringResults.size() + ")");
    				}
					else
					{
					GlobalVars.setText(resultsTextview, false, getResources().getString(R.string.layoutInputVoicePossibleResults) + stringResults.size() + ")");
					}
    			GlobalVars.talk(getResources().getString(R.string.layoutInputVoicePossibleResults2) + stringResults.size());
    			}
				catch(NullPointerException e)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutInputVoiceSystemError));
				}
    			catch(Exception e)
    			{
   				GlobalVars.talk(getResources().getString(R.string.layoutInputVoiceSystemError));
    			}
            }

    	public void onPartialResults(Bundle partialResults)
            {
			stringResults.clear();
			selectedValue = -1;
    		if (GlobalVars.activityItemLocation==2)
    			{
    			GlobalVars.setText(resultsTextview, true, getResources().getString(R.string.layoutInputVoicePossibleResults) + stringResults.size() + ")");
    			}
				else
				{
				GlobalVars.setText(resultsTextview, false, getResources().getString(R.string.layoutInputVoicePossibleResults) + stringResults.size() + ")");
				}
    		GlobalVars.talk(getResources().getString(R.string.layoutInputVoiceNoRecognition));
            }

    	public void onEvent(int eventType, Bundle params)
            {
            }
    	}
		
	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //START RECOGNITION
			GlobalVars.selectTextView(start,true);
			GlobalVars.selectTextView(resultsTextview,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutInputVoiceStartRecognition2));
			break;

			case 2: //RESULTS IN TEXTVIEW
			GlobalVars.selectTextView(resultsTextview, true);
			GlobalVars.selectTextView(start,false);
			GlobalVars.selectTextView(enter,false);
			if (selectedValue==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutInputVoicePossibleResults2) + stringResults.size() +  getResources().getString(R.string.layoutInputVoicePossibleResults3));
				}
				else
				{
				GlobalVars.talk(getResources().getString(R.string.layoutInputVoicePossibleResult) + (selectedValue + 1) + ". " + stringResults.get(selectedValue));
				}
			break;
			
			case 3: //ENTER SELECTED RESULT
			GlobalVars.selectTextView(enter, true);
			GlobalVars.selectTextView(resultsTextview,false);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutInputVoiceEnterSelectedResult));
			break;
			
			case 4: //CANCEL AND GO BACK TO MAIN MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(enter,false);
			GlobalVars.selectTextView(start,false);
			GlobalVars.talk(getResources().getString(R.string.layoutInputVoiceCancelAndGoBack));
			break;
			}
		}

	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //START RECOGNITION
			try
				{
				stringResults.clear();
				selectedValue = -1;
				sr = SpeechRecognizer.createSpeechRecognizer(this);
				sr.setRecognitionListener(new listener());
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Voice Recognition...");
				intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,5000000);
				sr.startListening(intent);
				}
				catch(NullPointerException e)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutInputVoiceSystemError));
				}
				catch(Exception e)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutInputVoiceSystemError));
				}
			break;

			case 2: //RESULTS IN TEXTVIEW
			if (stringResults.size()==0)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutInputVoiceNoRecognition));
				}
				else
				{
				if (selectedValue+1==stringResults.size())
					{
					selectedValue=-1;
					}
				selectedValue = selectedValue + 1;
    			GlobalVars.setText(resultsTextview, true, getResources().getString(R.string.layoutInputVoicePossibleResults) + stringResults.size() + ")\n" + (selectedValue + 1) + " - " + stringResults.get(selectedValue));
				GlobalVars.talk(getResources().getString(R.string.layoutInputVoicePossibleResult) + (selectedValue + 1) + ". " + stringResults.get(selectedValue));
				}
			break;
			
			case 3: //ENTER SELECTED RESULT
			if (selectedValue==-1)
				{
				GlobalVars.talk(getResources().getString(R.string.layoutInputVoiceNoResultToSelect));
				}
				else
				{
				GlobalVars.inputModeResult = stringResults.get(selectedValue);
				this.finish();
				}
			break;

			case 4: //GO BACK TO MAIN MENU
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
