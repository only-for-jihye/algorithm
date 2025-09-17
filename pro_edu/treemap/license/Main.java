package pro_edu.treemap.license;

import java.io.*;
import java.util.*;

class Main {

	private final static int CMD_INIT = 100; 
	private final static int CMD_ADDEXAM = 200;  
	private final static int CMD_CUSTOMEXAM = 300;  
	private final static int CMD_CHANGE = 400;  
	private final static int CMD_MAXEXAM = 500; 
	private final static int CMD_MINEXAM = 600; 
	private final static int CMD_RANGE = 700; 

	private final static Solution userSolution = new Solution();

	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	 

	private static boolean run() throws Exception {
		int numQuery, cmd; 
		int level, studyAmount, beforeLevel, afterLevel, A, B; 
		String name, ans_str, userAns_str;
		int ans_int, userAns_int;
		boolean isCorrect = false; 
		
		st = new StringTokenizer(br.readLine());
		numQuery = Integer.parseInt(st.nextToken());
		
		for(int i = 0; i < numQuery; i++) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());
			
			switch(cmd) 
			{
			case CMD_INIT:
				userSolution.init(); 
				isCorrect = true;
				break;
			case CMD_ADDEXAM :
				level = Integer.parseInt(st.nextToken());
				name = st.nextToken(); 
				userSolution.addExam(level, name); 
				break; 
			case CMD_CUSTOMEXAM :
				studyAmount = Integer.parseInt(st.nextToken());
				ans_str = st.nextToken();
				userAns_str = userSolution.getCustomExam(studyAmount);
				if(!userAns_str.equals(ans_str))
					isCorrect = false; 
				break;
			case CMD_CHANGE : 
				beforeLevel = Integer.parseInt(st.nextToken());
				afterLevel = Integer.parseInt(st.nextToken());
				ans_int = Integer.parseInt(st.nextToken());
				userAns_int = userSolution.changeExamLevel(beforeLevel, afterLevel); 
				if(userAns_int != ans_int)
					isCorrect = false;
				break;
			case CMD_MAXEXAM:
				ans_str = st.nextToken();
				userAns_str = userSolution.getMaxExam(); 
				if(!userAns_str.equals(ans_str))
					isCorrect = false; 
				break; 
			case CMD_MINEXAM:
				ans_str = st.nextToken();
				userAns_str = userSolution.getMinExam(); 
				if(!userAns_str.equals(ans_str))
					isCorrect = false; 
				break; 
			case CMD_RANGE: 
				A = Integer.parseInt(st.nextToken());
				B = Integer.parseInt(st.nextToken());
				userAns_int = userSolution.countRangeExam(A, B);
				ans_int = Integer.parseInt(st.nextToken());
				if(userAns_int != ans_int)
					isCorrect = false;
				break;
			default:
				isCorrect = false;
				break;
			}
		}
		return isCorrect; 
	}

	public static void main(String[]args) throws Exception {
		int T, SUCCESS;
		st = new StringTokenizer(br.readLine());

		T = Integer.parseInt(st.nextToken());
		SUCCESS = Integer.parseInt(st.nextToken());

		for (int tc = 1; tc <= T; tc++) {
			int score = run() ? SUCCESS : 0;
			System.out.println("#" + tc + " " + score);
		}
		br.close();
	}
}