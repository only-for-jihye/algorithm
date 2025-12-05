package 단어장;

class UserSolution_Answer {

	private static class Node {
	    Node[] children = new Node[26]; // a~z까지 26개의 자식 노드(폴더)
	    int subTreeSize = 0;            // ★ 이 노드 아래에 등록된 총 단어 개수
	    boolean isEnd = false;          // 여기서 단어가 끝나는지 여부 (예: car의 'r')
	    
	    // 검색 최적화를 위한 캐싱 데이터 (미리 계산해두는 값)
	    String bestWord = null;         // 이 경로를 지나는 단어 중 '짱'인 단어
	    int bestPriority = -1;          // 그 짱인 단어의 중요도
	    int bestOrder = -1;             // 그 짱인 단어의 등록 순서
	}

    private Node root;
    private String currentWord; // 현재 보고 있는 페이지의 단어
    private int globalOrder; // 단어 등록 순서 (Tie-breaker 용)

    public void init() {
        root = new Node();
        globalOrder = 0;
        currentWord = "a";
        
        // 초기에는 "a"만 등록되어 있음 (중요도 1)
        add("a", 1);
        
        // add 함수 호출 후 currentWord가 "a"로 설정되지만,
        // 명시적으로 초기화 상태를 확실히 함
        currentWord = "a";
    }

    public Main.PAGE add(String mWord, int mImportance) {
        insert(mWord, mImportance, ++globalOrder);
        currentWord = mWord;
        
        Main.PAGE ret = new Main.PAGE();
        ret.word = mWord;
        ret.no = getRank(mWord);
        return ret;
    }

    public Main.PAGE move(int mDir) {
        int currentRank = getRank(currentWord);
        int nextRank = currentRank + mDir;
        
        String nextWord = getWordByRank(nextRank);
        currentWord = nextWord;
        
        Main.PAGE ret = new Main.PAGE();
        ret.word = nextWord;
        ret.no = nextRank;
        return ret;
    }

    public Main.PAGE search(String mStr) {
        Main.PAGE ret = new Main.PAGE();
        
        // 1. 정확히 일치하는 단어 검색
        Node targetNode = findNode(mStr);
        if (targetNode != null && targetNode.isEnd) {
            currentWord = mStr;
            ret.word = mStr;
            ret.no = getRank(mStr);
            return ret;
        }
        
        // 2. 일치하는 단어가 없으면 접두어 검색 (Best Word 반환)
        if (targetNode != null) {
            // 해당 접두어로 시작하는 단어들 중 가장 좋은 단어 정보는
            // insert 시점에 이미 targetNode.bestWord에 계산되어 저장됨
            String best = targetNode.bestWord;
            currentWord = best;
            ret.word = best;
            ret.no = getRank(best);
            return ret;
        }
        
        // 3. 검색 실패
        ret.no = -1;
        ret.word = null;
        return ret;
    }

    public Main.PAGE go(int mNo) {
        String targetWord = getWordByRank(mNo);
        currentWord = targetWord;
        
        Main.PAGE ret = new Main.PAGE();
        ret.word = targetWord;
        ret.no = mNo;
        return ret;
    }

    // --- Helper Methods ---

    // Trie에 단어 삽입
    private void insert(String word, int importance, int order) {
        Node curr = root;
        curr.subTreeSize++; // 루트에도 단어 하나 추가됐다고 알림

        for (char c : word.toCharArray()) {
            int idx = c - 'a'; // 'a' -> 0, 'b' -> 1 ...
            if (curr.children[idx] == null) {
                curr.children[idx] = new Node(); // 길(노드)이 없으면 새로 뚫음
            }
            curr = curr.children[idx]; // 다음 칸으로 이동
            curr.subTreeSize++;        // 지나가는 길마다 "내 아래 단어 하나 늘었어!" 표시
            
            // ★ Best Word 갱신 로직 (캐싱)
            // 지금 넣는 단어(word)가 이 노드에 저장된 기존 'bestWord'보다 좋은지 확인
            boolean updateBest = false;
            if (curr.bestWord == null) { // 아직 베스트 단어가 없으면 무조건 등록
                updateBest = true;
            } else {
                // 1. 중요도가 더 높거나
                if (importance > curr.bestPriority) {
                    updateBest = true;
                // 2. 중요도는 같은데 더 먼저 등록된 놈이라면
                } else if (importance == curr.bestPriority && order < curr.bestOrder) {
                    updateBest = true;
                }
            }
            
            // 더 좋은 단어라면 정보를 덮어씌움 (나중에 검색할 때 바로 꺼내 쓰려고)
            if (updateBest) {
                curr.bestWord = word;
                curr.bestPriority = importance;
                curr.bestOrder = order;
            }
        }
        curr.isEnd = true; // 단어의 끝 표시
    }

    // 특정 단어의 사전순 순서(Rank) 반환 (1-based)
    private int getRank(String word) {
        Node curr = root;
        int rank = 1; // 1부터 시작
        
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            // 현재 depth에서 내 문자보다 앞에 있는 형제 노드들의 사이즈를 모두 더함
            for (int i = 0; i < idx; i++) {
                if (curr.children[i] != null) {
                    rank += curr.children[i].subTreeSize;
                }
            }
            
            // 부모 노드 자체가 단어의 끝이었다면, 그 단어는 나보다 사전순으로 앞섬 (예: "car" vs "cart")
            if (curr.isEnd) {
                rank++;
            }
            
            curr = curr.children[idx];
        }
        // 마지막 노드까지 내려왔을 때, 해당 노드 자체가 단어의 끝이므로
        // 위 로직에서는 자기 자신을 카운트하지 않음. 
        // Rank 정의상 자기 자신 앞의 개수 + 1이므로 현재까지 누적된 값(rank)이 정확함.
        // (단, 위 반복문 로직은 "내 앞의 것들"만 더하고 시작값 1을 줬으므로 정확함)
        
        return rank;
    }

    // Rank(k번째)에 해당하는 단어 찾기
    private String getWordByRank(int k) {
        Node curr = root;
        StringBuilder sb = new StringBuilder();
        
        while (true) {
            // 현재 노드 자체가 단어의 끝이라면, 이것이 1번째 순서임
            if (curr.isEnd) {
                if (k == 1) return sb.toString();
                k--; // 현재 단어 스킵
            }
            
            for (int i = 0; i < 26; i++) {
                if (curr.children[i] != null) {
                    int size = curr.children[i].subTreeSize;
                    if (k <= size) {
                        // 찾으려는 단어가 이 자식 노드 안에 있음
                        sb.append((char)('a' + i));
                        curr = curr.children[i];
                        break; // for loop 탈출, while 문 계속
                    } else {
                        // 이 자식 노드 전체를 건너뜀
                        k -= size;
                    }
                }
            }
        }
    }
    
    // 접두어에 해당하는 마지막 노드 찾기
    private Node findNode(String prefix) {
        Node curr = root;
        for (char c : prefix.toCharArray()) {
            int idx = c - 'a';
            if (curr.children[idx] == null) return null;
            curr = curr.children[idx];
        }
        return curr;
    }
}