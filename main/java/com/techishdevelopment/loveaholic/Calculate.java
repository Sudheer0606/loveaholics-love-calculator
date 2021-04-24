package com.techishdevelopment.loveaholic;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Objects;

import static com.google.android.gms.ads.AdSize.BANNER;

public class Calculate extends AppCompatActivity {

    EditText you, partner;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        Objects.requireNonNull(getSupportActionBar()).hide();
        you = findViewById(R.id.editTextYou);
        partner = findViewById(R.id.editTextPartner);
        builder = new AlertDialog.Builder(this);
        AdView adView = new AdView(this);
        adView.setAdSize(BANNER);
        adView.setAdUnitId("ca-app-pub-2403730688875576/1362921040");
        MobileAds.initialize(this, initializationStatus -> {

        });

        AdView madView = findViewById(R.id.banner_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        madView.loadAd(adRequest);

    }



    public void calculate(View view) {

        String yourName = you.getText().toString().toUpperCase();
        String partnerName = partner.getText().toString().toUpperCase();


        if (TextUtils.isEmpty(yourName)) {
            you.setError("Please Enter Your Name");

        }  else if (TextUtils.isEmpty(partnerName)) {
            partner.setError("Please Enter Your Partner Name");

        }
        else if (yourName.length() > 15) {
            Toast.makeText(this, "Please Enter only 15 characters ", Toast.LENGTH_LONG).show();
        } else if (partnerName.length() > 15) {
            Toast.makeText(this, "Please Enter only 15 characters ", Toast.LENGTH_LONG).show();
        }
        else {

            Intent i = new Intent(Calculate.this, Result.class);
            i.putExtra("y_name", yourName);
            i.putExtra("y_p_name", partnerName);
            startActivity(i);

        }
    }
}