package com.example.mytetrisapplication.ui.RecordView

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mytetrisapplication.ViewModel.RecordViewModel
import com.example.mytetrisapplication.ViewModel.RecordEvent
import com.example.mytetrisapplication.R
import android.content.Context

@Composable
fun RecordHeader(
    viewModel: RecordViewModel,
    context: Context
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(id = R.string.records_title) + "  ")
        Button(onClick = { viewModel.handleEvent(RecordEvent.SortByScore, context) }) {
            Text(stringResource(id = R.string.sort_by_score))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { viewModel.handleEvent(RecordEvent.SortByDate, context) }) {
            Text(stringResource(id = R.string.sort_by_date))
        }
    }
} 