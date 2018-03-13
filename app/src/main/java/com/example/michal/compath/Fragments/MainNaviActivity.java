package com.example.michal.compath.Fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.michal.compath.Database.LocationsDatabase;
import com.example.michal.compath.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainNaviActivity extends Fragment implements LocationListener {

    private View view;
    private Activity activity;

    // location variables
    private Criteria criteria;
    private Location currentLocation;
    private Location newTargetLoation;
    private String provider;
    private LocationManager locationManager;
    private SharedPreferences preferences;
    private boolean useAsCompass = true;

    // UI elements variables
    private Button addToFavorite;
    private Button startPauseButton;
    private UIUpdater uiUpdater;
    private FloatingActionButton fab;
    ImageView arrow;

    // Sensor orientation
    private DeviceOrientation deviceOrientation;
    private boolean flagExecute = true;
    private boolean flagGPSNaviOn = false;
    private float angleBetweenLocations = 0.0f;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.activity_main_navi, container, false);
        activity = getActivity();
        addToFavorite = (Button) view.findViewById(R.id.favorite_in_navi_button);
        startPauseButton = (Button) view.findViewById(R.id.start_pause_in_navi_button);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        arrow = (ImageView) view.findViewById(R.id.arrow);

        uiUpdater = new UIUpdater(view);


        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagGPSNaviOn) {
                    if (uiUpdater.isCompasBitmap()) {
                        uiUpdater.setUpNavigationArrow();
                        uiUpdater.showTextInfo();
                        useAsCompass = false;
                    } else {
                        uiUpdater.setUpCompassArrow();
                        uiUpdater.hideTextInfo();
                        useAsCompass = true;
                    }
                }
            }
        });



        deviceOrientation = new DeviceOrientation(getActivity()) {
            @Override
            void onAngleChanged(float angle) {
                if(flagExecute)
                    if(useAsCompass)
                        uiUpdater.rotateArrow(-angle);
                    else {
                        uiUpdater.rotateArrow(-angle + angleBetweenLocations);
                        uiUpdater.setDirection((double) (angle));
                    }



            }
        };
        deviceOrientation.registerListeners();


        addToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createConfirmAddToFavoriteDialog().show();
            }
        });


        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOrPauseNavigation();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTargetDialog(view.getContext()).show();
            }
        });


        if(LocationsDatabase.getInstance().getNewLocationFromFavorite() != null) {
            newTargetLoation = LocationsDatabase.getInstance().getNewLocationFromFavorite();
            LocationsDatabase.getInstance().setFromFavoriteLocation(null);
            setUpLocation();
            startNewNavigation(String.valueOf(newTargetLoation.getLatitude()), String.valueOf(newTargetLoation.getLongitude()));
        }

        return view;
    }



    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        setUpLocation();

    }


    @Override
    public void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(
                provider, preferences.getInt("seconds_refresh_time",R.string.default_refresh_seconds),
                preferences.getInt("meters_refresh_distance",R.string.default_refresh_meters),
                this
        );

        setUpLocation();
    }


    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);

    }


    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;

        if(currentLocation != null && newTargetLoation != null && !uiUpdater.isCompasBitmap()) {
            uiUpdater.setDistance(currentLocation.distanceTo(newTargetLoation));
            uiUpdater.setSpeed(currentLocation.getSpeed());
            angleBetweenLocations = getAngleBetwenLocations(newTargetLoation, location);
        }
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


    private Dialog createConfirmAddToFavoriteDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Dodaj").setMessage(
                "Dodać aktualną lokalizację do ulubionyh?"
        ).setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addCurrentLocationToFavorite();
            }
        }).setNegativeButton("Anuluj", null);
        return dialogBuilder.create();
    }


    private Dialog createNewTargetDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.new_target_dialog);
        dialog.setTitle("Nawiguj do..");

        final EditText latitude = (EditText) dialog.findViewById(R.id.latitude_target_text_edit_dialog);
        final EditText longitude = (EditText) dialog.findViewById(R.id.longitude_target_text_edit_dialog);
        Button cancelBut = (Button) dialog.findViewById(R.id.cancel_button_dialog);
        Button confirmBut = (Button) dialog.findViewById(R.id.start_navigation_dialog);

        confirmBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String tmp = latitude.getText().toString();
                    tmp = longitude.getText().toString();

                    startNewNavigation(latitude.getText().toString(), longitude.getText().toString());

                    dialog.dismiss();
                }catch(Exception e) {
                    Log.e(e.getLocalizedMessage(), e.getMessage());
                }

            }
        });

        cancelBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }


    void startNewNavigation(String latitude, String longitude) {
        newTargetLoation = new Location(LocationManager.GPS_PROVIDER);
         double latitudeDouble = Double.parseDouble(latitude);
        double longitudeDouble = Double.parseDouble(longitude);
         newTargetLoation.setLatitude(latitudeDouble);
        newTargetLoation.setLongitude(longitudeDouble);

//        newTargetLoation.setLatitude(51.223302);
//        newTargetLoation.setLongitude(22.575340);
        if (newTargetLoation != null && currentLocation != null) {
            LocationsDatabase.getInstance().pushHistoryLocation(currentLocation, newTargetLoation);
            angleBetweenLocations = getAngleBetwenLocations(newTargetLoation, currentLocation);
            flagGPSNaviOn = true;
            uiUpdater.setUpNavigationArrow();
            uiUpdater.setDistance(currentLocation.distanceTo(newTargetLoation));
            uiUpdater.setSpeed(currentLocation.getSpeed());
            uiUpdater.showTextInfo();
            useAsCompass = false;

        } else {
            createNoCurrentLocationWarn().show();
        }
    }


    private void addCurrentLocationToFavorite() {
        SimpleDateFormat sdf = new SimpleDateFormat("  dd - MM - yyyy§");
        String currentDateandTime = sdf.format(new Date());

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null)
            LocationsDatabase.getInstance().pushFavoriteLocaion("Punkt z dnia " + currentDateandTime, location.getLatitude(), location.getLongitude());
        else
            createNoCurrentLocationWarn().show();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startOrPauseNavigation() {
        if(flagExecute) {
            flagExecute = false;
            startPauseButton.setBackground(getContext().getDrawable(R.drawable.ic_play_circle_filled_white_24dp));
            deviceOrientation.unregisterListeners();
        }
        else {
            flagExecute = true;
            startPauseButton.setBackground(getContext().getDrawable(R.drawable.ic_pause_circle_filled_white_48dp));
            deviceOrientation.registerListeners();
        }
    }


    private float getAngleBetwenLocations(Location l1, Location l2) {
        double x1, x2, y1, y2;
        x1 = l1.getLatitude();
        x2 = l2.getLatitude();
        y1 = l1.getLongitude();
        y2 = l2.getLongitude();

        return (float) (Math.atan2(y1 - y2, x1 - x2) * 180 / Math.PI);
    }


    private void setUpLocation() {
        criteria = new Criteria();
        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        provider = locationManager.getBestProvider(criteria, true);
        currentLocation = locationManager.getLastKnownLocation(provider);
    }

    private Dialog createNoCurrentLocationWarn() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Brak lokalizacji").setMessage(
                "Brak dostępu do lokalizacji. Włącz lokalizację lub spróbuj ponownie za chwilę."
        ).setPositiveButton("Zamknij", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return dialogBuilder.create();
    }


}


