package com.example.mytetrisapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mytetrisapplication.ViewModel.RecordViewModel
import com.example.mytetrisapplication.ViewModel.RecordEvent
import com.example.mytetrisapplication.ui.RecordView.RecordHeader
import com.example.mytetrisapplication.ui.RecordView.RecordList
import com.example.mytetrisapplication.ui.RecordView.NoteEditDialog
import com.example.mytetrisapplication.R

@Composable
fun RecordScreenRoot(
    onBack: () -> Unit,
    viewModel: RecordViewModel = viewModel()
) {
    RecordScreen(onBack = onBack, viewModel = viewModel)
}

@Composable
fun RecordScreen(
    onBack: () -> Unit,
    viewModel: RecordViewModel
) {
    val context = LocalContext.current
    val recordState by viewModel.recordState.collectAsStateWithLifecycle()
    var noteText by remember { mutableStateOf(TextFieldValue("")) }

    // 加载记录
    LaunchedEffect(Unit) {
        viewModel.handleEvent(RecordEvent.LoadRecords, context)
    }

    // 获取排序后的记录
    val sortedRecords = remember(recordState.records, recordState.sortByScore) {
        viewModel.getSortedRecords()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RecordHeader(viewModel = viewModel, context = context)
        Spacer(modifier = Modifier.height(16.dp))
        RecordList(
            records = sortedRecords,
            viewModel = viewModel,
            context = context,
            onEditNote = { note -> noteText = TextFieldValue(note) },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) { Text(stringResource(id = R.string.go_back)) }
    }

    // 备注编辑弹窗
    if (recordState.editingNote != null && recordState.editingIndex >= 0) {
        NoteEditDialog(
            noteText = noteText,
            onNoteTextChange = { noteText = it },
            viewModel = viewModel,
            context = context
        )
    }
} 