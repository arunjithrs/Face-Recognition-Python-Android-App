package com.example.arunjith.homeprosecurity;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arunjith.homeprosecurity.adapters.UsersAdapter;
import com.example.arunjith.homeprosecurity.api.RetrofitClient;
import com.example.arunjith.homeprosecurity.model.CustomProgressBar;
import com.example.arunjith.homeprosecurity.model.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewUsers extends AppCompatActivity {
    CustomProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);
        progressBar = new CustomProgressBar(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final ListView usersListView = (ListView) findViewById(R.id.users_list_view);


        final TextView toolbarText = (TextView) findViewById(R.id.toolBarText);
        toolbarText.setText("View users");

        final ImageView navBackButton = (ImageView) findViewById(R.id.navBackButton);
        navBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Call<List<Users>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getUsersList();
        progressBar.show();
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                progressBar.hide();
                List usersResp = response.body();

                String[] name = new String[usersResp.size()];
                Boolean[] access = new Boolean[usersResp.size()];
                String[] proPic = new String[usersResp.size()];

                for (int i=0; i<usersResp.size(); i++) {
                    Users subItem = (Users) usersResp.get(i);
                    name[i] = subItem.getName();
                    proPic[i] = subItem.getPro_pic();
                    access[i] = subItem.isAccess();
                }

                UsersAdapter usersAdapter = new UsersAdapter(getApplicationContext(), name, access, proPic);

                usersListView.setAdapter(usersAdapter);

            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                progressBar.hide();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
