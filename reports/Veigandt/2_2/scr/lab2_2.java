import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input = reader.readLine();
            String[] params = input.split("\\s+");

            boolean fOption = false;
            boolean iOption = false;
            String sourceFile = null;
            String targetFile = null;

            for (int i = 1; i < params.length; i++) {
                String param = params[i];
                if (param.contains("-")) {
                    if (param.contains("i")){
                        iOption = true;
                    }if (param.contains("f")){
                        fOption = true;
                    }if (param.contains("n")){
                        iOption = false;
                    }
                } else if (sourceFile == null) {
                    sourceFile = param;
                } else if (targetFile == null) {
                    targetFile = param;
                }
            }

            if (sourceFile == null || targetFile == null) {
                System.out.println("Error: Please provide both source and target file paths.");
                return;
            }
            File source = new File(sourceFile);
            File target = new File(targetFile);

            if (target.exists()) {
                if(iOption){
                    System.out.print("File already exists. Overwrite? (y/n): ");
                    reader = new BufferedReader(new InputStreamReader(System.in));
                    input = reader.readLine();
                    if (!input.equals("y")) {
                        System.out.println("Copying aborted.");
                        return;
                    }
                } else if (!fOption) {
                    System.out.println("Target File already exists.");
                    return;
                }
            }

            FileInputStream inputStream = new FileInputStream(source);
            FileOutputStream outputStream = new FileOutputStream(target);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            System.out.println("File copied successfully.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}