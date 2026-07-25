package pro.udeedit.netspace.vespernavigator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import pro.udeedit.netspace.vespernavigator.browser.BrowserScreen
import pro.udeedit.netspace.vespernavigator.browser.BrowserViewModel
import pro.udeedit.netspace.vespernavigator.ui.theme.VesperNavigatorTheme

/**
 * Main entry point for the Vesper Navigator application.
 *
 * Hosts the top-level browser screen and integrates Hilt into the activity.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val browserViewModel: BrowserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Opt in to edge-to-edge layout behavior on modern Android versions.
        enableEdgeToEdge()

        setContent {
            VesperNavigatorTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BrowserScreen(viewModel = browserViewModel)
                }
            }
        }
    }
}
