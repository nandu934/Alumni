package com.example.user.alumni.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.user.alumni.R;

public class ShowContactsDetails extends AppCompatActivity {

        //Defining views
        private TextView textViewName;
        private TextView textViewEmail;
        private TextView textViewPhone;
        private TextView textViewBookInStock;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_show_contacts_details);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            //Initializing Views
            textViewName = (TextView) findViewById(R.id.textName);
            textViewEmail = (TextView) findViewById(R.id.textEmail);
            textViewPhone = (TextView) findViewById(R.id.textPhone);

            //Getting intent
            Intent intent = getIntent();

            //Displaying values by fetching from intent
            textViewName.setText(intent.getStringExtra(Contacts.KEY_NAME));
            textViewEmail.setText(intent.getStringExtra(Contacts.KEY_EMAIL));
            textViewPhone.setText(intent.getStringExtra(Contacts.KEY_PHONE));
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, Contacts.class);
                startActivity(intent);
                this.finish();
                //return true;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
}