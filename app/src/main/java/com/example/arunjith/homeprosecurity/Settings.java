package com.example.arunjith.homeprosecurity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arunjith.homeprosecurity.api.RetrofitClient;
import com.example.arunjith.homeprosecurity.model.CustomProgressBar;
import com.example.arunjith.homeprosecurity.model.Image;

import retrofit2.Callback;
import retrofit2.Response;

public class Settings extends AppCompatActivity {

    CustomProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        progressBar = new CustomProgressBar(this);

        final TextView toolbarText = (TextView) findViewById(R.id.toolBarText);
        toolbarText.setText("Settings");
        final ImageView navBackButton = (ImageView) findViewById(R.id.navBackButton);
        final Button rebootBtn = (Button) findViewById(R.id.reboot);
        navBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Switch privateSwitch = (Switch) findViewById(R.id.private_switch);


        retrofit2.Call<Image> call = RetrofitClient
                .getInstance()
                .getApi()
                .getSettingsMode();

        progressBar.show();
        call.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(retrofit2.Call<Image> call, Response<Image> response) {
                Image res = response.body();
                if(res.getMessage().equals("True")){
                    privateSwitch.setChecked(true);
                } else {
                    privateSwitch.setChecked(false);
                }
                progressBar.hide();
            }

            @Override
            public void onFailure(retrofit2.Call<Image> call, Throwable t) {

            }
        });
        privateSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofit2.Call<Image> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .updateSettingsMode(privateSwitch.isChecked());
                progressBar.show();

                call.enqueue(new Callback<Image>() {
                    @Override
                    public void onResponse(retrofit2.Call<Image> call, Response<Image> response) {
                        progressBar.hide();
                        Image responseText = response.body();

                        Toast.makeText(getApplicationContext(), responseText.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(retrofit2.Call<Image> call, Throwable t) {
                        Toast.makeText(Settings.this, t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

        rebootBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofit2.Call<Image> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .rebootServer();
                progressBar.show();

                call.enqueue(new Callback<Image>() {
                    @Override
                    public void onResponse(retrofit2.Call<Image> call, Response<Image> response) {
                        progressBar.hide();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                        builder.setMessage("Server is rebooting. Please wait for 10 seconds!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    @Override
                    public void onFailure(retrofit2.Call<Image> call, Throwable t) {

                    }
                });
            }
        });
    }
}
