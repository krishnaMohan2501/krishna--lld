package com.mc.lld.socialntw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

// Main Service Class
public class SocialNtwService {

    // Thread-safe collections for storing users, posts, and notifications
    private final Map<String, User> users;
    private final Map<String, Post> posts;
    private final Map<String, CopyOnWriteArrayList<Notification>> notifications;

    private static volatile SocialNtwService INSTANCE;

    // ReentrantLock for critical sections involving multiple shared resources
    private final ReentrantLock lock = new ReentrantLock();

    private SocialNtwService() {
        users = new ConcurrentHashMap<>();
        posts = new ConcurrentHashMap<>();
        notifications = new ConcurrentHashMap<>();
    }

    public static SocialNtwService getSocialNtwService() {
        if (INSTANCE == null) {
            synchronized (SocialNtwService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SocialNtwService();
                }
            }
        }
        return INSTANCE;
    }

    // Register a new user
    public void registerUser(User user) {
        users.putIfAbsent(user.getId(), user);
    }

    // Login a user by email and password
    public User loginUser(String email, String password) {
        return users.values()
                .stream()
                .filter(user -> user.getEmail().equals(email) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    // Update user profile
    public void updateUserProfile(User user) {
        users.put(user.getId(), user);
    }

    // Send a friend request
    public void sendFriendRequest(String senderId, String receiverId) {
        lock.lock();
        try {
            User receiver = users.get(receiverId);
            if (receiver != null) {
                Notification notification = new Notification(
                        receiverId,
                        NotificationType.FRIEND_REQUEST,
                        "Friend request from " + senderId
                );
                addNotification(receiverId, notification);
            }
        } finally {
            lock.unlock();
        }
    }

    // Accept a friend request
    public void acceptFriendRequest(String userId, String friendId) {
        lock.lock();
        try {
            User user = users.get(userId);
            User friend = users.get(friendId);
            if (user != null && friend != null) {
                user.getFriends().add(friendId);
                friend.getFriends().add(userId);
                Notification notification = new Notification(
                        friendId,
                        NotificationType.FRIEND_REQUEST_ACCEPTED,
                        "Friend request accepted by " + userId
                );
                addNotification(friendId, notification);
            }
        } finally {
            lock.unlock();
        }
    }

    // Create a new post
    public void createPost(Post post) {
        lock.lock();
        try {
            posts.put(post.getId(), post);
            User user = users.get(post.getUserId());
            if (user != null) {
                user.getPosts().add(post.getId());
            }
        } finally {
            lock.unlock();
        }
    }

    // Comment on a post
    public void commentOnPost(Comment comment) {
        lock.lock();
        try {
            Post post = posts.get(comment.getPostId());
            if (post != null) {
                post.getComments().add(comment);
                Notification notification = new Notification(
                        post.getUserId(),
                        NotificationType.COMMENT,
                        "Your post received a comment from " + comment.getUserId()
                );
                addNotification(post.getUserId(), notification);
            }
        } finally {
            lock.unlock();
        }
    }

    // Like a post
    public void likePost(String userId, String postId) {
        lock.lock();
        try {
            Post post = posts.get(postId);
            if (post != null && !post.getLikes().contains(userId)) {
                post.getLikes().add(userId);
                Notification notification = new Notification(
                        post.getUserId(),
                        NotificationType.LIKE,
                        "Your post was liked by " + userId
                );
                addNotification(post.getUserId(), notification);
            }
        } finally {
            lock.unlock();
        }
    }

    // Fetch a user's newsfeed
    public List<Post> getNewsfeed(String userId) {
        User user = users.get(userId);
        if (user == null) return Collections.emptyList();

        List<Post> newsfeed = new ArrayList<>();
        List<String> friendIds = user.getFriends();

        for (String friendId : friendIds) {
            User friend = users.get(friendId);
            if (friend != null) {
                friend.getPosts().forEach(postId -> {
                    Post post = posts.get(postId);
                    if (post != null) newsfeed.add(post);
                });
            }
        }

        user.getPosts().forEach(postId -> {
            Post post = posts.get(postId);
            if (post != null) newsfeed.add(post);
        });

        newsfeed.sort(Comparator.comparing(Post::getTimestamp).reversed());
        return newsfeed;
    }

    // Paginate the newsfeed
    public List<Post> getNewsfeedPaginated(String userId, int pageNumber, int pageSize) {
        List<Post> newsfeed = getNewsfeed(userId);
        int start = pageNumber * pageSize;
        int end = Math.min(start + pageSize, newsfeed.size());
        return start < newsfeed.size() ? newsfeed.subList(start, end) : Collections.emptyList();
    }

    // Add a notification for a user
    private void addNotification(String userId, Notification notification) {
        notifications.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(notification);
    }

    // Get notifications for a user
    public List<Notification> getNotifications(String userId) {
        return notifications.getOrDefault(userId, new CopyOnWriteArrayList<>());
    }

    // Method to follow another user
    public void followUser(String followerId, String followeeId) {
        lock.lock();
        try {
            User follower = users.get(followerId);
            User followee = users.get(followeeId);

            if (follower == null || followee == null) {
                throw new IllegalArgumentException("Invalid user IDs provided.");
            }

            if (!follower.getFollowing().contains(followeeId)) {
                follower.getFollowing().add(followeeId);
                followee.getFollowers().add(followerId);

                // Notify the followee
                Notification notification = new Notification(
                        followeeId,
                        NotificationType.FOLLOW,
                        followerId + " has started following you."
                );
                addNotification(followeeId, notification);
            }
        } finally {
            lock.unlock();
        }
    }

    // Method to unfollow another user
    public void unfollowUser(String followerId, String followeeId) {
        lock.lock();
        try {
            User follower = users.get(followerId);
            User followee = users.get(followeeId);

            if (follower == null || followee == null) {
                throw new IllegalArgumentException("Invalid user IDs provided.");
            }

            if (follower.getFollowing().contains(followeeId)) {
                follower.getFollowing().remove(followeeId);
                followee.getFollowers().remove(followerId);
            }
        } finally {
            lock.unlock();
        }
    }

}
