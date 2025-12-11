package 단어장;

class UserSolution3 {
    // ==========================================
    // 상수 및 전역 변수 설정
    // ==========================================
    private static final int MAX_WORDS = 70001;      // 최대 단어 수
    private static final int MAX_NODES = 420001;     // 트라이 노드 풀 크기 (단어 수 * 평균 길이)
    private static final int ALPHABET_SIZE = 26;     // 알파벳 소문자 개수

    // 단어 ID를 통해 실제 문자열을 찾기 위한 배열 (1-based index 사용)
    private String[] wordDictionary;
    private int wordIdCounter;
    
    // 현재 커서가 가리키는 단어의 순위 (0-based index: 0이 첫 번째 단어)
    private int currentRank;

    // ==========================================
    // 내부 클래스: TrieNode (트라이 노드)
    // ==========================================
    class TrieNode {
        int wordId;            // 이 노드에서 끝나는 단어의 ID (-1이면 단어가 아님)
        int maxImportance;     // 이 노드를 루트로 하는 서브트리 내의 최대 중요도
        int maxImportanceId;   // 최대 중요도를 가진 단어의 ID
        
        // 각 자식 노드(알파벳) 하위에 존재하는 단어의 개수 저장 (Rank 계산용)
        int[] subtreeWordCount;
        TrieNode[] children;

        // 생성자 대신 init() 메서드 사용 (메모리 풀 재사용을 위해)
        void init() {
            this.wordId = -1;
            this.maxImportance = -1;
            this.maxImportanceId = -1;
            
            if (this.children == null) {
                this.children = new TrieNode[ALPHABET_SIZE];
                this.subtreeWordCount = new int[ALPHABET_SIZE];
            } else {
                for (int i = 0; i < ALPHABET_SIZE; i++) {
                    this.children[i] = null;
                    this.subtreeWordCount[i] = 0;
                }
            }
        }
    }

    // ==========================================
    // 내부 클래스: Trie (자료구조 핵심 로직)
    // ==========================================
    class Trie {
        TrieNode root;

        Trie() {
            this.root = allocateNode();
        }

        /**
         * 단어를 트라이에 삽입하고, 해당 단어의 사전 순서(Rank)를 반환합니다.
         * @param str 삽입할 단어
         * @param importance 단어의 중요도
         * @return 삽입된 단어보다 사전순으로 앞선 단어의 개수 (0-based Rank)
         */
        int insertAndGetRank(String str, int importance) {
            TrieNode current = root;
            wordDictionary[++wordIdCounter] = str; // 단어 저장 및 ID 부여

            int rank = 0; // 내 앞에 있는 단어 개수

            for (int i = 0; i < str.length(); i++) {
                // 1. 서브트리 최대 중요도 갱신 (Search 최적화용)
                if (importance > current.maxImportance) {
                    current.maxImportance = importance;
                    current.maxImportanceId = wordIdCounter;
                }

                int charIndex = str.charAt(i) - 'a';

                // 2. 자식 노드가 없으면 생성
                if (current.children[charIndex] == null) {
                    current.children[charIndex] = allocateNode();
                }

                // 3. Rank 계산: 내 앞에 있는 형제 노드들의 단어 수를 모두 더함
                //    (현재 글자보다 사전순으로 앞선 글자로 시작하는 단어들)
                for (int j = 0; j < charIndex; j++) {
                    rank += current.subtreeWordCount[j];
                }

                // 4. 현재 노드 자체가 단어의 끝이라면(즉, 내 접두사가 단어라면), 그 단어도 내 앞임
                if (current.wordId != -1) {
                    rank++;
                }

                // 5. 현재 경로(자식)의 단어 수 증가시키고 이동
                current.subtreeWordCount[charIndex]++;
                current = current.children[charIndex];
            }

            // 마지막 노드에 단어 ID 마킹
            current.wordId = wordIdCounter;
            return rank;
        }

        /**
         * K번째(targetRank) 단어의 ID를 찾습니다.
         * @param targetRank 찾으려는 순위 (1-based)
         * @return 단어 ID
         */
        int getWordIdByRank(int targetRank) {
            TrieNode current = root;

            while (true) {
                // 현재 노드 자체가 단어라면, 순위 하나 차감 (이 단어를 지나침)
                if (current.wordId != -1) {
                    targetRank--;
                }

                // 찾았다!
                if (targetRank == 0) {
                    return current.wordId;
                }

                // 자식 노드들을 순회하며 어디로 내려갈지 결정
                for (int i = 0; i < ALPHABET_SIZE; i++) {
                    // i번째 자식 쪽 서브트리에 찾는 단어가 포함되어 있다면 진입
                    if (targetRank <= current.subtreeWordCount[i]) {
                        current = current.children[i];
                        break; // while문의 다음 턴으로 (깊이 들어감)
                    }
                    // 아니라면, 해당 서브트리의 단어 수만큼 랭크에서 빼고 다음 형제로 넘어감
                    targetRank -= current.subtreeWordCount[i];
                }
            }
        }

