package edu.aku.hassannaqvi.typbar_tcv.core;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.aku.hassannaqvi.typbar_tcv.R;
import edu.aku.hassannaqvi.typbar_tcv.contracts.EnrollmentContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.FormsContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.MembersContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.MotherContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.SchoolContract;
import edu.aku.hassannaqvi.typbar_tcv.contracts.VersionAppContract;
import edu.aku.hassannaqvi.typbar_tcv.ui.EndingActivity;
import edu.aku.hassannaqvi.typbar_tcv.utils.DateUtils;

/**
 * Created by hassan.naqvi on 11/30/2016.
 */

public class MainApp extends Application {

    public static final String _IP = "43.245.131.159"; // Test PHP server
    public static final String _ALTERNATE_IP = "58.65.211.13"; // Test PHP server
    public static final Integer _PORT = 8080; // Port - with colon (:)
    public static final String _HOST_URL_1 = "http://" + MainApp._IP + ":" + MainApp._PORT + "/typbar/api/";
    public static final String _HOST_URL_2 = "http://" + MainApp._ALTERNATE_IP + ":" + MainApp._PORT + "/typbar/api/";
    public static final String _TEST_URL = "http://f49461:" + MainApp._PORT + "/typbar/api/";
    public static final String[] HOST = new String[]{_HOST_URL_1, _HOST_URL_2};
    public static final String _UPDATE_URL = "http://" + MainApp._IP + ":" + MainApp._PORT + "/typbar/app/";

    public static String DATABASE_NAME = "typbar_tcv";
    public static final Integer MONTHS_LIMIT = 11;
    public static final Integer DAYS_LIMIT = 29;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
    private static final int TWENTY_MINUTES = 1000 * 60 * 20;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final long MILLIS_IN_SECOND = 1000;
    private static final long SECONDS_IN_MINUTE = 60;
    private static final long MINUTES_IN_HOUR = 60;
    private static final long HOURS_IN_DAY = 24;
    private static final long HOURS_IN_2DAYS = 24 * 2;
    public static final long MILLISECONDS_IN_DAY = MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY;
    private static final long DAYS_IN_YEAR = 365;
    public static final long MILLISECONDS_IN_YEAR = MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_YEAR;
    private static final long DAYS_IN_5_YEAR = 365 * 5;
    public static final long MILLISECONDS_IN_5Years = MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_5_YEAR;
    private static final long DAYS_IN_10_YEAR = 365 * 10;
    public static final long MILLISECONDS_IN_10Years = MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_10_YEAR;
    private static final long DAYS_IN_MONTH = 30;
    public static final long MILLISECONDS_IN_MONTH = MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_MONTH;
    private static final long DAYS_IN_6_MONTHS = 30 * 6;
    public static final long MILLISECONDS_IN_6_MONTH = MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_6_MONTHS;

    private static final long DAYS_IN_2_YEAR = 365 * 2;
    public static final long MILLISECONDS_IN_2Years = MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_2_YEAR;
    //public static final long MILLISECONDS_IN_100_YEAR = MILLISECONDS_IN_YEAR * 100;
    public static String deviceId;

    public static final long MILLISECONDS_IN_2DAYS = MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_2DAYS;

    public static Boolean admin = false;
    public static FormsContract fc;
    public static MotherContract mc;
    public static MembersContract cc;
    public static SchoolContract sc;
    public static EnrollmentContract ec;
    public static String userName = "0000";
    public static long versionCode;
    public static int totalChild = 0;
    public static String versionName;
    public static String teamNo;
    public static Integer areaCode;
    protected static LocationManager locationManager;

    public static String[] schTypes = new String[]{"....",
            "Government Boys/Girls Primary School",
            "Government Boys/Girls Secondary School", "Private", "Madarasa", "Other"};

    public static String[] schClasses = new String[]{"....",
            "Playgroup", "KG-I", "KG-II", "Montessori-J", "Montessori-S", "Class-I", "Class-II",
            "Class-III", "Class-IV", "Class-V", "Class-VI", "Class-VII", "Class-VIII",
            "Class-IX", "Class-X", "Other"
    };

    public static final String childListing = "SECRET";
    public static final String massImunization = "SECRETMI";
    public static final String casecontrol = "SECRETCC";
    public static final String CHILDLISTINGTYPE = "cl";
    public static final String SCHOOLLISTINGTYPE = "sl";
    public static final String MASSIMMUNIZATIONTYPE = "mi";
    public static final String VACCINECOVERAGE = "vc";
    public static final String CRFCase = "sca";
    public static final String CRFCaseEnroll = "sca_enroll";
    public static final String CRFControl = "scl";
    public static final String CRFControlEnroll = "scl_enroll";
    public static final String HF = "hfType";
    public static final String CAMPHF = "chf";
    public static final String SCHOOLHF = "shf";

    public static final String CASESCR = "cas";
    public static final String CASEID = "caid";
    public static final String CONTROLSCR = "cls";
    public static final String CONTROLID = "clid";
    public static final String VCID = "vcid";

    public static int monthsBetweenDates(Date startDate, Date endDate) {

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        int monthsBetween = 0;
        int dateDiff = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);

        if (dateDiff < 0) {
            int borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateDiff = (end.get(Calendar.DAY_OF_MONTH) + borrrow) - start.get(Calendar.DAY_OF_MONTH);
            monthsBetween--;

            if (dateDiff > 0) {
                monthsBetween++;
            }
        }

        monthsBetween += end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        monthsBetween += (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
        return monthsBetween;
    }

    public static long ageInMonths(String year, String month) {
        long ageInMonths = (Integer.valueOf(year) * 12) + Integer.valueOf(month);
        return ageInMonths;
    }

