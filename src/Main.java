import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    private static final StringBuilder stringBuilder = new StringBuilder();

    private static final List<String> filesToArchive = new ArrayList<>();

    private static final List<String> directories = new ArrayList<>();
    private static final List<String> files = new ArrayList<>();

    private static void logger(String logMessage) {
        stringBuilder.append("[" + new Date() + "]" + " " + logMessage);
    }

    private static void saveGame(String path, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gameProgress);
            logger("Игра успешно сохранена\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void zipFiles(String archivePath, List<String> zippedFiles) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(archivePath + "savedgames.zip"))) {
            for (String zipFile : zippedFiles) {
                FileInputStream fis = new FileInputStream(zipFile);
                ZipEntry zipEntry = new ZipEntry(zipFile);
                zos.putNextEntry(zipEntry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                //добавляем содержимое к архиву
                zos.write(buffer);
                fis.close();
                logger("Архивация  " + zipFile + " успешно выполнена в файл savedgames.zip\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteFile() {
        for (String fileToDelete : filesToArchive) {
            File file = new File(fileToDelete);
            if (file.delete()) {
                logger("Файл " + fileToDelete + " успешно удален\n");
            }
        }
    }

    private static void unZipFiles(String zipFilePath) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath + "savedgames.zip"))) {
            ZipEntry entry;
            String name;
            while ((entry = zis.getNextEntry()) != null) {
                name = entry.getName();
                //Распаковка
                FileOutputStream fos = new FileOutputStream(name);
                for (int c = zis.read(); c != -1; c = zis.read()) {
                    fos.write(c);
                }
                fos.flush();
                zis.closeEntry();
                fos.close();
                logger("Файл " + name + " успешно разархивирован\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        directories.add("Games/src");
        directories.add("Games/src/main");
        files.add("Games/src/main/Main.java");
        files.add("Games/src/main/Utils.java");
        directories.add("Games/src/test");
        directories.add("Games/res");
        directories.add("Games/res/drawable");
        directories.add("Games/res/vectors");
        directories.add("Games/res/icons");
        directories.add("Games/savegames");
        directories.add("Games/temp");
        files.add("Games/temp/temp.txt");

        for (String directoryPath : directories) {
            File tmp = new File(directoryPath);
            if (tmp.mkdir()) {
                logger("Папка " + directoryPath + " успешно создана\n");
            }
        }

        for (String filePath : files) {
            File tmp = new File(filePath);
            try {
                if (tmp.createNewFile()) {
                    logger("Файл " + tmp + " успешно создан");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        GameProgress gameProgress1 = new GameProgress(78, 46, 2, 134.8);
        GameProgress gameProgress2 = new GameProgress(25, 4, 1, 10.7);
        GameProgress gameProgress3 = new GameProgress(99, 99, 6, 500.4);
        int i = 0;
        saveGame("Games/savegames/data" + i++ + ".dat", gameProgress1);
        saveGame("Games/savegames/data" + i++ + ".dat", gameProgress2);
        saveGame("Games/savegames/data" + i++ + ".dat", gameProgress3);
        i = 0;
        filesToArchive.add("Games/savegames/data" + i++ + ".dat");
        filesToArchive.add("Games/savegames/data" + i++ + ".dat");
        filesToArchive.add("Games/savegames/data" + i++ + ".dat");
        zipFiles("Games/savegames/", filesToArchive);
        deleteFile();
        unZipFiles("Games/savegames/");

        GameProgress deserializedGameProgress = null;

        try (FileInputStream fis = new FileInputStream("Games/savegames/data1.dat"); ObjectInputStream ous = new ObjectInputStream(fis)) {
            deserializedGameProgress = (GameProgress) ous.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(deserializedGameProgress);

        File tempFile = new File("Games/temp/temp.txt");
        if (tempFile.exists()) {
            try (FileWriter fw = new FileWriter(tempFile, true)) {
                fw.write(stringBuilder.toString());
                fw.flush();
                System.out.println("Информация о созданных папках и файлах записана в файл " + tempFile.getPath());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}