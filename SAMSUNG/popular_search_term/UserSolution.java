package popular_search_term;

import java.io.*;
import java.util.*; 

class UserSolution {

    // 전역 변수 및 자료구조
    private int N; // 윈도우 크기
    private String[] window; // 순차적으로 들어오는 단어 저장 (Circular Buffer 역할)
    private int currentIdx; // 현재 윈도우에 추가될 인덱스
    private int currentSize; // 현재 윈도우에 차있는 개수
    private Map<String, Integer> countMap; // 단어별 빈도수 저장

    // 그룹 정보를 담을 클래스
    private static class Group implements Comparable<Group> {
        String representative; // 대표 검색어
        int totalCount; // 그룹 내 단어들의 총 빈도수 합

        public Group(String representative, int totalCount) {
            this.representative = representative;
            this.totalCount = totalCount;
        }

        // 문제의 순위 선정 규칙에 따른 정렬 기준
        // 1. 총 빈도수 내림차순
        // 2. 대표 검색어 사전순 오름차순
        @Override
        public int compareTo(Group o) {
            if (this.totalCount != o.totalCount) {
                return Integer.compare(o.totalCount, this.totalCount);
            }
            return this.representative.compareTo(o.representative);
        }
    }

    public void init(int N) {
        this.N = N;
        this.window = new String[N];
        this.currentIdx = 0;
        this.currentSize = 0;
        this.countMap = new HashMap<>();
    }

    public void addKeyword(String mKeyword) {
        // 1. 윈도우가 꽉 찼다면 가장 오래된 단어 제거
        if (currentSize == N) {
            String oldKeyword = window[currentIdx];
            int cnt = countMap.get(oldKeyword);
            if (cnt == 1) {
                countMap.remove(oldKeyword);
            } else {
                countMap.put(oldKeyword, cnt - 1);
            }
        } else {
            currentSize++;
        }

        // 2. 새로운 단어 추가
        window[currentIdx] = mKeyword;
        countMap.put(mKeyword, countMap.getOrDefault(mKeyword, 0) + 1);

        // 3. 인덱스 이동 (Circular)
        currentIdx = (currentIdx + 1) % N;
    }

    public int top5Keyword(String[] mRet) {
        // 현재 존재하는 유니크한 단어들 리스트화
        List<String> uniqueWords = new ArrayList<>(countMap.keySet());
        int uniqueSize = uniqueWords.size();
        
        boolean[] visited = new boolean[uniqueSize];
        List<Group> groups = new ArrayList<>();

        // 유니크 단어들을 대상으로 그룹핑 (Connected Components 찾기)
        for (int i = 0; i < uniqueSize; i++) {
            if (visited[i]) continue;

            // 새로운 그룹 발견 -> BFS 시작
            visited[i] = true;
            Queue<Integer> queue = new LinkedList<>();
            queue.add(i);

            int groupTotalCount = 0;
            String bestWord = null;
            int maxFreqInGroup = -1;

            while (!queue.isEmpty()) {
                int currIdx = queue.poll();
                String currWord = uniqueWords.get(currIdx);
                int currFreq = countMap.get(currWord);

                // 그룹 통계 업데이트 (총 빈도수)
                groupTotalCount += currFreq;

                // 대표 검색어 갱신 로직
                // 1. 빈도수가 더 크면 교체
                // 2. 빈도수가 같으면 사전순으로 더 작으면 교체
                if (bestWord == null) {
                    bestWord = currWord;
                    maxFreqInGroup = currFreq;
                } else {
                    if (currFreq > maxFreqInGroup) {
                        bestWord = currWord;
                        maxFreqInGroup = currFreq;
                    } else if (currFreq == maxFreqInGroup) {
                        if (currWord.compareTo(bestWord) < 0) {
                            bestWord = currWord;
                        }
                    }
                }

                // 인접한(유사한) 단어 탐색
                for (int next = 0; next < uniqueSize; next++) {
                    if (!visited[next]) {
                        if (isSimilar(currWord, uniqueWords.get(next))) {
                            visited[next] = true;
                            queue.add(next);
                        }
                    }
                }
            }
            groups.add(new Group(bestWord, groupTotalCount));
        }

        // 그룹들을 우선순위에 따라 정렬
        Collections.sort(groups);

        // 상위 5개 (또는 그 이하) 결과 담기
        int resultCount = Math.min(5, groups.size());
        for (int i = 0; i < resultCount; i++) {
            mRet[i] = groups.get(i).representative;
        }

        return resultCount;
    }

    // 두 단어가 유사한지 판단 (길이가 같고, 다른 문자가 딱 1개)
    private boolean isSimilar(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        
        int diffCount = 0;
        int len = s1.length();
        
        for (int i = 0; i < len; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                diffCount++;
                if (diffCount > 1) return false; // 가지치기
            }
        }
        return diffCount == 1;
    }
}