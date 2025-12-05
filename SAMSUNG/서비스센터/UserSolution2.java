package 서비스센터;

import java.util.*;

class UserSolution2 {

	// 수리요청
	// 업무량, 등급, 접수시간 : 등급 높을수록 -> 접수시간 빠를수록 // 대기열1
	// 수리요청이 접수된 시간으로부터 지연 기준 시간 이상 경과 // 대기열2 (지연, 우선순위가 대기열1보다 높음), 접수시간 빠를수록
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
	
	// 로봇
	// 처리량 : 처리량 높을수록 -> ID가 작을수록
	class Robot {
		int id;
		int throughput;
		int finishTime; // 작업 완료 예상 시간
		Job currentJob; // 현재 수행중인 작업
		public Robot(int id, int throughput) {
			super();
			this.id = id;
			this.throughput = throughput;
			this.finishTime = 0;
			this.currentJob = null;
		}
	}

	int delayStandardTime; // 지연 기준 시간
	int maxGrade; // 최대 등급
	int[] totalTime;
	
	// 로봇
	Robot[] robots;
	PriorityQueue<Robot> idleRobots; // 대기중인 로봇 (처리량 desc, id asc) 
	PriorityQueue<Robot> busyRobots; // 작업중인 로봇 (완료시간 asc)
	
	// 작업 대기열
	ArrayDeque<Job>[] waitingQueueByGrade; // 등급별 대기열 (접수시간 asc)
	PriorityQueue<Job> delayedQueue; // 지연된 작업 (접수시간 asc)
	
	void init(int N, int M) {
		this.delayStandardTime = N;
		this.maxGrade = M;
		this.robots = new Robot[1001]; // 1~1000
		
		this.idleRobots = new PriorityQueue<>((r1, r2) -> {
			if (r1.throughput != r2.throughput) return Integer.compare(r2.throughput, r1.throughput);
			return Integer.compare(r1.id, r2.id);
		});
		this.busyRobots = new PriorityQueue<>((b1, b2) -> {
			return Integer.compare(b1.finishTime, b2.finishTime);
		});
		this.waitingQueueByGrade = new ArrayDeque[M + 1]; // 1~M
		for (int i = 1; i <= M; i++) {
			waitingQueueByGrade[i] = new ArrayDeque<>();
		}
		this.delayedQueue = new PriorityQueue<>((d1, d2) -> {
			return Integer.compare(d1.arrivalTime, d2.arrivalTime);
		});
		totalTime = new int[M + 1];
	}
	
	private void updateSystem(int targetTime) {
		while (!busyRobots.isEmpty() && busyRobots.peek().finishTime <= targetTime) {
			int currentTime = busyRobots.peek().finishTime;
			
			// 위 시간에 작업을 완료하는 로봇들 처리
			while (!busyRobots.isEmpty() && busyRobots.peek().finishTime == currentTime) {
				Robot robot = busyRobots.poll();
				Job finishedJob = robot.currentJob;
				// 통계 기록
				totalTime[finishedJob.grade] += (currentTime - finishedJob.arrivalTime);
				
				// 로봇 상태 초기화 및 대기열 이동
				robot.currentJob = null;
				robot.finishTime = 0;
				idleRobots.add(robot);
			}
			// 대기중인 로봇들에게 새로운 작업을 할당하자
			assignJobs(currentTime);
		}
	}
	
	private void moveDelayedJobs(int currentTime) {
		for (int i = 1; i <= maxGrade; i++) {
			ArrayDeque<Job> dq = waitingQueueByGrade[i];
			while (!dq.isEmpty()) {
				Job job = dq.peekFirst();
				// (현재 시간 - 접수 시간)이 기준 시간보다 초과하면 지연 큐로 넘김
				if (currentTime - job.arrivalTime >= delayStandardTime) {
					delayedQueue.add(dq.pollFirst());
				} else {
					// 이미 watingQueueByGrade는 시간 순으로 정렬되어 있으므로 이 이후에는 pass해도 됨
					break;
				}
			}
		}
	}
	
	private void assignJobs(int currentTime) {
		// 지연되는 작업들을 지연 대기열로 옮김
		moveDelayedJobs(currentTime);
		
		// 작업 할당 시작
		while (!idleRobots.isEmpty()) {
			Job firstJob = null;
			// 지연된 작업
			if (!delayedQueue.isEmpty()) {
				firstJob = delayedQueue.poll();
			} else {
				// 지연된 작업이 없다면, 등급이 높은 순으로 작업이 있는지 확인
				for (int i = maxGrade; i >= 1 ; i--) {
					if (!waitingQueueByGrade[i].isEmpty()) {
						firstJob = waitingQueueByGrade[i].pollFirst();
						break;
					}
				}
			}
			
			if (firstJob == null) break; // 할당할 작업이 없으므로 종료
			
			Robot robot = idleRobots.poll(); // 우선순위가 가장 높은 로봇을 찾는다.
			
			// 작업 완료 예정 시간 구하기
			int duration = (int) Math.ceil((double)firstJob.workload / robot.throughput);
//			System.out.println(duration);
			robot.finishTime = currentTime + duration;
			robot.currentJob = firstJob;
			
			busyRobots.add(robot);
		}
	}

	void receive(int mTime, int mId, int mWorkload, int mGrade) {
		updateSystem(mTime);
		
		Job job = new Job(mId, mWorkload, mGrade, mTime);
		waitingQueueByGrade[mGrade].addLast(job);
		
		assignJobs(mTime);
	}

	void add(int mTime, int rId, int mThroughput) {
		updateSystem(mTime);
		
		Robot robot = new Robot(rId, mThroughput);
		robots[rId] = robot;
		idleRobots.add(robot);
		
		assignJobs(mTime);
	}

	int remove(int mTime, int rId) {
		updateSystem(mTime);
		
		Robot robot = robots[rId];
		robots[rId] = null; // 로봇 제거
		
		int jobId = -1;
		if (idleRobots.remove(robot)) {
			
		} else if (busyRobots.remove(robot)) {
			if (robot.currentJob != null) {
				Job job = robot.currentJob;
				jobId = job.id;
				
				// 진행중이던 수리요청이 현재 시간 기준으로 지연된 건인지 확인하기
				if (mTime - job.arrivalTime >= delayStandardTime) {
					delayedQueue.add(job);
				} else {
					waitingQueueByGrade[job.grade].addFirst(job);
				}
			}
		}
		
		
		assignJobs(mTime);
		return jobId;
	}

	int evaluate(int mTime, int mGrade) {
		updateSystem(mTime);
		return totalTime[mGrade];
	}
	
}