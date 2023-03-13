package io.pixfuser

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import io.phantomBridge.PhantomHandler
import io.pixfuser.databinding.ActivityMainBinding

class StartActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val phantomHandler = PhantomHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onResume() {
        super.onResume()

        intent.data?.let {
            phantomHandler.handleWalletConnection(
                "connectWallet",
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

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_content_main)
            .navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}