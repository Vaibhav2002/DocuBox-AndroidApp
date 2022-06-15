package com.docubox.data.remote.api

import com.docubox.data.modes.remote.requests.GetFileRequest
import com.docubox.data.modes.remote.requests.GetFolderRequest
import com.docubox.data.modes.remote.responses.file.GetFileResponse
import com.docubox.data.modes.remote.responses.folder.GetFolderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface StorageService {

    @POST("documents/get-folders-in-folder")
    suspend fun getFolders(
        @Body body: GetFolderRequest,
        @Header("Authorization") token: String
    ): Response<GetFolderResponse>

    @POST("documents/get-files-in-folder")
    suspend fun getFiles(
        @Body body: GetFileRequest,
        @Header("Authorization") token: String
    ): Response<GetFileResponse>
}
