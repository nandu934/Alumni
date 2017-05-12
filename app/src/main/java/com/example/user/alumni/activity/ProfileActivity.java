package com.example.user.alumni.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.alumni.R;
import com.example.user.alumni.Utility;
import com.example.user.alumni.adapter.ExpandableList;
import com.example.user.alumni.app.AppConfig;
import com.example.user.alumni.app.AppController;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import de.hdodenhof.circleimageview.CircleImageView;
import static com.example.user.alumni.R.id.home;
import static com.example.user.alumni.R.id.imageButton;


//import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE = 1;
    private static final int CAPTURE_IMAGE = 2;
    private static final int MY_SOCKET_TIMEOUT_MS = 20000 ;
    private static final int STORAGE_PERMISSION_CODE = 123 ;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int SD_REQUEST = 202;
    private static final int SELECT_PICTURE = 100;
    private ProgressDialog pDialog;

    //********************************************
    private int PICK_IMAGE_REQUEST = 1;
    private int REQUEST_CAMERA = 0;
    private String userChoosenTask;
    private String KEY_IMAGE = "image";
    private String KEY_UID = "id";
    private Uri filePath;
    private Uri mCropImageUri;
    //Bitmap photo;
    String picturePath;
    private String path;
//**********************************************

    private String selectedImagePath = "";
    private String imgPath;
    private Bitmap bitmap;
    private String userId;
    ImageButton imb;
    private Button upload;
    private CircleImageView cirleImg;
    ExpandableList expListAdapter;
    List<String> ChildList;
    Map<String, List<String>> ParentListItems;
    ExpandableListView expandablelistView;

    List<String> ParentList = new ArrayList<String>();

    {
        ParentList.add("INTRODUCTION");
        ParentList.add("BACKGROUND");
        ParentList.add("SKILLS");
//        ParentList.add("ACCOMPLISHMENTS");
//        ParentList.add("ADDITIONAL INFORMATIONS");
    }

    // Assign children list element using string array.
    String[] INTRO = {"introduction"};
    String[] BACKGND = {"Work Experience", "Education", "Volunteer Experience"};
    String[] SKIL = {"Skills"};
    //    String[] ACCOM = { "Certifications","Courses","Projects","Honors & Awards","Publications","Patents" };
