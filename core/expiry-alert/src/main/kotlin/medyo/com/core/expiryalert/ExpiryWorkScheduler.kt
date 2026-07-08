package medyo.com.core.expiryalert

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpiryWorkScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun schedulePeriodicChecks() {
        val constraints = Constraints.Builder()
            // We only need basic constraints; doesn't need network
            .build()

        val workRequest = PeriodicWorkRequestBuilder<ExpiryCheckWorker>(12, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, // Keep existing if already scheduled
            workRequest
        )
    }

    companion object {
        private const val WORK_NAME = "MedyoExpiryCheckWorker"
    }
}
