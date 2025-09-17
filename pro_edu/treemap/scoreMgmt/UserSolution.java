package pro_edu.treemap.scoreMgmt;

import java.util.*;

class UserSolution {

	TreeMap<Student, Boolean> students[];
	HashMap<Integer, Student> idToStudent;
	
    public void init() {
    	idToStudent = new HashMap<>();
    	students = new TreeMap[32];
    	for (int i = 0; i < 32; i++) {
    		students[i] = new TreeMap<>();
    	}
    }
    
    int getIdx(int grade, String mGender) {
    	return grade * 10 + (mGender.equals("male") ? 0 : 1);
    }
//    int getIdx(int grade, String gender) {
//    	if(gender.equals("male")) return grade * 10 + 0;
//    	else return grade * 10 + 1;
//    }

    // 20,000번 호출
    public int addScore(int mId, int mGrade, String mGender, int mScore) {
    	int idx = getIdx(mGrade, mGender);
    	Student student = new Student(mId, mScore, idx);
    	students[student.index].put(student, null);
    	idToStudent.put(mId, student);
    	
    	return students[student.index].lastKey().id;
    }

    // id -> data로 access할 수 있게 해야함
    // key : id -> value : data => 시간복잡도 1의 반복
    // TreeMap erase : logN -> long20000 -> 16
    public int removeScore(int mId) {
    	Student student = idToStudent.get(mId);
    	if (student == null) return 0;
    	idToStudent.remove(mId);
    	students[student.index].remove(student);
    	if (students[student.index].isEmpty()) return 0; // 1개 있었는데 삭제되었으면 0 반환
        return students[student.index].firstKey().id;
    }

    // grade, gender를 이용 -> TreeMap<기준 score,id> data -> mScore 이상이면서 가장 작은 score (ceiling)
    // [grade. gender] -> 데이터 추리기 (데이터 양을 작게 만듦)
    // DAT index -> [grade,gender]
    // TreeMap<Student, Boolean> students[];
    // Student <- score, id, studentsIndex 필요하다. remove 때문에 studentsIndex가 필요함
    // 최악의 경우, 6가지 조합을 전부 확인 -> 6 * logN
    public int get(int mGradeCnt, int mGrade[], int mGenderCnt, String mGender[], int mScore) {
    	
    	Student minScoreStudent = new Student(0, Integer.MAX_VALUE, -1); // 최소 점수에 해당하는 학생
    	for (int i = 0; i < mGradeCnt; i++) {
    		for (int j = 0; j < mGenderCnt; j++) {
    			int index = getIdx(mGrade[i], mGender[j]);
    			TreeMap<Student, Boolean> studentTree = students[index];
    			Student threshold = new Student(-1, mScore, 0); // 커스텀 class 사용 시, id와 같은 값의 설정이 중요하다.
    			Student student = studentTree.ceilingKey(threshold);
    			if (student != null && minScoreStudent.compareTo(student) > 0) {
    				minScoreStudent = student; // 최소 점수 학생 갱신
    			}
    		}
    	}
        return minScoreStudent.id;
    }
}

class Student implements Comparable<Student> {
	int id;
	int score;
	int index;
	public Student(int id, int score, int index) {
		super();
		this.id = id;
		this.score = score;
		this.index = index;
	}
	@Override
	public int compareTo(Student o) {
		if (this.score < o.score) return -1;
		if (this.score > o.score) return 1;
		if (this.id < o.id) return -1;
		if (this.id > o.id) return 1;
		if (this.index < o.index) return -1;
		if (this.index > o.index) return 1;
		return 0;
	}
	
}