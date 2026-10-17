# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: A data-binding adapter that drives a `TextView`'s D-day badge. When the task produces a non-empty label, it shows the label and makes the view visible. When no label is produced (task is null, or active with no due date), it hides the view (`View.GONE`) so that no stale label remains.

**Pre-conditions**
- `textView` is a non-null, initialized `TextView`.

**Post-conditions**
- Success cases
  - When `task` is non-null, active, and has a due date of today: sets `textView.text` to `"D-Day"` and sets `textView.visibility` to `View.VISIBLE`. `[Integration]`
    - Tip: Given a TextView (initial visibility GONE), an active Task with dueDate = LocalDate.now() / When setDueDateLabel called / Then textView.text == "D-Day" and textView.visibility == View.VISIBLE
  - When `task` is non-null, active, and has a future due date: sets `textView.text` to `"D-N"` and sets `textView.visibility` to `View.VISIBLE`. `[Integration]`
    - Tip: Given a TextView (initial visibility GONE), an active Task with dueDate = LocalDate.now().plusDays(3) / When setDueDateLabel called / Then textView.text == "D-3" and textView.visibility == View.VISIBLE
  - When `task` is non-null, completed, and has a due date: sets `textView.text` to `"CLEAR"` and sets `textView.visibility` to `View.VISIBLE`. `[Integration]`
    - Tip: Given a TextView (initial visibility GONE), a completed Task with dueDate = LocalDate.now().plusDays(2) / When setDueDateLabel called / Then textView.text == "CLEAR" and textView.visibility == View.VISIBLE
  - When `task` is non-null, completed, and `dueDate` is `null`: sets `textView.text` to `"CLEAR"` and sets `textView.visibility` to `View.VISIBLE`. `[Integration]`
    - Tip: Given a TextView (initial visibility GONE), a completed Task with dueDate = null / When setDueDateLabel called / Then textView.text == "CLEAR" and textView.visibility == View.VISIBLE
  - When `task` is non-null, active, and `dueDate` is `null`: sets `textView.visibility` to `View.GONE`. `[Integration]`
    - Tip: Given a TextView with visibility VISIBLE and text "D-3", an active Task with dueDate = null / When setDueDateLabel called / Then textView.visibility == View.GONE
  - When `task` is `null`: sets `textView.visibility` to `View.GONE`. `[Integration]`
    - Tip: Given a TextView with visibility VISIBLE and text "D-3", task = null / When setDueDateLabel called / Then textView.visibility == View.GONE
- Error cases
  - None.
- Edge cases
  - When a TextView showing "CLEAR" is rebound after the task (dueDate = null) is unchecked: sets visibility to `View.GONE`. `[Integration]`
    - Tip: Given a completed Task with dueDate = null and setDueDateLabel called once (text "CLEAR", VISIBLE) / When the task is changed to active and setDueDateLabel called again / Then textView.visibility == View.GONE
  - When a TextView showing "CLEAR" is rebound after the task (dueDate = today + 3) is unchecked: shows "D-3" and stays VISIBLE. `[Integration]`
    - Tip: Given a completed Task with dueDate = LocalDate.now().plusDays(3) and setDueDateLabel called once / When the task is changed to active and setDueDateLabel called again / Then textView.text == "D-3" and textView.visibility == View.VISIBLE
  - When an active Task with dueDate = today + 3 is checked as completed: label changes from "D-3" to "CLEAR". `[Integration]`
    - Tip: Given an active Task with dueDate = LocalDate.now().plusDays(3) and setDueDateLabel called once (text "D-3") / When the task is changed to completed and setDueDateLabel called again / Then textView.text == "CLEAR" and textView.visibility == View.VISIBLE

**Undefined behavior**
- The label is computed against `LocalDate.now()` at call time, so tests set `dueDate` relative to `LocalDate.now()` at test execution time. Exact-date boundary cases are covered in `DueDateUtils.spec.md` by calling `dueDateLabel()` with an explicit `today`.