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
public class ViewHomeSchedule extends Activity {


    static app a = new app();

    public static final String IPhome = a.IPhome;
    public static final String IPutil = a.IPutil;

    private Handler handler;
    private Bundle bundle;
    private String name;
    static String data;
    private ArrayList<String> apps = new ArrayList<String>();
    private ArrayList<app> appliance = new ArrayList<app>();
    ArrayAdapter<String> adapter;
    private int pos;
    private app appl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_home_schedule);


        Button back = (Button)findViewById(R.id.button31);
        Button AcceptHome = (Button)findViewById(R.id.button33);
        Button sendu = (Button)findViewById(R.id.button37);

        TextView show = (TextView)findViewById(R.id.textView25);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });



        name = getIntent().getExtras().getString("username");
        String conResult = getConnection("http://"+IPhome+"/gethstime.php", name);

        try {
            JSONArray jArray = new JSONArray(conResult);

            //for(int i=0;i<jArray.length();i++){
            for (int i = 0; i < jArray.length(); i++) {

                JSONObject json_data = jArray.getJSONObject(i);
                app ap = new app();
                ap.setAppliancename(json_data.getString("appliancename"));
                ap.setDeadline(json_data.getInt("deadline"));
                ap.setPower(json_data.getInt("power"));
                ap.setRuntime(json_data.getInt("runtime"));
                ap.setStartime(json_data.getInt("starttime"));
                ap.setTaskid(json_data.getInt("taskid"));
                ap.setHsstarttime(json_data.getInt("hsstarttime"));

                appliance.add(ap);

            }


        } catch (JSONException e) {
            //Log.e("log_tag", "Error parsing data "+e.toString());
            Log.e("log_tag", "Error parsing data " + e.toString());
        }


        data = "";


        for(int i=0; i<appliance.size();i++){

            //String conResult1 = getConnection("http://"+IPutil+"/puthsstart.php", name, appliance.get(i));


            data = data + "Appliance Name :  " +  appliance.get(i).getAppliancename() + "\n" +
                    "Scheduled Start time :  " + appliance.get(i).getHsstarttime() + "\n" +
                    "Scheduled end time   :  " + (appliance.get(i).getHsstarttime() + (appliance.get(i).getRuntime()*100)) + "\n";



        }

        show.setText(data);




        sendu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                for (int i = 0; i < appliance.size(); i++){

                    String start = Integer.toString(appliance.get(i).getHsstarttime());
                    String apname =  appliance.get(i).getAppliancename();

                    String Resulthome = getConnection("http://" + IPutil + "/puthsstart.php", name, apname, start);

                }


                Toast.makeText(getBaseContext(),"Home Schedule Sent to utility",Toast.LENGTH_SHORT).show();



            }
        });





        AcceptHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub



                for(int i=0; i<appliance.size(); i++){
                    String apname =  appliance.get(i).getAppliancename();
                    String start = Integer.toString(appliance.get(i).getHsstarttime());
                    int e = (appliance.get(i).getHsstarttime() + (appliance.get(i).getRuntime()*100));
                    String end = Integer.toString(e);

                    String Resulthome = getConnection("http://" + IPhome + "/accepthome.php", name, apname, start, end);
                    //String Resultutil = getConnection("http://"+IPutil+"/accepthome.php", name, apname, start, end);

                }

                Toast.makeText(getBaseContext(),"Home Schedule Accepted at home",Toast.LENGTH_SHORT).show();


                for(int i=0; i<appliance.size(); i++){
                    String apname =  appliance.get(i).getAppliancename();
                    String start = Integer.toString(appliance.get(i).getHsstarttime());
                    int e = (appliance.get(i).getHsstarttime() + (appliance.get(i).getRuntime()*100));
                    String end = Integer.toString(e);

                   // String Resulthome = getConnection("http://" + IPhome + "/accepthome.php", name, apname, start, end);
                    String Resultutil = getConnection("http://"+IPutil+"/accepthome.php", name, apname, start, end);

                }

                Toast.makeText(getBaseContext(),"Home Schedule Accepted at utility",Toast.LENGTH_SHORT).show();


                finish();
            }
        });


    }






    public String getConnection(String url, String usr, String apname, String start) {

        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();

        nameValuePairs1.add(new BasicNameValuePair("username", usr));
        nameValuePairs1.add(new BasicNameValuePair("appliancename", apname));
        nameValuePairs1.add(new BasicNameValuePair("hsstarttime",start));

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





    public String getConnection(String url, String name)
    {


        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();

        //nameValuePairs1.add(new BasicNameValuePair("trackno", trackno));
        nameValuePairs1.add(new BasicNameValuePair("username", name));
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





    public String getConnection(String url, String name, String apname, String start, String end)
    {


        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();

        //nameValuePairs1.add(new BasicNameValuePair("trackno", trackno));
        nameValuePairs1.add(new BasicNameValuePair("username", name));
        nameValuePairs1.add(new BasicNameValuePair("appliancename", apname));
        nameValuePairs1.add(new BasicNameValuePair("currentstart", start));
        nameValuePairs1.add(new BasicNameValuePair("currentend", end));
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
