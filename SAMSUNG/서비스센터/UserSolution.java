package 서비스센터;

import java.util.*;

class UserSolution {

    // 작업(수리 요청) 클래스
    static class Job {
        int id;
        int workload;
        int grade;
        int arrivalTime;
        
        public Job(int id, int workload, int grade, int arrivalTime) {
            this.id = id;
            this.workload = workload;
            this.grade = grade;
            this.arrivalTime = arrivalTime;
        }
    }

    // 로봇 클래스
    static class Robot {
        int id;
        int throughput;
        int finishTime; // 작업 완료 예정 시간
        Job currentJob; // 현재 수행 중인 작업
        
        public Robot(int id, int throughput) {
            this.id = id;
            this.throughput = throughput;
            this.finishTime = 0;
            this.currentJob = null;
        }
    }

    // 전역 변수 및 자료구조
    int N; // 지연 기준 시간
    int M; // 최대 등급
    
    // 로봇 관리
    Robot[] robots; // ID로 로봇 접근 (최대 1000)
    PriorityQueue<Robot> idleRobots; // 대기 중인 로봇 (우선순위: 처리량 DESC, ID ASC)
    PriorityQueue<Robot> busyRobots; // 작업 중인 로봇 (우선순위: 완료시간 ASC)
    
    // 작업 대기열
    Deque<Job>[] waitingQueueByGrade; // 등급별 대기열 (시간 순)
    PriorityQueue<Job> delayedQueue; // 지연된 작업 (우선순위: 시간 ASC)
    
    // 통계
    long[] totalWaitTimeByGrade; // 등급별 총 대기 시간 합

    public void init(int N, int M) {
        this.N = N;
        this.M = M;
        
        this.robots = new Robot[1001]; // rId 1~1000
        
        // 대기 로봇 우선순위: 처리량 높은 순 -> ID 작은 순
        this.idleRobots = new PriorityQueue<>((r1, r2) -> {
            if (r1.throughput != r2.throughput) return r2.throughput - r1.throughput;
            return r1.id - r2.id;
        });
        
        // 작업 중 로봇 우선순위: 끝나는 시간 빠른 순 (ID는 상관없음, 시뮬레이션 순서용)
        this.busyRobots = new PriorityQueue<>((r1, r2) -> Integer.compare(r1.finishTime, r2.finishTime));
        
        // 등급별 대기열 초기화
        this.waitingQueueByGrade = new ArrayDeque[M + 1];
        for (int i = 1; i <= M; i++) {
            this.waitingQueueByGrade[i] = new ArrayDeque<>();
        }
        
        // 지연 큐 우선순위: 접수 시간 빠른 순
        this.delayedQueue = new PriorityQueue<>((j1, j2) -> Integer.compare(j1.arrivalTime, j2.arrivalTime));
        
        this.totalWaitTimeByGrade = new long[M + 1];
    }

    // 현재 시간(targetTime)까지 시뮬레이션 진행
    private void updateSystem(int targetTime) {
        while (!busyRobots.isEmpty() && busyRobots.peek().finishTime <= targetTime) {
            int currentTime = busyRobots.peek().finishTime;
            
            // 1. 해당 시간에 완료되는 모든 로봇 처리
            while (!busyRobots.isEmpty() && busyRobots.peek().finishTime == currentTime) {
                Robot r = busyRobots.poll();
                Job finishedJob = r.currentJob;
                
                // 통계 기록: (완료 시간 - 접수 시간)
                totalWaitTimeByGrade[finishedJob.grade] += (currentTime - finishedJob.arrivalTime);
                
                // 로봇 상태 초기화 및 대기열 이동
                r.currentJob = null;
                r.finishTime = 0;
                idleRobots.offer(r);
            }
            
            // 2. 빈 로봇들에게 새로운 작업 할당 (currentTime 기준)
            assignJobs(currentTime);
        }
        
        // busyRobots에 남은 것들은 targetTime 이후에 끝남.
        // 하지만 targetTime 시점에 아직 할당되지 않은 로봇이 있다면 할당 시도해야 함
        // (receive나 add로 인해 호출된 경우를 위해, 혹은 시간이 흘러 지연 상태가 바뀐 경우)
        // 단, 위 루프는 '이벤트(작업 완료)' 기반이었고, 이제 targetTime 시점으로 강제 이동하여 정리
        
        // 여기서는 별도 할당을 하지 않음. 호출하는 쪽(receive, add, remove)에서 로직 수행 후 assignJobs 호출함.
        // 다만, remove 등의 호출 없이 evaluate만 호출될 때도 지연 큐 이동은 반영되어야 함?
        // -> evaluate는 '완료된' 것만 계산하므로 상관없음.
        // -> 하지만 remove 호출 시 '지연 여부' 판단을 위해 큐 이동은 필요할 수 있음.
    }

