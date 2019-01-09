package ch.uzh.marugoto.core.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.uzh.marugoto.core.data.entity.ImageResource;
import ch.uzh.marugoto.core.exception.ResourceNotFoundException;
import ch.uzh.marugoto.core.service.FileService;
import ch.uzh.marugoto.core.service.ImageService;
import ch.uzh.marugoto.core.test.BaseCoreTest;

public class ImageServiceTest extends BaseCoreTest {
    @Autowired
    private ImageService imageService;
    private final int imageWidth = 600;
    private final String imageUrl = String.format("https://picsum.photos/%s/?random", imageWidth);

    @Test
    public void testResizeImageFromWidth() throws IOException {
        var resizedWidth = 400;
        var storeImageFolder = FileService.generateFolder(System.getProperty("user.home") + File.separator + "unit-test-folder");
        var imagePath = Paths.get(storeImageFolder + File.separator + "img.jpg");
        ImageIO.write(ImageIO.read(new URL(imageUrl)), "jpg", imagePath.toFile());

        imagePath = imageService.resizeImage(imagePath, resizedWidth);
        var resizedImageWidth = imageService.getImageWidth(imagePath);

        assertNotEquals(resizedImageWidth, imageWidth);
        assertEquals(resizedWidth, resizedImageWidth);

        FileService.deleteFolder(storeImageFolder.getAbsolutePath());
    }

    @Test
    public void testResizeImageFromColumn() throws IOException {
        var storeImageFolder = FileService.generateFolder(System.getProperty("user.home") + File.separator + "unit-test-folder");
        var imagePath = Paths.get(storeImageFolder + File.separator + "img.jpg");
        ImageIO.write(ImageIO.read(new URL(imageUrl)), "jpg", imagePath.toFile());

        var columns = 3;
        imagePath = imageService.resizeImage(imagePath, imageService.getImageWidthFromColumns(columns));
        var resizedImageWidth = imageService.getImageWidth(imagePath);

        assertNotEquals(resizedImageWidth, imageWidth);
        assertEquals(imageService.getImageWidthFromColumns(columns), resizedImageWidth);

        FileService.deleteFolder(storeImageFolder.getAbsolutePath());
    }
}
