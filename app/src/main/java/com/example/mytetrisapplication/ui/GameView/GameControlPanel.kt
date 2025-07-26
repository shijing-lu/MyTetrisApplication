package com.example.mytetrisapplication.ui.GameView

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mytetrisapplication.R
import com.example.mytetrisapplication.ui.GameView.ControlPanelIconSize

@Composable
fun GameControlPanel(
    onLeft: () -> Unit,
    onRight: () -> Unit,
    onDown: () -> Unit,
    onRotate: () -> Unit,
    onHardDrop: () -> Unit // 新增
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = ControlPanelIconSize)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onRotate) {
                Image(
                    painterResource(id = R.drawable.up),
                    contentDescription = "旋转",
                    modifier = Modifier.size(ControlPanelIconSize)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onLeft) {
                Image(
                    painterResource(id = R.drawable.left),
                    contentDescription = "左",
                    modifier = Modifier.size(ControlPanelIconSize)
                )
            }
            IconButton(onClick = onRight) {
                Image(
                    painterResource(id = R.drawable.right),
                    contentDescription = "右",
                    modifier = Modifier.size(ControlPanelIconSize)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onDown) {
                Image(
                    painterResource(id = R.drawable.down),
                    contentDescription = "下",
                    modifier = Modifier.size(ControlPanelIconSize)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        // 新增：快速下落按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onHardDrop) {
                Image(
                    painterResource(id = R.drawable.double_down),
                    contentDescription = "快速下落",
                    modifier = Modifier.size(ControlPanelIconSize)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
} 