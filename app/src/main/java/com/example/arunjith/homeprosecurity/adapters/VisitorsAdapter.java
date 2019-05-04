package com.example.arunjith.homeprosecurity.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arunjith.homeprosecurity.R;
import com.example.arunjith.homeprosecurity.Settings;
import com.example.arunjith.homeprosecurity.api.RetrofitClient;
import com.example.arunjith.homeprosecurity.model.Image;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitorsAdapter  extends BaseAdapter {

    Context context;
    private final String [] name;
    private final String [] date;
    private final String [] pic;
    private final String [] time;

    private int dimensionInPixel = 200;


    View view;
    LayoutInflater layoutInflater;

    public VisitorsAdapter(Context context, String[] name, String[] date, String[] pic, String[] time) {
        this.context = context;
        this.name = name;
        this.date = date;
        this.pic = pic;
        this.time = time;
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
        if (convertView == null) {
            view = new View((context));
            view = layoutInflater.inflate(R.layout.visitors_list, null);

            final TextView nameText = (TextView) view.findViewById(R.id.visitor_name);
            final ImageView  imageView = (ImageView) view.findViewById(R.id.image_view);

            final Button allowBtn = (Button) view.findViewById(R.id.allowBtn);
            final Button denyBtn = (Button) view.findViewById(R.id.denyBtn);

            nameText.setText(name[position]);

            int width  = Resources.getSystem().getDisplayMetrics().widthPixels;

            try {
                URL url = new URL(pic[position]);
                Log.i("okokok", url.toString());
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                imageView.setImageBitmap(image);

                imageView.getLayoutParams().width = width;
                imageView.requestLayout();

            } catch(IOException e) {
                System.out.println(e);
            }

            allowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Toast.makeText(context, "Permission granted! Door will be open for 5 seconds", Toast.LENGTH_LONG).show();

                    Call<Image> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .allowPermission();
                    call.enqueue(new Callback<Image>() {
                        @Override
                        public void onResponse(Call<Image> call, Response<Image> response) {
                            Image res = response.body();
                            if (res.getSuccess()) {
                                Toast.makeText(context, "The door is closed", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Image> call, Throwable t) {

                        }
                    });
                }
            });

            denyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Toast.makeText(context, "Permission denied!", Toast.LENGTH_LONG).show();
                }
            });

//            try {
//                JSONArray jr = new JSONArray(name[position]);
//
//                for(int i=0;i<jr.length();i++)
//                {
//                    Log.i("okokokookokkok",""+jr.get(i).toString());
//                    // loop and add it to array or arraylist
//                }
//            }catch(Exception e)
//            {
//                e.printStackTrace();
//            }


        }
        return view;
    }
}
