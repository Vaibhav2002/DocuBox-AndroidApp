package com.docubox.domain.repo

import com.docubox.data.modes.local.StorageItem
import com.docubox.data.modes.remote.MessageResponse
import com.docubox.data.modes.remote.responses.StorageConsumption
import com.docubox.util.Resource
import kotlinx.coroutines.flow.Flow

interface StorageRepo {

    suspend fun getAllFiles(fileDirectory: String?): Flow<Resource<List<StorageItem.File>>>

    suspend fun getAllFolders(folderParentDirectory: String?): Flow<Resource<List<StorageItem.Folder>>>

    suspend fun createFolder(folderName: String, folderDirectory: String): Flow<Resource<StorageItem.Folder>>

    suspend fun getFilesSharedByMe(): Flow<Resource<List<StorageItem.File>>>

    suspend fun getFilesSharedToMe(): Flow<Resource<List<StorageItem.File>>>

    suspend fun shareFile(fileId: String, email: String): Flow<Resource<MessageResponse>>

    suspend fun revokeShareFile(fileId: String, email: String): Flow<Resource<MessageResponse>>

    suspend fun deleteFile(fileId: String): Flow<Resource<MessageResponse>>

    suspend fun deleteFolder(folderId: String): Flow<Resource<MessageResponse>>

    suspend fun getStorageConsumption(): Flow<Resource<StorageConsumption>>

    suspend fun searchFileByQuery(query: String): Flow<Resource<List<StorageItem.File>>>

    suspend fun searchFileByType(type: String): Flow<Resource<List<StorageItem.File>>>

    suspend fun downloadFile(file: StorageItem.File)

    suspend fun renameFile(
        file: StorageItem.File,
        newName: String
    ): Flow<Resource<MessageResponse>>

    suspend fun renameFolder(
        folder: StorageItem.Folder,
        newName: String
    ): Flow<Resource<MessageResponse>>

    suspend fun getStorageConsumptionValue(): StorageConsumption?
}