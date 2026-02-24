package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class M_벽화에서좌표찾기 {
	
	static class Grid {
		int y;
		int x;
		public Grid(int y, int x) {
			super();
			this.y = y;
			this.x = x;
		}
		@Override
		public String toString() {
			return "(" + y + "," + x + ")";
		}
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		HashMap<Integer, ArrayList<Grid>> hm = new HashMap<>();
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				int val = Integer.parseInt(st.nextToken());
				Grid grid = new Grid(i, j);
				if (hm.containsKey(val)) {
					hm.get(val).add(grid);
				} else {
					ArrayList<Grid> al = new ArrayList<>();
					al.add(grid);
					hm.put(val, al);
				}
			}
		}
		
		st = new StringTokenizer(br.readLine());
		int ans = Integer.parseInt(st.nextToken());
		
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < ans; i++) {
			int a = Integer.parseInt(st.nextToken());
			if (!hm.containsKey(a)) {
				System.out.println("none");
			} else {
				ArrayList<Grid> hma = hm.get(a);
				if (hma.size() > 0) {
					for (int k = 0; k < hma.size(); k++) {
						System.out.print(hma.get(k).toString() + " ");
					}
					System.out.println();
				} else {
					System.out.println("none");
				}
			}
		}
	}
	
}
