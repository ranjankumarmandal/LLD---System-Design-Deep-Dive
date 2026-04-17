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

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }
}

class NotificationService {
    private List<NotificationChannel> channels;

    public NotificationService() {
        channels = new ArrayList<>();
    }

    public void registerChannel(NotificationChannel channel) {
        channels.add(channel);
    }

    public void send(User user, Notification notification) {
        for (NotificationChannel channel : channels) {
            channel.sendNotification(user, notification);
        }
    }
}

public class Main {
    public static void main(String[] args) {
    }
}