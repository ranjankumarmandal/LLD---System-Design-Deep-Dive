import java.util.HashMap;
import java.util.Map;

class RateLimiter {
    private int maxRequests;
    private long windowSizeMs;
    private Map<String, WindowInfo> userRequestMap;

    private static class WindowInfo {
        int count;
        long windowEnd;

        WindowInfo(int count, long windowEnd) {
            this.count = count;
            this.windowEnd = windowEnd;
        }
    }

    public RateLimiter(int maxRequests, long windowSizeMs) {
        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeMs;
        this.userRequestMap = new HashMap<>();
    }

    public boolean allowRequest(String clientId) {
        long now = System.currentTimeMillis();
        WindowInfo info = userRequestMap.getOrDefault(clientId, new WindowInfo(0, now + windowSizeMs));

        if (now > info.windowEnd) {
            info.count = 1;
            info.windowEnd = now + windowSizeMs;
            userRequestMap.put(clientId, info);
            return true;
        } else {
            if (info.count < maxRequests) {
                info.count++;
                userRequestMap.put(clientId, info);
                return true;
            } else {
                return false;
            }
        }
    }
}