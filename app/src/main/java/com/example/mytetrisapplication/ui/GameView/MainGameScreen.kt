package com.example.mytetrisapplication.ui.GameView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytetrisapplication.database.GameRecordEntity
import com.example.mytetrisapplication.RecordActivity
import com.example.mytetrisapplication.ViewModel.GameViewModel
import com.example.mytetrisapplication.ui.theme.MyTetrisApplicationTheme
import java.text.SimpleDateFormat
import java.util.Date
import com.example.mytetrisapplication.R
import android.content.Intent


@Composable
fun MainGameScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel()
) {
    val context = LocalContext.current
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()

    if (gameState.isGameOver) {
        GameOverDialog(
            score = gameState.score,
            time = gameState.time,
            onRestart = { viewModel.resetGameState() },
            onSaveRecord = {
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                viewModel.saveGameRecord(context, GameRecordEntity(score = gameState.score, time = gameState.time, date = date))
            },
            onViewRecords = {
                viewModel.resetGameState() // 先重置游戏状态，防止返回后弹窗再出现
                val intent = Intent(context, RecordActivity::class.java)
                context.startActivity(intent)
            }
        )
    }
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(MainScreenPadding)
    ) {
        // 左侧
        Column(
            modifier = Modifier
                .weight(6f)
                .fillMaxHeight()
        ) {
            ScorePanel(value = gameState.time, message = stringResource(id = R.string.game_time))
            GameBoard(
                board = gameState.board,
                activeBlock = gameState.activeBlock,
                ghostBlock = gameState.ghostBlock, // 传入预测方块位置
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        // 右侧
        Column(
            modifier = Modifier
                .weight(4f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScorePanel(value = gameState.score, message = stringResource(id = R.string.score))
            NextBlockPanel(nextBlockShape = gameState.nextBlockShape)
            ActionButton(
                text = if (!gameState.isStarted) stringResource(id = R.string.start_game)
                else stringResource(id = R.string.end_game),
                onClick = {
                    if (!gameState.isStarted) {
                        viewModel.startNewGame()
                    } else {
                        viewModel.resetGameState()
                    }
                },
                modifier = Modifier.padding(top = SectionTopPadding)
            )
            Spacer(modifier = Modifier.height(SectionSpacing))
            ActionButton(
                text = if (gameState.isPaused) stringResource(id = R.string.resume)
                else stringResource(
                    id = R.string.pause
                ),
                onClick = {
                    if (gameState.isPaused) viewModel.resumeGame()
                    else viewModel.pauseGame()
                },
                enabled = gameState.isStarted && !gameState.isGameOver // 只有游戏开始且未结束时才启用
            )
            Spacer(modifier = Modifier.height(SectionSpacing))
            ActionButton(
                text = if (gameState.assistMode) "关闭辅助" else "辅助模式",
                onClick = {
                    viewModel.toggleAssistMode()
                },
                backgroundColor = if (gameState.assistMode) Color(0xFF4CAF50) else Color(0xFF9E9E9E),
                textColor = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            GameControlPanel(
                onLeft = { viewModel.moveLeft() },
                onRight = { viewModel.moveRight() },
                onDown = { viewModel.moveDown() },
                onRotate = { viewModel.rotate() },
                onHardDrop = { viewModel.hardDrop() },

            )

            Spacer(modifier = Modifier.weight(1f))
            ActionButton(
                text = stringResource(id = R.string.more),
                onClick = { 
                    val intent = Intent(context, RecordActivity::class.java)
                    context.startActivity(intent)
                },
                backgroundColor = Color(0xFFE0E0E0),
                textColor = Color.Black,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(ActionButtonHeight)
                    .padding(bottom = SectionSpacing)
            )
        }
    }
}

@Preview
@Composable
fun pre() {
    MyTetrisApplicationTheme {
        MainGameScreen()
    }
} 