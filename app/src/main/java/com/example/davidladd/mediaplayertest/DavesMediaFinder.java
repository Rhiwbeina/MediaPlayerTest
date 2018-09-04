package com.example.davidladd.mediaplayertest;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

public class DavesMediaFinder {
    String TAG = "Dave";
    Context context;
    Handler mHandler;

    int titleColumnIndex;
    String title;

    int artistColumnIndex;
    String artist;

    int dataColumnIndex;
    String data;

    int albumColumnIndex;
    String album;

    int yearColumnIndex;
    String year;

    String duration;

    public DavesMediaFinder(Context ctx, Handler hdr) {
        this.context = ctx;
        this.mHandler = hdr;
    }

    public void  chooseSong(String search){
        RunnableSearchDb runable = new RunnableSearchDb(search);
        new Thread(runable).start();
    }

    class RunnableSearchDb implements Runnable {
        String[] columns = {MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.YEAR,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };
        String searchString;

        public RunnableSearchDb(String searchString) {
            this.searchString = searchString;
        }

        @Override
        public void run() {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            int imageCounter = 0;
            //Context myAppContext = c;

            Log.d(TAG, "run: " + uri.getPath());
            String search = MediaStore.Audio.Media.TITLE + " LIKE ? ";
            //String[] searchy = {"%w%"};
            String[] searchy = {"%" + searchString + "%", "%" + searchString + "%"};
            try {
                //Cursor cursor = myAppContext.getContentResolver().query(uri, columns, MediaStore.Audio.Media.TITLE + " LIKE ? ", searchy, null);
                Cursor cursor =context.getContentResolver().
                        query(uri, columns, MediaStore.Audio.Media.TITLE + " LIKE ? AND artist LIKE ?" , searchy, " RANDOM() LIMIT 1 ");

                assert cursor != null;

                        cursor.moveToPosition(0);

                        titleColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                        title = cursor.getString(titleColumnIndex);
                        artistColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                        artist = cursor.getString(artistColumnIndex);

                        albumColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                        album = cursor.getString(albumColumnIndex);
                        dataColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                        data = cursor.getString(dataColumnIndex);
                        yearColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.YEAR);
                        year = cursor.getString(yearColumnIndex);

                        duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

                        final String outputDetail = title + " " + artist + " " + album + " " + year + "\n" + data + " -- " + duration + "\n" ;
                        final Bundle songBundle = new Bundle();
                        songBundle.putString("title", title);
                        songBundle.putString("artist",artist);
                        songBundle.putString("data", data);
                        songBundle.putString("duration", duration);
                //songBundle.putString();

                        Log.d(TAG, "found " + songBundle.toString() );

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, " detail " + outputDetail);
                                //MainActivity.songChosen(data);
                                MainActivity.songChosen(songBundle);
                            }
                        });

                        cursor.close();

                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
