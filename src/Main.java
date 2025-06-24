import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
//Main TOTAL: 10 MARKS
//Compilation and Correctness: 10 MARKS
public class Main {

    private static final String[] COLOR_TAGS = {"green", "blue", "brown", "white", "black", "yellow", "red", "gray"};

    private static final Map<String, ColorRange> colorMap = Map.of(
        "green", new ColorRange(new Color(0, 100, 0), new Color(120, 255, 120)),
        "blue", new ColorRange(new Color(0, 0, 100), new Color(120, 120, 255)),
        "brown", new ColorRange(new Color(80, 30, 0), new Color(150, 100, 80)),
        "white", new ColorRange(new Color(200, 200, 200), new Color(255, 255, 255)),
        "black", new ColorRange(new Color(0, 0, 0), new Color(50, 50, 50)),
        "yellow", new ColorRange(new Color(200, 200, 0), new Color(255, 255, 150)),
        "red", new ColorRange(new Color(150, 0, 0), new Color(255, 100, 100)),
        "gray", new ColorRange(new Color(100, 100, 100), new Color(200, 200, 200))
    );

    public static Set<String> generateTags(File imageFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        Map<String, Integer> tagCounts = new HashMap<>();

        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y += 5) {
            for (int x = 0; x < width; x += 5) {
                Color pixel = new Color(image.getRGB(x, y));
                for (String tag : COLOR_TAGS) {
                    if (colorMap.get(tag).contains(pixel)) {
                        tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);
                    }
                }
            }
        }

        return tagCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    static class ColorRange {
        Color min, max;
        public ColorRange(Color min, Color max) {
            this.min = min;
            this.max = max;
        }

        public boolean contains(Color c) {
            return c.getRed() >= min.getRed() && c.getRed() <= max.getRed() &&
                   c.getGreen() >= min.getGreen() && c.getGreen() <= max.getGreen() &&
                   c.getBlue() >= min.getBlue() && c.getBlue() <= max.getBlue();
        }
    }

    public static void main(String[] args) throws IOException {
        File imageDir = new File("images/");
        if (!imageDir.exists() || !imageDir.isDirectory()) {
            System.out.println("Image directory not found.");
            return;
        }

        SkipList<String, List<String>> skipList = new SkipList<>();

        for (File file : Objects.requireNonNull(imageDir.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png")))) {
        	//Generate tags for each image and add to skiplist
        	// TODO: COMPLETE - 5 MARKS
        	
        	Set<String> tags = generateTags(file);
            for (String tag : tags) {
                List<String> files = skipList.find(tag) != null ? 
                    new ArrayList<>(skipList.find(tag).getValue()) : new ArrayList<>();
                files.add(file.getName());
                skipList.insert(tag, files);
            }
        }

        // Test search that uses a query that contains a colour and should print out the files that have that colour
        String query = "black";
        // TODO: COMPLETE - 5 MARKS
        
        IEntry<String, List<String>> result = skipList.find(query);
        if (result != null) {
            System.out.println("Files with color " + query + ":");
            for (String filename : result.getValue()) {
                System.out.println(filename);
            }
        } else {
            System.out.println("No files found with color " + query);
        }

    }
}