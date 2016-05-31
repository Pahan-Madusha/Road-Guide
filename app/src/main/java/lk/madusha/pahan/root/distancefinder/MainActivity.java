package lk.madusha.pahan.root.distancefinder;

import android.app.DownloadManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static JSONObject results = null;
    private static final String requestPattern = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial";
    private static final String KEY = "AIzaSyAyOE0-MuGvLhmtsiStp72Hkvvp66FP49g";
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        //get input from text boxes
        EditText loc1 = (EditText) findViewById(R.id.location1);
        EditText loc2 = (EditText) findViewById(R.id.location2);
        final TextView display = (TextView) findViewById(R.id.display);

        final String location1 = loc1.getText().toString();
        final String location2 = loc2.getText().toString();

        //button actions
        Button find = (Button) findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResults(location1, location2);
                if(results != null)
                    display.setText(results.toString());
                else
                    System.out.println("results is null");
            }
        });
    }

    //get a json object with results
    public static void getResults(String loc1, String loc2)
    {
        sendGetRequest(requestPattern + "&origins=" + loc1 + "&destinations=" + loc2 + "&key=" + KEY);
    }

    //sent a get request and return result string
    public static void sendGetRequest(String URL)
    {
        InputStream in = null;
        final String result;

        RequestQueue queue = MyVolley.getRequestQueue();
        if(queue == null)
            queue = MyVolley.newRequestQueue(context);

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET,URL, null,
                reqSuccessListener(),
                reqErrorListener());
        System.out.println("Just after sending request");

        queue.add(myReq);

    }

    private static Response.Listener<JSONObject> reqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
               results = response;
               System.out.println("inside on response");
            }
        };
    }


    private static Response.ErrorListener reqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("err resp");
                results = null;
            }
        };
    }

}
