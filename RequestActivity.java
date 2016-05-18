package uncc.abilash.edu;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RequestActivity extends Activity {

    static app a = new app();
    private Handler handler;
    private Bundle bundle;
    private String name;

    private ArrayList<app> appliance = new ArrayList<app>();

    public static final String IPhome = a.IPhome;
    public static final String IPutil = a.IPutil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);


        Button requesthome = (Button)findViewById(R.id.button26);
        Button viewhome = (Button)findViewById(R.id.button27);
        Button requestutil = (Button)findViewById(R.id.button28);
        Button viewutil = (Button)findViewById(R.id.button29);
        Button back = (Button)findViewById(R.id.button30);


        viewhome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Intent homeIntent = new Intent(getBaseContext(), ViewHomeSchedule.class);
                homeIntent.getExtras();
                bundle = getIntent().getExtras();
                homeIntent.putExtras(bundle);
                startActivity(homeIntent);

            }
        });





        viewutil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Intent utilIntent = new Intent(getBaseContext(), ViewUtilSchedule.class);
                utilIntent.getExtras();
                bundle = getIntent().getExtras();
                utilIntent.putExtras(bundle);
                startActivity(utilIntent);

            }
        });



        requesthome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                String name = getIntent().getExtras().getString("username");

                String result = getConnection("http://" + IPhome + "/requesthome.php", name);
                //show.setText(result);

                //Toast.makeText(getBaseContext(),result,Toast.LENGTH_SHORT).show();

                if(result.equals("true\n"))
                {
                    Toast.makeText(getBaseContext(),"Schedule Requested",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(getBaseContext(), "Error requesting schedule, try again", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });


        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                finish();

            }
        });

        requestutil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String name = getIntent().getExtras().getString("username");

                String result = getConnection("http://" + IPutil + "/requestutil.php", name);
                //show.setText(result);

                //Toast.makeText(getBaseContext(),result,Toast.LENGTH_SHORT).show();

                if(result.equals("true\n"))
                {
                    Toast.makeText(getBaseContext(),"Schedule Requested",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(getBaseContext(), "Error requesting schedule, try again", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });



    }


    public String getConnection(String url, String name)
    {


        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();

        //nameValuePairs1.add(new BasicNameValuePair("trackno", trackno));
        nameValuePairs1.add(new BasicNameValuePair("username", name));

        //http postappSpinners
        try{
            HttpClient httpclient = new DefaultHttpClient();

            // have to change the ip here to your ip
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
        }
        catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
        }
        //convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            result=sb.toString();
            if(result.equalsIgnoreCase(null))
            {
                result = "invalid";
            }
            // else {
            result = result;
            //Toast.makeText(getBaseContext(), "Tracking Package", Toast.LENGTH_SHORT).show();
            //}
        }
        catch(Exception e){
            Log.e("log_tag", "Error converting result " + e.toString());
        }

        return result;


    }




    public String getConnection(String url, String usr, app ap) {

        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
        nameValuePairs1.add(new BasicNameValuePair("username", usr));
        nameValuePairs1.add(new BasicNameValuePair("appliancename", ap.getAppliancename()));
        nameValuePairs1.add(new BasicNameValuePair("starttime", Double.toString(ap.getStartime())));
        nameValuePairs1.add(new BasicNameValuePair("deadline", Double.toString(ap.getDeadline())));
        nameValuePairs1.add(new BasicNameValuePair("runtime", Integer.toString(ap.getRuntime())));
        nameValuePairs1.add(new BasicNameValuePair("power", Integer.toString(ap.getPower())));
        nameValuePairs1.add(new BasicNameValuePair("job", ap.getJobtype().toString()));
        nameValuePairs1.add(new BasicNameValuePair("currentstart", Integer.toString(ap.getCurrentstart())));
        nameValuePairs1.add(new BasicNameValuePair("currentend", Integer.toString(ap.getCurrentend())));

        //http postappSpinners
        try {
            HttpClient httpclient = new DefaultHttpClient();

            // have to change the ip here to correct ip
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
            Toast.makeText(getBaseContext(), "Server Not Responding", Toast.LENGTH_SHORT).show();
            return "";
        }
        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        return result;
    }




}
