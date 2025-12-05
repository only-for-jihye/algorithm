package 박테리아;

import java.io.*;
import java.util.*;

public class Main {
	private static final int CMD_INIT	 	= 0;
	private static final int CMD_ADD 		= 1;
	private static final int CMD_MINSPAN	= 2;
	private static final int CMD_GET   		= 3; 

//	private static UserSolution userSolution = new UserSolution();
//	private static UserSolution2 userSolution = new UserSolution2();
	private static UserSolution_Teaching userSolution = new UserSolution_Teaching();
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;

	public static boolean run(BufferedReader br) throws IOException {
		int Q, time, id, minSpan, maxSpan, halftime;
		int ret, ans; 
		boolean ok = false;
		st = new StringTokenizer(br.readLine());
		Q = Integer.parseInt(st.nextToken());

		for (int q = 0; q < Q; q++) {
			st = new StringTokenizer(br.readLine());
			int cmd = Integer.parseInt(st.nextToken());
			switch (cmd) {
			case CMD_INIT:
				userSolution.init();
				ok = true;
				break; 
			case CMD_ADD:
				time = Integer.parseInt(st.nextToken());
				id = Integer.parseInt(st.nextToken());
				maxSpan = Integer.parseInt(st.nextToken());
				halftime = Integer.parseInt(st.nextToken());
				userSolution.addBacteria(time, id, maxSpan, halftime); 
				break; 
			case CMD_MINSPAN:
				time = Integer.parseInt(st.nextToken());
				ret = userSolution.getMinLifeSpan(time);
				ans = Integer.parseInt(st.nextToken());
				if(ans != ret)
					ok = false;
				break; 
			case CMD_GET:
				time = Integer.parseInt(st.nextToken());
				minSpan = Integer.parseInt(st.nextToken());
				maxSpan = Integer.parseInt(st.nextToken());
				ret = userSolution.getCount(time, minSpan, maxSpan);
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

	public static void main(String[] args) throws IOException {
		System.setIn(new java.io.FileInputStream("SAMSUNG/Bacteria/input.txt"));
		br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		int TC = Integer.parseInt(st.nextToken());
		int MARK = Integer.parseInt(st.nextToken());

		for (int tc = 1; tc <= TC; ++tc) {
			boolean result = run(br);
			int score = result ? MARK : 0;
			System.out.println("#" + tc + " " + score);
		}
	}
}