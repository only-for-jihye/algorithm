package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class K_산타의선물 {
	
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int T = Integer.parseInt(st.nextToken());
		
		HashMap<String, Integer> hm = new HashMap<>();
		for (int t = 0; t < T; t++) {
			st = new StringTokenizer(br.readLine());
			String nara = st.nextToken();
			String gu = st.nextToken();
			String age = st.nextToken();
			int score = Integer.parseInt(st.nextToken());
			
			StringBuilder sb = new StringBuilder();
			sb.append(nara).append(" ").append(gu).append(" ").append(age);
			String key = sb.toString();
			
			if (!hm.containsKey(key)) {
				hm.put(key, score);
			} else {
				hm.put(key, hm.getOrDefault(key, 0) + score);
			}
		}
		
		int maxValue = 0;
		String winner = "";
		for (Map.Entry<String, Integer> entry : hm.entrySet()) {
			if (entry.getValue() > maxValue) {
				maxValue = entry.getValue();
				winner = entry.getKey();
			}
		}
		
		System.out.println(winner);
	}
	
}
