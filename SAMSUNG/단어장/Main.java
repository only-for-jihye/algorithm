package 단어장;

//Main.java

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Main
{
	private static final int CMD_INIT           = 100;
	private static final int CMD_ADD	        = 200;
	private static final int CMD_MOVE		    = 300;
	private static final int CMD_SEARCH			= 400;
	private static final int CMD_GO				= 500;

	private static UserSolution usersolution = new UserSolution();

	public static class PAGE
	{
		int no;
		String word;

		PAGE()
		{
			no = -1;
		}
	}

	private static boolean run(BufferedReader br) throws Exception
	{
		int Q;
		String mWord, mStr;
		int mImportance, mDir, mNo;

		PAGE res;

		int ret = -1, no;
		String ans;

		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		Q = Integer.parseInt(st.nextToken());

		boolean okay = false;

		for (int q = 0; q < Q; ++q)
		{
			st = new StringTokenizer(br.readLine(), " ");
			int cmd = Integer.parseInt(st.nextToken());

			switch(cmd)
			{
			case CMD_INIT:
				usersolution.init();
				okay = true;
				break;
			case CMD_ADD:
				mWord = st.nextToken();
				mImportance = Integer.parseInt(st.nextToken());
				ans = mWord;
				res = usersolution.add(mWord, mImportance);
				no = Integer.parseInt(st.nextToken());
				if (res.no != no || !ans.equals(res.word))
					okay = false;
				break;
			case CMD_MOVE:
				mDir = Integer.parseInt(st.nextToken());
				res = usersolution.move(mDir);
				no = Integer.parseInt(st.nextToken());
				ans = st.nextToken();
				if (res.no != no || !ans.equals(res.word))
					okay = false;
				break;
			case CMD_SEARCH:
				mStr = st.nextToken();
				res = usersolution.search(mStr);
				no = Integer.parseInt(st.nextToken());
				if (res.no != no)
					okay = false;
				if (no != -1)
				{
					ans = st.nextToken();
					if (!ans.equals(res.word))
						okay = false;
				}
				break;
			case CMD_GO:
				mNo = Integer.parseInt(st.nextToken());
				res = usersolution.go(mNo);
				ans = st.nextToken();
				if (res.no != mNo || !ans.equals(res.word))
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
		//System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(st.nextToken());
		int MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase)
		{
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
	}
}