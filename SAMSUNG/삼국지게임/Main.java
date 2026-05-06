package 삼국지게임;

import java.io.*;
import java.util.*;

public class Main {
	private static final int CMD_INIT 		= 100;
	private static final int CMD_DESTROY 	= 200;
	private static final int CMD_ALLY 		= 300;
	private static final int CMD_ATTACK 	= 400;
	private static final int CMD_RECRUIT 	= 500;

	private static UserSolution userSolution = new UserSolution();
	private final static int MAX_N = 25;
	private final static int MAX_L = 10; 

	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	
	static int[][] soldier = new int[MAX_N][MAX_N];
	static char[][][] generalList = new char[MAX_N][MAX_N][MAX_L+1]; 

	public static boolean run(BufferedReader br) throws IOException {
		
		boolean isOK = false;
		int Q, cmd, result, check, N; 
		char[] monarchA = new char[11];
		char[] monarchB = new char[11];
		char[] general = new char[11]; 
		int sign;
		int num; 
		
		Q = Integer.parseInt(br.readLine());

		for (int q = 0; q < Q; q++) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());
			
			switch (cmd) {
			case CMD_INIT:
				N = Integer.parseInt(st.nextToken());
				for(int j = 0; j < N; j++) 
					for(int i = 0; i < N; i++) 
						soldier[j][i] = Integer.parseInt(st.nextToken());
				for(int j = 0; j < N; j++) {
					for(int i = 0; i < N; i++) {
						generalList[j][i] = st.nextToken().toCharArray();
					}
				}
				userSolution.init(N, soldier, generalList); 
				isOK = true; 
				break;
			case CMD_ALLY:
				monarchA = st.nextToken().toCharArray();
				monarchB = st.nextToken().toCharArray();
				result = userSolution.ally(monarchA, monarchB); 
				check = Integer.parseInt(st.nextToken());
				if (result != check)
					isOK = false;
				break;
			case CMD_ATTACK:
				monarchA = st.nextToken().toCharArray();
				monarchB = st.nextToken().toCharArray();
				general = st.nextToken().toCharArray(); 
				result = userSolution.attack(monarchA, monarchB, general); 
				check = Integer.parseInt(st.nextToken());
				if (result != check)
					isOK = false;
				break;
			case CMD_RECRUIT:
				monarchA = st.nextToken().toCharArray();
				num = Integer.parseInt(st.nextToken());
				sign = Integer.parseInt(st.nextToken());
				result = userSolution.recruit(monarchA, num, sign); 
				check = Integer.parseInt(st.nextToken());
				if (result != check)
					isOK = false;
				break;
			default:
				isOK = false; 
				break;
			}
		}
		return isOK;
	}

	public static void main(String[] args) throws IOException {
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