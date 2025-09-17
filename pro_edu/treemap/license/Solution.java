package pro_edu.treemap.license;

import java.io.*;
import java.util.*;

class Solution 
{
	TreeMap<Integer, String> lc = new TreeMap<>();
	
	public void init() 
	{
		lc.clear();
	}
	
	public void addExam(int level, String name) 
	{
		if (lc.containsKey(level)) return;
		lc.put(level, name);
	}
	
	public String getCustomExam(int studyAmount) 
	{
		NavigableMap<Integer, String> lcmap = lc.subMap(1, true, studyAmount, true);
		if (lcmap.isEmpty()) return "noExam";
		return lcmap.get(lcmap.lastKey());
//		Map.Entry<Integer, String> exam = lc.floorEntry(studyAmount);
//		if (exam == null) return "noExam";
//		return exam.getValue();
	}	
	
	public int changeExamLevel(int prevLevel, int AfterLevel) 
	{
		if (!lc.containsKey(prevLevel)) return -1;
		String license = lc.get(prevLevel);
		
		lc.remove(prevLevel);
		// prevLevel < afterLevel : -1 씩 조정
		// prevLevel > afterLevel : +1 씩 조정
		int sign = 1;
		if (prevLevel < AfterLevel) sign = -1;
		
		while (lc.containsKey(AfterLevel)) {
			AfterLevel += sign;
		}
		
		lc.put(AfterLevel, license);
		return AfterLevel;
//		if (!lc.containsKey(AfterLevel)) {
//			lc.put(AfterLevel, license);
//			lc.remove(prevLevel);
//			return AfterLevel;
//		} else {
//			NavigableMap<Integer, String> lcmap = lc.subMap(Math.min(AfterLevel, prevLevel), false, Math.max(AfterLevel, prevLevel), false);
//			int newLevel = AfterLevel;
//			int newLevel2 = AfterLevel;
//			while (true) {
//				newLevel--;
//				newLevel2++;
//				if (!lcmap.containsKey(newLevel) || !lcmap.containsKey(newLevel2)) break;
//			}
//			
//			if (newLevel == prevLevel) return prevLevel;
//			if (newLevel2 == prevLevel) return prevLevel;
//			lc.put(newLevel, license);
//			lc.remove(prevLevel);
//			
//			return newLevel;
//		}
	}
	
	public String getMaxExam() 
	{
		return lc.get(lc.lastKey());
//		return lc.lastEntry().getValue();
	}
	
	public String getMinExam() 
	{
		return lc.get(lc.firstKey());
//		return lc.firstEntry().getValue();
	}
	
	public int countRangeExam(int A, int B)
	{
		NavigableMap<Integer, String> lcmap = lc.subMap(A, true, B, true);
		
		return lcmap.size(); 
	}
}