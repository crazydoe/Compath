package com.example.michal.compath;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.activeandroid.ActiveAndroid;
import com.example.michal.compath.Fragments.FavoriteFragment;
import com.example.michal.compath.Fragments.HistoryFragment;
import com.example.michal.compath.Fragments.MainNaviActivity;
import com.example.michal.compath.Fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    android.support.v4.app.FragmentTransaction fragmentTransaction;

    MainNaviActivity mainNaviActivity;
    FavoriteFragment favoriteFragment;
    Button buttonBackDrawer;
    Toolbar toolbar;
    View.OnClickListener backOnClickListener;
    View.OnClickListener drawerOnClickListener;




    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActiveAndroid.initialize(this);

        setContentView(R.layout.activity_main);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        buttonBackDrawer = (Button) findViewById(R.id.back_or_drawer_button);




        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);


        backOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_frame, new MainNaviActivity());
                fragmentTransaction.commit();
                findViewById(R.id.back_or_drawer_button).setBackground(getDrawable(R.drawable.ic_menu_white_48dp));
                findViewById(R.id.back_or_drawer_button).setOnClickListener(drawerOnClickListener);
            }
        };

        drawerOnClickListener =  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert drawer != null;
                drawer.openDrawer(GravityCompat.START);
            }
        };


        findViewById(R.id.back_or_drawer_button).setBackground(getDrawable(R.drawable.ic_menu_white_48dp));
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_frame, new MainNaviActivity());
        fragmentTransaction.commit();
        buttonBackDrawer.setOnClickListener(drawerOnClickListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_layout, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_navigation) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_frame,new MainNaviActivity());
            fragmentTransaction.commit();
            findViewById(R.id.back_or_drawer_button).setBackground(getDrawable(R.drawable.ic_menu_white_48dp));
            buttonBackDrawer.setOnClickListener(drawerOnClickListener);

        } else if (id == R.id.nav_actual_coor) {

            createCoordinatesDialog().show();

        } else if (id == R.id.nav_favorite) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_frame, new FavoriteFragment());
            fragmentTransaction.commit();
            findViewById(R.id.back_or_drawer_button).setBackground(getDrawable(R.drawable.ic_arrow_back_white_48dp));
            buttonBackDrawer.setOnClickListener(backOnClickListener);


        } else if (id == R.id.nav_history) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_frame, new HistoryFragment());
            fragmentTransaction.commit();
            findViewById(R.id.back_or_drawer_button).setBackground(getDrawable(R.drawable.ic_arrow_back_white_48dp));
            buttonBackDrawer.setOnClickListener(backOnClickListener);



        } else if (id == R.id.nav_quit) {
            createQuitDialog().show();

        } else if (id == R.id.nav_settings) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_frame, new SettingsFragment());
            fragmentTransaction.commit();
            findViewById(R.id.back_or_drawer_button).setBackground(getDrawable(R.drawable.ic_arrow_back_white_48dp));
            buttonBackDrawer.setOnClickListener(backOnClickListener);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private Dialog createCoordinatesDialog() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(current != null) {

            double latitude  = current.getLatitude();
            double longitude = current.getLongitude();
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Aktualne koordynaty").setMessage(
                    "Szerokość: " + latitude + "\n\n" + "Długość: " + longitude
            ).setPositiveButton("Zamknij", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ;
                }
            });
            return dialogBuilder.create();

        }else {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Aktualne koordynaty").setMessage(
                    "Brak dostępu do lokalizacji. Włącz lokalizację lub spróbuj ponownie za chwilę."
            ).setPositiveButton("Zamknij", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ;
                }
            });
            return dialogBuilder.create();

        }




    }

    private Dialog createQuitDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Zamykanie nawigacji").setMessage(
                "Napewno zamknąć?"
        ).setPositiveButton("Zamknij", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());

            }
        }).setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ;
            }
        });
        return dialogBuilder.create();
    }



}



