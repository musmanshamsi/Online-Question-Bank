package com.example.quizapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quizapp.ui.theme.CorrectGreen
import com.example.quizapp.ui.theme.WrongRed

@Composable
fun AnswerButton(
    text: String,
    isSelected: Boolean,
    isCorrectAnswer: Boolean,
    showFeedback: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor: Color
    val contentColor: Color
    val borderStroke: BorderStroke?

    when {
        showFeedback -> {
            when {
                isCorrectAnswer -> {
                    // Show correct answer in emerald green
                    containerColor = CorrectGreen.copy(alpha = 0.15f)
                    contentColor = CorrectGreen
                    borderStroke = BorderStroke(2.dp, CorrectGreen)
                }
                isSelected -> {
                    // Show wrong selected answer in crimson red
                    containerColor = WrongRed.copy(alpha = 0.15f)
                    contentColor = WrongRed
                    borderStroke = BorderStroke(2.dp, WrongRed)
                }
                else -> {
                    // Other options: faded slate
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    borderStroke = null
                }
            }
        }
        isSelected -> {
            // Selected but not submitted yet: beautiful indigo overlay
            containerColor = MaterialTheme.colorScheme.primaryContainer
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            borderStroke = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        }
        else -> {
            // Normal idle state
            containerColor = MaterialTheme.colorScheme.surface
            contentColor = MaterialTheme.colorScheme.onSurface
            borderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
        }
    }

    val animatedContainerColor by animateColorAsState(targetValue = containerColor, label = "ButtonBg")
    val animatedContentColor by animateColorAsState(targetValue = contentColor, label = "ButtonContent")

    Card(
        onClick = onClick,
        enabled = !showFeedback,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = animatedContainerColor,
            contentColor = animatedContentColor
        ),
        border = borderStroke,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected && !showFeedback) 4.dp else 1.dp
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            if (showFeedback) {
                Spacer(modifier = Modifier.width(12.dp))
                when {
                    isCorrectAnswer -> {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = "Correct",
                            tint = CorrectGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    isSelected -> {
                        Icon(
                            imageVector = Icons.Rounded.Cancel,
                            contentDescription = "Incorrect",
                            tint = WrongRed,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
