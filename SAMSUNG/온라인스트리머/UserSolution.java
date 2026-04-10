package 온라인스트리머;

import java.io.*;
import java.util.*;

class UserSolution {
    
    private int[] subs;
    private int[] treeSum;
    private int[] treeMax;
    private int[] treeMin;
    private int n;

    public void init(int N, int[] mSubscriber) {
        this.n = N;
        
        // N의 크기에 맞춰 트리 및 배열 동적 할당 (넉넉하게 N * 4)
        subs = new int[N + 1];
        treeSum = new int[N * 4];
        treeMax = new int[N * 4];
        treeMin = new int[N * 4];
        
        build(1, 1, N, mSubscriber);
    }

    // 세그먼트 트리 초기 구성
    private void build(int node, int start, int end, int[] mSubscriber) {
        if (start == end) {
            int val = mSubscriber[start - 1]; // mSubscriber는 0-index, start는 1-index
            subs[start] = val;
            treeSum[node] = val;
            treeMax[node] = val;
            treeMin[node] = val;
            return;
        }
        
        int mid = (start + end) / 2;
        build(node * 2, start, mid, mSubscriber);
        build(node * 2 + 1, mid + 1, end, mSubscriber);
        
        treeSum[node] = treeSum[node * 2] + treeSum[node * 2 + 1];
        treeMax[node] = Math.max(treeMax[node * 2], treeMax[node * 2 + 1]);
        treeMin[node] = Math.min(treeMin[node * 2], treeMin[node * 2 + 1]);
    }

    // 단일 값 업데이트
    private void update(int node, int start, int end, int idx, int val) {
        if (idx < start || idx > end) {
            return;
        }
        if (start == end) {
            treeSum[node] = val;
            treeMax[node] = val;
            treeMin[node] = val;
            return;
        }
        
        int mid = (start + end) / 2;
        update(node * 2, start, mid, idx, val);
        update(node * 2 + 1, mid + 1, end, idx, val);
        
        treeSum[node] = treeSum[node * 2] + treeSum[node * 2 + 1];
        treeMax[node] = Math.max(treeMax[node * 2], treeMax[node * 2 + 1]);
        treeMin[node] = Math.min(treeMin[node * 2], treeMin[node * 2 + 1]);
    }

    // 구간 합 쿼리
    private int querySum(int node, int start, int end, int left, int right) {
        if (left > end || right < start) {
            return 0;
        }
        if (left <= start && end <= right) {
            return treeSum[node];
        }
        int mid = (start + end) / 2;
        return querySum(node * 2, start, mid, left, right) + querySum(node * 2 + 1, mid + 1, end, left, right);
    }

    // 구간 최댓값 쿼리
    private int queryMax(int node, int start, int end, int left, int right) {
        if (left > end || right < start) {
            return Integer.MIN_VALUE;
        }
        if (left <= start && end <= right) {
            return treeMax[node];
        }
        int mid = (start + end) / 2;
        return Math.max(queryMax(node * 2, start, mid, left, right), queryMax(node * 2 + 1, mid + 1, end, left, right));
    }

    // 구간 최솟값 쿼리
    private int queryMin(int node, int start, int end, int left, int right) {
        if (left > end || right < start) {
            return Integer.MAX_VALUE;
        }
        if (left <= start && end <= right) {
            return treeMin[node];
        }
        int mid = (start + end) / 2;
        return Math.min(queryMin(node * 2, start, mid, left, right), queryMin(node * 2 + 1, mid + 1, end, left, right));
    }

    // API 구현
    public int subscribe(int mId, int mNum) {
        subs[mId] += mNum;
        update(1, 1, n, mId, subs[mId]);
        return subs[mId];
    }

    public int unsubscribe(int mId, int mNum) {
        subs[mId] -= mNum;
        if (subs[mId] < 0) subs[mId] = 0; // 안전 장치: 음수 방지
        update(1, 1, n, mId, subs[mId]);
        return subs[mId];
    }

    public int count(int sId, int eId) {
        return querySum(1, 1, n, sId, eId);
    }

    public int calculate(int sId, int eId) {
        int max = queryMax(1, 1, n, sId, eId);
        int min = queryMin(1, 1, n, sId, eId);
        return max - min;
    }
}