/* eslint-disable camelcase */
/* eslint-disable no-unused-expressions */
import { NativeModules, DeviceEventEmitter } from 'react-native'

const { CompareVideo, TRIMVIDEO } = NativeModules

// Dung trong truong hop su dung function CompressVideo. su kien lang nghe bat buoc phai la proGress
// Neu muon thay doi, hay thay doi name nay trong code native
const getProgress = () => {
  // get ra progress khi nén video, nó sẽ được ném trong hàm Didmount
  this.subscription = DeviceEventEmitter.addListener('proGress', value => {
    console.log('value', value)
  })
}

const removeEnvetProgress = () => {
  DeviceEventEmitter.removeListener('proGress')
}

// Compress Video
const onCompressVideo = async uri => {
  // trong truong hop Video chua duoc luu, (recording video) luu video truoc -> su dung MediaStore để lấy ra đường dẫn Media cho nó
  // uri co dang: "content://....."
  return CompareVideo.Compare(uri).then(data => {
    return data.path
  })
}
// TRIM VIDEO
const onTrimVideo = async (startTime, endTime, source) => {
  // Cắt Video với thời gian đầu và kết thuc
  // uri Source // "content:.."
  return TRIMVIDEO.trimVideo(source, startTime, endTime).then(outPut => {
    const result = outPut.link
    if (result === '504') {
      return null
    }
    return result
  })
}

// Get Duration voi thoi duong dan la bat ki khi ghi hinh
const getDuartion = async source => {
  return CompareVideo.getDurationCallBack(source)
}

// get asset thumbnail video
// perTime -> seconds
const getAssetThumnail = async (uri, perTime) => {
  return CompareVideo.getThumbnailVideo(uri, perTime).then(result => {
    return result.image
  })
}

export {
  onTrimVideo,
  onCompressVideo,
  getDuartion,
  getProgress,
  removeEnvetProgress,
  getAssetThumnail,
}
