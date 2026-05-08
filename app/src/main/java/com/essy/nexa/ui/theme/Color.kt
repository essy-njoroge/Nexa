package com.essy.nexa.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

// Nexa Brand — Teal & White
val NexaPrimary     = Color(0xFF0D9488)   // Teal
val NexaDark        = Color(0xFF0A7568)   // Darker teal
val NexaLight       = Color(0xFFE0F2F1)   // Very light teal bg
val NexaAccent      = Color(0xFFFF6584)   // Coral accent
val NexaWarning     = Color(0xFFFFB347)   // Amber

val NexaWhite       = Color(0xFFFFFFFF)
val NexaCardBg      = Color(0xFFFFFFFF)
val NexaBackground  = Color(0xFF0D9488)

val NexaTextDark    = Color(0xFF1A1A2E)
val NexaTextGrey    = Color(0xFF6B7280)
val NexaTextWhite   = Color(0xFFFFFFFF)
val NexaTextWhite80 = Color(0xCCFFFFFF)

val NexaDivider     = Color(0xFFE5E7EB)
val NexaTagBg       = Color(0xFFE0F2F1)
val NexaTagText     = Color(0xFF0D9488)
val NexaSecondary = Color(0xFF4CAF50) // nice green accent




// ─── BASE THEME ─────────────────────────────────────────
val DeepMidnight = Color(0xFF06050F)
val DarkSurface   = Color(0xFF0D0918)

// ─── NEON ACCENTS ───────────────────────────────────────
val HotPink      = Color(0xFFFF2D9B)
val BlazeOrange  = Color(0xFFFF6400)
val GoldYellow   = Color(0xFFFFB300)
val VioletDeep   = Color(0xFF7B2FFF)

// ─── NEUTRALS ───────────────────────────────────────────
val White        = Color.White

// ─── GRADIENTS ──────────────────────────────────────────
val FireGradient = Brush.linearGradient(
    listOf(HotPink, BlazeOrange, GoldYellow)
)

val PinkVioletGradient = Brush.linearGradient(
    listOf(HotPink, VioletDeep)
)

val NeonGlowGradient = Brush.radialGradient(
    listOf(
        HotPink.copy(alpha = 0.35f),
        VioletDeep.copy(alpha = 0.20f),
        Color.Transparent
    )
)

// ─── CHAT BACKGROUND HELPERS ───────────────────────────
val GridColor = White.copy(alpha = 0.04f)
val SurfaceGlass = White.copy(alpha = 0.05f)
val SurfaceStroke = White.copy(alpha = 0.08f)
val SoftText = White.copy(alpha = 0.55f)
val MutedText = White.copy(alpha = 0.35f)