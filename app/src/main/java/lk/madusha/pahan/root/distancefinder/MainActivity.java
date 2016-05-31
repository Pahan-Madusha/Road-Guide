package lk.madusha.pahan.root.distancefinder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static JSONObject results = null;
    private static final String requestPattern = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial";
    private static final String KEY = "AIzaSyAyOE0-MuGvLhmtsiStp72Hkvvp66FP49g";
    static Context context;
    static TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        //get input from text boxes
        final EditText loc1 = (EditText) findViewById(R.id.location1);
        final EditText loc2 = (EditText) findViewById(R.id.location2);
        display = (TextView) findViewById(R.id.display);

        //button actions
        Button find = (Button) findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNetworkAvailable())
                    System.out.println("Not connected");
                getResults( loc1.getText().toString(),  loc2.getText().toString());
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
        System.out.println(URL);
        InputStream in = null;
        final String result;

        RequestQueue queue = MyVolley.getRequestQueue();
        if(queue == null)
            queue = MyVolley.newRequestQueue(context);

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET,URL, null,
                reqSuccessListener(),
                reqErrorListener());
        myReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(myReq);
        queue.start();

    }

    //success request
    private static Response.Listener<JSONObject> reqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
               results = response;
               System.out.println("inside on response");
                display.setText(response.toString());
            }
        };
    }

    //error
    private static Response.ErrorListener reqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                display.setText(error.toString());
                results = null;
            }
        };
    }

    //check connectivity
    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
