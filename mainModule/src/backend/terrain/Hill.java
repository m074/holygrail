package backend.terrain;

import java.io.Serializable;

public class Hill extends Terrain implements Serializable {
    public Hill(){
        super(TerrainType.HILL,1.2,1.4,1.2,4,3);
    }
}
