package friendlyitsolution.com.cit;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import friendlyitsolution.com.cit.adpter.ContactModel1;
import friendlyitsolution.com.cit.adpter.Userobject;

public class Detailpage extends AppCompatActivity implements OnMapReadyCallback {

    TextView title;
    CircleImageView img;

    ImageView call;
    TextView rs, date, name, address;
    Button btn;
    ContactModel1 model;

    ProgressDialog pd;

    private GoogleMap mMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getUserLiveData();


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                    MyApp.showToastMsg("faild");



                return false;
            }
        });
        // Add a marker in Sydney and move the camera

    }
void getUserLiveData()
{
    pd.show();
    MyApp.ref.child("users").child(model.data.get("id")+"").child("lastlocation").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            pd.dismiss();
            if(dataSnapshot.getValue()!=null)
            {
                Map<String,String> mm=(Map<String, String>)dataSnapshot.getValue();
                double lati=Double.parseDouble(mm.get("lati"));
                double longi=Double.parseDouble(mm.get("longi"));

                LatLng sydney = new LatLng(lati, longi);

                mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(model.data.get("name")+""));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lati, longi), 15));


                address.setText(mm.get("address")+"Last Update :"+mm.get("time"));

            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailpage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pd = new ProgressDialog(Detailpage.this);
        pd.setCancelable(false);
        pd.setMessage("Please wait..");
        call = findViewById(R.id.call);
        model = MyApp.curUserObject;
        btn = findViewById(R.id.btn);
        date = findViewById(R.id.date);
        rs = findViewById(R.id.rs);
        name = findViewById(R.id.name);
        img = findViewById(R.id.img);
        address = findViewById(R.id.address);
        title = findViewById(R.id.title);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.data.get("status").toString().equals("pending")) {
                    MyApp.showToastMsg("You can't call before accept your requst");
                } else {

                    makeCall();
                }


            }
        });

        setData();
        if (MyApp.reqType.equals("takerrequest")) {
            title.setText("Taker's Requests");
            if (model.data.get("status").toString().equals("pending")) {

                rs.setTextColor(Color.parseColor("#ff0000"));
                btn.setVisibility(View.VISIBLE);
            } else {
                rs.setTextColor(Color.parseColor("#00ff00"));
                rs.setText("Success -"+model.data.get("code") + "");

                btn.setVisibility(View.GONE);
            }
        } else {

            if (model.data.get("status").toString().equals("pending")) {

                rs.setTextColor(Color.parseColor("#ff0000"));

            } else {
                rs.setTextColor(Color.parseColor("#00ff00"));
                rs.setText("Success -"+model.data.get("code") + "");

            }
            btn.setVisibility(View.GONE);
            title.setText("My Requests");
        }
        ImageView backbtn = (ImageView) findViewById(R.id.backbtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Detailpage.super.onBackPressed();

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptRequest();
            }
        });


    }

    void makeCall() {

        try {
            String call = "tel:" + model.data.get("id").toString();
            Intent is = new Intent(Intent.ACTION_CALL, Uri.parse(call));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                MyApp.showToastMsg("Give Permission for calling");
               return;
            }
            startActivity(is);
        }
        catch(Exception e)
        {

        }
    }

    void acceptRequest() {
        pd.show();

        final String accpetcode = getRandomString();
        final Map<String, Object> dd = new HashMap<>();
        dd.put("status", "accept");
        dd.put("code", accpetcode);

        MyApp.ref.child("users").child(model.data.get("id") + "").child("myrequest").child(MyApp.mynumber).updateChildren(dd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                pd.setMessage("wait a min..");
                MyApp.userref.child("takerrequest").child(model.data.get("id") + "").updateChildren(dd).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        MyApp.sendNotificationTo1(model.data.get("id")+"",MyApp.userdata.get("name")+"","Accept your request");

                        rs.setText("Success -"+accpetcode + "");
                        model.data.remove("status");
                        model.data.put("status","accept");
                        btn.setVisibility(View.GONE);

                        rs.setTextColor(Color.parseColor("#00ff00"));
                        pd.dismiss();
                        MyApp.showToastMsg("Successfully Updated");

                    }
                });

            }
        });


    }

    String getRandomString() {
        String st;
        Random r = new Random();
        st = r.nextInt(9) + "" + r.nextInt(9) + "" + r.nextInt(9) + "" + r.nextInt(9);

        return st;
    }

    void setData() {

        date.setText(model.data.get("time") + "");
        name.setText(model.data.get("name") + "");

        address.setText(model.data.get("address") + "");

        rs.setText(model.data.get("status") + "");



        Glide.with(img.getContext()).load(model.data.get("img") + "")
                .override(100, 100)
                .fitCenter()
                .into(img);
    }

}
