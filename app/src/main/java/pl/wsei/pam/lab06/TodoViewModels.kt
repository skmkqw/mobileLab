package pl.wsei.pam.lab06

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.*
import pl.wsei.pam.lab06.data.*
import java.time.LocalDate

// List ViewModel
class ListViewModel(val repository: TodoTaskRepository) : ViewModel() {
    val listUiState: StateFlow<ListUiState> = repository.getAllAsStream()
        .map { ListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ListUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ListUiState(val items: List<TodoTask> = listOf())

// Form ViewModel
class FormViewModel(
    private val repository: TodoTaskRepository,
    private val dateProvider: CurrentDateProvider
) : ViewModel() {

    var todoTaskUiState by mutableStateOf(TodoTaskUiState())
        private set

    init {
        // Initialize with default date from provider
        todoTaskUiState = TodoTaskUiState(
            todoTask = TodoTaskForm(deadline = LocalDateConverter.toMillis(dateProvider.currentDate))
        )
    }

    suspend fun save() {
        if (validate()) {
            repository.insertItem(todoTaskUiState.todoTask.toTodoTask())
        }
    }

    fun updateUiState(todoTaskForm: TodoTaskForm) {
        todoTaskUiState = TodoTaskUiState(todoTask = todoTaskForm, isValid = validate(todoTaskForm))
    }

    private fun validate(uiState: TodoTaskForm = todoTaskUiState.todoTask): Boolean {
        val deadlineDate = LocalDateConverter.fromMillis(uiState.deadline)
        return uiState.title.isNotBlank() && (deadlineDate.isAfter(dateProvider.currentDate) || deadlineDate.isEqual(dateProvider.currentDate))
    }
}

data class TodoTaskUiState(
    var todoTask: TodoTaskForm = TodoTaskForm(),
    val isValid: Boolean = false
)

data class TodoTaskForm(
    val id: Int = 0,
    val title: String = "",
    val deadline: Long = LocalDateConverter.toMillis(LocalDate.now()),
    val isDone: Boolean = false,
    val priority: String = Priority.Low.name
)

fun TodoTask.toTodoTaskUiState(isValid: Boolean = false): TodoTaskUiState = TodoTaskUiState(
    todoTask = this.toTodoTaskForm(),
    isValid = isValid
)

fun TodoTaskForm.toTodoTask(): TodoTask = TodoTask(
    id = id,
    title = title,
    deadline = LocalDateConverter.fromMillis(deadline),
    isDone = isDone,
    priority = Priority.valueOf(priority)
)

fun TodoTask.toTodoTaskForm(): TodoTaskForm = TodoTaskForm(
    id = id,
    title = title,
    deadline = LocalDateConverter.toMillis(deadline),
    isDone = isDone,
    priority = priority.name
)

// ViewModel Provider
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ListViewModel(
                repository = todoApplication().container.todoTaskRepository
            )
        }
        initializer {
            FormViewModel(
                repository = todoApplication().container.todoTaskRepository,
                dateProvider = todoApplication().container.currentDateProvider
            )
        }
    }
}

fun CreationExtras.todoApplication(): TodoApplication {
    val app = this[APPLICATION_KEY]
    return app as TodoApplication
}
