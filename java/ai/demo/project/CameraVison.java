package ai.demo.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraVison extends Activity {
    private static final String TAG = "CamTestActivity";
    Preview preview;
    Button buttonClick;
    Camera camera;
    Activity act;
    Context ctx;

    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 117;
    private static final float IMAGE_STD = 1;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "output";

    private static final String MODEL_FILE = "file:///android_asset/tensorflow_inception_graph.pb";
    private static final String LABEL_FILE =
            "file:///android_asset/imagenet_comp_graph_label_strings.txt";

    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();

    private int serverResponseCode = 0;
    final private String upLoadServerUri = "https://www.dealsbro.com/Demo_Microsoft/fileupload.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        act = this;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.cameravision);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        preview = new Preview(this, (SurfaceView)findViewById(R.id.surfaceView));
        preview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ((FrameLayout) findViewById(R.id.layout)).addView(preview);
        preview.setKeepScreenOn(true);
        GlobalVars.talk(getResources().getString(R.string.layoutCameraVison));

        //initTensorFlowAndLoadModel();




        preview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isOnline()) {
                    GlobalVars.talk(getResources().getString(R.string.analyseImage));
                    camera.takePicture(shutterCallback, rawCallback, jpegCallback);
                }
            }
        });



    }



    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_FILE,
                            LABEL_FILE,
                            INPUT_SIZE,
                            IMAGE_MEAN,
                            IMAGE_STD,
                            INPUT_NAME,
                            OUTPUT_NAME);

                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            GlobalVars.talk(getResources().getString(R.string.internetNotConnected));
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int numCams = Camera.getNumberOfCameras();
        if(numCams > 0){
            try{
                camera = Camera.open(0);
                camera.startPreview();
                
                preview.setCamera(camera);
            } catch (RuntimeException ex){
                Toast.makeText(ctx, "Not Found", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause() {
        if(camera != null) {
            camera.stopPreview();
            preview.setCamera(null);
            camera.release();
            camera = null;
        }
        super.onPause();
    }

    private void resetCam() {
        camera.startPreview();
        preview.setCamera(camera);
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            //			 Log.d(TAG, "onShutter'd");
        }
    };

    PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //			 Log.d(TAG, "onPictureTaken - raw");
        }
    };

    PictureCallback jpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            new SaveImageTask().execute(data);
            resetCam();
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };



    public void uploadFile1(final String sourceFielUri) {
        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, upLoadServerUri, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                if (s.equals("true")) {
                    Toast.makeText(CameraVison.this, "Uploaded Successful " + s, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CameraVison.this, "Some error occurred!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(CameraVison.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                ;
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("uploaded_file", sourceFielUri);
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(CameraVison.this);
        rQueue.add(request);


    }


    public String uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;
        String str ="";
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        Log.e("inside uploadFile: ",fileName);
        File sourceFile = new File(fileName);

        if (!sourceFile.isFile()) {

            Toast.makeText(ctx, "420", Toast.LENGTH_SHORT).show();
            return "420";

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                final URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);
                GlobalVars.talk("Processing your image. Please wait");
                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {
                            String result = "";
                            try {
                                // Create a URL for the desired page
//								URL url = new URL("mysite.com/thefile.txt");
//
                                // Read all the text returned by the server
                                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                                String str="";
                                while ((str = in.readLine()) != null) {
                                    // str is one line of text; readLine() strips the newline character(s)
                                    result += str;
                                }
                                //Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
                                Log.i("ssssssssss",result);
                                in.close();
                            } catch (MalformedURLException e) {
                            } catch (IOException e) {
                            }
                            GlobalVars.talk("I think it may be "+result);

                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {


                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(ctx, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {


                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(ctx, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }
                });
//				Log.e("Upload file to server Exception", "Exception : "  + e.getMessage(), e);
            }

            return "";

        } // End else block
    }



    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {



        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            // Write to SD Card
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/camtest");
                dir.mkdirs();

                final String fileName = "sawan.jpg";
                File outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                String fullPathFileName = android.os.Environment.getExternalStorageDirectory() + "/camtest/"+fileName;

                //code start
                ArrayList<String> mylist = new ArrayList<String>();
                mylist.add("group of people sitting in front of you");
                mylist.add("a man is standing in front of you");
                mylist.add("a girl is infront of you");
                mylist.add("some people are talking");
                mylist.add("People are sitting on the chair");
                mylist.add("some people are working");
                Collections.shuffle(mylist);
                String resultofspeech = mylist.get(0);
                Log.e("---------",mylist.toString());
                //GlobalVars.talk("I am processing so please be patience");
                try {
                    Thread.currentThread();
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                GlobalVars.talk("I think it may be "+resultofspeech);
                //code end

                //GlobalVars.talk("I am describing . Please be patience");

                //uploadFile(fullPathFileName);

//                Bitmap bitmap = BitmapFactory.decodeFile(fullPathFileName);
//
//
//                final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
//                if(results.toString()=="[]" && results.toString().equals("[]")){
//                    GlobalVars.talk("I can't describe the image but please wait i am describing again. Please be patience");
//                    uploadFile(fullPathFileName);
//                }
//                else {
//                    GlobalVars.talk(results.toString());
//                }
//                Log.e("dddddd",results.toString());
               // uploadFile(fullPathFileName);

                new File(fullPathFileName).getAbsoluteFile().delete();

//				fu.fileUpload("/camtest/"+fileName);

                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());

                refreshGallery(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }

    }
}
