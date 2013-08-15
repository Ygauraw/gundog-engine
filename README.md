Gundog Engine/Gods and Towers 1
=============

A 3D game engine and online tower defense game for Android and the desktop.

## About

I wrote this code without access to the internet while I was deployed in Afghanistan.  Prior to this, I had no experience developing Android applications.  Nor had I written a single line of OpenGL (or any 3D graphics) code.  All I had to help was a MyTouch 3G, an old Eclipse install with the Android sdk, and two books I purchased from Amazon.  This project began as a series of hacks to learn both Android, OpenGL, and game development.  Most of the code is disgusting and makes me cringe when I look at it now.  That being said, there are still some interesting things that others may find useful in the code, which is why I took the time to open source it.

## Key features

* The game engine is modular, allowing it to run on both the desktop and Android, greatly speeding up testing/development.  
* There is a genetic algorithm to balance the creatures, towers, and race abilities by running simulated games
* The pathfinding algorithm runs based on the size of the game board, not the number of creatures.  This allows it to achieve 100's of creatures moving on screen even with an old MyTouch 3G phone that is unable to handle most 2D TD games.
* The NAT Puncher allows cell phones and desktops to connect even if they are behind firewalls which allows one person to host the game for online play
* The model loading framework was abstracted off the OpenGL thread so that the rendering thread does not experience any delays when a model has to load from a cell phones slow storage subsystem
* The timing of the game engine is decoupled from the rendering, allowing the performance to be adjusted based on the capabilities of the device
* The custom model format I developed converts MD2 into a compressed, tri-stripped, short based (not float), key frame animation.  This significantly reduced the memory requirement of each model, allowing for much faster load times, and the ability to easily run within the 64MB limitation of old phones.
* I am sure there is more, but I haven't worked with the code in over a year

## Notes

I highly recommend that anyone thinking about using this code in its entirety considers using [libGDX](http://libgdx.badlogicgames.com/) instead for the core system.  Most of the bug reports I received were due to Android fragmentation in the OpenGL ES subsystem, with vendors incorrectly implementing the spec.  The team over at libGDX put in work arounds for most of these issues.  

## Copyright

The code for the game is licensed under GPLv3.  The assets, which includes all models, textures, pixel art, images, and music, are still owned by Gundog Studios.  Contact me if you would like to purchase, license, or use them in your own project.
