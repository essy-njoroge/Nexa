package com.essy.nexa.model

data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val date: String = "",
    val time: String = "",
    val category: String = "",
    val attendees: Int = 0,
    val maxAttendees: Int = 0,
    val organizerName: String = "",
    val isRsvped: Boolean = false
)

data class Post(
    val id: String = "",
    val authorName: String = "",
    val authorCourse: String = "",
    val content: String = "",
    val timestamp: String = "",
    val likes: Int = 0,
    val comments: Int = 0,
    val isLiked: Boolean = false,
    val category: String = ""
)

data class ChatPreview(
    val id: String = "",
    val name: String = "",
    val lastMessage: String = "",
    val timestamp: String = "",
    val unreadCount: Int = 0,
    val isGroup: Boolean = false
)

data class Message(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val content: String = "",
    val timestamp: String = ""
)

data class Club(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val members: Int = 0,
    val isJoined: Boolean = false,
    val iconEmoji: String = ""
)

data class Notification(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val timestamp: String = "",
    val type: String = "",
    val isRead: Boolean = false
)

data class Opportunity(
    val id: String = "",
    val title: String = "",
    val company: String = "",
    val type: String = "",
    val location: String = "",
    val deadline: String = "",
    val skills: List<String> = emptyList(),
    val description: String = ""
)

data class StudyGroup(
    val id: String = "",
    val course: String = "",
    val topic: String = "",
    val members: List<String> = emptyList(),
    val maxMembers: Int = 5,
    val schedule: String = ""
)
