package com.docubox.data.repo

import com.docubox.data.modes.local.StorageItem
import com.docubox.data.modes.mapper.FileMapper
import com.docubox.data.modes.mapper.FolderMapper
import com.docubox.data.modes.remote.responses.StorageConsumption
import com.docubox.data.remote.dataSources.StorageDataSource
import com.docubox.util.Resource
import com.docubox.util.extensions.mapMessages
import com.docubox.util.extensions.mapTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageRepo @Inject constructor(
    private val storageDataSource: StorageDataSource,
    private val preferencesRepo: PreferencesRepo,
    private val fileMapper: FileMapper,
    private val folderMapper: FolderMapper
) {

    private val token
        get() = preferencesRepo.getUserToken()!!

    suspend fun getAllFiles(fileDirectory: String?) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.getAllFiles(fileDirectory, token))
    }.map { resource -> //mapping all files in GetFileResponse model to list of Local StorageItem.File
        resource mapTo { fileMapper.toLocal(it.fileList) }
    }.flowOn(Dispatchers.IO)

    suspend fun getAllFolders(folderParentDirectory: String?) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.getAllFolders(folderParentDirectory, token))
    }.map { resource -> //mapping all folders in GetFolderResponse model to list of Local StorageItem.Folder
        resource mapTo { folderMapper.toLocal(it.folderList) }
    }.flowOn(Dispatchers.IO)

    suspend fun createFolder(folderName: String, folderDirectory: String) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.createFolder(folderName, folderDirectory, token))
    }.map { res ->
        res.mapTo { folderMapper.toLocal(it.folder) }
    }.flowOn(Dispatchers.IO)

    suspend fun getFilesSharedByMe() = flow {
        emit(Resource.Loading())
        emit(storageDataSource.getFilesSharedByMe(token))
    }.map { res ->
        res.mapTo { fileMapper.toLocal(it.fileList) }
    }.flowOn(Dispatchers.IO)

    suspend fun getFilesSharedToMe() = flow {
        emit(Resource.Loading())
        emit(storageDataSource.getFilesSharedToMe(token))
    }.map { res ->
        res.mapTo { fileMapper.toLocal(it.fileList) }
    }.flowOn(Dispatchers.IO)

    suspend fun shareFile(fileId: String, email: String) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.shareFile(fileId, email, token))
    }.map { res ->
        res.mapMessages(successMessage = res.data?.message)
    }.flowOn(Dispatchers.IO)

    suspend fun revokeShareFile(fileId: String, email: String) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.revokeFile(fileId, email, token))
    }.map { res ->
        res.mapMessages(successMessage = res.data?.message)
    }.flowOn(Dispatchers.IO)

    suspend fun deleteFile(fileId: String) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.deleteFile(fileId, token))
    }.map { res ->
        res.mapMessages(successMessage = res.data?.message)
    }.flowOn(Dispatchers.IO)

    suspend fun deleteFolder(folderId: String) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.deleteFolder(folderId, token))
    }.map { res ->
        res.mapMessages(successMessage = res.data?.message)
    }.flowOn(Dispatchers.IO)

    suspend fun getStorageConsumption() = flow {
        emit(Resource.Loading())
        emit(storageDataSource.getStorageConsumption(token))
    }.flowOn(Dispatchers.IO)

    suspend fun searchFileByQuery(query: String) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.searchFilesByName(query, token))
    }.map { res ->
        res.mapTo { fileMapper.toLocal(it.fileList) }
    }.flowOn(Dispatchers.IO)

    suspend fun searchFileByType(type: String) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.searchFilesByType(type, token))
    }.map { res ->
        res.mapTo { fileMapper.toLocal(it.fileList) }
    }.flowOn(Dispatchers.IO)

    suspend fun downloadFile(file: StorageItem.File) {
        storageDataSource.downloadFile(file)
    }

    suspend fun renameFile(
        file: StorageItem.File,
        newName: String
    ) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.renameFile(file, newName, token))
    }.map {
        it.mapMessages(successMessage = it.data?.message)
    }.flowOn(Dispatchers.IO)

    suspend fun renameFolder(
        folder: StorageItem.Folder,
        newName: String
    ) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.renameFolder(folder, newName, token))
    }.map {
        it.mapMessages(successMessage = it.data?.message)
    }.flowOn(Dispatchers.IO)

    suspend fun getStorageConsumptionValue(): StorageConsumption? {
        return storageDataSource.getStorageConsumption(token).data
    }
}