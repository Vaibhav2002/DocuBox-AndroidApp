package com.docubox.data.remote.dataSources

import com.docubox.data.modes.remote.requests.*
import com.docubox.data.remote.api.StorageService
import com.docubox.util.extensions.asJwt
import com.docubox.util.safeApiCall
import javax.inject.Inject

class StorageDataSource @Inject constructor(private val service: StorageService) {

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
        service.deleteFile(mapOf("folderId" to folderId), token.asJwt())
    }
}