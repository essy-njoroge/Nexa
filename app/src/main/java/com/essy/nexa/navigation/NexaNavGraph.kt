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
import com.essy.nexa.ui.screens.feed.CreatePostScreen
import com.essy.nexa.ui.screens.home.HomeScreen
import com.essy.nexa.ui.screens.leaderboard.LeaderboardScreen
import com.essy.nexa.ui.screens.lostfound.LostFoundScreen
import com.essy.nexa.ui.screens.notifications.NotificationsScreen
import com.essy.nexa.ui.screens.onboarding.OnboardingScreen
import com.essy.nexa.ui.screens.opportunities.CreateOpportunityScreen
import com.essy.nexa.ui.screens.opportunities.OpportunitiesScreen
import com.essy.nexa.ui.screens.profile.*
import com.essy.nexa.ui.screens.splash.SplashScreen
import com.essy.nexa.ui.screens.study.StudyGroupDetailScreen
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
        composable("event_detail/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            EventDetailScreen(navController, eventId)
        }
        composable("qr_checkin/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            QrCheckinScreen(navController, eventId)
        }
        composable("checkin_done/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            CheckInDoneScreen(navController, eventId)
        }
        composable(Routes.CHAT)          { ChatScreen(navController) }
        composable("conversation/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")
            ConversationScreen(navController, chatId)
        }
        composable(Routes.CLUBS)         { ClubsScreen(navController) }
        composable("club_detail/{clubId}") { backStackEntry ->
            val clubId = backStackEntry.arguments?.getString("clubId")
            ClubDetailScreen(navController, clubId)
        }
        composable(Routes.PROFILE)       { ProfileScreen(navController) }
        composable(Routes.EDIT_PROFILE)  { EditProfileScreen(navController) }

        composable(Routes.NOTIFICATIONS) { NotificationsScreen(navController) }
        composable(Routes.OPPORTUNITIES) { OpportunitiesScreen(navController) }
        composable("create_opportunity") {
            CreateOpportunityScreen(navController)
        }
        composable(Routes.AI_ASSISTANT)  { AiAssistantScreen(navController) }
        composable(Routes.CREATE_POST)   { CreatePostScreen(navController) }
        composable(Routes.STUDY_GROUP)   { StudyGroupScreen(navController) }
        composable(
            route = "study_group_detail/{groupId}"
        ) { backStackEntry ->

            val groupId =
                backStackEntry.arguments?.getString("groupId") ?: "1"

            StudyGroupDetailScreen(
                navController = navController,
                groupId = groupId
            )
        }
        composable(Routes.LOST_FOUND)    { LostFoundScreen(navController) }
        composable(Routes.TIMETABLE)     { TimetableScreen(navController) }
        composable(Routes.LEADERBOARD)   { LeaderboardScreen(navController) }
        composable(Routes.CREATE_EVENT)   { CreateEventScreen(navController) }
    }
}

@Composable
fun QrCheckinScreen(x0: NavHostController, x1: String?) {
    TODO("Not yet implemented")
}
