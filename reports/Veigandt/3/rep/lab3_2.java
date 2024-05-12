import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

class FileSystemObject {
    String name;

    public FileSystemObject(String name) {
        this.name = name;
    }
}

class File extends FileSystemObject {
    String extension;
    int size;

    public File(String name, String extension, int size) {
        super(name);
        this.extension = extension;
        this.size = size;
    }
}

class Directory extends FileSystemObject {
    List<FileSystemObject> contents;

    public Directory(String name) {
        super(name);
        this.contents = new ArrayList<>();
    }

    public void addFile(File file) {
        contents.add(file);
        System.out.println("Файл '" + file.name + "." + file.extension + "' добавлен в директорию '" + name + "'.");
    }

    public void addDirectory(Directory directory) {
        contents.add(directory);
        System.out.println("Директория '" + directory.name + "' добавлена в директорию '" + name + "'.");
    }

    public void printTree(String prefix) {
        System.out.println(prefix + name);
        for (FileSystemObject object : contents) {
            if (object instanceof File) {
                System.out.println(prefix + "  - " + ((File) object).name + "." + ((File) object).extension);
            }
        }
        for (FileSystemObject object : contents) {
            if (object instanceof Directory) {
                ((Directory) object).printTree(prefix + "  ");
            }
        }
    }

}

class Segment {
    int start;
    int size;
    String fileName = "free";

    public Segment(int start, int size) {
        this.start = start;
        this.size = size;
    }
    public Segment(int start, int size, String fileName) {
        this.start = start;
        this.size = size;
        this.fileName = fileName;
    }
}

class Disk {
    int totalSize;
    int freeSpace;
    Directory rootDirectory;
    List<Segment> freeSegments;

    public Disk(int totalSize) {
        this.totalSize = totalSize;
        this.freeSpace = totalSize;
        this.rootDirectory = new Directory("root");
        this.freeSegments = new ArrayList<>();
        this.freeSegments.add(new Segment(0, totalSize));
    }

    public void addFile(File file, String path) {
        Directory currentDirectory = findDirectory(path);
        if (currentDirectory == null) {
            System.out.println("Ошибка: Директория '" + path + "' не найдена. Файл не добавлен.");
            return;
        }

        if (file.size > totalSize) {
            System.out.println("Ошибка: Размер файла '" + file.name + "' превышает размер диска. Файл не добавлен.");
            return;
        }

        if (file.size > freeSpace) {
            System.out.println("Ошибка: Недостаточно свободного места на диске для файла '" + file.name + "'. Файл не добавлен.");
            return;
        }

        currentDirectory.addFile(file);
        allocateSpace(file, true);
    }

    public void addDirectory(Directory directory, String path) {
        Directory currentDirectory = findDirectory(path);
        if (currentDirectory == null) {
            System.out.println("Ошибка: Директория '" + path + "' не найдена. Директория не добавлена.");
            return;
        }

        currentDirectory.addDirectory(directory);
    }

    public void removeFile(String fileName, String path) {
        Directory currentDirectory = findDirectory(path);
        if (currentDirectory == null) {
            return;
        }

        Iterator<FileSystemObject> iterator = currentDirectory.contents.iterator();
        while (iterator.hasNext()) {
            FileSystemObject object = iterator.next();
            if (object instanceof File && object.name.equals(fileName)) {
                iterator.remove();
                freeSpace += ((File) object).size;
                allocateSpace((File) object,false);
                System.out.println("Файл '" + fileName + "' удален из директории '" + path + "'.");
                return;
            }
        }

        System.out.println("Ошибка: Файл '" + fileName + "' не найден в директории '" + path + "'.");
    }

    public void removeDirectory(String directoryName, String path) {
        Directory currentDirectory = findDirectory(path);
        if (currentDirectory == null) {
            return;
        }

        Iterator<FileSystemObject> iterator = currentDirectory.contents.iterator();
        while (iterator.hasNext()) {
            FileSystemObject object = iterator.next();
            if (object instanceof Directory && object.name.equals(directoryName)) {
                iterator.remove();
                freeSpace += calculateDirectorySize((Directory) object);
                removeTree((Directory) object);
                System.out.println("Директория '" + directoryName + "' удалена из директории '" + path + "'.");
                return;
            }
        }

        System.out.println("Ошибка: Директория '" + directoryName + "' не найдена в директории '" + path + "'.");
    }

