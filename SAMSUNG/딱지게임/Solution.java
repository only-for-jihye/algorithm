package 딱지게임;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Solution {
  
    private final static int LM = 20003; 
    private static int[] PARENT = new int[LM]; 
    
    public int Find(int x) {
    	if(PARENT[x] == x) return x;
    	return PARENT[x] = Find(PARENT[x]);
    }
    
    public void Union(int x, int y, int pid) {
    	x = Find(x);
    	y = Find(y);
    	if(x == y) return;
    	PARENT[x] = y; 
    }
    
    public void init(int N, int M) {
      	return; 
    }
    
    public int add(int row, int col, int size, int pid) {
    	return 0; 
    }
    
    public int get(int row, int col) {
    	return 0; 
    }
}