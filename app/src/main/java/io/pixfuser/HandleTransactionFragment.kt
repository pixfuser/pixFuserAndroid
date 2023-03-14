package io.pixfuser

import android.content.Context
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.iwebpp.crypto.TweetNacl
import io.phantomBridge.PhantomBridge
import io.pixfuser.databinding.FragmentHandleTransactionBinding

class HandleTransactionFragment : Fragment(), NfcCallback {

    private var _binding: FragmentHandleTransactionBinding? = null

    private val binding get() = _binding!!
    private var pubKey: ByteArray? = null
    private var sharedPubKey: ByteArray? = null
    private var privateKey: ByteArray? = null
    private var phantomBridge = PhantomBridge()
    private lateinit var nfcManager: NfcManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nfcManager = requireContext().getSystemService(Context.NFC_SERVICE) as NfcManager
        if(nfcManager.defaultAdapter != null){
            val message = NdefMessage(pubKey)
            nfcManager.defaultAdapter.setNdefPushMessage(message, requireActivity())
            with(TweetNacl.Box.keyPair()){
                pubKey = this.publicKey
                privateKey = this.publicKey
            }
        }

        _binding = FragmentHandleTransactionBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun sharedPubReceived(sharedPub: ByteArray) {
        sharedPubKey = sharedPub
        val pubMessage = NdefMessage(NdefRecord(InternalConstants.PUB_KEY.toByteArray()), NdefRecord(pubKey))
        nfcManager.defaultAdapter.setNdefPushMessage(pubMessage, requireActivity())
    }

    override fun payloadReceived(payload: ByteArray) {
        val encryptor = Encryptor(sharedPubKey!!, privateKey!!)
        val editedTransaction = phantomBridge.editTransactionReceiver(encryptor.decryptData(payload), phantomBridge.getWallet()!!)
        val transactionMessage =  NdefMessage(NdefRecord(InternalConstants.TRANSACTION.toByteArray()), NdefRecord(encryptor.encryptPayload(editedTransaction)))
        nfcManager.defaultAdapter.setNdefPushMessage(transactionMessage, requireActivity())
    }
}