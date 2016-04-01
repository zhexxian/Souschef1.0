package com.example.zhexian.souschef10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by zhexian on 3/24/2016.
 */
public class yourActivityrRunOnStartup extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//            Intent serviceIntent = new Intent(context, MainActivity.class);
//            context.startService(serviceIntent);
//        }

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){


            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }
    }
}
