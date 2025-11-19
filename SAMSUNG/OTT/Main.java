package OTT;

import java.io.*;
import java.util.*;

class Main {
	private static final int CMD_INIT 		= 100; 
	private static final int CMD_ADD 		= 200; 
	private static final int CMD_ERASE 		= 300;
	private static final int CMD_WATCH 		= 400;
	private static final int CMD_SUGGEST 	= 500;

//	private static UserSolution userSolution = new UserSolution();
	private static UserSolution2 userSolution = new UserSolution2();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	
	public static class RESULT {
		int cnt;
		int[] IDs;
		RESULT() {
			this.cnt = 0;
			this.IDs = new int[5]; 
		}
	}
	
	private static boolean run(BufferedReader br) throws Exception {

		int Q, N;
		int mID, mGenre, mTotal, mRating, uID;
		int ret = -1;
		int cnt, ans;
		RESULT res = new RESULT(); 
		
		st = new StringTokenizer(br.readLine(), " ");
		Q = Integer.parseInt(st.nextToken());
		
		boolean okay = false;

		for (int q = 0; q < Q; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			int cmd= Integer.parseInt(st.nextToken());

			switch (cmd) {

			case CMD_INIT:
				N = Integer.parseInt(st.nextToken());
				userSolution.init(N);
				okay = true;
				break;
			case CMD_ADD:
				mID = Integer.parseInt(st.nextToken());
				mGenre = Integer.parseInt(st.nextToken());
				mTotal = Integer.parseInt(st.nextToken());
				ret = userSolution.add(mID, mGenre, mTotal);
				ans = Integer.parseInt(st.nextToken());
				if(ret != ans)
					okay = false;
				break;
			case CMD_ERASE:
				mID = Integer.parseInt(st.nextToken());
				ret = userSolution.erase(mID);
				ans = Integer.parseInt(st.nextToken());
				if(ret != ans)
					okay = false;
				break;
			case CMD_WATCH:
				uID = Integer.parseInt(st.nextToken());
				mID = Integer.parseInt(st.nextToken());
				mRating = Integer.parseInt(st.nextToken());
				ret = userSolution.watch(uID, mID, mRating);
				ans = Integer.parseInt(st.nextToken());
				if(ret != ans)
					okay = false;
				break;
			case CMD_SUGGEST:
				uID = Integer.parseInt(st.nextToken());
				res = userSolution.suggest(uID);
				cnt = Integer.parseInt(st.nextToken());
				if(res.cnt != cnt)
					okay = false;
				for(int i = 0; i < cnt; ++i) {
					ans = Integer.parseInt(st.nextToken());
					if(res.IDs[i] != ans)
						okay = false; 
				}
				break;
			default:
				okay = false;
				break;
			}
		}
		return okay;
	}

	public static void main(String[] args) throws Exception {
		 System.setIn(new java.io.FileInputStream("SAMSUNG/OTT/sample_input.txt"));

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