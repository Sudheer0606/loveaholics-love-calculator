package com.techishdevelopment.loveaholic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.Objects;

import static com.google.android.gms.ads.AdSize.BANNER;

public class Photo extends AppCompatActivity {
    Uri resulturi1;
    EditText your_name_tv, partnr_name_tv;
    Boolean first = false, second = false;
    ImageView your_img_btn, your_partner_img;
    String imgString, partnerString;
    Bitmap bitmap1;
    Button btnYourImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Objects.requireNonNull(getSupportActionBar()).hide();
        your_img_btn = findViewById(R.id.yourImage);
        your_partner_img = findViewById(R.id.yourPartnerImage);
        AdView adView = new AdView(this);
        adView.setAdSize(BANNER);
        adView.setAdUnitId("ca-app-pub-2403730688875576/1362921040");
        MobileAds.initialize(this, initializationStatus -> {

        });

        AdView madView = findViewById(R.id.adViewPhoto);
        AdRequest adRequest = new AdRequest.Builder().build();
        madView.loadAd(adRequest);


    }


    public void uploadYour(View view) {
        first = true;
        chooseImage();


    }


    public void uploadPartnerImg(View view) {
        second = true;
        chooseImage();
    }

    private void chooseImage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                cropImage();

            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

        } else {
            cropImage();
        }


    }

    private void cropImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            } else {
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                assert result != null;
                resulturi1 = result.getUri();
                bitmap1 = null;
                try {
                    bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), resulturi1);
                    if (first) {
                        your_img_btn.setImageBitmap(bitmap1);
                        first = false;
                        imgString = resulturi1.toString();

                    } else if (second) {
                        your_partner_img.setImageBitmap(bitmap1);
                        second = false;
                        partnerString = resulturi1.toString();

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

                your_name_tv = findViewById(R.id.etYouImg);
                partnr_name_tv = findViewById(R.id.etPImg);
                btnYourImg = findViewById(R.id.setImage);
                btnYourImg.setOnClickListener(v -> {
                    String yourNameImg = your_name_tv.getText().toString();
                    String partnerNameImg = partnr_name_tv.getText().toString();
                    if (imgString == null) {
                        Toast.makeText(getApplicationContext(), "Please Select Your Image", Toast.LENGTH_SHORT).show();
                    } else if (partnerString == null) {
                        Toast.makeText(getApplicationContext(), "Please Select Your Partner Image", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(yourNameImg)) {
                        your_name_tv.setError("Please Enter Your Name");

                    } else if (TextUtils.isEmpty(partnerNameImg)) {
                        partnr_name_tv.setError("Please Enter Your Partner Name");

                    } else if (yourNameImg.length() > 15) {
                        Toast.makeText(this, "Please Enter only 15 characters ", Toast.LENGTH_LONG).show();
                    } else if (partnerNameImg.length() > 15) {
                        Toast.makeText(this, "Please Enter only 15 characters ", Toast.LENGTH_LONG).show();
                    } else {

                        Intent newIntent = new Intent(Photo.this, ResultPhoto.class);
                        newIntent.putExtra("YourImgUri", imgString);
                        newIntent.putExtra("PartnerImgUri", partnerString);
                        newIntent.putExtra("y_name_Img", yourNameImg);
                        newIntent.putExtra("p_name_Img", partnerNameImg);
                        startActivity(newIntent);
                    }
                });
            }
        }
    }


}
