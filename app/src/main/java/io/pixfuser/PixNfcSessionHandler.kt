package io.pixfuser

object PixNfcSessionHandler {

    val nfcCallback: NfcCallback? = null

    fun sharedPubReceived(byteArray: ByteArray) {
        nfcCallback?.sharedPubReceived(byteArray)
    }

    fun payloadReceived(byteArray: ByteArray){
        nfcCallback?.payloadReceived(byteArray)
    }
}