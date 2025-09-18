package pro_edu.pq.parking;

import java.io.*;
import java.util.*;

class Main {
	private final static int CMD_INIT = 1;
	private final static int CMD_ARRIVE = 2;
	private final static int CMD_LEAVE = 3;

	private final static UserSolution usersolution = new UserSolution();

	private static boolean run(BufferedReader br) throws Exception {
		int q = Integer.parseInt(br.readLine());

		int basetime, basefee, unittime, unitfee, capacity, mtime, mcar;
		int cmd, ans, ret = 0;
		boolean okay = false;

		for (int i = 0; i < q; ++i) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());
			switch (cmd) {
				case CMD_INIT:
					basetime = Integer.parseInt(st.nextToken());
					basefee = Integer.parseInt(st.nextToken());
					unittime = Integer.parseInt(st.nextToken());
					unitfee = Integer.parseInt(st.nextToken());
					capacity = Integer.parseInt(st.nextToken());
					usersolution.init(basetime, basefee, unittime, unitfee, capacity);
					okay = true;
					break;
				case CMD_ARRIVE:
					mtime = Integer.parseInt(st.nextToken());
					mcar = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					ret = usersolution.arrive(mtime, mcar);
					if (ret != ans)
						okay = false;
					break;
				case CMD_LEAVE:
					mtime = Integer.parseInt(st.nextToken());
					mcar = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					ret = usersolution.leave(mtime, mcar);
//					if (ret != ans)
//						okay = false;
//					break;
				default:
					okay = false;
					break;
			}
		}
		return okay;
	}

	public static void main(String[] args) throws Exception {
		int TC, MARK;

		//System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

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