package com.apptest.compassvideo;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.daasuu.mp4compose.composer.Mp4Composer;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import static com.beetsoft.Capitri.compassvideo.VideoEdit.shouldUseURI;


public class CompareVideo extends ReactContextBaseJavaModule {

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

    public CompareVideo(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "CompareVideo";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

    @ReactMethod
    public void getInfomation(String url, Callback successCallback) {
        successCallback.invoke(url, "false roi");
    }

    // Lay ra anh thumnail
    @ReactMethod
    public void getThumbnailVideo(String path, int perTime, Promise callback) {

        WritableMap event = Arguments.createMap();

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        if (shouldUseURI(path)) {
            retriever.setDataSource(getReactApplicationContext(), Uri.parse(path));
        } else {
            retriever.setDataSource(path);
        }

        int resizeWidth = 50;
        int resizeHeight = 100;

        Matrix mx = new Matrix();

        mx.postScale(resizeWidth, resizeHeight);

        Bitmap frame = retriever.getFrameAtTime(perTime * 1000 * 1000, retriever.OPTION_NEXT_SYNC);

        if (frame == null) {
            event.putString("image", "");
            return;
        } else {
            Bitmap currBmp = Bitmap.createScaledBitmap(frame, resizeWidth, resizeHeight, false);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            currBmp.compress(Bitmap.CompressFormat.PNG, 30, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = "data:image/png;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
            event.putString("image", encoded);
        }
        callback.resolve(event);

    }


    // Lấy ra thời gian của file video
    @ReactMethod
    public void getDurationCallBack(String videoPath, Promise promise) {
//        Log.e("duration", "start get duration");
        Double time= 0d;
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            if (shouldUseURI(videoPath)) {
                retriever.setDataSource(getReactApplicationContext(), Uri.parse(videoPath));
            } else {
                retriever.setDataSource(videoPath);
            }
            String str = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//            Log.e("str", str);
            if (str != null)
            time = Double.parseDouble(str);
            else time = 0d;
        }catch (Exception e){
            time = 0d;
        }

        WritableMap event = Arguments.createMap();
//        Log.e("duration", String.valueOf(time));
        event.putDouble("duration", time);
        //        successCallback.invoke(time);
        promise.resolve(event);
    }


    private String readableFileSize(long size) {
        if (size <= 0) return size + " B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups))
                + " "
                + units[digitGroups];
    }

    // @ReactMethod
    public int getBitrate(String pathInput) throws Exception{
        int bitRate = 0;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        if (shouldUseURI(pathInput)) {
            retriever.setDataSource(getReactApplicationContext(), Uri.parse(pathInput));
        } else {
            retriever.setDataSource(pathInput);
        }
        String str = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        bitRate = Integer.parseInt(str);
        return bitRate;
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable Double params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @ReactMethod
    public void Compare(String input, Promise promise) throws Exception {

        // Tao ra mot file moi
        //Log.e("input"," file: " + input);
        File file = new File(input);
        if (!file.exists())return;

        // tao mot file moi
        String outPut = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
        final File tempFile = File.createTempFile(outPut+"", ".mp4");

        String pathOutput = tempFile.getPath();

        WritableMap event = Arguments.createMap();

        new Mp4Composer(input, pathOutput)
                .size(540, 960)
//                .fillMode(FillMode.PRESERVE_ASPECT_FIT)
                .videoBitrate(2000000)
//                .filter(new GlWatermarkFilter()) // filter cho man hinh video
                .listener(new Mp4Composer.Listener() {
                    // tieens trinh khi nen video
                    @Override
                    public void onProgress(double progress) {
                        sendEvent(getReactApplicationContext(), "proGress", progress);
                    }

                    // Sau khi nen thanh cong Khi thanh cong
                    @Override
                    public void onCompleted() {
                        event.putString("path", pathOutput);
                        promise.resolve(event);
                    }

                    @Override
                    public void onCanceled() {
                        Log.d("Destroy", "onCanceled");
//                        processing.invoke("Falied");
                    }

                    //That bai
                    @Override
                    public void onFailed(Exception exception) {
                        event.putString("path", "");
                        promise.resolve(event);
                    }
                })
                .start();
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

}
