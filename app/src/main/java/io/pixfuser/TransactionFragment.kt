package io.pixfuser

import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.phantomBridge.PhantomBridge
import io.pixfuser.databinding.FragmentSecondBinding

class TransactionFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private var nfcAdapter: NfcAdapter? = null
    private val phantomBridge = PhantomBridge()
    private var pubKey: ByteArray? = null
    private var sharedPubKey: ByteArray? = null
    private var privateKey: ByteArray? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())

        binding.btnSendTransaction.setOnClickListener {
            if (binding.cbTransactionWithoutReceiver.isChecked) {

                val transaction = phantomBridge.createTransaction()
                val encryptor = Encryptor(sharedPubKey!!, privateKey!!)
                val transactionMessage =  NdefMessage(encryptor.encryptPayload(transaction))
                val message = NdefMessage(pubKey)

                nfcAdapter?.setNdefPushMessage(message, requireActivity())
                nfcAdapter?.setNdefPushMessage(transactionMessage, requireActivity())

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
    }

    fun handleNfcMessage(){
        val rawMsgs: Array<Parcelable> =
            requireActivity().intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)!!
        var msg: NdefMessage? = null
        if (rawMsgs != null && rawMsgs.size > 0) {
            msg = rawMsgs[0] as NdefMessage
        }
        if (msg != null) {
            sharedPubKey = msg.toByteArray()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}