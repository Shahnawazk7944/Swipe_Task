package dev.swipe_task

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.designsystem.theme.Swipe_TaskTheme
import dev.swipe_task.presentation.navGraph.Swipe_TaskNavGraph

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Swipe_TaskTheme {
                Swipe_TaskNavGraph()
            }
        }
    }
}