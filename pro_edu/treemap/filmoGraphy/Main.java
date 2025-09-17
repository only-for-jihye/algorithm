package pro_edu.treemap.filmoGraphy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken()); 
		int M = Integer.parseInt(st.nextToken()); 
		
		HashMap<String, TreeMap<Integer, String>> bookMap = new HashMap<>();
		
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				String name = st.nextToken();
				String book_name = st.nextToken();
				int year = Integer.parseInt(st.nextToken());
				TreeMap<Integer, String> map = new TreeMap<>();
				map.put(year, book_name);
				bookMap.put(name, map);
			}
		}
		
		st = new StringTokenizer(br.readLine());
		TreeMap<Integer, String> entry = bookMap.get(st.nextToken());
		System.out.println(entry.get(1998));
	}
}