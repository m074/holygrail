package frontend.units;

import backend.worldBuilding.Location;
import javafx.scene.image.Image;

public class LancerUI extends UnitUI {
    public LancerUI(Location drawLocation, Integer cellHeight, Integer cellWidth, Integer ownerID) {
        super(drawLocation,new Image("file:mainModule/resources/lancer.png", cellWidth, cellHeight, false, false),
                ownerID, cellHeight,cellWidth);
    }
}