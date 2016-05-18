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
import android.widget.Spinner;
import android.widget.TextView;

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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ManageAppliance extends Activity {

    private Handler handler;
    private Bundle bundle;
    private String name;
    app ap = new app();
    app appl = new app();
    String data;
    int pos;

    static app a = new app();

    public static final String IPhome = a.IPhome;
    public static final String IPutil = a.IPutil;

    ArrayAdapter<String> adapter;
    ArrayList<app> appliances = new ArrayList<app>();
    ArrayList<String> apps = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_appliance);

        Button back = (Button) findViewById(R.id.button13);


        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });



        Button add = (Button) findViewById(R.id.button10);
        Button delete = (Button) findViewById(R.id.button11);
        Button edit = (Button) findViewById(R.id.button12);

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent addAppIntent = new Intent(getBaseContext(), AddAppliance.class);
                addAppIntent.getExtras();
                bundle = getIntent().getExtras();
                addAppIntent.putExtras(bundle);
                startActivity(addAppIntent);
            }
        });

        final Spinner select = (Spinner)findViewById(R.id.spinner4);

       // final Spinner appspinner = (Spinner)findViewById(R.id.spinner);
      //  final TextView showdata = (TextView)findViewById(R.id.textView);

        name = getIntent().getExtras().getString("username");

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

                appliances.add(ap);
                apps.add(ap.getAppliancename());

            }


        } catch (JSONException e) {
            //Log.e("log_tag", "Error parsing data "+e.toString());
            Log.e("log_tag", "Error parsing data " + e.toString());
        }


        adapter = new ArrayAdapter<String>(ManageAppliance.this, R.layout.list_item, R.id.txtName, apps);
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


        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String apname = select.getItemAtPosition(pos).toString();
                String name = getIntent().getExtras().getString("username");

                String result = getConnection("http://" + IPhome + "/delete.php", name, apname);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String result1 = getConnection("http://" + IPutil + "/delete.php", name, apname);

                if(result.equals("true\n"))
                {
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getBaseContext(),"Deleted appliance!!",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(getBaseContext(), "Error deleting appliance", Toast.LENGTH_SHORT).show();
                }


            }
        });



        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent editAppIntent = new Intent(getBaseContext(), EditAppliance.class);
                editAppIntent.getExtras();
                bundle = getIntent().getExtras();
                bundle.putString("appname", select.getItemAtPosition(pos).toString());
                editAppIntent.putExtras(bundle);
                startActivity(editAppIntent);
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



    public String getConnection(String url, String name, String apname)
    {


        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();

        //nameValuePairs1.add(new BasicNameValuePair("trackno", trackno));
        nameValuePairs1.add(new BasicNameValuePair("username", name));
        nameValuePairs1.add(new BasicNameValuePair("appliancename", apname));

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
