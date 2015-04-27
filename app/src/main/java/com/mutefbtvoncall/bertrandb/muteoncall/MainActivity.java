package com.bertrandb.mutefbtvoncall;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity {

    String LOG_TAG = "FREEMUTEACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences data = getSharedPreferences("FreeMute",
                Context.MODE_PRIVATE);

        // activate checkbox
        CheckBox chk = (CheckBox) findViewById(R.id.checkBoxActivate);
        chk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isActivated = ((CheckBox) v).isChecked();
                SharedPreferences data = getSharedPreferences("FreeMute",
                        Context.MODE_PRIVATE);
                Editor editor = data.edit();
                editor.putBoolean("activated", isActivated);
                editor.apply();
            }
        });
        // set value
        chk.setChecked(data.getBoolean("activated", true));

        // Code Text field
        EditText textMessage = (EditText) findViewById(R.id.editTextCode);
        textMessage.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // Log.d(LOG_TAG,s.toString());
                SharedPreferences data = getSharedPreferences("FreeMute",
                        Context.MODE_PRIVATE);
                Editor editor = data.edit();
                editor.putString("code", s.toString());
                editor.apply();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });
        // set value
        textMessage.setText(data.getString("code", ""));

        // Mute mode radio box
        RadioGroup radioModeGroup = (RadioGroup) findViewById(R.id.radioModeGroup);
        radioModeGroup
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.radioRing: {
                                Log.d(LOG_TAG, "Option changed to Ring mode");
                                SharedPreferences data = getSharedPreferences(
                                        "FreeMute", Context.MODE_PRIVATE);
                                Editor editor = data.edit();
                                editor.putString("mode", "ring");
                                editor.apply();
                            }
                            break;
                            case R.id.radioOffHook: {
                                Log.d(LOG_TAG, "Option changed to Off Hook mode");
                                SharedPreferences data = getSharedPreferences(
                                        "FreeMute", Context.MODE_PRIVATE);
                                Editor editor = data.edit();
                                editor.putString("mode", "offhook");
                                editor.apply();
                            }
                            break;
                        }
                    }

                });
        // set values
        String mode = data.getString("mode", "offhook");
        if (mode.compareTo("ring")==0)
        {
            RadioButton button = (RadioButton) findViewById(R.id.radioRing);
            button.setChecked(true);
        }
        else if (mode.compareTo("offhook")==0)
        {
            RadioButton button = (RadioButton) findViewById(R.id.radioOffHook);
            button.setChecked(true);
        }
    }

}
