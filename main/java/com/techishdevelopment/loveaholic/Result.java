package com.techishdevelopment.loveaholic;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Objects;
import java.util.Random;

import static com.google.android.gms.ads.AdSize.BANNER;

public class Result extends AppCompatActivity {
TextView r_y_name,r_p_name,r_percent,couple_tv;
Animation  resultAnim;
LinearLayout linearLayout;
    String[] couple={"Perfect","Cute","Nice","Beautiful","Best","Loyal"};
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Objects.requireNonNull(getSupportActionBar()).hide();
        r_y_name=findViewById(R.id.youName);
        r_p_name=findViewById(R.id.prtnrName);
        r_percent=findViewById(R.id.tvPercent);
        couple_tv=findViewById(R.id.tvCouples);
        linearLayout=findViewById(R.id.resultLayout);
        resultAnim= AnimationUtils.loadAnimation(this,R.anim.layout_animation);
        linearLayout.setAnimation(resultAnim);
        AdView adView = new AdView(this);
        adView.setAdSize(BANNER);
        adView.setAdUnitId("ca-app-pub-2403730688875576/1362921040");
        MobileAds.initialize(this, initializationStatus -> {

        });

        AdView madView = findViewById(R.id.adViewResult);
        AdRequest adRequest = new AdRequest.Builder().build();
        madView.loadAd(adRequest);
        r_y_name.setText(getIntent().getStringExtra("y_name"));
        r_p_name.setText(getIntent().getStringExtra("y_p_name"));
        final int random = new Random().nextInt(20) + 80;
        String num=Integer.toString(random);
        r_percent.setText(String.format("%s%%", num));
        final int r = new Random().nextInt(6);
        couple_tv.setText(String.format("%s Couple", couple[r]));

        ObjectAnimator objectAnimator=ObjectAnimator.ofPropertyValuesHolder(
                r_percent,
                PropertyValuesHolder.ofFloat("scaleX",1.2f),
                PropertyValuesHolder.ofFloat("scaleY",1.2f)

        );
        objectAnimator.setStartDelay(2600);
        objectAnimator.setDuration(500);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();
        AdRequest adRequest1 = new AdRequest.Builder().build();
        InterstitialAd.load(this,getResources().getString(R.string.interstitial_ad), adRequest1, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i("TAG", "onAdLoaded");
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i("TAG", loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });




    }

    @Override
    public void onBackPressed() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

        startActivity(new Intent(getApplicationContext(),Option.class));
    }

    public void clickShare(View view) {
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plan");
        String msg="Your partner loves you this much. Click on the link and match your love . Download the app now \n";
        String shareBody =msg+"https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID+"\n\n";
        String shareSub="Your Subject here";
        myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
        myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
        startActivity(Intent.createChooser(myIntent,"Share Using "));
    }

    public void clickRetry(View view) {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");

        }
        startActivity(new Intent(getApplicationContext(),Option.class));

    }
}