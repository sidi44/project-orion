package ui;

import com.badlogic.gdx.graphics.Color;

public class FontConfiguration {
    
    private String fontName;
    private String fontFilePath;
    private String fontColour; // Hex representation of the rgba8888 colour
    private int fontBorderWidth;
    
    public FontConfiguration() {
        
        fontFilePath = "data/fonts/droid-serif-bold.ttf";
        fontName = "droid-seriff-bold-font";
        fontBorderWidth = 2;
        fontColour = "0x7fff00ff"; // basically green
    }


    public String getFontName() {
        return fontName;
    }

    public String getFontFilePath() {
        return fontFilePath;
    }

    public Color getFontColour() {
        return Color.valueOf(fontColour);
    }

    public int getFontBorderWidth() {
        return fontBorderWidth;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public void setFontFilePath(String fontFilePath) {
        this.fontFilePath = fontFilePath;
    }

    public void setFontColour(String fontColour) {
        this.fontColour = fontColour;
    }

    public void setFontBorderWidth(int fontBorderWidth) {
        this.fontBorderWidth = fontBorderWidth;
    }
}