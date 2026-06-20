package medyo.com.logger.impl


import medyo.com.logger.api.LoggerApi
import timber.log.Timber

internal class LoggerApiImpl(
    private val shouldEnableLogs: Boolean = BuildConfig.DEBUG
) : LoggerApi {

    override fun logD(message: String) {
        if (shouldEnableLogs)
            Timber.d(message)
    }

    override fun logDWithTag(tag: String, message: String) {
        if (shouldEnableLogs)
            Timber.tag(tag).d(message)
    }

    override fun logE(message: String, e: Exception) {
        if (shouldEnableLogs)
            Timber.e(e, message)
    }

    override fun logEWithTag(tag: String, message: String, e: Exception) {
        if (shouldEnableLogs)
            Timber.tag(tag).e(e, message)
    }
}