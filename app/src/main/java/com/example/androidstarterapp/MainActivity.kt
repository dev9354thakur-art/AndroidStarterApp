package com.example.androidstarterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class TodoItem(
    val id: Int,
    val text: String,
    var checked: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(),
                typography = Typography(),
                content = { TodoApp() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoApp() {
    var taskText by remember { mutableStateOf("") }

    val tasks = remember {
        mutableStateListOf(
            TodoItem(1, "Plan your day", true),
            TodoItem(2, "Build a simple Android app"),
            TodoItem(3, "Ship a polished UI")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Todo App") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val trimmedText = taskText.trim()
                    if (trimmedText.isNotEmpty()) {
                        val nextId = (tasks.maxOfOrNull { it.id } ?: 0) + 1
                        tasks.add(TodoItem(nextId, trimmedText))
                        taskText = ""
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            OutlinedTextField(
                value = taskText,
                onValueChange = { taskText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Add a task") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tasks yet",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(tasks, key = { it.id }) { task ->
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = task.checked,
                                    onCheckedChange = { task.checked = it },
                                    modifier = Modifier.padding(end = 8.dp)
                                )

                                Text(
                                    text = task.text,
                                    modifier = Modifier.weight(1f),
                                    fontSize = 16.sp,
                                    fontWeight = if (task.checked) FontWeight.Normal else FontWeight.Medium,
                                    color = if (task.checked) {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
                                )

                                IconButton(
                                    onClick = { tasks.removeIf { it.id == task.id } }
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete task"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoAppPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme(),
        typography = Typography(),
        content = { TodoApp() }
    )
}
