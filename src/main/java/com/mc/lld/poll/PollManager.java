package com.mc.lld.poll;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PollManager {
    Map<String, Poll> pollMap;
    Map<String, List<Vote>> pollVoteMap;

    private PollManager() {
        this.pollMap = new ConcurrentHashMap<>();
        this.pollVoteMap = new ConcurrentHashMap<>();
    }

    // Singleton instance with double-checked locking
    private static volatile PollManager INSTANCE;

    public static PollManager getInstance() {
        if (INSTANCE == null) {
            synchronized (PollManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PollManager();
                }
            }
        }
        return INSTANCE;
    }

    public void createPoll(String question, List<String> options) {
        Poll poll = new Poll(question);
        poll.setOptions(options);
        pollMap.putIfAbsent(poll.getPollId(), poll);
    }

    // Update an existing poll
    public void updatePoll(String pollId, String question, List<String> options) {
        Poll poll = pollMap.get(pollId);
        if (poll == null) {
            throw new IllegalArgumentException("Poll not found.");
        }
        synchronized (poll) { // Lock the poll to ensure thread-safe updates
            poll.setQuestion(question);
            poll.setOptions(options);
        }
    }

    public void deletePoll(String pollId) {
        if (pollMap.get(pollId)!=null) {
            pollMap.remove(pollId);
        }
        pollVoteMap.remove(pollId); // Clean up associated votes
    }

    // Cast a vote in a poll
    public void voteInPoll(String pollId, String userId, String option) {
        Poll poll = pollMap.get(pollId);
        if (poll == null) {
            throw new IllegalArgumentException("Poll not found.");
        }
        synchronized (poll) { // Lock the poll during voting to validate options
            if (!poll.getOptions().contains(option)) {
                throw new IllegalArgumentException("Invalid option.");
            }
        }

        // Add the vote (thread-safe due to CopyOnWriteArrayList)
        pollVoteMap.computeIfAbsent(pollId, k -> new CopyOnWriteArrayList<>())
                .add(new Vote(userId, pollId, option));
    }

    // View poll results
    public List<Vote> viewPollResults(String pollId) {
        List<Vote> votes = pollVoteMap.get(pollId);
        if (votes == null) {
            throw new IllegalArgumentException("Poll not found.");
        }
        return Collections.unmodifiableList(votes); // Return an immutable view
    }


}
