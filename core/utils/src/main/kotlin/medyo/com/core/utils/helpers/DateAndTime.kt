package medyo.com.core.utils.helpers

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.formatDate(pattern: String = "dd MMM yyyy"): String {
    val instant = Instant.ofEpochMilli(this)
    val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return localDate.format(formatter)
}

fun parseToLocalDate(dateStr: String?): LocalDate? {
    if (dateStr.isNullOrBlank()) return null
    return try {
        LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
    } catch (e: Exception) {
        null
    }
}