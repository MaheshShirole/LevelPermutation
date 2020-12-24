# LevelPermutation
Level permutation algorithm (LevelP ermute) takes input activity graph and outputs  a  set  of  test  scenarios concurrent activity diagrams.
In the level permutation approach, all the activities inside the fork-join are divided into different levels. 
Activities, which are the immediate neighbors of the fork, are in the first level, the immediate neighbors of the first level are in the second level, and so on up to the join node.
Then, activities at each level are permuted to generate the subsequences.
After that, current level each subsequence is augmented with the previous level subsequences.
This includes adding first level subsequences in first iteration. 
Then, algorithm increases size of TS to accommodate all possible combinations of TS and new subsequences in each level. After that, sub-sequences of each level are appended to the TS. 
Finally, this process returns a set of interleaving test scenarios inside the fork-join structure.
