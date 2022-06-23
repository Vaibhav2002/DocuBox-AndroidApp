package com.docubox.data.remote.dataSources

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.docubox.data.modes.local.StorageItem
import com.docubox.data.modes.remote.requests.*
import com.docubox.data.remote.api.StorageService
import com.docubox.util.FileUtil
import com.docubox.util.extensions.asJwt
import com.docubox.util.runSafeAsync
import com.docubox.util.safeApiCall
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class StorageDataSource @Inject constructor(
    private val service: StorageService,
    @ApplicationContext private val context: Context
) {

    suspend fun getAllFiles(fileDirectory: String?, token: String) = safeApiCall {
        service.getFiles(GetFileRequest(fileDirectory), token.asJwt())
    }

    suspend fun getAllFolders(folderParentDirectory: String?, token: String) = safeApiCall {
        service.getFolders(GetFolderRequest(folderParentDirectory), token.asJwt())
    }

    suspend fun createFolder(
        folderName: String,
        folderDirectory: String,
        token: String
    ) = safeApiCall {
        service.createFolder(CreateFolderRequest(folderName, folderDirectory), token.asJwt())
    }

    suspend fun getFilesSharedByMe(token: String) = safeApiCall {
        service.getFilesSharedByMe(token.asJwt())
    }

    suspend fun getFilesSharedToMe(token: String) = safeApiCall {
        service.getFilesSharedToMe(token.asJwt())
    }

    suspend fun shareFile(fileId: String, toEmail: String, token: String) = safeApiCall {
        service.shareFile(ShareFileRequest(fileId, toEmail), token.asJwt())
    }

    suspend fun revokeFile(fileId: String, ofEmail: String, token: String) = safeApiCall {
        service.revokeFile(RevokeFileRequest(fileId, ofEmail), token.asJwt())
    }

    suspend fun deleteFile(fileId: String, token: String) = safeApiCall {
        service.deleteFile(mapOf("fileId" to fileId), token.asJwt())
    }

    suspend fun deleteFolder(folderId: String, token: String) = safeApiCall {
        service.deleteFolder(mapOf("folderId" to folderId), token.asJwt())
    }

    suspend fun getStorageConsumption(token: String) = safeApiCall {
        service.getStorageConsumption(token.asJwt())
    }

    suspend fun searchFilesByName(query: String, token: String) = safeApiCall {
        service.searchFilesByName(mapOf("fileNameQuery" to query), token.asJwt())
    }

    suspend fun searchFilesByType(query: String, token: String) = safeApiCall {
        service.searchFilesByType(mapOf("fileTypeQuery" to query), token.asJwt())
    }

    private fun download() {

    }

    suspend fun downloadFile(file: StorageItem.File) = runSafeAsync {
        try {
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val fileStorageUrl = FileUtil.getParsedFileUrl(file.file.fileStorageUrl)

            Timber.d("DocuBox Download: File Storage Url = $fileStorageUrl")

            val uri = Uri.parse(fileStorageUrl)
            DownloadManager.Request(uri).apply {
                setTitle(file.file.fileName)
                setDescription("Downloading")
                setMimeType(FileUtil.getMimeTypeFromFile(file.file.fileName))
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file.file.fileName);
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                manager.enqueue(this)
            }
        } catch(e: Exception) {
            Timber.d("DocuBox Download Error:\n${e.localizedMessage}\n${e.localizedMessage}\n\n")
        }
    }

    suspend fun renameFile(file: StorageItem.File, newName: String, token: String) = safeApiCall {
        service.renameFile(RenameFileRequest(file.id, newName), token.asJwt())
    }

    suspend fun renameFolder(folder: StorageItem.Folder, newName: String, token: String) = safeApiCall {
        service.renameFolder(RenameFolderRequest(folder.id, newName), token.asJwt())
    }
}