package com.example.user.alumni.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.user.alumni.R;
import com.example.user.alumni.activity.MyProfile;
import com.example.user.alumni.activity.Update_skill;
import com.example.user.alumni.activity.Update_vol;
import com.example.user.alumni.models.SkillData;
import com.example.user.alumni.models.WorkexpData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 10-04-2017.
 */

public class Skill_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private  LayoutInflater inflater;
    List<SkillData> data = new ArrayList<>();

    // create constructor to innitilize context and data sent from MainActivity
    public Skill_adapter(Context context, List<SkillData> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cardview_skill,parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder= (MyHolder) holder;
        SkillData current=data.get(position);
        myHolder.skname.setText(current.skillname);
        myHolder.sklevel.setText(current.Skilllevel);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView skname,sklevel;
        CardView sCard;
        ImageButton update;
        public MyHolder(View itemView) {
            super(itemView);

            sCard = (CardView) itemView.findViewById(R.id.card_skill);
            skname = (TextView) (itemView).findViewById(R.id.skilln);
            sklevel = (TextView) (itemView).findViewById(R.id.skilllev);
            update = (ImageButton) itemView.findViewById(R.id.skill_update);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,Update_skill.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("id",data.get(getAdapterPosition()).getId());
                    bundle.putString("skillname", data.get(getAdapterPosition()).getSkillname());
                    bundle.putString("skilllevel", data.get(getAdapterPosition()).getSkilllevel());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
