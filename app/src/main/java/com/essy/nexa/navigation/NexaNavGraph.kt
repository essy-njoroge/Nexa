package com.essy.nexa.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.essy.nexa.ui.screens.ai.AiAssistantScreen
import com.essy.nexa.ui.screens.auth.*
import com.essy.nexa.ui.screens.chat.*
import com.essy.nexa.ui.screens.clubs.*
import com.essy.nexa.ui.screens.events.*
import com.essy.nexa.ui.screens.home.HomeScreen
import com.essy.nexa.ui.screens.leaderboard.LeaderboardScreen
import com.essy.nexa.ui.screens.lostfound.LostFoundScreen
import com.essy.nexa.ui.screens.notifications.NotificationsScreen
import com.essy.nexa.ui.screens.onboarding.OnboardingScreen
import com.essy.nexa.ui.screens.opportunities.OpportunitiesScreen
import com.essy.nexa.ui.screens.post.CreatePostScreen
import com.essy.nexa.ui.screens.profile.*
import com.essy.nexa.ui.screens.splash.SplashScreen
import com.essy.nexa.ui.screens.study.StudyGroupScreen
import com.essy.nexa.ui.screens.timetable.TimetableScreen

@Composable
fun NexaNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH)        { SplashScreen(navController) }
        composable(Routes.ONBOARDING)    { OnboardingScreen(navController) }
        composable(Routes.LOGIN)         { LoginScreen(navController) }
        composable(Routes.REGISTER)      { RegisterScreen(navController) }
        composable(Routes.INTERESTS)     { InterestSelectionScreen(navController) }

        composable(Routes.HOME)          { HomeScreen(navController) }
        composable(Routes.EVENTS)        { EventsScreen(navController) }
        composable(Routes.EVENT_DETAIL)  { EventDetailScreen(navController) }
        composable(Routes.QR_CHECKIN)    { QrCheckInScreen(navController) }
        composable(Routes.CHECKIN_DONE)  { CheckInDoneScreen(navController) }

        composable(Routes.CHAT)          { ChatScreen(navController) }
        composable(Routes.CONVERSATION)  { ConversationScreen(navController) }

        composable(Routes.CLUBS)         { ClubsScreen(navController) }
        composable(Routes.CLUB_DETAIL)   { ClubDetailScreen(navController) }

        composable(Routes.PROFILE)       { ProfileScreen(navController) }
        composable(Routes.EDIT_PROFILE)  { EditProfileScreen(navController) }

        composable(Routes.NOTIFICATIONS) { NotificationsScreen(navController) }
        composable(Routes.OPPORTUNITIES) { OpportunitiesScreen(navController) }
        composable(Routes.AI_ASSISTANT)  { AiAssistantScreen(navController) }
        composable(Routes.CREATE_POST)   { CreatePostScreen(navController) }
        composable(Routes.STUDY_GROUP)   { StudyGroupScreen(navController) }
        composable(Routes.LOST_FOUND)    { LostFoundScreen(navController) }
        composable(Routes.TIMETABLE)     { TimetableScreen(navController) }
        composable(Routes.LEADERBOARD)   { LeaderboardScreen(navController) }
    }
}
