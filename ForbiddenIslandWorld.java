import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import javalib.colors.*;        // Predefined colors (Red, Green, Yellow, Blue, Black, White)

//represent a IListIterator<T>
class IListIterator<T> implements Iterator<T> {
    IList<T> items;
    IListIterator(IList<T> items) {
        this.items = items;
    }
    public boolean hasNext() {
        return !this.items.isEmpty();
    }
    // In IListIterator
    public T next() {
        ConsList<T> itemsAsCons = this.items.asCons();
        T answer = itemsAsCons.first;
        this.items = itemsAsCons.rest;
        return answer;
    }
    public void remove() {
        throw new UnsupportedOperationException("Don't do this!");
    }
}
/** A list */
interface IList<T> extends Iterable<T> {
    boolean isEmpty();
    int length();
    boolean contains(T t);
    int sharedItems(IList<T> that);
    int sharedItemsHelp(IList<T> that);
    ConsList<T> asCons();
    IList<Target> remove(Pilot p);

}
class Empty<T> implements IList<T> {
    public boolean isEmpty() {
        return true;
    }
    public int length() {
        return 0;
    }
    public boolean contains(T t) {
        return false;
    }
    public int sharedItems(IList<T> that) {
        return 0;
    }
    public int sharedItemsHelp(IList<T> that) {
        return 0;
    }
    public ConsList<T> asCons() {
        return null;
    }
    public Iterator<T> iterator() {
        return new IListIterator<T>(this);
    }

    public IList<Target> remove(Pilot p) {
        return new Empty<Target>();
    }

}
class ConsList<T> implements IList<T> {
    T first;
    IList<T> rest;
    ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }
    public boolean isEmpty() {
        return false;
    }
    public int length() {
        return 1 + this.rest.length();
    }
    public boolean contains(T t) {
        return this.first.equals(t) || this.rest.contains(t);
    }
    public int sharedItems(IList<T> that) {
        return that.sharedItemsHelp(this);
    }
    public int sharedItemsHelp(IList<T> that) {
        if (that.contains(this.first)) {
            return 1 + this.rest.sharedItems(that);
        }
        else {
            return this.rest.sharedItems(that);
        }
    }
    public ConsList<T> asCons() {
        return this;
    }
    public Iterator<T> iterator() {
        return new IListIterator<T>(this);
    }

    public IList<Target> remove(Pilot p) {
        if (((Target) this.first).sameLoc(p)) {
            return this.rest.remove(p);
        }
        else {
            return new ConsList<Target>((Target) this.first, this.rest.remove(p));
        }
    }

}


//represent the player
class Pilot {
    //location of the pilot
    Posn center;

    //constructor for Pilot
    Pilot(Posn center) {
        this.center = center;
    }


    // Returns world images of the "pilot-icon.png" file
    WorldImage pilotImage() {
        return new RectangleImage(new Posn(this.center.x * ForbiddenIslandWorld.CELL_SIZE,
                this.center.y * ForbiddenIslandWorld.CELL_SIZE),
                ForbiddenIslandWorld.CELL_SIZE,
                ForbiddenIslandWorld.CELL_SIZE,
                Color.CYAN);
    }

    // Moves pilot based on left, right, up, down keypress
    public Posn moveOnKey(String ke) {
        if (ke.equals("left") && this.center.x - 1 >= 0) {
            return new Posn(this.center.x - 1, this.center.y);
        }
        else if (ke.equals("right") &&
                this.center.x + 1 <= ForbiddenIslandWorld.ISLAND_SIZE
                        * ForbiddenIslandWorld.CELL_SIZE) {
            return new Posn(this.center.x + 1, this.center.y);
        }
        else if (ke.equals("up") && this.center.y - 1 >= 0) {
            return new Posn(this.center.x, this.center.y - 1);
        }
        else if (ke.equals("down") &&
                this.center.y + 1 <= ForbiddenIslandWorld.ISLAND_SIZE
                        * ForbiddenIslandWorld.CELL_SIZE) {
            return new Posn(this.center.x, this.center.y + 1);
        }

        return this.center;
    }
}

//represent Player2
class Player2 extends Pilot {

    //contructor for Player2
    Player2(Posn center) {
        super(center);
    }

