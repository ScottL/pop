package com.schen.pop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class CustomReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW))PopActivity.PAUSE_FROM_BROADCAST_RECEIVER_ONLY();
	}
}
