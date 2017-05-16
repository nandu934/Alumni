package com.example.user.alumni.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.user.alumni.R;

public class Create_Event extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__event);

        textView = (TextView) findViewById(R.id.tv);
        Bundle bundle = getIntent().getExtras();
        String s = bundle.getString("DATE");
        textView.setText(s);
    }
}
