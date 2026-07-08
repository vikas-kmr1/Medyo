package medyo.com.core.dosage_alert.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import medyo.com.core.dosage_alert.receiver.DosageAlarmReceiver
import javax.inject.Inject
import javax.inject.Singleton

interface DosageAlarmScheduler {
    fun scheduleDosageAlarm(
        scheduleId: Long,
        medicationId: Long,
        scheduledTimestamp: Long
    )

    fun cancelDosageAlarm(scheduleId: Long)
}


@Singleton
class DosageAlarmSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
): DosageAlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleDosageAlarm(
        scheduleId: Long,
        medicationId: Long,
        scheduledTimestamp: Long
    ) {
        if (scheduledTimestamp <= System.currentTimeMillis()) return

        val intent = Intent(context, DosageAlarmReceiver::class.java).apply {
            putExtra(EXTRA_SCHEDULE_ID, scheduleId)
            putExtra(EXTRA_MEDICATION_ID, medicationId)
            putExtra(EXTRA_SCHEDULED_TIMESTAMP, scheduledTimestamp)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            scheduleId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Using setAlarmClock to show full-screen intent even over other apps / doze mode
        val alarmClockInfo = AlarmManager.AlarmClockInfo(
            scheduledTimestamp,
            pendingIntent // Optionally provide an intent for showing an alarm details page
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
            } else {
                // Fallback or notify user to grant permission
                // In production, we should request ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            }
        } else {
            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
        }
    }

    override fun cancelDosageAlarm(scheduleId: Long) {
        val intent = Intent(context, DosageAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            scheduleId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    companion object {
        const val EXTRA_SCHEDULE_ID = "EXTRA_SCHEDULE_ID"
        const val EXTRA_MEDICATION_ID = "EXTRA_MEDICATION_ID"
        const val EXTRA_SCHEDULED_TIMESTAMP = "EXTRA_SCHEDULED_TIMESTAMP"
    }
}