class UIUpdater {

    private View view;
    private TextView directionText;
    private TextView speedText;
    private TextView distanceToTargetText;
    private ImageView arrow;
    private float lastArrowPositionAngle = 0;
    Drawable arrowGreen;
    Drawable arrowCompass;
    Drawable buttonCompass;
    Drawable buttonNavi;
    Drawable buttonStart;
    Drawable buttonStop;
    boolean flagArrowCompas = true;

    private String speed = "", direction = "", distance = "";
    private String cleanText = "";



    public UIUpdater(View v) {
        view  = v;
        directionText = (TextView) view.findViewById(R.id.direction_text_view);
        speedText = (TextView) view.findViewById(R.id.speed_text_view);
        distanceToTargetText = (TextView) view.findViewById(R.id.distance_text_view);
        arrow = (ImageView) view.findViewById(R.id.arrow);

        // --- getting UI elements from drawable
        arrowGreen = view.getContext().getDrawable(R.drawable.arrow);
        arrowCompass = view.getContext().getDrawable(R.drawable.compass_arrow);
        buttonCompass = view.getContext().getDrawable(R.drawable.ic_explore_white_48dp);
        buttonNavi = view.getContext().getDrawable(R.drawable.ic_navigation_white_48dp);
        buttonStart = view.getContext().getDrawable(R.drawable.ic_play_circle_filled_white_24dp);
        buttonStop = view.getContext().getDrawable(R.drawable.ic_pause_circle_filled_white_48dp);

    }

