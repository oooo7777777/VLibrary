package com.v.base.model

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.text.format.Formatter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.forjrking.lubankt.Luban
import com.orhanobut.logger.Logger
import com.v.base.BaseViewModel
import com.v.base.utils.log
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * author  : ww
 * desc    : 图片处理
 * time    : 2021-03-16 09:52:45
 */
class ImgSelectViewModel : BaseViewModel() {

    var fileSuccess = MutableLiveData<File>()

    fun createSDCardFile(mContext: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = File(mContext.externalCacheDir!!.path + "/" + timeStamp)
        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        return File.createTempFile(
            "JPEG_${timeStamp}", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }


    fun getFilePathForN(mContext: Context, uri: Uri): File? {
        try {
            val returnCursor = mContext.contentResolver.query(uri, null, null, null, null)
            val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            val name = returnCursor.getString(nameIndex)
            val file = File(mContext.filesDir, name)
            val inputStream = mContext.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable = inputStream!!.available()

            val bufferSize = Math.min(bytesAvailable, maxBufferSize)

            val buffers = ByteArray(bufferSize)

//            while ((read = inputStream.read(buffers)) != -1)
//            {
//                outputStream.write(buffers, 0, read)
//            }

            do {
                read = inputStream.read(buffers)
                if (read != -1) {
                    outputStream.write(buffers, 0, read)
                } else {
                    break
                }
            } while (true)

            returnCursor.close()
            inputStream.close()
            outputStream.close()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("getFilePathForN$e")
        }

        return null
    }

    fun formatSize(mContext: Context, size: Long): String {
        return Formatter.formatFileSize(mContext, size)
    }

    fun imageCompression(mContext: Fragment, file: File, ignoreBy: Long) {

//        .quality(100)                     //(可选)质量压缩系数  0-100
        Luban.with(mContext)
            .load(file)                  //支持 File,Uri,InputStream,String,Bitmap 和以上数据数组和集合
            .ignoreBy(ignoreBy)          //(可选)期望大小,大小和图片呈现质量不能均衡所以压缩后不一定小于此值,
            .compressObserver {
                onSuccess = {
                    loadingChange.dismissDialog.postValue(false)
                    (formatSize(mContext.requireContext(), it.length())).log()
                    fileSuccess.value = it
                }
                onStart = {
                    loadingChange.showDialog.postValue("")
                }
                onCompletion = {
                }
                onError = { e, _ ->
                    loadingChange.dismissDialog.postValue(false)
                }
            }.launch()
    }
}