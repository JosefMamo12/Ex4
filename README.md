# Ex4 Pokemon Game (One Piece Catch the Pirate Game)
## Game Process
* Put the amount of pirates that we got from the server on one of the edges
* Put the marine soldier on one of the vertices
* Search for the closest marine soldier to a pirate
* Chase after the pirate
* After every catch the grade go up depent on the value of the pirate

![GifMaker_20220109105727517](https://user-images.githubusercontent.com/73185009/148676111-2dd2e154-938a-45b1-8513-19347c042417.gif)

## Game Components

| Pirate | Points | 
| --- | --- | 
| **![Luffy](https://github.com/JosefMamo12/Ex4/blob/master/resources/luffy.png)** | 11 - 15 |
| ![Zoro](https://github.com/JosefMamo12/Ex4/blob/master/resources/zoro.png) | 6 - 10 |
| ![Sunji](https://github.com/JosefMamo12/Ex4/blob/master/resources/sunji.png) | 5 |

![Marine Soldier](https://github.com/JosefMamo12/Ex4/blob/master/resources/agent.png)  Marine soldier who chase the pirates and gain points

![pirateboat](https://github.com/JosefMamo12/Ex4/blob/master/resources/pirateboat.png)  Vertex in the game where the marine soldier start the chase


## How to run
There is two ways

1. Clone this repository using this [url](https://github.com/JosefMamo12/Ex4.git) and download the Ex4_Server_v0.0.jar from [here](https://github.com/JosefMamo12/Ex4/releases/tag/Execute). put the jar file in same directory of the repository. run this code in the terminal
`java -jar Ex4_Server_v0.0.jar <Case Number [0 - 15]> ` then click run.
2. Download the two release from [here](https://github.com/JosefMamo12/Ex4/releases/tag/Execute)  put them in the same direcotry and from 
two different terminal. first run this at the first terminal `java -jar Ex4_Server_v0.0.jar <Case Number [0 - 15]>` at the other terminal 
run `java -jar Ex4.jar`.

* [How to run clip](https://www.youtube.com/watch?v=n8h_7lhBrug)

## Detailed Information
* You can see at the [WIKI Pages](https://github.com/JosefMamo12/Ex4/wiki)



# Classe

### Agent
 `public void setPos(GeoLocation pos)`- 
 
 `public int getId()`- 
 
 `public void setId(int id)` - 
 
 `public double getValue()` - 
 
 `public void setValue(double value)` - 
 
 `public int getSrc()` - 
 
 `public void setSrc(int src)`- 
 
 `public int getDest()`- 
 
 `public void setDest(int dest)`- 
 
 `public GeoLocation getPos()` - 
 
 `public String toString()` - 
 
 `public void setSpeed(double speed)` - 


### DirectedWeightedGraph

`public HashMap<Integer, NodeData> getNodes()` - 

`public HashMap<Integer, HashMap<Integer, EdgeData>> getGraph()` - 

`public static final String ERROR = "ConcurrentModificationException: Changed the structure of the data structure while iterating!";` - 

`public int hashCode()` - 

`public DirectedWeightedGraph()` - 

`public NodeData getNode(int key) ` - 
  
`public EdgeData getEdge(int src, int dest)` - 
  
`public void addNode(@NotNull NodeData n)` - 
  
`public void connect(int src, int dest, double w)` - 

`public boolean equals(Object o)` - 

`public Iterator<NodeData> nodeIter()` - 
 
`public boolean hasNext()` - 
 
`public NodeData next()` - 
 
`public Iterator<EdgeData> edgeIter()`-  
 
`public boolean hasNext()` - 
 
`public EdgeData next()` - 
 
`public Iterator<EdgeData> edgeIter(int node_id)` - 
 
`public boolean hasNext()` - 
 
`public EdgeData next()` - 

`public NodeData removeNode(int key)` - 
 
`public EdgeData removeEdge(int src, int dest)` - 
 
`public int nodeSize()` - 
 
`public int edgeSize()`- 
 
`public int getMC()` - 
 
 
### NodeData

`public void setWeight(double w)` - 

***

### EdgeData
