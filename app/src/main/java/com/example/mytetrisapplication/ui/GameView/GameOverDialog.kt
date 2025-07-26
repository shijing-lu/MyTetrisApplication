package com.example.mytetrisapplication.ui.GameView

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.example.mytetrisapplication.R

@Composable
fun GameOverDialog(
    score: Int,
    time: Int,
    onRestart: () -> Unit,
    onSaveRecord: () -> Unit
) {
    // 只在首次显示时保存记录
    val alreadySaved = remember { mutableStateOf(false) }
    val currentOnSaveRecord by rememberUpdatedState(onSaveRecord)
    LaunchedEffect(score, time) {
        if (!alreadySaved.value) {
            currentOnSaveRecord()
            alreadySaved.value = true
        }
    }
    AlertDialog(
        onDismissRequest = {},
        title = { Text(stringResource(id = R.string.game_over)) },
        text = { Text(stringResource(id = R.string.score_and_time, score, time)) },
        confirmButton = {
            TextButton(onClick = onRestart) {
                Text(stringResource(id = R.string.restart))
            }
        }
    )
} 