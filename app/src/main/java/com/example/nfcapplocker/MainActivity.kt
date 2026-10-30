package com.example.nfcapplocker

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Palette sampled from Brick screenshots
val BgColor = Color(0xFFEBE7E4)
val CardColor = Color(0xFFFAF9F8)
val DarkText = Color(0xFF1E1E1E)
val SubtitleColor = Color(0xFF7F7B78)
val AccentRed = Color(0xFFC04B4B)
val LightPill = Color(0xFFE2DDD8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BrickApp()
        }
    }
}

@Composable
fun BrickApp() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Brick", "Routines", "Activity", "Settings")

    Scaffold(
        containerColor = BgColor,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgColor)
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTab == index
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { selectedTab = index }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = title,
                                color = if (isSelected) DarkText else SubtitleColor,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .background(
                                        if (isSelected) DarkText else Color.Transparent,
                                        CircleShape
                                    )
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (selectedTab) {
                0 -> BrickHomeScreen()
                1 -> RoutinesScreen()
                2 -> ActivityScreen()
                3 -> SettingsScreen()
            }
        }
    }
}

@Composable
fun BrickHomeScreen() {
    val context = LocalContext.current
    var isBricked by remember { mutableStateOf(false) }
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (pressed) 0.93f else 1f, label = "scale")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top NFC Banner
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardColor),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = AccentRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("NFC is turned off", fontWeight = FontWeight.Bold, color = DarkText, fontSize = 15.sp)
                    Text("Turn on NFC in your settings to use Brick.", color = SubtitleColor, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { context.startActivity(Intent(Settings.ACTION_NFC_SETTINGS)) },
                        colors = ButtonDefaults.buttonColors(containerColor = LightPill),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Turn on NFC", color = DarkText, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // 3D Brick Hardware Mockup
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(210.dp)
                .scale(scale)
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(48.dp), spotColor = Color(0x33000000))
                .clip(RoundedCornerShape(48.dp))
                .background(Color(0xFFE8E4E0))
                .border(2.dp, Color(0xFFF7F5F3), RoundedCornerShape(48.dp))
                .clickable {
                    pressed = !pressed
                    isBricked = !isBricked
                }
        ) {
            Text(
                text = "Brick",
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFA5A19D)
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = if (isBricked) "Active Lock" else "No Socials",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (isBricked) "Apps are blocked" else "Blocks 1 app",
            fontSize = 15.sp,
            color = SubtitleColor
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Manage modes ›",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkText,
            modifier = Modifier.clickable { }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Bottom CTA Button
        Button(
            onClick = { isBricked = !isBricked },
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2DDD8)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(1.dp, Color(0xFFD6D0CA), RoundedCornerShape(32.dp))
        ) {
            Text(
                text = if (isBricked) "Tap or hold to Unbrick" else "Tap or hold to Brick",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RoutinesScreen() {
    var selectedRoutineTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Routines", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkText)
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("SCHEDULES", "SPACES").forEachIndexed { idx, tab ->
                val active = selectedRoutineTab == idx
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedRoutineTab = idx },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        tab,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (active) DarkText else SubtitleColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(if (active) DarkText else Color.Transparent)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Deep Work", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkText)
                        Text("9:00am · Weekdays", color = SubtitleColor, fontSize = 13.sp)
                    }
                    var checked by remember { mutableStateOf(true) }
                    Switch(checked = checked, onCheckedChange = { checked = it })
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
        Text(
            "Blocking that works anywhere",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = DarkText,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Schedules automate blocking wherever you are, until a set time or a tap of your Brick.",
            fontSize = 14.sp,
            color = SubtitleColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { },
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkText),
            modifier = Modifier.height(48.dp)
        ) {
            Text("Add schedule", color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ActivityScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Weekly Activity ˇ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkText)
        Text("THIS WEEK", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SubtitleColor)

        Spacer(modifier = Modifier.height(32.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Avg Brick Time", color = SubtitleColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(DarkText, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Your first day is underway. Check back for your average.",
                color = SubtitleColor,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Account Item
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Account", fontWeight = FontWeight.Bold, color = DarkText, fontSize = 15.sp)
                    Text("Signed in as your.email@gmail.com", color = SubtitleColor, fontSize = 13.sp)
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SubtitleColor)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Menu items
        val settingsList = listOf(
            "My Brick Rules",
            "Emergency UnBrick",
            "Devices",
            "Notifications",
            "Background Access",
            "Help Center",
            "Contact Us"
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                settingsList.forEachIndexed { idx, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(horizontal = 16.dp, vertical = 18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DarkText)
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SubtitleColor)
                    }
                    if (idx < settingsList.lastIndex) {
                        HorizontalDivider(color = BgColor, thickness = 1.dp)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}
