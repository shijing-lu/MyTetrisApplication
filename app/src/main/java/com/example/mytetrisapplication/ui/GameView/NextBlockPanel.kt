package com.example.mytetrisapplication.ui.GameView

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.unit.dp
import com.example.mytetrisapplication.ui.GameView.NextBlockPanelHeight
import com.example.mytetrisapplication.ui.GameView.NextBlockPanelTopPadding
import com.example.mytetrisapplication.ui.GameView.CardCornerRadius
import com.example.mytetrisapplication.ui.GameView.NextBlockPanelElevation
import com.example.mytetrisapplication.ui.GameView.NextBlockPanelSize

@Composable
fun NextBlockPanel(nextBlockShape: List<Pair<Int, Int>>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(NextBlockPanelHeight)
            .padding(top = NextBlockPanelTopPadding),
        shape = RoundedCornerShape(CardCornerRadius),
        elevation = CardDefaults.cardElevation(NextBlockPanelElevation),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD0D0D0))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Canvas(modifier = Modifier.size(NextBlockPanelSize)) {
                val cell = size.width / 4
                Log.d("NextBlockPanel", "nextBlockShape: $nextBlockShape")
                for ((r, c) in nextBlockShape) {

                    drawBlockCell(
                        row = r,
                        col = c,
                        cellWidth = cell,
                        cellHeight = cell,
                        cellSpacing = 10f // 可调整
                    )
                }
//                for ((r, c) in nextBlockShape) {
//                    drawRect(
//                        color = Color.Blue,
//                        topLeft = Offset(c * cell, (r + 1) * cell),
//                        size = Size(cell, cell),
//                        style = Fill
//                    )
//                }
            }
        }
    }
} 