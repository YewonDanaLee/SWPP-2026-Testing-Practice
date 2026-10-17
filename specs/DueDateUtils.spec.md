# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Returns a human-readable D-day label for a task. Completed tasks always show `"CLEAR"`, with or without a due date. An active task with no due date shows nothing (`""`). Otherwise the label encodes the total number of calendar days until (`"D-N"`) or past (`"D+N"`) the due date, or `"D-Day"` when due today.

**Pre-conditions**
- `today` is a valid, non-null `LocalDate`.

**Post-conditions**
- Success cases
  - When `isCompleted` is `true` and `dueDate` is after `today`: returns `"CLEAR"`. `[Unit]`
    - Tip: Given today = 2026-10-17, dueDate = 2026-10-18, isCompleted = true / When dueDateLabel called / Then returns "CLEAR"
  - When `isCompleted` is `true` and `dueDate` is before `today` (overdue): returns `"CLEAR"`. `[Unit]`
    - Tip: Given today = 2026-10-17, dueDate = 2026-10-10, isCompleted = true / When dueDateLabel called / Then returns "CLEAR"
  - When `isCompleted` is `true` and `dueDate` is `null`: returns `"CLEAR"`. `[Unit]`
    - Tip: Given today = 2026-10-17, dueDate = null, isCompleted = true / When dueDateLabel called / Then returns "CLEAR"
  - When `isCompleted` is `false` and `dueDate` is `null`: returns `""`. `[Unit]`
    - Tip: Given today = 2026-10-17, dueDate = null, isCompleted = false / When dueDateLabel called / Then returns ""
  - When `isCompleted` is `false` and `dueDate` equals `today`: returns `"D-Day"`. `[Unit]`
    - Tip: Given today = 2026-10-17, dueDate = 2026-10-17, isCompleted = false / When dueDateLabel called / Then returns "D-Day"
  - When `isCompleted` is `false` and `dueDate` is N total calendar days after `today` (N > 0): returns `"D-N"`. `[Unit]`
    - Tip: Given today = 2026-10-17, dueDate = 2026-10-22, isCompleted = false / When dueDateLabel called / Then returns "D-5"
  - When `isCompleted` is `false` and `dueDate` is N total calendar days before `today` (N > 0): returns `"D+N"`. `[Unit]`
    - Tip: Given today = 2026-10-17, dueDate = 2026-10-14, isCompleted = false / When dueDateLabel called / Then returns "D+3"
- Error cases
  - None.
- Edge cases
  - When `dueDate` is exactly 1 day after `today`: returns `"D-1"`. `[Unit]`
    - Tip: Given today = 2026-10-17, dueDate = 2026-10-18, isCompleted = false / When dueDateLabel called / Then returns "D-1"
  - When `dueDate` is exactly 1 day before `today`: returns `"D+1"`. `[Unit]`
    - Tip: Given today = 2026-10-17, dueDate = 2026-10-16, isCompleted = false / When dueDateLabel called / Then returns "D+1"
  - When `dueDate` is exactly one month later with the same day-of-month: returns the total day count `"D-31"`, not `"D-Day"`. `[Unit]`
    - Tip: Given today = 2026-10-17, dueDate = 2026-11-17, isCompleted = false / When dueDateLabel called / Then returns "D-31"
  - When the interval spans more than one calendar month (future): returns the total day count, not the days component of a `Period`. `[Unit]`
    - Tip: Given today = 2026-01-28, dueDate = 2026-03-05, isCompleted = false / When dueDateLabel called / Then returns "D-36"
  - When the interval spans more than one calendar month (overdue): returns the total day count. `[Unit]`
    - Tip: Given today = 2026-03-05, dueDate = 2026-01-28, isCompleted = false / When dueDateLabel called / Then returns "D+36"
  - When the interval crosses a year boundary: returns the total day count. `[Unit]`
    - Tip: Given today = 2026-12-30, dueDate = 2027-01-02, isCompleted = false / When dueDateLabel called / Then returns "D-3"