package pro_edu.unionFind.china;

import java.io.*;
import java.util.*;

class Main
{
	private final static int CMD_INIT 		= 1;
	private final static int CMD_ALLIANCE	= 2;
	private final static int CMD_WAR		= 3;
	private final static int CMD_GET_CNT  = 4;
	private final static int CMD_GET_POPCNT = 5;

	private final static UserSolution usersolution = new UserSolution();

	private final static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static boolean run() throws Exception
	{
		StringTokenizer st;

		int numQuery;
		int userAns, ans;
		char A, B; 
		boolean isCorrect = false;

		numQuery = Integer.parseInt(br.readLine());

		for (int q = 0; q < numQuery; ++q)
		{
			st = new StringTokenizer(br.readLine());

			int cmd;
			cmd = Integer.parseInt(st.nextToken());

			switch (cmd)
			{
				case CMD_INIT:
					int N = Integer.parseInt(st.nextToken());
					int[] countries = new int[N];
					for(int i = 0; i < N; i++)
						countries[i] = Integer.parseInt(st.nextToken());
					usersolution.init(N, countries);
					isCorrect = true;
					break;
				case CMD_ALLIANCE:
					A = st.nextToken().charAt(0); 
					B = st.nextToken().charAt(0); 
					userAns = usersolution.alliance(A, B);
					ans = Integer.parseInt(st.nextToken());
					if (userAns != ans)
						isCorrect = false;
					break;
				case CMD_WAR:
					A = st.nextToken().charAt(0); 
					B = st.nextToken().charAt(0); 
					userAns = usersolution.war(A, B);
					ans = Integer.parseInt(st.nextToken());
					if (userAns != ans)
						isCorrect = false;
					break;
				case CMD_GET_CNT:
					userAns = usersolution.getCnt(); 
					ans = Integer.parseInt(st.nextToken());
					if (userAns != ans)
						isCorrect = false;
					break;
				case CMD_GET_POPCNT:
					A = st.nextToken().charAt(0); 
					userAns = usersolution.getPopCnt(A); 
					ans = Integer.parseInt(st.nextToken());
					if (userAns != ans) {
//						System.out.println(A + " userAns : " + userAns + ", ans :" + ans);
						isCorrect = false;
					}
					break;
				default:
					isCorrect = false;
					break;
			}
		}

		return isCorrect;
	}

	public static void main(String[] args) throws Exception
	{
		int TC, MARK;

		//System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

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