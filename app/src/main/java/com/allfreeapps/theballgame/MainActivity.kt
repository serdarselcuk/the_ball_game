package com.allfreeapps.theballgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.composables.MainLayout
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<BallGameViewModel>()
    private lateinit var mainLayOut: MainLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainLayOut = MainLayout(viewModel)

        enableEdgeToEdge()
        setContent {
            TheBallGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    mainLayOut.Build(Modifier.padding(innerPadding))
                }
            }
        }
    }
}