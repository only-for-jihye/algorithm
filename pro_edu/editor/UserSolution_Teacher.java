package pro_edu.editor;

import java.util.ArrayDeque;
import java.util.*;

public class UserSolution_Teacher {
    int DAT[]; // index : 3자리 수 조합, value : 해당 수 조합의 출현 횟수
	ArrayDeque<Integer> FirstList;
	ArrayDeque<Integer> SecondList;
	
	public void init(int mCnt1, int[] mDigitList1, int mCnt2, int[] mDigitList2) {
		DAT = new int[1000];
		FirstList = new ArrayDeque<>();
		SecondList = new ArrayDeque<>();
		
		for(int i = 0; i < mCnt1; i++)
			update(mDigitList1[i], 1, FirstList);
		for(int i = 0; i < mCnt2; i++)
			update(mDigitList2[i], 1, SecondList);
	}
	
	void update(int num, int dir, ArrayDeque<Integer> ad) { 
		// ad에 dir방향으로 num이라는 수를 추가! <- num은 4자리 수 이하
		if(dir == 0) {
			updateForward(num, ad);
		}
		else {
			updateBackward(num, ad);
		}
	}
	
	void updateForward(int num, ArrayDeque<Integer> ad) { // 정방향으로 숫자를 추가
		while(num > 0) { // num = 1234 => 4->3->2->1순으로 추가
			ad.addFirst(num % 10); // <- 1의 자리 추출
			num /= 10; // <- 1의 자리 삭제
			
			if(ad.size() < 3) continue;
			
			Iterator<Integer> it = ad.iterator();
			int idx = it.next() * 100 + it.next() * 10 + it.next();
			DAT[idx]++;
		}
	}
	
	void updateBackward(int num, ArrayDeque<Integer> ad) {
		// num = 1234 => 1->2->3->4순으로 추가
		int len = findLen(num);
		int d = pow(len);
		while(num > 0) {
			ad.addLast(num / d);
			num %= d;
			d /= 10;
			
			if(ad.size() < 3) continue;
			
			Iterator<Integer> it = ad.descendingIterator();
			int idx = it.next() + it.next() * 10 + it.next() * 100;
			DAT[idx]++;
		}
	}
	
	int pow(int len) {
		int d = 1;
		for (int i = 0; i < len - 1; i++)
			d *= 10;
		return d;
	}
	
	int findLen(int num) { // 몇 자리 수인가?
		int cnt = 0;
		while(num > 0) {
			num /= 10;
			cnt += 1;
		}
		return cnt;
	}

	public void append(int mDir, int mNum1, int mNum2) {
		update(mNum1, mDir, FirstList);
		update(mNum2, mDir, SecondList);
	}

	public int countNum(int mNum) {
		int cnt = DAT[mNum];
		
		// FirstList와 SecondList를 붙였을때 나올 수 있는 조합 2가지
		
		// 1. FirstList 맨 뒤 1개, SecondList 맨 앞 2개
		Iterator<Integer> it = SecondList.iterator();
		int num = FirstList.getLast() * 100 + it.next() * 10 + it.next();
		if(num == mNum) cnt++;
		
		// 2. FirstList 맨 뒤 2개, SecondList 맨 앞 1개
		// FirstList 123456    SecondList 123456 => 561
		it = FirstList.descendingIterator();
		num = SecondList.getFirst() + it.next() * 10 + it.next() * 100;
		if(num == mNum) cnt++;
		
		return cnt;
	}
}
