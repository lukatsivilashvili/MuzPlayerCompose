package com.example.muzplayer.presentation.components.sleep_timer

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Preview
@Composable
fun SleepTimerDialogPreview() {
    val radioOptions = listOf(
        TimerValues.FIVE_MINUTES.stringValue,
        TimerValues.TEN_MINUTES.stringValue,
        TimerValues.FIFTEEN_MINUTES.stringValue,
        TimerValues.THIRTY_MINUTES.stringValue,
        TimerValues.FORTY_FIVE_MINUTES.stringValue,
        TimerValues.ONE_HOUR.stringValue,

        )
    val mContext = LocalContext.current

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    val (checkedState, onStateChange) = remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
        },
        title = {
            Text(text = "Sleep Timer")
        },
        text = {

            // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
            Column(Modifier.selectableGroup()) {
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = {
                                    onOptionSelected(text)
                                    Toast
                                        .makeText(mContext, selectedOption, Toast.LENGTH_SHORT)
                                        .show()
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = null // null recommended for accessibility with screenreaders
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
                Row(
                    Modifier
                        .height(56.dp)
                        .toggleable(
                            value = checkedState,
                            onValueChange = { onStateChange(!checkedState) },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checkedState,
                        onCheckedChange = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = "Play last song to end",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {

                }
            ) {
                Text("Start")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                }
            ) {
                Text("Cancel")
            }
        },
        properties = DialogProperties()
    )
}