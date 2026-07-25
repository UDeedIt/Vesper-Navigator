package pro.udeedit.netspace.vespernavigator.browser

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.udeedit.netspace.vespernavigator.ui.theme.VesperNavigatorTheme
import pro.udeedit.netspace.vespernavigator.web.BrowserWebView

/**
 * Top-level browser screen that hosts the WebView and basic browser chrome.
 *
 * Responsibilities:
 * - observe [BrowserUiState] from [BrowserViewModel]
 * - render the current URL in the app bar
 * - show the WebView and loading indicator
 * - surface errors via snackbars
 *
 * @param viewModel Provider of [BrowserUiState]. In production this will be
 * a [BrowserViewModel] instance created by Hilt.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    viewModel: BrowserViewModel
) {
    // Collect the current browser UI state as Compose state.
    val uiState = viewModel.uiState.collectAsState()

    // Snackbar host state for showing transient error messages.
    val snackbarHostState = remember { SnackbarHostState() }

    // Show error messages as snackbars whenever errorMessage changes.
    LaunchedEffect(uiState.value.errorMessage) {
        uiState.value.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Vesper Navigator")
                        Text(
                            text = uiState.value.currentUrl,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.onReload() },
                        enabled = !uiState.value.isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Reload"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            BrowserWebView(
                url = uiState.value.startUrl,
                viewModel = viewModel
            )

            if (uiState.value.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

/**
 * Preview of the browser chrome in a typical "page loading" state.
 *
 * Note: This preview renders only a static layout (no real WebView).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun BrowserScreenPreview() {
    val previewState = BrowserUiState(
        startUrl = "https://netspace.udeedit.pro/VesperNavigator/index.html",
        currentUrl = "https://netspace.udeedit.pro/VesperNavigator/index.html",
        isLoading = true
    )

    VesperNavigatorTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = previewState.currentUrl) }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
