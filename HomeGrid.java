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

public class HomeGrid extends Activity {


    static app a = new app();

    public static final String IP = a.IPhome;

    private Handler handler;
    private Bundle bundle;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_grid);

        Button viewApp = (Button)findViewById(R.id.button);
        Button manageApp = (Button)findViewById(R.id.button6);
        Button viewSch = (Button)findViewById(R.id.button7);
        Button manageSch = (Button)findViewById(R.id.button8);
        Button back = (Button) findViewById(R.id.button9);

        Button request = (Button) findViewById(R.id.button10);


        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        viewApp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent viewAppIntent = new Intent(getBaseContext(), ViewAppliance.class);
                viewAppIntent.getExtras();
                bundle = getIntent().getExtras();
                viewAppIntent.putExtras(bundle);
                startActivity(viewAppIntent);
            }
        });

        manageApp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent manageAppIntent = new Intent(getBaseContext(), ManageAppliance.class);
                manageAppIntent.getExtras();
                bundle = getIntent().getExtras();
                manageAppIntent.putExtras(bundle);
                startActivity(manageAppIntent);
            }
        });

        viewSch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent viewSchIntent = new Intent(getBaseContext(), ViewSchedule.class);
                viewSchIntent.getExtras();
                bundle = getIntent().getExtras();
                viewSchIntent.putExtras(bundle);
                startActivity(viewSchIntent);
            }
        });

        manageSch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent manageSchIntent = new Intent(getBaseContext(), ManageSchedule.class);
                manageSchIntent.getExtras();
                bundle = getIntent().getExtras();
                manageSchIntent.putExtras(bundle);
                startActivity(manageSchIntent);
            }
        });



        request.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Intent reqschIntent = new Intent(getBaseContext(), RequestActivity.class);
                reqschIntent.getExtras();
                bundle = getIntent().getExtras();
                reqschIntent.putExtras(bundle);
                startActivity(reqschIntent);

            }
        });










    }


}
