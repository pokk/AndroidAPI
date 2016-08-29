package module.taiwan.no1.api.GPS;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import module.taiwan.no1.api.Log.AppLog;

/**
 * @author Jieyi Wu
 * @version 0.0.1
 * @since 8/29/16
 */

public class GPSTracker extends Service implements LocationListener
{
    // Tag.
    private static final String TAG = "GPSTracker";
    // The minimum distance to change Updates in meters.
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
    // The minimum time between updates in milliseconds.
    private static final long MIN_TIME_BW_UPDATES = 0; // 1 minute

    private final Context context;

    // Declaring a Location Manager.
    protected LocationManager locationManager;

    // Flag for GPS status.
    boolean isGPSEnabled = false;
    // Flag for network status.
    boolean isNetworkEnabled = false;
    // Flag for GPS status.
    boolean canGetLocation = false;

    private String bestProvider;
    private Location location; // location


    public GPSTracker(Context context)
    {
        this.context = context;

        this.getLocation();
        this.getLastBestLocation(0, 0);
    }

    public Location getLastBestLocation(int minDistance, long minTime)
    {
        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestTime = Long.MIN_VALUE;

        List<String> matchingProviders = locationManager.getAllProviders();
        for (String provider : matchingProviders)
        {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null)
            {
                AppLog.w(provider, location);
                float accuracy = location.getAccuracy();
                long time = location.getTime();

                if ((time > minTime && accuracy < bestAccuracy))
                {
                    bestResult = location;
                    bestAccuracy = accuracy;
                    bestTime = time;
                }
                else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime)
                {
                    bestResult = location;
                    bestTime = time;
                }
            }
        }

        return bestResult;
    }

    protected Location getLocation()
    {
        try
        {
            this.locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            this.isGPSEnabled = this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            this.isNetworkEnabled = this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!this.isGPSEnabled && !this.isNetworkEnabled)
            {
                // no network provider is enabled
            }
            else
            {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (this.isNetworkEnabled)
                {
                    Log.d(TAG, "Network is Enable.");
                    this.bestProvider = LocationManager.NETWORK_PROVIDER;
                }
                // if GPS Enabled get lat/long using GPS Services
                if (this.isGPSEnabled)
                {
                    Log.d(TAG, "GPS is Enable.");
                    this.bestProvider = LocationManager.GPS_PROVIDER;
                }

                Criteria criteria = new Criteria();
                //                this.bestProvider = this.locationManager.getBestProvider(criteria, true);
                Log.d(TAG, "getLocation best provider: " + this.bestProvider);
                this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                                            this);
                if (null != locationManager)
                {
                    this.location = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return this.location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopUsingGPS()
    {
        if (null != this.locationManager)
        {
            this.locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude()
    {
        if (null != this.location)
        {
            // return latitude
            return this.location.getLatitude();
        }

        return 0;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude()
    {
        if (null != this.location)
        {
            // return longitude
            return this.location.getLongitude();
        }
        return 0;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation()
    {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if (null != location)
        {
            this.location = location;
            Log.i(TAG, "onLocationChanged: " + this.location.getLatitude() + " - " + this.location.getLongitude());
            Toast.makeText(this.context, this.location.getLatitude() + " - " + this.location.getLongitude() + " -- " + this.location.getAccuracy(),
                           Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProviderDisabled(String provider)
    {
    }

    @Override
    public void onProviderEnabled(String provider)
    {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }
}