    // Moves pilot based on left, right, up, down keypress
    public Posn moveOnKey(String ke) {
        if (ke.equals("a") && this.center.x - 1 >= 0) {
            return new Posn(this.center.x - 1, this.center.y);
        }
        else if (ke.equals("d") &&
                this.center.x + 1 <= ForbiddenIslandWorld.ISLAND_SIZE
                        * ForbiddenIslandWorld.CELL_SIZE) {
            return new Posn(this.center.x + 1, this.center.y);
        }
        else if (ke.equals("w") && this.center.y - 1 >= 0) {
            return new Posn(this.center.x, this.center.y - 1);
        }
        else if (ke.equals("s") &&
                this.center.y + 1 <= ForbiddenIslandWorld.ISLAND_SIZE
                        * ForbiddenIslandWorld.CELL_SIZE) {
            return new Posn(this.center.x, this.center.y + 1);
        }

        return this.center;
    }

    // Returns world images of the "pilot-icon.png" file
    WorldImage player2Image() {
        return new RectangleImage(new Posn(this.center.x * ForbiddenIslandWorld.CELL_SIZE,
                this.center.y * ForbiddenIslandWorld.CELL_SIZE),
                ForbiddenIslandWorld.CELL_SIZE,
                ForbiddenIslandWorld.CELL_SIZE,
                Color.CYAN);
    }

}

//represent all the things the player need to collect
class Target {
    Posn center;
    Color color;
    int rad;

    //constructor for target
    Target(Posn center) {
        this.center = center;
        this.color = new Color(0, 0, 0);
        this.rad = ForbiddenIslandWorld.CELL_SIZE;
    }

    //returns a image of target in world
    WorldImage targetImage() {
        return new CircleImage(new Posn(this.center.x * ForbiddenIslandWorld.CELL_SIZE,
                this.center.y * ForbiddenIslandWorld.CELL_SIZE), this.rad, this.color);
    }

    //is the pilot has the same location as target
    public boolean sameLoc(Pilot p) {
        return this.center.x == p.center.x && this.center.y == p.center.y;
    }
}

//represent a helicopter
class HelicopterTarget extends Target {

    //constructor for helicopter
    HelicopterTarget(Posn center) {
        super(center);
    }

    //returns a image of helicopter in world
    WorldImage helicopterImage() {
        return new RectangleImage(new Posn(this.center.x * ForbiddenIslandWorld.CELL_SIZE,
                this.center.y * ForbiddenIslandWorld.CELL_SIZE),
                ForbiddenIslandWorld.CELL_SIZE,
                ForbiddenIslandWorld.CELL_SIZE,
                Color.RED);
    }

}
// Represents a single square of the game area
class Cell {
    // represents absolute height of this cell, in feet
    double height;
    // In logical coordinates, with the origin at the top-left corner of the screen
    int x;
    int y;
    // the four adjacent cells to this one
    Cell left;
    Cell top;
    Cell right;
    Cell bottom;
    // reports whether this cell is flooded or not
    boolean isFlooded;

    //constructor for cell
    Cell(double height, int x, int y) {
        this.height = height;
        this.x = x;
        this.y = y;
        this.left = this;
        this.top = this;
        this.right = this;
        this.bottom = this;
        this.isFlooded = false;
    }

    //get the color for cell with the given waterHeight
    Color getColor(double waterHeight) {
        if (this.isFlooded) {
            return new Color(0, 0, 255 +
                    (int)((this.height - waterHeight)
                            * 510 / ForbiddenIslandWorld.ISLAND_SIZE));
        }
        if (this.height < waterHeight) {
            return new Color((int)(waterHeight - this.height)
                    * 510 / ForbiddenIslandWorld.ISLAND_SIZE,
                    255 + ((int)(this.height - waterHeight)
                            * 510 / ForbiddenIslandWorld.ISLAND_SIZE), 0);
        }
        if (this.height > ForbiddenIslandWorld.ISLAND_HEIGHT) {
            return new Color(255, 255, 255);
        }
        else {
            return new Color((int)this.height
                    * 510 / ForbiddenIslandWorld.ISLAND_SIZE, 255,
                    (int)this.height
                            * 510 / ForbiddenIslandWorld.ISLAND_SIZE);
        }
    }
}

//represent a ocean cell
class OceanCell extends Cell {

    OceanCell(double height, int x, int y) {
        super(height, x, y);
        this.isFlooded = true;
    }
    Color getColor(double waterHeight) {
        return new Color(0, 0, 255);
    }
}

