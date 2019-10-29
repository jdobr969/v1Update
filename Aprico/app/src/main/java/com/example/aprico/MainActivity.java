package com.example.aprico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

//THIS IS DIFFERENT

public class MainActivity extends AppCompatActivity {
    //https://medium.com/@peeyush.pathak18/qr-code-barcode-scanning-in-android-app-a84d290fffad

    TextView textView;
    ImageView imageView;
    Button button;
    DecoratedBarcodeView dbvScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.txtContent);

        dbvScanner = (DecoratedBarcodeView) findViewById(R.id.dbv_barcode);
        requestPermission();


        dbvScanner.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                updateText(result.getText());
                beepSound();
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });

        //llProgress.setVisibility(View.GONE);
        //setupNfcAdapter();

        /*Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtView = (TextView) findViewById(R.id.txtContent);

                ImageView myImageView = (ImageView) findViewById(R.id.imgview);
                Bitmap myBitmap = BitmapFactory.decodeResource(
                        getApplicationContext().getResources(),
                        R.drawable.hi);
                myImageView.setImageBitmap(myBitmap);
                BarcodeDetector detector =
                        new BarcodeDetector.Builder(getApplicationContext())
                                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                                .build();
                if(!detector.isOperational()){
                    txtView.setText("Could not set up the detector!");
                    return;
                }
                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<Barcode> barcodes = detector.detect(frame);
                Barcode thisCode = barcodes.valueAt(0);

                txtView.setText(thisCode.rawValue);
            }
        });*/

    }
    private void updateText(String scanCode) {
        textView.setText(scanCode);
    }
    private void updateUserName(String text) {
        //this text the scancode from camera
    }


    protected void beepSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeScanner();
    }
    Boolean isScanDone = true;
    protected void resumeScanner() {
        isScanDone = false;
        if (!dbvScanner.isActivated())
            dbvScanner.resume();
        Log.d("peeyush-pause", "paused: false");
    }

    protected void pauseScanner() {
        dbvScanner.pause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseScanner();
    }




    void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults.length < 1) {
            requestPermission();
        } else {
            dbvScanner.resume();
        }
    }

}
