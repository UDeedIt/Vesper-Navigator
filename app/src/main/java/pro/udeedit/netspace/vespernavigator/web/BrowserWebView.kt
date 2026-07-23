package pro.udeedit.netspace.vespernavigator.web

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.viewinterop.AndroidView
import pro.udeedit.netspace.vespernavigator.browser.BrowserViewModel

/**
 * Minimal WebView wrapper used by the browser screen.
 *
 * It is responsible only for:
 * - configuring basic WebView settings
 * - forwarding navigation events back to [BrowserViewModel]
 * - loading the requested URL
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BrowserWebView(
    url: String,
    viewModel: BrowserViewModel
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                // Basic settings suitable for a general-purpose browser.
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.cacheMode = WebSettings.LOAD_DEFAULT

                // Attach a WebViewClient to observe navigation and errors.
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        if (url != null) {
                            viewModel.onPageStarted(url)
                        }
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        if (view != null && url != null) {
                            viewModel.onPageFinished(
                                url = url,
                                canGoBack = view.canGoBack(),
                                canGoForward = view.canGoForward()
                            )
                        }
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: android.webkit.WebResourceRequest?,
                        error: android.webkit.WebResourceError?
                    ) {
                        super.onReceivedError(view, request, error)
                        val description = error?.description?.toString() ?: "Unknown error"
                        viewModel.onPageError(description)
                    }

                    @Deprecated(
                        "Deprecated WebView error callback kept only for compatibility with older API levels.",
                        ReplaceWith("onReceivedError(view, request, error)")
                    )
                    @Suppress("DEPRECATION")
                    override fun onReceivedError(
                        view: WebView?,
                        errorCode: Int,
                        description: String?,
                        failingUrl: String?
                    ) {
                        super.onReceivedError(view, errorCode, description, failingUrl)
                        viewModel.onPageError(description ?: "Unknown error")
                    }
                }

                // Initial load.
                loadUrl(url)
            }
        },
        update = { webView ->
            // Only trigger a new load if the URL actually changed.
            if (webView.url != url) {
                webView.loadUrl(url)
            }
        }
    )

    // Hook for cleaning up if we later add explicit WebView lifecycle management.
    DisposableEffect(Unit) {
        onDispose {
            // Currently no explicit cleanup needed; AndroidView will handle detaching.
            // We keep this hook in place for future extensions (e.g., webView.destroy()).
        }
    }
}