    public void rotateArrow(float degree) {
        final RotateAnimation rotateAnim = new RotateAnimation(lastArrowPositionAngle, degree, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);


        rotateAnim.setDuration((long) (Math.abs(degree*3)));
        rotateAnim.setFillAfter(true);
        arrow.startAnimation(rotateAnim);
        lastArrowPositionAngle = degree;
    }


    private String getDirectionInText(double bearing) {

        int range = (int) (bearing / (360f / 16f));
        String dirTxt = "Idziesz na ";

        if (range == 15 || range == 0)
            dirTxt += "północ";
        if (range == 1 || range == 2)
            dirTxt += "północny wschód";
        if (range == 3 || range == 4)
            dirTxt += "wschód";
        if (range == 5 || range == 6)
            dirTxt += "południowy wschód";
        if (range == 7 || range == 8)
            dirTxt += "południe";
        if (range == 9 || range == 10)
            dirTxt += "połudionwy zachód";
        if (range == 11 || range == 12)
            dirTxt += "zachód";
        if (range == 13 || range == 14)
            dirTxt += "północny zachód";

        return dirTxt;
    }



    public boolean isCompasBitmap() {
        return flagArrowCompas;
    }

    public void setUpCompassArrow() {
        arrow.setImageDrawable(arrowCompass);
        flagArrowCompas = true;
    }

    public void setUpNavigationArrow() {
        arrow.setImageDrawable(arrowGreen);
        flagArrowCompas = false;
    }

    public void setDirection(Double bearing) {
        direction = getDirectionInText(bearing);
        directionText.setText(direction);
    }

    public void setSpeed(float speed) {
        this.speed = speed + " km/h";
        speedText.setText(this.speed);
    }

    public void setDistance(double distance) {
        if(distance < 1000)
            this.distance = Math.round(distance) + " m";
        else
            this.distance = Math.round(distance / 1000) + " km";
        distanceToTargetText.setText(this.distance);
    }

    public void hideTextInfo() {
        directionText.setText(cleanText);
        speedText.setText(cleanText);
        distanceToTargetText.setText(cleanText);
    }

    public void showTextInfo() {
        directionText.setText(direction);
        speedText.setText(speed);
        distanceToTargetText.setText(distance);
    }

}


abstract class DeviceOrientation implements SensorEventListener{

    private Activity activity;
    private static final int sampleSize = 1;

    private float correction = 0f;
    private SensorManager sensorManager;
    private Sensor sensorOrientation;
    private float table[];
    private int index = 0;


    public DeviceOrientation(Activity a) {
        activity = a;
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sensorOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        table = new float[sampleSize];
    }

    private void addToTable(float degree) {
        if(index<sampleSize)
            table[index++] = degree;
        else {
            index = 0;
            onAngleChanged(getAverageTable());
        }
    }


    private float getAverageTable() {
        long average = 0;
        for(int i=0; i<sampleSize; i++) {
            average += table[i];
        }
        average = average / sampleSize;
        return average;
    }


    public void registerListeners() {
        sensorManager.registerListener(this, sensorOrientation, SensorManager.SENSOR_DELAY_UI);
    }

    public void unregisterListeners() {
        sensorManager.unregisterListener(this, sensorOrientation);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
       // addToTable(event.values[0]);
        onAngleChanged(event.values[0] + correction);
    }


    abstract void onAngleChanged(float angle);


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}