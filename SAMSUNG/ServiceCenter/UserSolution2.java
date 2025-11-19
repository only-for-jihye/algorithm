package ServiceCenter;

import java.util.*;

class UserSolution2 {
	
	class Repair implements Comparable<Repair> {
		int id;
		int workload;
		int grade;
		int time;
		public Repair(int id, int workload, int grade, int time) {
			super();
			this.id = id;
			this.workload = workload;
			this.grade = grade;
			this.time = time;
		}
		@Override
		public int compareTo(Repair o) {
			if (this.grade != o.grade) return Integer.compare(o.grade, this.grade);
			if (this.time != o.time) return Integer.compare(this.time, o.time);
			return 0;
		}
	}
	
	class Robot implements Comparable<Robot> {
		int id;
		int throughput;
		int repairId;
		int finishTime;
		public Robot(int id, int throughput, int finishTime) {
			super();
			this.id = id;
			this.throughput = throughput;
			this.repairId = 0;
			this.finishTime = finishTime;
		}
		@Override
		public int compareTo(Robot o) {
			if (this.throughput != o.throughput) return Integer.compare(o.throughput, this.throughput);
			if (this.id != o.id) return Integer.compare(this.id, o.id);
			return 0;
		}
	}
	
	TreeSet<Repair> waitingQueue = new TreeSet<>();
	TreeSet<Repair> deplayQueue = new TreeSet<>();
	TreeSet<Robot> waitingRobotQueue = new TreeSet<>();
	TreeSet<Robot> workingRobotQueue = new TreeSet<>();
	int N;
	int M;
	Map<Integer, Robot> robotList = new HashMap<>();
	
	void init(int N, int M) {
		this.N = N;
		this.M = M;
		waitingQueue.clear();
		deplayQueue.clear();
		waitingRobotQueue.clear();
		workingRobotQueue.clear();
		robotList.clear();
	}

	void receive(int mTime, int mId, int mWorkload, int mGrade) {
		Repair repair = new Repair(mId, mWorkload, mGrade, mTime);
		// 대기중인 로봇이 없다면, 대기열에 추가하고 종료
		if (waitingRobotQueue.isEmpty()) {
			waitingQueue.add(repair);
			return;
		}
		// 대기중인 로봇이 있다면 할당 후 종료
		Iterator<Robot> it = waitingRobotQueue.iterator();
		while (it.hasNext()) {
			Robot robot = it.next();
			int modifyFinishTime = (int) Math.ceil(repair.workload / robot.throughput);
			robot.finishTime = modifyFinishTime;
			robot.repairId = repair.id;
			it.remove();
			workingRobotQueue.add(robot);
			return;
		}
	}

	void add(int mTime, int rId, int mThroughput) {
		Robot robot = new Robot(rId, mThroughput, mTime);
		// 대기중인 수리 요청이 없다면, 대기열에 추가하고 종료
		if (waitingQueue.isEmpty()) {
			waitingRobotQueue.add(robot);
		}
		// 대기중인 수리 요청이 있다면, 할당
		Iterator<Repair> it = waitingQueue.iterator();
		while (it.hasNext()) {
			Repair repair = it.next();
			int modifyFinishTime = (int) Math.ceil(repair.workload / robot.throughput);
			robot.finishTime = modifyFinishTime;
			robot.repairId = repair.id;
			it.remove();
			workingRobotQueue.add(robot);
			robotList.put(rId, robot);
			return;
		}
	}

	int remove(int mTime, int rId) {
		return 0;
	}

	int evaluate(int mTime, int mGrade) {
		return 0;
	}
	
	
}