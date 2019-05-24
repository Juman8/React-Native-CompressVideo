package com.apptest.trimmer;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TrimManager extends ReactContextBaseJavaModule {
    public final static String TAG = "TAGGGG";
    static final String REACT_PACKAGE = "TrimmerVideo";
    public FFmpeg ffmpeg;

    public TrimManager(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return REACT_PACKAGE;
    }

    @ReactMethod
    public void trimVideo(String input, int startMs, int endMs, Promise promise)throws Exception{
        loadFFMpegBinary(getReactApplicationContext());
        String date = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
        File file = File.createTempFile(date, ".mp4");
        String output = file.getAbsolutePath();
        Uri uri = Uri.parse(input);
        String getAbsol = getRealPathFromURI_API19(getReactApplicationContext(), uri);
        WritableMap event = Arguments.createMap();

        String[] complexCommand = { "-y", "-i", getAbsol,"-ss", "" + startMs, "-t", "" + (endMs - startMs), "-c","copy", output};
        try {
            ffmpeg.execute(complexCommand, new ExecuteBinaryResponseHandler() {
                        @Override
                        public void onFailure(String s) {
                            event.putString("link", "504");
                            promise.resolve(event);
                        }

                        @Override
                        public void onSuccess(String s) {
                            event.putString("link", output);
                            promise.resolve(event);
                        }

                        @Override
                        public void onProgress(String s) {
                        }

                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onFinish() {
                        }
                    }
            );
        } catch (Exception e) {
//            callback.invoke("504");
            event.putString("link", "504");
            promise.resolve(event);
            Log.d(TAG, "Finished command : ffmpeg " + e);
        }
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(ReactApplicationContext context, Uri uri){
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;
        CursorLoader cursorLoader = new CursorLoader(
                context,
                uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    @ReactMethod
    public void getThumnailAsset(String input, Double startT, Promise promise) throws Exception{
        WritableMap event = Arguments.createMap();
        loadFFMpegBinary(getReactApplicationContext());
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Uri uriInput  = Uri.fromFile(new File(input));
        File file = File.createTempFile(date+"_%03d", ".png");
        String outPut = file.getAbsolutePath();
        String[] commandStr = {"-y", "-i", uriInput.getPath(), "-an", "-r", "1", "-ss", startT+"", "-t", startT+"", outPut};
        ffmpeg.execute(commandStr, new ExecuteBinaryResponseHandler() {
            @Override
            public void onFailure(String s) {
                Log.d("111111111", s);
                event.putString("thumb", "");
                promise.resolve(event);
            }

            @Override
            public void onSuccess(String s) {
                String ar = s.split("Output")[1];
                String link = ar.split(".png")[0];
                String url = link.split("'")[1];
                event.putString("thumb", url+".png");
                promise.resolve(event);
            }

            @Override
            public void onProgress(String s) {

            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {
                Log.d("FINISHED: ", "");
            }
        });
    }

    private void loadFFMpegBinary(ReactApplicationContext content) {
        try {
            if (ffmpeg == null) {
                Log.d("TAGG", "ffmpeg : null");
                ffmpeg = FFmpeg.getInstance(content);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                }

                @Override
                public void onSuccess() {
                    Log.d("TAGG", "ffmpeg : correct Loaded");
                }
            });
        } catch (Exception e) {
            Log.d("TAGG", "EXception not supported : " + e);
        }
    }
}
