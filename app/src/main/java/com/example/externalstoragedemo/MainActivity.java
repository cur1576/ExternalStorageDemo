package com.example.externalstoragedemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        doIt();
    }

    private void doIt(){
        tv.setText(String.format("Medium kann%s entfernt werden",
                // Environment ist die Systemumgebung in welcher unsere App liegt
                Environment.isExternalStorageRemovable()?"":" nicht"));
    }
}
