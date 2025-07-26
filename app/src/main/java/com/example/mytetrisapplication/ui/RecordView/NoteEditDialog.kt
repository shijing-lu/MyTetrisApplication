package com.example.mytetrisapplication.ui.RecordView

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.mytetrisapplication.ViewModel.RecordViewModel
import com.example.mytetrisapplication.ViewModel.RecordEvent
import com.example.mytetrisapplication.R
import android.content.Context

@Composable
fun NoteEditDialog(
    noteText: TextFieldValue,
    onNoteTextChange: (TextFieldValue) -> Unit,
    viewModel: RecordViewModel,
    context: Context
) {
    val clipboardManager = LocalClipboardManager.current
    
    AlertDialog(
        onDismissRequest = {
            viewModel.handleEvent(RecordEvent.CancelEditNote, context)
        },
        title = { Text(stringResource(id = R.string.edit_note)) },
        text = {
            Column {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = onNoteTextChange,
                    label = { Text(stringResource(id = R.string.note_content)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Button(onClick = {
                        clipboardManager.setText(AnnotatedString(noteText.text))
                    }) { Text(stringResource(id = R.string.copy_note)) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        val clip = clipboardManager.getText()?.text ?: ""
                        onNoteTextChange(TextFieldValue(noteText.text + clip))
                    }) { Text(stringResource(id = R.string.paste_note)) }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                viewModel.handleEvent(RecordEvent.SaveNote(noteText.text), context)
            }) { Text(stringResource(id = R.string.save)) }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.handleEvent(RecordEvent.CancelEditNote, context)
            }) { Text(stringResource(id = R.string.cancel)) }
        }
    )
} 