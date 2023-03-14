package io.pixfuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.phantomBridge.PhantomBridge
import io.pixfuser.databinding.FragmentFirstBinding

class ConnectWalletFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.connectWallet.setOnClickListener {
            PhantomBridge().connectWallet(
                resources.getString(R.string.app_scheme),
                resources.getString(R.string.app_host),
                resources.getString(R.string.connect_path),
                requireActivity() as AppCompatActivity,
                "https://www.google.com",
                requireActivity().packageManager
            ) {
                Toast.makeText(requireContext(), "Phantom app is not installed ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}