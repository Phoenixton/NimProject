# Nim & Hackembush Games

# What's implemented

## Generic Editing
- Possibility of playing against a **computer** as well as a human.
- The nodes can **move freely and be settled on the ground** (the thick black line).
- The edges can be curved thanks to their **control point**, which is displayed by clicking on the edge in question. Clicking again on the edge will make the control point disappear.
- When you click on "**play**", the graph will be checked. If it's incorrect, the problematic nodes will be displayed in **red**.
- You can **change the color** of an edge by scrolling on it. The selected colors can be **changed on the left**.

## Nim Editing
- **Possibility of playing against a computer, a human, or having two computers play each other.**
- Two strategies implemented  : **Winning** and **Random**.
- **Quick setup implemented** : Just fill the seven (or less, as you wish) boxes on the bottom of the screen with the number of edges you want in each column. If you fill the boxes, the changes to the graph on the pane will be disregarded.
- The neutral color can be selected as well, if you don't like the green.

## Generic playing

- You play your moves, it's rather easy. You right-click to remove an edge or an Ellipse.
- An Alert will be displayed if you try to get one that's not yours.
- You play until the game ends. Then an alert is displayed to take you back to the menu or the editing scene.

## Nim Playing

- If you play against a human, it's basically the same as Generic Editing.
- If you play against a computer, it's the same.
- The slider appears in Computer vs Computer games. It allows you to set up the speed you want during the game. 3 is extremely slow and the closer it gets to 0, the faster it is. A tooltip indicates that if you hover over it.
------

### Notes

 - The jar can be used to launch the project directly. You can also find it in out/artifacts/NymProject_jar

 - The github link is : https://github.com/Phoenixton/NimProject
