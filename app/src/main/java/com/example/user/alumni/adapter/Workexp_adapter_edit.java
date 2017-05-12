package com.example.user.alumni.adapter;

/**
 * Created by User on 04-04-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.alumni.R;
import com.example.user.alumni.activity.Update_workexp;
import com.example.user.alumni.models.WorkexpData;

import java.util.Collections;
import java.util.List;

public class Workexp_adapter_edit extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ImageButton Prof_workex;
    List<WorkexpData> data= Collections.emptyList();
    WorkexpData current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public Workexp_adapter_edit(Context context, List<WorkexpData> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.cardview_work_edit, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        WorkexpData current=data.get(position);
        myHolder.company.setText(current.company);
        myHolder.title.setText(current.title);
        myHolder.location.setText(current.location);
        myHolder.startdate.setText(current.startdate);
        myHolder.enddate.setText(current.enddate);
        //myHolder.enddate.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        myHolder.description.setText(current.description);


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

    public void delete(int position) { //removes the row
        data.remove(position);
        Log.v("adapter_position", String.valueOf(position));
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }


    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView company;
        TextView title;
        TextView location;
        TextView startdate;
        TextView enddate;
        TextView description;
        ImageButton delete,update;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            company= (TextView) itemView.findViewById(R.id.w_companye);
            title= (TextView) itemView.findViewById(R.id.w_titlee);
            location = (TextView) itemView.findViewById(R.id.w_locatione);
            startdate = (TextView) itemView.findViewById(R.id.w_sdatee);
            enddate = (TextView) itemView.findViewById(R.id.w_tdatee);
            description = (TextView) itemView.findViewById(R.id.w_desce);
            delete = (ImageButton) itemView.findViewById(R.id.Prof_workexpe);
            delete.setOnClickListener(this);
            update = (ImageButton) itemView.findViewById(R.id.updatee);
            update.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Prof_workexpe:
                    delete(getAdapterPosition()); //calls the method above to delete
                    Toast.makeText(context, "Removed : ", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.updatee:

//            WorkexpData al= data.get(position);
//            Intent intent = new Intent(context, Workexp_adapter_edit.class);
//            intent.putExtra("company", al.getCompany());
//            context.startActivity(intent);
                    Intent intent=new Intent(context,Update_workexp.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("COMPANY", data.get(getAdapterPosition()).getCompany());
                    bundle.putString("TITLE", data.get(getAdapterPosition()).getTitle());
                    bundle.putString("LOCATION", data.get(getAdapterPosition()).getLocation());
                    bundle.putString("SDATE", data.get(getAdapterPosition()).getStartdate());
                    bundle.putString("EDATE", data.get(getAdapterPosition()).getEnddate());
                    bundle.putString("JOBDESC", data.get(getAdapterPosition()).getDescription());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                    break;
            }
        }
    }
}