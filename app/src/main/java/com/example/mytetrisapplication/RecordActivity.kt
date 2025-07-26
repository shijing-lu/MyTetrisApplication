package com.example.mytetrisapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mytetrisapplication.ui.RecordScreenRoot
import com.example.mytetrisapplication.ui.theme.MyTetrisApplicationTheme

class RecordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTetrisApplicationTheme {
                RecordScreenRoot(onBack = { finish() })
            }
        }
    }
} 