package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import java.io.BufferedReader;

public class activity_location_select extends AppCompatActivity {
    private Button sangnokwon3;
    private Button sangnokwon2;
    private Button sangnokwon1;
    private Button namsan;
    private Button caffe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select);

        sangnokwon1 = findViewById(R.id.btn1);
        sangnokwon2 = findViewById(R.id.btn2);
        sangnokwon3 = findViewById(R.id.btn3);
        namsan = findViewById(R.id.btn4);
        caffe = findViewById(R.id.btn5);


    }
}