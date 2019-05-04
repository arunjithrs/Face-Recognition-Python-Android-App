package com.example.arunjith.homeprosecurity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    LinearLayout addUserLayout;
    LinearLayout viewUserLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final TextView toolbarText = (TextView) findViewById(R.id.toolBarText);
        final ImageView navBackButton = (ImageView) findViewById(R.id.navBackButton);
        toolbarText.setText("Home Security Pro");
        navBackButton.setVisibility(View.INVISIBLE);


//        addUserLayout = (LinearLayout) findViewById(R.id.add_user);
//        viewUserLayout = (LinearLayout) findViewById(R.id.view_user);
//
//        addUserLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//
//            }
//        });
//
//        viewUserLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });

    }

    public void redirect(View v){
        String tag = "com.example.arunjith.homeprosecurity." + v.getTag().toString();
        Log.i("okokokok", tag.toString());
        try {
            Class<?> newClass = Class.forName(tag);
            Intent intent = new Intent(getApplicationContext(), newClass);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
