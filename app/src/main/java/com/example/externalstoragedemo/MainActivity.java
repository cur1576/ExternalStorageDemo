package com.example.externalstoragedemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView tv;
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            }else{
                doIt();
            }
        }else{
            doIt();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE && grantResults.length>0
                && grantResults[0]== PackageManager.PERMISSION_GRANTED){

            Log.d(TAG, "onRequestPermissionsResult: " +requestCode);
            Log.d(TAG, "onRequestPermissionsResult: " + permissions[0]);
            Log.d(TAG, "onRequestPermissionsResult: " + grantResults[0]);

            doIt();
        }
    }

    private void print(String str){
        tv.append(str + "\n");
    }

    private void newLine(){
        print("");
    }

    private void doIt() {
        // print für tv.setText(..)..
        print(String.format("Medium kann%s entfernt werden",
                // Environment ist die Systemumgebung in welcher unsere App liegt
                Environment.isExternalStorageRemovable() ? "" : " nicht"));

        final String state = Environment.getExternalStorageState();

        newLine();
        print(state);
        newLine();

        final boolean canRead, canWrite;
        switch (state) {
            case Environment.MEDIA_MOUNTED:
                canRead = true;
                canWrite = true;
                break;
            case Environment.MEDIA_MOUNTED_READ_ONLY:
                canRead = true;
                canWrite = false;
                break;
            default:
                canRead = canWrite = false;
        }
        tv.append(String.format("Lesen ist%s möglich\n",
                canRead ? "" : " nicht"));
        tv.append(String.format("Schreiben ist%s möglich\n",
                canWrite ? "" : " nicht"));
        File dirBase = Environment.getExternalStorageDirectory();
        tv.append(dirBase.getAbsolutePath() + "\n");
        File pubDirBase = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        tv.append(pubDirBase.getAbsolutePath() + "\n");
        File root = Environment.getRootDirectory();
        tv.append(root.getAbsolutePath() + "\n");
        File dirAppBase = new File(dirBase.getAbsolutePath() + "/Android/data/" + getClass().getPackage().getName() + "/files");
        tv.append(getPackageName());

        print(dirAppBase.getAbsolutePath());
        newLine();

        if (!dirAppBase.mkdirs()) {
            tv.append(String.format("alle Unterverzeichnisse von %s schon vorhanden", dirAppBase.getAbsoluteFile()));
        }
        File f1 = getExternalFilesDir(null);
        if (f1 != null) {
            tv.append("Basisverzeichnis\n" + f1.getAbsolutePath());
        }
        File f2 = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (f2 != null) {
            tv.append("Bildverzeichnis\n" + f2.getAbsolutePath());

        }
        if (!pubDirBase.mkdirs()) {
            tv.append("PubPictures ist bereits vorhanden");
        }

        File file = new File(pubDirBase, "grafik.png");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            saveBitmap(fos);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "", e);
        } catch (IOException e) {
            Log.e(TAG, "", e);
        }

    }

    private void saveBitmap(FileOutputStream fos) {
        int w = 100;
        int h = 100;
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bm);
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        canvas.drawRect(0,0,w-1,h-1,paint);
        paint.setColor(Color.BLUE);
        canvas.drawLine(0,0,w-1,h-1,paint);
        canvas.drawLine(0,h-1,w-1,0,paint);
        paint.setColor(Color.BLACK);
        canvas.drawText("Hallo Android!",w/2,h/2,paint);
        bm.compress(Bitmap.CompressFormat.PNG, 100,fos);

    }
}
