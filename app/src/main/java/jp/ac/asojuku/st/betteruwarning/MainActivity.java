package jp.ac.asojuku.st.betteruwarning;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private MyBroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int scale = intent.getIntExtra("scale", 0);
                int level = intent.getIntExtra("level", 0);
                int status = intent.getIntExtra("level", 0);
                String statusString = "";
                switch (status) {
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        statusString = "unknow";
                        break;
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        statusString = "charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        statusString = "discharging";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        statusString = "not charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        statusString = "full";
                        break;
                }

                final String message = "ステータスは"+statusString + " 残量は" + level + "/" + scale;

                //Log.v("Battery Watch", "" + hour + ":" + minute + "" + second + "" + statusString + "" + level + "/" + scale);

                Activity mainActivity = (Activity) context;
                TextView tvmsg = (TextView) mainActivity.findViewById(R.id.buttry_text);
                tvmsg.setText(message);

                if(level<=15){

                    Intent receiverdIntent = new Intent();
                    int notificationId = receiverdIntent.getIntExtra("notificationId",0);
                    
                            NotificationManager myNotification = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

                            Intent boolIntent  = new Intent(context,MainActivity.class);
                            PendingIntent contentIntent = PendingIntent.getActivity(context,0,boolIntent,0);
                            Notification.Builder builder = new Notification.Builder(context);
                            builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("15%以下です")
                                    .setWhen(System.currentTimeMillis())
                                    .setPriority(Notification.PRIORITY_DEFAULT)
                                    .setAutoCancel(true)
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setContentIntent(contentIntent);

                            myNotification.notify(notificationId,builder.build());


                        }
            }

        };

    }
}
