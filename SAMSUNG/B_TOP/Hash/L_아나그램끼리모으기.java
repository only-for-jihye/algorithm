package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class L_아나그램끼리모으기 {
	
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int T = Integer.parseInt(st.nextToken());
		
		HashMap<String, Integer> hm = new HashMap<>();
		
		for (int t = 0; t < T; t++) {
			st = new StringTokenizer(br.readLine());
			String word = st.nextToken();
			
			char[] ch = word.toCharArray();
			
			Arrays.sort(ch);
			
			String key = new String(ch);
			
			hm.put(key, hm.getOrDefault(key, 0) + 1);
		}
		
		int maxValue = 0;
		
		for (int v : hm.values()) {
			if (v > maxValue) {
				maxValue = v;
			}
		}
		
		System.out.println(maxValue);
	}
	
}
