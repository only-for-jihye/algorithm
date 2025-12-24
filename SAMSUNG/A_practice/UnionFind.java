package A_practice;

import java.util.HashMap;

public class UnionFind {

	public static void main(String[] args) {
		
		idToString = new String[10];
		// idToString
		HashMap<String, Integer> hm = new HashMap<>();
		String str = "A";
		int idCount = 0;
		if (hm.get(str) == null) {
			hm.put(str, idCount++);
		}
		int id = hm.get(str);
		idToString[id] = str;
		
		System.out.println(idToString[0]);
		System.out.println(hm.toString());
	}
	
	static int[] parents;
	static int[] wordCount;
	static int[] groupCount;
	static String[] idToString;
	
	int find(int n) {
		if (parents[n] == n) return n;
		return parents[n] = find(parents[n]);
	}
	
	void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if (pa == pb) return;
		
		int winner = -1;
		int loser = -1;
		
		if (wordCount[pa] > wordCount[pb]) {
			winner = pa;
			loser = pb;
		} else if (wordCount[pa] < wordCount[pb]) {
			winner = pb;
			loser = pa;
		} else {
			if (idToString[pa].compareTo(idToString[pb]) < 0) {
				winner = pa;
				loser = pb;
			} else {
				winner = pb;
				loser = pa;
			}
		}
		
		parents[loser] = winner;
		groupCount[winner] += groupCount[loser];
	}
	
}

// 재은프로님 사각형 겹침 풀이 공식
class NodeInfo {
    // C++ struct는 기본적으로 public이므로, 알고리즘 문제 풀이용으로 public 필드 사용
    public int id;
    public int x, y;
    public int w, h;
    public int size;

    // 1. 기본 생성자
    public NodeInfo() {
        this.id = -1;
        this.x = -1;
        this.y = -1;
        this.w = 0;
        this.h = 0;
        this.size = 0;
    }

    // 2. 파라미터 생성자
    public NodeInfo(int id, int x, int y, int w, int h) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.size = 0;
    }

    // 3. 연산자 오버로딩 (==) -> equals 메서드 오버라이드
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NodeInfo other = (NodeInfo) obj;
        return this.id == other.id;
    }

    // (!=) 연산자는 Java에서 !a.equals(b) 로 사용하면 되므로 별도 구현 불필요

    // 4. 겹침 확인 로직 (AABB 충돌 감지)
    public boolean overlap(NodeInfo rec) {
        // 겹치지 않는 4가지 경우 (상, 하, 좌, 우로 완전히 벗어난 경우)
        boolean cond1 = this.x + this.w <= rec.x;    // 내가 rec보다 왼쪽에 있음
        boolean cond2 = this.y + this.h <= rec.y;    // 내가 rec보다 위쪽에 있음
        boolean cond3 = this.y >= rec.y + rec.h;     // 내가 rec보다 아래쪽에 있음
        boolean cond4 = this.x >= rec.x + rec.w;     // 내가 rec보다 오른쪽에 있음

        // 위 조건 중 하나라도 만족하면 겹치지 않음 (false)
        if (cond1 || cond2 || cond3 || cond4) return false;
        
        // 아니면 겹침 (true)
        return true;
    }
    
    // 디버깅용 toString (선택 사항)
    @Override
    public String toString() {
        return "NodeInfo [id=" + id + ", x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + "]";
    }
}