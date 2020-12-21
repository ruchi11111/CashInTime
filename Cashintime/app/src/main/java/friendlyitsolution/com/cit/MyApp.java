package friendlyitsolution.com.cit;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Map;

import friendlyitsolution.com.cit.adpter.ContactModel1;

/**
 * Created by hiren on 01/10/2017.
 */

public class MyApp extends Application {


    public static String serverkey="AAAAfC_IyUg:APA91bHnYvQ-ZFaZ_8uDv5p1USRpE7D6vfr6nqbl8WiGAFaGtF2EkIETYyluwO3dkDs6Und6hdhDexTWSiXwzA1hcxxfdLGpKY2uPT_oyJXp3TWVCFU1PUxrZErkXzwkNiNlGSNk7moJ";
    public static String senderid="533377632584";


    public static String searchType="";
    public static Context con;
    public static double facepoint=0.50;
    public static ValueEventListener velistener;
    static FirebaseDatabase fd;
    static DatabaseReference ref;

    public static Map<String,Object> userdata;
    public static String mynumber,myname;

    public static Map<String,String> CurSong;
    public static DatabaseReference userref;
static SharedPreferences sharedpreferences;

public static ContactModel1 curUserObject=null;
public static String reqType="";
    @Override
    public void onCreate() {
        super.onCreate();

        con=getApplicationContext();



        fd= FirebaseDatabase.getInstance();
      //  fd.setPersistenceEnabled(true);

        ref=fd.getReference();
      sharedpreferences = getSharedPreferences("myinfo", Context.MODE_PRIVATE);
        mynumber=sharedpreferences.getString("mynumber","");


        if(!mynumber.equals(""))
        {

            FirebaseMessaging.getInstance().subscribeToTopic(mynumber);
            getUserData();

        }
    }



    static void showToastMsg(String msg)
    {
        Toast.makeText(con,msg, Toast.LENGTH_LONG).show();
    }

   static void getUserData()
    {
        userref=fd.getReference("users").child(mynumber);
        velistener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userdata=(Map<String, Object>)dataSnapshot.getValue();
                try{//ewallet.setStatus();
                     }
                catch(Exception e)
                {}
               //Toast.makeText(con,"Data : "+userdata,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        userref.addValueEventListener(velistener);
      //  userref.keepSynced(true);
    }

    public static void clearUserData()
    {
        userref.removeEventListener(velistener);
        userdata.clear();
    }

    public static void sendNotificationTo1(final String to,final String title,final String msg) {


        class GetImage extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String st) {
                super.onPostExecute(st);

               //MyApp.showToastMsg("Msg : "+st);

            }

            @Override
            protected String doInBackground(String... params) {

                String data2="";

                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                String msg1=msg.replace(" ","%20");
                String title1=title.replace(" ","%20");
                Webservices ws=new Webservices();
                ws.setUrl("https://m19.io/meet/Default.aspx?topic="+to+"&title="+title1+"&msg="+msg1+"&key="+serverkey+"&senderid="+senderid);
                //  ws.addParam("title",nn);
                // ws.addParam("topic",to);
                // ws.addParam("msg",mm);
                ws.connect();
                String dd=ws.getData();


                return dd;
            }
        }

        GetImage gi = new GetImage();
        gi.execute("");



    }

}
