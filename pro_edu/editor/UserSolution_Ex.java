package pro_edu.editor;

import java.util.*;

public class UserSolution_Ex {

    static int[] wordCnt;
    static ArrayDeque<Integer> line1;
    static ArrayDeque<Integer> line2;

    public void init(int mCnt1, int[] mDigitList1, int mCnt2, int[] mDigitList2) {
        wordCnt = new int[1000];
        line1 = new ArrayDeque<>();
        line2 = new ArrayDeque<>();

        for (int i=0; i<mCnt1-2; i++) {
            line1.add(mDigitList1[i]);
            int key = 0; 
            key += 100*mDigitList1[i];
            key += 10*mDigitList1[i+1];
            key += mDigitList1[i+2];
            wordCnt[key] += 1;                                
        }

        line1.add(mDigitList1[mCnt1-2]);
        line1.add(mDigitList1[mCnt1-1]);

        for (int i=0; i<mCnt2-2; i++) {
            line2.add(mDigitList2[i]);
            int key = 0; 
            key += 100*mDigitList2[i];
            key += 10*mDigitList2[i+1];
            key += mDigitList2[i+2];
            wordCnt[key] += 1;
        }

        line2.add(mDigitList2[mCnt2-2]);
        line2.add(mDigitList2[mCnt2-1]);
    }

    public void append(int mDir, int mNum1, int mNum2) {

        if (mDir == 0) { // left
            String num1 = String.valueOf(mNum1);
            for (int i=num1.length()-1; i>=0; i--) {
                int val = Integer.parseInt(String.valueOf(num1.charAt(i)));
                Iterator<Integer> iterator = line1.iterator();
                int key = 0; 
                key += 100*val;
                key += 10*(iterator.next());
                key += iterator.next();
                wordCnt[key] += 1;
                line1.addFirst(val);
            }

            String num2 = String.valueOf(mNum2);
            for (int i=num2.length()-1; i>=0; i--) {
                int val = Integer.parseInt(String.valueOf(num2.charAt(i)));
                Iterator<Integer> iterator = line2.iterator();
                int key = 0; 
                key += 100*val;
                key += 10*(iterator.next());
                key += iterator.next();
                wordCnt[key] += 1;
                line2.addFirst(val);
            }

        } else { // right

            String num1 = String.valueOf(mNum1);
            for (int i=0; i<num1.length(); i++) {
                int val = Integer.parseInt(String.valueOf(num1.charAt(i)));
                Iterator<Integer> iterator = line1.descendingIterator();
                int key = 0; 
                key += val;
                key += 10*(iterator.next());
                key += 100*iterator.next();
                wordCnt[key] += 1;
                line1.addLast(val);
            }

            String num2 = String.valueOf(mNum2);
            for (int i=0; i<num2.length(); i++) {
                int val = Integer.parseInt(String.valueOf(num2.charAt(i)));
                Iterator<Integer> iterator = line2.descendingIterator();
                int key = 0; 
                key += val;
                key += 10*(iterator.next());
                key += 100*iterator.next();
                wordCnt[key] += 1;
                line2.addLast(val);
            }
        }
    }

    public int countNum(int mNum) {

        int result = wordCnt[mNum];
        Iterator<Integer> iterator; 
                        
        int a = 0;
        iterator = line1.descendingIterator();
        a += 10*iterator.next();
        a += 100*iterator.next();
        a += line2.getFirst();
        if (a == mNum) {
            result++;
        } 

        a = 0;
        a += 100*(line1.getLast());
        iterator = line2.iterator();
        a += 10*(iterator.next());
        a += iterator.next();
        if (a == mNum) {
            result++;
        } 
        return result;
    }
}