//resprent a ForbiddenIslandWorld
class ForbiddenIslandWorld extends World {
    // Defines an int constant
    static final int ISLAND_SIZE = 64;
    static final int ISLAND_HEIGHT = ISLAND_SIZE / 2;
    static final int CELL_SIZE = 10;
    // the heights of all the cells in the board
    ArrayList<ArrayList<Integer>> heights;
    // all the cells, in the form of a two dimensional arraylist
    ArrayList<ArrayList<Cell>> cells;
    // All the cells of the game, including the ocean
    IList<Cell> board;
    // the current height of the ocean
    double waterHeight;
    //a list of targets
    IList<Target> items = new Empty<Target>();
    //a helicopter
    HelicopterTarget heli;
    //a Scuba
    IList<Target> scuba = new Empty<Target>();
    //number of steps taken
    int score = 0;
    //time before island is flooded
    int worldTimer = ForbiddenIslandWorld.ISLAND_SIZE;
    //Counter for scuba
    int scubaCounter = 0;
    //player 1
    Pilot p1;
    //player 2
    Player2 p2;

    //Constructor for ForbiddenIslandWorld with all the initialized fields
    ForbiddenIslandWorld() {
        p1 = new Pilot(new Posn(ForbiddenIslandWorld.ISLAND_SIZE / 2,
                ForbiddenIslandWorld.ISLAND_SIZE / 2));
        this.heights = new ArrayList<ArrayList<Integer>>();
        this.cells = new ArrayList<ArrayList<Cell>>();
        this.board = new Empty<Cell>();
        waterHeight = 0;
        this.initializeMountainHeights();
        this.initializeCells();
        this.initializeTargets();

    }

    //Constructor for new world based on key input
    ForbiddenIslandWorld(String key) {
        p1 = new Pilot(new Posn(ForbiddenIslandWorld.ISLAND_SIZE / 2,
                ForbiddenIslandWorld.ISLAND_SIZE / 2));
        this.heights = new ArrayList<ArrayList<Integer>>();
        this.cells = new ArrayList<ArrayList<Cell>>();
        this.board = new Empty<Cell>();
        waterHeight = 0;
        if (key.equals("m")) {
            this.initializeMountainHeights();
        }
        if (key.equals("t")) {
            this.initializeBumpyHeights();
        }
        if (key.equals("r")) {
            this.initializeRandomHeights();
        }
        this.initializeCells();
        this.initializeTargets();
    }

    //constructor to create a new world with player 2
    ForbiddenIslandWorld(ArrayList<ArrayList<Integer>> lastHeight) {
        p1 = new Pilot(new Posn(ForbiddenIslandWorld.ISLAND_SIZE / 2,
                ForbiddenIslandWorld.ISLAND_SIZE / 2));
        this.heights = lastHeight;
        this.cells = new ArrayList<ArrayList<Cell>>();
        this.board = new Empty<Cell>();
        waterHeight = 0;
        p2 = new Player2(new Posn(ForbiddenIslandWorld.ISLAND_SIZE / 2,
                ForbiddenIslandWorld.ISLAND_SIZE / 2));
        this.initializeCells();
        this.initializeTargets();
    }

    //initialize heights for mountain terrain
    public void initializeMountainHeights() {
        for (int i = 0; i <= ForbiddenIslandWorld.ISLAND_SIZE; i++) {
            heights.add(new ArrayList<Integer>());
            for (int j = 0; j <= ForbiddenIslandWorld.ISLAND_SIZE; j++) {
                heights.get(i).add((ForbiddenIslandWorld.ISLAND_HEIGHT)
                        - Math.abs((ForbiddenIslandWorld.ISLAND_HEIGHT) - i)
                        - Math.abs((ForbiddenIslandWorld.ISLAND_HEIGHT) - j));
            }
        }
    }

    //initialize heights for bumpy terrain
    public void initializeBumpyHeights() {
        for (int i = 0; i <= ForbiddenIslandWorld.ISLAND_SIZE; i++) {
            heights.add(new ArrayList<Integer>());
            for (int j = 0; j <= ForbiddenIslandWorld.ISLAND_SIZE; j++) {
                if ((ForbiddenIslandWorld.ISLAND_HEIGHT)
                        - Math.abs((ForbiddenIslandWorld.ISLAND_HEIGHT) - i)
                        - Math.abs((ForbiddenIslandWorld.ISLAND_HEIGHT) - j) <= 0) {
                    heights.get(i).add((ForbiddenIslandWorld.ISLAND_HEIGHT)
                            - Math.abs((ForbiddenIslandWorld.ISLAND_HEIGHT) - i)
                            - Math.abs((ForbiddenIslandWorld.ISLAND_HEIGHT) - j));
                }
                else {
                    heights.get(i).add(1 + (int)(Math.random() * 32));
                }
            }
        }
    }