        /**
         * 특정 접두사(str)로 시작하는 단어 중 중요도가 가장 높은 단어를 찾습니다.
         * @return 단어 ID (없으면 -1)
         */
        int searchBestWordId(String str) {
            TrieNode current = root;

            for (int i = 0; i < str.length(); i++) {
                int charIndex = str.charAt(i) - 'a';
                if (current.children[charIndex] == null) {
                    return -1; // 접두사가 존재하지 않음
                }
                current = current.children[charIndex];
            }

            // 접두사와 정확히 일치하는 단어가 있다면 그 단어 ID 반환
            // (문제 조건에 따라, 정확히 일치하는 단어가 있어도 하위 단어 중 중요도가 더 높은걸 찾아야 할 수도 있으나,
            //  원본 코드는 접두사 끝지점에서 maxIdx를 리턴하거나 현재 idx를 리턴함)
            
            // 원본 로직 유지:
            // 탐색 끝난 지점이 단어의 끝이라면 그 단어 ID 반환 (우선순위 1)
            // 아니라면 해당 서브트리에서 가장 중요도 높은 단어 ID 반환 (우선순위 2)
            if (current.wordId == -1) {
                return current.maxImportanceId;
            }
            return current.wordId;
        }

        /**
         * 특정 단어 ID의 현재 순위(Rank)를 계산합니다.
         * @param targetId 찾으려는 단어 ID
         * @return 0-based Rank
         */
        int getRankByWordId(int targetId) {
            String word = wordDictionary[targetId];
            TrieNode current = root;
            int rank = 0;

            for (int i = 0; i < word.length(); i++) {
                int charIndex = word.charAt(i) - 'a';

                // 내 앞의 형제 노드들의 단어 수를 더함
                for (int j = 0; j < charIndex; j++) {
                    rank += current.subtreeWordCount[j];
                }

                // 부모가 단어라면(접두사 단어), 순위 추가
                if (current.wordId != -1) {
                    rank++;
                }

                current = current.children[charIndex];
            }
            return rank;
        }
    }

    // ==========================================
    // Memory Pool (메모리 풀)
    // ==========================================
    TrieNode[] nodePool;
    int poolIndex;

    TrieNode allocateNode() {
        if (nodePool[poolIndex] == null) {
            nodePool[poolIndex] = new TrieNode();
        }
        nodePool[poolIndex].init();
        return nodePool[poolIndex++];
    }

    // ==========================================
    // API Implementation
    // ==========================================
    Trie trie;

    public void init() {
        // 메모리 풀 및 변수 초기화
        if (nodePool == null) {
            nodePool = new TrieNode[MAX_NODES];
            wordDictionary = new String[MAX_WORDS];
        }
        poolIndex = 0;
        wordIdCounter = 0;
        
        trie = new Trie();
        
        // 문제 조건: 초기 상태에 "a" (중요도 1) 존재
        // 루트 노드의 idx를 0으로 설정하는 원본 코드의 의도는 
        // 0번 ID를 더미 혹은 루트용으로 쓰겠다는 의미일 수 있음
        trie.root.wordId = 0; 
        
        // "a" 삽입 -> 반환값은 0 (첫 번째 단어이므로)
        currentRank = trie.insertAndGetRank("a", 1); 
    }

    public Main.PAGE add(String mWord, int mImportance) {
        Main.PAGE res = new Main.PAGE();
        
        // 단어 삽입 후, 해당 단어의 순위(페이지 번호) 업데이트
        currentRank = trie.insertAndGetRank(mWord, mImportance);
        
        res.no = currentRank;
        res.word = mWord;
        return res;
    }

    public Main.PAGE move(int mDir) {
        Main.PAGE res = new Main.PAGE();
        
        // 페이지 이동 (범위 체크는 문제 조건상 불필요하거나 호출부에서 보장됨을 가정)
        if (mDir < 0) currentRank--;
        else currentRank++;

        res.no = currentRank;
        // getWordIdByRank는 1-based index를 사용하므로 +1
        int wordId = trie.getWordIdByRank(currentRank + 1);
        res.word = wordDictionary[wordId];
        
        return res;
    }

    public Main.PAGE search(String mStr) {
        Main.PAGE res = new Main.PAGE();
        
        // 접두사 혹은 단어 검색
        int foundId = trie.searchBestWordId(mStr);
        
        if (foundId == -1) {
            res.no = -1;
            res.word = null; // or empty string depending on specs
        } else {
            // 찾은 단어의 순위 계산
            currentRank = trie.getRankByWordId(foundId);
            res.no = currentRank;
            res.word = wordDictionary[foundId];
        }
        return res;
    }

    public Main.PAGE go(int mNo) {
        Main.PAGE res = new Main.PAGE();
        
        currentRank = mNo; // 지정된 페이지로 이동
        res.no = currentRank;
        
        // 해당 순위의 단어 찾기
        int wordId = trie.getWordIdByRank(currentRank + 1);
        res.word = wordDictionary[wordId];
        
        return res;
    }
}