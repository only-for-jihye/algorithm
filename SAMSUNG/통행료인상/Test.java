package 통행료인상;

import java.util.*;

public class Test {
	
	static class Road {
		int id;
		boolean isDeleted;
		public Road(int id) {
			super();
			this.id = id;
			this.isDeleted = false;
		}
	}
	
	public static void main(String[] args) {
		HashMap<Integer, Road> map = new HashMap<>();
		
		Road rd = new Road(4);
		map.put(1, new Road(1));
		map.put(2, new Road(2));
		map.put(3, new Road(3));
		map.put(4, rd);
		
		ArrayList<Road> roads = new ArrayList<>();
		roads.add(new Road(1));
		roads.add(new Road(2));
		roads.add(new Road(3));
		roads.add(rd);
		
		Road road = map.get(1);
		road.isDeleted = true;
		
		Road road2 = map.get(4);
		road2.isDeleted = true;
		
		System.out.println(roads.get(0).isDeleted);
		System.out.println(roads.get(3).isDeleted);
	}
}
