package ServiceCenter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

class UserSolution {

    // --- 시뮬레이션 상태 변수 ---
    int delayThresholdN; // 지연 기준 시간
    int maxGradeM;       // 수리 요청 등급 최대값

    // --- 헬퍼 클래스 ---
    class Job {
        int id;
        int receiveTime;
        int workload; // (중요) 이 값은 불변 (원래 업무량)
        int grade;
        
        int robotId = -1;    // 이 작업을 처리 중인 로봇 ID
        int finishTime = -1; // 예상 완료 시간

        Job(int id, int receiveTime, int workload, int grade) {
            this.id = id;
            this.receiveTime = receiveTime;
            this.workload = workload;
            this.grade = grade;
        }
    }

    class Robot {
        int id;
        int throughput; // 처리량
        
        int assignedJobId = -1; // 현재 할당된 작업 ID
        int finishTime = -1;    // 현재 작업 완료 시간

        Robot(int id, int throughput) {
            this.id = id;
            this.throughput = throughput;
        }
    }

    // --- 핵심 데이터 구조 ---

    // K=rId, V=Robot : 모든 로봇 정보 (ID로 빠른 조회)
    HashMap<Integer, Robot> robots;
    // K=mId, V=Job : 모든 수리 요청 정보 (ID로 빠른 조회)
    HashMap<Integer, Job> jobs;

    /**
     * 대기 중인 로봇 (유휴 로봇)
     * 1. mThroughput (처리량) DESC (내림차순)
     * 2. rId (ID) ASC (오름차순)
     */
    PriorityQueue<Robot> availableRobots;

    /**
     * 현재 처리 중인 수리 요청
     * 1. finishTime (완료 시간) ASC (오름차순)
     */
    PriorityQueue<Job> finishingJobs;

    /**
     * 대기열 (지연된 요청)
     * 1. receiveTime (접수 시간) ASC (오름차순)
     */
    PriorityQueue<Job> delayedQueue;

    /**
     * 대기열 (등급별 일반 요청)
     * 인덱스: 등급 (1 ~ M)
     * 각 큐의 우선순위:
     * 1. receiveTime (접수 시간) ASC (오름차순)
     */
    PriorityQueue<Job>[] regularQueues;

    /**
     * 완료된 요청의 대기 시간 합계
     * K=grade (등급), V=totalWaitTime (총 대기 시간)
     */
    HashMap<Integer, Long> completedWaitTimes;

    
    // --- 핵심 시뮬레이션 함수 ---

    /**
     * (매 시각 프로세스 1 & 2)
     * 1. 지연 요청 처리: regularQueues -> delayedQueue
     * 2. 완료 요청 처리: finishingJobs -> completedWaitTimes, availableRobots
     *
     * @param currentTime 현재 시각
     */
    void advanceTime(int currentTime) {
        // 1. 지연 요청 확인 (regular -> delayed)
        for (int g = 1; g <= maxGradeM; g++) {
            while (!regularQueues[g].isEmpty() &&
                    regularQueues[g].peek().receiveTime <= currentTime - delayThresholdN) {
                // 지연 기준 시간을 초과한 경우
                delayedQueue.add(regularQueues[g].poll());
            }
        }

        // 2. 완료된 작업 처리
        while (!finishingJobs.isEmpty() &&
                finishingJobs.peek().finishTime <= currentTime) {

            Job finishedJob = finishingJobs.poll();
            
            // (방어 코드) 이미 remove()에 의해 처리된 작업일 수 있음 (robotId가 -1)
            // 이 경우 그냥 큐에서 꺼내기만 하고 무시
            if (finishedJob.robotId == -1) {
                continue;
            }

            Robot robot = robots.get(finishedJob.robotId);
            
            // (방어 코드) 로봇이 존재하지 않으면 (remove된 직후) 무시
            if (robot == null) {
                continue;
            }

            // 대기 시간 합계 기록 (완료시간 - 접수시간)
            long waitTime = (long)finishedJob.finishTime - finishedJob.receiveTime;
            completedWaitTimes.put(finishedJob.grade,
                    completedWaitTimes.getOrDefault(finishedJob.grade, 0L) + waitTime);

            // 로봇을 유휴 상태로 변경
            robot.assignedJobId = -1;
            robot.finishTime = -1;
            availableRobots.add(robot); // 유휴 로봇 큐에 다시 추가

            // 작업 상태도 초기화 (필수는 아님)
            finishedJob.robotId = -1;
            finishedJob.finishTime = -1;
        }
    }

