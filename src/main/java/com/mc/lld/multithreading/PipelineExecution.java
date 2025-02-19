package com.mc.lld.multithreading;

import java.util.*;
import java.util.concurrent.*;

// Represents a Job in the pipeline
class Job {
    String jobId;

    public Job(String jobId) {
        this.jobId = jobId;
    }

    // Simulates job execution
    public void doWork() throws Exception {
        System.out.println("Executing Job: " + jobId);
        // Simulating a failure scenario with a 10% probability
        if (Math.random() < 0.1) {
            throw new Exception("Job Failed: " + jobId);
        }
    }

    @Override
    public String toString() {
        return jobId;
    }
}

// Manages the execution of jobs with dependencies in a pipeline
class PipelineManager {
    private final Map<Job, List<Job>> dependencyGraph = new HashMap<>(); // Stores job dependencies
    private final Map<Job, Integer> inDegree = new HashMap<>(); // Stores the number of dependencies for each job
    private final ExecutorService executorService = Executors.newFixedThreadPool(4); // Thread pool for parallel execution
    private final CountDownLatch latch; // Synchronization mechanism to track job completion
    private volatile boolean failureOccurred = false; // Flag to indicate failure in pipeline execution

    public PipelineManager(List<Job> jobs, Map<Job, List<Job>> dependencies) {
        // Initialize graph and in-degree map
        for (Job job : jobs) {
            dependencyGraph.put(job, new ArrayList<>());
            inDegree.put(job, 0);
        }

        // Build the dependency graph
        for (Map.Entry<Job, List<Job>> entry : dependencies.entrySet()) {
            Job parent = entry.getKey();
            for (Job child : entry.getValue()) {
                dependencyGraph.get(parent).add(child);
                inDegree.put(child, inDegree.get(child) + 1);
            }
        }

        latch = new CountDownLatch(jobs.size()); // Initialize latch with total job count
    }

    // Executes the pipeline in parallel
    public void execute() {
        Queue<Job> readyQueue = new LinkedList<>(); // Queue to store jobs ready for execution

        // Identify jobs with no dependencies (in-degree == 0)
        for (Job job : inDegree.keySet()) {
            if (inDegree.get(job) == 0) {
                readyQueue.offer(job);
            }
        }

        Map<Job, Future<?>> jobFutures = new HashMap<>(); // Stores futures for tracking job execution

        while (!readyQueue.isEmpty() && !failureOccurred) {
            Job job = readyQueue.poll(); // Fetch a ready job

            Future<?> future = executorService.submit(() -> {
                try {
                    job.doWork(); // Execute job

                    synchronized (this) {
                        // Unlock dependent jobs
                        for (Job dependent : dependencyGraph.get(job)) {
                            inDegree.put(dependent, inDegree.get(dependent) - 1);
                            if (inDegree.get(dependent) == 0) {
                                readyQueue.offer(dependent);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    failureOccurred = true; // Mark failure and stop execution
                    executorService.shutdownNow();
                } finally {
                    latch.countDown(); // Decrease the count of remaining jobs
                }
            });

            jobFutures.put(job, future);
        }

        try {
            latch.await(); // Wait for all jobs to complete
            executorService.shutdown(); // Shutdown thread pool

            if (!failureOccurred) {
                System.out.println("Pipeline Execution Successful!");
            } else {
                System.out.println("Pipeline Execution Failed!");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Main class to run the pipeline
public class PipelineExecution {
    public static void main(String[] args) {
        // Define Jobs
        Job A = new Job("A");
        Job B = new Job("B");
        Job C = new Job("C");
        Job D = new Job("D");
        Job E = new Job("E");
        Job F = new Job("F");
        Job G = new Job("G");

        List<Job> jobs = Arrays.asList(A, B, C, D, E, F, G);

        // Define Dependencies
        Map<Job, List<Job>> dependencies = new HashMap<>();
        dependencies.put(A, Arrays.asList(B, C)); // A must finish before B and C can start
        dependencies.put(B, Arrays.asList(D, E)); // B must finish before D and E
        dependencies.put(C, Arrays.asList(F)); // C must finish before F
        dependencies.put(F, Arrays.asList(G)); // F must finish before G

        // Initialize and execute pipeline
        PipelineManager pipelineManager = new PipelineManager(jobs, dependencies);
        pipelineManager.execute();
    }
}

