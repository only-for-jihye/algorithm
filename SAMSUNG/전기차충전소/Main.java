package 전기차충전소;

import java.io.*;
import java.util.*;

class Main
{
	private final static int CMD_INIT 					= 100;
	private final static int CMD_ADD		 			= 200;
	private final static int CMD_REMOVE 				= 300;
	private final static int CMD_COST 				 	= 400;
//	private final static UserSolution usersolution = new UserSolution();
	private final static UserSolution4 usersolution = new UserSolution4();
	
	private final static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static boolean run() throws Exception
	{
		StringTokenizer st;
		
		int q, n, k; 
		String strTmp; 
		int[] mCostArr = new int[300];
		int[] mIdArr = new int[2000];
		int[] sCityArr = new int[2000];
		int[] eCityArr = new int[2000];
		int[] mDistArr = new int[2000]; 
		int mId, sCity, eCity, mDist;
		int cmd, ans, ret;
		boolean okay = false;
		
		st = new StringTokenizer(br.readLine());
		q = Integer.parseInt(st.nextToken());

		for (int i = 0; i < q; ++i)
		{
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());
			strTmp = st.nextToken(); 

			switch (cmd)
			{
			case CMD_INIT:
				okay = true; 
				st = new StringTokenizer(br.readLine());
				strTmp = st.nextToken();
				n = Integer.parseInt(st.nextToken());
				strTmp = st.nextToken();
				k = Integer.parseInt(st.nextToken());
				for(int j = 0; j < n; ++j) {
					st = new StringTokenizer(br.readLine());
					strTmp = st.nextToken();
					mCostArr[j] = Integer.parseInt(st.nextToken());
				}
				for(int j = 0; j < k; ++j) {
					st = new StringTokenizer(br.readLine());
					strTmp = st.nextToken(); 
					mIdArr[j] = Integer.parseInt(st.nextToken());
					strTmp = st.nextToken(); 
					sCityArr[j] = Integer.parseInt(st.nextToken());
					strTmp = st.nextToken();
					eCityArr[j] = Integer.parseInt(st.nextToken());
					strTmp = st.nextToken();
					mDistArr[j] = Integer.parseInt(st.nextToken()); 
				}
				usersolution.init(n, mCostArr, k, mIdArr, sCityArr, eCityArr, mDistArr);
				break;
			case CMD_ADD :
				st = new StringTokenizer(br.readLine());
				strTmp = st.nextToken();
				mId = Integer.parseInt(st.nextToken());
				strTmp = st.nextToken();
				sCity = Integer.parseInt(st.nextToken());
				strTmp = st.nextToken();
				eCity = Integer.parseInt(st.nextToken());
				strTmp = st.nextToken();
				mDist = Integer.parseInt(st.nextToken());
				usersolution.add(mId, sCity, eCity, mDist);
				break;
			case CMD_REMOVE:
				st = new StringTokenizer(br.readLine());
				strTmp = st.nextToken();
				mId = Integer.parseInt(st.nextToken());
				usersolution.remove(mId);
				break;
			case CMD_COST:
				st = new StringTokenizer(br.readLine());
				strTmp = st.nextToken();
				sCity = Integer.parseInt(st.nextToken());
				strTmp = st.nextToken();
				eCity = Integer.parseInt(st.nextToken());
				st = new StringTokenizer(br.readLine());
				strTmp = st.nextToken();
				ans = Integer.parseInt(st.nextToken());
				ret = usersolution.cost(sCity, eCity);
				if(ans != ret)
					okay = false;
				break; 
			default:
				okay = false; 
				break;
			}
		}

		return okay; 
	}

	public static void main(String[] args) throws Exception
	{
		int TC, MARK;

		System.setIn(new java.io.FileInputStream("SAMSUNG/전기차충전소/sample_input.txt"));

		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase)
		{
			int score = run() ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
	}
}