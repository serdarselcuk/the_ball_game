package com.allfreeapps.theballgame.ui.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.ui.composables.coreAppComposables.GameText
import com.allfreeapps.theballgame.viewModels.GameOverScreenViewModel

@Composable
fun GameOverScreen(
    viewModel: GameOverScreenViewModel = hiltViewModel(),
    modifier: Modifier,
    score: Int,
    onSkipClicked: () -> Unit,
    onSaveScoreClicked: () -> Unit,
    onSettingsClicked: () -> Unit = {},
) {
    Box(
        modifier = modifier
    ) {
        SavingScoreScreen(
            modifier = modifier,
            score = score,
            onSaveScoreClicked = { username ->
                viewModel.saveScoreClicked(username, score)
                onSaveScoreClicked()
            },
            onSkipClicked = {
                viewModel.skipClicked()
                onSkipClicked()
            },
            onSettingsClicked = {
                viewModel.playClickSound()
                onSettingsClicked()
            },
        )
    }
}

@Composable
fun SavingScoreScreen(
    modifier: Modifier,
    score: Int,
    onSaveScoreClicked: (username: String) -> Unit,
    onSkipClicked: () -> Unit,
    onSettingsClicked: () -> Unit = {}
){
    var username by remember { mutableStateOf("") }
    var displayedScore by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = score) {
        val animationSpec = tween<Float>(durationMillis = 1000, easing = LinearEasing)
        animate(
            initialValue = 0F,
            targetValue = score.toFloat(),
            animationSpec = animationSpec
        ) { value, /* velocity */ _ ->
            displayedScore = value.toInt()
        }
    }

    Image(
        painter = painterResource(id = R.drawable.game_over_screen),
        contentDescription = "Background",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth()
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(top = 50.dp)
    ) {

        SettingsButton(
            onClick = { onSettingsClicked() },
            modifier = Modifier
                .wrapContentHeight()
                .width(110.dp)
                .align(Alignment.TopStart)
                .padding(16.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = ButtonDefaults.shape
                ),
        )

        SkipButton(
            onClick = onSkipClicked,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        GameText(
            text = "Your score: $displayedScore",
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .wrapContentHeight(),
            value = username,
            onValueChange = { username = it },
            label = {
                GameText("Enter your name")
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Black,
                focusedContainerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        SaveScoreButton(
            modifier = Modifier.fillMaxWidth(0.5f), // Make Button take 50% of column width,
            onClick = {
                onSaveScoreClicked(username)
            },
            username = username
        )

        Spacer(modifier = Modifier.height(100.dp)) // Increased space
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewGameOverScreen(){
    GameOverScreen(
        modifier = Modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.onPrimary),
        score = 100,
        onSaveScoreClicked = {},
        onSkipClicked = {},
        onSettingsClicked = {},
    )
}
