package com.allfreeapps.theballgame.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.sp
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.ui.theme.BackgroundColor
import com.allfreeapps.theballgame.ui.theme.GameOverBackground
import com.allfreeapps.theballgame.ui.theme.HeaderTextColor


@Composable
fun GameOverScreen(
    onSaveScoreClicked: (username: String) -> Unit, // Callback now includes the username
    onSkipClicked: () -> Unit
) {
    // State for the TextField
    var username by remember { mutableStateOf("") }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(GameOverBackground)) {
        Image(
            painter = painterResource(id = R.drawable.game_over_screen),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )

        TextButton(
            onClick = onSkipClicked,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
           Text(text = "SKIP", color = Color.White, fontSize = 20.sp)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                // .background(BackgroundColor) // Consider removing if background image is sufficient
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Enter your name") },
                singleLine = true,
                modifier = Modifier.
                fillMaxWidth(0.8f) // Make TextField take 80% of column width
                    .background(BackgroundColor)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth(0.5f) // Make Button take 80% of column width
                    .background(BackgroundColor.copy(alpha = 0.5f)),
                onClick = { onSaveScoreClicked(username) },
                enabled = username.isNotBlank() // Enable button only if username is not empty
            ) {
                Text("SAVE SCORE")
            }

            Spacer(modifier = Modifier.height(100.dp)) // Increased space
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGameOverScreen(){
    GameOverScreen(onSaveScoreClicked = {}, onSkipClicked = {})
}