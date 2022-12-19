package com.whbnd.canvascompose

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ClockStyle(
    val clockRadius: Dp = 100.dp,
    val backgroundColor: Int = android.graphics.Color.WHITE,
    val handHoursColor: Color = Color.Black,
    val handHoursLength: Dp = 150.dp,
    val handHoursWidth: Dp = 4.dp,
    val handMinutesColor: Color = Color.Black,
    val handMinutesLength: Dp = 170.dp,
    val handMinutesWidth: Dp = 3.dp,
    val handSecondsColor: Color = Color.Red,
    val handSecondsLength: Dp = 185.dp,
    val handSecondsWidth: Dp = 2.dp,
    val hourStepColor: Color = Color.DarkGray,
    val hourStepLength: Dp = 20.dp,
    val minuteStepColor: Color = Color.LightGray,
    val minuteStepLength: Dp = 15.dp
)
