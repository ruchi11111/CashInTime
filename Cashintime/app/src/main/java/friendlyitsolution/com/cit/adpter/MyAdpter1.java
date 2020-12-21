package friendlyitsolution.com.cit.adpter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import friendlyitsolution.com.cit.Detailpage;
import friendlyitsolution.com.cit.MyApp;
import friendlyitsolution.com.cit.R;

public class MyAdpter1 extends RecyclerView.Adapter<MyAdpter1.MyViewHolder> {

    private List<ContactModel1> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        CircleImageView img;

        RelativeLayout rel;
        TextView rs,date,name,dd,address;

        public MyViewHolder(View view) {
            super(view);

            dd=view.findViewById(R.id.dd);
            date=view.findViewById(R.id.date);
            rs=view.findViewById(R.id.rs);
            name=view.findViewById(R.id.name);
            img=view.findViewById(R.id.img);
            address=view.findViewById(R.id.address);
            rel=view.findViewById(R.id.rel);
        }
    }


    public MyAdpter1(List<ContactModel1> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemusers, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ContactModel1 model = moviesList.get(position);


        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApp.curUserObject=model;
                Intent i=new Intent(MyApp.con, Detailpage.class);
                MyApp.con.startActivity(i);
            }
        });

        holder.date.setText(model.data.get("time")+"");
        holder.name.setText(model.data.get("name")+"");

        holder.address.setText(model.data.get("address")+"");

        holder.rs.setText(model.data.get("status")+"");

        Glide.with(holder.img.getContext()).load(model.data.get("img")+"")
                .override(60, 60)
                .fitCenter()
                .into(holder.img);




    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