    public void removeTree(Directory directory) {
        for (FileSystemObject obj : directory.contents){
            if (obj instanceof File){
                removeFile(obj.name, directory.name);
                allocateSpace((File) obj, false);
            }
        }
        for (FileSystemObject object : directory.contents) {
            if (object instanceof Directory) {
                removeTree((Directory) object);
            }
        }
    }

    private int calculateDirectorySize(Directory directory) {
        int size = 0;
        for (FileSystemObject object : directory.contents) {
            if (object instanceof File) {
                size += ((File) object).size;
            } else if (object instanceof Directory) {
                size += calculateDirectorySize((Directory) object);
            }
        }
        return size;
    }

    private void allocateSpace(File file, boolean mode) {
        for (int i = 0; i < freeSegments.size(); i++) {
            Segment suitableSegment = freeSegments.get(i);
            if(mode) {
                if (suitableSegment.fileName.equals("free") && file.size <= suitableSegment.size) {
                    freeSpace -= file.size;
                    int newStart = suitableSegment.start + file.size;
                    int newSize = suitableSegment.size - file.size;
                    Segment freeSegment = new Segment(newStart, newSize);
                    freeSegments.add(freeSegment);
                    suitableSegment.fileName = file.name;
                    suitableSegment.size = file.size;
                    return;
                }
            } else {
                if(suitableSegment.fileName.equals(file.name)){
                    suitableSegment.fileName = "free";
                    mergeFreeSegments();
                }
            }
        }
    }

    private void mergeFreeSegments() {
        freeSegments.sort(Comparator.comparingInt(segment -> segment.start));
        for (int i = 0; i < freeSegments.size() - 1; i++) {
            Segment currentSegment = freeSegments.get(i);
            Segment nextSegment = freeSegments.get(i + 1);
            if (currentSegment.fileName == "free" && nextSegment.fileName == "free") {
                currentSegment.size += nextSegment.size;
                freeSegments.remove(i + 1);
                i--;
            }
        }
    }

    private Directory findDirectory(String path) {
        if (path.equals("root")) {
            return rootDirectory;
        }

        String[] parts = path.split("/");
        Directory currentDirectory = rootDirectory;
        boolean directoryFound = false;
        for (String part : parts) {

            for (FileSystemObject object : currentDirectory.contents) {
                if (object instanceof Directory && object.name.equals(part)) {
                    currentDirectory = (Directory) object;
                    directoryFound = true;
                    break;
                }
            }

        }

        if (!directoryFound) {
            return null;
        }

        return currentDirectory;
    }

    public void printDiskStatus() {
        System.out.println("Общий размер диска: " + totalSize + " Кб");
        System.out.println("Свободное место на диске: " + freeSpace + " Кб");
        for(Segment segment : freeSegments){
            System.out.println(segment.start + " - " + (segment.size + segment.start) + "(" + segment.fileName + ")");
        }
        System.out.println("Структура файлов и директорий:");
        rootDirectory.printTree("");
    }
}

public class Zad2 {
        public static void main(String[] args) {
            Disk disk = new Disk(1440);

            try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Вадим\\IdeaProjects\\untitled2\\out\\input.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split("\\s+");

                    switch (tokens[0]) {
                        case "ADD_FILE":
                            File file = new File(tokens[1], tokens[2], Integer.parseInt(tokens[3]));
                            disk.addFile(file, tokens[4]);
                            break;

                        case "ADD_DIRECTORY":
                            Directory directory = new Directory(tokens[1]);
                            disk.addDirectory(directory, tokens[2]);
                            break;

                        case "REMOVE_FILE":
                            disk.removeFile(tokens[1], tokens[2]);
                            break;

                        case "REMOVE_DIRECTORY":
                            disk.removeDirectory(tokens[1], tokens[2]);
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            disk.printDiskStatus();
        }
    }
