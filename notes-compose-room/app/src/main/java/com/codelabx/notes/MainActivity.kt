package com.codelabx.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codelabx.notes.data.Note
import com.codelabx.notes.ui.EditNoteScreen
import com.codelabx.notes.ui.NotesScreen
import com.codelabx.notes.ui.theme.NotesTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { NotesTheme { NotesApp() } }
    }
}

/** Two screens: the list ("notes") and the editor ("edit/{id}", where id 0 means a new note). */
@Composable
fun NotesApp(viewModel: NotesViewModel = viewModel()) {
    val nav = rememberNavController()
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    NavHost(navController = nav, startDestination = "notes") {
        composable("notes") {
            val notes by viewModel.notes.collectAsStateWithLifecycle()
            val query by viewModel.query.collectAsStateWithLifecycle()
            NotesScreen(
                notes = notes,
                query = query,
                snackbar = snackbar,
                onQueryChange = viewModel::onQueryChange,
                onOpen = { nav.navigate("edit/${it.id}") },
                onNew = { nav.navigate("edit/0") },
                onTogglePin = { viewModel.togglePin(it) },
            )
        }
        composable("edit/{id}", arguments = listOf(navArgument("id") { type = NavType.LongType })) { entry ->
            val id = entry.arguments?.getLong("id") ?: 0L
            // Load the note once; a new note starts empty.
            var note by remember { mutableStateOf<Note?>(if (id == 0L) Note(title = "", content = "") else null) }
            LaunchedEffect(id) { if (id != 0L) note = viewModel.load(id) }

            note?.let { current ->
                EditNoteScreen(
                    note = current,
                    onSave = {
                        viewModel.save(it)
                        nav.popBackStack()
                    },
                    onDelete = {
                        viewModel.delete(it)
                        nav.popBackStack()
                        scope.launch {
                            val result = snackbar.showSnackbar("Note deleted", actionLabel = "Undo", withDismissAction = true)
                            if (result == SnackbarResult.ActionPerformed) viewModel.restore(it)
                        }
                    },
                )
            }
        }
    }
}
