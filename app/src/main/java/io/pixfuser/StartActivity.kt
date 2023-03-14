package io.pixfuser

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import io.phantomBridge.PhantomHandler
import io.pixfuser.databinding.ActivityMainBinding

class StartActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val phantomHandler = PhantomHandler()
    private lateinit var nfcAdapter: NfcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        if(phantomHandler.isWalletConnected()){
            navController.navigate(R.id.SecondFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        intent.data?.let {
            phantomHandler.handleWalletConnection(
                resources.getString(R.string.connect_path),
                intent.action.orEmpty(),
                it,
                { wallet ->
                    if (wallet.isNotEmpty()) {
                        findNavController(R.id.nav_host_fragment_content_main)
                            .navigate(R.id.SecondFragment)
                    }
                },
                {
                    Toast.makeText(
                        applicationContext,
                        "Wallet not connected",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleNfcMessage(intent)
    }

    private fun handleNfcMessage(intent: Intent?){
        val rawMsgs: Array<Parcelable> =
            intent?.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)!!
        var msg: NdefMessage? = null
        if (rawMsgs != null && rawMsgs.size > 0) {
            msg = rawMsgs[0] as NdefMessage
        }
        if (msg != null) {
            when(msg.records[0].payload.toString()){
                InternalConstants.PUB_KEY -> {
                    PixNfcSessionHandler.sharedPubReceived(msg.records[1].payload)
                }
                InternalConstants.TRANSACTION -> {
                    PixNfcSessionHandler.payloadReceived(msg.records[1].payload)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_content_main)
            .navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}