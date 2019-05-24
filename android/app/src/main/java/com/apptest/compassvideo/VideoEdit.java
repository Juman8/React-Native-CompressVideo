package com.apptest.compassvideo;

import android.support.annotation.Nullable;

public class VideoEdit {

  private static final String TAG = "RNVideoEdit";

  public static boolean shouldUseURI(@Nullable String path) {
    String[] supportedProtocols = {
            "content://",
            "file://",
            "http://",
            "https://"
    };
    if (path == null) {
      return false;
    }
    boolean lookupWithURI = false;
    for (String protocol : supportedProtocols) {
      if (path.toLowerCase().startsWith(protocol)) {
        lookupWithURI = true;
        break;
      }
    }
    return lookupWithURI;
  }
}
