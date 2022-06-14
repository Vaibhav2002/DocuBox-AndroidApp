package com.docubox.data.local.fileEncryptor

import com.docubox.util.EncryptionDetails
import com.docubox.util.Resource
import com.docubox.util.runSafeAsync
import com.rockaport.alice.Alice
import javax.inject.Inject

// Class to implement file encryption and decryption functions using FileEncryptor interface
class AliceFileEncryptor @Inject constructor(private val alice: Alice) : FileEncryptor {
    override suspend fun encryptFile(byteArray: ByteArray): Resource<ByteArray> = runSafeAsync {
        alice.encrypt(byteArray, EncryptionDetails.password)
    }

    override suspend fun decryptFile(encryptedBytes: ByteArray): Resource<ByteArray> =
        runSafeAsync {
            alice.decrypt(encryptedBytes, EncryptionDetails.password)
        }
}
