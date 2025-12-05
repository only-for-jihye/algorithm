package 사전테스트_2025_11;

import java.io.*;
import java.util.*;

public class Main3 {

    static int N, M, R;
    static long[][] dist; 
    
    static int[][] nextCityMap;
    static int[][] edgeWeightMap;
    
    static int[] records;
    static final long INF = 100_000_000_000L;

    static class Pair implements Comparable<Pair> {
        int sCity;
        int eCity;
        public Pair(int sCity, int eCity) {
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

        dist = new long[N + 1][N + 1];
        nextCityMap = new int[N + 1][101];   
        edgeWeightMap = new int[N + 1][101]; 

        for (int i = 1; i <= N; i++) {
            Arrays.fill(dist[i], INF);
            dist[i][i] = 0;
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int color = Integer.parseInt(st.nextToken());

            nextCityMap[u][color] = v;
            nextCityMap[v][color] = u;
            
            edgeWeightMap[u][color] = w;
            edgeWeightMap[v][color] = w;

            if (dist[u][v] > w) {
                dist[u][v] = w;
                dist[v][u] = w;
            }
        }

        for (int k = 1; k <= N; k++) {
            for (int i = 1; i <= N; i++) {
                for (int j = 1; j <= N; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF) {
                        dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                    }
                }
            }
        }

        st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken()); 
        
        records = new int[R];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < R; i++) {
            records[i] = Integer.parseInt(st.nextToken());
        }

        List<Pair> answer = new ArrayList<>();
        
        for (int sCity = 1; sCity <= N; sCity++) {
            for (int eCity = 1; eCity <= N; eCity++) {
                if (dist[sCity][eCity] == INF) continue;

                if (isPathPossible(sCity, eCity)) {
                    answer.add(new Pair(sCity, eCity));
                }
            }
        }

        Collections.sort(answer);
        StringBuilder sb = new StringBuilder();
        for (Pair pair : answer) {
            sb.append(pair.sCity).append(" ").append(pair.eCity).append("\n");
        }
        System.out.print(sb);
    }

    static boolean isPathPossible(int start, int target) {
        int curr = start;

        for (int record : records) {
            int color = Math.abs(record);
            boolean shouldBeShortest = (record > 0); 

            int next = nextCityMap[curr][color];
            int weight = edgeWeightMap[curr][color];

            if (next == 0) return false;

            if (dist[next][target] == INF || dist[curr][target] == INF) return false;

            boolean isActuallyShortest = (dist[curr][target] == weight + dist[next][target]);

            if (shouldBeShortest != isActuallyShortest) {
                return false;
            }

            curr = next;
        }

        return true;
    }
}