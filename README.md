# React-Native-CompressVideo
This is a native module use for compress and trimmer video for android solution.

Use:

First step:
Copy 2 pakage compassvideo + trimmer follow url: "android/app/src/main/java/com/apptest/"
And paste in your project.
Don't forget change your pakage import on top.
- in MainApplication.java
+ import your-pakage-name.compassvideo.ComparePackage;
+ in List<ReactPakage> getPackages
  add more new ComparePackage().

- To use native library you have to import native modules react-native

import { NativeModules, DeviceEventEmitter } from 'react-native'

const { CompareVideo, TRIMVIDEO } = NativeModules

// Function Solution
// Compress video function
// on my project, I only need video about 3->5mb/15second.
// you can edit quality for video in function CompareVideo/Compare()
// bitrate hash code: ~ 2*1000*1000
const onCompressVideo = async uri => {
  return CompareVideo.Compare(uri).then(data => {
    return data.path
  })
}

// get asset thumbnail video
// perTime -> seconds
// get a picture at time in Video, you can pass input with time is any in duration of video
const getAssetThumnail = async (uri, perTime) => {
  return CompareVideo.getThumbnailVideo(uri, perTime).then(result => {
    return result.image
  })
}

// Get Duration video
const getDuartion = async source => {
  return CompareVideo.getDurationCallBack(source)
}

// TRIM VIDEO
const onTrimVideo = async (startTime, endTime, source) => {
  // Trim video with start time and end time
  // if(result) === 504 -> failed
  return TRIMVIDEO.trimVideo(source, startTime, endTime).then(outPut => {
    const result = outPut.link
    if (result === '504') {
      return null
    }
    return result
  })
}

// in Trim video you can get progress with solution event DeviceEventEmitter in React Native
// note: the name "proGress"is obligatory. you can edit it in class CompareVideo
this.subscription = DeviceEventEmitter.addListener('proGress', value => {
    console.log('value', value)
  })h

