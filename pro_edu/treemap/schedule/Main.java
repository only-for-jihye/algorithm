package pro_edu.treemap.schedule;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Main {
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int Q = Integer.parseInt(st.nextToken()); 
		
//		TreeMap<Schedule, Integer> tm = new TreeMap<>();
		TreeMap<Integer, Integer> tm = new TreeMap<>();
		
		int count = 0;
		
		for (int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			int startDate = Integer.parseInt(st.nextToken()); 
			int endDate = Integer.parseInt(st.nextToken()); 
			
			Map.Entry<Integer, Integer> before_schedule = tm.lowerEntry(startDate); // floor 일 때
			Map.Entry<Integer, Integer> after_schedule = tm.ceilingEntry(startDate); // 여기는 higher가 되어야 한다. 어느 한쪽에 equal 필요
			
			if (before_schedule != null && before_schedule.getValue() >= startDate) continue;
			if (after_schedule != null && after_schedule.getKey() <= endDate) continue;
			
			tm.put(startDate, endDate);
			count++;
			// 나의 풀이
//			Schedule schedule = new Schedule(startDate, endDate);
			
//			if (tm.isEmpty()) {
//				tm.put(schedule, endDate - startDate);
//				count++;
//				continue;
//			}
//			
//			Schedule before = tm.lowerKey(schedule);
//			Schedule after = tm.ceilingKey(schedule);
//			if (before == null) {
//				if (schedule.endDate < after.startDate) {
//					count++;
//					tm.put(schedule, endDate - startDate);
//				}
//			}
//			if (after == null) {
//				if (before.endDate < schedule.startDate) {
//					count++;
//					tm.put(schedule, endDate - startDate);
//				}
//			}
//			if (before != null && after != null) {
//				if (before.endDate < schedule.startDate
//						&& schedule.endDate < after.startDate) {
//					count++;
//					tm.put(schedule, endDate - startDate);
//				}
//			}
		}
		
//		System.out.println(count);
		System.out.println(tm.size());
	}
}

class Schedule implements Comparable<Schedule> {
	int startDate;
	int endDate;
	public Schedule (int startDate, int endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}
	@Override
	public int compareTo(Schedule o) {
		if (this.startDate < o.startDate) return -1;
		if (this.startDate > o.startDate) return 1;
		if (this.endDate < o.endDate) return -1;
		if (this.endDate > o.endDate) return 1;
		return 0;
	}
	
}