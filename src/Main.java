import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    private static File srcFolder = new File("Games/src");
    private static File mainFolder = new File("Games/src/main");

    private static File mainJavaFile = new File("Games/src/main/Main.java");
    private static File utilsJavaFile = new File("Games/src/main/Utils.java");

    private static File testFolder = new File("Games/src/test");


    private static File resFolder = new File("Games/res");
    private static File drawableFolder = new File("Games/res/drawable");
    private static File vectorsFolder = new File("Games/res/vectors");
    private static File iconsFolder = new File("Games/res/icons");
    private static File saveGamesFolder = new File("Games/savegames");
    private static File tempFolder = new File("Games/temp");
    private static File tempFile = new File("Games/temp/temp.txt");

    private static StringBuilder stringBuilder = new StringBuilder();

    private static List<String> filesToArchive = new ArrayList<>();


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

        if (tempFolder.mkdir()) {
            logger("Каталог temp успешно создан\n");
        }

        if (srcFolder.mkdir()) {
            logger("Каталог src успешно создан\n");
        }
        if (resFolder.mkdir()) {
            logger("Каталог res успешно создан\n");
        }
        if (saveGamesFolder.mkdir()) {
            logger("Каталог savegames успешно создан\n");
        }
        if (mainFolder.mkdir()) {
            logger("Каталог main успешно создан\n");
        }
        if (testFolder.mkdir()) {
            logger("Каталог test успешно создан\n");
        }

        try {
            if (mainJavaFile.createNewFile()) {
                logger("Файл Main.java успешно создан\n");
            }
            if (tempFile.createNewFile()) {
                logger("Файл temp.txt успешно создан\n");
            }
            if (utilsJavaFile.createNewFile()) {
                logger("Файл Utils.java успешно создан\n");
            }
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }

        if (drawableFolder.mkdir()) {
            logger("Каталог drawable успешно создан\n");
        }

        if (vectorsFolder.mkdir()) {
            logger("Каталог vectors успешно создан\n");
        }

        if (iconsFolder.mkdir()) {
            logger("Каталог icons успешно создан\n");
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

        try (FileWriter fw = new FileWriter(tempFile, true)) {
            fw.write(stringBuilder.toString());
            fw.flush();
            System.out.println("Информация о созданных папках и файлах записана в файл " + tempFile.getPath());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}