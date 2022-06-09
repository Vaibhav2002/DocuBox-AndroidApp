package com.docubox.data.local.fileEncryptor

import com.docubox.util.EncryptionDetails
import com.docubox.util.Resource
import com.google.common.truth.Truth.assertThat
import com.rockaport.alice.Alice
import com.rockaport.alice.AliceContextBuilder
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class AliceFileEncryptorTest {

    private lateinit var alice: Alice
    private lateinit var aliceFileEncryptor: AliceFileEncryptor

    private fun getAlice(): Alice {
        val aliceContext = AliceContextBuilder().setAlgorithm(EncryptionDetails.algo)
            .setMode(EncryptionDetails.mode)
            .setIvLength(EncryptionDetails.ivLength)
            .setGcmTagLength(EncryptionDetails.gcmTagLength)
            .build()
        return Alice(aliceContext)
    }

    @Before
    fun setUp() {
        alice = getAlice()
        aliceFileEncryptor = AliceFileEncryptor(alice)
    }

    @Test
    fun `file is encrypted correctly`() = runTest {
        val sampleBytes = ByteArray(20)
        Random.nextBytes(sampleBytes)
        val encryptedBytes = aliceFileEncryptor.encryptFile(sampleBytes)
        assertThat(encryptedBytes).isInstanceOf(Resource.Success::class.java)
        assertThat(sampleBytes).isNotEqualTo(encryptedBytes.data)
    }

    @Test
    fun `file is decrypted correctly`() = runTest {
        val sampleBytes = ByteArray(20)
        Random.nextBytes(sampleBytes)
        val encryptedBytes = aliceFileEncryptor.encryptFile(sampleBytes)

        // success encryption
        assertThat(encryptedBytes).isInstanceOf(Resource.Success::class.java)
        assertThat(sampleBytes).isNotEqualTo(encryptedBytes.data)

        val decryptedBytes = aliceFileEncryptor.decryptFile(encryptedBytes.data!!)

        // success decryption
        assertThat(decryptedBytes).isInstanceOf(Resource.Success::class.java)
        assertThat(sampleBytes).isEqualTo(decryptedBytes.data)
    }
}
