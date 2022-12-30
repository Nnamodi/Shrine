package com.roland.android.shrine.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ShrineTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun ComponentTest() {
    Column(
        modifier = Modifier.padding(32.dp)
    ) {
        ShrineTheme {
            Column {
                ShrineButton()
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        MaterialTheme {
            Column {
                Button(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Face"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("This is a Material baseline button")
                }
            }
        }
    }
}

@Composable
private fun ShrineButton() {
    Button(onClick = {}) {
        Icon(
            imageVector = Icons.Default.Face,
            contentDescription = "Face"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("This is a Shrine button".uppercase())
    }
}