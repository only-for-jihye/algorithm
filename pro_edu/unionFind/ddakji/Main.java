package pro_edu.unionFind.ddakji;
import java.io.*;
import java.util.*;

class Main {

	private static int seed = 5;

	private final static UserSolution userSolution = new UserSolution();

	private static int board_size, area_size, query_cnt;
	public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	public static StringTokenizer st;

	private static int pseudo_rand() {
		seed = seed * 214013 + 2531011;
		return (seed >> 16) & 0x7FFF;
	}

	private static int run(int score) throws Exception {
		st = new StringTokenizer(br.readLine());
		seed = Integer.parseInt(st.nextToken());
		board_size = Integer.parseInt(st.nextToken());
		area_size = Integer.parseInt(st.nextToken());
		query_cnt = Integer.parseInt(st.nextToken());
		userSolution.init(board_size, area_size);
      
		for (int q = 0; q < query_cnt; q++) {
			st = new StringTokenizer(br.readLine());
			int r, c, size, user_ans, correct_ans;
			for (int i = 1; i <= 2; i++)
			{
				size = (pseudo_rand() * pseudo_rand()) % area_size + 1;
				r = (pseudo_rand() * pseudo_rand()) % (board_size - size + 1);
				c = (pseudo_rand() * pseudo_rand()) % (board_size - size + 1);

				user_ans = userSolution.add(r, c, size, i);
				correct_ans = Integer.parseInt(st.nextToken());

				if (user_ans != correct_ans)
					score = 0;
			}
			r = (pseudo_rand() * pseudo_rand()) % board_size;
			c = (pseudo_rand() * pseudo_rand()) % board_size;
			user_ans = userSolution.get(r, c);
			correct_ans = Integer.parseInt(st.nextToken());
			if (user_ans != correct_ans)
				score = 0;
		}
		return score;
	}

	public static void main(String[]args) throws Exception {
		int tc, score;

		st = new StringTokenizer(br.readLine());
		tc = Integer.parseInt(st.nextToken());
		score = Integer.parseInt(st.nextToken());

		for (int t = 1; t <= tc; t++) {
			int t_score = run(score);
			System.out.println("#" + t + " " + t_score);
		}
		br.close();
	}
}