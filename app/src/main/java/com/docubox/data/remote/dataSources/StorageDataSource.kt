package com.docubox.data.remote.dataSources

import com.docubox.data.modes.remote.requests.GetFileRequest
import com.docubox.data.modes.remote.requests.GetFolderRequest
import com.docubox.data.remote.api.StorageService
import com.docubox.util.extensions.asJwt
import com.docubox.util.safeApiCall
import javax.inject.Inject

class StorageDataSource @Inject constructor(private val service: StorageService) {

    suspend fun getAllFiles(fileDirectory: String, token: String) = safeApiCall {
        service.getFiles(GetFileRequest(fileDirectory), token.asJwt())
    }

    suspend fun getAllFolders(folderParentDirectory: String, token: String) = safeApiCall {
        service.getFolders(GetFolderRequest(folderParentDirectory), token.asJwt())
    }
}