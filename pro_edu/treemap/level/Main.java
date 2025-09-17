package pro_edu.treemap.level;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.NavigableMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Main {
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken()); // 길드원 수
		int M = Integer.parseInt(st.nextToken()); // 아이템 수
		
		TreeMap<Integer, Integer> tm = new TreeMap<>();
		
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < M; i++) {
			int item = Integer.parseInt(st.nextToken());
			tm.put(item, 0);
		}
		int count = 0;
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			int level = Integer.parseInt(st.nextToken());
			
			Map.Entry<Integer, Integer> map = tm.floorEntry(level);
//			Integer key = tm.floorKey(level);
			
			if (map != null) {
				tm.remove(map.getKey());
				count++;
			} else {
				break;
			}
//			if (key == null) {
//				break;
//			} else {
//				tm.remove(key);
//				count++;
//			}
		
		}
		System.out.println(count);
	}
}