    /**
     * (매 시각 프로세스 3)
     * 3. 작업 할당: (delayedQueue, regularQueues) -> availableRobots
     * 유휴 로봇이 있거나, 대기 중인 작업이 있는 한 계속 할당
     *
     * @param currentTime 현재 시각
     */
    void assignJobs(int currentTime) {
        while (!availableRobots.isEmpty()) {
            // 1. 할당할 작업 찾기 (최고 우선순위)
            Job jobToAssign = findNextJobInQueues();

            if (jobToAssign == null) {
                break; // 대기 중인 작업 없음
            }

            // 2. 할당할 로봇 찾기 (최고 우선순위)
            Robot robot = availableRobots.poll();

            // 3. 작업 할당
            assignJobToRobot(jobToAssign, robot, currentTime);
        }
    }

    /**
     * 대기열(delayed, regular)에서 가장 우선순위 높은 작업을 찾아 반환 (및 큐에서 제거)
     */
    Job findNextJobInQueues() {
        // 1순위: 지연된 요청 (접수 시간 ASC)
        if (!delayedQueue.isEmpty()) {
            return delayedQueue.poll();
        }

        // 2순위: 일반 요청 (등급 DESC, 접수 시간 ASC)
        for (int g = maxGradeM; g >= 1; g--) {
            if (!regularQueues[g].isEmpty()) {
                return regularQueues[g].poll();
            }
        }

        return null; // 대기 중인 작업 없음
    }

    /**
     * 특정 로봇에게 특정 작업을 할당 (상태 업데이트 및 finishingJobs에 추가)
     */
    void assignJobToRobot(Job job, Robot robot, int currentTime) {
        int work = job.workload; // (중요) 항상 원래 workload 사용
        int throughput = robot.throughput;

        // 올림 나눗셈: (a + b - 1) / b
        int timeTaken = (work + throughput - 1) / throughput;
        int finishTime = currentTime + timeTaken;

        // 작업 상태 업데이트
        job.finishTime = finishTime;
        job.robotId = robot.id;

        // 로봇 상태 업데이트
        robot.assignedJobId = job.id;
        robot.finishTime = finishTime;

        // "처리 중인 작업" 큐에 추가
        finishingJobs.add(job);
    }


    // --- API 구현 ---

    public void init(int N, int M) {
        delayThresholdN = N;
        maxGradeM = M;

        // 모든 자료구조 초기화
        robots = new HashMap<>();
        jobs = new HashMap<>();
        completedWaitTimes = new HashMap<>();

        // 로봇 우선순위 큐 (처리량 DESC, ID ASC)
        availableRobots = new PriorityQueue<>((r1, r2) -> {
            if (r1.throughput != r2.throughput) {
                return r2.throughput - r1.throughput;
            }
            return r1.id - r2.id;
        });

        // 작업 완료 우선순위 큐 (완료시간 ASC, ID ASC - 동일 시간 처리 보장)
        finishingJobs = new PriorityQueue<>((j1, j2) -> {
            if (j1.finishTime != j2.finishTime) {
                return j1.finishTime - j2.finishTime;
            }
            return j1.id - j2.id;
        });

        // 지연 작업 우선순위 큐 (접수시간 ASC)
        delayedQueue = new PriorityQueue<>(Comparator.comparingInt(j -> j.receiveTime));

        // 일반 작업 우선순위 큐 배열 (등급별, 접수시간 ASC)
        regularQueues = new PriorityQueue[M + 1];
        for (int i = 1; i <= M; i++) {
            regularQueues[i] = new PriorityQueue<>(Comparator.comparingInt(j -> j.receiveTime));
        }
    }

