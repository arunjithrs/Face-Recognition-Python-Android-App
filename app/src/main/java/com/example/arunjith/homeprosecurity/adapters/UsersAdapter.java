package com.example.arunjith.homeprosecurity.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arunjith.homeprosecurity.HomeActivity;
import com.example.arunjith.homeprosecurity.MainActivity;
import com.example.arunjith.homeprosecurity.R;
import com.example.arunjith.homeprosecurity.ViewUsers;
import com.example.arunjith.homeprosecurity.api.RetrofitClient;
import com.example.arunjith.homeprosecurity.model.CustomProgressBar;
import com.example.arunjith.homeprosecurity.model.Image;
import com.example.arunjith.homeprosecurity.model.Users;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersAdapter extends BaseAdapter {

    Context context;
    private final String [] name;
    private final Boolean [] access;
    private final String [] pro_pic;

    View view;
    LayoutInflater layoutInflater;

    public UsersAdapter(Context context, String[] name, Boolean[] access, String[] pro_pic) {
        this.context = context;
        this.name = name;
        this.access = access;
        this.pro_pic = pro_pic;
    }


    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            view = new View((context));
            view = layoutInflater.inflate(R.layout.users_list, null);

            final TextView nameText = (TextView) view.findViewById(R.id.username);
            final TextView accessText = (TextView) view.findViewById(R.id.access_text);
            final Switch accessSwitch = (Switch) view.findViewById(R.id.access_switch);
            CircleImageView proPic = (CircleImageView) view.findViewById(R.id.profile_image);
            final ImageView deleteUser = (ImageView) view.findViewById(R.id.delete_user_icon);

            if (access[position]) {
                accessText.setText("Full Access");
            } else {
                accessText.setText("Access Restricted");
                accessText.setTextColor(Color.parseColor("#F43F86"));
            }

            nameText.setText(name[position]);
            accessSwitch.setChecked(access[position]);

            try {
                URL url = new URL(pro_pic[position]);
                Log.i("okokok", url.toString());
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                proPic.setImageBitmap(image);
            } catch(IOException e) {
                System.out.println(e);
            }

            accessSwitch.setTag(R.id.username, name[position]);
            deleteUser.setTag(R.id.username, name[position]);


            accessSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Boolean status = accessSwitch.isChecked();
                    String name = view.getTag(R.id.username).toString();
                    Call<Image> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .access(name, status);
                    call.enqueue(new Callback<Image>() {
                        @Override
                        public void onResponse(Call<Image> call, Response<Image> response) {
                            Image res = response.body();
                            accessSwitch.setChecked(status);
                            if (status ) {
                                accessText.setText("Full Access");
                                accessText.setTextColor(Color.parseColor("#cfcfcf"));
                            }else {
                                accessText.setText("Access Restricted");
                                accessText.setTextColor(Color.parseColor("#F43F86"));
                            }
                        }

                        @Override
                        public void onFailure(Call<Image> call, Throwable t) {
                        }
                    });
                }
            });

            deleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Action in process please wait!", Toast.LENGTH_LONG).show();
                   final String name = view.getTag(R.id.username).toString();
                    Call<Image> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .deleteUser(name);
                    call.enqueue(new Callback<Image>() {
                        @Override
                        public void onResponse(Call<Image> call, Response<Image> response) {
                            Image res = response.body();
                            Toast.makeText(context, res.getMessage(), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(context, HomeActivity.class);
                            context.startActivity(intent);

                        }

                        @Override
                        public void onFailure(Call<Image> call, Throwable t) {
                        }
                    });


                }
            });

        }
        return view;
    }
}
