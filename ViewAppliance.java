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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ViewAppliance extends Activity {



    static app a = new app();

    public static final String IPhome = a.IPhome;
    public static final String IPutil = a.IPutil;
    private Handler handler;
    private Bundle bundle;
    private String name;
    static String data;
    private ArrayList<String> apps = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private int pos;
    private app appl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appliance);


        Button back = (Button) findViewById(R.id.button18);


        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        name = getIntent().getExtras().getString("username");
         app ap = new app();

        final Spinner appspinner = (Spinner)findViewById(R.id.spinner);
        final TextView showdata = (TextView)findViewById(R.id.textView);
        Button submit = (Button) findViewById(R.id.button19);

        String conResult = getConnection("http://"+IPhome+"/getappliance.php", name);

        try {
            JSONArray jArray = new JSONArray(conResult);

            //for(int i=0;i<jArray.length();i++){
            for (int i = 0; i < jArray.length(); i++) {
                ap = new app();
                JSONObject json_data = jArray.getJSONObject(i);
                ap.setAppliancename(json_data.getString("appliancename"));
                ap.setDeadline(json_data.getInt("deadline"));
                ap.setPower(json_data.getInt("power"));
                ap.setRuntime(json_data.getInt("runtime"));
                ap.setStartime(json_data.getInt("starttime"));
                ap.setTaskid(json_data.getInt("taskid"));

                apps.add(ap.getAppliancename());

            }


        } catch (JSONException e) {
            //Log.e("log_tag", "Error parsing data "+e.toString());
            Log.e("log_tag", "Error parsing data " + e.toString());
        }


        adapter = new ArrayAdapter<String>(ViewAppliance.this, R.layout.list_item, R.id.txtName, apps);
        appspinner.setPrompt("Select an appliance");
        appspinner.setAdapter(adapter);

        appspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int attrPos, long arg3) {
                pos = attrPos;


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                app ap = new app();
                String apname = appspinner.getItemAtPosition(pos).toString();
                String conResult = getConnection("http://"+IPhome+"/gettheapp.php", name, apname);

                try {
                    JSONArray jArray = new JSONArray(conResult);

                    //for(int i=0;i<jArray.length();i++){
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        ap.setAppliancename(json_data.getString("appliancename"));
                        ap.setDeadline(json_data.getInt("deadline"));
                        ap.setPower(json_data.getInt("power"));
                        ap.setRuntime(json_data.getInt("runtime"));
                        ap.setStartime(json_data.getInt("starttime"));
                        ap.setTaskid(json_data.getInt("taskid"));
                        ap.setJobtype(json_data.getString("jobtype"));

                    }


                } catch (JSONException e) {
                    //Log.e("log_tag", "Error parsing data "+e.toString());
                    Log.e("log_tag", "Error parsing data " + e.toString());
                }

                data = pos + "\n" +"Appliance name    : " + ap.getAppliancename() + "\n" +
                        "Power requirement: " + ap.getPower() + "\n" +
                        "Start time       : " + ap.getStartime() + "\n" +
                        "Deadline         : " + ap.getDeadline() + "\n" +
                        "Runtime          : " + ap.getRuntime() + "\n";

                showdata.setText(data);

            }
        });

    }



    public String getConnection(String url, String name, String apname)
    {


        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();

        //nameValuePairs1.add(new BasicNameValuePair("trackno", trackno));
        nameValuePairs1.add(new BasicNameValuePair("username",name));
        nameValuePairs1.add(new BasicNameValuePair("appliancename",apname));

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



    public String getConnection(String url, String name)
    {


        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();

        //nameValuePairs1.add(new BasicNameValuePair("trackno", trackno));
        nameValuePairs1.add(new BasicNameValuePair("username",name));
        //nameValuePairs1.add(new BasicNameValuePair("appliancename",apname));

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




}
