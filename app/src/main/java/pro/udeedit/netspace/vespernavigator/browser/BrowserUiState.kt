package pro.udeedit.netspace.vespernavigator.browser

/**
 * Immutable UI state for the browser screen.
 *
 * This state is owned by [BrowserViewModel] and observed by the Compose UI.
 */
data class BrowserUiState(
    /**
     * Initial URL to load when the WebView is first created.
     */
    val startUrl: String = "https://example.com",

    /**
     * URL currently shown (or being loaded) in the WebView.
     */
    val currentUrl: String = "https://example.com",

    /**
     * Indicates whether a page load is currently in progress.
     */
    val isLoading: Boolean = false,

    /**
     * Whether the current WebView back stack allows going back.
     */
    val canGoBack: Boolean = false,

    /**
     * Whether the current WebView forward stack allows going forward.
     */
    val canGoForward: Boolean = false,

    /**
     * Optional error message to show to the user (e.g., via snackbar).
     */
    val errorMessage: String? = null
)
