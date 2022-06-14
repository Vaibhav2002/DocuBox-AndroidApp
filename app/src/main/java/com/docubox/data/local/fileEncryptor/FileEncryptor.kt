package com.docubox.data.local.fileEncryptor

import com.docubox.util.Resource

// An interface to encrypt and decrypt the files
interface FileEncryptor {

    suspend fun encryptFile(byteArray: ByteArray): Resource<ByteArray>

    suspend fun decryptFile(encryptedBytes: ByteArray): Resource<ByteArray>
}
