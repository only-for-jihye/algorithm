package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class H_노래가사분석기 {
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		String lyrics = st.nextToken();
		
		HashMap<String, Integer> hm = new HashMap<>();
		
		for (int i = 2; i <= 5; i++) {
			for (int j = 0; j <= lyrics.length() - i; j++) {
				String sub = lyrics.substring(j, j + i);
				hm.put(sub, hm.getOrDefault(sub, 0) + 1);
			}
		}
		
		st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			String find = st.nextToken();
			
			System.out.println(hm.get(find));
		}
	}		
}
