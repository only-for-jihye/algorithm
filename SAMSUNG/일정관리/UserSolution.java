package 일정관리;

import java.util.*;

class UserSolution {
	
	class Schedule implements Comparable<Schedule> {
		String subject;
		int start;
		int end;
		
		public Schedule(String subject, int start, int end) {
			super();
			this.subject = subject;
			this.start = start;
			this.end = end;
		}

		@Override
		public int compareTo(Schedule o) {
			if (this.start != o.start) return Integer.compare(this.start, o.start);
			return this.subject.compareTo(o.subject);
		}
	}
	
	HashMap<String, Schedule> hm;
	TreeSet<Schedule> set;
	int N;
	
	// N 최대 1,000,000
	public void init(int N)	{
		this.N = N;
		hm = new HashMap<>();
		set = new TreeSet<>();
	}
	
	// 30,000
	public int addSchedule(String mTitle, int mStartDay, int mEndDay, int mForced) {
		
	    Schedule prevSchedule = set.lower(new Schedule("", mStartDay + 1, 0));
	    ArrayList<Schedule> al = new ArrayList<>();
	    
	    boolean isOverlap = false;
	    
	    // 앞에 일정이 있고, 그 일정의 종료 시간이 내 시작 시간과 같거나 크면 겹침
	    if (prevSchedule != null && prevSchedule.end >= mStartDay) {
	        // 단, 시작 시간이 완전히 똑같은 경우는 아래 subSet에서 잡히므로 중복 방지
	        if (prevSchedule.start < mStartDay) { 
	            isOverlap = true;
	            al.add(prevSchedule);
	        }
	    }
		
	    Schedule dummyStart = new Schedule("", mStartDay, 0);
	    Schedule dummyEnd = new Schedule("", mEndDay + 1, 0);
	    // subset dummyStart <= innerSchedule < dummyEnd
		SortedSet<Schedule> innerSchedule = set.subSet(dummyStart, dummyEnd);
		
		// 신규 일정과 겹치는 일정이 있는지 ?
		if (!innerSchedule.isEmpty()) {
			al.addAll(innerSchedule);
			isOverlap = true;
		}
		
		if (isOverlap) {
			if (mForced == 0) { // 일반모드
				return 0;
			} else { // 강제 모드
				for (Schedule schedule : al) {
					set.remove(schedule);
					hm.remove(schedule.subject);
				}
			}
		}
		
		Schedule newSchedule = new Schedule(mTitle, mStartDay, mEndDay);
		hm.put(mTitle, newSchedule);
		set.add(newSchedule);
		return 1;
	}
	
	
	// 20,000
	public Main.RESULT getSchedule(int mDay) {
		
		Main.RESULT result = new Main.RESULT();
		
		// mDay보다 같거나 작은 일정 검색
//		Schedule prevSchedule = set.floor(new Schedule("", mDay, 0));
		Schedule prevSchedule = set.lower(new Schedule("", mDay + 1, 0));
		
		if (prevSchedule != null && prevSchedule.end >= mDay) {
			result.mTitle = prevSchedule.subject;
			result.mStartDay = prevSchedule.start;
			result.mEndDay = prevSchedule.end;
		} else {
			result.mTitle = "$";
		}
		
		return result;
	}
	
	// 5,000
	public int deleteSchedule(String mTitle) {
		Schedule schedule = hm.get(mTitle);
		if (schedule != null) {
			hm.remove(mTitle);
			set.remove(schedule);
			return 1;
		} else {
			return 0;
		}
	}
	
	// 100
	public int findEmptySchedule() {
	    if (set.isEmpty()) return N; // 일정 없으면 그냥 N 리턴
	    
	    int maxEmpty = 0;
	    int prevEnd = 0;
	    
	    // 일정 사이의 빈 공간 계산
	    for (Schedule current : set) {
	        int currentEmpty = current.start - prevEnd - 1;
	        if (currentEmpty > maxEmpty) {
	            maxEmpty = currentEmpty;
	        }
	        // [수정 포인트 1] currentEmpty가 아니라 현재 일정의 종료일(current.end)로 갱신해야 합니다.
	        prevEnd = current.end; 
	    }
	    
	    // 마지막 일정 종료일 ~ N일(끝)까지의 빈 공간 계산
	    // [수정 포인트 2] 마지막 일정이 6일에 끝났고 N이 10이라면, 7,8,9,10일 (총 4일)이 빕니다. 
	    // 즉 (10 - 6 - 1)이 아니라 (10 - 6) = 4 가 되어야 합니다.
	    int emptyEnd = N - prevEnd; 
	    if (emptyEnd > maxEmpty) {
	        maxEmpty = emptyEnd;
	    }
	    
	    return maxEmpty;
	}
}