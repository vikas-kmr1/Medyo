package medyo.com.core.notification.api

import android.app.Activity
import medyo.com.core.domain.model.MedicationInfo

interface Notifier{
    fun notifyExpiry(medications: List<MedicationInfo>)
    fun notifyDosage(medications: List<MedicationInfo>)
    fun requestPermissionIfNeeded(activity: Activity)
}