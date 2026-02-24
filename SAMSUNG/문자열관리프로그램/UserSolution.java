package 문자열관리프로그램;

import java.util.*;

class UserSolution {
	
	/*
	 * 충분한 문자열 공간
	 * 초기 문자열 최대 30,000자
	 * 추가 횟수 30,000 X 최대 길이 4자 : 120,000자
	 * 정방향 : 120,000자 여유 공간 필요
	 * 반대방향 : 120,000자 여유 공간 필요
	 * 즉, 120,000 + 30,000 + 120,000 => 최소 270,000자 필요 // 여유있게 300,000 선언
	 */
    static final int MAX_LEN = 300000;
    
    char[] deque = new char[MAX_LEN];
    int head, tail;
    boolean isReversed;

    /*
     * 27진법 기준 최대 인덱스: 26*(27^3) + 26*(27^2) + 26*(27^1) + 26
     * 4글자로는 zzzz가 가장 마지막이며, z는 26이므로 26을 사용하여 계산
     * 총합 : 511,758 + 18,954 + 702 + 26 = 531,440
     */
    int[] fCount = new int[600000];
    int[] bCount = new int[600000];

    public void init(char[] mainStr) {
        // 배열 및 플래그 초기화
        Arrays.fill(fCount, 0);
        Arrays.fill(bCount, 0);
        isReversed = false;
        
        // 중앙에서 시작 (앞뒤로 늘어날 수 있도록)
        head = 150000;
        tail = head;

        for (char c : mainStr) {
            pushBack_internal(c);
        }
    }
    
    /*
     *    (데이터 없음)   [ 유효 데이터 ]   (데이터 없음)
... -----------------| A | B | C | D |---------------- ...
                       ↑              ↑
                      head           tail
     */

    // 실제 추가 로직 (방향에 따라 tail 혹은 head에 추가)
    private void pushBack_internal(char c) {
        deque[tail++] = c;
        // 새로 생긴 1~4자리 부분 문자열의 해시값 카운트 증가
        update(tail - 1, true, 1);
    }

    private void pushFront_internal(char c) {
        deque[--head] = c;
        update(head, false, 1);
    }

    // 삭제 로직
    private void popBack_internal() {
        update(--tail, true, -1);
    }

    private void popFront_internal() {
        update(head++, false, -1);
    }

    // 특정 위치에 글자가 추가/삭제될 때 영향을 받는 1~4자리 해시 업데이트
    private void update(int pos, boolean atTail, int val) {
        for (int len = 1; len <= 4; len++) {
            if (atTail) { // 뒤쪽에 추가/삭제 시
                if (pos - len + 1 < head) continue;
                fCount[getHash(pos - len + 1, len, false)] += val;
                bCount[getHash(pos - len + 1, len, true)] += val;
            } else { // 앞쪽에 추가/삭제 시
                if (pos + len > tail) continue;
                fCount[getHash(pos, len, false)] += val;
                bCount[getHash(pos, len, true)] += val;
            }
        }
    }

    // 27진법 해시 생성 함수
    private int getHash(int start, int len, boolean reverse) {
        int hash = 0;
        if (!reverse) {
            for (int i = 0; i < len; i++) {
                hash = hash * 27 + (deque[start + i] - 'a' + 1);
            }
        } else {
            for (int i = len - 1; i >= 0; i--) {
                hash = hash * 27 + (deque[start + i] - 'a' + 1);
            }
        }
//        System.out.println(hash);
        return hash;
    }

    public void pushBack(char[] newStr) {
        for (char c : newStr) {
            if (!isReversed) pushBack_internal(c);
            else pushFront_internal(c);
        }
    }

    public void popBack(int n) {
        for (int i = 0; i < n; i++) {
            if (!isReversed) popBack_internal();
            else popFront_internal();
        }
    }

    public void reverseStr() {
        isReversed = !isReversed;
    }

    public int getCount(char[] subStr) {
        int hash = 0;
        for (char c : subStr) {
            hash = hash * 27 + (c - 'a' + 1);
        }
        // 뒤집힌 상태면 bCount에서, 아니면 fCount에서 조회
        return isReversed ? bCount[hash] : fCount[hash];
    }
}