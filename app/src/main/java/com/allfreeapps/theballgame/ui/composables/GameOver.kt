package com.allfreeapps.theballgame.ui.composables

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.model.entities.Score
import com.allfreeapps.theballgame.ui.theme.GameOverBackground
import com.allfreeapps.theballgame.ui.theme.HeaderTextColor
import com.allfreeapps.theballgame.ui.theme.UserNameFieldColor

@Composable
fun GameOverScreen(
    onSaveScoreClicked: (username: String) -> Unit,
    onSkipClicked: () -> Unit,
    onSettingsClicked: () -> Unit = {},
    onDeleteClicked: (Int?) -> Unit = {},
    allScores: List<Score>
) {
    var scoreSaved = remember {  false }
    // State for the TextField
    if(scoreSaved){
        ScoresTable(
            modifier = Modifier
                .fillMaxWidth(),
            scores = allScores,
            onDeleteClicked = { id ->
                onDeleteClicked(id)
            }
        )
    }else {
        SavingScoreScreen(
            onSaveScoreClicked = { username ->
                onSaveScoreClicked(username)
                scoreSaved = true
            },
            onSkipClicked = onSkipClicked,
            onSettingsClicked = onSettingsClicked,
        )
    }
}

@Composable
fun SavingScoreScreen(
    onSaveScoreClicked: (username: String) -> Unit,
    onSkipClicked: () -> Unit,
    onSettingsClicked: () -> Unit = {}
){
    var username by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameOverBackground)
    ) {

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
                        color = Color.Black,
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

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .wrapContentHeight(),
                value = username,
                onValueChange = { username = it },
                label = {
                    Text("Enter your name")
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = HeaderTextColor,
                    unfocusedTextColor = HeaderTextColor.copy(alpha = 0.5f),
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Black,
                    focusedContainerColor = UserNameFieldColor.copy(alpha = 0.5f),
                    unfocusedContainerColor = UserNameFieldColor
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
}

@Preview(showBackground = true)
@Composable
fun PreviewGameOverScreen(){
    GameOverScreen(onSaveScoreClicked = {}, onSkipClicked = {}, onSettingsClicked = {}, onDeleteClicked = {}, allScores = listOf())
}