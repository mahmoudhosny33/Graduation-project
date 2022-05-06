package com.hos.imagepro.room;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hos.imagepro.R;

import org.w3c.dom.Text;

import java.io.ObjectInput;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class SpeechRecognition extends AppCompatActivity {

    ImageButton mic;
    TextView text;
    private static final int Speech_Input=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recognition);

        mic = findViewById(R.id.imageButton);
        text = findViewById(R.id.editTextTextMultiLine);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech To Text");
                try {
                    startActivityForResult(intent, Speech_Input);
                } catch (Exception e) {
                    Toast.makeText(SpeechRecognition.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Speech_Input){
            if(resultCode==RESULT_OK&& data !=null){
                ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                text.setText((Objects.requireNonNull(result).get(0)));
                //    Toast.makeText(this,Speech_Input,Toast.LENGTH_LONG).show();
            }
        }
    }
}