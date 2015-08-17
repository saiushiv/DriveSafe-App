# Raymond-Protocol-for-Distributed-Systems
Implemented Raymond Token based Mutual Exclusion Algorithm on Distributed Linux systems.

NOTE: THIS PROJECT WAS IMPLEMENTED IN A TEAM ENVIRONMENT.

Compile the main program MutexMain.java
> javac MutexMain.java
-  Once the program is compiled it generates all the required class file.
- Now run the launcher script:
> sh launcher.sh
- It reads all the parameters from the configuration file. Inside which number of critical section requests per node, mean delay between two consecutive critical section requests and mean duration of critical section are also specified. In our case we have dedicated node 1 to be the holder of token for the first time (can be changed if needed).

The service writes all its activity in the folder called “Critical_Section” and can be used to debug as well as check its overall activity node-wise.
<PATH>/Critical_Section
- The file which we are interested to check for mutual exclusion property is “CS_Info.txt”.
- Once all the nodes are finished executing their CSs, we can check its correctness by:

> java UniqueLineReader

Extra functions:
To achieve all this initially it created spanning tree using message passing using TCP.
