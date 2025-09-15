package pro_edu.editor;

import java.io.*;
import java.util.*;

class Main {
	private static final int CMD_INIT 		= 1; 
	private static final int CMD_APPEND 	= 2; 
	private static final int CMD_COUNT	 	= 3;

	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	
	private static int[] mDigitList1;
	private static int[] mDigitList2;
	private static String temp; 
	
	private static boolean run(BufferedReader br) throws Exception {

		int query_num;
		query_num = Integer.parseInt(br.readLine());
		
		int ans;
		boolean ok = false;

		for (int q = 0; q < query_num; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			int cmd= Integer.parseInt(st.nextToken());

			switch (cmd) {

			case CMD_INIT:
				st = new StringTokenizer(br.readLine());
				int mCnt1 = Integer.parseInt(st.nextToken());
				temp = st.nextToken();
				mDigitList1 = new int[mCnt1]; 
				for(int i = 0; i < mCnt1; i++) 
					mDigitList1[i] = temp.charAt(i) - '0'; 
				
				st = new StringTokenizer(br.readLine());
				int mCnt2 = Integer.parseInt(st.nextToken());
				temp = st.nextToken();
				mDigitList2 = new int[mCnt2]; 
				for(int i = 0; i < mCnt2; i++) 
					mDigitList2[i] = temp.charAt(i) - '0'; 
				
				userSolution.init(mCnt1, mDigitList1, mCnt2, mDigitList2);
				ok = true;
				break;
			case CMD_APPEND:
				int mDir = Integer.parseInt(st.nextToken());
				int mNum1 = Integer.parseInt(st.nextToken());
				int mNum2 = Integer.parseInt(st.nextToken());
				userSolution.append(mDir, mNum1, mNum2);
				break;
			case CMD_COUNT:
				int mNum = Integer.parseInt(st.nextToken());
				int ret = userSolution.countNum(mNum);
				ans = Integer.parseInt(st.nextToken());
				if(ans != ret)
					ok = false; 
				break;
			default:
				ok = false;
				break;
			}
		}
		return ok;
	}

	public static void main(String[] args) throws Exception {
		System.setIn(new java.io.FileInputStream("./sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
	}
}