    //initialize heights for a random terrain
    public void initializeRandomHeights() {
        for (int i = 0; i <= ForbiddenIslandWorld.ISLAND_SIZE; i++) {
            heights.add(new ArrayList<Integer>());
            for (int j = 0; j <= ForbiddenIslandWorld.ISLAND_SIZE; j++) {
                heights.get(i).add(0);
            }
        }
        Posn origin = new Posn(0, 0);
        Posn corner = new Posn(ForbiddenIslandWorld.ISLAND_SIZE, ForbiddenIslandWorld.ISLAND_SIZE);
        randomBoxes(origin, new Posn(average(origin.x, corner.x),
                        average(origin.y, corner.y)), 0, 1, 1,
                ForbiddenIslandWorld.ISLAND_HEIGHT); //topLeft
        randomBoxes(new Posn(average(origin.x, corner.x), origin.y),
                new Posn(corner.y, average(origin.y, corner.y)),
                1, 0, ForbiddenIslandWorld.ISLAND_HEIGHT, 1); //topRight
        randomBoxes(new Posn(origin.x, average(origin.x, corner.x)),
                new Posn(average(origin.x, corner.x), corner.y),
                1, ForbiddenIslandWorld.ISLAND_HEIGHT, 0, 1); //bottomLeft
        randomBoxes(new Posn(average(origin.x, corner.x),
                        average(origin.y, corner.y)), corner,
                ForbiddenIslandWorld.ISLAND_HEIGHT, 1, 1, 0); //bottomRight
        for (int i = 0; i <= ForbiddenIslandWorld.ISLAND_SIZE; i++) {
            //      heights.add(new ArrayList<Integer>());
            for (int j = 0; j <= ForbiddenIslandWorld.ISLAND_SIZE; j++) {
                heights.get(i).set(j, heights.get(i).get(j) - ForbiddenIslandWorld.ISLAND_HEIGHT);
            }
        }
        if (heights.get(ForbiddenIslandWorld.ISLAND_SIZE / 2).
                get(ForbiddenIslandWorld.ISLAND_SIZE / 2) <= 0) {
            this.initializeRandomHeights();
        }
    }

    //return the average between two numbers
    public int average(int low, int high) {
        return (int)((low + high) / 2);
    }

    //create random boxes
    public void randomBoxes(Posn origin, Posn corner,
                            int topLeft, int topRight, int bottomLeft, int bottomRight) {
        int size = corner.x - origin.x; //32
        int top = (int)(size * Math.random()) + (topLeft + topRight) / 2; //rando(32)
        int left = (int)(size * Math.random()) + (topLeft + bottomLeft) / 2; //rando(32)
        int right = (int)(size * Math.random()) + (topRight + bottomRight) / 2; //32 + rando(32)
        int bottom = (int)(size * Math.random()) + (bottomLeft + bottomRight) / 2; //32 + rando(32)
        int middle = (int)(size * Math.random()) +
                (topLeft + topRight + bottomLeft + bottomRight) / 4; //8

        heights.get(origin.y).set(average(origin.x, corner.x), top);
        heights.get(average(origin.y, corner.y)).set(origin.x, left);
        heights.get(average(origin.y, corner.y)).set(corner.x, right);
        heights.get(corner.y).set(average(origin.x, corner.x), bottom);
        heights.get(average(origin.y, corner.y)).set(average(origin.x, corner.x), middle);

        if (size > 1) {
            randomBoxes(origin, new Posn(average(origin.x, corner.x),
                            average(origin.y, corner.y)),
                    topLeft, top, left, middle); //topLeft
            randomBoxes(new Posn(average(origin.x, corner.x), origin.y),
                    new Posn(corner.x, average(origin.y, corner.y)),
                    top, topRight, middle, right); //topRight
            randomBoxes(new Posn(origin.x, average(origin.y, corner.y)),
                    new Posn(average(origin.x, corner.x), corner.y),
                    left, middle, bottomLeft, bottom); //bottomLeft
            randomBoxes(new Posn(average(origin.x, corner.x),
                            average(origin.y, corner.y)), corner, middle,
                    right, bottom, bottomRight); //bottomRight
        }
    }

