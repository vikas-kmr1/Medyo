package medyo.com.core.dosage_alert.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.max

object NextAlarmCalculator {
    fun computeNextAlarmMillis(
        timeOfDay: String,
        startDate: Long,
        endDate: Long?,
        frequency: String
    ): Long? {
        val time = LocalTime.parse(timeOfDay)
        val zone = ZoneId.systemDefault()
        
        val startDateLocal = Instant.ofEpochSecond(startDate).atZone(zone).toLocalDate()
        val endDateLocal = endDate?.let { Instant.ofEpochSecond(it).atZone(zone).toLocalDate() }
        
        val today = LocalDate.now(zone)
        
        var candidateDate = if (startDateLocal.isAfter(today)) startDateLocal else today
        
        var candidateZoned = ZonedDateTime.of(candidateDate, time, zone)
        
        if (candidateZoned.toInstant().isBefore(Instant.now())) {
            // For DAILY frequency, next alarm is the next day
            candidateDate = candidateDate.plusDays(1)
            candidateZoned = ZonedDateTime.of(candidateDate, time, zone)
        }
        
        if (endDateLocal != null && candidateDate.isAfter(endDateLocal)) {
            return null
        }
        
        return candidateZoned.toInstant().toEpochMilli()
    }
}
