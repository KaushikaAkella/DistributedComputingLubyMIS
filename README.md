# DistributedComputingLubyMIS 
Implement LubyMIS distributed algorithm using Multi threading 

Team Members & contributions - 
Contribution - The entire project was developed, tested and debugged together by us (P.S We are roommates). Specific modules in which we worked on - 
Vijaya Kaushika Akella - VXA180028 - FileReader.java, Node.java, MasterThread.java, Main.java
Padma Priya Cheruvu - PPC180000 - SharedMemory.java, Node.java, MasterThread.java, Main.java

Instructions to Execute - 
This program was developed to run on Java version > 1.7
1. Make sure that all the files are in a folder - Distributed-Computing-LubyMIS - "input.txt" in the same folder as other files
2. Compile Main.java program 
3. Upon successful compilation, run "java Main" to see the MIS output 

Project Description - 
This folder consists of the following files
1. input.txt - This file consists of the input data - number of processes, process ids and adjancecy matrix of the graph - considering that each of this is in separate line and they are all space separated values.  
2. FileReader.java - This file reads the "input.txt" and saves the data. 
3. SharedMemory.java - This file consists of a class which has all the variables which are in shared memory used for inter thread communication 
4. Node.java - This file consists of the process thread execution - LubyMIS algorithm run by each process
5. MasterThread.java - This file consists of the Master Thread execution - makes sure that n processes are run concurrently 
6. Main.java - This file is the main file which runs the master thread and does MIS verification
7. Screenshots folder - consists of the output screenshots for the provided input.txt adjancency matrix