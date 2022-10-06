package com.docubox.data.repo

import com.docubox.data.local.models.StorageItem
import com.docubox.data.mapper.toLocal
import com.docubox.data.remote.dataSources.StorageDataSource
import com.docubox.domain.repo.PreferenceRepo
import com.docubox.domain.repo.StorageRepo
import com.docubox.data.remote.models.responses.StorageConsumption
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
class StorageRepoImpl @Inject constructor(
    private val storageDataSource: StorageDataSource,
    private val preferencesRepo: PreferencesRepo
) {

    private val token
        get() = preferenceRepo.getUserToken()!!

    override suspend fun getAllFiles(fileDirectory: String?) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.getAllFiles(fileDirectory, token))
    }.map { resource -> //mapping all files in GetFileResponse model to list of Local StorageItem.File
        resource mapTo { it.fileList.toLocal() }
    }.flowOn(Dispatchers.IO)

    override suspend fun getAllFolders(folderParentDirectory: String?) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.getAllFolders(folderParentDirectory, token))
    }.map { resource -> //mapping all folders in GetFolderResponse model to list of Local StorageItem.Folder
        resource mapTo { it.folderList.toLocal() }
    }.flowOn(Dispatchers.IO)

    override suspend fun createFolder(folderName: String, folderDirectory: String) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.createFolder(folderName, folderDirectory, token))
    }.map { res ->
        res.mapTo { it.folder.toLocal() }
    }.flowOn(Dispatchers.IO)

    override suspend fun getFilesSharedByMe() = flow {
        emit(Resource.Loading())
        emit(storageDataSource.getFilesSharedByMe(token))
    }.map { res ->
        res.mapTo { it.fileList.toLocal() }
    }.flowOn(Dispatchers.IO)

    override suspend fun getFilesSharedToMe() = flow {
        emit(Resource.Loading())
        emit(storageDataSource.getFilesSharedToMe(token))
    }.map { res ->
        res.mapTo { it.fileList.toLocal() }
    }.flowOn(Dispatchers.IO)

    override suspend fun shareFile(fileId: String, email: String) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.shareFile(fileId, email, token))
    }.map { res ->
        res.mapMessages(successMessage = res.data?.message)
    }.flowOn(Dispatchers.IO)

    override suspend fun revokeShareFile(fileId: String, email: String) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.revokeFile(fileId, email, token))
    }.map { res ->
        res.mapMessages(successMessage = res.data?.message)
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteFile(fileId: String) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.deleteFile(fileId, token))
    }.map { res ->
        res.mapMessages(successMessage = res.data?.message)
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteFolder(folderId: String) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.deleteFolder(folderId, token))
    }.map { res ->
        res.mapMessages(successMessage = res.data?.message)
    }.flowOn(Dispatchers.IO)

    override suspend fun getStorageConsumption() = flow {
        emit(Resource.Loading())
        emit(storageDataSource.getStorageConsumption(token))
    }.flowOn(Dispatchers.IO)

    override suspend fun searchFileByQuery(query: String) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.searchFilesByName(query, token))
    }.map { res ->
        res.mapTo { it.fileList.toLocal() }
    }.flowOn(Dispatchers.IO)

    override suspend fun searchFileByType(type: String) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.searchFilesByType(type, token))
    }.map { res ->
        res.mapTo { it.fileList.toLocal() }
    }.flowOn(Dispatchers.IO)

    override suspend fun downloadFile(file: StorageItem.File) {
        storageDataSource.downloadFile(file)
    }

    override suspend fun renameFile(
        file: StorageItem.File,
        newName: String
    ) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.renameFile(file, newName, token))
    }.map {
        it.mapMessages(successMessage = it.data?.message)
    }.flowOn(Dispatchers.IO)

    override suspend fun renameFolder(
        folder: StorageItem.Folder,
        newName: String
    ) = flow {
        emit(Resource.Loading())
        emit(storageDataSource.renameFolder(folder, newName, token))
    }.map {
        it.mapMessages(successMessage = it.data?.message)
    }.flowOn(Dispatchers.IO)

    override suspend fun getStorageConsumptionValue(): StorageConsumption? {
        return storageDataSource.getStorageConsumption(token).data
    }
}