package com.docubox.data.remote.api

import com.docubox.data.modes.remote.MessageResponse
import com.docubox.data.modes.remote.requests.*
import com.docubox.data.modes.remote.responses.StorageConsumption
import com.docubox.data.modes.remote.responses.file.GetFileResponse
import com.docubox.data.modes.remote.responses.folder.CreateFolderResponse
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

    @POST("documents/create-folder")
    suspend fun createFolder(
        @Body body: CreateFolderRequest,
        @Header("Authorization") token: String
    ): Response<CreateFolderResponse>

    @POST("documents/get-files-shared-to-me")
    suspend fun getFilesSharedToMe(
        @Header("Authorization") token: String
    ): Response<GetFileResponse>

    @POST("documents/get-files-shared-by-me")
    suspend fun getFilesSharedByMe(
        @Header("Authorization") token: String
    ): Response<GetFileResponse>

    @POST("documents/share-file")
    suspend fun shareFile(
        @Body body: ShareFileRequest,
        @Header("Authorization") token: String
    ): Response<MessageResponse>

    @POST("documents/revoke-file")
    suspend fun revokeFile(
        @Body body: RevokeFileRequest,
        @Header("Authorization") token: String
    ): Response<MessageResponse>

    @POST("documents/delete-file")
    suspend fun deleteFile(
        @Body body: Map<String, String>,
        @Header("Authorization") token: String
    ): Response<MessageResponse>

    @POST("documents/delete-folder")
    suspend fun deleteFolder(
        @Body body: Map<String, String>,
        @Header("Authorization") token: String
    ): Response<MessageResponse>

    @POST("documents/storage-consumption")
    suspend fun getStorageConsumption(
        @Header("Authorization") token: String
    ): Response<StorageConsumption>
}
