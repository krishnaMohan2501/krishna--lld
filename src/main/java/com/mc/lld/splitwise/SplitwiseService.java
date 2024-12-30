package com.mc.lld.splitwise;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SplitwiseService {

    private final Map<String, User> users;
    private final Map<String, Group> groups;

    private static final String TRANSACTION_ID_PREFIX = "TXN";
    private static final AtomicInteger transactionCounter = new AtomicInteger(0);

    private static volatile SplitwiseService INSTANCE;

    private SplitwiseService() {
        users = new ConcurrentHashMap<>();
        groups = new ConcurrentHashMap<>();
    }

    public static SplitwiseService getInstance() {
        if (INSTANCE == null) {
            synchronized (SplitwiseService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SplitwiseService();
                }
            }
        }
        return INSTANCE;
    }

    public void addUser(User user) {
        users.putIfAbsent(user.getId(), user);
    }

    public void addGroup(Group group) {
        groups.putIfAbsent(group.getId(), group);
    }

    public void addExpense(String groupId, Expense expense) {
        Group group = groups.get(groupId);
        if (group != null) {
            synchronized (group) {
                group.addExpense(expense);
            }
            splitExpense(expense);
            updateBalances(expense);
        }
    }

    private void splitExpense(Expense expense) {
        double totalAmount = expense.getAmount();
        List<Split> splits = expense.getSplits();
        int totalSplits = splits.size();

        double splitAmount = totalAmount / totalSplits;
        for (Split split : splits) {
            if (split instanceof EqualSplit) {
                split.setAmount(splitAmount);
            } else if (split instanceof PercentageSplit) {
                PercentageSplit percentSplit = (PercentageSplit) split;
                split.setAmount(totalAmount * percentSplit.getPercent() / 100.0);
            }
        }
    }

    private void updateBalances(Expense expense) {
        User paidBy = expense.getPaidBy();
        for (Split split : expense.getSplits()) {
            User user = split.getUser();
            double amount = split.getAmount();

            if (!paidBy.equals(user)) {
                updateBalance(paidBy, user, amount);
                updateBalance(user, paidBy, -amount);
            }
        }
    }

    private void updateBalance(User user1, User user2, double amount) {
        String key = getBalanceKey(user1, user2);
        user1.getBalances().put(key, user1.getBalances().getOrDefault(key, 0.0) + amount);
    }

    private String getBalanceKey(User user1, User user2) {
        return user1.getId() + ":" + user2.getId();
    }

    public void settleBalance(String userId1, String userId2) {
        User user1 = users.get(userId1);
        User user2 = users.get(userId2);

        if (user1 != null && user2 != null) {
            synchronized (getBalanceKey(user1, user2).intern()) {
                String key = getBalanceKey(user1, user2);
                double balance = user1.getBalances().getOrDefault(key, 0.0);

                // If the balance is positive (i.e., balance > 0), it means user1 owes user2 money. In this case, a transaction is created where user1 pays user2.
                //If the balance is negative (i.e., balance < 0), it means user2 owes user1 money. In this case, a transaction is created where user2 pays user1.
                if (balance > 0) {
                    createTransaction(user1, user2, balance);
                } else if (balance < 0) {
                    createTransaction(user2, user1, Math.abs(balance));
                }

                // After the transaction is created, the balances between the two users are reset to zero. This is done by calling the resetBalances function, which sets both users' balances related to each other to 0.0, effectively "settling" the balance.
                user1.getBalances().put(key, 0.0);
                user2.getBalances().put(getBalanceKey(user2, user1), 0.0);
            }
        }
    }

    private void createTransaction(User sender, User receiver, double amount) {
        String transactionId = generateTransactionId();
        Transaction transaction = new Transaction(transactionId, sender, receiver, amount);
        // Persist or log the transaction
    }

    private String generateTransactionId() {
        int transactionNumber = transactionCounter.incrementAndGet();
        return TRANSACTION_ID_PREFIX + String.format("%06d", transactionNumber);
    }
}
