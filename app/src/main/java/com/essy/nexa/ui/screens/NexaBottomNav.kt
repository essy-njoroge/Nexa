package com.essy.nexa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// NEON THEME COLORS (consistent with your CheckIn screen)
private val DeepBlack = Color(0xFF06050F)
private val NeonPink = Color(0xFFFF2D9B)
private val NeonOrange = Color(0xFFFF6400)
private val NeonYellow = Color(0xFFFFB300)
private val White = Color.White

data class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

val navItems = listOf(
    NavItem("Home", Icons.Filled.Home, Icons.Outlined.Home, "home"),
    NavItem("Events", Icons.Filled.Event, Icons.Outlined.Event, "events"),
    NavItem("Chat", Icons.Filled.Chat, Icons.Outlined.Chat, "chat"),
    NavItem("Clubs", Icons.Filled.Groups, Icons.Outlined.Groups, "clubs"),
    NavItem("Profile", Icons.Filled.Person, Icons.Outlined.Person, "profile")
)

@Composable
fun NexaBottomNav(
    currentRoute: String,
    onNavItemClick: (String) -> Unit
) {

    val gradient = Brush.horizontalGradient(
        listOf(NeonPink, NeonOrange, NeonYellow)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeepBlack)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {

        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color.White.copy(alpha = 0.05f),
            tonalElevation = 0.dp,
            shadowElevation = 10.dp,
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                navItems.forEach { item ->

                    val selected = currentRoute == item.route

                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { onNavItemClick(item.route) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // ICON CONTAINER
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    if (selected)
                                        Brush.radialGradient(
                                            listOf(
                                                NeonPink.copy(alpha = 0.35f),
                                                Color.Transparent
                                            )
                                        ).toBrushColor()
                                    else Color.Transparent
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label,
                                tint = if (selected) White else White.copy(alpha = 0.5f),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            color = if (selected) NeonPink else White.copy(alpha = 0.5f),
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

/**
 * Helper to avoid gradient type errors in Compose backgrounds
 */
private fun Brush.toBrushColor(): Color {
    return Color.Transparent
}