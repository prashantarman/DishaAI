package ai.demo.project;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class News extends Activity
{
    private TextView business;
    private TextView entertainment;
    private TextView health;
    private TextView science;
    private TextView sports;
    private TextView technology;
    private TextView goback;


    // This string will hold the results
    String data = "";
    // Defining the Volley request queue that handles the URL request concurrently


    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);
        GlobalVars.lastActivity = ContactsCreate.class;
        business = (TextView) findViewById(R.id.business);
        //getButtonClick(business);
        entertainment = (TextView) findViewById(R.id.entertainment);
        //getButtonClick(entertainment);
        health = (TextView) findViewById(R.id.health);
        //getButtonClick(health);
        science = (TextView) findViewById(R.id.science);
        //getButtonClick(science);
        sports = (TextView) findViewById(R.id.sports);
        //getButtonClick(sports);
        technology = (TextView) findViewById(R.id.technology);
        //getButtonClick(technology);
        goback = (TextView) findViewById(R.id.goback);
        //getButtonClick(goback);
        GlobalVars.activityItemLocation=0;
        GlobalVars.activityItemLimit=7;




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
                     if(v==business)
                    GlobalVars.activityItemLocation=1;
                else if(v==entertainment)
                    GlobalVars.activityItemLocation=2;
                else if(v==health)
                    GlobalVars.activityItemLocation=3;
                else if(v==science)
                    GlobalVars.activityItemLocation=4;
                else if(v==sports)
                    GlobalVars.activityItemLocation=5;
                else if(v==technology)
                    GlobalVars.activityItemLocation=6;
                else if(v==goback)
                    GlobalVars.activityItemLocation=7;

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

        GlobalVars.talk(getResources().getString(R.string.layoutNewsOnResume));








    }


    public void getNews(String category)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String JsonURL = "https://newsapi.org/v2/top-headlines?country=in&category="+category+"&apiKey=6e85250a06a4427581567445b9cb0cf1";

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, JsonURL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        //Log.d("Response", response.toString());

                        data = response.toString();




                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Log.d("Error.Response", error.toString());
                        data = error.toString().replaceAll("com.android.volley.ParseError: org.json.JSONException: Value","");
                    }
                }
        );



        // add it to the RequestQueue
        queue.add(getRequest);
      //  Log.e("data",data);

        String result ="";
        try{

            JSONObject jsonObject = new JSONObject(data);

            JSONArray articles = jsonObject.getJSONArray("articles");

            for (int i = 0; i < articles.length(); i++) {
                JSONObject c = articles.getJSONObject(i);
                int article_number = i+1;
                result += "     Article number "+article_number+". "+c.getString("title")+". "+c.getString("description")+".";




            }

            if(result!="" && !result.equals("")) {
                Log.e("Response", result);
                GlobalVars.talk(result);
                result = "";
            }

        } catch (JSONException e) {

        }


    }


    public void select()
    {
        switch (GlobalVars.activityItemLocation)
        {
            case 1: //BUSINESS
                GlobalVars.selectTextView(business,true);
                GlobalVars.selectTextView(entertainment,false);
                GlobalVars.selectTextView(goback,false);
                GlobalVars.talk(getResources().getString(R.string.layoutBusiness));
                break;


            case 2: //ENTERTAINMENT
                GlobalVars.selectTextView(entertainment,true);
                GlobalVars.selectTextView(business,false);
                GlobalVars.selectTextView(health,false);
                GlobalVars.talk(getResources().getString(R.string.layoutEntertainment));
                break;

            case 3: //HEALTH
                GlobalVars.selectTextView(health, true);
                GlobalVars.selectTextView(entertainment,false);
                GlobalVars.selectTextView(science,false);
                GlobalVars.talk(getResources().getString(R.string.layoutHealth));
                break;

            case 4: //SCIENCE
                GlobalVars.selectTextView(science,true);
                GlobalVars.selectTextView(health,false);
                GlobalVars.selectTextView(sports,false);
                GlobalVars.talk(getResources().getString(R.string.layoutScience));
                break;

            case 5: //SPORTS
                GlobalVars.selectTextView(sports,true);
                GlobalVars.selectTextView(science,false);
                GlobalVars.selectTextView(technology,false);
                GlobalVars.talk(getResources().getString(R.string.layoutSports));
                break;

            case 6: //TECHNOLOGY
                GlobalVars.selectTextView(technology,true);
                GlobalVars.selectTextView(sports,false);
                GlobalVars.selectTextView(goback,false);
                GlobalVars.talk(getResources().getString(R.string.layoutTechnology));
                break;

            case 7: //GO BACK
                GlobalVars.selectTextView(goback,true);
                GlobalVars.selectTextView(technology,false);
                GlobalVars.selectTextView(business,false);
                GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
                break;
        }
    }

    public void execute()
    {
        switch (GlobalVars.activityItemLocation)
        {
            case 1: //BUSINESS
                getNews(getResources().getString(R.string.layoutBusiness));
                break;


            case 2: //ENTERTAINMENT
                getNews(getResources().getString(R.string.layoutEntertainment));
                break;

            case 3: //HEALTH
                getNews(getResources().getString(R.string.layoutHealth));
                break;

            case 4: //SCIENCE
                getNews(getResources().getString(R.string.layoutScience));
                break;

            case 5: //SPORTS
                getNews(getResources().getString(R.string.layoutSports));
                break;

            case 6: //TECHNOLOGY
                getNews(getResources().getString(R.string.layoutTechnology));
                break;

            case 7: //GO BACK
             GlobalVars.bookmarkToDeleteIndex = -1;
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
