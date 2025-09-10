import java.io.*;
import java.util.*;

class UserSolution {
	
	private List<Node>[] graph;
	private int[] costs;
	private Map<Integer, Road> road;
	// UserSolution 클래스에 멤버 변수 추가
	private Map<Integer, Integer> priceToIndex;
	private int priceCount;
	
	public void init(int N, int[] mCost, int K, int[] mId, int[] sCity, int[] eCity, int[] mDistance) {
		costs = new int[N];
		System.arraycopy(mCost, 0, costs, 0, N);
		graph = new ArrayList[N];
		for (int i = 0; i < N; i++) {
			graph[i] = new ArrayList<>();
		}
		
		road = new HashMap<>();
		for (int i = 0; i < K; i++) {
			graph[sCity[i]].add(new Node(eCity[i], mDistance[i]));
			road.put(mId[i], new Road(sCity[i], eCity[i], mDistance[i]));
		}
		
		// [성능 개선] 요금 좌표 압축
	    Set<Integer> uniquePrices = new HashSet<>();
	    for (int price : mCost) {
	        uniquePrices.add(price);
	    }
	    List<Integer> sortedPrices = new ArrayList<>(uniquePrices);
	    Collections.sort(sortedPrices);

	    priceToIndex = new HashMap<>();
	    priceCount = sortedPrices.size();
	    for (int i = 0; i < priceCount; i++) {
	        priceToIndex.put(sortedPrices.get(i), i);
	    }
    }

    public void add(int mId, int sCity, int eCity, int mDistance) {
    	graph[sCity].add(new Node(eCity, mDistance));
    	road.put(mId, new Road(sCity, eCity, mDistance));
    }

    public void remove(int mId) {
    	int startCity = road.get(mId).startCity;
    	int endCity = road.get(mId).endCity;
    	int mDistance = road.get(mId).distance;

    	List<Node> connections = graph[startCity];
    	for (int i = 0; i < connections.size(); i++) {
    	    Node node = connections.get(i);
    	    if (node.city == endCity && node.distance == mDistance) {
    	        connections.remove(i);
    	        break; // 찾았으니 루프 종료
    	    }
    	}
    	road.remove(mId);
    }

    int cost(int sCity, int eCity) {
        int N = costs.length;
        // [성능 개선] Map 배열 대신 2D 배열 사용
        // long[도시ID][요금인덱스]
        long[][] minTotalCosts = new long[N][priceCount];
        for (int i = 0; i < N; i++) {
            Arrays.fill(minTotalCosts[i], Long.MAX_VALUE);
        }

        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));

        // 1. 시작 도시 정보 설정
        int startPrice = costs[sCity];
        int startPriceIndex = priceToIndex.get(startPrice);
        minTotalCosts[sCity][startPriceIndex] = 0;
        pq.add(new long[]{0, sCity, startPrice}); // {총비용, 도시, 실제요금}

        while (!pq.isEmpty()) {
            long[] current = pq.poll();
            long u_cost = current[0];
            int u = (int) current[1];
            int u_min_price = (int) current[2];
            int u_min_price_idx = priceToIndex.get(u_min_price);

            if (minTotalCosts[u][u_min_price_idx] < u_cost) {
                continue;
            }

            for (Node neighbor : graph[u]) {
                int v = neighbor.city;
                int distance = neighbor.distance;

                long newCostTo_v = u_cost + (long) distance * u_min_price;
                int new_min_price = Math.min(u_min_price, costs[v]);
                int new_min_price_idx = priceToIndex.get(new_min_price);

                // [최적화 1]
                // 이 새로운 경로가 해당 요금 인덱스의 기존 최적 경로보다 비싸면 무시
                if (newCostTo_v >= minTotalCosts[v][new_min_price_idx]) {
                    continue;
                }

                // 새로운 최적 경로 발견, 우선순위 큐에 추가
                minTotalCosts[v][new_min_price_idx] = newCostTo_v;
                pq.add(new long[]{newCostTo_v, v, new_min_price});

                // [최적화 2 - 핵심] "경로 지배" 원리 적용
                // 이 새로운 비용을 더 안 좋은 요금(더 높은 인덱스) 상태들에게 전파.
                // 만약 더 안 좋은 요금 상태의 비용이 이 새로운 비용보다 비싸다면,
                // 그 상태는 더 이상 탐색할 가치가 없으므로 비용을 갱신해준다.
                for (int i = new_min_price_idx + 1; i < priceCount; i++) {
                    if (minTotalCosts[v][i] > newCostTo_v) {
                        minTotalCosts[v][i] = newCostTo_v;
                    } else {
                        // 이미 더 좋은 비용으로 기록되어 있다면, 그 뒤의 인덱스들도 모두 더 좋을 것이므로 break.
                        // (비용이 단조적으로 감소하는 경향이 있기 때문)
                        break;
                    }
                }
            }
        }

        // 최종 결과: eCity에 도착한 모든 경로 중 가장 저렴한 비용 찾음
        long finalResult = Long.MAX_VALUE;
        for (int i = 0; i < priceCount; i++) {
            finalResult = Math.min(finalResult, minTotalCosts[eCity][i]);
        }

        return (finalResult == Long.MAX_VALUE) ? -1 : (int) finalResult;
    }
}

class Road {
	int startCity;
	int endCity;
	int distance;
	
	public Road (int startCity, int endCity, int distance) {
		this.startCity = startCity;
		this.endCity = endCity;
		this.distance = distance;
	}
}

class Node implements Comparable<Node> {
	int city;
	int distance;
	long cost;
	
	public Node (int city, int distance) {
		this.city = city;
		this.distance = distance;
	}
	
	public Node (int city, long cost) {
		this.city = city;
		this.cost = cost;
	}

	@Override
	public int compareTo(Node o) {
		return Long.compare(this.cost, o.cost);
	}
	
}
