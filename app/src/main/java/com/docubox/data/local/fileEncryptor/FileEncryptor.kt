package com.docubox.data.local.fileEncryptor

import com.docubox.util.Resource

interface FileEncryptor {

    suspend fun encryptFile(byteArray: ByteArray): Resource<ByteArray>

    suspend fun decryptFile(encryptedBytes: ByteArray): Resource<ByteArray>
}