    //initialize all the cells
    public void initializeCells() {
        for (int i = 0; i <= ForbiddenIslandWorld.ISLAND_SIZE; i++) {
            cells.add(new ArrayList<Cell>());
            for (int j = 0; j <= ForbiddenIslandWorld.ISLAND_SIZE; j++) {
                Cell newCell = new Cell(heights.get(i).get(j), j, i);
                if (heights.get(i).get(j) <= waterHeight) {
                    newCell = new OceanCell(heights.get(i).get(j), j, i);
                }
                cells.get(i).add(newCell);
                board = new ConsList<Cell>(newCell, board);
            }
        }
        this.setLefts();
        this.setTops();
        this.setRights();
        this.setBottoms();
    }
    public void setLefts() {
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 1; j < cells.size(); j++) {
                cells.get(i).get(j).left = cells.get(i).get(j - 1);
            }
        }
    }
    public void setTops() {
        for (int i = 1; i < cells.size(); i++) {
            for (int j = 0; j < cells.size(); j++) {
                cells.get(i).get(j).top = cells.get(i - 1).get(j);
            }
        }
    }
    public void setRights() {
        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < cells.size() - 1; j++) {
                cells.get(i).get(j).right = cells.get(i).get(j + 1);
            }
        }
    }
    public void setBottoms() {
        for (int i = 0; i < cells.size() - 1; i++) {
            for (int j = 0; j < cells.size(); j++) {
                cells.get(i).get(j).bottom = cells.get(i + 1).get(j);
            }
        }
    }

    //flood the cell if cell height is less than or equal to water height
    public void flood() {
        waterHeight += 0.5;
        for (Cell c : board) {
            if ((c.isFlooded)) {
                //DO NOTHING
            }
            if ((c.left.isFlooded ||
                    c.top.isFlooded ||
                    c.right.isFlooded ||
                    c.bottom.isFlooded) &&
                    (c.height <= this.waterHeight)) {
                c.isFlooded = true;
                c = new OceanCell(c.height, c.x, c.y);

            }
        }
    }

    //initialize all the location for targets and helicopter
    public void initializeTargets() {
        //set location for items that need to be picked up
        for (int i = 0; i <= 5; i++) {
            int x = (1 + (int)(Math.random() * 64));
            int y = (1 + (int)(Math.random() * 64));
            while (cells.get(y).get(x).isFlooded) {
                x = (1 + (int)(Math.random() * 64));
                y = (1 + (int)(Math.random() * 64));
            }
            Posn center = new Posn(x, y);
            Target t = new Target(center);
            items = new ConsList<Target>(t, items);
        }

        //set helicopter position
        Cell highest = new Cell(0, 0, 0);
        for (Cell c: board) {
            if (c.height >= highest.height) {
                highest = c;
                heli = new HelicopterTarget(new Posn(highest.x, highest.y));
            }
        }

        //set scuba position
        int c = (1 + (int)(Math.random() * 64));
        int d = (1 + (int)(Math.random() * 64));
        while (cells.get(d).get(c).isFlooded) {
            c = (1 + (int)(Math.random() * 64));
            d = (1 + (int)(Math.random() * 64));
        }
        Posn center = new Posn(c, d);
        Target s = new Target(center);
        scuba = new ConsList<Target>(s, scuba);


    }


    //create image for ForbiddenIslandWorld
    public WorldImage makeImage() {
        //canvas
        WorldImage island = new RectangleImage(new Posn(0, 0), ForbiddenIslandWorld.CELL_SIZE
                * (1 + ForbiddenIslandWorld.ISLAND_SIZE),
                ForbiddenIslandWorld.CELL_SIZE
                        * (1 + ForbiddenIslandWorld.ISLAND_SIZE), new White());
        //Step Taken image
        WorldImage st = new TextImage(new Posn(50, 30), "Step Taken",
                new Color(0, 0 , 0));
        //Image of score
        WorldImage sc = new TextImage(new Posn(50, 50), Integer.toString(score),
                new Color(0, 0 , 0));
        //Time Left Image
        WorldImage tf = new TextImage(new Posn(50, 70), "Time Left:",
                new Color(0, 0 , 0));
        //Timer Image
        WorldImage tim = new TextImage(new Posn(50, 90), Integer.toString(worldTimer),
                new Color(0, 0 , 0));
        //ScubaTime Image
        WorldImage sct = new TextImage(new Posn(50, 110), "Time Left:",
                new Color(0, 0 , 0));
        //Timer Image
        WorldImage sf = new TextImage(new Posn(50, 130), Integer.toString(scubaCounter),
                new Color(0, 0 , 0));
        //draw board
        for (Cell c : board) {
            island = island.overlayImages(new RectangleImage(
                    new Posn(c.x * ForbiddenIslandWorld.CELL_SIZE,
                            c.y * ForbiddenIslandWorld.CELL_SIZE),
                    ForbiddenIslandWorld.CELL_SIZE,
                    ForbiddenIslandWorld.CELL_SIZE,
                    c.getColor(waterHeight)));
        }
        //draw targets
        for (Target t : items) {
            island = island.overlayImages(t.targetImage());
        }
        //draw scuba
        for (Target t : scuba) {
            island = island.overlayImages(new RectangleImage(
                    new Posn(t.center.x * ForbiddenIslandWorld.CELL_SIZE,
                            t.center.y * ForbiddenIslandWorld.CELL_SIZE),
                    ForbiddenIslandWorld.CELL_SIZE,
                    ForbiddenIslandWorld.CELL_SIZE, new Color(0, 0, 0)));
        }
        //draw everything else
        island = island.overlayImages(st, sc, tf, tim, sct, sf,
                p1.pilotImage(),
                heli.helicopterImage());
        //if player2 is not null draw player2
        if (p2 != null) {
            island = island.overlayImages(p2.player2Image());
        }

        return island;
    }


    //Do something when a key is pressed
    public ForbiddenIslandWorld onKeyEvent(String ke) {
        if (ke.equals("m") || ke.equals("t") || ke.equals("r")) {
            return new ForbiddenIslandWorld(ke);
        }
        if (ke.equals("left") || ke.equals("up") || ke.equals("down") || ke.equals("right")) {
            p1.center = p1.moveOnKey(ke);
            items = items.remove(p1);
            scuba = scuba.remove(p1);
            score = score + 1;
        }

        if (ke.equals("e")) {
            return new ForbiddenIslandWorld(heights);
        }

        if ((ke.equals("a") || ke.equals("s") || ke.equals("d") || ke.equals("w"))
                && (p2 != null)) {
            p2.center = p2.moveOnKey(ke);
            items = items.remove(p2);
            scuba = scuba.remove(p2);
            score = score + 1;
        }
        if (ke.equals("f") && scuba.isEmpty()) {
            scubaCounter = 5;
        }
        return this;

    }

    //Change the world per tick
    public ForbiddenIslandWorld onTick() {
        if (scubaCounter > 0) {
            scubaCounter = scubaCounter - 1;
        }
        this.flood();
        worldTimer = worldTimer - 1;
        return this;
    }

    //end the game when conditions are met
    public WorldEnd worldEnds() {
        if (p2 == null) {
            if (items.isEmpty() && heli.sameLoc(p1)) {
                return new WorldEnd(true, new OverlayImages(this.makeImage(),
                        new TextImage(new Posn(320, 320),
                                "Congratulations, you fixed the helicopter!",
                                new Red())));
            }
            if ((waterHeight >= ForbiddenIslandWorld.ISLAND_HEIGHT ||
                    cells.get(p1.center.y).get(p1.center.x).isFlooded) && scubaCounter <= 0) {
                return new WorldEnd(true, new OverlayImages(this.makeImage(),
                        new TextImage(new Posn(320, 320),
                                "Game Over",
                                new Red())));
            }
        }
        else {
            if (items.isEmpty() && heli.sameLoc(p1) && heli.sameLoc(p2)) {
                return new WorldEnd(true, new OverlayImages(this.makeImage(),
                        new TextImage(new Posn(320, 320),
                                "Congratulations, you fixed the helicopter!",
                                new Red())));
            }
            if ((waterHeight >= ForbiddenIslandWorld.ISLAND_HEIGHT ||
                    cells.get(p1.center.y).get(p1.center.x).isFlooded ||
                    cells.get(p2.center.y).get(p2.center.x).isFlooded) &&
                    scubaCounter <= 0) {
                return new WorldEnd(true, new OverlayImages(this.makeImage(),
                        new TextImage(new Posn(320, 320),
                                "Game Over",
                                new Red())));
            }
        }
        return new WorldEnd(false, this.makeImage());
    }
}
//represent worlds and tests
class ExamplesIslandWorld {
    Pilot player1 = new Pilot(new Posn(10, 10));
    Player2 player2 = new Player2(new Posn(20, 20));
    Target t1 = new Target(new Posn(10, 10));
    HelicopterTarget h1 = new HelicopterTarget(new Posn(21, 23));
    IList<Target> mt = new Empty<Target>();
    IList<Target> target1 = new ConsList<Target>(t1, mt);
    IList<Target> target2 = new ConsList<Target>(h1, target1);
    Cell c1 = new Cell(10.0, 1, 3);
    OceanCell o1 = new OceanCell(0, 10, 10);

