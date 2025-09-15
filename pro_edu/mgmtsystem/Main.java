package pro_edu.mgmtsystem;

import java.io.*;
import java.util.*;

class Main {

	private final static int CMD_REGISTER 	= 1;
	private final static int CMD_INOUT	 	= 2;

	private static boolean run(BufferedReader br) throws IOException {

		StringTokenizer st;
		st = new StringTokenizer(br.readLine());
		int Q = Integer.parseInt(st.nextToken());

		usersolution.init();

		boolean isCorrect = true; 
		int cmd; 
		int id;
		String name; 

		for (int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());
			switch(cmd) 
			{
				case CMD_REGISTER:
					id = Integer.parseInt(st.nextToken());
					name = st.nextToken();
					String userAns1 = usersolution.register(id, name);
					String ans1 = st.nextToken();
					if(!userAns1.equals(ans1)) 
						isCorrect = false; 
					break;
	
				case CMD_INOUT:
					id = Integer.parseInt(st.nextToken());
					String[] userAns2 = usersolution.inout(id); 
					String a1 = st.nextToken();
					String a2 = st.nextToken();  
					if (!userAns2[0].equals(a1) || !userAns2[1].equals(a2)) 
						isCorrect = false;
					break;
	
				default:
					isCorrect = false;
					break;
			}
		}
		return isCorrect;
	}

	private final static UserSolution usersolution = new UserSolution();

	public static void main(String[] args) throws Exception {

		// System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		int TC = Integer.parseInt(st.nextToken());
		int MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; testcase++) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
	}
}