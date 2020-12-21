package friendlyitsolution.com.cit;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import friendlyitsolution.com.cit.adpter.Userobject;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class Home extends AppCompatActivity implements OnMapReadyCallback {
    TextView tvstatus;
    private GoogleMap mMap;

    public static Context con;
    CircleImageView iv;

    TextView address;
    double lati,longi;
    Button btn;
    RelativeLayout lic,lic1;

    boolean isGiver;
    ProgressDialog pd;
    Location location;

    static List<Userobject> giver,taker;

    Button btntkr,btngvr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        giver=new ArrayList<>();
        taker=new ArrayList<>();
        btngvr=findViewById(R.id.gvr);
        btntkr=findViewById(R.id.tkr);
        btngvr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyApp.searchType="giver";
                Intent i=new Intent(Home.this,searchResult.class);
                startActivity(i);
            }
        });
        btntkr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApp.searchType="taker";
                Intent i=new Intent(Home.this,searchResult.class);
                startActivity(i);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        pd=new ProgressDialog(Home.this);
        pd.setCancelable(false);
        pd.setMessage("Please wait..");


        lic=findViewById(R.id.lic);

        lic1=findViewById(R.id.lic1);
        btn=findViewById(R.id.btn);

        lic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  MyApp.sendNotificationTo("9099366511","hello","Sending Request for money");

                MyApp.reqType="takerrequest";
                Intent i=new Intent(Home.this,Requests.class);
                startActivity(i);


            }
        });

        lic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               MyApp.reqType="myrequest";
              Intent i=new Intent(Home.this,Requests.class);
     startActivity(i);


            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDia();
            }
        });
        address=findViewById(R.id.address);
        con=this;
        if(SmartLocation.with(Home.this).location().state().isGpsAvailable()) {
            SmartLocation.with(Home.this).location().start(new OnLocationUpdatedListener() {
                @Override
                public void onLocationUpdated(Location location) {
                    updateLocation(location);
                }
            });
        }
        else
        {
            MyApp.showToastMsg("Please enable GPS");
        }
        ImageView lgout=(ImageView)findViewById(R.id.lgbtn);
        ImageView backbtn=(ImageView)findViewById(R.id.backbtn);
        tvstatus=(TextView)findViewById(R.id.tvname);

    lgout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logOut();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.super.onBackPressed();
            }
        });
        iv=(CircleImageView)findViewById(R.id.profile_image);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MyApp.con,profile.class);

                startActivity(i);
            }
        });
    }


    Map<String,String> updateLocation(Location location)
    {

        try {
            this.location=location;
            lati = location.getLatitude();
            longi = location.getLongitude();
            Map<String, String> lastloc = new HashMap<>();
            lastloc.put("lati", lati + "");
            lastloc.put("longi", longi + "");
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            lastloc.put("time", formattedDate);
            Geocoder code = new Geocoder(getApplicationContext(), Locale.ENGLISH);
            List<Address> list = code.getFromLocation(lati, longi, 10);
            Address add;
            String myadd[];
            add = list.get(0);
            myadd = new String[add.getMaxAddressLineIndex()];
            //fulladd=fulladd+"\nlati"+lati+" longi:"+longi;

            LatLng sydney = new LatLng(lati, longi);

            //mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory
           //         .defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("My Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
           mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lati, longi), 15));

            String postalcode = add.getPostalCode();
            String city = add.getLocality();
            String area = add.getSubLocality();
            String adds =  area + " , " + postalcode;
            String fulladd="";
            fulladd = fulladd + adds + "\n";
            fulladd = fulladd + city + " , " + add.getAdminArea();
            address.setText(fulladd+"\nLast Update : "+formattedDate);
            lastloc.put("address",fulladd);
            MyApp.userref.child("lastlocation").setValue(lastloc);

            return lastloc;
        }
        catch(Exception e)
        {

            return null;
        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        // googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

        // Showing / hiding your current location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

        // Enable / Disable zooming controls
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Enable / Disable my location button
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Enable / Disable Compass icon
        googleMap.getUiSettings().setCompassEnabled(true);

        // Enable / Disable Rotate gesture
        googleMap.getUiSettings().setRotateGesturesEnabled(true);

        // Enable / Disable zooming functionality
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                try{
                    Userobject u=(Userobject)marker.getTag();

                    if(u!=null)
                    {
                        showUserDia(u);
                    }

                }
                catch(Exception e)
                {

                    MyApp.showToastMsg("faild");
                }


                return false;
            }
        });
        // Add a marker in Sydney and move the camera

    }

    void showUserDia(final Userobject userobject)
    {
       final Dialog dia=new Dialog(con);
        dia.setCancelable(false);
        dia.setContentView(R.layout.dialoug_user);
        Window window = dia.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final ProgressBar pbb=dia.findViewById(R.id.pb);
        ImageView btnClose = (ImageView) dia.findViewById(R.id.img);
        TextView name = (TextView) dia.findViewById(R.id.winname);
        TextView type = (TextView) dia.findViewById(R.id.type);

        ImageView back=dia.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dia.dismiss();
            }
        });
        Button btn=dia.findViewById(R.id.btn);
        name.setText(userobject.data.get("name")+"");
        if(userobject.data.get("type").toString().equals("taker")) {
            type.setText(userobject.data.get("type") + "");
            btn.setVisibility(View.GONE);
        }
        else
        {
            btn.setVisibility(View.VISIBLE);
            type.setText(userobject.data.get("type") + "("+userobject.data.get("amt")+","+userobject.data.get("rate")+"% )");

        }
        Glide.with(btnClose.getContext()).load(userobject.data.get("img")+"")
                .override(200, 200)
                .fitCenter()
                .into(btnClose);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dia.dismiss();
                pd.show();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd MMM,yyyy HH:mm");
                final String formattedDate = df.format(c.getTime());

                Map<String,String> req=new HashMap<>();
                req.put("id",MyApp.mynumber);
                req.put("time",formattedDate);
                req.put("status","pending");
                Map<String,String> loca=(Map<String, String>)MyApp.userdata.get("lastlocation");

                req.put("address",loca.get("address")+"");
                req.put("img",MyApp.userdata.get("imgurl")+"");
                req.put("name",MyApp.userdata.get("name")+"");
                MyApp.ref.child("users").child(userobject.data.get("id")+"").child("takerrequest").child(MyApp.mynumber).setValue(req).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Map<String,String> myreq=new HashMap<>();
                        myreq.put("name",userobject.data.get("name")+"");
                        myreq.put("time",formattedDate);
                        myreq.put("img",userobject.data.get("img")+"");
                        myreq.put("id",userobject.data.get("id")+"");
                        Map<String,String> loc=(Map<String, String>)userobject.data.get("location");
                        myreq.put("address",loc.get("address"));
                        myreq.put("status","pending");
                        MyApp.userref.child("myrequest").child(userobject.data.get("id")+"").setValue(myreq).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                pd.dismiss();
                                MyApp.showToastMsg("Successfully placed request");

                                MyApp.sendNotificationTo1(userobject.data.get("id")+"",MyApp.userdata.get("name")+"","Sending request for money");



                            }
                        });


                    }
                });


            }
        });


        dia.show();


    }

    @Override
    protected void onResume() {
        super.onResume();


       setUserData();
    }
    void setUserData()
    {
        if(MyApp.userdata==null)
        {

            pd.show();
            MyApp.ref.child("users").child(MyApp.mynumber).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    MyApp.ref.child("users").child(MyApp.mynumber).removeEventListener(this);
                    MyApp.userdata=(Map<String, Object>)dataSnapshot.getValue();
                    setUserData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }
        else {

            pd.dismiss();
            if (MyApp.userdata.get("ison").equals("no")) {
                btn.setText("Turn On");
            } else {
                btn.setText(MyApp.userdata.get("ison") + "");
            }

            Glide.with(iv.getContext()).load(MyApp.userdata.get("imgurl").toString())
                    .override(100, 100)
                    .fitCenter()
                    .into(iv);


            tvstatus.setText(MyApp.userdata.get("name") + "");
            getAllOnlineData();
        }
    }
    void logOut()
    {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialoug_logout);
        dialog.show();

        final Button btnInDialog = (Button) dialog.findViewById(R.id.btn);
        btnInDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = MyApp.sharedpreferences.edit();
                editor.clear();
                editor.commit();
                finish();
                MyApp.mynumber="";
                MyApp.myname="";

                Intent i = new Intent(Home.this,login.class);
                startActivity(i);

                finish();
            }
        });
        final ImageView btnClose = (ImageView) dialog.findViewById(R.id.canclebtn);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });




       /* new AwesomeInfoDialog(this)
                .setTitle("Logout")
                .setMessage("Want To Logout?")
                .setColoredCircle(R.color.colorAccent)
                .setDialogIconAndColor(R.drawable.logoutbtn, R.color.white)
                .setCancelable(true)
                .setPositiveButtonText(getString(R.string.dialog_yes_button))
                .setPositiveButtonbackgroundColor(R.color.colorAccent)
                .setPositiveButtonTextColor(R.color.white)
                .setNegativeButtonText(getString(R.string.dialog_no_button))
                .setNegativeButtonbackgroundColor(R.color.colorAccent)
                .setNegativeButtonTextColor(R.color.white)
                .setPositiveButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        //click

                        SharedPreferences sharedPreferences = getSharedPreferences("myinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        finish();


                        Intent i = new Intent(new_home.this,Login.class);
                        startActivity(i);

                        finish();

                    }
                })
                .setNegativeButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        //click


                    }
                })
                .show();
        */

    }


    void getAllOnlineData()
    {
        giver.clear();
        taker.clear();
        pd.show();
        MyApp.ref.child("online").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                pd.dismiss();
                MyApp.ref.child("online").removeEventListener(this);
                if(dataSnapshot.getValue()!=null)
                {
                    Map<String,Object> allda=(Map<String, Object>)dataSnapshot.getValue();
                    List<String> keys=new ArrayList<>(allda.keySet());
                    for(int i=0;i<keys.size();i++)
                    {

                        Map<String,Object> data=(Map<String, Object>)allda.get(keys.get(i));
                        Map<String,String> loc=(Map<String, String>)data.get("location");

                        Userobject userob=new Userobject(data,keys.get(i));
                        if(data.get("type").toString().equals("taker"))
                        {


                            taker.add(userob);
                           Marker m= mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(loc.get("lati")),Double.parseDouble(loc.get("longi")))).icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title(data.get("name")+""));

                           m.setTag(userob);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(loc.get("lati")),Double.parseDouble(loc.get("longi"))), 15));


                        }
                        else
                        {

                           Marker m= mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(loc.get("lati")),Double.parseDouble(loc.get("longi")))).icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(data.get("name")+""));

                            m.setTag(userob);
                            giver.add(userob);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(loc.get("lati")),Double.parseDouble(loc.get("longi"))), 15));

                        }


                    }
                    updateLocation(location);
                }
                else
                {
                    MyApp.showToastMsg("No user online");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    void showDia()
    {

       final Dialog d=new Dialog(Home.this);
        d.setContentView(R.layout.dialoug_layout);
        d.show();
        RadioGroup grp=d.findViewById(R.id.grp);
        isGiver=true;
        grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.giver)
                {
                   isGiver=true;
                } else if (checkedId == R.id.taker) {
                    isGiver=false;
                }

            }

        });
        Button btnsave=d.findViewById(R.id.btnset);
        Button btnoff=d.findViewById(R.id.btnoff);

        final EditText etamt=d.findViewById(R.id.amt);
        final EditText etrate=d.findViewById(R.id.rate);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(isGiver) {
                   if (!etamt.getText().toString().isEmpty() && !etrate.getText().toString().isEmpty()) {

                       d.dismiss();
                       Map<String,Object> da=new HashMap<>();
                       da.put("type","giver");
                       da.put("id",MyApp.mynumber);
                       da.put("rate",etrate.getText().toString());
                       da.put("amt",etamt.getText().toString());
                       da.put("img",MyApp.userdata.get("imgurl")+"");
                       da.put("location",updateLocation(location));
                       da.put("name",MyApp.userdata.get("name")+"");

                       pd.show();
                       MyApp.ref.child("online").child("M"+MyApp.mynumber).setValue(da).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {

                               MyApp.userref.child("ison").setValue("Giver(Rs. "+etamt.getText().toString()+","+etrate.getText().toString()+" %)").addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       pd.dismiss();
                                       setUserData();
                                   }
                               });

                           }
                       });

                   }
                   else
                   {
                       etamt.setError("Enter");
                       etrate.setError("Enter");
                   }
               }
               else
               {
                   d.dismiss();
                   Map<String,Object> da=new HashMap<>();
                   da.put("type","taker");
                   da.put("id",MyApp.mynumber);
                   da.put("location",updateLocation(location));
                   da.put("img",MyApp.userdata.get("imgurl")+"");
                   da.put("name",MyApp.userdata.get("name")+"");
                   pd.show();
                   MyApp.ref.child("online").child("M"+MyApp.mynumber).setValue(da).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {

                           MyApp.userref.child("ison").setValue("Taker").addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   pd.dismiss();
                                   setUserData();
                               }
                           });

                       }
                   });

               }
            }
        });

        btnoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
                pd.show();
                MyApp.ref.child("online").child("M"+MyApp.mynumber).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        MyApp.userref.child("ison").setValue("no").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                pd.dismiss();
                                setUserData();
                            }
                        });

                    }
                });
            }
        });




    }

}
