package com.example.mytetrisapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mytetrisapplication.ui.RecordViewModel
import com.example.mytetrisapplication.ui.RecordEvent
import com.example.mytetrisapplication.ui.theme.MyTetrisApplicationTheme
import com.google.gson.reflect.TypeToken

class RecordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTetrisApplicationTheme {
                RecordScreen(
                    onBack = { finish() },
                    viewModel = viewModel()
                )
            }
        }
    }
}

// 记录数据类
data class GameRecord(val score: Int, val time: Int, val date: String, val note: String = "")

fun saveRecord(context: Context, record: GameRecord) {
    val prefs = context.getSharedPreferences("records", Context.MODE_PRIVATE)
    val gson = Gson()
    val list = loadRecords(context).toMutableList()
    list.add(0, record)
    prefs.edit().putString("list", gson.toJson(list)).apply()
}

fun loadRecords(context: Context): List<GameRecord> {
    val prefs = context.getSharedPreferences("records", Context.MODE_PRIVATE)
    val json = prefs.getString("list", null) ?: return emptyList()
    val type = object : TypeToken<List<GameRecord>>(){}.type
    return Gson().fromJson(json, type)
}

@Composable
fun RecordScreen(
    onBack: () -> Unit,
    viewModel: RecordViewModel = viewModel()
) {
    val context = LocalContext.current
    val recordState by viewModel.recordState.collectAsStateWithLifecycle()
    var noteText by remember { mutableStateOf(TextFieldValue("")) }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("历史记录  ")
            Button(onClick = { viewModel.handleEvent(RecordEvent.SortByScore, context) }) { 
                Text("按得分") 
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.handleEvent(RecordEvent.SortByDate, context) }) { 
                Text("按日期") 
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(sortedRecords.indices.toList()) { idx ->
                val record = sortedRecords[idx]
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
                            Text("得分: ", fontWeight = FontWeight.Bold)
                            Text("${record.score}", color = Color(0xFF1976D2), fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("时长: ", fontWeight = FontWeight.Bold)
                            Text("${record.time}s", color = Color(0xFF388E3C), fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("日期: ${record.date}", color = Color.Gray, fontWeight = FontWeight.Light)
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("备注: ", fontWeight = FontWeight.Bold)
                            Text(record.note, modifier = Modifier.weight(1f))
                            Button(onClick = {
                                viewModel.handleEvent(
                                    RecordEvent.StartEditNote(record.note, recordState.records.indexOf(record)), 
                                    context
                                )
                                noteText = TextFieldValue(record.note)
                            }) { 
                                Text("编辑备注") 
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Button(onClick = {
                                viewModel.handleEvent(
                                    RecordEvent.DeleteRecord(recordState.records.indexOf(record)), 
                                    context
                                )
                            }) {
                                Text("删除")
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) { Text("返回") }
    }
    
    // 备注编辑弹窗
    if (recordState.editingNote != null && recordState.editingIndex >= 0) {
        AlertDialog(
            onDismissRequest = { 
                viewModel.handleEvent(RecordEvent.CancelEditNote, context) 
            },
            title = { Text("编辑备注") },
            text = {
                Column {
                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        label = { Text("备注内容") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Button(onClick = {
                            clipboardManager.setText(AnnotatedString(noteText.text))
                        }) { Text("复制备注") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            val clip = clipboardManager.getText()?.text ?: ""
                            noteText = TextFieldValue(noteText.text + clip)
                        }) { Text("粘贴备注") }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.handleEvent(RecordEvent.SaveNote(noteText.text), context)
                }) { Text("保存") }
            },
            dismissButton = {
                TextButton(onClick = { 
                    viewModel.handleEvent(RecordEvent.CancelEditNote, context) 
                }) { Text("取消") }
            }
        )
    }
}

@Preview
@Composable
fun RecordScreenPreview() {
    RecordScreen(onBack = {})
} 