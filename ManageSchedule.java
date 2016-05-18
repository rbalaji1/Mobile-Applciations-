package uncc.abilash.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ManageSchedule extends Activity {




    private Handler handler;
    private Bundle bundle;
    private String name;
    app ap = new app();
    app appl = new app();
    String data;
    int pos;

    static app a = new app();

    public static final String IP = a.IPhome;

    ArrayAdapter<String> adapter;
    ArrayList<app> appliances = new ArrayList<app>();
    ArrayList<String> apps = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_schedule);

        Button back = (Button) findViewById(R.id.button17);
        final Button start = (Button) findViewById(R.id.button15);
        Button dead = (Button) findViewById(R.id.button16);
        Button submit = (Button) findViewById(R.id.button36);

        final EditText s = (EditText)findViewById(R.id.editText);
        final EditText d = (EditText)findViewById(R.id.editText17);
        final TextView show1 = (TextView)findViewById(R.id.textView27);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });





        final Spinner select = (Spinner)findViewById(R.id.spinner6);

        // final Spinner appspinner = (Spinner)findViewById(R.id.spinner);
        //  final TextView showdata = (TextView)findViewById(R.id.textView);


        name = getIntent().getExtras().getString("username");

        String conResult = getConnection("http://"+IP+"/getappliance.php", name);

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

                appliances.add(ap);
                apps.add(ap.getAppliancename());

            }


        } catch (JSONException e) {
            //Log.e("log_tag", "Error parsing data "+e.toString());
            Log.e("log_tag", "Error parsing data " + e.toString());
        }


        adapter = new ArrayAdapter<String>(ManageSchedule.this, R.layout.list_item, R.id.txtName, apps);
        select.setPrompt("Select an appliance");
        select.setAdapter(adapter);

        select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int attrPos, long arg3) {
                pos = attrPos;
                //appl = appliances.get(pos);

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
                String apname = select.getItemAtPosition(pos).toString();
                show1.setText(apname);

            }
        });



        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String apname = select.getItemAtPosition(pos).toString();
                String username = getIntent().getExtras().getString("username");
                String starttime;
                starttime = s.getText().toString();



                if (starttime.equals("")) {
                    s.setError("Please provide the start time of the appliance");
                }  else {

                   // String stata = "running the connection";
                    String status = getConnection("http://"+IP+"/changestart.php", username, starttime, apname);

                    if (status.equals("true\n")) {
                        Toast.makeText(getBaseContext(), "Appliance Successfully Editted!!", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(getBaseContext(), "Error while editting appliance", Toast.LENGTH_SHORT).show();


                }

            }
        });


        dead.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String apname = select.getItemAtPosition(pos).toString();
                String username = getIntent().getExtras().getString("username");
                String deadline;
                deadline = d.getText().toString();



                if (deadline.equals("")) {
                    s.setError("Please provide the deadline of the appliance");
                } else {

                    // String stata = "running the connection";
                    String status = getConnection("http://"+IP+"/changedead.php", username, deadline, apname);

                    if (status.equals("true\n")) {
                        Toast.makeText(getBaseContext(), "Appliance Successfully Editted!!", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(getBaseContext(), "Error while editting appliance", Toast.LENGTH_SHORT).show();


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




    public String getConnection(String url, String name, String value, String apname)
    {


        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();

        //nameValuePairs1.add(new BasicNameValuePair("trackno", trackno));
        nameValuePairs1.add(new BasicNameValuePair("username", name));
        nameValuePairs1.add(new BasicNameValuePair("apname", apname));
        nameValuePairs1.add(new BasicNameValuePair("value", value));

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
