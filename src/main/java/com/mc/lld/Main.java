package com.mc.lld;

import com.mc.lld.cab2.*;
import com.mc.lld.socialntw.*;
//import com.mc.lld.splitwise.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Example: Initializing Tic-Tac-Toe game
//        List<Player> players = Arrays.asList(new Player("Player 1", 1, "X"), new Player("Player 2", 2, "O"));
//        Scanner scanner = new Scanner(System.in);

        // Choose the game to play: "tictactoe" or "chess"
//        String gameType = "tictactoe"; // Can be dynamically decided at runtime
//        Board board = new TicTacToeBoard(); // or ChessBoard for chess

        // Create and start the game manager
//        GameManager gameManager = new GameManager(gameType, board, players, scanner);
//        gameManager.startGame();
//        SprintService sprintService = SprintService.getInstance();

//        System.out.println("======Creating users.==========");
//        User user1 = new User("krishna");
//        User user2 = new User("Divya");
//        User user3 = new User("Chinky");
//        User user4 = new User("Neeraj");
//        User user5 = new User("Alisha");
//
//        System.out.println("======User creation done.==========");
//
//        System.out.println("creating sprint");
//        Sprint sprint = sprintService.createSprint("Sprint-1", LocalDateTime.now().minusDays(4), LocalDateTime.now().plusDays(10), user1);
//
//        System.out.println("creating sprint done");
//
//        System.out.println("creating task");
//        Task task1 = sprintService.createTask(TaskType.BUG, user1, user2);
//        Task task2 = sprintService.createTask(TaskType.STORY, user1, user3);
//        Task task3 = sprintService.createTask(TaskType.FEATURE, user1, user3);
//        Task task4 = sprintService.createTask(TaskType.FEATURE, user1, user3);
//
//
//        System.out.println("creating task done.");
//        sprintService.addTaskToSprint(task1, sprint);
//        sprintService.addTaskToSprint(task2, sprint);
//        sprintService.addTaskToSprint(task3, sprint);
//        System.out.println("adding task to sprint");
//
//        System.out.println("Starting task");
//        sprintService.startTask(task1.getTaskId(), LocalDateTime.now());
//        sprintService.startTask(task2.getTaskId(), LocalDateTime.now().minusDays(1));
//        sprintService.startTask(task3.getTaskId(), LocalDateTime.now().minusDays(2));
//        sprintService.startTask(task3.getTaskId(), LocalDateTime.now().plusDays(1));
//
//        System.out.println("Starting task Done.");
//
//        sprintService.changeTaskStatus(task2.getTaskId(), Status.IN_PROGESS);
//        sprintService.changeTaskStatus(task4.getTaskId(), Status.IN_PROGESS);
////        sprintService.changeTaskStatus(task3.getTaskId(), Status.IN_PROGESS);
//
//        sprintService.getDelayedTask(sprint.getId());


//        AdminService adminService = AdminService.getInstance();
//
//        Station station1 = adminService.createStation("Station1", 12.9716, 77.5946);
//        Station station2 = adminService.createStation("Station2", 13.0827, 80.2707);
//
//        Car car1 = new Car(VehicleType.SUV, 11);
//        Car car2 = new Car(VehicleType.SEDAN, 12);
//        Car car3 = new Car(VehicleType.BIKE, 5);
//        Car car4 = new Car(VehicleType.SUV, 10);
//        Car car5 = new Car(VehicleType.HATCHBACK, 8);
//
//
//        adminService.onboardVehicleToStation(station1.getStationId(), Arrays.asList(
//                car1,
//                car2,
//                car3
//        ));
//
//        adminService.onboardVehicleToStation(station2.getStationId(), Arrays.asList(
//                car4,
//                car5
//        ));
//
//
//        VehicleRentalService vehicleRentalService = VehicleRentalService.getInstance();
//        vehicleRentalService.generateStationReport(station2.getStationId());
//
//        System.out.println("Test");
//        vehicleRentalService.bookVehicle(station1, car1.getVehicleId());
//        Map<Station, Map<VehicleType, List<String>>> stationListMap = vehicleRentalService.searchVehicle(VehicleType.SUV, 13.0, 77.0);
//        vehicleRentalService.dropVehicle(car1.getVehicleId(), station1.getStationId());


