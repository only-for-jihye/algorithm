package pro_edu.mgmtsystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.io.IOException;

class UserSolution {
	
	private final static int MAXNUM = 9999; 
	private HashMap<Integer, Employee> map;
	private Employee[] array;
	private Employee employee;
	
	public void init() 
	{
		map = new HashMap<>();
		array = new Employee[10000];
	}

	public String register(int id, String name)
	{
//		employee = new Employee(id, name, false);
//		if (!map.containsKey(id)) {
//			map.put(id, employee);
//			return "OK"; 
//		} else {
//			return "ERROR";
//		}
		if (array[id] == null) {
			array[id] = new Employee(id, name, false);
//			System.out.println("OK");
			return "OK";
		} else {
			return "ERROR";
		}
	}

	public String[] inout(int id)
	{
//		if (!map.containsKey(id)) {
//			return new String[] {"ERROR", "ERROR"};
//		}
//		if (map.get(id).status) { // 출근 상태
//			String name = map.get(id).name;
//			map.get(id).status = false;
//			return new String[] {name, "OUT"};
//		} else {
//			String name = map.get(id).name;
//			map.get(id).status = true;
//			return new String[] {name, "IN"};
//		}
		if (array[id] == null) {
			return new String[] {"ERROR", "ERROR"};
		}
		if (array[id].status) {
			array[id].status = false;
			return new String[] {array[id].name, "OUT"};
		} else {
			array[id].status = true;
			return new String[] {array[id].name, "IN"};
		}
	}
}

class Employee {
	int id;
	String name;
	boolean status;
	
	public Employee (int id, String name, boolean status) {
		this.id = id;
		this.name = name;
		this.status = status;
	}
	
	public Employee (int id, boolean status) {
		this.id = id;
		this.status = status;
	}
}