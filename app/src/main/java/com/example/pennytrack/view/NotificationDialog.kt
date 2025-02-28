package com.example.pennytrack.view


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.pennytrack.data.models.Notification
import com.example.pennytrack.ui.theme.md_theme_light_onPrimary
import com.example.pennytrack.ui.theme.md_theme_light_onSurface
import com.example.pennytrack.ui.theme.md_theme_light_onSurfaceVariant
import com.example.pennytrack.ui.theme.md_theme_light_primary
import com.example.pennytrack.ui.theme.md_theme_light_primaryContainer
import com.example.pennytrack.ui.theme.md_theme_light_surface
import com.example.pennytrack.ui.theme.md_theme_light_surfaceVariant
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NotificationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    notifications: List<Notification>,
    onDeleteNotification: (String) -> Unit,
    onMarkAsRead: (String) -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(dismissOnClickOutside = true)
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Notifications",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                    if (notifications.isEmpty()) {
                        Text(
                            text = "No new notifications",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        notifications.forEach { notification ->
                            NotificationItem(
                                notification = notification,
                                onDelete = { onDeleteNotification(notification.id) },
                                onMarkAsRead = { onMarkAsRead(notification.id) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: Notification,
    onDelete: () -> Unit,
    onMarkAsRead: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(notification.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}