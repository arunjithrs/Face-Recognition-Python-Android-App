package com.example.arunjith.homeprosecurity;
import com.example.arunjith.homeprosecurity.model.CustomProgressBar;
import com.pusher.pushnotifications.PushNotifications;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arunjith.homeprosecurity.api.RetrofitClient;
import com.example.arunjith.homeprosecurity.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button btn;
    CircleImageView imageView;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    String dialogInputValue;
    TextView username;
    ImageView nameEditBtn;

    RelativeLayout uploadLayout;
    Button uploadBtn;

    LinearLayout nameSection;
    RelativeLayout controlSection;

    Switch accessSwitch;
    CustomProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = new CustomProgressBar(this);

        PushNotifications.start(getApplicationContext(), "97bc1b7f-aa2a-4760-af68-3052371c6dbd");
        PushNotifications.subscribe("hello");

        btn = (Button) findViewById(R.id.takePictureBtn);
        imageView = (CircleImageView) findViewById(R.id.imageView);
        uploadLayout = (RelativeLayout) findViewById(R.id.upload_layout);
        username = (TextView) findViewById(R.id.username);
        uploadBtn = (Button) findViewById(R.id.upload_picture);
        nameSection = (LinearLayout) findViewById(R.id.layout_name_section);
        nameEditBtn = (ImageView) findViewById(R.id.name_edit_btn);
        controlSection = (RelativeLayout) findViewById(R.id.layout_control);
        accessSwitch = (Switch) findViewById(R.id.access_switch);

        nameSection.setVisibility(View.INVISIBLE);
        controlSection.setVisibility(View.GONE);


        final TextView toolbarText = (TextView) findViewById(R.id.toolBarText);
        toolbarText.setText("Add User");
        final ImageView navBackButton = (ImageView) findViewById(R.id.navBackButton);
        navBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nameEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

                alert.setTitle("Update Name");
                alert.setMessage("Name must be unique *");

                final EditText dialogInput = new EditText(view.getContext());
                alert.setView(dialogInput);
                dialogInput.setText(username.getText().toString());

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialogInputValue = dialogInput.getText().toString();

                        if( dialogInputValue.isEmpty() ) {
                            Toast.makeText(getApplicationContext(), "Cannot be empty", Toast.LENGTH_LONG).show();
                            return;
                        }

                        nameSection.setVisibility(View.VISIBLE);

                        username.setText(dialogInputValue);

                    }
                });

                alert.show();
            }
        });


//        upload image to server
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
                String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);


//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
//                byte[] imageBytes = baos.toByteArray();
//                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);


                Call<Image> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .sendImage(dialogInputValue, encodedImage, accessSwitch.isChecked());

                progressBar.show();
                call.enqueue(new Callback<Image>() {
                    @Override
                    public void onResponse(Call<Image> call, Response<Image> response) {
                        progressBar.hide();
                        Image responseText = response.body();

                        if(responseText.getSuccess().equals(true)){

                            Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), responseText.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Image> call, Throwable t) {
                        progressBar.hide();
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void openCamera(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

        alert.setTitle("Enter the name");
        alert.setMessage("Name must be unique *");

        final EditText dialogInput = new EditText(view.getContext());
        alert.setView(dialogInput);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialogInputValue = dialogInput.getText().toString();

                if( dialogInputValue.isEmpty() ) {
                    Toast.makeText(getApplicationContext(), "Cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                nameSection.setVisibility(View.VISIBLE);
                username.setText(dialogInputValue);
                controlSection.setVisibility(View.VISIBLE);


                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {

                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                                "com.example.android.fileprovider",
                                photoFile);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alert.show();

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpeg",
                storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            File file = new File(mCurrentPhotoPath);
            Bitmap bitmap = null;
            bitmap = MediaStore.Images.Media
                    .getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(file));

            if (bitmap != null) {

                Bitmap rotateBitmap = rotateImage(bitmap);
                bitmap = rotateBitmap;

                imageView.setImageBitmap(bitmap);

                uploadLayout.setVisibility(View.VISIBLE);
                btn.setVisibility(View.GONE);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Bitmap rotateImage(Bitmap bitmap){
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;

             default:
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return rotatedBitmap;
    }




}
