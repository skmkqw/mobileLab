package pl.wsei.pam.lab06.data

import java.time.LocalDate

enum class Priority {
    High, Medium, Low
}

data class TodoTask(
    val id: Int = 0,
    val title: String,
    val deadline: LocalDate,
    val isDone: Boolean,
    val priority: Priority
)
