package com.bertrandb.mutefbtvoncall;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

public class FreeMutePhoneReceiver extends BroadcastReceiver {

	String LOG_TAG = "FREEMUTERECEIVER";

	protected boolean SwitchMuteFreebox(Context context) {
		SharedPreferences data = context.getSharedPreferences("FreeMute",
				Context.MODE_PRIVATE);
		// if (isPhoneCalling) {
		String code = data.getString("code", "");
		String url = "http://hd1.freebox.fr/pub/remote_control?code=" + code
				+ "&key=mute";
		try {
			InputStream content = null;
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(url));
			content = response.getEntity().getContent();
			StringBuilder sb = new StringBuilder();
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.d("LOG_TAG", sb.toString());
			return true;
		} catch (Exception e) {
			Log.d(LOG_TAG, "Network exception", e);
			return false;
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences data = context.getSharedPreferences("FreeMute",
				Context.MODE_PRIVATE);
		if (data.getBoolean("activated", true)) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				String state = extras.getString(TelephonyManager.EXTRA_STATE);

				if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
					String mode = data.getString("mode", "offhook");
					if (mode.compareTo("ring") == 0) {
						boolean mute = SwitchMuteFreebox(context);
						Editor editor = data.edit();
						editor.putBoolean("mute", mute);
						editor.apply();
					}
				}

				if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
					Log.i(LOG_TAG, "OFFHOOK");
					if (!data.getBoolean("mute", false)) {
						boolean mute = SwitchMuteFreebox(context);
						Editor editor = data.edit();
						editor.putBoolean("mute", mute);
						editor.apply();
					}
				}

				if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
					Log.i(LOG_TAG, "IDLE");
					// if (isPhoneCalling) {
					if (data.getBoolean("mute", false)) {
						SwitchMuteFreebox(context);
						Editor editor = data.edit();
						editor.putBoolean("mute", false);
						editor.apply();
					}
				}

			}
		}
	}

}
