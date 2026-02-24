package 당근마켓;

import java.io.*;
import java.util.*;

class UserSolution {
	
	final int TAG_SIZE = 30;
	
	HashMap<String, Integer> tagNameToTagId;
	int carrotCnt;
	boolean[] isSold;
	int[] prices;
	
	ArrayList<Integer>[] threeTag;
	ArrayList<Integer>[] oneTag;
	
	public void init(int N) {
		tagNameToTagId = new HashMap<>();
		carrotCnt = 0;
		isSold = new boolean[30000];
		prices = new int[30000];
		
		oneTag = new ArrayList[TAG_SIZE];
		threeTag = new ArrayList[TAG_SIZE * TAG_SIZE * TAG_SIZE];
		
		for (int i = 0; i < TAG_SIZE; i++) {
			oneTag[i] = new ArrayList<>();
		}
		for (int i = 0; i < TAG_SIZE * TAG_SIZE * TAG_SIZE; i++) {
			threeTag[i] = new ArrayList<>();
		}
	}
	
	int getTagId(String tagName) {
		if (!tagNameToTagId.containsKey(tagName)) {
			tagNameToTagId.put(tagName, tagNameToTagId.size());
		}
		return tagNameToTagId.get(tagName);
	}
	
	int getThreeTagId(int id1, int id2, int id3) {
		int[] ids = {id1, id2, id3};
		Arrays.sort(ids);
		int idx = 0;
		for (int i = 0; i < 3; i++) {
			idx = idx * TAG_SIZE + ids[i];
		}
		return idx;
	}

	public void addCarrot(int price, int tagCnt, String tagName[]) {
		int carrotId = carrotCnt;
		carrotCnt++;
		prices[carrotId] = price;
		isSold[carrotId] = false;
		
		ArrayList<Integer> tagIds = new ArrayList<>();
		for (int i = 0; i < tagCnt; i++) {
			int tagId = getTagId(tagName[i]);
			tagIds.add(tagId);
			oneTag[tagId].add(carrotId);
		}
		
		for (int i = 0; i < tagCnt; i++) {
			for (int j = i + 1; j < tagCnt; j++) {
				for (int k = j + 1; k < tagCnt; k++) {
					int idx = getThreeTagId(tagIds.get(i), tagIds.get(j), tagIds.get(k));
					threeTag[idx].add(carrotId);
				}
			}
		}
	}

	public int sellCarrot(String tag1, String tag2, String tag3) {
		int id1 = getTagId(tag1);
		int id2 = getTagId(tag2);
		int id3 = getTagId(tag3);
		
		int threeTagId = getThreeTagId(id1, id2, id3);
		ArrayList<Integer> carrotList = threeTag[threeTagId];
		
		if (carrotList.isEmpty()) return -1;
		
		int minPrice = Integer.MAX_VALUE;
		int minPriceCarrotId = -1;
		for (int carrotId : carrotList) {
			if (isSold[carrotId]) continue;
			if (minPrice > prices[carrotId]) {
				minPrice = prices[carrotId];
				minPriceCarrotId = carrotId;
			}
		}
		
		if (minPriceCarrotId == -1) return -1;

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