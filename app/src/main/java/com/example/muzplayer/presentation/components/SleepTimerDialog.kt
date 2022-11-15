package com.example.muzplayer.presentation.components

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.muzplayer.domain.models.Song
import com.example.muzplayer.presentation.ui.bottom_bar.BottomBarViewModel
import java.util.Timer
import java.util.TimerTask
import kotlin.system.exitProcess

@Composable
fun SleepTimerDialog(
    isOpen: MutableState<Boolean>,
    song: Song,
    bottomBarViewModel: BottomBarViewModel
) {
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

    if (isOpen.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                isOpen.value = false
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
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        isOpen.value = false
                        setTimer(
                            sleepTime = handleSelectedTime(selectedOption),
                            bottomBarViewModel = bottomBarViewModel,
                            song = song
                        )
                    }
                ) {
                    Text("Start")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isOpen.value = false
                    }
                ) {
                    Text("Cancel")
                }
            },
            properties = DialogProperties()
        )
    }
}

private fun setTimer(sleepTime: Int, song: Song, bottomBarViewModel: BottomBarViewModel) {
    val timer = Timer()
    val pauseTask = object : TimerTask() {
        override fun run() {
            bottomBarViewModel.playOrToggleSong(mediaItem = song, toggle = true)
        }
    }

    val killTask = object : TimerTask() {
        override fun run() {
            exitProcess(1)
        }

    }

    timer.run {
        schedule(pauseTask, sleepTime * 60000.toLong())
        schedule(killTask, (sleepTime * 60000 + 1000).toLong())
    }

}

private fun handleSelectedTime(chosenText: String): Int {
    return when (chosenText) {
        TimerValues.FIVE_MINUTES.stringValue -> TimerValues.FIVE_MINUTES.minutesValue
        TimerValues.TEN_MINUTES.stringValue -> TimerValues.TEN_MINUTES.minutesValue
        TimerValues.FIFTEEN_MINUTES.stringValue -> TimerValues.FIFTEEN_MINUTES.minutesValue
        TimerValues.THIRTY_MINUTES.stringValue -> TimerValues.THIRTY_MINUTES.minutesValue
        TimerValues.FORTY_FIVE_MINUTES.stringValue -> TimerValues.FORTY_FIVE_MINUTES.minutesValue
        TimerValues.ONE_HOUR.stringValue -> TimerValues.ONE_HOUR.minutesValue
        else -> TimerValues.FIVE_MINUTES.minutesValue
    }
}

enum class TimerValues(val minutesValue: Int, val stringValue: String) {
    FIVE_MINUTES(minutesValue = 5, stringValue = "5 minutes"),
    TEN_MINUTES(minutesValue = 10, stringValue = "10 minutes"),
    FIFTEEN_MINUTES(minutesValue = 15, stringValue = "15 minutes"),
    THIRTY_MINUTES(minutesValue = 30, stringValue = "30 minutes"),
    FORTY_FIVE_MINUTES(minutesValue = 45, stringValue = "45 minutes"),
    ONE_HOUR(minutesValue = 60, stringValue = "1 hour"),

}