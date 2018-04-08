package ai.demo.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ai.demo.project.R;

public class DishaAI extends Activity
{
    private SpeechRecognizer sr;
    private TextView start;

    private TextView goback;
    private List<String> stringResults = new ArrayList<String>();

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disha_ai);
        GlobalVars.lastActivity = Main.class;
        start = (TextView) findViewById(R.id.startrecognition);

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
        GlobalVars.lastActivity = Main.class;
        GlobalVars.activityItemLocation=0;
        GlobalVars.activityItemLimit=2;
        GlobalVars.selectTextView(start,false);
        GlobalVars.selectTextView(goback,false);
        GlobalVars.talk("Now you are in Voice command menu");


//        stringResults.clear();
//        sr = SpeechRecognizer.createSpeechRecognizer(this);
//        sr.setRecognitionListener(new listener());
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Voice Recognition...");
//        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,5000000);
//        sr.startListening(intent);

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

            GlobalVars.talk(getResources().getString(R.string.layoutInputVoiceNoRecognition));
        }

        public void onResults(Bundle results)
        {
            try
            {

                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for (int i=0;i<data.size();i++)
                {
                    stringResults.add(data.get(i));
                }
                GlobalVars.getVoiceRecognitionResult(stringResults.toString());
                Log.e("String resulttttt",stringResults.toString());
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
                GlobalVars.selectTextView(goback,false);
                GlobalVars.talk(getResources().getString(R.string.layoutInputVoiceStartRecognition2));
                break;



            case 2: //CANCEL AND GO BACK TO MAIN MENU
                GlobalVars.selectTextView(goback,true);
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



            case 2: //GO BACK TO MAIN MENU
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
