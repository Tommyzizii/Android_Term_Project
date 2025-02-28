package com.example.pennytrack.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pennytrack.data.models.Notification
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class NotificationViewModel : ViewModel() {
    private val _notifications = MutableLiveData<List<Notification>>(emptyList())
    val notifications: LiveData<List<Notification>> get() = _notifications

    private val firestore = FirebaseFirestore.getInstance()
    private var notificationsListener: ListenerRegistration? = null

    // Fetch notifications from Firestore
    fun fetchNotifications(userId: String) {
        notificationsListener = firestore.collection("users")
            .document(userId)
            .collection("notifications") // Correct path: users/{userId}/notifications
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                val notificationsList = snapshot?.documents?.map { doc ->
                    Notification(
                        id = doc.id,
                        message = doc.getString("message") ?: "",
                        timestamp = doc.getLong("timestamp") ?: 0L,
                        isRead = doc.getBoolean("isRead") ?: false
                    )
                } ?: emptyList()

                _notifications.value = notificationsList
            }
    }

    // Add a new notification to Firestore
    fun addNotification(userId: String, message: String) {
        viewModelScope.launch {
            val notification = Notification(
                id = UUID.randomUUID().toString(), // Generate a unique ID for the notification
                message = message
            )

            // Correct Firestore path: users/{userId}/notifications/{notificationId}
            firestore.collection("users")
                .document(userId)
                .collection("notifications")
                .document(notification.id) // Add the notification ID as the document ID
                .set(notification)
                .await()
        }
    }

    // Mark a notification as read
    fun markAsRead(userId: String, notificationId: String) {
        viewModelScope.launch {
            firestore.collection("users")
                .document(userId)
                .collection("notifications")
                .document(notificationId)
                .update("isRead", true)
                .await()
        }
    }

    // Delete a notification
    fun deleteNotification(userId: String, notificationId: String) {
        viewModelScope.launch {
            firestore.collection("users")
                .document(userId)
                .collection("notifications")
                .document(notificationId)
                .delete()
                .await()
        }
    }

    // Clean up the Firestore listener
    override fun onCleared() {
        super.onCleared()
        notificationsListener?.remove()
    }
}