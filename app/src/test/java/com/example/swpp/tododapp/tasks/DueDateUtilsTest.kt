package com.example.swpp.tododapp.tasks

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DueDateUtilsTest {

    private val today = LocalDate.of(2026, 10, 17)

    // ---------- Success cases ----------

    @Test
    fun dueDateLabel_completedFutureDue_returnsClear() {
        // GIVEN - completed task due tomorrow
        val dueDate = LocalDate.of(2026, 10, 18)
        // WHEN
        val result = dueDateLabel(dueDate, today, true)
        // THEN
        assertEquals("CLEAR", result)
    }

    @Test
    fun dueDateLabel_completedOverdue_returnsClear() {
        val result = dueDateLabel(LocalDate.of(2026, 10, 10), today, true)
        assertEquals("CLEAR", result)
    }

    @Test
    fun dueDateLabel_completedNoDueDate_returnsClear() {
        val result = dueDateLabel(null, today, true)
        assertEquals("CLEAR", result)
    }

    @Test
    fun dueDateLabel_activeNoDueDate_returnsEmpty() {
        val result = dueDateLabel(null, today, false)
        assertEquals("", result)
    }

    @Test
    fun dueDateLabel_activeDueToday_returnsDDay() {
        val result = dueDateLabel(LocalDate.of(2026, 10, 17), today, false)
        assertEquals("D-Day", result)
    }

    @Test
    fun dueDateLabel_activeDueIn5Days_returnsDMinus5() {
        val result = dueDateLabel(LocalDate.of(2026, 10, 22), today, false)
        assertEquals("D-5", result)
    }

    @Test
    fun dueDateLabel_activeOverdue3Days_returnsDPlus3() {
        val result = dueDateLabel(LocalDate.of(2026, 10, 14), today, false)
        assertEquals("D+3", result)
    }

    // ---------- Edge cases ----------

    @Test
    fun dueDateLabel_activeDueTomorrow_returnsDMinus1() {
        val result = dueDateLabel(LocalDate.of(2026, 10, 18), today, false)
        assertEquals("D-1", result)
    }

    @Test
    fun dueDateLabel_activeDueYesterday_returnsDPlus1() {
        val result = dueDateLabel(LocalDate.of(2026, 10, 16), today, false)
        assertEquals("D+1", result)
    }

    @Test
    fun dueDateLabel_activeDueOneMonthLaterSameDay_returnsDMinus31() {
        // GIVEN - same day-of-month, one month later (31 days)
        val result = dueDateLabel(LocalDate.of(2026, 11, 17), today, false)
        // THEN - total day count, not "D-Day"
        assertEquals("D-31", result)
    }

    @Test
    fun dueDateLabel_activeDueAcrossMonths_returnsDMinus36() {
        val result = dueDateLabel(
            LocalDate.of(2026, 3, 5), LocalDate.of(2026, 1, 28), false
        )
        assertEquals("D-36", result)
    }

    @Test
    fun dueDateLabel_activeOverdueAcrossMonths_returnsDPlus36() {
        val result = dueDateLabel(
            LocalDate.of(2026, 1, 28), LocalDate.of(2026, 3, 5), false
        )
        assertEquals("D+36", result)
    }

    @Test
    fun dueDateLabel_activeDueAcrossYear_returnsDMinus3() {
        val result = dueDateLabel(
            LocalDate.of(2027, 1, 2), LocalDate.of(2026, 12, 30), false
        )
        assertEquals("D-3", result)
    }
}
