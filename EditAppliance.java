package uncc.abilash.edu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EditAppliance extends Activity {

    static app a = new app();

    public static final String IPhome = a.IPhome;
    public static final String IPutil = a.IPutil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appliance);


        Button back = (Button)findViewById(R.id.button22);
        Button set = (Button)findViewById(R.id.button20);
        Button reset = (Button)findViewById(R.id.button21);
        final TextView appname = (TextView)findViewById(R.id.textView16);
        final TextView stat = (TextView)findViewById(R.id.textView17);

        final EditText power = (EditText)findViewById(R.id.editText8);
        final EditText starttime = (EditText)findViewById(R.id.editText9);
        final EditText deadline = (EditText)findViewById(R.id.editText10);
        final EditText runtime = (EditText)findViewById(R.id.editText11);
        final EditText job = (EditText)findViewById(R.id.editText21);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });


        appname.setText(getIntent().getExtras().getString("appname"));

        reset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                power.setText("");
                starttime.setText("");
                deadline.setText("");
                runtime.setText("");
                job.setText("");

            }
        });



        set.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = getIntent().getExtras().getString("username");
                String apname = getIntent().getExtras().getString("appname");
                app a = new app();
                a.setPower(Integer.parseInt(power.getText().toString()));
                a.setStartime(Integer.parseInt(starttime.getText().toString()));
                a.setRuntime(Integer.parseInt(starttime.getText().toString()));
                a.setDeadline(Integer.parseInt(deadline.getText().toString()));
                a.setJobtype(job.getText().toString());

                if (a.getPower() == 0) {
                    power.setError("Please provide the power consumption of the appliance");
                } else if (a.getStartime() > 2400 || a.getStartime() < 0) {
                    starttime.setError("Please provide a valid start time between 0000 and 2400");
                } else if (a.getRuntime() == 0) {
                    runtime.setError("Please provide the runtime of the applaince");
                } else if (a.getDeadline() == 0) {
                    deadline.setError("Please provide deadline for the appliance");
                }
                else if (a.getJobtype().equalsIgnoreCase("")) {
                    job.setError("Please provide jobtype for the appliance");
                } else {

                    //String stata = "running the connection";
                    String status = getConnection("http://"+IPhome+"/editappliance.php", username, a, apname);


                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    String status1 = getConnection("http://"+IPutil+"/editappliance.php", username, a, apname);


                    if (status.equals("true\n")) {
                        Toast.makeText(getBaseContext(), "Appliance Successfully Editted!!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(getBaseContext(), "Error while editting appliance", Toast.LENGTH_SHORT).show();


                }

            }
        });


    }




    public String getConnection(String url, String usr, app ap, String appliancename) {

        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
        nameValuePairs1.add(new BasicNameValuePair("username", usr));
        nameValuePairs1.add(new BasicNameValuePair("applaincename", appliancename));
        nameValuePairs1.add(new BasicNameValuePair("starttime", Double.toString(ap.getStartime())));
        nameValuePairs1.add(new BasicNameValuePair("deadline", Double.toString(ap.getDeadline())));
        nameValuePairs1.add(new BasicNameValuePair("runtime", Integer.toString(ap.getRuntime())));
        nameValuePairs1.add(new BasicNameValuePair("power", Integer.toString(ap.getPower())));
        nameValuePairs1.add(new BasicNameValuePair("job", ap.getJobtype()));

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

