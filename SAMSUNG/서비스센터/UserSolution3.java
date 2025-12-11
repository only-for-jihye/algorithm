package 서비스센터;

import java.util.*;

class UserSolution3 {
	
	class Job {
		int id;
		int workload;
		int grade;
		int arrivalTime;
		public Job(int id, int workload, int grade, int arrivalTime) {
			super();
			this.id = id;
			this.workload = workload;
			this.grade = grade;
			this.arrivalTime = arrivalTime;
		}
	}
	
	class Robot {
		int id;
		int throughput;
		int finishTime;
		Job currentJob;
		public Robot(int id, int throughput) {
			super();
			this.id = id;
			this.throughput = throughput;
			this.finishTime = 0;
			this.currentJob = null;
		}
	}
	
	int delayTime;
	int maxGrade;
	int[] totalTime;
	
	Robot[] robots;
	PriorityQueue<Robot> idleRobots;
	PriorityQueue<Robot> busyRobots;
	
	ArrayDeque<Job>[] waitingByGradeQueue;
	PriorityQueue<Job> delayedQueue;
	
	void init(int N, int M) {
		delayTime = N;
		maxGrade = M;
		robots = new Robot[1001];
		
		idleRobots = new PriorityQueue<>((i1, i2) -> {
			if (i1.throughput != i2.throughput) return Integer.compare(i2.throughput, i1.throughput);
			return Integer.compare(i1.id, i2.id);
		});
		busyRobots = new PriorityQueue<>((b1, b2) -> {
			return Integer.compare(b1.finishTime, b2.finishTime);
		});
		waitingByGradeQueue = new ArrayDeque[M + 1];
		for (int i = 0; i < M + 1; i++) {
			waitingByGradeQueue[i] = new ArrayDeque<>();
		}
		delayedQueue = new PriorityQueue<>((d1, d2) -> {
			return Integer.compare(d1.arrivalTime, d2.arrivalTime);
		});
		totalTime = new int[M + 1];
	}
	
	void updateSystem(int targetTime) {
		while (!busyRobots.isEmpty() && busyRobots.peek().finishTime <= targetTime) {
			int currentTime = busyRobots.peek().finishTime;
			
			while (!busyRobots.isEmpty() && busyRobots.peek().finishTime == currentTime) {
				Robot rb = busyRobots.poll();
				Job finishedJob = rb.currentJob;
				totalTime[finishedJob.grade] += (currentTime - finishedJob.arrivalTime);
				rb.currentJob = null;
				rb.finishTime = 0;
			}
			assignJobs(currentTime);
		}
	}

	void assignJobs(int currentTime) {
		moveDelayJob(currentTime);
		
		while (!idleRobots.isEmpty()) {
			Job firstJob = null;
			if (!delayedQueue.isEmpty()) {
				firstJob = delayedQueue.poll();
			} else {
				for (int i = maxGrade; i >= 1; i--) {
					while (!waitingByGradeQueue[i].isEmpty()) {
						firstJob = waitingByGradeQueue[i].pollFirst();
						break;
					}
				}
			}
			
			if (firstJob == null) break;
			
			Robot rb = idleRobots.poll();
			
			int duration = (int) Math.ceil((double) firstJob.workload / rb.throughput);
			rb.finishTime = currentTime + duration;
			rb.currentJob = firstJob;
			busyRobots.add(rb);
		}
		
	}

	void moveDelayJob(int currentTime) {
		for (int i = 1; i <= maxGrade; i++) {
			ArrayDeque<Job> dq = waitingByGradeQueue[i];
			while (!dq.isEmpty()) {
				Job job = dq.peekFirst();
				if (currentTime - job.arrivalTime >= delayTime) {
					delayedQueue.add(dq.pollFirst());
				} else {
					break;
				}
			}
		}
	}

	void receive(int mTime, int mId, int mWorkload, int mGrade) {
	}

	void add(int mTime, int rId, int mThroughput) { 
	}

	int remove(int mTime, int rId) {
		return 0;
	}

	int evaluate(int mTime, int mGrade) {
		return 0;
	}
	
}