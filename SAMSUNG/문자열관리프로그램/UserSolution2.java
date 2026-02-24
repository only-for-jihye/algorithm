package 문자열관리프로그램;

import java.util.*;

class UserSolution2 {

	char[] deque = new char[300000];
	int head;
	int tail;
	boolean isReversed;
	
	int[] frontCount = new int[600000];
	int[] backCount = new int[600000];
	
    public void init(char[] mainStr) {
    	Arrays.fill(frontCount, 0);
    	Arrays.fill(backCount, 0);
    	isReversed = false;
    	head = 150000;
    	tail = head;
    	
    	for (char c : mainStr) {
    		pushBack_internal(c);
    	}
    }
    

    private void pushBack_internal(char c) {
		deque[tail++] = c;
		update(tail - 1, true, 1);
	}
    private void pushFront_internal(char c) {
    	deque[--head] = c;
    	update(head, false, 1);
    }
    private void popBack_internal() {
    	update(--tail, true, -1);
    }
    private void popFront_internal() {
    	update(head++, false, -1);
    }


	private void update(int pos, boolean isTail, int val) {
		for (int len = 1; len <= 4; len++) {
			if (isTail) {
				if (pos - len + 1 < head) continue; // 포인터가 head를 넘어가서는 안됨
				frontCount[getHash(pos - len + 1, len, false)] += val;
				backCount[getHash(pos - len + 1, len, true)] += val;
			} else {
				if (pos + len > tail) continue;
				frontCount[getHash(pos, len, false)] += val;
				backCount[getHash(pos, len, true)] += val;
			}
		}
	}
	
	private int getHash(int start, int len, boolean reversed) {
		int hash = 0;
		if (!reversed) {
			for (int i = 0; i < len; i++) {
				hash = hash * 27 + (deque[start + i] - 'a' + 1);
			}
		} else {
			for (int i = len -1; i >= 0; i--) {
				hash = hash * 27 + (deque[start + i] - 'a' + 1);
			}
		}
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
    	return isReversed ? backCount[hash] : frontCount[hash];
    }
}