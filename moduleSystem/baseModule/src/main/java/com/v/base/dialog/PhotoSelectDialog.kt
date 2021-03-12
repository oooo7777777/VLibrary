package com.v.base.dialog

import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import com.orhanobut.logger.Logger
import com.qw.photo.CoCo
import com.qw.photo.callback.CoCoAdapter
import com.qw.photo.callback.CoCoCallBack
import com.qw.photo.pojo.PickResult
import com.qw.photo.pojo.TakeResult
import com.v.base.BlankViewModel
import com.v.base.R
import com.v.base.databinding.BaseDialogPhotoSelectBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class PhotoSelectDialog : BaseDialogFragment<BaseDialogPhotoSelectBinding, BlankViewModel>(),
    View.OnClickListener {

    override fun useDirection(): Int {
        return DIRECTION_BOTTOM
    }

    private var listener: PhotoSelectDialogListener? = null

    fun setPhotoSelectDialogListener(listener: PhotoSelectDialogListener):PhotoSelectDialog
    {
        this.listener = listener
        return this
    }

    override fun initData() {
        mViewBinding.v = this
    }

    override fun createObserver() {
    }


    interface PhotoSelectDialogListener {
        fun onSuccess(file: File)
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.tvCamera -> {
                CoCo.with(this)
                    .take(createSDCardFile())
                    .start(object : CoCoCallBack<TakeResult> {

                        override fun onSuccess(data: TakeResult) {
                            listener?.run {
                                onSuccess(data.savedFile!!)
                            }
                            dismiss()
                        }

                        override fun onFailed(exception: Exception) {}
                    })
            }
            R.id.tvPhoto -> {
                CoCo.with(this)
                    .pick()
                    .start(object : CoCoAdapter<PickResult>() {
                        override fun onSuccess(data: PickResult) {
                            listener?.run {
                                onSuccess(getFilePathForN(data.originUri)!!)
                            }
                            dismiss()
                        }
                    })
            }
        }
    }


    private fun createSDCardFile(): File {
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


    fun getFilePathForN(uri: Uri): File?
    {
        try
        {
            val returnCursor = mContext.getContentResolver().query(uri, null, null, null, null)
            val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            val name = returnCursor.getString(nameIndex)
            val file = File(mContext.getFilesDir(), name)
            val inputStream = mContext.getContentResolver().openInputStream(uri)
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

            do
            {
                read = inputStream.read(buffers)
                if (read != -1)
                {
                    outputStream.write(buffers, 0, read)
                } else
                {
                    break
                }
            } while (true)

            returnCursor.close()
            inputStream.close()
            outputStream.close()
            return file
        } catch (e: Exception)
        {
            e.printStackTrace()
            Logger.e("getFilePathForN$e")
        }

        return null
    }

}