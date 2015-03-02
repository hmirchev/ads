# Advanced Data Structures

These are two of the non-trivial and interesting data structures that I
have implemented for university courses.

* Trie

A trie is an ordered tree data structure that stores key-value pairs
where the keys are usually strings. Unlike a regular binary-search tree
where each node would store the whole key that it is associated with, a
trie’s node is part of the key (the node shares a common string prefix
with its ancestors).

For more info, go to: [Trie] (https://en.wikipedia.org/wiki/Trie)

* Fibonacci Heap

A Fibonacci heap is a heap data structures that consists of a collection
of trees. With a little complexity of construction, the Fibonacci heap
achieves better amortized running time for basic operations than several
other data structures. In particular, insert, findMin, decreaseKey all
have a O(1) running time, while deleteMin and delete have a O(logN)
time. This makes Fibonacci heaps a top choice when implementing
algorithms that rely heavily on decreasing the value of key (e.g.
[Dijkstra’s Algorithm]
(https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm))

## Build

Make sure to have JDK and Gradle installed.

* Clone the repo
```shell
git clone git@github.com:hmirchev/ads.git
```

* Buld the project
```shell
./gradlew build
```
