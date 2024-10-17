package app.page;

import app.page_elements.Notification;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class NotificationsInbox {
    private final ArrayList<Notification> notifications;

    public NotificationsInbox() {
        notifications = new ArrayList<>();
    }

    /**
     * Adds a new notification to the inbox
     * @param notification the new notification to be added
     */
    public void addNotification(final Notification notification) {
        notifications.add(notification);
    }

    /**
     * Clears all the notifications from the inbox
     */
    public void clearInbox() {
        notifications.clear();
    }
}
