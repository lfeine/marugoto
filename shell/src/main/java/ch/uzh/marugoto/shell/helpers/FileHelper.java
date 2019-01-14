package ch.uzh.marugoto.shell.helpers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

abstract public class FileHelper {
    private static String rootFolder;
    public static final String JSON_EXTENSION = ".json";

    private final static ObjectMapper mapper = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).disable(MapperFeature.USE_ANNOTATIONS)
            .disable(MapperFeature.ALLOW_EXPLICIT_PROPERTY_RENAMING);

    public static ObjectMapper getMapper() {
        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        return mapper;
    }

    public static String getRootFolder() {
        return rootFolder;
    }

    public static void setRootFolder(String filePath) {
        rootFolder = filePath;
    }

    /**
     * Returns all files that are not hidden from folder
     *
     * @param pathToDirectory
     * @return
     */
    public static File[] getAllFiles(String pathToDirectory) {
        File folder = new File(pathToDirectory);
        return folder.listFiles(file -> !file.isHidden() && !file.isDirectory());
    }

    public static File[] getAllDirectories(String pathToDirectory) {
        File folder = new File(pathToDirectory);
        var dirs = folder.listFiles(file -> !file.isHidden() && file.isDirectory());
        Arrays.sort(dirs);
        return dirs;
    }

    /**
     * Create object from json file
     *
     * @param file
     * @param cls
     * @return
     * @throws IOException
     */
    public static Object generateObjectFromJsonFile(File file, Class<?> cls) throws IOException {
        return getMapper().readValue(file, cls);
    }

    /**
     * Generate multiple json files from provided object
     *
     * @param object
     * @param fileName
     * @param destinationFolder
     * @param numOfFiles
     */
    public static void generateInitialJsonFilesFromObject(Object object, String fileName, File destinationFolder,
                                                          int numOfFiles) {
        for (int i = 1; i <= numOfFiles; i++) {
            var jsonFileName = fileName + i;
            generateJsonFileFromObject(object, jsonFileName, destinationFolder);
        }
    }

    /**
     * Create/update json file and writes it to destination folder
     *
     *
     * @param object
     * @param fileName
     * @param destinationFolder
     */
    public static void generateJsonFileFromObject(Object object, String fileName, File destinationFolder) {
        var jsonPath = destinationFolder.getPath() + File.separator + fileName + JSON_EXTENSION;
        generateJsonFileFromObject(object, jsonPath);
    }

    /**
     * Create/update json file
     *
     * @param object
     * @param pathToFile
     */
    public static void generateJsonFileFromObject(Object object, String pathToFile) {
        try {
            getMapper().writeValue(new File(pathToFile), object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateReferenceValueInJsonFile(JsonNode jsonNode, String key, Object value, File jsonFile) {
        ((ObjectNode) jsonNode).replace(key, getMapper().convertValue(value, JsonNode.class));
        FileHelper.generateJsonFileFromObject(jsonNode, jsonFile.getAbsolutePath());
    }

    public static File getJsonFileByReference(String relativePath) {
        return Paths.get(getRootFolder() + File.separator + relativePath).toFile();
    }

    public static String getJsonFileRelativePath(File file) {
        return getJsonFileRelativePath(file.getAbsolutePath());
    }

    public static String getJsonFileRelativePath(String filePath) {
        return filePath.replace(getRootFolder() + File.separator, "");
    }


    public static String removeDigitsDotsAndWhitespacesFromFileName(String fileName) {
        return fileName.replaceAll("\\d", "");
    }

}
