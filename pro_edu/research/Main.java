package pro_edu.research;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
	
	// 1. 나라별로 id값 <- 함수화
	// 2. id값 2개를 조합 => DAT의 index
	
	static HashMap<String, Integer> NationName2Id = new HashMap<>();
	static int getID(String nationName) {
		if(!NationName2Id.containsKey(nationName))
			NationName2Id.put(nationName, NationName2Id.size());
		return NationName2Id.get(nationName);
	}
	
	static int getIndex(int id1, int id2) {
		// id의 범위 : 0 ~ 199 <- 200진법 이상의 수로 합치면 된다!
		if(id1 < id2)
			return id1 * 1000 + id2;
		else
			return id2 * 1000 + id1;
	}
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int N = Integer.parseInt(br.readLine());
		
		int DAT[] = new int[200 * 1000]; 
		// [2개 나라의 id 조합 <- ] = 해당 나라의 피를 갖는 아이 수
		
		// 각 아이별로 어떤 피의 조합을 갖는지 확인
		// 2개 나라의 피 조합에 해당하는 아이가 몇 명인지 사전에 계산
		for(int i = 0; i < N; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int nation_cnt = Integer.parseInt(st.nextToken());
			ArrayList<Integer> nation_id_list = new ArrayList<>();
			for(int j = 0; j < nation_cnt; j++) {
				String nation = st.nextToken();
				int nation_id = getID(nation);
				nation_id_list.add(nation_id);
			}
			for(int j = 0; j < nation_cnt; j++) {
				for(int k = j + 1; k < nation_cnt; k++) {
					int id1 = nation_id_list.get(j);
					int id2 = nation_id_list.get(k);
					int index = getIndex(id1, id2);
					DAT[index]++;
				}
			}
		}
		
		int Q = Integer.parseInt(br.readLine());
		for(int q = 0; q < Q; q++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int id1 = getID(st.nextToken());
			int id2 = getID(st.nextToken());
			int index = getIndex(id1, id2);
			System.out.print(DAT[index] + " ");
		}
	}
	
}
