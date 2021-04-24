package com.techishdevelopment.loveaholic;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Love Calculator");
        Button clickStart = findViewById(R.id.btnStart);

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                clickStart,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f)

        );
        objectAnimator.setDuration(500);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-2403730688875576/9979777664", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i("TAG", "onAdLoaded");
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
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
            mInterstitialAd.show(MainActivity.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");

        }
        finishAffinity();
        finish();
    }

    public void startGame(View view) {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
        startActivity(new Intent(getApplicationContext(), Option.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_share:
                shareAppClick();
                break;
            case R.id.nav_aboutus:
                showAboutUs();
                break;
            case R.id.nav_rating:
                rateUs();
                break;
            case R.id.nav_privacy:
                showPrivacy();
                break;
            case R.id.nav_term:
                showTerm();
                break;
            case R.id.nav_dis:
                showDisclaim();

        }
        return true;
    }

    private void showDisclaim() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Disclaimer")
                .setMessage("This is Fun app,all the love percentage shown is for entertainment purpose")
                .setCancelable(false)
                .setNegativeButton("Close", (dialog, which) -> {
                    dialog.cancel();
                    dialog.dismiss();
                });
        alertDialog.show();
    }

    private void showTerm() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Terms & Conditions")
                .setMessage(getString(R.string.term_condition))
                .setCancelable(false)
                .setNegativeButton("Close", (dialog, which) -> dialog.cancel());
        alertDialog.show();

    }

    private void showPrivacy() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Privacy Policy")
                .setMessage(getString(R.string.policy))
                .setCancelable(false)
                .setNegativeButton("Close", (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    private void rateUs() {
        String rateLink = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;

        Uri uriRate = Uri.parse(rateLink);
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, uriRate));
        } catch (Exception e) {
            Toast.makeText(this, "Unable  to Open", Toast.LENGTH_SHORT).show();
        }

    }

    private void showAboutUs() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("About Us")
                .setMessage(getString(R.string.contact))
                .setCancelable(false)
                .setNegativeButton("Close", (dialog, which) -> dialog.cancel());
        alertDialog.show();


    }

    private void shareAppClick() {
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plan");
        String msg = "Your partner loves you this much. Click on the link and match your love . Download the app now \n";
        String shareBody = msg + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
        String shareSub = "Your Subject here";
        myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(myIntent, "Share Using "));
    }

}

