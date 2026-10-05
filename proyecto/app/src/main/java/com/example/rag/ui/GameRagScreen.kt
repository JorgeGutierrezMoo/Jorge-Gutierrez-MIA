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
        "Action RPG mundo abierto",
        "Supervivencia y construcción submarina",
        "Shooter de zombis cooperativo",
        "Exploración espacial de ciencia ficción",
        "JRPG por turnos con historia"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Steam game recomendations RAG", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                    placeholder = { Text("¿Qué estás buscando?") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = { viewModel.submitQuery() },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Buscar")
                }
            }

            // Top-K Slider Adjustment Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recomendaciones (Top-K): ${state.topK}", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                Slider(
                    value = state.topK.toFloat(),
                    onValueChange = { viewModel.onTopKChanged(it.toInt()) },
                    valueRange = 1f..10f,
                    steps = 8,
                    modifier = Modifier.width(200.dp)
                )
            }

            // Quick Suggestion Chips
            Text("Sugerencias de Búsqueda Semántica:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
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
                        Text("Recuperando chunks en 5 documentos JSON...", style = MaterialTheme.typography.bodyMedium)
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
                        "¡Ingresa una consulta semántica o ajusta Top-K para ejecutar el RAG!",
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
                    // Answer Summary Card
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
                                Text("Resumen RAG Sintetizado", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                                Text(state.currentResponse!!.answer, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    // Retrieved Chunks Header
                    item {
                        if (state.currentResponse!!.retrievedSources.isNotEmpty()) {
                            Text(
                                "Chunks de Documentos Recuperados (${state.currentResponse!!.retrievedSources.size}):",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Sources List with Chunk Inspection
                    items(state.currentResponse!!.retrievedSources) { source ->
                        val similarity = state.currentResponse!!.similarityScores[source.id] ?: 0f
                        ChunkInspectionCard(source, similarity)
                    }
                }
            }
        }
    }
}

@Composable
fun ChunkInspectionCard(source: GameDocument, similarityScore: Float) {
    val percentage = (similarityScore * 100).toInt()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(source.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.fillMaxWidth())
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Género: ${source.genre} | Plataforma: ${source.platform}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                Badge(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        "Coincidencia: $percentage%",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(2.dp))
            
            // RAG Chunk Inspection Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("📄 Chunk RAG Recuperado:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text("Resumen: ${source.description}", style = MaterialTheme.typography.bodySmall)
                    if (source.lore.isNotBlank() && source.lore != source.description) {
                        Text("Historia / Lore: ${source.lore}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