    // 대기 중인 작업들을 지연 큐로 이동 (time 기준)
    private void refreshDelayedJobs(int time) {
        for (int g = 1; g <= M; g++) {
            Deque<Job> q = waitingQueueByGrade[g];
            while (!q.isEmpty()) {
                Job head = q.peekFirst();
                if (time - head.arrivalTime >= N) {
                    delayedQueue.offer(q.pollFirst());
                } else {
                    // 시간 순으로 정렬되어 있으므로, Head가 지연 아니면 뒤도 지연 아님
                    break;
                }
            }
        }
    }

    // 작업 할당 로직
    private void assignJobs(int currentTime) {
        // 우선, 지연될 작업들을 이동시킴
        refreshDelayedJobs(currentTime);

        while (!idleRobots.isEmpty()) {
            Job bestJob = null;
            
            // 1. 지연된 작업 확인
            if (!delayedQueue.isEmpty()) {
                bestJob = delayedQueue.poll();
            } else {
                // 2. 등급 높은 순으로 일반 작업 확인
                for (int g = M; g >= 1; g--) {
                    if (!waitingQueueByGrade[g].isEmpty()) {
                        bestJob = waitingQueueByGrade[g].pollFirst();
                        break;
                    }
                }
            }
            
            if (bestJob == null) break; // 할당할 작업이 없음
            
            Robot r = idleRobots.poll(); // 가장 우선순위 높은 로봇
            
            // 소요 시간 계산 (올림 처리)
            // 시간 = ceil(workload / throughput)
            int duration = (bestJob.workload + r.throughput - 1) / r.throughput;
            
            r.currentJob = bestJob;
            r.finishTime = currentTime + duration;
            
            busyRobots.offer(r);
        }
    }

    public void receive(int mTime, int mId, int mWorkload, int mGrade) {
        updateSystem(mTime); // mTime 직전까지 처리 완료
        
        Job newJob = new Job(mId, mWorkload, mGrade, mTime);
        waitingQueueByGrade[mGrade].offerLast(newJob);
        
        assignJobs(mTime); // 새 작업이 들어왔으니 할당 시도
    }

    public void add(int mTime, int rId, int mThroughput) {
        updateSystem(mTime);
        
        Robot newRobot = new Robot(rId, mThroughput);
        robots[rId] = newRobot;
        idleRobots.offer(newRobot);
        
        assignJobs(mTime); // 새 로봇이 들어왔으니 할당 시도
    }

    public int remove(int mTime, int rId) {
        updateSystem(mTime);
        
        Robot r = robots[rId];
        robots[rId] = null; // 로봇 삭제 마킹
        
        int workingJobId = -1;
        
        // 로봇이 대기 목록에 있는지 확인
        if (idleRobots.remove(r)) {
            // 대기 중이었으면 별도 처리 없음
        } else if (busyRobots.remove(r)) { // 작업 중이었는지 확인
             // PriorityQueue.remove는 O(N)이지만 N이 작아(최대 500 call * 1000 size) 허용
             if (r.currentJob != null) {
                 Job job = r.currentJob;
                 workingJobId = job.id;
                 
                 // 작업 초기화 후 대기열 복귀
                 // 현재 시간 기준 지연 여부 판단 (mTime)
                 if (mTime - job.arrivalTime >= N) {
                     delayedQueue.offer(job);
                 } else {
                     // 원래 시간 순서를 유지하기 위해 등급 큐의 맨 앞에 넣음
                     // (이 작업은 과거에 들어온 것이므로 큐의 가장 오래된 작업임이 보장됨)
                     waitingQueueByGrade[job.grade].offerFirst(job);
                 }
             }
        }
        
        // 로봇 제거로 인해 작업이 반환되었으므로 재할당 시도
        // (반환된 작업이 매우 급하거나 높은 등급일 수 있으므로 즉시 다른 로봇에게 할당 시도)
        assignJobs(mTime);
        
        return workingJobId;
    }

    public int evaluate(int mTime, int mGrade) {
        updateSystem(mTime);
        return (int) totalWaitTimeByGrade[mGrade];
    }
}