    ForbiddenIslandWorld world;
    ForbiddenIslandWorld mountain;
    ForbiddenIslandWorld bumpy;
    ForbiddenIslandWorld multiplayer;

    void initializeWorld() {
        this.world = new ForbiddenIslandWorld();
    }

    boolean testisEmpty(Tester t) {
        return t.checkExpect(target1.isEmpty(), false) &&
                t.checkExpect(mt.isEmpty(), true);
    }


    boolean testMoveOnkey(Tester t) {
        return t.checkExpect(player1.moveOnKey("up"), new Posn(10, 9)) &&
                t.checkExpect(player1.moveOnKey("down"), new Posn(10, 11)) &&
                t.checkExpect(player1.moveOnKey("left"), new Posn(9, 10)) &&
                t.checkExpect(player1.moveOnKey("right"), new Posn(11, 10)) &&
                t.checkExpect(player1.moveOnKey("c"), player1.center) &&
                t.checkExpect(player2.moveOnKey("w"), new Posn(20, 19)) &&
                t.checkExpect(player2.moveOnKey("s"), new Posn(20, 21)) &&
                t.checkExpect(player2.moveOnKey("a"), new Posn(19, 20)) &&
                t.checkExpect(player2.moveOnKey("d"), new Posn(21, 20)) &&
                t.checkExpect(player2.moveOnKey("up"), player2.center);
    }

