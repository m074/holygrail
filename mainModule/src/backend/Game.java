package backend;

import backend.building.Building;
import backend.building.Castle;
import backend.building.ProductionBuilding;
import backend.exceptions.NullArgumentException;
import backend.units.Unit;
import backend.worldBuilding.Cell;
import backend.worldBuilding.Location;
import backend.worldBuilding.Player;
import backend.worldBuilding.World;
import javafx.scene.control.Alert;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

/**
 * Represents a Game session, it has a World and Players.
 */
public class Game implements Serializable {
    private World world;
    private Location selectedLocation;
    private Player player1, player2;
    private Player activePlayer;
    private Queue<String> logQueue;
    private boolean hasGameEnded=false;

    /**
     * Constructs a Game with a World of a specific Width and Height, and 2 players.
     *
     * @param worldWidth  Width of the World.
     * @param worldHeight Height of the World.
     * @param player1     Player 1.
     * @param player2     Player 2.
     */
    public Game(Integer worldWidth, Integer worldHeight, String player1, String player2) {
        loadPlayers(player1, player2);
        world = new World(worldWidth, worldHeight, this.player1, this.player2);
        startNewGame();
    }

    /**
     * Creates and loads the two players.
     * @param player1 Player 1.
     * @param player2 Player 2.
     */
    public void loadPlayers(String player1, String player2) {
        this.player1 = new Player(player1);
        this.player2 = new Player(player2);
    }

    /**
     * Returns the World Height.
     * @return Integer value of world Height.
     */
    public Integer getWorldHeight() {
        return world.getWorldHeight();
    }

    /**
     * Return the World Width.
     * @return Integer value of the
     */
    public Integer getWorldWidth() {
        return world.getWorldWidth();
    }

    /**
     * Starts a New Game, setting the active player to Player 1, and selecting his Castle.
     */
    public void startNewGame() {
        logQueue = new ArrayDeque<>();
        activePlayer = this.player1;
        selectPlayerCastle(activePlayer);
    }

    //TODO: Hacer un metodo para revisar si el player sigue teniendo Castle, para separarlo de este.
    /**
     * Selects the specified Player's castle.
     * @param player owner of the Castle.
     * @return
     */
    private boolean selectPlayerCastle(Player player) {
        //Searches the castle from the first player and selects the cell where it is located
        //#Building needs location
        if (!hasCastle(player)){
            addLog(player + "lost, he has no more buildings");
            return false;
        } else selectedLocation = world.getPlayerProductionBuilding(player).getLocation();
        return true;
    }

    public boolean hasCastle(Player player){
        return player.getProductionBuilding().getOwner() == player;
    }

    /**
     * //TODO: Esto deber�a estar en el controller.
     * @param clickedLocation
     */
    public void actionAttempt(Location clickedLocation) {
        if(hasGameEnded)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Message");
            alert.setHeaderText("Warning");
            alert.setContentText("A unit has been moved");
            alert.showAndWait();
            System.exit(0);
        }
        if (selectedLocation == null) {
            selectedLocation = clickedLocation;
            return;
        }

