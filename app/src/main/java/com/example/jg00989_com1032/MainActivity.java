package com.example.jg00989_com1032;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity {

    private int[] tempFileStr = new int[9];
    public static int filepositionNumber = 0;
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;

    private MoveFile move = new MoveFile();
    TextView Progress;

    private File[] files = move.getDirectories("/sdcard/Source");
    ReentrantLock re = new ReentrantLock();

    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            try {

            }catch (Exception e){
            }
            Progress.setText("Progress: " + filepositionNumber+ " files out of " + tempFileStr.length +" completed");
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Here, this is the current activity
        //Context thisActivity;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        if(MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE==0){
            Progress = (TextView) findViewById(R.id.Progress);

            tempFileStr[0] = R.drawable.sample1;
            tempFileStr[1] = R.drawable.sample2;
            tempFileStr[2] = R.drawable.sample3;
            tempFileStr[3] = R.drawable.sample4;
            tempFileStr[4] = R.drawable.sample5;
            tempFileStr[5] = R.drawable.sample6;
            tempFileStr[6] = R.drawable.sample7;
            tempFileStr[7] = R.drawable.sample8;
            tempFileStr[8] = R.drawable.sample9;





            //attempting to make the new file directories for the images
            try {

                File source = new File("/sdcard/Source");

                File destination = new File("/sdcard/Destination");
                if (!source.exists()) {
                    if (!source.mkdir()) {
                        Toast.makeText(getApplicationContext(), "Failed to make the directory", Toast.LENGTH_SHORT).show();
                    }
                }
                if (!destination.exists()) {
                    if (!destination.mkdir()) {
                        Toast.makeText(getApplicationContext(), "Failed to make the directory", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void onClickButton(View v) {

        for (int i = 0; i < 9; i++) {
            try {
                Bitmap bm = BitmapFactory.decodeResource(getResources(), tempFileStr[i]);
                File file = new File("/sdcard/Source/sample" + (i + 1) + ".jpeg");
                FileOutputStream stream1 = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
                stream1.flush();
                stream1.close();
                tempFileStr[i] = 0;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        files = move.getDirectories("/sdcard/Source");

    }

    public void transferFiles(View v) {

        CreatingTheInitialFiles A = new CreatingTheInitialFiles();
        CreatingTheInitialFiles B = new CreatingTheInitialFiles();

        Thread AThread = new Thread(A);
        Thread BThread = new Thread(B);

        AThread.start();
        BThread.start();

    }

    public class CreatingTheInitialFiles implements Runnable {

        @Override
        public void run() {

            //MoveFile mover = new MoveFile();
            File[] files = move.getDirectories("/sdcard/Source/");

            while (filepositionNumber < files.length) {
                try {
                    //Getting Outer Lock
                    boolean ans = re.tryLock();

                    // Returns True if lock is free
                    if (ans) {
                        // Getting Inner Lock

                        try {
                            re.lock();
                            move.moveFile(files[filepositionNumber]);
                            filepositionNumber++;
                            re.unlock();
                            re.unlock();
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("",null);
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                            Thread.sleep((long)(500));
                        } catch (Exception e) {
                            e.printStackTrace();
                            re.unlock();
                            re.unlock();
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }
}