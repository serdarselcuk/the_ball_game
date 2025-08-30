package com.allfreeapps.theballgame.ui.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.viewModels.BallGameViewModel

@Composable
fun LearnerPopup(
    modifier: Modifier,
    color: Color,
    viewModel: BallGameViewModel = hiltViewModel()
) {
    val messages by viewModel.messagePool.collectAsState()
    val isAFreshUser by viewModel.isAFreshUser.collectAsState()

    if (messages.isEmpty()) {
        return // Don't proceed to show an empty popup
    }

    var doNotShowAgainChecked by remember { mutableStateOf(false) }
    // Show popup if the user is fresh AND there are messages
    var showPopup by remember(isAFreshUser) { mutableStateOf(messages.isNotEmpty() && isAFreshUser) }

    var currentMessageText by remember { mutableIntStateOf(messages.entries.first().key) }
    var currentMessageOffset by remember { mutableStateOf(messages.entries.first().value) }

    LaunchedEffect(messages.size) {
        if (messages.isEmpty()) {
            showPopup = false
        }
    }

    // Only compose the Popup if showPopup is true and there's a message to display
    if (showPopup) {
        Popup(
            alignment = Alignment.TopStart,
            offset = currentMessageOffset,
            onDismissRequest = { // User clicked outside the popup
                viewModel.removeAllMessages()
                showPopup = false
            }
        ) {
            Box(
                modifier
                    .background(
                        shape = RoundedCornerShape(10.dp),
                        brush = Brush.horizontalGradient(
                            colors = listOf(color, Color.White)
                        ),
                        alpha = 0.7f
                    )
                    .width(350.dp)
                    .wrapContentHeight()
                    .border(
                        2.dp,
                        brush = Brush.sweepGradient(colors = listOf(Color.Black, Color.White)),
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Column(
                    Modifier.padding(24.dp)
                ) {
                    Text(ContextCompat.getString(LocalContext.current, currentMessageText))
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Checkbox(
                                modifier = Modifier.weight(1f),
                                checked = doNotShowAgainChecked,
                                onCheckedChange = { isChecked ->
                                    Log.d("LearnerPopup", "Checkbox checked: $isChecked")
                                    doNotShowAgainChecked = isChecked
                                    viewModel.setUserAsAFreshUser(!isChecked)

                                },
                            )

                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 2.dp),
                                text = ContextCompat.getString(
                                    LocalContext.current,
                                    R.string.dont_show_again
                                ),
                                maxLines = 2,
                                style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.weight(0.2f))

                        // "Next" button: Only show if there's more than one message currently
                        if (messages.size > 1) {
                            ButtonWithText(
                                modifier = Modifier.weight(0.5f),
                                buttonText = ContextCompat.getString(
                                    LocalContext.current,
                                    R.string.next
                                ),
                                onclick = {
                                    viewModel.removeMessages()
                                    if (messages.isNotEmpty()) {
                                        currentMessageText = messages.entries.first().key
                                        currentMessageOffset = messages.entries.first().value
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.weight(0.1f))

                        ButtonWithText(
                            modifier = Modifier.weight(0.5f),
                            buttonText = if (messages.size > 1)
                                ContextCompat.getString(LocalContext.current, R.string.skip_all)
                            else
                                ContextCompat.getString(LocalContext.current, R.string.close),
                            onclick = {
                                showPopup = false
                            }
                        )
                    }
                }
            }
        }
    }
}
