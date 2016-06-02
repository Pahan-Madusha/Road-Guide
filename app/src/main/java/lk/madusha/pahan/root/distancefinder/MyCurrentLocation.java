package lk.madusha.pahan.root.distancefinder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

public class MyCurrentLocation implements LocationListener {

    public MyCurrentLocation(LocationManager locationManager,Context context) {

        if (ActivityCompat.checkSelfPermission(context , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        MainActivity.myLocation = (Location) locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    public void onLocationChanged(Location location)
    {
        MainActivity.myLocation = new Location(location);
    }
    public void onProviderDisabled(String arg0)
    {

    }
    public void onProviderEnabled(String provider)
    {

    }
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

}
