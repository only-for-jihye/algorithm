package AD_20240710_기출;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Solution {
    static int w; // 가로
    static int h; // 세로
    static int n; // N번 접기

    static int horizontal = 0; // 가로로 접고
    static int vertital = 1; // 세로로 접고

    static Card[][] cards;
    static Card card;

    static int direction; // 방향
    static int range; // 구멍뚫을 곳

    static int w_start;
    static int h_start;

    static int hole_x;
    static int hole_y;
    static int final_x;
    static int final_y;

    public static void main(String[] args) throws Exception {
        FileInputStream fls = new FileInputStream("sample.txt");
        System.setIn(fls);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int T = Integer.parseInt(br.readLine());
        StringTokenizer st;
        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            w = Integer.parseInt(st.nextToken());
            h = Integer.parseInt(st.nextToken());
            w_start = 0;
            h_start = 0;
            cards = new Card[w + 1][h + 1];
            for (int ww = 1; ww <= w; ww++) {
                for (int hh = 1; hh <= h; hh++) {
                    cards[ww][hh] = new Card(ww, hh, false);
                }
            }
            
            for (int j = 0; j < n; j++) {
                // 접기 시작
                st = new StringTokenizer(br.readLine());
                direction = Integer.parseInt(st.nextToken());
                range = Integer.parseInt(st.nextToken());
                start(direction, range);
                hole();
            }

            st = new StringTokenizer(br.readLine());
            hole_x = Integer.parseInt(st.nextToken());
            hole_y = Integer.parseInt(st.nextToken());
            final_x = Integer.parseInt(st.nextToken());
            final_y = Integer.parseInt(st.nextToken());

        }
        

    }

    static void hole() {
        
    }

    static void start(int direction, int range) {
        if (direction == 0) { // 가로
            w_start += range;
        } else { // 세로
            h_start += range;
        }
    }

    static class Card {
        int x;
        int y;
        boolean hole;

        Card(int x, int y, boolean hole) {
            this.x = x;
            this.y = y;
            this.hole = hole;
        }
    }
}

