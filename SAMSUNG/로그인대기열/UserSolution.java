package 로그인대기열;

class UserSolution {

    // -------------------------------------------------------------
    // 1. 자료구조 정의
    // -------------------------------------------------------------
    class Node {
        Node[] children = new Node[26];
        int ticket = -1; // 이 노드에서 끝나는 ID의 티켓 번호 (-1이면 없음)
    }

    private static final int MAX_USERS = 50005; // 최대 호출 횟수만큼 여유 있게
    
    private Node root;
    private Node[] ticketToNode;    // ticket 번호로 Trie Node 바로 접근
    private boolean[] isRemoved;    // 해당 ticket이 삭제되었는지 여부
    
    private int head; // 대기열의 맨 앞 (입장 차례)
    private int tail; // 대기열의 맨 뒤 (다음 티켓 번호)

    // -------------------------------------------------------------
    // 2. 초기화 (init)
    // -------------------------------------------------------------
    public void init() {
        root = new Node();
        ticketToNode = new Node[MAX_USERS];
        isRemoved = new boolean[MAX_USERS];
        head = 1; // 티켓은 1번부터 시작
        tail = 1;
    }

    // -------------------------------------------------------------
    // 3. 로그인 (loginID) - O(L)
    // -------------------------------------------------------------
    public void loginID(char mID[]) {
        // 1. 트라이 탐색 및 노드 생성
        Node curr = root;
        for (char c : mID) {
            if (c == '\0') break;
            int idx = c - 'a';
            if (curr.children[idx] == null) {
                curr.children[idx] = new Node();
            }
            curr = curr.children[idx];
        }

        // 2. 이미 대기열에 있는 ID라면 기존 티켓 무효화
        if (curr.ticket != -1) {
            isRemoved[curr.ticket] = true; 
            // 주의: 기존 티켓이 가리키던 Node의 ticket 값은 
            // 아래에서 새 티켓번호로 덮어쓰므로 따로 -1 처리 안 해도 됨
        }

        // 3. 새 티켓 발급
        int newTicket = tail++;
        curr.ticket = newTicket;    // 트라이 노드에 최신 티켓 기록
        ticketToNode[newTicket] = curr; // 역참조 테이블 기록
        isRemoved[newTicket] = false;
    }

    // -------------------------------------------------------------
    // 4. 일괄 삭제 (closeIDs) - Trie DFS 활용
    // -------------------------------------------------------------
    public int closeIDs(char mStr[]) {
        Node curr = root;
        // 1. Prefix 노드까지 이동
        for (char c : mStr) {
            if (c == '\0') break;
            int idx = c - 'a';
            if (curr.children[idx] == null) {
                return 0; // 해당 prefix가 없으면 삭제할 것도 없음
            }
            curr = curr.children[idx];
        }

        // 2. 해당 노드 하위의 모든 유효 ID 삭제 (DFS)
        return deleteDFS(curr);
    }

    private int deleteDFS(Node curr) {
        int count = 0;

        // 현재 노드에 유효한 티켓이 있다면 삭제 처리
        if (curr.ticket != -1) {
            isRemoved[curr.ticket] = true;
            curr.ticket = -1; // 트라이에서도 제거
            count++;
        }

        // 자식 노드들 탐색
        for (int i = 0; i < 26; i++) {
            if (curr.children[i] != null) {
                count += deleteDFS(curr.children[i]);
            }
        }
        return count;
    }

    // -------------------------------------------------------------
    // 5. 입장 (connectCnt) - Lazy Deletion 처리
    // -------------------------------------------------------------
    public void connectCnt(int mCnt) {
        // head부터 mCnt명을 입장시킴
        // 중간에 isRemoved==true인(이미 나간) 티켓은 카운트하지 않고 건너뜀
        while (mCnt > 0 && head < tail) {
            if (!isRemoved[head]) {
                // 유효한 유저 입장 -> 트라이에서도 정보 삭제해야 함
                Node node = ticketToNode[head];
                if (node != null && node.ticket == head) {
                    node.ticket = -1; 
                }
                isRemoved[head] = true; // 입장 완료 처리
                mCnt--;
            }
            head++; // 다음 사람
        }
    }

    // -------------------------------------------------------------
    // 6. 대기 순서 확인 (waitOrder) - O(N) but Fast enough
    // -------------------------------------------------------------
    public int waitOrder(char mID[]) {
        // 1. 트라이에서 해당 ID의 티켓 번호 찾기
        Node curr = root;
        for (char c : mID) {
            if (c == '\0') break;
            int idx = c - 'a';
            if (curr.children[idx] == null) {
                return 0; // 대기열에 아예 없음
            }
            curr = curr.children[idx];
        }

        int myTicket = curr.ticket;
        // 티켓이 없거나(-1), 이미 삭제된(isRemoved) 상태면 0 반환
        if (myTicket == -1 || isRemoved[myTicket]) {
            return 0;
        }

        // 2. head부터 myTicket 전까지 실제 대기 인원 세기
        // (세그먼트 트리 등을 쓰면 O(logN)이지만, N=50,000에 호출 1,000회라 단순 루프도 통과)
        int order = 0;
        for (int t = head; t < myTicket; t++) {
            if (!isRemoved[t]) {
                order++;
            }
        }
        
        return order + 1; // 내 순서는 +1
    }
}