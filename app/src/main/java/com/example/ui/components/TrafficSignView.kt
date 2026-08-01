package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Question
import com.example.data.SignVisualType
import com.example.ui.theme.PakGoldAccent
import com.example.ui.theme.PakGreenPrimary
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TrafficSignCard(
    question: Question,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(24.dp))
            .background(Color.White, RoundedCornerShape(24.dp))
            .border(2.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(24.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RenderSignVisual(signType = question.signType, emoji = question.emoji)
        }
    }
}

@Composable
private fun RenderSignVisual(
    signType: SignVisualType,
    emoji: String
) {
    Box(
        modifier = Modifier
            .size(140.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        when (signType) {
            SignVisualType.STOP_OCTAGON -> OctagonStopSign()
            SignVisualType.NO_ENTRY -> NoEntrySign()

            // Prohibition / Restricted Signs (Red Circle with Red Border & Diagonal Slash)
            SignVisualType.NO_HORN,
            SignVisualType.NO_PEDESTRIAN,
            SignVisualType.NO_CYCLE,
            SignVisualType.NO_UTURN,
            SignVisualType.NO_OVERTAKING,
            SignVisualType.NO_PARKING,
            SignVisualType.NO_STOPPING,
            SignVisualType.NO_PHONE,
            SignVisualType.NO_DRUNK,
            SignVisualType.NO_TRUCKS -> RedProhibitionSign(emoji = emoji)

            // Speed & Weight & Height Limits (White Circle with Red Border Ring)
            SignVisualType.SPEED_LIMIT_50 -> SpeedLimitSign(number = "50")
            SignVisualType.WEIGHT_LIMIT -> SpeedLimitSign(number = "3.5t")
            SignVisualType.HEIGHT_LIMIT -> SpeedLimitSign(number = "3m")

            // Mandatory / Required Actions (Solid Blue Circle with White Border/Icon) - NO RED CIRCLE
            SignVisualType.BLUE_CIRCLE_RIGHT,
            SignVisualType.BLUE_CIRCLE_LEFT,
            SignVisualType.GO_STRAIGHT,
            SignVisualType.ROUNDABOUT,
            SignVisualType.HELMET,
            SignVisualType.SEATBELT,
            SignVisualType.HEADLIGHT,
            SignVisualType.ZEBRA_CROSSING -> BlueMandatorySign(emoji = emoji)

            // Parking Allowed (Blue Rectangle) - NO RED CIRCLE
            SignVisualType.PARKING_ALLOWED -> ParkingSign()

            // Warning Signs (Yellow Triangle with Red Border)
            SignVisualType.WARNING_TRIANGLE,
            SignVisualType.SLIPPERY_ROAD,
            SignVisualType.SCHOOL_AHEAD,
            SignVisualType.WORK_IN_PROGRESS,
            SignVisualType.SHARP_TURN,
            SignVisualType.CROSS_ROAD,
            SignVisualType.ZIGZAG_ROAD,
            SignVisualType.CHILDREN_CROSSING,
            SignVisualType.ROAD_CLOSED,
            SignVisualType.RAILWAY_CROSSING,
            SignVisualType.STEEP_DESCENT,
            SignVisualType.STEEP_ASCENT,
            SignVisualType.NARROW_BRIDGE,
            SignVisualType.ANIMALS_CROSSING,
            SignVisualType.LOW_AIRCRAFT,
            SignVisualType.FALLING_ROCKS -> TriangleWarningSign(emoji = emoji)

            // Information / Allowed Rules / General Knowledge (Green/Blue Info Card) - NO RED CIRCLE
            else -> GreenInfoSign(emoji = emoji)
        }
    }
}

@Composable
fun OctagonStopSign() {
    val octagonShape = GenericShape { size, _ ->
        val width = size.width
        val height = size.height
        val corner = width * 0.3f
        moveTo(corner, 0f)
        lineTo(width - corner, 0f)
        lineTo(width, corner)
        lineTo(width, height - corner)
        lineTo(width - corner, height)
        lineTo(corner, height)
        lineTo(0f, height - corner)
        lineTo(0f, corner)
        close()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(octagonShape)
            .background(Color(0xFFD32F2F))
            .border(4.dp, Color.White, octagonShape),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "🛑 STOP",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp
            )
            Text(
                text = "رکیں",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun NoEntrySign() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(Color(0xFFD32F2F))
            .border(4.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = 80.dp, height = 20.dp)
                .background(Color.White, RoundedCornerShape(4.dp))
        )
    }
}

@Composable
fun BlueMandatorySign(emoji: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(Color(0xFF0D47A1))
            .border(4.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = 54.sp
        )
    }
}

@Composable
fun SpeedLimitSign(number: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(Color.White)
            .border(8.dp, Color(0xFFD32F2F), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            color = Color.Black,
            fontWeight = FontWeight.Black,
            fontSize = 32.sp
        )
    }
}

@Composable
fun ParkingSign() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0D47A1))
            .border(4.dp, Color.White, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "P",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 60.sp
        )
    }
}

@Composable
fun TriangleWarningSign(emoji: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val path = Path().apply {
                moveTo(width / 2f, 8f)
                lineTo(width - 8f, height - 8f)
                lineTo(8f, height - 8f)
                close()
            }
            // Yellow fill
            drawPath(path, color = Color(0xFFFFD54F))
            // Red border
            drawPath(
                path = path,
                color = Color(0xFFD32F2F),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 12f)
            )
        }
        Text(
            text = emoji,
            fontSize = 44.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun RedProhibitionSign(emoji: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(Color.White)
            .border(6.dp, Color(0xFFD32F2F), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            // Draw diagonal red prohibition slash line
            drawLine(
                color = Color(0xFFD32F2F),
                start = Offset(width * 0.2f, height * 0.2f),
                end = Offset(width * 0.8f, height * 0.8f),
                strokeWidth = 10f
            )
        }
        Text(
            text = emoji,
            fontSize = 48.sp
        )
    }
}

@Composable
fun GreenInfoSign(emoji: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(Color(0xFF00796B))
            .border(4.dp, Color(0xFFFFD54F), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = 50.sp
        )
    }
}
