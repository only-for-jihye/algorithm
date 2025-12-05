package 도로파괴;

import java.io.*;
import java.util.*;

class Main {
	private final static int MAX_K = 5000;
	private final static int CMD_INIT = 100;
	private final static int CMD_ADD = 200;
	private final static int CMD_REMOVE = 300;
	private final static int CMD_CALC = 400;

//	private final static UserSolution usersolution = new UserSolution();
//	private final static UserSolution2 usersolution = new UserSolution2();
	private final static UserSolution4 usersolution = new UserSolution4();

	private static boolean run(BufferedReader br) throws Exception {
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		int q = Integer.parseInt(st.nextToken());

		int n, k;
		int[] mIdArr = new int[MAX_K];
		int[] sCityArr = new int[MAX_K];
		int[] eCityArr = new int[MAX_K];
		int[] mTimeArr = new int[MAX_K];
		int mId, sCity, eCity, mTime;
		int cmd, ans, ret = 0;
		boolean okay = false;

		for (int i = 0; i < q; ++i) {
			st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken()); 
			switch (cmd) {
				case CMD_INIT:
					okay = true;
					n = Integer.parseInt(st.nextToken());
					k = Integer.parseInt(st.nextToken());
					for (int j = 0; j < k; ++j) {
						st = new StringTokenizer(br.readLine(), " "); 
						mIdArr[j] = Integer.parseInt(st.nextToken());
						sCityArr[j] = Integer.parseInt(st.nextToken());
						eCityArr[j] = Integer.parseInt(st.nextToken());
						mTimeArr[j] = Integer.parseInt(st.nextToken());
					}
					usersolution.init(n, k, mIdArr, sCityArr, eCityArr, mTimeArr);
					break;
				case CMD_ADD:
					mId = Integer.parseInt(st.nextToken());
					sCity = Integer.parseInt(st.nextToken());
					eCity = Integer.parseInt(st.nextToken());
					mTime = Integer.parseInt(st.nextToken());
					usersolution.add(mId, sCity, eCity, mTime);
					break;
				case CMD_REMOVE:
					mId = Integer.parseInt(st.nextToken());
					usersolution.remove(mId);
					break;
				case CMD_CALC:
					sCity = Integer.parseInt(st.nextToken());
					eCity = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					ret = usersolution.calculate(sCity, eCity);
					if (ret != ans)
						okay = false;
					break;
				default:
					okay = false;
					break;
			}
		}
		return okay;
	}

	public static void main(String[] args) throws Exception {
		int TC, MARK;

		System.setIn(new java.io.FileInputStream("SAMSUNG/도로파괴/sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " "); 
		
		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
	}
}