package com.example.mytetrisapplication.ui.RecordView

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mytetrisapplication.database.GameRecordEntity
import com.example.mytetrisapplication.ViewModel.RecordViewModel
import android.content.Context

@Composable
fun RecordList(
    records: List<GameRecordEntity>,
    viewModel: RecordViewModel,
    context: Context,
    onEditNote: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(records.indices.toList()) { idx ->
            val record = records[idx]
            RecordCard(
                record = record,
                recordIndex = idx,
                viewModel = viewModel,
                context = context,
                onEditNote = onEditNote
            )
        }
    }
} 