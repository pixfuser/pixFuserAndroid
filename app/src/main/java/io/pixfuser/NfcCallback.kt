package io.pixfuser

interface NfcCallback {

    fun sharedPubReceived(sharedPub: ByteArray)

    fun payloadReceived(payload: ByteArray)
}