package pro_edu.editor;

import java.io.*;
import java.util.*;

class UserSolution {

    private ArrayDeque<Integer> first_digit_list;
    private ArrayDeque<Integer> second_digit_list;
    private int[] pre_counting_dat;

	public void init(int mCnt1, int[] mDigitList1, int mCnt2, int[] mDigitList2) {
        first_digit_list = new ArrayDeque<>();
        second_digit_list = new ArrayDeque<>();
        pre_counting_dat = new int[10000]; // 최대 4자리 수
        String mDigitStr = null;
        for (int i = 0; i < mCnt1; i++) {
            first_digit_list.add(mDigitList1[i]);
            mDigitStr.concat(mDigitList1[i]+"");
        }
        for (int i = 0; i < mCnt2; i++) {
            second_digit_list.add(mDigitList2[i]);
        }
        // String으로 바꿔야함

	}

	public void append(int mDir, int mNum1, int mNum2) {
		return;
	}

	public int countNum(int mNum) {
		return 0;
	}
}