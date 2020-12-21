package friendlyitsolution.com.cit.adpter;

import android.content.Intent;
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
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import friendlyitsolution.com.cit.Detailpage;
import friendlyitsolution.com.cit.MyApp;
import friendlyitsolution.com.cit.R;
import friendlyitsolution.com.cit.searchResult;

public class MyAdpter2 extends RecyclerView.Adapter<MyAdpter2.MyViewHolder> {

    private List<Userobject> moviesList;

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


    public MyAdpter2(List<Userobject> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemusers_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Userobject model = moviesList.get(position);


       if(MyApp.searchType.equals("taker"))
       {

           holder.rs.setVisibility(View.GONE);
       }
       else
       {
           holder.rs.setVisibility(View.VISIBLE);

           holder.rs.setText(model.data.get("amt")+"Rs.\n"+model.data.get("rate")+" %");
       }

        holder.name.setText(model.data.get("name")+"");

        Map<String,String> dd=(Map<String, String>)model.data.get("location");

        holder.address.setText(dd.get("address")+"");
        holder.date.setText(dd.get("time")+"");

        Glide.with(holder.img.getContext()).load(model.data.get("img")+"")
                .override(60, 60)
                .fitCenter()
                .into(holder.img);





        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchResult.showDia(model);
            }
        });

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