    public static void errorCheck(final Context context, String error) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder
                .setMessage(error)
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public static void errorCountDialog(final Context context, final Activity activity, String error) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder
                .setMessage(error)
                .setCancelable(false)
                .setPositiveButton("Discard",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
//                                MainApp.memFlag--;
                                activity.finish();
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public static void savingAppVersion(Context context, JSONArray array) {

        JSONObject object = null;
        try {
            object = array.getJSONObject(0);
            VersionAppContract contract = new VersionAppContract();
            contract.Sync(object);
            String json = new Gson().toJson(contract);
            context.getSharedPreferences("main", MODE_PRIVATE).edit().putString("appVersion", json).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public static void endActivity(final Context context, final Activity activity, final boolean flag) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder
                .setMessage("Do you want to End Activity??")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                activity.finish();
                                Intent end_intent = new Intent(context, EndingActivity.class);
                                end_intent.putExtra("complete", flag);
                                context.startActivity(end_intent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public static boolean checkingGPSRules(Context mContext) {

        MainApp.LocClass locClass = MainApp.setGPS(mContext);

        if (locClass.getTime().equals("0") || locClass.getAccuracy().equals("0")) return false;

        Long minutes = DateUtils.getMinutes(DateUtils.getDateFormat(locClass.getTime()), DateUtils.getDateFormat(
                new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date())));

        Toast.makeText(mContext, "Date Time: " + locClass.getTime()
                        + "\nMinutes: " + minutes
                        + "\nAccuracy: " + locClass.getAccuracy()
                , Toast.LENGTH_LONG).show();

        boolean flag = minutes.intValue() < 20 && Double.valueOf(locClass.getAccuracy()).intValue() < 15;
        if (flag) return true;

        new AlertDialog.Builder(mContext)
                .setTitle("GPS WARNING")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setCancelable(false)
                .setMessage("Please show sky to device and then re-start app")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();

        return false;

    }

    public static LocClass setGPS(Context mContext) {

        SharedPreferences GPSPref = mContext.getSharedPreferences("GPSCoordinates", Context.MODE_PRIVATE);
        try {
            String lat = GPSPref.getString("Latitude", "0");
            String lang = GPSPref.getString("Longitude", "0");
            String acc = GPSPref.getString("Accuracy", "0");

            if (lat == "0" && lang == "0") {
                Toast.makeText(mContext, "Could not obtained GPS points", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(mContext, "GPS set", Toast.LENGTH_SHORT).show();
            }

            String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", Long.parseLong(GPSPref.getString("Time", "0"))).toString();

            return new LocClass(lang, lat, acc, date);

        } catch (Exception e) {
            Log.e("SET GPS ERROR", e.getMessage());
        }

        return new LocClass();

    }

    public static Calendar getCalendarDate(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = sdf.parse(value);
            calendar.setTime(date);
            return calendar;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/JameelNooriNastaleeq.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf

        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        // Requires Permission for GPS -- android.permission.ACCESS_FINE_LOCATION
        // Requires Additional permission for 5.0 -- android.hardware.location.gps
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new GPSLocationListener() // Implement this class from code

        );


//        Initialize Dead Member List
//        deadMembers = new ArrayList<String>();

    }

    protected void showCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            String message = String.format(
                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            //Toast.makeText(getApplicationContext(), message,
            //Toast.LENGTH_SHORT).show();
        }

    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;

            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else return isNewer && !isSignificantlyLessAccurate && isFromSameProvider;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public class GPSLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {

            SharedPreferences sharedPref = getSharedPreferences("GPSCoordinates", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            String dt = DateFormat.format("dd-MM-yyyy HH:mm", Long.parseLong(sharedPref.getString("Time", "0"))).toString();

            Location bestLocation = new Location("storedProvider");
            bestLocation.setAccuracy(Float.parseFloat(sharedPref.getString("Accuracy", "0")));
            bestLocation.setTime(Long.parseLong(sharedPref.getString(dt, "0")));
            bestLocation.setLatitude(Float.parseFloat(sharedPref.getString("Latitude", "0")));
            bestLocation.setLongitude(Float.parseFloat(sharedPref.getString("Longitude", "0")));

            if (isBetterLocation(location, bestLocation)) {
                if (Double.valueOf(location.getAccuracy()).intValue() < 15) {
                    editor.putString("Longitude", String.valueOf(location.getLongitude()));
                    editor.putString("Latitude", String.valueOf(location.getLatitude()));
                    editor.putString("Accuracy", String.valueOf(location.getAccuracy()));
                    editor.putString("Time", String.valueOf(location.getTime()));
                    editor.apply();
                }
            }

            String date = DateFormat.format("dd-MM-yyyy HH:mm", Long.parseLong(String.valueOf(location.getTime()))).toString();
            /*Toast.makeText(getApplicationContext(),
                    "GPS Commit! LAT: " + String.valueOf(location.getLongitude()) +
                            " LNG: " + String.valueOf(location.getLatitude()) +
                            " Accuracy: " + String.valueOf(location.getAccuracy()) +
                            " Time: " + date,
                    Toast.LENGTH_SHORT).show();*/
        }


        public void onStatusChanged(String s, int i, Bundle b) {
            showCurrentLocation();
        }

        public void onProviderDisabled(String s) {

        }

        public void onProviderEnabled(String s) {

        }
    }

    public static class LocClass {
        String Longitude, Latitude, Accuracy, Time;

        public LocClass(String longitude, String latitude, String accuracy, String time) {
            Longitude = longitude;
            Latitude = latitude;
            Accuracy = accuracy;
            Time = time;
        }

        public LocClass() {
        }

        public String getLongitude() {
            return Longitude;
        }

        public String getLatitude() {
            return Latitude;
        }

        public String getAccuracy() {
            return Accuracy;
        }

        public String getTime() {
            return Time;
        }
    }

}