    /**
     * (매 시각 프로세스 4)
     * mTime 시점에 수리 요청 접수
     */
    public void receive(int mTime, int mId, int mWorkload, int mGrade) {
        // 1, 2. 지연/완료 처리
        advanceTime(mTime);
        // 3. 기존 유휴 로봇에 작업 할당
        assignJobs(mTime);

        // 4. (API 동작) 새 수리 요청 접수
        Job newJob = new Job(mId, mTime, mWorkload, mGrade);
        jobs.put(mId, newJob);
        regularQueues[mGrade].add(newJob); // 등급에 맞는 대기열에 추가

        // 3. (다시 실행) 방금 추가된 작업을 즉시 할당 시도
        assignJobs(mTime);
    }

    /**
     * (매 시각 프로세스 4)
     * mTime 시점에 로봇 추가
     */
    public void add(int mTime, int rId, int mThroughput) {
        // 1, 2. 지연/완료 처리
        advanceTime(mTime);
        // 3. 기존 유휴 로봇에 작업 할당
        assignJobs(mTime);

        // 4. (API 동작) 새 로봇 추가
        Robot newRobot = new Robot(rId, mThroughput);
        robots.put(rId, newRobot);
        availableRobots.add(newRobot); // 유휴 로봇 큐에 추가

        // 3. (다시 실행) 방금 추가된 로봇에 즉시 작업 할당 시도
        assignJobs(mTime);
    }

    /**
     * (매 시각 프로세스 4)
     * mTime 시점에 로봇 제거
     */
    public int remove(int mTime, int rId) {
        // 1, 2. 지연/완료 처리
        advanceTime(mTime);
        // 3. 기존 유휴 로봇에 작업 할당
        assignJobs(mTime);

        // 4. (API 동작) 로봇 제거
        Robot robotToRemove = robots.get(rId);
        
        // 전체 로봇 맵에서 제거
        robots.remove(rId);

        int interruptedJobId = robotToRemove.assignedJobId;

        if (interruptedJobId == -1) {
            // 로봇이 유휴 상태였음 (availableRobots 큐에 있었음)
            availableRobots.remove(robotToRemove); // O(N)
            return -1;
        } else {
            // 로봇이 작업 중이었음 (finishingJobs 큐에 있었음)
            Job interruptedJob = jobs.get(interruptedJobId);

            // [중요]
            // finishingJobs 큐에서 이 작업을 직접 remove() 하는 것은
            // O(N)이며 복잡합니다.
            // 대신, 작업(Job) 객체에 "주인이 없음"을 표시합니다.
            // advanceTime()에서 이 작업을 발견하면 (finishTime <= currentTime)
            // robotId가 -1인 것을 보고 무시하도록 처리합니다.
            interruptedJob.robotId = -1;
            interruptedJob.finishTime = -1;

            // 작업을 대기열로 복귀 (이때 mTime 기준으로 지연 여부 다시 판단)
            // "진행상황 초기화" = 원래 workload를 가지고 큐로 복귀
            if (interruptedJob.receiveTime <= mTime - delayThresholdN) {
                delayedQueue.add(interruptedJob);
            } else {
                regularQueues[interruptedJob.grade].add(interruptedJob);
            }

            // 3. (다시 실행) 방금 복귀된 작업을 다른 로봇에 즉시 할당 시도
            assignJobs(mTime);
            
            return interruptedJobId;
        }
    }

    /**
     * (매 시각 프로세스 4)
     * mTime 시점에 mGrade 등급의 완료된 요청들의 총 대기 시간 합산 반환
     */
    public int evaluate(int mTime, int mGrade) {
        // 1, 2. 지연/완료 처리
        advanceTime(mTime);
        // 3. 기존 유휴 로봇에 작업 할당
        assignJobs(mTime);

        // 4. (API 동작) 평가
        // "대기 시간의 합의 최대값은 10억을 넘지 않음을 보장" -> int로 반환 가능
        return (int) completedWaitTimes.getOrDefault(mGrade, 0L).longValue();
    }
}