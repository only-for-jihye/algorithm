package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class G_배틀로얄게임 {
	
	static class Team {
		int userCnt;
		int totalScore;
		public Team(int userCnt, int totalScore) {
			super();
			this.userCnt = userCnt;
			this.totalScore = totalScore;
		}
	}

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());

		HashMap<String, Team> hm = new HashMap<>();
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			String name = st.nextToken();
			int score = Integer.parseInt(st.nextToken());
			
			if (!hm.containsKey(name)) {
				Team team = new Team(1, score);
				hm.put(name, team);
			} else {
				Team team = hm.get(name);
				team.userCnt++;
				team.totalScore += score;
			}
		}
		
		st = new StringTokenizer(br.readLine());
		String coco = st.nextToken();
		String friend = st.nextToken();
		
		Team c = hm.get(coco);
		Team f = hm.get(friend);
		
		System.out.println(c.userCnt + " " + c.totalScore);
		System.out.println(f.userCnt + " " + f.totalScore);
		
		if (c.totalScore > f.totalScore) {
			System.out.println(coco);
		} else {
			System.out.println(friend);
		}
	}		
}
