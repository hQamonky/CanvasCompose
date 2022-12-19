package com.whbnd.canvascompose

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.whbnd.canvascompose.ui.theme.CanvasComposeTheme
import kotlinx.coroutines.delay
import kotlin.math.*
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

enum class NavigationRoute(val text: String) {
    MAIN_MENU_ROUTE("Main Menu"),
    SIMPLE_SHAPES_ROUTE("Simple Shapes"),
    CLICK_GAME_ROUTE("Click Game"),
    SCALE_ROUTE("Scale"),
    CLOCK_ROUTE("Clock")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = NavigationRoute.MAIN_MENU_ROUTE.name) {
                composable(NavigationRoute.MAIN_MENU_ROUTE.name) {
                    OptionSelectorScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        options = NavigationRoute
                            .values()
                            .toMutableList()
                            .subList(1, NavigationRoute.values().size)
                            .map{ it.text },
                        onOptionSelected = {
                            var route: String = NavigationRoute.MAIN_MENU_ROUTE.name
                            NavigationRoute.values().map { navigationRoute ->
                                if (navigationRoute.text == it) route = navigationRoute.name
                            }
                            navController.navigate(route)
                        }
                    )
                }

                composable(NavigationRoute.SIMPLE_SHAPES_ROUTE.name) {
                    SimpleShapesCanvas()
                }

                composable(NavigationRoute.CLICK_GAME_ROUTE.name) {
                    ClickGameCanvas()
                }

                composable(NavigationRoute.SCALE_ROUTE.name) {
                    ScaleCanvas()
                }

                composable(NavigationRoute.CLOCK_ROUTE.name) {
                    ClockCanvas()
                }
            }
        }
    }
}

@Composable
fun OptionSelectorScreen (
    modifier: Modifier = Modifier,
    options: List<String> = emptyList(),
    onOptionSelected: (selectedMode: String) -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        options.forEach { option ->
            ClickableText(
                modifier = Modifier.padding(12.dp),
                text = AnnotatedString(option),
                onClick = {
                    onOptionSelected(option)
                }
            )
            if (option != options.last()) {
                Divider()
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CanvasComposeTheme {
        Greeting("Android")
    }
}

@Composable
fun SimpleShapesCanvas() {
    Canvas(
        modifier = Modifier
            .padding(20.dp)
            .size(300.dp)
    ) {
        drawRect(
            color = Color.Black,
            size = size
        )
        drawRect(
            color = Color.Red,
            topLeft = Offset(100f, 100f),
            size = Size(100f, 100f),
            style = Stroke(
                width = 3.dp.toPx()
            )
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.Red, Color.Yellow),
                center = center,
                radius = 100f
            ),
            radius = 100f
        )
        drawArc(
            color = Color.Green,
            startAngle = 0f,
            sweepAngle = 270f,
            useCenter = false,
            topLeft = Offset(100f, 500f),
            size = Size(200f,200f),
            style = Stroke(
                width = 3.dp.toPx()
            )
        )
        drawOval(
            color = Color.Magenta,
            topLeft = Offset(500f, 100f),
            size = Size(200f, 300f)
        )
        drawLine(
            color = Color.Cyan,
            start = Offset(300f, 700f),
            end = Offset(700f, 700f),
            strokeWidth = 5.dp.toPx()
        )
    }
}

@Composable
fun ClickGameCanvas() {
    var points by remember {
        mutableStateOf(0)
    }
    var isTimerRunning by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Points: $points",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Button(onClick = {
                isTimerRunning = !isTimerRunning
                points = 0
            }) {
                Text(text = if(isTimerRunning) "Reset" else "Start")
            }
            CountDownTimer(
                isTimerRunning = isTimerRunning
            ) {
                isTimerRunning = false
            }
        }
        BallClicker(enabled = isTimerRunning) {
            points++
        }
    }
}

