package part03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        Path logFile = Path.of("pizza-commits.txt");
        String fileContents = Files.readString(logFile, StandardCharsets.UTF_8);

        
        List<String> lines = new ArrayList<>();
        for (String line : fileContents.split("\\R")) {
            if (!line.isBlank()) {
                lines.add(line.trim());
            }
        }

        
        Map<String, Commit> commitsByParent = new HashMap<>();
        for (int i = 0; i + 2 < lines.size(); i += 3) {
            Commit commit = new Commit(lines.get(i), lines.get(i + 1), lines.get(i + 2));
            commitsByParent.put(commit.parentId, commit);
        }

        
        Commit current = commitsByParent.get("None");
        while (current != null) {
            System.out.println(current.headerLine);
            System.out.println(current.title);
            System.out.println(current.message);
            System.out.println();
            current = commitsByParent.get(current.id);
        }
    }

    private static class Commit {
        final String headerLine;
        final String title;
        final String message;
        final String id;
        final String parentId;

        Commit(String headerLine, String title, String message) {
            this.headerLine = headerLine;
            this.title = title;
            this.message = message;
            // "Commit <id> by <tekijä> (Parent: <id>)"
            this.id = headerLine.split(" ")[1];
            int start = headerLine.indexOf("(Parent: ") + "(Parent: ".length();
            int end = headerLine.lastIndexOf(')');
            this.parentId = headerLine.substring(start, end);
        }
    }
}