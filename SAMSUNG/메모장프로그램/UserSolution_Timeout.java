package 메모장프로그램;

import java.io.*;
import java.util.*;

class UserSolution_Timeout {
	
	ArrayDeque<Character> leftAq; // 커서 기준 왼쪽
	ArrayDeque<Character> rightAq; // 커서 기준 오른쪽
	
	int H, W;
	int[] charCount; // 커서 기준 오른쪽의 count
  
	void init(int H, int W, char mStr[]) {
		leftAq = new ArrayDeque<>();
		rightAq = new ArrayDeque<>();
		this.H = H;
		this.W = W;
		charCount = new int[26];
		
		for (char c : mStr) {
			rightAq.addLast(c);
			int idx = getIdx(c);
			charCount[idx]++;
		}
		
		return; 
	}
	
	int getIdx(char c) {
		return c - 'a';
	}
	
	// 30,000
	void insert(char mChar) {
		leftAq.addLast(mChar);
//		rightAq.addFirst(mChar);
//		charCount[getIdx(mChar)]++;
		return;
	}

	// 30,000
	char moveCursor(int mRow, int mCol) {
		int targetIdx = (mRow - 1) * W + (mCol - 1);
		
		int totalSize = leftAq.size() + rightAq.size();
		if (targetIdx > totalSize) {
			targetIdx = totalSize;
		}
		
		int currentCursor = leftAq.size();
		
		if (targetIdx > currentCursor) {
			int move = targetIdx - currentCursor;
//			System.out.println("left move : " + move);
			for (int i = 0; i < move; i++) {
				// 오른쪽으로 움직임
				char c = rightAq.pollFirst();
				leftAq.addLast(c);
				charCount[getIdx(c)]--;
			}
		} else if (targetIdx < currentCursor) {
			int move = currentCursor - targetIdx;
//			System.out.println("right move : " + move);
			for (int i = 0; i < move; i++) {
				// 왼쪽으로 움직임
				char c = leftAq.pollLast();
				rightAq.addFirst(c);
				charCount[getIdx(c)]++;
			}
		}
		
		return rightAq.isEmpty() ? '$' : rightAq.peekFirst();
	}

	// 40,000
	int countCharacter(char mChar) {
		return charCount[getIdx(mChar)];
	}
}