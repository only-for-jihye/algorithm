package pro_edu.carrot;

import java.io.*;
import java.util.*;

class Main {
	private final static int INIT = 0;
	private final static int ADD = 1;
	private final static int SELL = 2;
	private final static int UPDATE = 3;
	
	private final static UserSolution usersolution = new UserSolution();
	private final static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private final static String tagNames[] = new String[5]; 
	
	private static boolean run() throws Exception {
		
		StringTokenizer st;
		int N, cmd, ans, ret, tagNum, price;

		int Q = 0;
		boolean correct = false;

		ret = ans = 0;
		
		int n;
		Q = Integer.parseInt(br.readLine());
		
		for (int q = 0; q < Q; ++q) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {
			case INIT:
				N = Integer.parseInt(st.nextToken());
				usersolution.init(N);
				correct = true;
				break;
			case ADD:
				price = Integer.parseInt(st.nextToken());
				tagNum = Integer.parseInt(st.nextToken());
				for (int m = 0; m < tagNum; ++m)
					tagNames[m] = st.nextToken();
				usersolution.addCarrot(price, tagNum, tagNames);
				break;
			case SELL:
				ans = Integer.parseInt(st.nextToken());
				for (int m = 0; m < 3; m++)
					tagNames[m] = st.nextToken();
				ret = usersolution.sellCarrot(tagNames[0], tagNames[1], tagNames[2]);
				if (ans != ret) {
					correct = false;
				}
				break;
			case UPDATE:
				tagNames[0] = st.nextToken();
				price = Integer.parseInt(st.nextToken());
				usersolution.updatePrice(tagNames[0], price);
				break;
			default:
				correct = false;
			}
		}
		return correct;
	}

	public static void main(String[] args) throws Exception {
		int TC, MARK;

		 System.setIn(new java.io.FileInputStream("src/carrot/sample_input.txt"));

		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run() ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
	}
}