package ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

class StarDisplayPanel extends Table {

    // The widgets used in this panel
    private final Image bronze;
    private final Image silver;
    private final Image gold;

    // The UI skin
    private Skin skin;

    // The names of the images in the Skin file used by this panel
    private static final String IMG_EMPTY = "Star_Incomplete_Icon";
    private static final String IMG_STAR = "Star_Complete_Icon";


    StarDisplayPanel(Skin skin) {
    	
        this.skin = skin;

        this.bronze = new Image();
        this.silver = new Image();
        this.gold = new Image();

        setNotComplete();
        layoutWidgets();
    }

    private void layoutWidgets() {

        Table stars = new Table();
        stars.add(bronze);
        stars.add(silver);
        stars.add(gold);

        add(stars);
    }

    public void setNotComplete() {
        bronze.setDrawable(skin.getDrawable(IMG_EMPTY));
        silver.setDrawable(skin.getDrawable(IMG_EMPTY));
        gold.setDrawable(skin.getDrawable(IMG_EMPTY));
    }

    public void setCompleteBronze() {
        bronze.setDrawable(skin.getDrawable(IMG_STAR));
        silver.setDrawable(skin.getDrawable(IMG_EMPTY));
        gold.setDrawable(skin.getDrawable(IMG_EMPTY));
    }

    public void setCompleteSilver() {
        bronze.setDrawable(skin.getDrawable(IMG_STAR));
        silver.setDrawable(skin.getDrawable(IMG_STAR));
        gold.setDrawable(skin.getDrawable(IMG_EMPTY));
    }

    public void setCompleteGold() {
        bronze.setDrawable(skin.getDrawable(IMG_STAR));
        silver.setDrawable(skin.getDrawable(IMG_STAR));
        gold.setDrawable(skin.getDrawable(IMG_STAR));
    }
}
