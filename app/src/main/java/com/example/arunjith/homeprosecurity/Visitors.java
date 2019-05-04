package com.example.arunjith.homeprosecurity;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.arunjith.homeprosecurity.adapters.UsersAdapter;
import com.example.arunjith.homeprosecurity.adapters.VisitorsAdapter;
import com.example.arunjith.homeprosecurity.adapters.VisitorsRecycleAdapter;
import com.example.arunjith.homeprosecurity.api.RetrofitClient;
import com.example.arunjith.homeprosecurity.model.CustomProgressBar;
import com.example.arunjith.homeprosecurity.model.Users;
import com.example.arunjith.homeprosecurity.model.VisitorsModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Visitors extends AppCompatActivity {

    CustomProgressBar progressBar;
    private List<VisitorsModel> visitorsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VisitorsRecycleAdapter mAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors);
        progressBar = new CustomProgressBar(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        listView = (ListView) findViewById(R.id.visitors_list_view);

        final TextView toolbarText = (TextView) findViewById(R.id.toolBarText);
        toolbarText.setText("View Visitors");

        final ImageView navBackButton = (ImageView) findViewById(R.id.navBackButton);
        navBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


//        mAdapter = new VisitorsRecycleAdapter(visitorsList);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);

        Call<List<VisitorsModel>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getVisitors();
        progressBar.show();


        call.enqueue(new Callback<List<VisitorsModel>>() {
            @Override
            public void onResponse(Call<List<VisitorsModel>> call, Response<List<VisitorsModel>> response) {
                progressBar.hide();
                List visitorsResp = response.body();

                String[] name = new String[visitorsResp.size()];
                String[] date = new String[visitorsResp.size()];
                String[] time = new String[visitorsResp.size()];
                String[] pic = new String[visitorsResp.size()];

                for (int i=0; i<visitorsResp.size(); i++) {
                    VisitorsModel subItem = (VisitorsModel) visitorsResp.get(i);
                    name[i] = subItem.getName();
                    pic[i] = subItem.getPic();
                    time[i] = subItem.getTime();
                    date[i] = subItem.getDate();
//                    visitorsList.add(subItem);
                }


                VisitorsAdapter visitorsAdapter = new VisitorsAdapter(getApplicationContext(), name, date, pic, time);

                listView.setAdapter(visitorsAdapter);


//                mAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<List<VisitorsModel>> call, Throwable t) {

            }
        });
    }
}
