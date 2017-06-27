package com.example.user.alumni.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.alumni.R;
import com.example.user.alumni.models.ContactsModel;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.app.ProgressDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.StringTokenizer;


//Class having OnItemClickListener to handle the clicks on list
public class Contacts extends AppCompatActivity implements ListView.OnItemClickListener {

    //Root URL of our web service
    public static final String ROOT_URL = "http://www.netibiz.com/";

    //Strings to bind with intent will be used to send data to other activity
    //public static final String KEY_BOOK_ID = "key_book_id";
    public static final String KEY_NAME = "key_name";
    public static final String KEY_EMAIL = "key_email";
    public static final String KEY_PHONE = "key_phone";

    //List view to show data
    private ListView listView;

    //List of type books this list will store type Book which is our data model
    private List<ContactsModel> contacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Initializing the listview
        listView = (ListView) findViewById(R.id.listViewBooks);

        //Calling the method that will fetch data
        getContacts();

        //Setting onItemClickListener to listview
        listView.setOnItemClickListener(this);
    }

    private void getContacts(){
        //While the app fetched data we are displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Fetching Data","Please wait...",false,false);

        //Creating a rest adapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .build();

        //Creating an object of our api interface
        ContactsRequest api = adapter.create(ContactsRequest.class);

        //Defining the method
        api.getContacts(new Callback<List<ContactsModel>>() {
            @Override
            public void success(List<ContactsModel> list, Response response) {
                //Dismissing the loading progressbar
                loading.dismiss();

                //Storing the data in our list
                contacts = list;

                //Calling a method to show the list
                showList();

            }

            @Override
            public void failure(RetrofitError error) {
                //you can handle the errors here
            }
        });
    }

    //Our method to show list
    private void showList(){
        //String array to store all the book names
        String[] items = new String[contacts.size()];

        //Traversing through the whole list to get all the names
        for(int i=0; i<contacts.size(); i++){
            //Storing names to string array
            items[i] = contacts.get(i).getName();
        }

        //Creating an array adapter for list view
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.contact_list,items);

        //Setting adapter to listview
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
                //return true;
                break;
            default:
                return super.onOptionsItemSelected(item);
            //return super.onOptionsItemSelected()
        }
        return false;
    }

    //This method will execute on listitem click
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Creating an intent
        Intent intent = new Intent(this, ShowContactsDetails.class);

        //Getting the requested book from the list
        ContactsModel model = contacts.get(position);

        //Adding book details to intent
        intent.putExtra(KEY_NAME,model.getName());
        intent.putExtra(KEY_EMAIL,model.getEmail());
        intent.putExtra(KEY_PHONE,model.getPhone());

        //Starting another activity to show book details
        startActivity(intent);
    }
}