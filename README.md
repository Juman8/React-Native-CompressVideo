# React-Native-CompressVideo
This is a native module use for compress and trimmer video for android solution.

## Use:

##  First step:
Copy 2 pakage compassvideo + trimmer follow url: "android/app/src/main/java/com/apptest/"
And paste in your project.
Don't forget change your pakage import on top.
## Continue
MainApplication.java
```sh
+ import your-pakage-name.compassvideo.ComparePackage;
+ in List<ReactPakage> getPackages
  add more new ComparePackage().

- To use native library you have to import native modules react-native

import { NativeModules, DeviceEventEmitter } from 'react-native'

const { CompareVideo, TRIMVIDEO } = NativeModules
```
Compare(uri) function
```sh
 Function Solution
 Compress video function
 on my project, I only need video about 3->5mb with video 15second.
 you can edit quality for video in function CompareVideo/Compare()
 bitrate hash code: ~ 2*1000*1000
```
```sh
getThumbnailVideo(uri, perTime) function
 get asset thumbnail video with video 1.30p take about 5 second (with 1s/image)
 perTime -> seconds
 get a picture at time in Video, you can pass input with time is any in duration of video
```
```sh
getDurationCallBack(source) function
 Get Duration video
```
```sh
trimVideo(source, startTime, endTime) fucntion
 TRIM VIDEO
```
```sh
In Trim video you can get progress with solution event DeviceEventEmitter in React Native
Note: the name "proGress"is obligatory. you can edit it in class CompareVideo

this.subscription = DeviceEventEmitter.addListener('proGress', value => {
    console.log('value', value)
  })h
```

**More than in example/index.js**