//    String[] ADD = { "Topics for contacting" };
    String[] ByDefalutMessage = {"Items Loading"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //requestStoragePermission();
        imb = (ImageButton) findViewById(imageButton);
        imb.setOnClickListener(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        cirleImg = (CircleImageView) findViewById(R.id.cirle_img);
        upload = (Button) findViewById(R.id.img);
        upload.setOnClickListener(this);
        ParentListItems = new LinkedHashMap<String, List<String>>();

        for (String HoldItem : ParentList) {
            if (HoldItem.equals("INTRODUCTION")) {
                loadChild(INTRO);
            } else if (HoldItem.equals("BACKGROUND")) {
                loadChild(BACKGND);
            } else if (HoldItem.equals("SKILLS")) {
                loadChild(SKIL);
//            }else if (HoldItem.equals("ACCOMPLISHMENTS")){
//                loadChild(ACCOM);
//            }else if (HoldItem.equals("ADDITIONAL INFORMATIONS")){
//                loadChild(ADD);
            } else
                loadChild(ByDefalutMessage);

            ParentListItems.put(HoldItem, ChildList);
        }

        expandablelistView = (ExpandableListView) findViewById(R.id.activity_expandable_list_view);
        //final ExpandableList expListAdapter = new ()//new ExpandableList(this, ParentList, ParentListItems);
        expListAdapter = new ExpandableList(this, ParentList, ParentListItems);
        expandablelistView.setAdapter(expListAdapter);
        expandablelistView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub

                final String selected = (String) expListAdapter.getChild(groupPosition, childPosition);

                // Switch case to open selected child element activity on child element selection.

                Intent intent;
                switch (selected) {
                    case "introduction":
                        intent = new Intent(ProfileActivity.this, Prof_Introduction.class);
                        startActivity(intent);
                        break;

                    case "Work Experience":
                        intent = new Intent(ProfileActivity.this, Prof_WorkEx.class);
                        startActivity(intent);
                        break;

                    case "Education":
                        intent = new Intent(ProfileActivity.this, Prof_Education.class);
                        startActivity(intent);
                        break;

                    case "Volunteer Experience":
                        intent = new Intent(ProfileActivity.this, Prof_VolunteerExp.class);
                        startActivity(intent);
                        break;

                    case "Skills":
                        intent = new Intent(ProfileActivity.this, Prof_Skills.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });


        userId = Integer.toString(AppPrefManager.getUserId(ProfileActivity.this));
        Log.d("useridfromprofile1",userId);

        new MyAsync().execute();

    }


    //**************22-03-2017**********************************************************
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            //cirleImg.setImageBitmap(myBitmap);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    public class MyAsync extends AsyncTask<Void, Void, Bitmap>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog.setMessage("Please Wait ...");
            showDialog();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            try {
                URL url = new URL("http://www.netibiz.com/alumni/imageupload/"+userId+".jpg");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            super.onPostExecute(bmp);
            hideDialog();
            Bitmap bm = bmp;
            cirleImg.setImageBitmap(bm);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadChild(String[] ParentElementsName) {
        ChildList = new ArrayList<String>();
        for (String model : ParentElementsName)
            ChildList.add(model);
    }

    /**
     * Opens dialog picker, so the user can select image from the gallery. The
     * result is returned in the method <code>onActivityResult()</code>
     */

    private void showFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


        final CharSequence[] items = { "Take Photo", "Choose from Gallery", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(ProfileActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask ="Choose from Gallery";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }


    /**
     * Retrives the result returned from selecting image, by invoking the method
     * <code>selectImageFromGallery()</code>
     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
//                && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//
//            decodeFile(picturePath);
//
//        }
//    }

    /**
     * The method decodes the image file to avoid out of memory issues. Sets the
     * selected image in to the ImageView.
     *
     * @param filePath
     */
//    public void decodeFile(String filePath) {
//        // Decode image size
//        BitmapFactory.Options o = new BitmapFactory.Options();
//        o.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filePath, o);
//
//        // The new size we want to scale to
//        final int REQUIRED_SIZE = 1024;
//
//        // Find the correct scale value. It should be the power of 2.
//        int width_tmp = o.outWidth, height_tmp = o.outHeight;
//        int scale = 1;
//        while (true) {
//            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
//                break;
//            width_tmp /= 2;
//            height_tmp /= 2;
//            scale *= 2;
//        }
//
//        // Decode with inSampleSize
//        BitmapFactory.Options o2 = new BitmapFactory.Options();
//        o2.inSampleSize = scale;
//        bitmap = BitmapFactory.decodeFile(filePath, o2);
//
//        image.setImageBitmap(bitmap);
//    }

    /**
     * The class connects with server and uploads the photo
     *
     *
     */


//    class ImageUploadTask extends AsyncTask<Void, Void, String> {
//        private String webAddressToPost = "http://your-website-here.com";
//
//        // private ProgressDialog dialog;
//        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
//
//        @Override
//        protected void onPreExecute() {
//            dialog.setMessage("Uploading...");
//            dialog.show();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            try {
//                HttpClient httpClient = new DefaultHttpClient();
//                HttpContext localContext = new BasicHttpContext();
//                HttpPost httpPost = new HttpPost(webAddressToPost);
//
//                MultipartEntity entity = new MultipartEntity(
//                        HttpMultipartMode.BROWSER_COMPATIBLE);
//
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                bitmap.compress(CompressFormat.JPEG, 100, bos);
//                byte[] data = bos.toByteArray();
//                String file = Base64.encodeBytes(data);
//                entity.addPart("uploaded", new StringBody(file));
//
//                entity.addPart("someOtherStringToSend", new StringBody("your string here"));
//
//                httpPost.setEntity(entity);
//                HttpResponse response = httpClient.execute(httpPost,
//                        localContext);
//                BufferedReader reader = new BufferedReader(
//                        new InputStreamReader(
//                                response.getEntity().getContent(), "UTF-8"));
//
//                String sResponse = reader.readLine();
//                return sResponse;
//            } catch (Exception e) {
//                // something went wrong. connection with the server error
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            dialog.dismiss();
//            Toast.makeText(getApplicationContext(), "file uploaded",
//                    Toast.LENGTH_LONG).show();
//        }
//
//    }


//        @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri filePath = data.getData();
//            try {
//                //Getting the Bitmap from Gallery
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                //Setting the Bitmap to ImageView
//                cirleImg.setImageBitmap(bitmap);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton:
                //showFileChooser();
                CropImage.startPickImageActivity(ProfileActivity.this);

                break;
            case R.id.img:
                //new ImageUploadTask().execute();
                //uploadImage();
                uploadMultipart();
                //upload();

                String myBase64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
                AppPrefManager.setPrefProfileDp(ProfileActivity.this, myBase64Image);

                // convert string to bitmap
                Bitmap myBitmapAgain = decodeBase64(AppPrefManager.getPrefProfileDp(ProfileActivity.this));
                cirleImg.setImageBitmap(myBitmapAgain);
                Intent i = new Intent(this,Profile_ok.class);
                startActivity(i);
                break;
        }
    }

    //*****************************************************************

//    class ImageUploadTask extends AsyncTask<Void, Void, String> {
//        private String webAddressToPost = "http://www.netibiz.com/alumni/";
//        // private ProgressDialog dialog;
//        private ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);
//
//        @Override
//        protected void onPreExecute() {
//            dialog.setMessage("Uploading...");
//            dialog.show();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            try {
//                URL url = new URL(webAddressToPost);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Connection", "Keep-Alive");
//
//                MultipartEntity entity = new MultipartEntity(
//                        HttpMultipartMode.BROWSER_COMPATIBLE);
//
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                byte[] data = bos.toByteArray();
//                ByteArrayBody bab = new ByteArrayBody(data, "test.jpg");
//                entity.addPart("file", bab);
//
//                entity.addPart("someOtherStringToSend", new StringBody("your string here"));
//
//                conn.addRequestProperty("Content-length", entity.getContentLength() + "");
//                conn.addRequestProperty(entity.getContentType().getName(), entity.getContentType().getValue());
//
//                OutputStream os = conn.getOutputStream();
//                entity.writeTo(conn.getOutputStream());
//                os.close();
//                conn.connect();
//
//
//                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    return readStream(conn.getInputStream());
//                }
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                // something went wrong. connection with the server error
//            }
//            return null;
//        }
//
//
//        private String readStream(InputStream in) {
//            BufferedReader reader = null;
//            StringBuilder builder = new StringBuilder();
//            try {
//                reader = new BufferedReader(new InputStreamReader(in));
//                String line = "";
//                while ((line = reader.readLine()) != null) {
//                    builder.append(line);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return builder.toString();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            dialog.dismiss();
//            cirleImg.setImageBitmap(bitmap);
//            Toast.makeText(getApplicationContext(), "file uploaded",
//                    Toast.LENGTH_LONG).show();
//        }
//
//    }

    //*****************************************************************

    //*****************20-03*********************

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void uploadImage(){
        //Showing the progress dialog
        final String path = getPath(filePath);
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_UPLOAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Disimissing the progress dialog
                loading.dismiss();
                //Showing toast message of the response
                Toast.makeText(ProfileActivity.this, s , Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(ProfileActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                //String image = getStringImage(bitmap);
                String image = getStringImage(bitmap);
                //userId = Integer.toString(AppPrefManager.getUserId(ProfileActivity.this));
                Log.d("useridfromprofile",userId);

                //Getting Image Name
                //String name = editTextName.getText().toString().trim();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_UID, userId);
                params.put("image",path );

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }



//    public class uploadToServer extends AsyncTask<Void, Void, String> {
//
//        private ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Wait image uploading!");
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            //getting name for the image
//            //String name = editText.getText().toString().trim();
//            Log.d("useridfromprofile2",userId);
//
//            //getting the actual path of the image
//            String path = getPath(filePath);
//
//            //Uploading code
//            try {
//                String uploadId = UUID.randomUUID().toString();
//
//                //Creating a multi part request
//                new MultipartUploadRequest(ProfileActivity.this, uploadId, AppConfig.URL_UPLOAD)
//                        .addParameter("uid", userId) //Adding text parameter to the request
//                        .addFileToUpload(path, "image") //Adding file
//                        .setNotificationConfig(new UploadNotificationConfig())
//                        .setMaxRetries(2)
//                        .startUpload(); //Starting the upload
//
//            } catch (Exception exc) {
//                Toast.makeText(ProfileActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//            return "Success";
//        }
//
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.hide();
//            pd.dismiss();
//        }
//    }

    public void uploadMultipart() {
        //getting name for the image
        //String name = editText.getText().toString().trim();
        Log.d("useridfromprofile2",userId);

        //getting the actual path of the image
        //String path = getPath(filePath);
        Log.d("filepath_from_upload",path);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, AppConfig.URL_UPLOAD)
                    .addParameter("uid", userId) //Adding text parameter to the request
                    .addFileToUpload(path, "image") //Adding file
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //handling the image chooser activity result
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            filePath = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                cirleImg.setImageBitmap(bitmap);
//
////                //convert bitmap to string and store to shared pref
////                String myBase64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
////                AppPrefManager.setPrefProfileDp(ProfileActivity.this, myBase64Image);
////
////                // convert string to bitmap
////                Bitmap myBitmapAgain = decodeBase64(AppPrefManager.getPrefProfileDp(ProfileActivity.this));
////                cirleImg.setImageBitmap(myBitmapAgain);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == PICK_IMAGE_REQUEST)
//                onSelectFromGalleryResult(data);
//            else if (requestCode == REQUEST_CAMERA)
//                onCaptureImageResult(data);
//        }
//    }


//    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        switch(requestCode) {
//
//            case 0:
//
////                if (resultCode==RESULT_OK)
////                {
////                    thumbnail = (Bitmap) imageReturnedIntent.getExtras().get("data");
////                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
////                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
////                    File destination = new File(Environment.getExternalStorageDirectory(),"temp.jpg");
////                    FileOutputStream fo;
////                    try {
////                        fo = new FileOutputStream(destination);
////                        fo.write(bytes.toByteArray());
////                        fo.close();
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////
////                    new uploadToServerTask().execute();
////                }
//
//                if (resultCode == RESULT_OK) {
//
//                    try {
//                        filePath = imageReturnedIntent.getData();
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                        cirleImg.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//
//                    //filePath = imageReturnedIntent.getData();
//                    //photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
//
////                    // Cursor to get image uri to display
////
////                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
////                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
////                    cursor.moveToFirst();
////                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
////                    picturePath = cursor.getString(columnIndex);
////                    cursor.close();
////                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
////                    cirleImg.setImageBitmap(photo);
//                }
//
//                //Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//                break;
//
//            case 1:
//                if(resultCode == RESULT_OK){
//                    try {
//                        filePath = imageReturnedIntent.getData();
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                        cirleImg.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
//        }
//    }




    private void upload() {
        // Image location URL
        Log.e("path", "----------------" + picturePath);

        // Image
        Bitmap bm = BitmapFactory.decodeFile(picturePath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        String ba1 = Base64.encodeToString(ba, Base64.NO_WRAP);

        Log.e("base64", "-----" + ba1);

        // Upload image to server
        new uploadToServer().execute();

    }
    public class uploadToServer extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait image uploading!");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

//            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("base64", ba1));
//            nameValuePairs.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(URL);
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                HttpResponse response = httpclient.execute(httppost);
//                String st = EntityUtils.toString(response.getEntity());
//                Log.v("log_tag", "In the try Loop" + st);
//
//            } catch (Exception e) {
//                Log.v("log_tag", "Error in http connection " + e.toString());
//            }

            //String path = getPath(filePath);

            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(ProfileActivity.this, uploadId, AppConfig.URL_UPLOAD)
                        .addParameter("uid", userId) //Adding text parameter to the request
                        .addFileToUpload(picturePath, "image") //Adding file
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(ProfileActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }

            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }



    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

        //Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
    }

    private void cameraIntent()
    {
        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intent, REQUEST_CAMERA);

        //Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(takePicture, 0);//zero can be replaced with any action code

//        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
//        startActivityForResult(takePicture, 0);
//**************************************************************************************************


        // Check Camera
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // Open default camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);

            // start the image capture Intent
            startActivityForResult(intent, 0);

        } else {
            Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cirleImg.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        filePath = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                cirleImg.setImageBitmap(bitmap);
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        cirleImg.setImageBitmap(bm);
    }



//**************************************************************

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }


