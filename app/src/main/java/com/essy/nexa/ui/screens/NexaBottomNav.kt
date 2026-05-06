package com.essy.nexa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.essy.nexa.ui.theme.*

data class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

val navItems = listOf(
    NavItem("Home",    Icons.Filled.Home,    Icons.Outlined.Home,    "home"),
    NavItem("Events",  Icons.Filled.Event,   Icons.Outlined.Event,   "events"),
    NavItem("Chat",    Icons.Filled.Chat,    Icons.Outlined.Chat,    "chat"),
    NavItem("Clubs",   Icons.Filled.Groups,  Icons.Outlined.Groups,  "clubs"),
    NavItem("Profile", Icons.Filled.Person,  Icons.Outlined.Person,  "profile")
)

@Composable
fun NexaBottomNav(currentRoute: String, onNavItemClick: (String) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        navItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavItemClick(item.route) },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NexaPrimary,
                    selectedTextColor = NexaPrimary,
                    unselectedIconColor = NexaTextGrey,
                    unselectedTextColor = NexaTextGrey,
                    indicatorColor = NexaTagBg
                )
            )
        }
    }
}
