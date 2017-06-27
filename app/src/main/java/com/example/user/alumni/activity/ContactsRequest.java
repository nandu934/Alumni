package com.example.user.alumni.activity;

import com.example.user.alumni.models.ContactsModel;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;


/**
 * Created by User on 19-06-2017.
 */

public interface ContactsRequest {
    /*Retrofit get annotation with our URL
        And our method that will return us the list ob Book
     */
    @GET("/alumni/contacts_view.php")
    //public void getBooks(Callback<List<ContactsModel>> response);

    public void getContacts(Callback<List<ContactsModel>> response);
}
