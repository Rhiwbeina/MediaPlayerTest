package com.example.davidladd.mediaplayertest;

import android.Manifest;
import android.content.pm.PackageManager;

import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    DavesMediaPlayer dmp, odmp;
    //static DavesMediaFinder dmf;
    Button button, buttgong;
    TextView editText, textViewLibraryCount;
    final String TAG = "Dave";
    DavesTTS TTS;
    Handler mainHandler;
    MainActivity mInstance;
    davesSpeechComposer dsc;

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



            mInstance = this;
            mainHandler = new Handler();

            TTS = new DavesTTS(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    Log.d(TAG, "onInit: Text to Speech ready i guess");
                }
            }, mInstance);

            editText = findViewById(R.id.editText2);

            button = findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d(TAG, "but val =  " + button.getText());
                    if (button.getText() != "Stop") {
                        button.setText("Stop");
                        Log.d(TAG, "choosing a song");
                        chooseSong();
                        //dmf.chooseSong("");

                    } else {
                        button.setText("Start");
                        Log.d(TAG, "Trying to quit");
                        finish();
                        System.exit(0);
                    }
                }
            });

            buttgong = findViewById(R.id.buttgong);
            buttgong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: gonged");
                    buttgong.setText("gonged !");
                    //dmp.handler.removeCallbacks(dmp.rr);
                    mainHandler.removeCallbacks(dmp.rr);
                    //dmp.handler.postDelayed(dmp.rr, 500);
                    mainHandler.postDelayed(dmp.rr, 500);
                    //chooseSong();
                }
            });

            //dmf = new DavesMediaFinder(getApplicationContext(), mainHandler);
            textViewLibraryCount = findViewById(R.id.textViewLibraryCount);
            textViewLibraryCount.setText("Songs in library " + getLibraryCount());
            //textViewLibraryCount.setText(getDate());

            dsc = new davesSpeechComposer(getApplicationContext());

    }

    public void configUi(){

    }

    public void chooseSong(){
        DavesMediaFinder dmf = new DavesMediaFinder( this.mainHandler, this.getApplicationContext(), this.mInstance);

        dmf.chooseSong("");
    }

    public void songChosen(Bundle songBundle){
        Log.d("Dave", "songChosen so dip vol and start speaking ");
        //final String sss = this.dsc.TAG;
        //Log.d(TAG, "songChosen: sentence " + dsc.getSentence(songBundle));
        if (dmp != null)  dmp.setVolume((float) 0.35, (float) 0.35);
        final String textToSpeak = dsc.getSentence(songBundle);
        //final String textToSpeak = "And now the sweet, sweet sound of " + songBundle.getString("artist") + " with the smash hit " + songBundle.getString("title");
editText.setText(textToSpeak);
odmp = dmp;
        dmp = new DavesMediaPlayer(mInstance);
        dmp.prepSong(songBundle);
//dmp
        TTS.sayIt(textToSpeak, songBundle);
    }

    public void doneSpeaking(String utteranceId, Bundle songBundle){
        Log.d(TAG, "doneSpeaking: ");

        if(dmp.prepared){
            dmp.playSong(songBundle, odmp);
            buttgong.setText("gong me");
        }

        if (odmp != null) odmp.reset();
    }

    public MainActivity getmInstance() {
        return mInstance;
    }

    public String getLibraryCount() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] columns = {MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.YEAR,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };

        String libraryCount = "";
        String[] searchy = {"%" + "" + "%", "30000"};
        try {
            Cursor cursor = getContentResolver().query(uri, columns,  MediaStore.Audio.Media.TITLE + " LIKE ? AND duration > ?" , searchy, null);

            assert cursor != null;
            libraryCount = String.valueOf(cursor.getCount());
            Log.d(TAG, "onCreate: Library count = " + String.valueOf(cursor.getCount()));
            cursor.close();
            //textViewLibraryCount.setText("Soungs found in library: " + String.valueOf(cursor.getCount()) );
        }
        catch( Exception eee){
            Log.d(TAG, "onCreate: error " + eee);
            libraryCount = "error";
        }
        return libraryCount;
    }


}
