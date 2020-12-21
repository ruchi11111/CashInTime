package friendlyitsolution.com.cit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import friendlyitsolution.com.cit.adpter.ContactModel1;
import friendlyitsolution.com.cit.adpter.MyAdpter1;

public class Requests extends AppCompatActivity {

    TextView title;
    RecyclerView recy;

    List<ContactModel1> list;
    MyAdpter1 adpter1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title=findViewById(R.id.title);

        if(MyApp.reqType.equals("takerrequest"))
        {
            title.setText("Taker's Requests");
        }
        else
        {

            title.setText("My Requests");
        }

        list=new ArrayList<>();
        adpter1=new MyAdpter1(list);
        recy=findViewById(R.id.recy);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Requests.this);
        recy.setLayoutManager(mLayoutManager);
        recy.setItemAnimator(new DefaultItemAnimator());
        recy.setAdapter(adpter1);
        ImageView backbtn=(ImageView)findViewById(R.id.backbtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Requests.super.onBackPressed();

            }
        });
        getData();

    }

    void getData()
    {

        if(MyApp.userdata.containsKey(MyApp.reqType))
        {
            Map<String,Object> da=(Map<String, Object>)MyApp.userdata.get(MyApp.reqType);
            List<String> key=new ArrayList<>(da.keySet());
            for(int i=0;i<key.size();i++)
            {
                Map<String,Object> data=(Map<String, Object>)da.get(key.get(i));
                list.add(new ContactModel1(data));

            }
            adpter1.notifyDataSetChanged();

        }
        else
        {
            list.clear();
            adpter1.notifyDataSetChanged();
            MyApp.showToastMsg("No data found");
        }

    }



}