//        SplitwiseService splitwiseService = SplitwiseService.getInstance();
//
//        // Create users
//        User user1 = new User("Alice", "alice@example.com");
//        User user2 = new User("Bob", "bob@example.com");
//        User user3 = new User("Charlie", "charlie@example.com");
//
//        splitwiseService.addUser(user1);
//        splitwiseService.addUser(user2);
//        splitwiseService.addUser(user3);
//
//        // Create a group
//        Group group = new Group("Apartment");
//        group.addMember(user1);
//        group.addMember(user2);
//        group.addMember(user3);
//
//        splitwiseService.addGroup(group);
//
//        // Add an expense
//        Expense expense = new Expense(300.0, "Rent", user1);
//
//        EqualSplit equalSplit1 = new EqualSplit(user1);
//        EqualSplit equalSplit2 = new EqualSplit(user2);
//        PercentageSplit percentSplit = new PercentageSplit(user3, 20.0);
//
//        expense.addSplit(equalSplit1);
//        expense.addSplit(equalSplit2);
//        expense.addSplit(percentSplit);
//
//        splitwiseService.addExpense(group.getId(), expense);
//
//        // Settle balances
//        splitwiseService.settleBalance(user1.getId(), user2.getId());
//        splitwiseService.settleBalance(user1.getId(), user3.getId());
//
//        // Print user balances
//        for (User user : Arrays.asList(user1, user2, user3)) {
//            System.out.println("User: " + user.getName());
//            for (Map.Entry<String, Double> entry : user.getBalances().entrySet()) {
//                System.out.println("  Balance with " + entry.getKey() + ": " + entry.getValue());
//            }
//        }



        SocialNtwService socialNetworkingService = SocialNtwService.getSocialNtwService();

        // User registration
        User user1 = new User("John Doe", "john@example.com", "password", "profile1.jpg", "I love coding!");
        User user2 = new User("Jane Smith", "jane@example.com", "password", "profile2.jpg", "Exploring the world!");
        socialNetworkingService.registerUser(user1);
        socialNetworkingService.registerUser(user2);

        // User login
        User loggedInUser = socialNetworkingService.loginUser("john@example.com", "password");
        if (loggedInUser != null) {
            System.out.println("User logged in: " + loggedInUser.getName());
        } else {
            System.out.println("Invalid email or password.");
        }

        // Send friend request
        socialNetworkingService.sendFriendRequest(user1.getId(), user2.getId());

        // Accept friend request
        socialNetworkingService.acceptFriendRequest(user2.getId(), user1.getId());

        // Create posts
        Post post1 = new Post(user1.getId(), "My first post!");
        Post post2 = new Post(user2.getId(), "Having a great day!");
        socialNetworkingService.createPost(post1);
        socialNetworkingService.createPost(post2);

        // Like a post
        socialNetworkingService.likePost(user2.getId(), post1.getId());

        // Comment on a post
        Comment comment = new Comment(user2.getId(), post1.getId(), "Great post!");
        socialNetworkingService.commentOnPost(comment);

        // Get newsfeed
        List<Post> newsfeed = socialNetworkingService.getNewsfeed(user1.getId());
        System.out.println("Newsfeed:");
        for (Post post : newsfeed) {
            System.out.println("Post: " + post.getContent());
            System.out.println("Likes: " + post.getLikes().size());
            System.out.println("Comments: " + post.getComments().size());
            System.out.println();
        }

        // Get notifications
        List<Notification> notifications = socialNetworkingService.getNotifications(user1.getId());
        System.out.println("Notifications:");
        for (Notification notification : notifications) {
            System.out.println("Type: " + notification.getType());
            System.out.println("Content: " + notification.getContent());
            System.out.println();
        }
    }

}
