package com.example.swpp.tododapp.tasks

import android.view.View
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.data.Task
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@MediumTest
@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {

    private lateinit var textView: TextView

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            textView = TextView(context)
        }
    }

    private fun bind(task: Task?) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            setDueDateLabel(textView, task)
        }
    }

    private fun prepare(text: String, visibility: Int) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            textView.text = text
            textView.visibility = visibility
        }
    }

    // ---------- Success cases ----------

    @Test
    fun setDueDateLabel_activeDueToday_showsDDay() {
        // GIVEN
        prepare("", View.GONE)
        val task = Task("T", "D", false, "id1", LocalDate.now())
        // WHEN
        bind(task)
        // THEN
        assertEquals("D-Day", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_activeDueIn3Days_showsDMinus3() {
        prepare("", View.GONE)
        bind(Task("T", "D", false, "id1", LocalDate.now().plusDays(3)))
        assertEquals("D-3", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_completedWithDueDate_showsClear() {
        prepare("", View.GONE)
        bind(Task("T", "D", true, "id1", LocalDate.now().plusDays(2)))
        assertEquals("CLEAR", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_completedNoDueDate_showsClear() {
        prepare("", View.GONE)
        bind(Task("T", "D", true, "id1", null))
        assertEquals("CLEAR", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_activeNoDueDate_hidesView() {
        prepare("D-3", View.VISIBLE)
        bind(Task("T", "D", false, "id1", null))
        assertEquals(View.GONE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_nullTask_hidesView() {
        prepare("D-3", View.VISIBLE)
        bind(null)
        assertEquals(View.GONE, textView.visibility)
    }

    // ---------- Edge cases (state transitions) ----------

    @Test
    fun setDueDateLabel_uncheckCompletedNoDueDate_hidesClear() {
        // GIVEN - completed task with no due date shows CLEAR
        val task = Task("T", "D", true, "id1", null)
        bind(task)
        assertEquals("CLEAR", textView.text.toString())
        // WHEN - task is unchecked and rebound
        task.isCompleted = false
        bind(task)
        // THEN - stale CLEAR must not remain
        assertEquals(View.GONE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_uncheckCompletedDueIn3Days_showsDMinus3() {
        val task = Task("T", "D", true, "id1", LocalDate.now().plusDays(3))
        bind(task)
        task.isCompleted = false
        bind(task)
        assertEquals("D-3", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_checkActiveDueIn3Days_showsClear() {
        val task = Task("T", "D", false, "id1", LocalDate.now().plusDays(3))
        bind(task)
        assertEquals("D-3", textView.text.toString())
        task.isCompleted = true
        bind(task)
        assertEquals("CLEAR", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }
}
