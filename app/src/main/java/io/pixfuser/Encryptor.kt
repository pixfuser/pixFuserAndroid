package io.pixfuser

import com.iwebpp.crypto.TweetNacl
import io.phantomBridge.Base58

class Encryptor(private val publicKey: ByteArray, private val privateKey: ByteArray) {

    fun encryptPayload(payload: String) =
        TweetNacl.Box(publicKey, privateKey)
            .box(payload.toByteArray()
    )

    fun decryptData(data: String) = String(
        TweetNacl.Box(publicKey, privateKey)
            .open(Base58.decode(data))
    )

}