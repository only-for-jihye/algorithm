package 상품랭킹관리;

import java.io.*;
import java.util.*;

class Main {
	private final static int INIT = 100;
	private final static int ADD = 200;
	private final static int REMOVE = 300;
	private final static int PURCHASE = 400;
	private final static int TAKEBACK = 500;
	private final static int CHANGE_SCORE = 600;
	private final static int GET_TOP_RANK = 700;
	
	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;

	private static boolean run(BufferedReader br) throws Exception {
		boolean isCorrect = true;
		int cmd, ans, ret;
		int mGoodsID;
		int mCategory;
		int mScore;
		
		int n;
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		for (int q = 0; q<n; ++q) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {
			case INIT:
				userSolution.init();
	            break;
	        case ADD:
	        	mGoodsID = Integer.parseInt(st.nextToken());
	        	mCategory = Integer.parseInt(st.nextToken());
	        	mScore = Integer.parseInt(st.nextToken());
	            userSolution.add(mGoodsID, mCategory, mScore);
	            break;
	        case REMOVE:
	        	mGoodsID = Integer.parseInt(st.nextToken());
	        	userSolution.remove(mGoodsID);
	            break;
	        case PURCHASE:
	        	mGoodsID = Integer.parseInt(st.nextToken());
	        	userSolution.purchase(mGoodsID);
	            break;
	        case TAKEBACK:
	        	mGoodsID = Integer.parseInt(st.nextToken());
	        	userSolution.takeBack(mGoodsID);
	            break;
	        case CHANGE_SCORE:
	        	mCategory = Integer.parseInt(st.nextToken());
	        	mScore = Integer.parseInt(st.nextToken());
	        	userSolution.changeScore(mCategory,mScore);
	            break;
	        case GET_TOP_RANK:
	        	mCategory = Integer.parseInt(st.nextToken());
	            ret = userSolution.getTopRank(mCategory);
	            ans = Integer.parseInt(st.nextToken());
	            if (ans != ret)
	            	isCorrect = false;
	            break;
	        default:
	        	isCorrect = false;
	            break;
	        }
		}
		return isCorrect;
	}

	public static void main(String[] args) throws Exception {
		 System.setIn(new java.io.FileInputStream("SAMSUNG/상품랭킹관리/sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(st.nextToken());
		int MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
	}
}