@Composable
fun CountDownTimer(
    time: Int = 30000,
    isTimerRunning: Boolean = false,
    onTimerEnd: () -> Unit = {}
) {
    var curTime by remember {
        mutableStateOf(time)
    }
    LaunchedEffect(key1 = curTime, key2 = isTimerRunning) {
        if (!isTimerRunning) {
            curTime = time
            return@LaunchedEffect
        }
        if (curTime > 0) {
            delay(1000L)
            curTime -= 1000
        } else {
            onTimerEnd()
        }
    }
    Text(
        text = (curTime / 1000).toString(),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun BallClicker(
    radius: Float = 100f,
    enabled: Boolean = false,
    ballColor: Color = Color.Red,
    onBallClick: () -> Unit = {}
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        var ballPosition by remember {
            mutableStateOf(randomOffset(
                radius = radius,
                width = constraints.maxWidth,
                height = constraints.maxHeight
            ))
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(enabled) {
                    if (!enabled) {
                        return@pointerInput
                    }
                    detectTapGestures {
                        val distance = sqrt(
                            (it.x - ballPosition.x).pow(2) +
                                    (it.y - ballPosition.y).pow(2)
                        )
                        if (distance <= radius) {
                            ballPosition = randomOffset(
                                radius = radius,
                                width = constraints.maxWidth,
                                height = constraints.maxHeight
                            )
                            onBallClick()
                        }
                    }
                }
        ) {
            drawCircle(
                color = ballColor,
                radius = radius,
                center = ballPosition
            )
        }
    }
}

private fun randomOffset(radius: Float, width: Int, height: Int) : Offset {
    return Offset(
        x = nextInt(radius.roundToInt(), width - radius.roundToInt()).toFloat(),
        y = nextInt(radius.roundToInt(), height - radius.roundToInt()).toFloat()
    )
}

@Composable
fun ScaleCanvas() {
    var weight by remember {
        mutableStateOf(80f)
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            text = "Select your weight",
            textAlign = TextAlign.Center,
            fontSize = 30.sp
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            textAlign = TextAlign.Center,
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 100.sp)) {
                    append(weight.toInt().toString())
                }
                withStyle(style = SpanStyle(color = Color.Green, fontSize = 30.sp)) {
                    append(" KG")
                }
            }
        )
        Scale(
            style = ScaleStyle(scaleWidth = 150.dp),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(300.dp)
        ) {
            weight = it.toFloat()
        }
    }
}

@Composable
fun ClockCanvas() {
    Box(modifier = Modifier.fillMaxSize()) {
        val milliseconds = remember {
            System.currentTimeMillis()
        }
        var seconds by remember {
            mutableStateOf((milliseconds / 1000f) % 60f)
        }
        var minutes by remember {
            mutableStateOf(((milliseconds / 1000f) / 60) % 60f)
        }
        var hours by remember {
            mutableStateOf((milliseconds / 1000f) / 3600f + 1f)
        }
        LaunchedEffect(key1 = seconds) {
            delay(1000L)
            seconds += 1f
            minutes += 1f / 60f
            hours += 1f / (60f * 60f * 12f)
            Log.d("Clock", "$hours:$minutes:$seconds")
        }
        Clock(
            modifier = Modifier.align(Alignment.Center),
            hours = hours,
            minutes = minutes,
            seconds = seconds
        )
    }
}

@Composable
fun Clock(
    modifier: Modifier = Modifier,
    hours: Float = 0f,
    minutes: Float = 0f,
    seconds: Float = 0f,
    style: ClockStyle = ClockStyle()
) {
    Canvas(modifier = modifier.size(style.clockRadius * 2f)) {
        val radius = style.clockRadius.toPx()
        // Draw Circle
        drawContext.canvas.nativeCanvas.apply {
            drawCircle(
                center.x,
                center.y,
                radius,
                Paint().apply {
                    setStyle(Paint.Style.FILL)
                    color = style.backgroundColor
                    setShadowLayer(
                        60f,
                        0f,
                        0f,
                        android.graphics.Color.argb(50,0,0,0)
                    )
                }
            )
        }
        // Draw Steps
        for (i in 0..59) {
            val angleInRad = i * (360 / 60) * (PI / 180).toFloat()
            val lineType = when {
                i % 5 == 0 -> LineType.FiveStep
                else -> LineType.Normal
            }
            val lineLength = when(lineType) {
                LineType.FiveStep -> style.hourStepLength.toPx()
                else -> style.minuteStepLength.toPx()
            }
            val lineColor = when(lineType) {
                LineType.FiveStep -> style.hourStepColor
                else -> style.minuteStepColor
            }
            val lineStart = Offset(
                x = (radius - lineLength) * cos(angleInRad) + center.x,
                y = (radius - lineLength) * sin(angleInRad) + center.y
            )
            val lineEnd = Offset(
                x = radius * cos(angleInRad) + center.x,
                y = radius * sin(angleInRad) + center.y
            )
            drawLine(
                color = lineColor,
                start = lineStart,
                end = lineEnd,
                strokeWidth = 1.dp.toPx()
            )
        }
        // Draw Hands
        rotate(degrees = hours * (360f / 12f) + 180) {
            drawLine(
                color = style.handHoursColor,
                start = center,
                end = Offset(center.x, style.handHoursLength.toPx()),
                strokeWidth = style.handHoursWidth.toPx(),
                cap = StrokeCap.Round
            )
        }
        rotate(degrees = minutes * (360f / 60f) + 180) {
            drawLine(
                color = style.handMinutesColor,
                start = center,
                end = Offset(center.x, style.handMinutesLength.toPx()),
                strokeWidth = style.handMinutesWidth.toPx(),
                cap = StrokeCap.Round
            )
        }
        rotate(degrees = seconds * (360f / 60f)) {
            drawLine(
                color = style.handSecondsColor,
                start = center,
                end = Offset(center.x, style.handSecondsLength.toPx()),
                strokeWidth = style.handSecondsWidth.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}