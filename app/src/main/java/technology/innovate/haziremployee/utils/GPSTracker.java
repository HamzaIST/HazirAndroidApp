package technology.innovate.haziremployee.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPSTracker extends Service implements LocationListener {
    private final Context mContext;
    //flag for GPS Status
    public boolean isGPSEnabled = false;
    //flag for network status
    public boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double latitude = 0;
    double longitude = 0;
    //The minimum distance to change updates in metters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 300; //10 metters
    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 3; // 1 minute
    //Declaring a Location Manager
    protected LocationManager locationManager;

    private String provider;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.M)
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            //getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                showSettingsAlert();
            } else {
                this.canGetLocation = true;


                if (location == null) {
                    if (isGPSEnabled) {

                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        //Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            updateGPSCoordinates();
                        }

                    }
                }

//First get location from Network Provider
                if (isNetworkEnabled) {
                    //if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    //Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        updateGPSCoordinates();
                    }

                }

                //if GPS Enabled get lat/long using GPS Services



                if (location == null) {

                    Criteria criteria = new Criteria();

                    provider = locationManager.getBestProvider(criteria, false);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return null;
                    }
                    Location locationN = locationManager.getLastKnownLocation(provider);

                    if (locationN != null) {
                        location = locationN;
                        updateGPSCoordinates();
                    }
                }


            }
        } catch (Exception e) {
            //e.printStackTrace();
            //Log.e("Error : Location", "Impossible to connect to LocationManager", e);
        }
        return location;
    }

    public void updateGPSCoordinates() {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */

    @TargetApi(Build.VERSION_CODES.M)
    public void stopUsingGPS() {
        if (locationManager != null) {

            locationManager.removeUpdates(GPSTracker.this);

        }
    }

    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Mesaj basligi
        alertDialog.setTitle("");

        // Mesaj
        alertDialog.setMessage("Turn On Location Services to Allow 'Volts' to Determine Your Location");

        // Mesaj ikonu
        //alertDialog.setIcon(R.drawable.delete);

        // Ayarlar butonuna tiklandiginda
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
                dialog.dismiss();
            }
        });

        // Iptal butonuna tiklandiginda
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        // Mesaj kutusunu goster
        alertDialog.show();
    }

    /**
     * Function to get latitude
     /**/
    public double getLatitude()
    {
        if (location != null)
        {
            latitude = location.getLatitude();
        }
        return latitude;
    }
    /**
     * Function to get longitude
     */
    public double getLongitude()
    {
        if (location != null)
        {
            longitude = location.getLongitude();
        }
        return longitude;
    }
    /**
     * Function to check GPS/wifi enabled
     */
    public boolean canGetLocation()
    {
        return this.canGetLocation;
    }
    /**
     * Function to show settings alert dialog
     */



    /**
     * Get list of address by latitude and longitude
     * @return null or List<Address>
     */
    public List<Address> getGeocoderAddress(Context context)
    {
        if (location != null)
        {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try
            {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                return addresses;
            }
            catch (IOException e)
            {
                //e.printStackTrace();
                //Log.e("Error : Geocoder", "Impossible to connect to Geocoder", e);
            }
        }
        return null;
    }

    public String getAddress(LatLng currentLoc) {
        try{
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(currentLoc.latitude, currentLoc.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            return addresses.get(0).getAddressLine(0);
        }catch (Exception e){
//            return e.toString();
        }
        return "";
    }

    /**
     * Try to get AddressLine
     * @return null or addressLine
     */
    public String getAddressLine(Context context)
    {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0)
        {   String addressLine =null;
            for(int i=0;i<addresses.size();i++) {
                Address address = addresses.get(0);
                addressLine = address.getAddressLine(0);
                if (!TextUtils.isEmpty(addressLine)){
                    break;
                }
            }
            return addressLine;
        }
        else
        {
            return null;
        }
    }
    /**
     * Try to get Locality
     * @return null or locality
     */
    public String getLocality(Context context)
    {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0)
        {
            String locality =null;
            for(int i=0;i<addresses.size();i++) {
                Address address = addresses.get(0);
                locality = address.getLocality();
                if (!TextUtils.isEmpty(locality)){
                    break;
                }
            }
            return locality;
        }
        else
        {
            return null;
        }
    }

    public String getFeatureName(Context context)
    {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0)
        {
            String featureName =null;
            for(int i=0;i<addresses.size();i++) {
                Address address = addresses.get(0);
                featureName = address.getFeatureName();
                if (!TextUtils.isEmpty(featureName)){
                    break;
                }
            }
            return featureName;
        }
        else
        {
            return null;
        }
    }
    /**
     * Try to get Postal Code
     * @return null or postalCode
     */
    public String getPostalCode(Context context)
    {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0)
        {
            String postalCode =null;
            for(int i=0;i<addresses.size();i++) {
                Address address = addresses.get(0);
                postalCode = address.getPostalCode();
                if (!TextUtils.isEmpty(postalCode)){
                    break;
                }
            }
            return postalCode;
        }
        else
        {
            return null;
        }
    }

    public String getSubLocality(Context context)
    {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0)
        {
            String postalCode =null;
            for(int i=0;i<addresses.size();i++) {
                Address address = addresses.get(0);
                postalCode = address.getSubLocality();
                if (!TextUtils.isEmpty(postalCode)){
                    break;
                }
            }
            return postalCode;
        }
        else
        {
            return null;
        }
    }

    public String getState(Context context)
    {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0)
        {
            String postalCode =null;
            for(int i=0;i<addresses.size();i++) {
                Address address = addresses.get(0);
                postalCode = address.getAdminArea();
                if (!TextUtils.isEmpty(postalCode)){
                    break;
                }
            }
            return postalCode;
        }
        else
        {
            return null;
        }
    }
    /**
     * Try to get CountryName
     * @return null or postalCode
     */
    public String getCountryName(Context context)
    {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0)
        {
            String countryName =null;
            for(int i=0;i<addresses.size();i++) {
                Address address = addresses.get(0);
                countryName = address.getCountryName();
                if (!TextUtils.isEmpty(countryName)){
                    break;
                }
            }
            return countryName;
        }
        else
        {
            return null;
        }
    }

    /**
     * GPSTracker isGPSTrackingEnabled getter.
     * Check GPS/wifi is enabled
     */
    public boolean getIsGPSTrackingEnabled() {

        return this.isGPSEnabled;
    }

    @Override
    public void onLocationChanged(Location location)
    {


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }




}
