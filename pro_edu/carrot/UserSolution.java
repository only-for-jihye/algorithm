package pro_edu.carrot;

import java.io.*;
import java.util.*;

class UserSolution {
	
	final int TAG_SIZE = 30;
	
	int carrotCnt;
	int prices[];
	boolean isSold[];
	
	HashMap<String, Integer> tagNameToTagId;
	
	ArrayList<Integer> threeTag[];
	ArrayList<Integer> oneTag[];
	
	int getTagId(String tagName) {
		if (!tagNameToTagId.containsKey(tagName)) {
			tagNameToTagId.put(tagName, tagNameToTagId.size());
		}
		return tagNameToTagId.get(tagName);
	}
	
	int getThreeTagIndex(int id1, int id2, int id3) {
		int ids[] = {id1, id2, id3};
		Arrays.sort(ids); // 크기 순 정렬 -> 순서가 달라도 같은 조합으로 취급받도록!!
		int index = 0;
		for (int i = 0; i < 3; i++) {
			index = index * TAG_SIZE + ids[i];
		}
		return index;
	}
	
	public void init(int N) {
		carrotCnt = 0;
		prices = new int[30000];
		isSold = new boolean[30000];
		tagNameToTagId = new HashMap<>();
		oneTag = new ArrayList[TAG_SIZE];
		for (int i = 0; i < TAG_SIZE; i++) {
			oneTag[i] = new ArrayList<>();
		}
		threeTag = new ArrayList[TAG_SIZE * TAG_SIZE * TAG_SIZE];
		for (int i = 0; i < TAG_SIZE * TAG_SIZE * TAG_SIZE; i++) {
			threeTag[i] = new ArrayList<>();
		}
	}

	public void addCarrot(int price, int tagCnt, String tagName[]) {
		int carrotId = carrotCnt;
		carrotCnt++;
		prices[carrotId] = price;
		isSold[carrotId] = false;
		
		ArrayList<Integer> tagIds = new ArrayList<>();
		for (int i = 0; i < tagCnt; i++) { // 태그 1개에 대한 처리
			int tagId = getTagId(tagName[i]);
			tagIds.add(tagId);
			oneTag[tagId].add(carrotId);
		}
		
		for (int i = 0; i < tagCnt; i++) {
			for (int j = i + 1; j < tagCnt; j++) { // 중복 tag가 없게 i + 1로 세팅
				for (int k = j + 1; k < tagCnt; k++) { // 상동
					int index = getThreeTagIndex(tagIds.get(i), tagIds.get(j), tagIds.get(k));
					threeTag[index].add(carrotId);
				}
			}
		}
	}
	
	public int sellCarrot(String tag1, String tag2, String tag3) {
		int id1 = getTagId(tag1);
		int id2 = getTagId(tag2);
		int id3 = getTagId(tag3);
		
		int index = getThreeTagIndex(id1, id2, id3);
		ArrayList<Integer> carrotList = threeTag[index];
		
		if (carrotList.isEmpty()) return -1;
		
		int minPrice = Integer.MAX_VALUE;
		int minPriceCarrotId = -1;
		for (int carrotId : carrotList) { // 가격이 제일 저렴한 당근 찾기
			if (isSold[carrotId]) continue;
			if (minPrice > prices[carrotId]) {
				minPrice = prices[carrotId];
				minPriceCarrotId = carrotId;
			}
		}
		// 태그로 검색된게 없을 경우
		if (minPriceCarrotId == -1) return -1;
		
		// 어떤 당근이 팔렸는지 삭제해야 하는데, 실제로 삭제처리는 하지 않음 lazy delete
		isSold[minPriceCarrotId] = true;
		
		return minPrice;
	}

	public void updatePrice(String tag1, int addPrice) {
		int tagId = getTagId(tag1);
		ArrayList<Integer> carrotList = oneTag[tagId];
		for (int carrotId : carrotList) {
			if (isSold[carrotId]) continue;
			prices[carrotId] += addPrice;
		}
	}
}
