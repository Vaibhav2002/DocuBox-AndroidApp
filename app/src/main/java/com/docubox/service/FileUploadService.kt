package com.docubox.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import com.docubox.util.FileUtil
import com.docubox.util.Secrets.BASE_URL
import com.docubox.util.extensions.showToast
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FileUploadService : LifecycleService() {

    companion object {
        private const val UPLOAD_FILE_URL = BASE_URL + "documents/create-file"
    }

    private val binder = FileUploadBinder()

    inner class FileUploadBinder : Binder() {
        fun getService() = this@FileUploadService
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    suspend fun uploadFile(
        file: Uri,
        fileDirectory: String?,
        storageLeft:Float,
        token: String,
        lifeCycleOwner: LifecycleOwner
    ) = suspendCoroutine<Boolean> {
        val fileSize = FileUtil.getFileSize(this, file)
        if (fileSize > storageLeft) {
            showToast("Unable to upload file, you have crossed your file storage limit")
            it.resume(false)
        }
        val directory = fileDirectory?.let { dir -> listOf(dir) } ?: emptyList()
        MultipartUploadRequest(this@FileUploadService, UPLOAD_FILE_URL)
            .setMethod("POST")
            .addFileToUpload(filePath = file.toString(), parameterName = "upload")
            .addArrayParameter("fileDirectory", directory)
            .setBearerAuth(token)
            .subscribe(
                this@FileUploadService,
                lifeCycleOwner,
                object : RequestObserverDelegate {
                    override fun onCompleted(context: Context, uploadInfo: UploadInfo) = Unit

                    override fun onCompletedWhileNotObserving() = Unit

                    override fun onError(
                        context: Context,
                        uploadInfo: UploadInfo,
                        exception: Throwable
                    ) {
                        Timber.d("DOCUBOX_ERROR: ${exception.message}")
                        it.resume(false)
                    }

                    override fun onProgress(context: Context, uploadInfo: UploadInfo) = Unit

                    override fun onSuccess(
                        context: Context,
                        uploadInfo: UploadInfo,
                        serverResponse: ServerResponse
                    ) {
                        it.resume(true)
                    }
                })
    }

}