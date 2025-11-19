package main;

import object.CheatSheet;
import object.Coffee;
import object.Pencil;

public class AssetSetter {

    Panel gp;

    public AssetSetter(Panel gp) {
        this.gp = gp;
    }
    public void setObject() {
        gp.obj[0] = new Coffee(gp);
        gp.obj[0].worldX = gp.tileSize * 21;
        gp.obj[0].worldY = gp.tileSize * 21;

        gp.obj[1] = new CheatSheet(gp);
        gp.obj[1].worldX = gp.tileSize * 23;
        gp.obj[1].worldY = gp.tileSize * 22;

        gp.obj[2] = new Pencil(gp);
        gp.obj[2].worldX = gp.tileSize * 45;
        gp.obj[2].worldY = gp.tileSize * 40;
    }
}
