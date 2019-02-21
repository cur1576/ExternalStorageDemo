package com.example.externalstoragedemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        doIt();
    }

    private void doIt() {
        tv.setText(String.format("Medium kann%s entfernt werden\n",
                // Environment ist die Systemumgebung in welcher unsere App liegt
                Environment.isExternalStorageRemovable() ? "" : " nicht"));

        final String state = Environment.getExternalStorageState();
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
                canRead?"":" nicht"));
        tv.append(String.format("Schreiben ist%s möglich\n",
                canWrite?"":" nicht"));
    }
}
