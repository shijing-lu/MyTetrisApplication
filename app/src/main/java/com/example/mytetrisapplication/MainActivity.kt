package com.example.mytetrisapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold

import androidx.compose.ui.Modifier

import com.example.mytetrisapplication.ui.GameView.MainGameScreen


import com.example.mytetrisapplication.ui.theme.MyTetrisApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTetrisApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { padding ->
                    MainGameScreen(Modifier.padding(padding))
                }
            }
        }
    }
}

