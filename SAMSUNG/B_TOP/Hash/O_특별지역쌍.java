package B_TOP.Hash;

import java.io.*;
import java.util.*;

public class O_특별지역쌍 {
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		
		HashMap<String, Integer> hm = new HashMap<>();
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			String city = st.nextToken();
			String country = st.nextToken();
			
			String prefix = city.substring(0, 2);
			
			if (prefix.equals(country)) continue;
			
			String key = prefix + country;
			hm.put(key, hm.getOrDefault(key, 0) + 1);
		}
		
		int count = 0;
		
		for (String key : hm.keySet()) {
			String prefix = key.substring(0, 2);
			String country = key.substring(2, 4);
			
			String reversedKey = country + prefix;
			
			if (hm.containsKey(reversedKey)) {
				count += hm.get(key) * hm.get(reversedKey);
			}
		}
		
		System.out.println(count / 2);
	}
	
}
