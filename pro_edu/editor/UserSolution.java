package pro_edu.editor;

import java.io.*;
import java.util.*;

class UserSolution {
	
	private ArrayDeque<Integer> firstList;
	private ArrayDeque<Integer> secondList;
	private int[] counts;

	public void init(int mCnt1, int[] mDigitList1, int mCnt2, int[] mDigitList2) {
		firstList = new ArrayDeque<>();
		secondList = new ArrayDeque<>();
		counts = new int[1000];
//		firstList.clear();
//		secondList.clear();
		
        for (int i = 0; i < mCnt1; i++) {
        	update(mDigitList1[i], 1, firstList); // 역방향으로 삽입
        }
        for (int i = 0; i < mCnt2; i++) {
        	update(mDigitList2[i], 1, secondList);
        }
	}
	
	void update(int num, int dir, ArrayDeque<Integer> ad) {
		// ad에 dir 방향으로 num이라는 수를 추가
		if (dir == 0) {
			updateForward(num, ad);
		} else {
			updateBackward(num, ad);
		}
	}
	
	void updateForward(int num, ArrayDeque<Integer> ad) { // 정방향으로 숫자를 추가
		while (num > 0) { // num = 1234 => 4 -> 3 -> 2 -> 1
			ad.addFirst(num % 10); // <- 1의 자리 추출
			num /= 10; // <- 1의 자리 삭제
			
			if (ad.size() < 3) continue; // 3자리 이상 되어야 조합한다.
			
			Iterator<Integer> it = ad.iterator();
			int idx = it.next() * 100 + it.next() * 10 + it.next();
			counts[idx]++;
		}
	}
	
	void updateBackward(int num, ArrayDeque<Integer> ad) {
		int len = findLen(num);
		int d = pow(len);
		while (num > 0) { // num = 1234 => 1 -> 2 -> 3 -> 4
			ad.addLast(num / d);
			num %= d; // 지우고
			d /= 10; // 1000 ~ 10의 자리까지
			
			if (ad.size() < 3) continue; // 3자리 미만이면 무시
			
			Iterator<Integer> it = ad.descendingIterator();
			int idx = it.next() + it.next() * 10 + it.next() + 100;
			counts[idx]++;
		}
	}
	
	int pow(int len) {
		int d = 1;
		for (int i = 0; i < len - 1; i++) {
			d *= 10;
		}
		return d;
	}
	
	int findLen(int num) { // 몇 자리 수 인가?
		int cnt = 0;
		while (num > 0) {
			num /= 10;
			cnt += 1;
		}
		return cnt;
	}

	public void append(int mDir, int mNum1, int mNum2) {
		update(mNum1, mDir, firstList);
		update(mNum2, mDir, secondList);
	}

	public int countNum(int mNum) {
		int cnt = counts[mNum];
		// FirstList와 SecondList를 붙였을 때 나올 수 있는 조합 2가지
		// 1. FirstList 맨 뒤 1개 + SecondList 맨 앞 2개
		Iterator<Integer> it = secondList.iterator();
		int num = firstList.getLast() * 100 + it.next() * 10 + it.next();
		if(num == mNum) cnt++;
		// 2. FirstList 맨 뒤 2개 + SecondList 맨 앞 1개
		// F 123456 + S 123456 => 561 => 56 1
		it = firstList.descendingIterator();
		num = it.next() * 10 + it.next() * 100 + secondList.getFirst();
		if(num == mNum) cnt++;
//		System.out.println(mNum + " : " + counts[mNum]);
		return cnt;
	}
}