//    //Requesting permission
//    private void requestStoragePermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//            return;
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            //If the user has denied the permission previously your code will come to this block
//            //Here you can explain why you need this permission
//            //Explain here why you need this permission
//        }
//        //And finally ask for the permission
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//    }
//
//
//    //This method will be called when the user will tap on allow or deny
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        //Checking the request code of our request
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//
//            //If permission is granted
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //Displaying a toast
//                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
//            } else {
//                //Displaying another toast if permission is not granted
//                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
//            }
//        }
//    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    /*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageUri = data.getData();

                if (null != selectedImageUri) {

                    // Saving to Database...
                    if (saveImageInDB(selectedImageUri)) {
                        //showMessage("Image Saved in Database...");
                        Toast.makeText(this, "to DB", Toast.LENGTH_SHORT).show();
                        cirleImg.setImageURI(selectedImageUri);
                    }

                    // Reading from Database after 3 seconds just to show the message
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (loadImageFromDB()) {
                                // showMessage("Image Loaded from Database...");
                                Toast.makeText(ProfileActivity.this, "from DB", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 3000);
                }
            }
        }
    }


     //Save the
    Boolean saveImageInDB(Uri selectedImageUri) {

        try {
            dbHelper.open();
            InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
            byte[] inputData = Utils.getBytes(iStream);
            dbHelper.insertImage(inputData);
            dbHelper.close();
            return true;
        } catch (IOException ioe) {
            //Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
            dbHelper.close();
            return false;
        }

    }

    Boolean loadImageFromDB() {
        try {
            dbHelper.open();
            byte[] bytes = dbHelper.retreiveImageFromDB();
            dbHelper.close();
            // Show Image from DB in ImageView
            cirleImg.setImageBitmap(Utils.getImage(bytes));
            return true;
        } catch (Exception e) {
            // Log.e(TAG, "<loadImageFromDB> Error : " + e.getLocalizedMessage());
            dbHelper.close();
            return false;
        }
    } */




