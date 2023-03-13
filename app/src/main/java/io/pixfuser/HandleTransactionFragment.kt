package io.pixfuser

import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iwebpp.crypto.TweetNacl
import io.pixfuser.databinding.FragmentFirstBinding

class HandleTransactionFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!
    private var pubKey: ByteArray? = null
    private var sharedPubKey: String? = null
    private var privateKey: ByteArray? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        with(TweetNacl.Box.keyPair()){
            pubKey = this.publicKey
            privateKey = this.publicKey
        }

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())


        val message = NdefMessage(pubKey)
        nfcAdapter.setNdefPushMessage(message, requireActivity())


        binding.connectWallet.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}