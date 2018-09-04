package com.example.davidladd.mediaplayertest;

import android.Manifest;
import android.content.pm.PackageManager;

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


public class MainActivity extends AppCompatActivity {
    static DavesMediaPlayer dmp;
    static DavesMediaFinder dmf;
    Button button;
    static EditText editText;
    final String TAG = "Dave";
    static DavesTTS TTS;
    Handler mainHandler;
    MainActivity mInstance;

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
                    Log.d(TAG, "choosing a song");
                    chooseSong();
                    //dmf.chooseSong("");

                } else {
                    button.setText("Start");
                    Log.d(TAG, "Trying to stop player");

                }
            }
        });
        mInstance = this;
        mainHandler = new Handler();
        dmf = new DavesMediaFinder(getApplicationContext(), mainHandler);
    }

    public static void chooseSong(){
        dmf.chooseSong("");
    }

    public static void songChosen(Bundle songBundle){
        Log.d("Dave", "announceAndPlay: ");
        if (dmp != null)  dmp.setVolume((float) 0.35, (float) 0.35);
        final String textToSpeak = "And now the sweet, sweet sound of " + songBundle.getString("artist") + " with the smash hit " + songBundle.getString("title");
        TTS.sayIt(textToSpeak, songBundle);
    }

    public static void doneSpeaking(String utteranceId, Bundle songBundle){
        DavesMediaPlayer odmp = dmp;
        dmp = new DavesMediaPlayer();
        //dmp.playSong(songBundle.getString("data"));
        dmp.playSong(songBundle);
        odmp.reset();
    }

    public MainActivity getmInstance() {
        return mInstance;
    }
}
