package com.hos.imagepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hos.imagepro.room.SpeechRecognition;

import org.opencv.android.OpenCVLoader;

public class DetectOrCall extends AppCompatActivity {

    static {
        if(OpenCVLoader.initDebug()){
            Log.d("DetectOrCall","opencv is loaded");
        }
        else
            Log.d("DetectOrCall","opencv failed to load");
    }
    private Button camera_button;
    private Button speech_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_or_call);
        camera_button=findViewById(R.id.Detect);
        speech_button=findViewById(R.id.Speech);
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetectOrCall.this,CameraActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        speech_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetectOrCall.this, SpeechRecognition.class));

            }
        });
    }
}