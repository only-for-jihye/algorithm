# 우선 순위 Queue
# 가장 우선순위가 높은 데이터

# 우선순위 Queue를 구현하는 방법
# 1. List를 이용한다.
# 2. Heap을 이용한다.

# 시간 복잡도
# List : 삽입 - O(1), 삭제 - O(N)
# Heap : 삽입, 삭제 - O(logN) -> 동일하게 구성하면 O(NlogN) -> 힙 정렬, 정렬 알고리즘

# min heap
# - 루트 노드가 가장 작은 값을 가짐

# max heap
# - 루트 노드가 가장 큰 값을 가짐

# 완전 이진트리


# 파이썬의 heapq 라이브러리는 오름차순으로 제공한다.

import sys
import heapq
input = sys.stdin.readline

def heapsort(iterable) :
    h = []
    result = []
    # 모든 원소를 차례대로 heap에 삽입
    for value in iterable :
        heapq.heappush(h, value)
    # heap에 삽입된 모든 원소들을 차례대로 꺼내어 담기
    for i in range(len(h)):
        result.append(heapq.heappop(h))
    return result

n = int(input)
arr = []

for i in range(n):
    arr.append(int(input()))

res = heapsort(arr)

for i in range(n):
    print(res[i])


