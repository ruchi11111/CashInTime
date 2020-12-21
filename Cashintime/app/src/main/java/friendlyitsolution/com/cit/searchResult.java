package friendlyitsolution.com.cit;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import friendlyitsolution.com.cit.adpter.ContactModel1;
import friendlyitsolution.com.cit.adpter.MyAdpter1;
import friendlyitsolution.com.cit.adpter.MyAdpter2;
import friendlyitsolution.com.cit.adpter.Userobject;

public class searchResult extends AppCompatActivity {

    static Context con;
    static ProgressDialog pd;
    RecyclerView recy;

    List<Userobject> list;
    MyAdpter2 adpter2;

    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pd=new ProgressDialog(searchResult.this);
        pd.setCancelable(false);
        pd.setMessage("Please wait..");
        title=findViewById(R.id.title);

        title.setText(MyApp.searchType);
        con=this;
        list=new ArrayList<>();
        adpter2=new MyAdpter2(list);
        recy=findViewById(R.id.recy);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(searchResult.this);
        recy.setLayoutManager(mLayoutManager);
        recy.setItemAnimator(new DefaultItemAnimator());
        recy.setAdapter(adpter2);
        ImageView backbtn=(ImageView)findViewById(R.id.backbtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchResult.super.onBackPressed();

            }
        });

        getData();
    }
    void getData()
    {
        list.clear();
        adpter2.notifyDataSetChanged();
        if(MyApp.searchType.equals("taker"))
        {
            list.addAll(Home.taker);

        }else
        {

             list.addAll(Home.giver);

        }
        if(list.size()==0)
        {
MyApp.showToastMsg("No data found");
        }
        adpter2.notifyDataSetChanged();

    }
    public static void showDia(final Userobject userobject)
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
        pbb.setVisibility(View.GONE);


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



                            }
                        });


                    }
                });


            }
        });


        dia.show();

    }

}
