package dev.swipe_task

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.android.AndroidEntryPoint
import dev.designsystem.theme.Swipe_TaskTheme
import dev.swipe_task.presentation.features.products.components.ConnectivityStatus
import dev.swipe_task.presentation.features.utils.ConnectivityViewModel
import dev.swipe_task.presentation.navGraph.Swipe_TaskNavGraph

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val connectivityViewModel: ConnectivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Swipe_TaskTheme {
                Scaffold(
                    topBar = {
                        ConnectivityStatus(connectivityViewModel)
                    }
                ) { paddingValues ->
                    val context = LocalContext.current
                    LaunchedEffect(connectivityViewModel.localUploadSuccessMessage) {
                        connectivityViewModel.localUploadSuccessMessage.collect { message ->
                            message?.let {
                                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .consumeWindowInsets(paddingValues)
                    ) {
                        Swipe_TaskNavGraph()
                    }
                }
            }
        }
    }
}