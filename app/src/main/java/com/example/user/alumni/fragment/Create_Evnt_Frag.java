package com.example.user.alumni.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.alumni.R;

public class Create_Evnt_Frag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create__evnt, container, false);
        //return inflater.inflate(R.layout.hello_fragment_layout, null);
    }
}
