package com.example.user.alumni;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by User on 23-03-2017.
 */

public class Utility {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}























//*************************************

//public class MyActivity extends Activity {
//
//    Button btpic, btnup;
//    private Uri fileUri;
//    String picturePath;
//    Uri selectedImage;
//    Bitmap photo;
//    String ba1;
//    public static String URL = "Paste your URL here";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my);
//
//        btpic = (Button) findViewById(R.id.cpic);
//        btpic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickpic();
//            }
//        });
//
//        btnup = (Button) findViewById(R.id.up);
//        btnup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                upload();
//            }
//        });
//    }
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
//        ba1 = Base64.encodeToString(ba, Base64.NO_WRAP);
//
//        Log.e("base64", "-----" + ba1);
//
//        // Upload image to server
//        new uploadToServer().execute();
//
//    }
//
//    private void clickpic() {
//        // Check Camera
//        if (getApplicationContext().getPackageManager().hasSystemFeature(
//                PackageManager.FEATURE_CAMERA)) {
//            // Open default camera
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//
//            // start the image capture Intent
//            startActivityForResult(intent, 100);
//
//        } else {
//            Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 100 && resultCode == RESULT_OK) {
//
//            selectedImage = data.getData();
//            photo = (Bitmap) data.getExtras().get("data");
//
//            // Cursor to get image uri to display
//
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            picturePath = cursor.getString(columnIndex);
//            cursor.close();
//
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            ImageView imageView = (ImageView) findViewById(R.id.Imageprev);
//            imageView.setImageBitmap(photo);
//        }
//    }
//
//    public class uploadToServer extends AsyncTask<Void, Void, String> {
//
//        private ProgressDialog pd = new ProgressDialog(MyActivity.this);
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Wait image uploading!");
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
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
//}