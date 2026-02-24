package 메모장프로그램;

import java.io.*;
import java.util.*;

class UserSolution {
	
	ArrayList<Character>[] box;
	int W, H;
	int[][] charCount;
	int curR, curC;
  
	void init(int H, int W, char mStr[]) {
		this.H = H;
		this.W = W;
		this.curC = 0;
		this.curR = 0;
		box = new ArrayList[H];
		for (int i = 0; i < H; i++) {
			box[i] = new ArrayList<>();
		}
		charCount = new int[H][26];
		int row = 0;
		for (char c : mStr) {
			box[row].add(c);
			charCount[row][getIdx(c)]++;
			if (box[row].size() == W) {
				row++;
			}
		}
		return; 
	}
	
	int getIdx(char c) {
		return c - 'a';
	}
	
	void insert(char mChar) {
		box[curR].add(curC, mChar); // 커서 위치에 데이터 삽입
		charCount[curR][getIdx(mChar)]++;
		
		int nowR = curR;
		while (box[nowR].size() > W) { // W 사이즈 초과하면
			char c = box[nowR].remove(box[nowR].size() - 1); // 마지막 제거 
			charCount[nowR][getIdx(c)]--; // 현재 Row에서 카운트 줄이고
			charCount[++nowR][getIdx(c)]++; // 다음 Row에서 카운트 늘리고
			box[nowR].add(0, c); // 첫번째에 삽입
		}
		curC++; // 문자 추가했으니 커서 움직이고
		if (curC >= W) { // 커서가 또 W를 넘어가면
			curC = 0; // 첫번째로
			curR++;
		}
		
		return; 
	}

	char moveCursor(int mRow, int mCol) {
		// 0 based
		mRow--;
		mCol--;
		
		// 커서 보정 로직
		boolean isOutside = false;
		if (box[mRow].size() <= mCol) { // mRow 행에 문자 수가 mCol 보다 적을 때, 즉 문자를 넘어간 것임
			isOutside = true;
			while (mRow > 0 && box[mRow].isEmpty()) { // 문자가 있는 row가 나올 때까지
				mRow--; // 빼줌
			}
			mCol = box[mRow].size(); // 문자가 있는 row의 가장 끝에 커서 위치
		}
		
		curR = mRow;
		curC = mCol;
		char answer = '$';
		if (!isOutside) {
			answer = box[curR].get(curC);
		}
		
		return answer;
	}

	int countCharacter(char mChar) {
		int answer = 0;
		int col = 0;
		// 현재 커서가 위치한 행의 커서 다음부터 mChar이 있는지 카운트
		for (char c : box[curR]) {
			if (col >= curC && mChar == c) { // 커서의 위치부터 시작
				answer++;
			}
			col++; // 커서 위치 찾기 위해서 0부터 시작함, 향상된 for문이기 때문에~
		}
		for (int row = curR + 1; row < H; row++) {
			answer += charCount[row][getIdx(mChar)];
		}
		return answer;
	}
}