# Blox - A 3D Adventure Puzzle Platformer

Blox is a 3D Platformer game made using [SilenceEngine](https://github.com/sriharshachilakapati/SilenceEngine/), a 2D/3D game engine written on top of LWJGL 3. It is a 3D Adventure Puzzle Platformer type of game. I plan to make it a massive game with a story of 12 chapters (want to know the story? suspense...!) and with a ton of levels in each chapter with a lot of different entities.

The end goal of the game is to find the stolen (suspense object here) by searching all over the space, i.e., in the solar system. This game is actually started to showcase what SilenceEngine can do, and will be completely opensource using the MIT licence.

## In Game elements, with screenshots

__Tutorial screen:__

![Tutorial](http://i.imgur.com/lW5CtVR.png)

The tutorial screen is the first thing that opens when you launch Blox. It will give you training about using the controls, and controlling the player cube. Of course, at this point, there are only 5 tutorial levels.

__Direction changing:__

![Cone-Direction](http://i.imgur.com/7uNIb7m.png)

What you see in this screenshot is the middle of the rotation. In Blox, you can't change the direction on your own, you must be hitting the cones there to change the direction. The camera will then rotate towards the direction pointed by the cone. Of course, this is not the case in free mode..

__The Collectibles__

![Collectibles](http://i.imgur.com/ClV6miO.png)

I'll now partially expose the suspense element, these collectibles are actually the broken parts of the object you are searching for in the deep space. That's it for now, you will learn more when the game is completed. Just collect them for now, and increase your score. And by the way, you see PTAs under the bottom-right corner? That's the in game virtual currency.

__The ThunderBalls__

![Thunder-balls](http://i.imgur.com/LsWbrb8.png)

The Thunder Balls are the first type of enemies you face in the game. These are usually moving in a to and fro motion, like a pendulum, and if you hit them, they'll swallow you and the level restarts. The only way to survive is to escape them, you can go when they are far, or jump over them.

__The FREE MODE__

![Free-Mode](http://i.imgur.com/x6OmQWL.png)

The FREE MODE is a way that allows you to take a look at the level. You switch to the free mode using the TAB key on keyboard, or the BUTTON 1 or **△** button or the **Y** button depending on the controller. Here the game stops to update, I mean, you'll make the world freeze and move with freedom in the world. You can return to the game from the same point by hitting the TAB key again.

__The Spikes__

![Spikes](http://i.imgur.com/qCD3Bvc.png)

The Spikes are the second type of the enemies you will be facing. These go inside the floor, and come up every time, that is, every three seconds. Also as usual, your only way to survive them is to jump over them. Otherwise, pay them a life.

__Youtube Video:__

[![Youtube Intro dev log](http://img.youtube.com/vi/aKrhGGMYTxw/0.jpg)](http://www.youtube.com/watch?v=aKrhGGMYTxw)

This video is the dev log which shows the new intro screen in action. The intro screen shows a demo level with the camera moving between different pre configured points.

[![Youtube game play video](http://img.youtube.com/vi/XfOYCTV72wk/0.jpg)](http://www.youtube.com/watch?v=XfOYCTV72wk)

This video is an early dev log of the game which happens to just have the core mechanics, the platforms, and the player controls.

## Controls of the game

The game can be controlled using the keyboard, or a GENERIC or XBOX controller. The PS3/4 controllers sort-of works, but they have incorrect mappings since I have not tested them. If anybody has a working PS3/4 controller, I'd be glad to receive the key mappings as the contribution.

Game Mode  | Action         | Keyboard | GENERIC Controller  | XBOX for Windows    |
-----------|----------------|----------|---------------------|---------------------|
ANY        | Move forward   | W        | DPAD UP/LS UP       | DPAD UP/LS UP       |
ANY        | Move backward  | S        | DPAD DOWN/LS DOWN   | DPAD DOWN/LS DOWN   |
ANY        | Move left      | A        | DPAD LEFT/LS LEFT   | DPAD LEFT/LS LEFT   |
ANY        | Move right     | D        | DPAD RIGHT/LS RIGHT | DPAD RIGHT/LS RIGHT |
Normal     | Jump           | SPACE    | BUTTON 3            | BUTTON B            |
ANY        | Switch Mode    | TAB      | BUTTON 1            | BUTTON Y            |
ANY        | Restart Level  | R        | BUTTON 10/START     | START BUTTON        |
FREE       | Move upwards   | Q        | BUTTON 5            | LEFT BUMPER         |
FREE       | Move downwards | E        | BUTTON 6            | RIGHT BUMPER        |

These are all the controls that you can use to give the game. Additionally, the **F1** key on the keyboard is used to switch between the windowed and fullscreen game modes.

## Licence

This game is licenced with MIT licence, the same licence that covers SilenceEngine.