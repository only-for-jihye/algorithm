package pro_edu.unionFind.ddakji;

import java.io.*;
import java.util.*;

class UserSolution {
	  
	class Box{
		int row, col, size, id;

		public Box(int row, int col, int size, int id) {
			super();
			this.row = row;
			this.col = col;
			this.size = size;
			this.id = id;
		}
	}
	
	// N : MAP크기
	// M : Box의 최대 한변 길이
	// totalBoxCount : 현재까지 추가된 box개수 -> id 매겨주기 위해서
	int N, M, totalBoxCount;
	
	Box Boxes[]; // id -> 실제 Box 정보
	ArrayList<Box> Area[][]; // [row/M][col/M] = 해당 영역 위치의 box들
	
	int groupWhoes[]; // union-find구조에서 boss id -> player 번호
	int groupBoxCount[]; // union-find구조에서 boss id -> 해당 그룹의 box개수
	int playerBoxCount[]; // player id -> 해당 player가 갖고 있는 box 개수

    
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
    	
    	// groupBoxCount
    	groupBoxCount[y] += groupBoxCount[x];
    	groupBoxCount[x] = 0;
    	// groupWhoes
    	groupWhoes[y] = pid;
    	groupWhoes[x] = 0;
    }
    
    public void init(int N, int M) {
    	this.N = N;
    	this.M = M;
    	totalBoxCount = 0;
    	Boxes = new Box[LM];
    	Area = new ArrayList[10][10];
    	for(int i = 0; i < 10; i++)
    		for(int j = 0; j < 10; j++)
    			Area[i][j] = new ArrayList<>();
    	groupWhoes = new int[LM];
    	groupBoxCount = new int[LM];
    	playerBoxCount = new int[3];
    	for(int i = 0; i < LM; i++)
    		PARENT[i] = i;
      	return; 
    }
    
    public int add(int row, int col, int size, int pid) {
    	// 박스 생성
    	int boxId = totalBoxCount;
    	totalBoxCount++;
    	Box box = new Box(row, col, size, boxId); // 기본 생성
    	Boxes[boxId] = box;
    	groupWhoes[boxId] = pid;
    	groupBoxCount[boxId] = 1;
    	playerBoxCount[pid]++;

    	int areaRow = row / M;
    	int areaCol = col / M;
    	Area[areaRow][areaCol].add(box);
    	
    	int dr[] = {-1,-1,-1,0,0,0,1,1,1};
    	int dc[] = {-1,0,1,-1,0,1,-1,0,1};
    	
    	for(int i = 0 ; i < 9; i++) {
    		int r = areaRow + dr[i];
    		int c = areaCol + dc[i];
    		if(r < 0 || c < 0 || r >= 10 || c >= 10) continue;
    		
    		for(Box element : Area[r][c]) {
    			if(box.row + box.size <= element.row) continue;
    			if(box.col + box.size <= element.col) continue;
    			if(element.row + element.size <= box.row) continue;
    			if(element.col + element.size <= box.col) continue;
    			
    			// pid가 갖는 box들이 총 몇개?
    			// 상대방의 딱지 중 element의 그룹 box 개수만큼 pid에게 넘어감
    			int bossId = Find(element.id);
    			if(pid != groupWhoes[bossId]) {
	    			playerBoxCount[3 - pid] -= groupBoxCount[bossId];
	    			playerBoxCount[pid] += groupBoxCount[bossId];
    			}
    			Union(element.id, box.id, pid);
    		}
    	}
    	
    	return playerBoxCount[pid]; 
    }
    
    public int get(int row, int col) {
    	int areaRow = row / M;
    	int areaCol = col / M;

    	int dr[] = {-1,-1,-1,0,0,0,1,1,1};
    	int dc[] = {-1,0,1,-1,0,1,-1,0,1};
    	
    	for(int i = 0 ; i < 9; i++) {
    		int r = areaRow + dr[i];
    		int c = areaCol + dc[i];
    		if(r < 0 || c < 0 || r >= 10 || c >= 10) continue;
    		
    		for(Box element : Area[r][c]) {
    			if(row + 1 <= element.row) continue;
    			if(col + 1 <= element.col) continue;
    			if(element.row + element.size <= row) continue;
    			if(element.col + element.size <= col) continue;
				return groupWhoes[Find(element.id)];
    		}
    	}
    	
    	
    	return 0; 
    }
}