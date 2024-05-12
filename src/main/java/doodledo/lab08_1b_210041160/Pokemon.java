package doodledo.lab08_1b_210041160;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Pokemon {
    private String ID;
    private String Name;
    private String Height;
    private  String Weight;
    private String imgSrc;
    private int favourite;

    public Pokemon(String ID, String Name, String Height, String Weight, String imgSrc) {
        this.ID = ID;
        this.Name = Name;
        this.Height = Height;
        this.Weight = Weight;
        this.imgSrc = imgSrc;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getHeight() {
        return Height;
    }

    public String getWeight() {
        return Weight;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public String getColor() {
        try {
            BufferedImage image = ImageIO.read(new URL(getImgSrc()));
            Map<Integer, Integer> colorMap = new HashMap<>();

            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int rgb = image.getRGB(x, y);
                    colorMap.put(rgb, colorMap.getOrDefault(rgb, 0) + 1);
                }
            }

            int maxCount = 0;
            int maxColor = 0;

            for (Map.Entry<Integer, Integer> entry : colorMap.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    maxColor = entry.getKey();
                }
            }

            Color color = new Color(maxColor);
            return String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        } catch (IOException e) {
            throw new RuntimeException("Error reading image", e);
        }
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }
}
