package com.essy.nexa.navigation

object Routes {
    const val SPLASH        = "splash"
    const val ONBOARDING    = "onboarding"
    const val LOGIN         = "login"
    const val REGISTER      = "register"
    const val INTERESTS     = "interests"
    const val HOME          = "home"
    const val EVENTS        = "events"
    const val EVENT_DETAIL  = "event_detail"
    const val QR_CHECKIN    = "qr_checkin"
    const val CHECKIN_DONE  = "checkin_done"
    const val CHAT          = "chat"
    const val CONVERSATION  = "conversation"
    const val CLUBS         = "clubs"
    const val CLUB_DETAIL   = "club_detail"
    const val PROFILE       = "profile"
    const val EDIT_PROFILE  = "edit_profile"
    const val NOTIFICATIONS = "notifications"
    const val OPPORTUNITIES = "opportunities"
    const val AI_ASSISTANT  = "ai_assistant"
    const val CREATE_POST   = "create_post"
    const val STUDY_GROUP   = "study_group"
    const val LOST_FOUND    = "lost_found"
    const val TIMETABLE     = "timetable"
    const val LEADERBOARD   = "leaderboard"
}

val bottomNavRoutes = listOf(
    Routes.HOME, Routes.EVENTS, Routes.CHAT, Routes.CLUBS, Routes.PROFILE
)
