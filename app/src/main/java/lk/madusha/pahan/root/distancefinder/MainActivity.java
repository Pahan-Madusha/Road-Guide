package lk.madusha.pahan.root.distancefinder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static JSONObject results = null;
    private static final String requestPattern = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric";
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
                display.setText("");
                if (!isNetworkAvailable())
                    display.setText("Please check your internet connection!");
                else
                    getResults(loc1.getText().toString(), loc2.getText().toString());

            }
        });

        //find my location
        Button myLocation = (Button) findViewById(R.id.mylocation);
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loc1.setText(findMyLocation());
            }
        });
    }

    //get a json object with results
    public static void getResults(String loc1, String loc2) {
        sendGetRequest(requestPattern + "&origins=" + loc1 + "&destinations=" + loc2 + "&key=" + KEY);

    }

    //sent a get request and return result string
    public static void sendGetRequest(String URL) {
        System.out.println(URL);
        InputStream in = null;
        final String result;

        RequestQueue queue = MyVolley.getRequestQueue();
        if (queue == null)
            queue = MyVolley.newRequestQueue(context);

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                reqSuccessListener(),
                reqErrorListener());

        //retry policies
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
            public void onResponse(JSONObject response) {
                results = response;
                display.setText(processResponse(response));
            }
        };
    }

    //error
    private static Response.ErrorListener reqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                display.setText("Some error occured! Please try again");
                results = null;
            }
        };
    }

    //check connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //extract data from json
    private static String processResponse(JSONObject obj) {
        String origin = null, destination = null, distance = null, duration = null;
        JSONArray tmpObj;

        if (!obj.optString("status").toString().equals("OK"))
            return "Something wrong with what you entered!";

        try {
            tmpObj = new JSONArray(obj.getJSONArray("origin_addresses").toString());
            origin = tmpObj.getString(0);

            tmpObj = new JSONArray(obj.getJSONArray("destination_addresses").toString());
            destination = tmpObj.getString(0);

            tmpObj = obj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");
            distance = tmpObj.getJSONObject(0).getJSONObject("distance").getString("text");

            tmpObj = obj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");
            duration = tmpObj.getJSONObject(0).getJSONObject("duration").getString("text");
        } catch (Exception e) {
            System.out.println(e);
        }

        if (distance != null)
            return "From : " + origin + "\nTo :" + destination + "\n" + "Distance : " + distance + "\n" + "Approximate Time : " + duration;
        return "Something wrong with your input locations";
    }

    public static String findMyLocation()
    {
        final Location myLocation = new Location(LocationManager.NETWORK_PROVIDER);

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location)
            {
                myLocation.set(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        catch (SecurityException e)
        {
            System.out.println("Permission denied");
        }

        return myLocation.getLatitude()+myLocation.getLongitude()+"";
        //return "Maharagama";
    }

}
