package NOV_test;

import java.io.*;
import java.util.*;

public class Main2 {
	
    static int N;
    static int M;
    static int R;
    static int[][] dist;
    static ArrayList<Edge>[] arr;
    static int[] records;

    static class Edge {
        int eCity;
        int weight;
        int color;
		public Edge(int eCity, int weight, int color) {
			super();
			this.eCity = eCity;
			this.weight = weight;
			this.color = color;
		}
    }

    static class Pair implements Comparable<Pair> {
        int sCity;
        int eCity;
        public Pair(int sCity, int eCity) {
			super();
			this.sCity = sCity;
			this.eCity = eCity;
		}
		@Override
        public int compareTo(Pair o) {
            if (this.sCity != o.sCity) return this.sCity - o.sCity;
            return this.eCity - o.eCity;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        dist = new int[N + 1][N + 1];
        for (int i = 1; i <= N; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            dist[i][i] = 0;
        }

        arr = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) {
        	arr[i] = new ArrayList<>();
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int sCity = Integer.parseInt(st.nextToken());
            int eCity = Integer.parseInt(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());
            int color = Integer.parseInt(st.nextToken());

            arr[sCity].add(new Edge(eCity, weight, color));
            arr[eCity].add(new Edge(sCity, weight, color));

            // 양방향 도로이므로 양쪽 모두 갱신
            dist[sCity][eCity] = Math.min(dist[sCity][eCity], weight);
            dist[eCity][sCity] = Math.min(dist[eCity][sCity], weight);
        }

        R = Integer.parseInt(br.readLine());
        records = new int[R];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < R; i++) {
            records[i] = Integer.parseInt(st.nextToken());
        }

        for (int k = 1; k <= N; k++) {
            for (int i = 1; i <= N; i++) {
                for (int j = 1; j <= N; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE 
                    		&& dist[k][j] != Integer.MAX_VALUE) {
                        dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                    }
                }
            }
        }

        List<Pair> answer = new ArrayList<>();
        
        for (int sCity = 1; sCity <= N; sCity++) {
            for (int eCity = 1; eCity <= N; eCity++) {
                if (isPathPossible(sCity, eCity)) {
                    answer.add(new Pair(sCity, eCity));
                }
            }
        }

        Collections.sort(answer);
        for (Pair pair : answer) {
            System.out.println(pair.sCity + " " + pair.eCity);
        }
    }

    static boolean isPathPossible(int sCity, int target) {
        int curr = sCity;

        for (int record : records) {
            int targetColor = Math.abs(record);
            boolean isShortest = (record > 0);

            Edge nextEdge = null;
            for (Edge e : arr[curr]) {
                if (e.color == targetColor) {
                    nextEdge = e;
                    break;
                }
            }

            if (nextEdge == null) return false;
            
            int value = nextEdge.weight + dist[nextEdge.eCity][target];
            boolean isShortestPath = (dist[curr][target] == value);

            if (isShortest != isShortestPath) {
                return false;
            }
            curr = nextEdge.eCity;
        }

        return true;
    }
}

