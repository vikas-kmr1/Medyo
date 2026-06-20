package medyo.com.utils.app

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.util.Base64
import timber.log.Timber
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Arrays

class AppSignatureHelper(context: Context?) : ContextWrapper(context) {
    val appSignatures: ArrayList<String>
        /**
         * Get all the app signatures for the current package
         * @return
         */
        get() {
            val appCodes = ArrayList<String>()
            try {
                // Get all package signatures for the current package
                val packageName = packageName
                val packageManager = packageManager
                val packageInfo: PackageInfo =
                    packageManager.getPackageInfo(
                        packageName,
                        PackageManager.GET_SIGNING_CERTIFICATES
                    )

                val signatures: Array<out Signature?>? = if (packageInfo.signingInfo != null) {
                    // New method (API level 28 and above)
                    if (packageInfo.signingInfo!!.hasMultipleSigners()) {
                        // Handle multiple signers if necessary
                        packageInfo.signingInfo!!.apkContentsSigners
                    } else {
                        // Single signer
                        packageInfo.signingInfo!!.signingCertificateHistory
                    }
                } else {
                    // Old method (deprecated)
                    packageInfo.signatures
                }

                // For each signature create a compatible hash
                if (signatures != null) {
                    for (signature in signatures) {
                        val hash = hash(packageName, signature!!.toCharsString())
                        if (hash != null) {
                            appCodes.add(String.format("%s", hash))
                        }
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Timber.Forest.tag(TAG).e(e, "Unable to find package to obtain hash.")
            }
            return appCodes
        }

    companion object {
        val TAG = AppSignatureHelper::class.java.simpleName
        private const val HASH_TYPE = "SHA-256"
        private const val NUM_HASHED_BYTES = 9
        private const val NUM_BASE64_CHAR = 11
        private fun hash(packageName: String, signature: String): String? {
            val appInfo = "$packageName $signature"
            try {
                val messageDigest = MessageDigest.getInstance(HASH_TYPE)
                messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
                var hashSignature = messageDigest.digest()

                // truncated into NUM_HASHED_BYTES
                hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES)
                // encode into Base64
                var base64Hash: String =
                    Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
                base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)
                Timber.Forest.tag(TAG).d(String.format("pkg: %s -- hash: %s", packageName, base64Hash))
                return base64Hash
            } catch (e: NoSuchAlgorithmException) {
                Timber.Forest.tag(TAG).e(e, "hash:NoSuchAlgorithm")
            }
            return null
        }
    }
}