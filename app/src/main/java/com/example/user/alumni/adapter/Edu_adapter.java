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
import com.example.user.alumni.activity.Update_edu;
import com.example.user.alumni.activity.Update_workexp;
import com.example.user.alumni.models.EducationData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 05-04-2017.
 */

public class Edu_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<EducationData> data = new ArrayList<>();


    // create constructor to innitilize context and data sent from MainActivity
    public Edu_adapter(Context context, List<EducationData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cardview_education, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        EducationData current = data.get(position);
        myHolder.school.setText(current.school);
        myHolder.degree.setText(current.degree);
        myHolder.fos.setText(current.fieldofstudy);
        myHolder.grade.setText(current.grade);
        myHolder.activities.setText(current.activities);
        myHolder.startdate.setText(current.ed_startdate);
        myHolder.enddate.setText(current.ed_enddate);
        //myHolder.enddate.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        myHolder.description.setText(current.ed_description);

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

        TextView school;
        TextView degree;
        TextView fos,grade,activities;
        TextView startdate;
        TextView enddate;
        TextView description;
        ImageButton update;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            school = (TextView) itemView.findViewById(R.id.e_school);
            degree = (TextView) itemView.findViewById(R.id.e_degree);
            fos = (TextView) itemView.findViewById(R.id.e_fos);
            grade = (TextView) itemView.findViewById(R.id.e_grade);
            activities = (TextView) itemView.findViewById(R.id.e_activities);
            startdate = (TextView) itemView.findViewById(R.id.e_sdate);
            enddate = (TextView) itemView.findViewById(R.id.e_tdate);
            description = (TextView) itemView.findViewById(R.id.e_desc);
            update = (ImageButton) itemView.findViewById(R.id.update_edu);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,Update_edu.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("id",data.get(getAdapterPosition()).getId());
                    bundle.putString("school", data.get(getAdapterPosition()).getSchool());
                    bundle.putString("degree", data.get(getAdapterPosition()).getDegree());
                    bundle.putString("fos", data.get(getAdapterPosition()).getFieldofstudy());
                    bundle.putString("grade", data.get(getAdapterPosition()).getGrade());
                    bundle.putString("activity", data.get(getAdapterPosition()).getActivities());
                    bundle.putString("sdate", data.get(getAdapterPosition()).getEd_startdate());
                    bundle.putString("edate", data.get(getAdapterPosition()).getEd_enddate());
                    bundle.putString("jobdesc", data.get(getAdapterPosition()).getEd_description());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
        }

    }

}