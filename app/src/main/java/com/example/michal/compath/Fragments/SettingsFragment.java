package com.example.michal.compath.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.michal.compath.R;

public class SettingsFragment extends Fragment{

    private View view;

    private Button butSecRefreshPositive;
    private Button butSecRefreshNegative;
    private Button butMetRefreshPositive;
    private Button butMetRefreshNegative;
    private Switch themeSwitch;
    private Switch voiceCommandSwitch;
    private Button saveButton;
    private Button restoreButton;
    private EditText metersText;
    private EditText secondsText;
    private SharedPreferences preferences;
    private boolean themeDark;
    private boolean speachOn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.activity_settings_fragment, container, false);

        /* --------- buttons and switches initialization part --------- */

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        butSecRefreshPositive = (Button) view.findViewById(R.id.ref_sec_positive_button);
        butSecRefreshNegative = (Button) view.findViewById(R.id.ref_sec_negative_button);
        butMetRefreshPositive = (Button) view.findViewById(R.id.ref_meters_positive_but);
        butMetRefreshNegative = (Button) view.findViewById(R.id.ref_meters_negative_but);
        themeSwitch = (Switch) view.findViewById(R.id.theme_switch);

        voiceCommandSwitch = (Switch) view.findViewById(R.id.voice_command_switch);
        saveButton = (Button) view.findViewById(R.id.save_sett_but);
        restoreButton = (Button) view.findViewById(R.id.restore_def_but);
        metersText = (EditText) view.findViewById(R.id.ref_meters_text_edit );
        secondsText = (EditText) view.findViewById(R.id.ref_sec_text_edit);

        secondsText.setText(preferences.getInt("seconds_refresh_time",R.string.default_refresh_seconds) + "");
        metersText.setText(preferences.getInt("meters_refresh_distance",R.string.default_refresh_meters) + "");

        themeSwitch.setChecked(preferences.getBoolean("theme_dark", true));
        voiceCommandSwitch.setChecked(preferences.getBoolean("voice_commands", false));


        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    themeDark = true;
                else
                    themeDark = false;
            }
        });

        voiceCommandSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    speachOn = true;
                else
                    speachOn = false;
            }
        });



        /* --------- main settings functions --------- */

        butSecRefreshPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer newValue = Integer.parseInt(secondsText.getText().toString());
                newValue += 1;
                secondsText.setText(newValue + "");
            }
        });

        butSecRefreshNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer newValue = Integer.parseInt(secondsText.getText().toString());
                newValue -= 1;
                secondsText.setText(newValue + "");
            }
        });

        butMetRefreshPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer newValue = Integer.parseInt(metersText.getText().toString());
                newValue += 1;
                metersText.setText(newValue + "");
            }
        });

        butMetRefreshNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer newValue = Integer.parseInt(metersText.getText().toString());
                newValue -= 1;
                metersText.setText(newValue + "");
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer seconds = Integer.parseInt(secondsText.getText().toString());
                Integer meters = Integer.parseInt(metersText.getText().toString());

                preferences.edit().putInt("seconds_refresh_time", seconds)
                        .putInt("meters_refresh_distance", meters).commit();
                preferences.edit().putBoolean("theme_dark", themeDark)
                        .putBoolean("voice_commands", speachOn).commit();
                Toast.makeText(view.getContext(), "Zapisano", Toast.LENGTH_SHORT).show();
            }


        });

        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                metersText.setText(R.string.default_refresh_meters);
                secondsText.setText(R.string.default_refresh_seconds);
                themeSwitch.setChecked(true);
                voiceCommandSwitch.setChecked(false);
            }
        });

    return view;
    }
}
