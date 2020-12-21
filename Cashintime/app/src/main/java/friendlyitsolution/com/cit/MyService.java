package friendlyitsolution.com.cit;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

public class MyService extends FirebaseMessagingService {
    public MyService() {
    }

    private static int id = 0;
    private static int unread_notif = 0;
    private static final String TAG = "MyGcmListenerService";
    String type;
    int ids=0;
    String typePass = "O";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
      //  Log.e("dataChat",remoteMessage.getData().toString());
        try {
            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            this.showNotification(object.get("title")+"",""+object.get("body"));
      //      Log.e("JSON_OBJECT", object.toString());
        }
        catch (Exception e)
        {
       //     Log.e("JSON_OBJECT_Error", e.getMessage());

        }


    }

    void showNotification(String title, String body)
    {       NotificationManager manage = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent i1=new Intent(this,MainActivity.class);
        i1.putExtra("fine","yes");
        PendingIntent pi1= PendingIntent.getActivity(this, 0, i1, 0);
        Notification not = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pi1)// to start current app)
                .setContentTitle(title)
                .setLights(Color.BLUE, 500, 500)
                .setContentText(body).build();


        //Ton

        not.defaults = Notification.DEFAULT_ALL;
        //not.defaults=Notification.DEFAULT_VIBRATE;

        manage.notify(0, not);
    }
    private void sendNotification(String title, String text) throws UnsupportedEncodingException {

  /*      Intent notificationIntent = new Intent(getBaseContext(), AddClientFill.class);
        notificationIntent.putExtra("from", "Notification");
        notificationIntent.putExtra("type", typePass);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
*/
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.putExtra(NotificationData.TEXT, notificationData.getTextMessage());
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Bundle bundle = new Bundle();
//        bundle.putString("from", "Notification");
//        bundle.putString("type", "O");
//        intent.putExtras(bundle);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        String str1 = null, str2 = null, str3 = null;


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Notification notification = new NotificationCompat.Builder(getBaseContext())

                    .setColor(Color.GRAY)
                    .setSmallIcon(R.drawable.logo)
                    .setTicker("PokeWorld")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(false)
                    .setContentTitle(URLDecoder.decode(title, "UTF-8"))
                    .setContentText(URLDecoder.decode(text, "UTF-8"))
                    .setGroup("1")
                    .build();

            notificationManager.notify(ids++, notification);


        } else {
            Notification notification = new NotificationCompat.Builder(getBaseContext())

                    .setColor(Color.GRAY)
                    .setSmallIcon(R.drawable.logo)
                    .setTicker("PokeWorld")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle(URLDecoder.decode(title, "UTF-8"))
                    .setContentText(URLDecoder.decode(text, "UTF-8"))
                    .setGroup("1")
                    .build();

            notificationManager.notify(ids++, notification);


        }

//        Notification notification = new NotificationCompat.Builder(getBaseContext())
//                .setContentIntent(pendingIntent)
//                .setColor(Color.WHITE)
//                .setSmallIcon(R.drawable.aaaa)
//                .setTicker("Campusknot Inc.")
//                .setWhen(System.currentTimeMillis())
//                .setAutoCancel(true)
//                .setContentTitle(URLDecoder.decode(notificationData.getTitle(), "UTF-8"))
//                .setContentText(URLDecoder.decode(notificationData.getTextMessage(), "UTF-8"))
//                .setGroup("1")
//                .build();
//
//        notificationManager.notify(id++, notification);
//
//        unread_notif++;
//
//        if (unread_notif > 1) {
//            Notification summaryNotification = new NotificationCompat.Builder(getBaseContext())
//                    .setContentIntent(pendingIntent)
//                    .setContentTitle("Campusknot Inc.")
//                    .setColor(Color.WHITE)
//                    .setAutoCancel(true)
//                    .setContentText(Config.arr.size() + " messages")
//                    .setSmallIcon(R.drawable.aaaa)
//                    .setStyle(new NotificationCompat.InboxStyle()
//                            .addLine(str1)
//                            .addLine(str2)
//                            .addLine(str3)
//                            .setBigContentTitle("Campusknot Inc.")
//                            .setSummaryText(Integer.toString(unread_notif) + " messages"))
//                    .setGroup("1")
//                    .setGroupSummary(true)
//                    .build();
//
//            notificationManager.notify(1, summaryNotification);

//        }

//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
//
//        Intent notificationIntent = new Intent(this, HomeActivity.class);
//        Bundle bundle1 = new Bundle();
//        bundle1.putString("from", "Notification");
//        bundle1.putString("type", "O");
//        notificationIntent.putExtras(bundle);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, new Random().nextInt(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        notificationBuilder.setContentIntent(contentIntent);


    }
}
