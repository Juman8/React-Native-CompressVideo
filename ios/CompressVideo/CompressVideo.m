//
//  CompressVideo.m
//  Capitri
//
//  Created by CaoNT on 5/27/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CompressVideo.h"
#import <UIKit/UIKit.h>
#import <React/RCTEventDispatcher.h>
#import <React/RCTConvert.h>
#import <mobileffmpeg/MobileFFmpeg.h>
#import "SDAVAssetExportSession.h"

@implementation CompressVideo
RCT_EXPORT_MODULE();

RCT_REMAP_METHOD(findEvents,
                 name: (NSString *)name
                 findEventsWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
  int a = 10;
  if(a==10){
  NSString *events = name;
  resolve(events);
  }
}

RCT_REMAP_METHOD(getDuration,
                 path: (NSString *)path
                 findEventsWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
  NSString *documents = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];

  
//  NSString *desktopURL = FileManager.default.url(for: .desktopDirectory, in: .userDomainMask, appropriateFor: nil, create: true)
//  print("desktopURL: " + String(describing: desktopURL));
//  NSString *fileURL = desktopURL.appendingPathComponent("PhongTro").appendingPathExtension("mp4")
  
  MediaInformation *mediaInformation = [MobileFFmpeg getMediaInformation:path];
//   NSLog(@"Command execution cancelled by user.\n" + mediaInformation.getFormat);
  int a = 10;
  if(a==10){
  resolve(mediaInformation.getDuration);
  }else{
    NSString *events = @"505";
    reject(@"no_events", @"There were no events", events);
  }
}

RCT_REMAP_METHOD(comPressVideo,
                 videoUrl: (NSString *)videoUrl
                 findEventsWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
  
  NSDictionary *settings = @{AVVideoCodecKey:AVVideoCodecH264,
                             AVVideoWidthKey:@(540),
                             AVVideoHeightKey:@(960),
                             AVVideoCompressionPropertiesKey:
                               @{AVVideoAverageBitRateKey:@(2000),
                                 AVVideoProfileLevelKey:AVVideoProfileLevelH264Main31, /* Or whatever profile & level you wish to use */
                                 AVVideoMaxKeyFrameIntervalKey:@("1234")}};
  
  AVAssetWriterInput* writer_input = [AVAssetWriterInput assetWriterInputWithMediaType:AVMediaTypeVideo outputSettings:settings];
  int a = 10;
  if(a==10){
    resolve(writer_input);
  }else{
    NSString *events = @"504";
    reject(@"no_events", @"There were no events", events);
  }
}

RCT_REMAP_METHOD(getBitrate,
                 videoPath: (NSString *)videoPath
                 findEventsWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
  MediaInformation *mediaInformation = [MobileFFmpeg getMediaInformation:videoPath];
  //   NSLog(@"Command execution cancelled by user.\n" + mediaInformation.getFormat);
  int a = 10;
  if(a==10){
    resolve(mediaInformation.getBitrate);
  }else{
    NSString *events = @"505";
    reject(@"no_events", @"There were no events", events);
  }
}

@end
