package A_practice;

class TrieNormal {
    
    // C++의 #define 및 전역 변수 대응
    static final int MAX_WORD_COUNT = 70000;
    static final int MAX_LEN = 10;

    // 단어를 저장할 배열 (1-based index 사용을 위해 크기 넉넉히)
    static char[][] g_words = new char[MAX_WORD_COUNT + 1][MAX_LEN];
    static int g_idSeq = 0;

    // Node 클래스 (Inner Class)
    static class Node {
        int index;          // 단어의 고유 ID (단어 끝 지점에만 기록, 없으면 -1)
        int[] count;        // 해당 노드에서 각 알파벳(a-z)으로 시작하는 하위 단어들의 개수
        Node[] childs;      // 자식 노드 참조

        public Node() {
            this.index = -1;
            this.count = new int[26];
            this.childs = new Node[26];
            // 자바는 배열 생성 시 0/null로 자동 초기화되므로 루프 불필요
        }
    }

    // Trie 클래스
    static class Trie {
        Node root;

        public Trie() {
            root = new Node();
        }

        // 단어 삽입 및 사전 순서 반환
        // 반환값: 삽입된 단어가 현재 Trie 내에서 사전 순으로 몇 번째인지 (1-based)
        public int insert(char[] str) {
            // 1. 단어 저장소에 저장 (ID 증가)
            g_idSeq++;
            // str 내용을 g_words[g_idSeq]에 복사
            for (int i = 0; i < str.length; i++) {
                g_words[g_idSeq][i] = str[i];
            }
            // 문자열 끝 처리를 위해 길이 이후는 0(null char) 처리 (필요시)
            if (str.length < MAX_LEN) {
                g_words[g_idSeq][str.length] = 0; 
            }

            Node cur = root;
            int count = 0; // 내 앞에 있는 단어의 수

            for (int i = 0; i < str.length; i++) {
                int ch = str[i] - 'a';

                // 자식 노드가 없으면 생성
                if (cur.childs[ch] == null) {
                    cur.childs[ch] = new Node();
                }

                // 해당 자식(ch)으로 내려가는 단어 수 증가
                cur.count[ch]++;

                // *핵심 로직*: 사전 순서를 계산하기 위해 내 앞의 알파벳 가지들의 count 합산
                // 현재 깊이에서 'ch'보다 작은 알파벳(0 ~ ch-1) 쪽으로 간 단어들은 무조건 나보다 앞섬
                for (int j = 0; j < ch; j++) {
                    count += cur.count[j]; 
                    // 원본 코드: count += cur->count[ch]; 라고 되어 있었으나, 
                    // 문맥상 for(j<ch) { count += cur->count[j]; } 가 일반적인 Rank 로직입니다.
                    // **하지만**, 원본 C++ 코드가 `count += cur->count[ch]`로 되어 있다면
                    // 이는 로직 오류일 가능성이 높거나 특수한 의도입니다.
                    // 보통은 `count += cur.count[j]`가 맞습니다. 
                    // (작성해주신 C++ 코드의 `count += cur->count[ch]` 부분은 오타 가능성이 매우 높아 보입니다.
                    //  Rank를 구하려면 내 앞쪽(j) 가지들의 합을 더해야 합니다. 
                    //  따라서 아래 코드는 `cur.count[j]`로 수정하여 작성합니다.)
                    // *수정*: count += cur.count[j];
                }

                // 현재 노드 자체가 단어의 끝인 경우(짧은 단어가 접두어로 존재할 때) 나보다 앞섬
                if (cur.index != -1) {
                    count++;
                }

                cur = cur.childs[ch];
            }
            
            // 마지막 단어 끝 표시
            cur.index = g_idSeq;
            
            return count + 1; // 0-based count에 내 자신(+1)을 포함하여 리턴
        }
        
        // 오버로딩: String 입력 편의 제공
        public int insert(String str) {
            return insert(str.toCharArray());
        }

        // k번째(index) 단어 찾기
        // 반환값: 해당 순위 단어의 고유 ID (g_words 인덱스)
        public int move(int k) {
            Node cur = root;
            int index = k; // index 변수 재사용 (찾고자 하는 남은 순위)

            while (true) {
                // 현재 노드가 단어의 끝이라면, 이 단어가 순위에 포함됨
                if (cur.index != -1) {
                    index--; 
                    // 찾던 순위가 0이 되면 현재 단어가 바로 그 단어
                    if (index == 0) return cur.index;
                }

                // 자식 노드 순회 (a -> z 순서)
                for (int i = 0; i < 26; i++) {
                    // 갈 수 없는 가지(null)이거나 count가 0이면 패스
                    if (cur.childs[i] == null || cur.count[i] == 0) continue;

                    // 만약 현재 가지(i)에 포함된 단어 수가 찾는 순위(index)보다 많거나 같다면
                    // 정답은 이 가지 안에 있음 -> 내려감
                    if (index <= cur.count[i]) {
                        cur = cur.childs[i];
                        break; // for문 탈출, while문 계속
                    }
                    
                    // 이 가지가 아니라면, 이 가지에 있는 단어 수만큼 순위 차감하고 다음 알파벳으로
                    index -= cur.count[i];
                }
            }
        }

        // 특정 문자열 검색
        public int search(char[] str) {
            Node cur = root;
            for (int i = 0; i < str.length; i++) {
                int ch = str[i] - 'a';
                if (cur.childs[ch] == null) return -1; // 없음
                cur = cur.childs[ch];
            }
            return cur.index; // 단어 ID 리턴 (없으면 -1일 것임)
        }
    }
    
    // 초기화 함수 (문제 형식에 맞춤)
    public void init() {
        g_idSeq = 0;
        // g_words 초기화는 굳이 안 해도 덮어쓰므로 생략 가능하나 필요시 수행
    }
}