        if (world.isUnitOnLocation(selectedLocation)) {
            Unit selectedUnit = world.getUnitAt(selectedLocation);
            if (world.isUnitOnLocation(clickedLocation)) {
                if (world.getUnitAt(clickedLocation).getOwner().equals(activePlayer)) {
                    setSelectedLocation(clickedLocation);
                    System.out.println("location: " + selectedLocation);
                } else if (!selectedLocation.equals(clickedLocation)) {
                    attackAttempt(selectedUnit, world.getUnitAt(clickedLocation));
                }
            } else {
                if (selectedUnit.getOwner().equals(activePlayer)) { //Fixes bug that can move opponents units
                    moveAttempt(selectedUnit, clickedLocation);
                }
                selectedLocation = clickedLocation;
            }
        } else {
            //building is selected
            selectedLocation = clickedLocation;
        }
    }

    /**
     * Attempts to build an Archer. If achieved, returns true.
     * @return True if the Archer is created, false if not.
     */
    public void attemptBuildArcher() {
        ProductionBuilding productionBuilding = world.getPlayerProductionBuilding(activePlayer);
            addLog(productionBuilding.buildArcher(world));
    }

    /**
     * Attempts to build a Rider. If achieved, returns true.
     * @return True if the Rider is created, false if not.
     */
    public void attemptBuildRider() {
        ProductionBuilding productionBuilding = world.getPlayerProductionBuilding(activePlayer);
        addLog(productionBuilding.buildRider(world));
    }

    /**
     * Attempts to build a Lancer. If achieved, returns true.
     * @return True if the Lancer is created, false if not.
     */
    public void attemptBuildLancer() {
        ProductionBuilding productionBuilding = world.getPlayerProductionBuilding(activePlayer);
        addLog(productionBuilding.buildLancer(world));
    }

    /**
     * Adds a message to the log.
     * @param msg message to add.
     */
    private void addLog(String msg) {
        logQueue.add(msg);
    }

    /**
     * Attempts to move a unit to a Location.
     * @param unit unit to move.
     * @param clickedLocation destination Location.
     * @return True if the unit has moved, false if not.
     */
    private void moveAttempt(Unit unit, Location clickedLocation) {
        //TODO ask how we can display a log

        if (unit == null) {
            throw new NullArgumentException("null unit movement attempt");
        }
        if (clickedLocation == null) {
            throw new NullArgumentException("null to cell movement attempt");
        }

        addLog( unit.move(clickedLocation));

    }

    /**
     * Attempts to perform an attack from an attacker unit to a defender.
     * @param attacker attacking unit.
     * @param defender defending unit.
     * @return True if the unit has attacked.
     */
    public boolean attackAttempt(Unit attacker, Unit defender) {
        boolean hasAttacked = false;

        if (attacker == null) {
            throw new NullArgumentException("null attacker");
        }
        if (defender == null) {
            throw new NullArgumentException("null defender");
        }

        if (attacker.getOwner().equals(defender.getOwner())) {
            throw new IllegalStateException("tries to attack own unit");
        }
        if (attacker.attack(defender)) {
            addLog(attacker + " attacked " + defender);
            hasAttacked = true;
        } else {
            addLog(attacker + " Cant Attack ");
        }
        return hasAttacked;
    }

    /**
     * //TODO: CONTROLLER
     * @param location
     */
    private void setSelectedLocation(Location location) {
        selectedLocation = location;
    }

    /**
     * //TODO : CONTROLLER
     * @return
     */
    public Location getSelectedLocation() {
        return selectedLocation;
    }

    /**
     * Prints the queued Log.
     */
    public void printLog() {
        while (!logQueue.isEmpty()) {
            System.out.println(logQueue.poll());
        }
    }

    /**
     * Returns all the Units in the World.
     * @return Collection of all the Units.
     */
    public Collection<Unit> getUnits() {
        return world.getUnits();
    }

    /**
     * Returns all the Units in the World.
     * @return Collection of all the Buildings.
     */
    public Collection<Building> getBuildings() {
        return world.getBuildings();
    }

    /**
     * Returns all the Cells in the World.
     * @return Collection of all the Cells.
     */
    public Collection<Cell> getCells() {
        return world.getCells();
    }

    /**
     * Returns the active Player.
     * @return current active Player.
     */
    public Player getActivePlayer() {
        return activePlayer;
    }

    /**
     * Sets the next player to be the active player.
     */
    private void activateNextPlayer() {
        activePlayer = activePlayer.equals(player1) ? player2 : player1;
    }

    /** //TODO: El alert deber�a estar en el FrontEnd, el controller deber�a fijarse si algun jugador gan� y emitir el msg.
     * Ends the current Player turn.
     * Refills the player units action points, adds corresponding the gold per turn to the player.
     * If a unit is standing at his owner Castle with the Holy Grail, the player wins the game.
     * If the player does not own his Castle anymore, he loses the game.
     */
    public void endTurn() {
        world.refillUnitsAP(getActivePlayer());

        getActivePlayer().addGold(world.getPlayerIncome(getActivePlayer()));

        if (isHolyGrailSecure() || hasEverybodyElseLost(getActivePlayer())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Message");
            alert.setHeaderText("Congratulations");
            alert.setContentText("You have won!, do not try to move any more units please, start new game");

            alert.showAndWait();
            hasGameEnded = true;
        }
        activateNextPlayer();
        selectPlayerCastle(getActivePlayer());
        addLog(getActivePlayer() + " has " + getActivePlayer().getGold() + " gold and received " +
                world.getPlayerIncome(getActivePlayer()));
    }
    private boolean hasEverybodyElseLost(Player currentPlayer){
        if(player1.equals(currentPlayer))return hasPlayerLost(player2);
        if(player2.equals(currentPlayer))return hasPlayerLost(player1);
        return false;
    }

    private boolean hasPlayerLost(Player player){
        return  (!player.getProductionBuilding().getOwner().equals(player));
    }

    private boolean isHolyGrailSecure() {
        if (world.isUnitOnLocation(world.getPlayerProductionBuilding(activePlayer).getLocation())) {
            Unit selectedUnit = world.getUnitAt(world.getPlayerProductionBuilding(activePlayer).getLocation());
            if (selectedUnit.hasHolyGrail()) {
                return true;
            }
        }
        return false;
    }

    /**
     * If there is a unit there, pick an item in the current Cell.
     */
    public void pickItemAttempt() {

        if (selectedLocation == null) {
            return;
        }
        if (world.isUnitOnLocation(selectedLocation)) {
            Unit currentUnit = world.getUnitAt(selectedLocation);
            if (currentUnit.getOwner().equals(activePlayer)) {
                addLog(currentUnit.pickItem());
            }
        }
    }
}
