package com.example.user.alumni.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.user.alumni.R;
import com.example.user.alumni.activity.Update_vol;
import com.example.user.alumni.activity.Update_workexp;
import com.example.user.alumni.models.EducationData;
import com.example.user.alumni.models.VolExpData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 06-04-2017.
 */

public class VolExp_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<VolExpData> data = new ArrayList<>();


    // create constructor to innitilize context and data sent from MainActivity
    public VolExp_adapter(Context context, List<VolExpData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cardview_volexp, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        VolExpData current = data.get(position);
        myHolder.org.setText(current.organisation);
        myHolder.role.setText(current.role);
        myHolder.cause.setText(current.cause);
        myHolder.sdate.setText(current.sdate);
        myHolder.edate.setText(current.edate);
        myHolder.desc.setText(current.description);

//        // load image into imageview using glide
//        Glide.with(context).load("http://192.168.1.7/test/images/" + current.fishImage)
//                .placeholder(R.drawable.ic_img_error)
//                .error(R.drawable.ic_img_error)
//                .into(myHolder.ivFish);

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView org,role,cause,sdate,edate,desc;
        ImageButton update;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            org = (TextView) itemView.findViewById(R.id.v_organisation);
            role = (TextView) itemView.findViewById(R.id.v_role);
            cause = (TextView) itemView.findViewById(R.id.v_cause);
            sdate = (TextView) itemView.findViewById(R.id.v_sdate);
            edate = (TextView) itemView.findViewById(R.id.v_tdate);
            desc = (TextView) itemView.findViewById(R.id.v_desc);
            update = (ImageButton) itemView.findViewById(R.id.vol_u);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,Update_vol.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("id",data.get(getAdapterPosition()).getId());
                    bundle.putString("org", data.get(getAdapterPosition()).getOrganisation());
                    bundle.putString("role", data.get(getAdapterPosition()).getRole());
                    bundle.putString("cause", data.get(getAdapterPosition()).getCause());
                    bundle.putString("sdate", data.get(getAdapterPosition()).getSdate());
                    bundle.putString("edate", data.get(getAdapterPosition()).getEdate());
                    bundle.putString("jobdesc", data.get(getAdapterPosition()).getDescription());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
        }

    }

}
