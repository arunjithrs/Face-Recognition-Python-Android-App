package com.example.arunjith.homeprosecurity.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.arunjith.homeprosecurity.R;
import com.example.arunjith.homeprosecurity.model.VisitorsModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class VisitorsRecycleAdapter extends RecyclerView.Adapter<VisitorsRecycleAdapter.MyViewHolder> {

    private List<VisitorsModel> visitorsList;

    public VisitorsRecycleAdapter(List<VisitorsModel> visitorsList) {
        this.visitorsList = visitorsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.visitors_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        VisitorsModel visitor = visitorsList.get(i);

        myViewHolder.name.setText(visitor.getName());

        try {
            URL url = new URL(visitor.getPic());
            Log.i("okokok", url.toString());
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            myViewHolder.imageView.setImageBitmap(image);

        } catch(IOException e) {
            System.out.println(e);
        }


    }

    @Override
    public int getItemCount() {
        return visitorsList.size();

    }

    public class MyViewHolder  extends  RecyclerView.ViewHolder{
        public TextView name, date, time;
        ImageView imageView;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.visitor_name);

            imageView = (ImageView)itemView.findViewById(R.id.image_view);
        }

    }

}
