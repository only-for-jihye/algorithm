package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class P_인구조사 {
	
	static HashMap<String, Integer> map = new HashMap<>();
	static int keyNum;
	static int[][] grid;
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		
		keyNum = 0;
		grid = new int[201][201];
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			int M = Integer.parseInt(st.nextToken());
			
			int[] array = new int[M];
			for (int j = 0; j < M; j++) {
				String country = st.nextToken();
				array[j] = getNumber(country);
			}
			
			for (int j = 0; j < M; j++) {
				for (int k = j + 1; k < M; k++) {
					int c1 = array[j];
					int c2 = array[k];
					grid[c1][c2]++;
					grid[c2][c1]++;
				}
			}
		}
		
		st = new StringTokenizer(br.readLine());
		int M = Integer.parseInt(st.nextToken());
		
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			String c1 = st.nextToken();
			String c2 = st.nextToken();
			
			int seq_c1 = getNumber(c1);
			int seq_c2 = getNumber(c2);
			
			int sum = grid[seq_c1][seq_c2];
			
			System.out.print(sum + " ");
		}
		
	}
	
	public static int getNumber(String country) {
		if (!map.containsKey(country)) {
			map.put(country, keyNum++);
		}
		return map.get(country);
	}
	
}
