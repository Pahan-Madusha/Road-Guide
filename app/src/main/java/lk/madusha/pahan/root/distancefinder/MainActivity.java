package lk.madusha.pahan.root.distancefinder;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static JSONObject results;
    private final String requestPattern = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial";
    private final String KEY = "AIzaSyAyOE0-MuGvLhmtsiStp72Hkvvp66FP49g";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get input from text boxes
        EditText loc1 = (EditText) findViewById(R.id.location1);
        EditText loc2 = (EditText) findViewById(R.id.location2);
        final String location1 = loc1.getText().toString();
        final String location2 = loc2.getText().toString();

        //button actions
        Button find = (Button) findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                results = getResults(location1, location2);
            }
        });
    }

    //get a json object with results
    public static JSONObject getResults(String loc1, String loc2)
    {
        JSONObject response = null;
        String result = sendGetRequest(requestPattern + "&origins=" + loc1 + "&destinations=" + loc2 + "&key=" + KEY);
        try {
            response = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    //sent a get request and return result string
    public static String sendGetRequest(String URL)
    {
        InputStream in = null;
        String result = "";

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(DownloadManager.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
