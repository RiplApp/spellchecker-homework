import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Checker {
    public Checker() { }

    public static void unzipFile(String path, String destDir) throws IOException {
        //ClassLoader classLoader = Checker.class.getClassLoader();
        //String path = classLoader.getResource(fileName).getPath();
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();
        System.out.println("made file");
        FileInputStream fis;
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(path);
            System.out.println("file input stream");
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = null;
            ze = zis.getNextEntry();
            System.out.println(ze);
            while (ze != null) {
                // DOES NOT SATISFY THIS WHILE CONDITION
                System.out.println("ze not equal null");
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to " + newFile.getAbsolutePath());
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.getCause();
        }
    }


    public static void main(String[] args) throws Exception {
        //String zipFilePath = "words.gz";
        String destDir = "/Users/kioritanaka/Desktop/CS307/spellchecker-homework";
        String zipFilePath = "/Users/kioritanaka/Desktop/CS307/spellchecker-homework/data/words.gz";
        FileInputStream fis = new FileInputStream(zipFilePath);
        ZipInputStream zis = new ZipInputStream(fis);
        //System.out.print(zis);
        unzipFile(zipFilePath,destDir);

    }

}
