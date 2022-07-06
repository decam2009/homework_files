import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

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

    private static void logger(String logMessage) throws IOException {
        stringBuilder.append(logMessage);
   }

    public static void main(String[] args) throws IOException {

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
            throw new RuntimeException(e);
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

        FileWriter fw = new FileWriter(tempFile, true);
        fw.write(stringBuilder.toString());
        fw.flush();
        System.out.println("Информация о созданных папках и файлах записана в файл " + tempFile.getPath());
    }
    
}
