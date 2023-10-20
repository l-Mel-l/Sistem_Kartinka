import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    private static final String DOWNLOAD_DIRECTORY = "src";
    private static final Pattern IMAGE_PATTERN = Pattern.compile("(https?://.*?\\.(?:png|jpe?g|gif))");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String url = sc.next();

        try {
            String imageUrl = findImageInPage(url);
            if (imageUrl != null) {
                downloadImage(imageUrl, DOWNLOAD_DIRECTORY + "/image.jpg");
                System.out.println("Картинка успешно скачана!");
            } else {
                System.out.println("Картинка не найдена на странице.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filePath = "src\\image.jpg";
        File file = new File(filePath);
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String findImageInPage(String url) throws IOException {
        StringBuilder pageContent = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                pageContent.append(line);
            }
        }

        Matcher matcher = IMAGE_PATTERN.matcher(pageContent.toString());
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private static void downloadImage(String imageUrl, String filePath) throws IOException {
        URL url = new URL(imageUrl);
        ReadableByteChannel byteChannel = Channels.newChannel(url.openStream());

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            fileOutputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
        }
    }
}