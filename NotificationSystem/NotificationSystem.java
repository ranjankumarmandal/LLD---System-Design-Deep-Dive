import java.util.*;

class Notification {
    private String content;

    public Notification(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

interface NotificationChannel {
    void sendNotification(User user, Notification notification);
}

class EmailNotificationChannel implements NotificationChannel {
    public void sendNotification(User user, Notification notification) {
        System.out.println("Email sent to " + user.getEmail() + ": " + notification.getContent());
    }
}

class SMSNotificationChannel implements NotificationChannel {
    public void sendNotification(User user, Notification notification) {
        System.out.println("SMS sent to " + user.getPhone() + ": " + notification.getContent());
    }
}

class User {
    private String name;
    private String email;
    private String phone;
}