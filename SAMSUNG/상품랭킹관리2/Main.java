package 상품랭킹관리2;

import java.io.*;
import java.util.*;

class Main {
	private static final int CMD_INIT 			= 100; 
	private static final int CMD_ADD	 		= 200; 
	private static final int CMD_REMOVE			= 300;
	private static final int CMD_CHANGE_RANK	= 400;
	private static final int CMD_SWAP_RANK		= 500;
	private static final int CMD_GET_TOP_RANKS	= 600;

	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	
	public static final class Result {
		int[] ids;
		Result() {
			ids = new int[3];
			for(int i = 0; i < 3; i++)
				ids[i] = -1;
		}
	}
	
	
	private static boolean run(BufferedReader br) throws Exception {

		int Q;
	    int mGoodsID, mCategory, mRank, mRank1, mRank2;
	    Result Ret = new Result();
	    Result Ans = new Result(); 
	    int ret, ans;
	    boolean okay = false;

		Q = Integer.parseInt(br.readLine());
		
		for (int q = 0; q < Q; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			int cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {

			case CMD_INIT:
				userSolution.init();
				okay = true;
				break;
			case CMD_ADD:
				mGoodsID = Integer.parseInt(st.nextToken());
				mCategory = Integer.parseInt(st.nextToken());
				mRank = Integer.parseInt(st.nextToken());
				ret = userSolution.add(mGoodsID, mCategory, mRank);
				ans = Integer.parseInt(st.nextToken());
				if(ret != ans)
					okay = false; 
				break;
			case CMD_REMOVE:
				mGoodsID = Integer.parseInt(st.nextToken());
				ret = userSolution.remove(mGoodsID);
				ans = Integer.parseInt(st.nextToken());
				if(ret != ans)
					okay = false;
				break;
			case CMD_CHANGE_RANK:
				mGoodsID = Integer.parseInt(st.nextToken());
				mRank = Integer.parseInt(st.nextToken());
				ret = userSolution.changeRank(mGoodsID, mRank);
				ans = Integer.parseInt(st.nextToken());
				if(ret != ans)
					okay = false;
				break;
			case CMD_SWAP_RANK:
				mCategory = Integer.parseInt(st.nextToken());
				mRank1 = Integer.parseInt(st.nextToken());
				mRank2 = Integer.parseInt(st.nextToken());
				ret = userSolution.swapRank(mCategory, mRank1, mRank2);
				ans = Integer.parseInt(st.nextToken());
				if(ret != ans)
					okay = false;
				break;
			case CMD_GET_TOP_RANKS:
				Ret = userSolution.getTopRanks();
				Ans.ids[0] = Integer.parseInt(st.nextToken());
				Ans.ids[1] = Integer.parseInt(st.nextToken());
				Ans.ids[2] = Integer.parseInt(st.nextToken());
				if(Ret.ids[0] != Ans.ids[0] || Ret.ids[1] != Ans.ids[1] || Ret.ids[2] != Ans.ids[2])
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
		 System.setIn(new java.io.FileInputStream("SAMSUNG/상품랭킹관리2/sample_input.txt"));

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