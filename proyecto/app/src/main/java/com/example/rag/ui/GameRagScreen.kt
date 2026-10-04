package com.example.rag.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rag.data.GameDocument

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameRagScreen(
    viewModel: GameViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val suggestions = listOf(
        "Magic swords and dragons",
        "Open world exploration",
        "Nintendo Switch platformer",
        "Cyberpunk futuristic tech",
        "Turn-based tabletop RPG"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vector RAG Game Assistant 🧠🎮", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search Input Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = { viewModel.onQueryChanged(it) },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ask semantically (e.g., dragons, tech)...") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = { viewModel.submitQuery() },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Search")
                }
            }

            // Quick Suggestion Chips
            Text("Semantic Query Suggestions:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(suggestions) { suggestion ->
                    AssistChip(
                        onClick = { viewModel.submitQuery(suggestion) },
                        label = { Text(suggestion) }
                    )
                }
            }

            HorizontalDivider()

            // Main Content Area
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        CircularProgressIndicator()
                        Text("Computing embeddings & cosine similarity...", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } else if (state.currentResponse == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Enter a semantic query or tap a suggestion above to run Vector RAG!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Answer Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Synthesized RAG Answer", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                                Text(state.currentResponse!!.answer, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    // Retrieved Sources Header
                    item {
                        Text(
                            "Retrieved Semantic Chunks (${state.currentResponse!!.retrievedSources.size}):",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Sources List
                    items(state.currentResponse!!.retrievedSources) { source ->
                        val similarity = state.currentResponse!!.similarityScores[source.id] ?: 0f
                        SourceCard(source, similarity)
                    }
                }
            }
        }
    }
}

@Composable
fun SourceCard(source: GameDocument, similarityScore: Float) {
    val percentage = (similarityScore * 100).toInt()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(source.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Badge(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        "Match: $percentage%",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            Text("Genre: ${source.genre}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            Text("Platform: ${source.platform}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(2.dp))
            Text(source.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