    boolean testsameLoc(Tester t) {
        return t.checkExpect(t1.sameLoc(player1), true) &&
                t.checkExpect(h1.sameLoc(player1), false);
    }

    boolean testgetColor(Tester t) {
        return t.checkExpect(c1.getColor(0), new Color(79, 255, 79)) &&
                t.checkExpect(o1.getColor(0), new Color(0, 0, 255)) &&
                t.checkExpect(c1.getColor(20), new Color(79, 176, 0)) &&
                t.checkExpect(c1.getColor(10), new Color(79, 255, 79));
    }

    boolean testaverage(Tester t) {
        this.initializeWorld();
        return t.checkExpect(world.average(10, 20), 15);
    }

    void testsonKeyEvent(Tester t) {
        this.initializeWorld();
        world.p1 = new Pilot(new Posn(20, 20));
        world.onKeyEvent("up");
        t.checkExpect(world.p1.center, new Posn(20, 19));
        world.onKeyEvent("down");
        t.checkExpect(world.p1.center, new Posn(20, 20));
        world.onKeyEvent("left");
        t.checkExpect(world.p1.center, new Posn(19, 20));
        world.onKeyEvent("right");
        t.checkExpect(world.p1.center, new Posn(20, 20));
        world.p2 = new Player2(new Posn(20, 20));
        world.onKeyEvent("w");
        t.checkExpect(world.p2.center, new Posn(20, 19));
        world.onKeyEvent("s");
        t.checkExpect(world.p2.center, new Posn(20, 20));
        world.onKeyEvent("a");
        t.checkExpect(world.p2.center, new Posn(19, 20));
        world.onKeyEvent("d");
        t.checkExpect(world.p2.center, new Posn(20, 20));
    }

    void testscuba(Tester t) {
        this.initializeWorld();
        world.scuba = mt;
        t.checkExpect(world.scubaCounter, 0);
        world.onKeyEvent("f");
        t.checkExpect(world.scubaCounter, 5);
    }
    void testMountain(Tester t) {
        ForbiddenIslandWorld mountain = new ForbiddenIslandWorld();
        mountain.bigBang(ForbiddenIslandWorld.ISLAND_SIZE * ForbiddenIslandWorld.CELL_SIZE,
                ForbiddenIslandWorld.ISLAND_SIZE * ForbiddenIslandWorld.CELL_SIZE, 1);
    }

}
