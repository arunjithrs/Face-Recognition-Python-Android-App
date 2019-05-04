package com.example.arunjith.homeprosecurity.model;

import android.app.Activity;
import android.app.ProgressDialog;


public class CustomProgressBar {
    ProgressDialog pd;
    public CustomProgressBar(Activity activity){
        pd = new ProgressDialog(activity);
        pd.setMessage("Processing...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
    }
    public void show() {
        pd.show();
    }
    public void hide() {
        pd.hide();
    }
}