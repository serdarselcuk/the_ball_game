package com.allfreeapps.theballgame

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.allfreeapps.theballgame.ui.composables.MainLayout
import com.allfreeapps.theballgame.ui.model.Ball
import com.allfreeapps.theballgame.ui.theme.BackgroundColor
import com.allfreeapps.theballgame.ui.theme.TheBallGameTheme
import com.allfreeapps.theballgame.utils.Constants

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: BallGameViewModel
    private lateinit var mainLayOut: MainLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = BallGameViewModel(applicationContext)
        mainLayOut = MainLayout(viewModel)

        enableEdgeToEdge()
        setContent {
            TheBallGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val totalBallCount by viewModel.totalBallCount.collectAsState()

                    when (totalBallCount) {
                        (Constants.gridSize * Constants.gridSize) -> {
                            Text(
                                text = "Game Over, You LOST!!!",
                                color = colorResource(id = R.color.white)
                            )
                            viewModel.saveScore()
                            Button(onClick = {viewModel.resetGame()}){
                                Text("OK")
                            }
                        }

//                        0 -> {
//                            Text(
//                                text = "Game Over, You WON!!!",
//                                color = colorResource(id = R.color.white)
//                            )
//                            Button(onClick = {viewModel.resetGame()}){
//                                Text("OK")
//                            }
//                        }

                        else -> {
                            mainLayOut.Build(
                                Modifier
                                    .padding(innerPadding)
                                    .background(BackgroundColor)
                            )
                        }
                    }
                }
            }
        }
    }
}