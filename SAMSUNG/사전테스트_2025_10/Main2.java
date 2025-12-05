package 사전테스트_2025_10;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main2 {
	
	static int N; // N개의 도시
	static int M; // M개의 간선
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	static int id;
	static ArrayList<Airline>[] node;
	static PriorityQueue<Edge2> air_pq;
	static Map<Integer, Product> map;
	static TreeSet<Product> set;
	static int startNode = 0;
//	static int[] distances;
	static int dist[];

	public static void main(String[] args) throws Exception {
		st = new StringTokenizer(br.readLine());
		
		int T = Integer.parseInt(st.nextToken());
		
		for (int t = 1; t <= T; t++) {
			st = new StringTokenizer(br.readLine());
			int command = Integer.parseInt(st.nextToken()); 
			if (command == 100) {
				// 여객기 항공 노선 확보
				makeAirline();
			} else if (command == 200) {
				// 여행 상품 생성
				makeTourProduct();
			} else if (command == 300) {
				// 여행 상품 취소
				cancelTourProduct();
			} else if (command == 400) {
				// 여행 상품 판매
				sell();
			} else if (command == 500) {
				// 출발지 변경
				changeDeparture();
			}
			
		}
		
	}
	
	private static void changeDeparture() {
		int s = Integer.parseInt(st.nextToken());
		startNode = s;
		// 여행 상품들의 출발지를 전부 s로 바꿈
		Iterator<Product> it = set.iterator();
		ArrayList<Product> newProduct = new ArrayList<>();
		
		setAllDistance(s);
		while (it.hasNext()) {
			Product next = it.next();
			next.start = s;
			// 출발지가 바뀌면 dist가 바껴야할듯?
//			Product changedProduct = next;
			it.remove();
			map.remove(next.id);
//			int getDist = getMinDist(s, next.dest);
//			if (getDist == -1) getDist = Integer.MAX_VALUE;
			Product product = new Product(next.id, next.revenue, next.dest, dist[next.dest]);
			newProduct.add(product);
		}
		for (Product product : newProduct) {
			set.add(product);
			map.put(product.id, product);
		}
	}

	private static void sell() {
		// 최적의 여행 상품 판매 후, 상품 id 출력
		// 그리고 상품 목록에서 제거
		// 여행 상품이 없다면 -1;
		if (set.size() <= 0) {
			System.out.println(-1);
			return;
		}
//		Product product = set.getFirst();
		Product product = set.first();
		if (product.cash < 0 || product.dist < 0) {
			System.out.println(-1);
			return;
		}
		map.remove(product.id);
		set.remove(product);
		System.out.println(product.id);
	}

	private static void cancelTourProduct() {
		id = Integer.parseInt(st.nextToken());
		// id로 상품 삭제
		if (map.containsKey(id)) {
			Product product = map.get(id);
			map.remove(id);
			set.remove(product);
		}
	}

	private static void makeTourProduct() {
		id = Integer.parseInt(st.nextToken());
//		if (id == 4) {
//			System.out.println("a");
//		}
		int revenue = Integer.parseInt(st.nextToken());
		int dest = Integer.parseInt(st.nextToken());
		// 여행 상품 저장
//		int getDist = getMinDist(startNode, dest);
//		if (getDist == -1) getDist = Integer.MAX_VALUE;
//		if (getDist < 0) return; // 도달하지 못하면 추가하지 않음
//		if (distances[dest] == Integer.MAX_VALUE)
		Product product = new Product(id, revenue, dest, dist[dest]);
		set.add(product);
		map.put(id, product);
	}
	
//	private static int getMinDist(int startNode, int dest) {
//		air_pq = new PriorityQueue<>();
//		air_pq.add(new Edge2(startNode, 0));
//		int dist[] = new int[N];
//		Arrays.fill(dist, Integer.MAX_VALUE);
//		dist[startNode] = 0;
//		while (!air_pq.isEmpty()) {
//			Edge2 now = air_pq.poll();
//			if (dist[now.dest] < now.dist) continue;
//			if (now.dest == dest) return now.dist;
//			
//			for (Airline air : node[now.dest]) {
//				if (now.dist + air.dist < dist[air.eCity]) {
//					dist[air.eCity] = now.dist + air.dist;
//					air_pq.add(new Edge2(air.eCity, dist[air.eCity]));
//				}
//			}
//		}
//		return -1;
//	}
	
	private static void setAllDistance(int startNode) {
		air_pq = new PriorityQueue<>();
		air_pq.add(new Edge2(startNode, 0));
//		dist = new int[N];
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[startNode] = 0;
		while (!air_pq.isEmpty()) {
			Edge2 now = air_pq.poll();
			if (dist[now.dest] < now.dist) continue;
//			distances[now.dest] = now.dist;
			for (Airline air : node[now.dest]) {
				if (now.dist + air.dist < dist[air.eCity]) {
					dist[air.eCity] = now.dist + air.dist;
					air_pq.add(new Edge2(air.eCity, dist[air.eCity]));
				}
			}
		}
	}
	
	private static void makeAirline() {
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		node = new ArrayList[N];
//		air_pq = new PriorityQueue<>();
		set = new TreeSet<>();
		map = new HashMap<>();
		for (int i = 0; i < N; i++) {
			node[i] = new ArrayList<>();
		}
		for (int i = 0; i < M; i++) {
			// 간선 연결
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			// 간선 정보 저장
			Airline airline1 = new Airline(a, b, w);
			Airline airline2 = new Airline(b, a, w);
			node[a].add(airline1); 
			node[b].add(airline2); 
		}
//		distances = new int[N];
		dist = new int[N];
		setAllDistance(startNode);
	}
}

class Edge2 implements Comparable<Edge2> {
	int dest;
	int dist;
	public Edge2(int dest, int dist) {
		super();
		this.dest = dest;
		this.dist = dist;
	}
	@Override
	public int compareTo(Edge2 o) {
		return Integer.compare(this.dist, o.dist);
	}
}

class Product implements Comparable<Product> {
	int id;
	int start;
	int revenue;
	int dest;
	int dist;
	int cash;
	public Product(int id, int revenue, int dest, int dist) {
		super();
		this.id = id;
		this.start = 0;
		this.revenue = revenue;
		this.dest = dest;
		this.dist = dist;
		this.cash = revenue - dist;
	}
	@Override
	public int compareTo(Product o) {
		if (this.cash != o.cash) return Integer.compare(o.cash, this.cash);
		return Integer.compare(this.id, o.id);
	}
}

class Airline {
	int sCity;
	int eCity;
	int dist;
	public Airline(int sCity, int eCity, int dist) {
		super();
		this.sCity = sCity;
		this.eCity = eCity;
		this.dist = dist;
	}
}