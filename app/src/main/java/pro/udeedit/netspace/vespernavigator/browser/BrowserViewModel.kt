package pro.udeedit.netspace.vespernavigator.browser

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


/**
 * Abstraction for something that exposes [BrowserUiState].
 *
 * Used so the UI does not depend directly on [BrowserViewModel],
 * which makes previews and tests simpler.
 */
interface BrowserStateOwner {
    /** Read-only stream of browser UI state. */
    val uiState: StateFlow<BrowserUiState>
}

/**
 * ViewModel responsible for managing browser UI state.
 *
 * Implements [BrowserStateOwner] so it can be passed into UI in a
 * DI-friendly, testable way.
 */
@HiltViewModel
class BrowserViewModel @Inject constructor() : ViewModel() {

    // Backing state that can be updated inside the ViewModel only.
    private val _uiState = MutableStateFlow(BrowserUiState())

    /**
     * Public, read-only browser UI state for the UI layer.
     */
    val uiState: StateFlow<BrowserUiState> = _uiState.asStateFlow()

    /**
     * Should be called when a new page load starts in the WebView.
     *
     * @param url The URL that is starting to load.
     */
    fun onPageStarted(url: String) {
        _uiState.value = _uiState.value.copy(
            currentUrl = url,
            isLoading = true,
            errorMessage = null // clear previous errors for the new load
        )
    }

    /**
     * Should be called when a page finishes loading successfully.
     *
     * @param url The URL that finished loading.
     * @param canGoBack Whether the WebView can navigate back.
     * @param canGoForward Whether the WebView can navigate forward.
     */
    fun onPageFinished(
        url: String,
        canGoBack: Boolean,
        canGoForward: Boolean
    ) {
        _uiState.value = _uiState.value.copy(
            currentUrl = url,
            isLoading = false,
            canGoBack = canGoBack,
            canGoForward = canGoForward
        )
    }

    /**
     * Should be called when the WebView encounters an error.
     *
     * @param message Human-readable description of the error.
     */
    fun onPageError(message: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            errorMessage = message
        )
    }

    // Below methods represent navigation intents. For now they are placeholders
    // and will be wired to WebView navigation via callbacks later.

    /**
     * User intent: navigate back in the WebView history.
     */
    fun onBack() {
        // TODO: delegate to a WebView controller instance
    }

    /**
     * User intent: navigate forward in the WebView history.
     */
    fun onForward() {
        // TODO: delegate to a WebView controller instance
    }

    /**
     * User intent: reload the current page.
     */
    fun onReload() {
        // TODO: delegate to a WebView controller instance
    }
}
