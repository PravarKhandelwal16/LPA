package com.example.lpa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.lpa.ui.theme.PrismEsimLpaTheme
import com.example.lpa.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint

/**
 * Entry point of the PrismEsimLpa application.
 *
 * Hosts the Compose content and applies the Material 3 theme.
 * All navigation is delegated to [AppNavGraph].
 */
@AndroidEntryPoint

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrismEsimLpaTheme {
                AppNavGraph()
            }
        }
    }
}