package com.example.mytetrisapplication.ui.RecordView

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mytetrisapplication.Entity.GameRecord
import com.example.mytetrisapplication.ViewModel.RecordViewModel
import com.example.mytetrisapplication.ViewModel.RecordEvent
import com.example.mytetrisapplication.R
import android.content.Context

@Composable
fun RecordCard(
    record: GameRecord,
    recordIndex: Int,
    viewModel: RecordViewModel,
    context: Context,
    onEditNote: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(id = R.string.score_label), fontWeight = FontWeight.Bold)
                Text("${record.score}", color = Color(0xFF1976D2), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(12.dp))
                Text(stringResource(id = R.string.duration_label), fontWeight = FontWeight.Bold)
                Text("${record.time}${stringResource(id = R.string.seconds_unit)}", color = Color(0xFF388E3C), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(stringResource(id = R.string.date_label) + record.date, color = Color.Gray, fontWeight = FontWeight.Light)
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(id = R.string.note_label), fontWeight = FontWeight.Bold)
                Text(record.note, modifier = Modifier.weight(1f))
                Button(onClick = {
                    viewModel.handleEvent(
                        RecordEvent.StartEditNote(record.note, recordIndex),
                        context
                    )
                    onEditNote(record.note)
                }) {
                    Text(stringResource(id = R.string.edit_note))
                }
                Spacer(modifier = Modifier.width(4.dp))
                Button(onClick = {
                    viewModel.handleEvent(
                        RecordEvent.DeleteRecord(recordIndex),
                        context
                    )
                }) {
                    Text(stringResource(id = R.string.delete))
                }
            }
        }
    }
} 