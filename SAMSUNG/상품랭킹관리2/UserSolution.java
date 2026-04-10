package 상품랭킹관리2;

import java.io.*;
import java.util.*;

class UserSolution {
	
	// 객체 크기를 최소화하기 위해 그룹 ID만 보관합니다.
	class Product {
		int id;
		int category;
		int groupId;
		
		public Product(int id, int category, int groupId) {
			this.id = id;
			this.category = category;
			this.groupId = groupId;
		}
	}
	
	HashMap<Integer, Product> hm;
	
	// groups[카테고리][그룹ID] = 해당 그룹에 속한 상품 ID들을 내림차순 정렬하는 셋
	TreeSet<Integer>[][] groups;
	
	// rankToGroup[카테고리][랭크] = 현재 해당 랭크가 가리키는 실제 그룹ID (바구니)
	int[][] rankToGroup;
	
	// 카테고리별 총 상품 개수
	int[] categorySize;

	public void init() {
		hm = new HashMap<>();
		groups = new TreeSet[6][6];
		rankToGroup = new int[6][6];
		categorySize = new int[6];
		
		for (int c = 1; c <= 5; c++) {
			for (int r = 1; r <= 5; r++) {
				// ID가 큰 것이 우선순위가 높으므로 내림차순(ReverseOrder) 정렬
				groups[c][r] = new TreeSet<>(Collections.reverseOrder());
				rankToGroup[c][r] = r; // 초기에는 1랭크 = 1번바구니
			}
		}
	}

	public int add(int mGoodsID, int mCategory, int mRank) {
		int g = rankToGroup[mCategory][mRank]; // 해당 랭크의 바구니 번호 획득
		Product p = new Product(mGoodsID, mCategory, g);
		
		hm.put(mGoodsID, p);
		groups[mCategory][g].add(mGoodsID);
		categorySize[mCategory]++;
		
		return categorySize[mCategory];
	}

	public int remove(int mGoodsID) {
		Product p = hm.get(mGoodsID);
		
		groups[p.category][p.groupId].remove(mGoodsID);
		hm.remove(mGoodsID);
		categorySize[p.category]--;
		
		return categorySize[p.category];
	}

	public int changeRank(int mGoodsID, int mRank) {
		Product p = hm.get(mGoodsID);
		
		// 1. 기존 바구니에서 삭제
		groups[p.category][p.groupId].remove(mGoodsID);
		
		// 2. 새로운 랭크의 바구니를 찾아서 이동
		int newG = rankToGroup[p.category][mRank];
		p.groupId = newG;
		groups[p.category][newG].add(mGoodsID);
		
		// 3. 해당 카테고리에서 가장 랭킹이 높은(랭크 숫자가 작은) 상품 찾기
		for (int r = 1; r <= 5; r++) {
			int g = rankToGroup[p.category][r];
			if (!groups[p.category][g].isEmpty()) {
				// 내림차순 정렬되어 있으므로 first()가 가장 큰 ID입니다.
				return groups[p.category][g].first();
			}
		}
		return -1;
	}

	public int swapRank(int mCategory, int mRank1, int mRank2) {
		int g1 = rankToGroup[mCategory][mRank1];
		int g2 = rankToGroup[mCategory][mRank2];
		
		int count1 = groups[mCategory][g1].size();
		int count2 = groups[mCategory][g2].size();
		
		// 바구니 이름표(Pointer)만 서로 바꿔치기 (O(1))
		if (mRank1 != mRank2) {
			rankToGroup[mCategory][mRank1] = g2;
			rankToGroup[mCategory][mRank2] = g1;
		}
		
		return count1 - count2;
	}
	
	public Main.Result getTopRanks() {
		Main.Result res = new Main.Result();
		// 만약 Result 클래스 내부에 배열 선언이 안 되어 있을 경우를 대비한 안전코드
		if (res.ids == null) res.ids = new int[3]; 
		
		int count = 0;
		
		// 랭크 1부터 5까지 순회하면서 가장 좋은 상품들 수집
		for (int r = 1; r <= 5 && count < 3; r++) {
			List<Integer> temp = new ArrayList<>();
			
			for (int c = 1; c <= 5; c++) {
				int g = rankToGroup[c][r];
				int localCount = 0;
				// 각 카테고리의 현재 랭크에서 최대 3개까지만 뽑아옵니다. (Top 3를 구하므로)
				for (int id : groups[c][g]) {
					temp.add(id);
					localCount++;
					if (localCount == 3) break;
				}
			}
			
			// 모인 상품들을 ID 내림차순으로 정렬
			temp.sort(Collections.reverseOrder());
			
			// Top 3에 채워 넣기
			for (int id : temp) {
				res.ids[count++] = id;
				if (count == 3) break;
			}
		}
		
		// 3개를 다 채우지 못했다면 나머지는 -1 처리
		while (count < 3) {
			res.ids[count++] = -1;
		}
		
		return res; 
	}
}