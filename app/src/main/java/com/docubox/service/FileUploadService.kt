package com.docubox.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import com.docubox.util.Secrets.BASE_URL
import com.docubox.util.Secrets.BASE_URL_FILE_UPLOAD
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FileUploadService : LifecycleService() {

    companion object {
        private val UPLOAD_FILE_URL = "$BASE_URL_FILE_UPLOAD/documents/create-file"
    }

    private val binder = FileUploadBinder()

    inner class FileUploadBinder : Binder() {
        fun getService() = this@FileUploadService
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    suspend fun uploadFile(file: Uri, fileDirectory: String?, token: String) =
        suspendCoroutine<Boolean> {
            val directory = fileDirectory?.let { dir -> listOf(dir) } ?: emptyList()
            MultipartUploadRequest(this@FileUploadService, UPLOAD_FILE_URL)
                .setMethod("POST")
                .addFileToUpload(filePath = file.toString(), parameterName = "upload")
                .addArrayParameter("fileDirectory", directory)
                .setBearerAuth(token)
                .subscribe(
                    this@FileUploadService,
                    this@FileUploadService,
                    object : RequestObserverDelegate {
                        override fun onCompleted(context: Context, uploadInfo: UploadInfo) {
                            Timber.d("Complete")
                        }

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
                            Timber.d("Success")
                            it.resume(true)
                        }
                    })
        }

}