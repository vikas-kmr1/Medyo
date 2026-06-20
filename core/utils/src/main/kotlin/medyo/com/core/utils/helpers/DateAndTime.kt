package medyo.com.core.utils.helpers

import java.time.LocalDate
import java.time.format.DateTimeFormatter


private fun parseToLocalDate(dateStr: String?): LocalDate? {
    if (dateStr.isNullOrBlank()) return null
    return try {
        LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
    } catch (e: Exception) {
        null
    }
}