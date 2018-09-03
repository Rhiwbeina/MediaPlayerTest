package com.example.davidladd.mediaplayertest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //MediaPlayer mp;
    DavesMediaPlayer dmp;
    Button button;
    EditText editText;
    final String TAG = "Dave";
    DavesTTS TTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// Check whether this app has write external storage permission or not.
        int readExternalStoragePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
// If do not grant write external storage permission.
        if(readExternalStoragePermission!= PackageManager.PERMISSION_GRANTED)
        {
// Request user to grant write external storage permission.
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
        }

        TTS = new DavesTTS(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.d(TAG, "onInit: Text to Speech ready i guess");

            }
        });        //TTS.sayHello();

        editText = findViewById(R.id.editText);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "but val =  " + button.getText());
                if (button.getText() != "Stop"){
                    button.setText("Stop");
                    Log.d(TAG, "Trying to start player");
                    TTS.sayIt("Starting PLayback of " + editText.getText().toString());
                    dmp = new DavesMediaPlayer();
                    dmp.playSong(editText.getText().toString());
                    //mp = new MediaPlayer();

                } else {
                    button.setText("Start");
                    Log.d(TAG, "Trying to stop player");
                    TTS.sayIt("Trying to stop the media player.");
                    dmp.stopSong();
                }

            }
        });

        //handler = new Handler();
    }


}