//    @SuppressLint("NewApi")
//    public void onSelectImageClick(View view) {
//        if (CropImage.isExplicitCameraPermissionRequired(this)) {
//            requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
//        } else {
//            CropImage.startPickImageActivity(this);
//        }
//    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            boolean requirePermissions = false;
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //cirleImg.setImageURI(result.getUri());
                //ivProfileDp.setImageURI(result.getUri());

                path =  result.getUri().getPath();

                Log.d(">>>>>>>>>>>>>", result.getUri().getPath());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inDither = true;
                bitmap = BitmapFactory.decodeFile(result.getUri().getPath(), options);
                cirleImg.setImageBitmap(bitmap);
                //btUpdate.setVisibility(View.VISIBLE);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }
}









//**********************with_simple_popup********************************

//package com.example.user.alumni.activity;
//
//        import android.Manifest;
//        import android.app.Activity;
//        import android.app.ProgressDialog;
//        import android.content.DialogInterface;
//        import android.content.pm.PackageManager;
//        import android.database.Cursor;
//        import android.graphics.BitmapFactory;
//        import android.os.AsyncTask;
//        import android.os.Environment;
//        import android.support.annotation.NonNull;
//        import android.support.v4.app.ActivityCompat;
//        import android.support.v4.content.ContextCompat;
//        import android.support.v7.app.AlertDialog;
//        import android.support.v7.app.AppCompatActivity;
//        import android.os.Bundle;
//
//        import java.io.BufferedReader;
//        import java.io.ByteArrayOutputStream;
//        import java.io.DataOutputStream;
//        import java.io.File;
//        import java.io.FileInputStream;
//        import java.io.FileNotFoundException;
//        import java.io.FileOutputStream;
//        import java.io.IOException;
//        import java.io.InputStream;
//        import java.io.InputStreamReader;
//        import java.io.OutputStream;
//        import java.net.HttpURLConnection;
//        import java.net.MalformedURLException;
//        import java.net.URL;
//        import java.net.URLConnection;
//        import java.util.ArrayList;
//        import java.util.Date;
//        import java.util.HashMap;
//        import java.util.Hashtable;
//        import java.util.LinkedHashMap;
//        import java.util.List;
//        import java.util.Map;
//        import java.util.UUID;
//
//        import android.content.Intent;
//        import android.graphics.Bitmap;
//        import android.net.Uri;
//        import android.os.Bundle;
//        import android.os.Handler;
//        import android.provider.MediaStore;
//        import android.support.v7.app.AppCompatActivity;
//        import android.util.Base64;
//        import android.util.Log;
//        import android.view.MenuItem;
//        import android.view.View;
//        import android.widget.Button;
//        import android.widget.ExpandableListView;
//        import android.widget.ExpandableListView.OnChildClickListener;
//        import android.widget.ImageButton;
//        import android.widget.Toast;
//
//        import com.android.volley.AuthFailureError;
//        import com.android.volley.DefaultRetryPolicy;
//        import com.android.volley.Request;
//        import com.android.volley.RequestQueue;
//        import com.android.volley.Response;
//        import com.android.volley.RetryPolicy;
//        import com.android.volley.VolleyError;
//        import com.android.volley.toolbox.StringRequest;
//        import com.android.volley.toolbox.Volley;
//        import com.bumptech.glide.Glide;
//        import com.example.user.alumni.R;
//        import com.example.user.alumni.Utility;
//        import com.example.user.alumni.adapter.ExpandableList;
//        import com.example.user.alumni.app.AppConfig;
//        import com.example.user.alumni.app.AppController;
//        import com.github.siyamed.shapeimageview.CircularImageView;
//        import com.squareup.picasso.Callback;
//        import com.squareup.picasso.Picasso;
//
//        import net.gotev.uploadservice.MultipartUploadRequest;
//        import net.gotev.uploadservice.UploadNotificationConfig;
//
//        import org.apache.http.entity.mime.HttpMultipartMode;
//        import org.apache.http.entity.mime.MultipartEntity;
//        import org.apache.http.entity.mime.content.ByteArrayBody;
//        import org.apache.http.entity.mime.content.StringBody;
//        import org.json.JSONException;
//        import org.json.JSONObject;
//
//        import de.hdodenhof.circleimageview.CircleImageView;
//
//        import static com.example.user.alumni.R.id.home;
//        import static com.example.user.alumni.R.id.imageButton;
////import com.github.siyamed.shapeimageview.CircularImageView;
//
////import de.hdodenhof.circleimageview.CircleImageView;
//
//public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private static final int PICK_IMAGE = 1;
//    private static final int CAPTURE_IMAGE = 2;
//    private static final int MY_SOCKET_TIMEOUT_MS = 20000 ;
//    private static final int STORAGE_PERMISSION_CODE = 123 ;
//    private static int RESULT_LOAD_IMAGE = 1;
//    private static int SD_REQUEST = 202;
//    private static final int SELECT_PICTURE = 100;
//    private ProgressDialog pDialog;
//
//    //********************************************
//    private int PICK_IMAGE_REQUEST = 1;
//    private int REQUEST_CAMERA = 0;
//    private String userChoosenTask;
//    private String KEY_IMAGE = "image";
//    private String KEY_UID = "id";
//    private Uri filePath;
//    //Uri selectedImage;
//    //Bitmap photo;
//    String picturePath;
////**********************************************
//
//    private String selectedImagePath = "";
//    private String imgPath;
//    private Bitmap bitmap;
//    private String userId;
//    ImageButton imb;
//    private Button upload;
//    private CircularImageView cirleImg;
//    ExpandableList expListAdapter;
//    List<String> ChildList;
//    Map<String, List<String>> ParentListItems;
//    ExpandableListView expandablelistView;
//
//    List<String> ParentList = new ArrayList<String>();
//
//    {
//        ParentList.add("INTRODUCTION");
//        ParentList.add("BACKGROUND");
//        ParentList.add("SKILLS");
////        ParentList.add("ACCOMPLISHMENTS");
////        ParentList.add("ADDITIONAL INFORMATIONS");
//    }
//
//    // Assign children list element using string array.
//    String[] INTRO = {"introduction"};
//    String[] BACKGND = {"Work Experience", "Education", "Volunteer Experience"};
//    String[] SKIL = {"Skills"};
//    //    String[] ACCOM = { "Certifications","Courses","Projects","Honors & Awards","Publications","Patents" };
////    String[] ADD = { "Topics for contacting" };
//    String[] ByDefalutMessage = {"Items Loading"};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        requestStoragePermission();
//        imb = (ImageButton) findViewById(imageButton);
//        imb.setOnClickListener(this);
//        pDialog = new ProgressDialog(this);
//        pDialog.setCancelable(false);
//        cirleImg = (CircularImageView) findViewById(R.id.cirle_img);
//        upload = (Button) findViewById(R.id.img);
//        upload.setOnClickListener(this);
//        ParentListItems = new LinkedHashMap<String, List<String>>();
//
//        for (String HoldItem : ParentList) {
//            if (HoldItem.equals("INTRODUCTION")) {
//                loadChild(INTRO);
//            } else if (HoldItem.equals("BACKGROUND")) {
//                loadChild(BACKGND);
//            } else if (HoldItem.equals("SKILLS")) {
//                loadChild(SKIL);
////            }else if (HoldItem.equals("ACCOMPLISHMENTS")){
////                loadChild(ACCOM);
////            }else if (HoldItem.equals("ADDITIONAL INFORMATIONS")){
////                loadChild(ADD);
//            } else
//                loadChild(ByDefalutMessage);
//
//            ParentListItems.put(HoldItem, ChildList);
//        }
//
//        expandablelistView = (ExpandableListView) findViewById(R.id.activity_expandable_list_view);
//        //final ExpandableList expListAdapter = new ()//new ExpandableList(this, ParentList, ParentListItems);
//        expListAdapter = new ExpandableList(this, ParentList, ParentListItems);
//        expandablelistView.setAdapter(expListAdapter);
//        expandablelistView.setOnChildClickListener(new OnChildClickListener() {
//
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v,
//                                        int groupPosition, int childPosition, long id) {
//                // TODO Auto-generated method stub
//
//                final String selected = (String) expListAdapter.getChild(groupPosition, childPosition);
//
//                // Switch case to open selected child element activity on child element selection.
//
//                Intent intent;
//                switch (selected) {
//                    case "introduction":
//                        intent = new Intent(ProfileActivity.this, Prof_Introduction.class);
//                        startActivity(intent);
//                        break;
//
//                    case "Work Experience":
//                        intent = new Intent(ProfileActivity.this, Prof_WorkEx.class);
//                        startActivity(intent);
//                        break;
//
//                    case "Education":
//                        intent = new Intent(ProfileActivity.this, Prof_Education.class);
//                        startActivity(intent);
//                        break;
//
//                    case "Volunteer Experience":
//                        intent = new Intent(ProfileActivity.this, Prof_VolunteerExp.class);
//                        startActivity(intent);
//                        break;
//
//                    case "Skills":
//                        intent = new Intent(ProfileActivity.this, Prof_Skills.class);
//                        startActivity(intent);
//                        break;
//                }
//                return true;
//            }
//        });
//
//
//        userId = Integer.toString(AppPrefManager.getUserId(ProfileActivity.this));
//        Log.d("useridfromprofile1",userId);
//
//        new MyAsync().execute();
//
//    }
//
//
//    //**************22-03-2017**********************************************************
//    public static Bitmap getBitmapFromURL(String src) {
//        try {
//            URL url = new URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            //cirleImg.setImageBitmap(myBitmap);
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//
//    public class MyAsync extends AsyncTask<Void, Void, Bitmap>{
//
//        @Override
//        protected void onPreExecute(){
//            super.onPreExecute();
//            pDialog.setMessage("Please Wait ...");
//            showDialog();
//        }
//
//        @Override
//        protected Bitmap doInBackground(Void... params) {
//
//            try {
//                URL url = new URL("http://www.netibiz.com/alumni/imageupload/"+userId+".jpg");
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                InputStream input = connection.getInputStream();
//                Bitmap myBitmap = BitmapFactory.decodeStream(input);
//                return myBitmap;
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bmp) {
//            super.onPostExecute(bmp);
//            hideDialog();
//            Bitmap bm = bmp;
//            cirleImg.setImageBitmap(bm);
//        }
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//                this.finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    private void loadChild(String[] ParentElementsName) {
//        ChildList = new ArrayList<String>();
//        for (String model : ParentElementsName)
//            ChildList.add(model);
//    }
//
//    /**
//     * Opens dialog picker, so the user can select image from the gallery. The
//     * result is returned in the method <code>onActivityResult()</code>
//     */
//
//    private void showFileChooser() {
////        Intent intent = new Intent();
////        intent.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
////        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//
//
//        final CharSequence[] items = { "Take Photo", "Choose from Gallery", "Cancel" };
//        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
//        builder.setTitle("Add Photo!");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                boolean result= Utility.checkPermission(ProfileActivity.this);
//
//                if (items[item].equals("Take Photo")) {
//                    userChoosenTask ="Take Photo";
//                    if(result)
//                        cameraIntent();
//
//                } else if (items[item].equals("Choose from Gallery")) {
//                    userChoosenTask ="Choose from Gallery";
//                    if(result)
//                        galleryIntent();
//
//                } else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//
//    }
//
//
//    /**
//     * Retrives the result returned from selecting image, by invoking the method
//     * <code>selectImageFromGallery()</code>
//     */
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////
////        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
////                && null != data) {
////            Uri selectedImage = data.getData();
////            String[] filePathColumn = { MediaStore.Images.Media.DATA };
////
////            Cursor cursor = getContentResolver().query(selectedImage,
////                    filePathColumn, null, null, null);
////            cursor.moveToFirst();
////
////            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
////            String picturePath = cursor.getString(columnIndex);
////            cursor.close();
////
////            decodeFile(picturePath);
////
////        }
////    }
//
//    /**
//     * The method decodes the image file to avoid out of memory issues. Sets the
//     * selected image in to the ImageView.
//     *
//     * @param filePath
//     */
////    public void decodeFile(String filePath) {
////        // Decode image size
////        BitmapFactory.Options o = new BitmapFactory.Options();
////        o.inJustDecodeBounds = true;
////        BitmapFactory.decodeFile(filePath, o);
////
////        // The new size we want to scale to
////        final int REQUIRED_SIZE = 1024;
////
////        // Find the correct scale value. It should be the power of 2.
////        int width_tmp = o.outWidth, height_tmp = o.outHeight;
////        int scale = 1;
////        while (true) {
////            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
////                break;
////            width_tmp /= 2;
////            height_tmp /= 2;
////            scale *= 2;
////        }
////
////        // Decode with inSampleSize
////        BitmapFactory.Options o2 = new BitmapFactory.Options();
////        o2.inSampleSize = scale;
////        bitmap = BitmapFactory.decodeFile(filePath, o2);
////
////        image.setImageBitmap(bitmap);
////    }
//
//    /**
//     * The class connects with server and uploads the photo
//     *
//     *
//     */
//
//
////    class ImageUploadTask extends AsyncTask<Void, Void, String> {
////        private String webAddressToPost = "http://your-website-here.com";
////
////        // private ProgressDialog dialog;
////        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
////
////        @Override
////        protected void onPreExecute() {
////            dialog.setMessage("Uploading...");
////            dialog.show();
////        }
////
////        @Override
////        protected String doInBackground(Void... params) {
////            try {
////                HttpClient httpClient = new DefaultHttpClient();
////                HttpContext localContext = new BasicHttpContext();
////                HttpPost httpPost = new HttpPost(webAddressToPost);
////
////                MultipartEntity entity = new MultipartEntity(
////                        HttpMultipartMode.BROWSER_COMPATIBLE);
////
////                ByteArrayOutputStream bos = new ByteArrayOutputStream();
////                bitmap.compress(CompressFormat.JPEG, 100, bos);
////                byte[] data = bos.toByteArray();
////                String file = Base64.encodeBytes(data);
////                entity.addPart("uploaded", new StringBody(file));
////
////                entity.addPart("someOtherStringToSend", new StringBody("your string here"));
////
////                httpPost.setEntity(entity);
////                HttpResponse response = httpClient.execute(httpPost,
////                        localContext);
////                BufferedReader reader = new BufferedReader(
////                        new InputStreamReader(
////                                response.getEntity().getContent(), "UTF-8"));
////
////                String sResponse = reader.readLine();
////                return sResponse;
////            } catch (Exception e) {
////                // something went wrong. connection with the server error
////            }
////            return null;
////        }
////
////        @Override
////        protected void onPostExecute(String result) {
////            dialog.dismiss();
////            Toast.makeText(getApplicationContext(), "file uploaded",
////                    Toast.LENGTH_LONG).show();
////        }
////
////    }
//
//
////        @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////
////        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
////            Uri filePath = data.getData();
////            try {
////                //Getting the Bitmap from Gallery
////                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
////                //Setting the Bitmap to ImageView
////                cirleImg.setImageBitmap(bitmap);
////
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        }
////    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case imageButton:
//                showFileChooser();
//
//                break;
//            case R.id.img:
//                //new ImageUploadTask().execute();
//                //uploadImage();
//                uploadMultipart();
//                //upload();
//
//                String myBase64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
//                AppPrefManager.setPrefProfileDp(ProfileActivity.this, myBase64Image);
//
//                // convert string to bitmap
//                Bitmap myBitmapAgain = decodeBase64(AppPrefManager.getPrefProfileDp(ProfileActivity.this));
//                cirleImg.setImageBitmap(myBitmapAgain);
//                break;
//        }
//    }
//
//    //*****************************************************************
//
////    class ImageUploadTask extends AsyncTask<Void, Void, String> {
////        private String webAddressToPost = "http://www.netibiz.com/alumni/";
////        // private ProgressDialog dialog;
////        private ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);
////
////        @Override
////        protected void onPreExecute() {
////            dialog.setMessage("Uploading...");
////            dialog.show();
////        }
////
////        @Override
////        protected String doInBackground(Void... params) {
////            try {
////                URL url = new URL(webAddressToPost);
////                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
////                conn.setRequestMethod("POST");
////                conn.setRequestProperty("Connection", "Keep-Alive");
////
////                MultipartEntity entity = new MultipartEntity(
////                        HttpMultipartMode.BROWSER_COMPATIBLE);
////
////                ByteArrayOutputStream bos = new ByteArrayOutputStream();
////                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
////                byte[] data = bos.toByteArray();
////                ByteArrayBody bab = new ByteArrayBody(data, "test.jpg");
////                entity.addPart("file", bab);
////
////                entity.addPart("someOtherStringToSend", new StringBody("your string here"));
////
////                conn.addRequestProperty("Content-length", entity.getContentLength() + "");
////                conn.addRequestProperty(entity.getContentType().getName(), entity.getContentType().getValue());
////
////                OutputStream os = conn.getOutputStream();
////                entity.writeTo(conn.getOutputStream());
////                os.close();
////                conn.connect();
////
////
////                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
////                    return readStream(conn.getInputStream());
////                }
////
////
////            } catch (Exception e) {
////                e.printStackTrace();
////                // something went wrong. connection with the server error
////            }
////            return null;
////        }
////
////
////        private String readStream(InputStream in) {
////            BufferedReader reader = null;
////            StringBuilder builder = new StringBuilder();
////            try {
////                reader = new BufferedReader(new InputStreamReader(in));
////                String line = "";
////                while ((line = reader.readLine()) != null) {
////                    builder.append(line);
////                }
////            } catch (IOException e) {
////                e.printStackTrace();
////            } finally {
////                if (reader != null) {
////                    try {
////                        reader.close();
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }
////            }
////            return builder.toString();
////        }
////
////        @Override
////        protected void onPostExecute(String result) {
////            dialog.dismiss();
////            cirleImg.setImageBitmap(bitmap);
////            Toast.makeText(getApplicationContext(), "file uploaded",
////                    Toast.LENGTH_LONG).show();
////        }
////
////    }
//
//    //*****************************************************************
//
//    //*****************20-03*********************
//
//    public String getStringImage(Bitmap bmp){
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] imageBytes = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        return encodedImage;
//    }
//
//
//    private void uploadImage(){
//        //Showing the progress dialog
//        final String path = getPath(filePath);
//        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_UPLOAD, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                //Disimissing the progress dialog
//                loading.dismiss();
//                //Showing toast message of the response
//                Toast.makeText(ProfileActivity.this, s , Toast.LENGTH_LONG).show();
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        //Dismissing the progress dialog
//                        loading.dismiss();
//
//                        //Showing toast
//                        Toast.makeText(ProfileActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                //Converting Bitmap to String
//                //String image = getStringImage(bitmap);
//                String image = getStringImage(bitmap);
//                //userId = Integer.toString(AppPrefManager.getUserId(ProfileActivity.this));
//                Log.d("useridfromprofile",userId);
//
//                //Getting Image Name
//                //String name = editTextName.getText().toString().trim();
//
//                //Creating parameters
//                Map<String,String> params = new Hashtable<String, String>();
//
//                //Adding parameters
//                params.put(KEY_UID, userId);
//                params.put("image",path );
//
//                //returning parameters
//                return params;
//            }
//        };
//
//        //Creating a Request Queue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        //Adding request to the queue
//        requestQueue.add(stringRequest);
//    }
//
//
//
////    public class uploadToServer extends AsyncTask<Void, Void, String> {
////
////        private ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
////
////        protected void onPreExecute() {
////            super.onPreExecute();
////            pd.setMessage("Wait image uploading!");
////            pd.show();
////        }
////
////        @Override
////        protected String doInBackground(Void... params) {
////
////            //getting name for the image
////            //String name = editText.getText().toString().trim();
////            Log.d("useridfromprofile2",userId);
////
////            //getting the actual path of the image
////            String path = getPath(filePath);
////
////            //Uploading code
////            try {
////                String uploadId = UUID.randomUUID().toString();
////
////                //Creating a multi part request
////                new MultipartUploadRequest(ProfileActivity.this, uploadId, AppConfig.URL_UPLOAD)
////                        .addParameter("uid", userId) //Adding text parameter to the request
////                        .addFileToUpload(path, "image") //Adding file
////                        .setNotificationConfig(new UploadNotificationConfig())
////                        .setMaxRetries(2)
////                        .startUpload(); //Starting the upload
////
////            } catch (Exception exc) {
////                Toast.makeText(ProfileActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
////            }
////            return "Success";
////        }
////
////        protected void onPostExecute(String result) {
////            super.onPostExecute(result);
////            pd.hide();
////            pd.dismiss();
////        }
////    }
//
//    public void uploadMultipart() {
//        //getting name for the image
//        //String name = editText.getText().toString().trim();
//        Log.d("useridfromprofile2",userId);
//
//        //getting the actual path of the image
//        String path = getPath(filePath);
//
//        Log.d("filepath_from_upload",path);
//
//        //Uploading code
//        try {
//            String uploadId = UUID.randomUUID().toString();
//
//            //Creating a multi part request
//            new MultipartUploadRequest(this, uploadId, AppConfig.URL_UPLOAD)
//                    .addParameter("uid", userId) //Adding text parameter to the request
//                    .addFileToUpload(path, "image") //Adding file
//                    .setNotificationConfig(new UploadNotificationConfig())
//                    .setMaxRetries(2)
//                    .startUpload(); //Starting the upload
//
//        } catch (Exception exc) {
//            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    //handling the image chooser activity result
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////
////        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
////            filePath = data.getData();
////            try {
////                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
////                cirleImg.setImageBitmap(bitmap);
////
//////                //convert bitmap to string and store to shared pref
//////                String myBase64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
//////                AppPrefManager.setPrefProfileDp(ProfileActivity.this, myBase64Image);
//////
//////                // convert string to bitmap
//////                Bitmap myBitmapAgain = decodeBase64(AppPrefManager.getPrefProfileDp(ProfileActivity.this));
//////                cirleImg.setImageBitmap(myBitmapAgain);
////
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        }
////    }
//
//
//
////    @Override
////    public void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////
////        if (resultCode == Activity.RESULT_OK) {
////            if (requestCode == PICK_IMAGE_REQUEST)
////                onSelectFromGalleryResult(data);
////            else if (requestCode == REQUEST_CAMERA)
////                onCaptureImageResult(data);
////        }
////    }
//
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        switch(requestCode) {
//
//            case 0:
//
////                if (resultCode==RESULT_OK)
////                {
////                    thumbnail = (Bitmap) imageReturnedIntent.getExtras().get("data");
////                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
////                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
////                    File destination = new File(Environment.getExternalStorageDirectory(),"temp.jpg");
////                    FileOutputStream fo;
////                    try {
////                        fo = new FileOutputStream(destination);
////                        fo.write(bytes.toByteArray());
////                        fo.close();
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////
////                    new uploadToServerTask().execute();
////                }
//
//                if (resultCode == RESULT_OK) {
//
//                    try {
//                        filePath = imageReturnedIntent.getData();
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                        cirleImg.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//
//                    //filePath = imageReturnedIntent.getData();
//                    //photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
//
////                    // Cursor to get image uri to display
////
////                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
////                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
////                    cursor.moveToFirst();
////                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
////                    picturePath = cursor.getString(columnIndex);
////                    cursor.close();
////                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
////                    cirleImg.setImageBitmap(photo);
//                }
//
//                //Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//                break;
//
//            case 1:
//                if(resultCode == RESULT_OK){
//                    try {
//                        filePath = imageReturnedIntent.getData();
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                        cirleImg.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
//        }
//    }
//
//
//
//
//    private void upload() {
//        // Image location URL
//        Log.e("path", "----------------" + picturePath);
//
//        // Image
//        Bitmap bm = BitmapFactory.decodeFile(picturePath);
//        ByteArrayOutputStream bao = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
//        byte[] ba = bao.toByteArray();
//        String ba1 = Base64.encodeToString(ba, Base64.NO_WRAP);
//
//        Log.e("base64", "-----" + ba1);
//
//        // Upload image to server
//        new uploadToServer().execute();
//
//    }
//    public class uploadToServer extends AsyncTask<Void, Void, String> {
//
//        private ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Wait image uploading!");
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
////            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
////            nameValuePairs.add(new BasicNameValuePair("base64", ba1));
////            nameValuePairs.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));
////            try {
////                HttpClient httpclient = new DefaultHttpClient();
////                HttpPost httppost = new HttpPost(URL);
////                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
////                HttpResponse response = httpclient.execute(httppost);
////                String st = EntityUtils.toString(response.getEntity());
////                Log.v("log_tag", "In the try Loop" + st);
////
////            } catch (Exception e) {
////                Log.v("log_tag", "Error in http connection " + e.toString());
////            }
//
//            //String path = getPath(filePath);
//
//            //Uploading code
//            try {
//                String uploadId = UUID.randomUUID().toString();
//
//                //Creating a multi part request
//                new MultipartUploadRequest(ProfileActivity.this, uploadId, AppConfig.URL_UPLOAD)
//                        .addParameter("uid", userId) //Adding text parameter to the request
//                        .addFileToUpload(picturePath, "image") //Adding file
//                        .setNotificationConfig(new UploadNotificationConfig())
//                        .setMaxRetries(2)
//                        .startUpload(); //Starting the upload
//
//            } catch (Exception exc) {
//                Toast.makeText(ProfileActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            return "Success";
//
//        }
//
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.hide();
//            pd.dismiss();
//        }
//    }
//
//
//
//    private void galleryIntent()
//    {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
//
//        //Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        //startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
//    }
//
//    private void cameraIntent()
//    {
//        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        //startActivityForResult(intent, REQUEST_CAMERA);
//
//        //Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        //startActivityForResult(takePicture, 0);//zero can be replaced with any action code
//
////        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
////        startActivityForResult(takePicture, 0);
////**************************************************************************************************
//
//
//        // Check Camera
//        if (getApplicationContext().getPackageManager().hasSystemFeature(
//                PackageManager.FEATURE_CAMERA)) {
//            // Open default camera
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
//
//            // start the image capture Intent
//            startActivityForResult(intent, 0);
//
//        } else {
//            Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    private void onCaptureImageResult(Intent data) {
//        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//
//        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
//
//        FileOutputStream fo;
//        try {
//            destination.createNewFile();
//            fo = new FileOutputStream(destination);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        cirleImg.setImageBitmap(thumbnail);
//    }
//
//    @SuppressWarnings("deprecation")
//    private void onSelectFromGalleryResult(Intent data) {
//        filePath = data.getData();
////            try {
////                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
////                cirleImg.setImageBitmap(bitmap);
//        Bitmap bm=null;
//        if (data != null) {
//            try {
//                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        cirleImg.setImageBitmap(bm);
//    }
//
//
//
////**************************************************************
//
//    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
//    {
//        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
//        image.compress(compressFormat, quality, byteArrayOS);
//        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
//    }
//
//    public static Bitmap decodeBase64(String input)
//    {
//        byte[] decodedBytes = Base64.decode(input, 0);
//        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//    }
//
//
//    //method to get the file path from uri
//    public String getPath(Uri uri) {
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//        cursor.close();
//        cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
//        cursor.moveToFirst();
//        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//        cursor.close();
//        return path;
//    }
//
//
//    //Requesting permission
//    private void requestStoragePermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//            return;
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            //If the user has denied the permission previously your code will come to this block
//            //Here you can explain why you need this permission
//            //Explain here why you need this permission
//        }
//        //And finally ask for the permission
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//    }
//
//
//    //This method will be called when the user will tap on allow or deny
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        //Checking the request code of our request
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//
//            //If permission is granted
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //Displaying a toast
//                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
//            } else {
//                //Displaying another toast if permission is not granted
//                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//
//    private void showDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }
//
//    private void hideDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }
//
//
//    /*
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            if (requestCode == SELECT_PICTURE) {
//
//                Uri selectedImageUri = data.getData();
//
//                if (null != selectedImageUri) {
//
//                    // Saving to Database...
//                    if (saveImageInDB(selectedImageUri)) {
//                        //showMessage("Image Saved in Database...");
//                        Toast.makeText(this, "to DB", Toast.LENGTH_SHORT).show();
//                        cirleImg.setImageURI(selectedImageUri);
//                    }
//
//                    // Reading from Database after 3 seconds just to show the message
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (loadImageFromDB()) {
//                                // showMessage("Image Loaded from Database...");
//                                Toast.makeText(ProfileActivity.this, "from DB", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }, 3000);
//                }
//            }
//        }
//    }
//
//
//     //Save the
//    Boolean saveImageInDB(Uri selectedImageUri) {
//
//        try {
//            dbHelper.open();
//            InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
//            byte[] inputData = Utils.getBytes(iStream);
//            dbHelper.insertImage(inputData);
//            dbHelper.close();
//            return true;
//        } catch (IOException ioe) {
//            //Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
//            dbHelper.close();
//            return false;
//        }
//
//    }
//
//    Boolean loadImageFromDB() {
//        try {
//            dbHelper.open();
//            byte[] bytes = dbHelper.retreiveImageFromDB();
//            dbHelper.close();
//            // Show Image from DB in ImageView
//            cirleImg.setImageBitmap(Utils.getImage(bytes));
//            return true;
//        } catch (Exception e) {
//            // Log.e(TAG, "<loadImageFromDB> Error : " + e.getLocalizedMessage());
//            dbHelper.close();
//            return false;
//        }
//    } */
//}
