# Ex4 Pokemon Game (One Piece Catch the Pirate Game)
## Game Process
1. Retrieving Pirates: The game starts by fetching the number of pirates from the server. This information is displayed on one of the edges of the game screen.
2. Placing Marine Soldier: A marine soldier character is positioned on one of the vertices of the game environment.
3. Finding the Closest Pirate: The game algorithm identifies the nearest pirate to the marine soldier's location. This calculation ensures that the marine soldier can start chasing after the selected pirate.
4. Chasing Pirates: The marine soldier begins pursuing the chosen pirate, moving through the game environment to catch them.
5.  Catching Pirates: When the marine soldier successfully catches a pirate, the player earns points based on the value of the pirate. The grade or score of the player increases with each successful capture.

![GifMaker_20220109105727517](https://user-images.githubusercontent.com/73185009/148676111-2dd2e154-938a-45b1-8513-19347c042417.gif)

## Game Components

| Pirate | Points | 
| --- | --- | 
| **![Luffy](https://github.com/JosefMamo12/Ex4/blob/master/resources/luffy.png)** | 11 - 15 |
| ![Zoro](https://github.com/JosefMamo12/Ex4/blob/master/resources/zoro.png) | 6 - 10 |
| ![Sunji](https://github.com/JosefMamo12/Ex4/blob/master/resources/sunji.png) | 5 |

![Marine Soldier](https://github.com/JosefMamo12/Ex4/blob/master/resources/agent.png)  Marine soldier who chase the pirates and gain points

![pirateboat](https://github.com/JosefMamo12/Ex4/blob/master/resources/pirateboat.png)  Vertex in the game where the marine soldier start the chase
To run the Ex4 Pokemon Game (One Piece Catch the Pirate Game), you have two options:

## How to run

### Option 1:

1. Clone the repository from this URL to your local machine.
2. Download the Ex4_Server_v0.0.jar file from here and place it in the same directory as the cloned repository.
3. Open a terminal or command prompt and navigate to the directory where the repository and the server JAR file are located.
4. Run the following command in the terminal:

`java -jar Ex4_Server_v0.0.jar <Case Number [0 - 15]>`

Replace <Case Number [0 - 15]> with the desired case number for the game.

5. Click the "Run" button to start the game.

### Option 2:

1. Download both releases from here to your local machine.
2. Place both the Ex4_Server_v0.0.jar and Ex4.jar files in the same directory.
3. Open two separate terminal or command prompt windows.
4. In the first terminal, run the following command:

`java -jar Ex4_Server_v0.0.jar <Case Number [0 - 15]>`

Replace <Case Number [0 - 15]> with the desired case number for the game.

5. In the second terminal, run the following command:

`java -jar Ex4.jar`
6. The game will start running in the second terminal window.

Choose either option based on your preference, and you'll be able to run and play the Ex4 Pokemon Game (One Piece Catch the Pirate Game).

* [How to run clip](https://www.youtube.com/watch?v=n8h_7lhBrug)

## Detailed Information
* You can see at the [WIKI Pages](https://github.com/JosefMamo12/Ex4/wiki)



