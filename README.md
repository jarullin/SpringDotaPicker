Re-implementation of https://github.com/jarullin/OpenDotaPicker 
Helper tool for picking a right hero in DotA 2 Features:

* calculates the best pick based enemy heroes
* automatically determines which role a hero belongs to based on professional players' games over the last 7 days
* uses OpenDota API and a handwritten parser for Dota2ProTracker to refresh a data once a week
* Frontend: React + TS, Backend: Java + Spring (REST API)
Controls:

* Right-click to add hero to enemy pick
* Left-click to add hero to ally pick
* Middle-click (scroll wheel) to ban hero
* Press the according button on hero portrait on pick grid to undo (i.e. press right click on hero in enemy pick to remove it from enemy picks)
