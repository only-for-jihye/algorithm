package pro_edu.bfs.bacteria;

import java.io.*;
import java.util.*;

class Main {
	private static final int CMD_INIT				= 10000;
	private static final int CMD_PUT				= 20000;
	private static final int CMD_KILL				= 30000;
	private static final int CMD_CHECK				= 40000; 

	private static UserSolution usersolution = new UserSolution();
	
	public static final class Result {
		int row;
		int col;
		
		Result() {
			this.row = 0;
			this.col = 0; 
		}
	}
	
	public static final class Bacteria {
		int id;
		int size;
		int time; 
		Bacteria() {
			this.id = 0;
			this.size = 0;
			this.time = 0; 
		}
	}

	private static boolean run(BufferedReader br) throws Exception {
		
		int Q;
	
        StringTokenizer stdin = new StringTokenizer(br.readLine(), " ");
		
		int cmd, N, time, row, col;
		int ans, res;
		Result ret; 
		boolean okay = false; 
		
		Q = Integer.parseInt(stdin.nextToken());
		
		for (int q = 0; q < Q; ++q) {
            stdin = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(stdin.nextToken());
			
			switch(cmd) {
			
			case CMD_INIT:
				N = Integer.parseInt(stdin.nextToken()); 
				usersolution.init(N); 
				okay = true;
				break;
				
			case CMD_PUT:
				time = Integer.parseInt(stdin.nextToken());
				row = Integer.parseInt(stdin.nextToken());
				col = Integer.parseInt(stdin.nextToken());
				Bacteria bac = new Bacteria(); 
				bac.id = Integer.parseInt(stdin.nextToken());
				bac.size = Integer.parseInt(stdin.nextToken());
				bac.time = Integer.parseInt(stdin.nextToken());
				ret = usersolution.putBacteria(time, row, col, bac); 
				row = Integer.parseInt(stdin.nextToken());
				col = Integer.parseInt(stdin.nextToken());
				if (ret.row != row || ret.col != col)
					okay = false;
				break;
				
			case CMD_KILL:
				time = Integer.parseInt(stdin.nextToken());
				row = Integer.parseInt(stdin.nextToken());
				col = Integer.parseInt(stdin.nextToken());
				ans = Integer.parseInt(stdin.nextToken());	
				res = usersolution.killBacteria(time, row, col);  
//				if (ans != res)
//					okay = false;
				break;
				
			case CMD_CHECK: 
				time = Integer.parseInt(stdin.nextToken());
				row = Integer.parseInt(stdin.nextToken());
				col = Integer.parseInt(stdin.nextToken());
				ans = Integer.parseInt(stdin.nextToken());
				res = usersolution.checkCell(time, row, col);  
//				if(ans != res)
//					okay = false;
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
		// System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());
		
		for (int testcase = 1; testcase <= TC; ++testcase)
		{
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		
		br.close();
	}
}