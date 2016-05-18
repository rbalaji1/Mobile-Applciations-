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

public class AddAppliance extends Activity {
    static app a = new app();

    public static final String IPhome = a.IPhome;
    public static final String IPutil = a.IPutil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_appliance);


        Button back = (Button)findViewById(R.id.button25);
        Button add = (Button)findViewById(R.id.button23);
        Button reset = (Button)findViewById(R.id.button24);

        final TextView show = (TextView)findViewById(R.id.textView24);

        final EditText appname = (EditText)findViewById(R.id.editText12);
        final EditText power = (EditText)findViewById(R.id.editText13);
        final EditText starttime = (EditText)findViewById(R.id.editText14);
        final EditText deadline = (EditText)findViewById(R.id.editText15);
        final EditText runtime = (EditText)findViewById(R.id.editText16);
        final EditText job = (EditText)findViewById(R.id.editText18);
        final EditText currentstart = (EditText)findViewById(R.id.editText19);
        final EditText currentend = (EditText)findViewById(R.id.editText20);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                appname.setText("");
                power.setText("");
                starttime.setText("");
                deadline.setText("");
                runtime.setText("");
                job.setText("");
                currentend.setText("");
                currentstart.setText("");

            }
        });



        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = getIntent().getExtras().getString("username");
                app aa = new app();
                aa.setAppliancename(appname.getText().toString());
                aa.setPower(Integer.parseInt(power.getText().toString()));
                aa.setStartime(Integer.parseInt(starttime.getText().toString()));
                aa.setRuntime(Integer.parseInt(runtime.getText().toString()));
                aa.setDeadline(Integer.parseInt(deadline.getText().toString()));
                aa.setCurrentstart(Integer.parseInt(currentstart.getText().toString()));
                aa.setCurrentend(Integer.parseInt(currentend.getText().toString()));
                aa.setJobtype(job.getText().toString());

                if(aa.getAppliancename().length() == 0)
                {
                    appname.setError("Please provide the appliance name");
                }
                else if (aa.getPower() == 0)
                {
                    power.setError("Please provide the power consumption of the appliance");
                }
                else if (aa.getStartime() > 2400 || a.getStartime() < 0)
                {
                    starttime.setError("Please provide a valid start time between 0000 and 2400");
                }
                else if (aa.getRuntime() == 0)
                {
                    runtime.setError("Please provide the runtime of the applaince");
                }
                else if (aa.getDeadline() == 0)
                {
                    deadline.setError("Please provide deadline for the appliance");
                }
                else if (aa.getJobtype().equalsIgnoreCase(""))
                {
                    job.setError("Please provide jobtype for the appliance");
                }

                else
                {

                    String status = getConnection("http://"+IPhome+"/addappliance.php", username,aa);

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    String status1 = getConnection("http://"+IPutil+"/addappliance.php", username,aa);

                    show.setText(status);
                    if (status.equals("true\n"))
                    {
                        Toast.makeText(getBaseContext(),"Appliance Successfully Added!!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                        Toast.makeText(getBaseContext(),"Error while adding appliance",Toast.LENGTH_SHORT).show();


                }

            }
        });


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
