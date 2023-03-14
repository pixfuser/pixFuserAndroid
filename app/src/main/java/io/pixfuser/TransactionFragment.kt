package io.pixfuser

import android.content.Context
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.phantomBridge.PhantomBridge
import io.pixfuser.InternalConstants.PUB_KEY
import io.pixfuser.InternalConstants.TRANSACTION
import io.pixfuser.databinding.FragmentSecondBinding

class TransactionFragment : Fragment(), NfcCallback {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private var nfcManager: NfcManager? = null
    private val phantomBridge = PhantomBridge()
    private var pubKey: ByteArray? = null
    private var sharedPubKey: ByteArray? = null
    private var privateKey: ByteArray? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nfcManager = requireContext().getSystemService(Context.NFC_SERVICE) as NfcManager

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSendTransaction.setOnClickListener {
            if (binding.cbTransactionWithoutReceiver.isChecked && (nfcManager?.defaultAdapter != null)) {

                 val message = NdefMessage(NdefRecord(PUB_KEY.toByteArray()), NdefRecord(pubKey))

                nfcManager?.defaultAdapter?.setNdefPushMessage(message, requireActivity())

            } else {
                phantomBridge.sendTransaction(
                    requireActivity() as AppCompatActivity,
                    requireActivity().packageManager,
                    resources.getString(R.string.app_scheme),
                    resources.getString(R.string.app_host),
                    resources.getString(R.string.send_transaction_path),
                    binding.enterTransaction.text.toString()
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Phantom Wallet app is not installed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.btnReceiveTransaction.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_HandleTransactionFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun sharedPubReceived(sharedPub: ByteArray) {
        sharedPubKey = sharedPub
        val transaction = phantomBridge.createTransaction()
        val encryptor = Encryptor(sharedPubKey!!, privateKey!!)
        val transactionMessage =  NdefMessage(NdefRecord(TRANSACTION.toByteArray()), NdefRecord(encryptor.encryptPayload(transaction)))
        nfcManager?.defaultAdapter?.setNdefPushMessage(transactionMessage, requireActivity())
    }

    override fun payloadReceived(payload: ByteArray) {
        //todo sign and send transaction
    }
}