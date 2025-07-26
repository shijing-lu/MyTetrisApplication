package com.example.mytetrisapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytetrisapplication.ui.GameView.MainGameScreen
import com.example.mytetrisapplication.ViewModel.GameViewModel
import com.example.mytetrisapplication.ui.theme.MyTetrisApplicationTheme

class MainActivity : ComponentActivity() {
    private lateinit var gameViewModel: GameViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTetrisApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { padding ->
                    // 获取ViewModel引用以便在生命周期方法中使用
                    gameViewModel = viewModel()
                    MainGameScreen(
                        modifier = Modifier.padding(padding),
                        viewModel = gameViewModel
                    )
                }
            }
        }
    }
    
    override fun onPause() {
        super.onPause()
        // 当Activity暂停时（如按下Home键、打开其他应用、点击更多按钮等），自动暂停游戏
        if (::gameViewModel.isInitialized) {
            val gameState = gameViewModel.gameState.value
            if (gameState.isStarted && !gameState.isGameOver && !gameState.isPaused) {
                gameViewModel.pauseGame()
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
    }
}

