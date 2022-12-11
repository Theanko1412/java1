package hr.fer.zemris.java.hw06.shell;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.System.exit;

public class MyShell {

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to MyShell v 1.0");

        Character PROMPTSYMBOL = '>';
        Character MORELINESSYMBOL = '\\';
        Character MULTILINE = '|';

        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.print(PROMPTSYMBOL + " ");

            Stream<String> stream = Arrays.stream(sc.nextLine().strip().split(" ")).map(String::strip);
            ArrayList<String> nextLine = new ArrayList<>(stream.toList());
            int nextLineLast = nextLine.size()-1;
            if(nextLine.get(0).equals("exit")) exit(0);

            if(nextLine.get(nextLineLast).equals(MORELINESSYMBOL.toString())) {
                nextLine.remove(nextLineLast);
                System.out.print(MULTILINE + " ");
                Stream<String> multiLineStream = Arrays.stream(sc.nextLine().strip().split(" ")).map(String::strip);
                ArrayList<String> multiLine = new ArrayList<>(multiLineStream.toList());
                int multiLineLast = multiLine.size()-1;
                while(multiLine.get(multiLineLast).equals(MORELINESSYMBOL.toString())) {
                    System.out.print(MULTILINE + " ");
                    multiLine.remove(multiLineLast);
                    nextLine.addAll(multiLine);
                    multiLineStream = Arrays.stream(sc.nextLine().strip().split(" ")).map(String::strip);
                    multiLine = new ArrayList<>(multiLineStream.toList());
                    multiLineLast = multiLine.size()-1;
                    if(multiLine.get(0).equals("exit")) exit(0);
                }
                if(!Objects.equals(multiLine.get(0), "")) {
                    nextLine.addAll(multiLine);
                }
            }

            switch (nextLine.get(0)) {
                case "symbol":
                    if (nextLine.size() == 2) {
                        switch (nextLine.get(1)) {
                            case "PROMPT" -> System.out.printf("Symbol for PROMPT is '%c'\n", PROMPTSYMBOL);
                            case "MORELINES" -> System.out.printf("Symbol for MORELINES is '%c'\n", MORELINESSYMBOL);
                            case "MULTILINE" -> System.out.printf("Symbol for MULTILINE is '%c'\n", MULTILINE);
                        }
                    } else if (nextLine.size() == 3) {
                        switch (nextLine.get(1)) {
                            case "PROMPT" -> {
                                Character old = PROMPTSYMBOL;
                                PROMPTSYMBOL = nextLine.get(2).charAt(0);
                                System.out.printf("Symbol for PROMPT changed from '%c' to '%c'\n", old, PROMPTSYMBOL);
                            }
                            case "MORELINES" -> {
                                Character old = MORELINESSYMBOL;
                                MORELINESSYMBOL = nextLine.get(2).charAt(0);
                                System.out.printf("Symbol for MORELINES changed from '%c' to '%c'\n", old, MORELINESSYMBOL);
                            }
                            case "MULTILINE" -> {
                                Character old = MULTILINE;
                                MULTILINE = nextLine.get(2).charAt(0);
                                System.out.printf("Symbol for MULTILINE changed from '%c' to '%c'\n", old, MULTILINE);
                            }
                        }
                    } else System.out.println("Number of arguments doesnt match for 'symbol'!");
                    break;
                case "charsets":
                    Charset.availableCharsets().keySet().forEach(System.out::println);
                    break;
                case "cat":
                    if (nextLine.size() == 2) {
                        Path path = Paths.get(nextLine.get(1));
                        BufferedReader br;
                        try {
                            br = new BufferedReader(
                                    new InputStreamReader(
                                            new BufferedInputStream(
                                                    Files.newInputStream(path))));
                        } catch (NoSuchFileException e) {
                            System.out.println("File not found in this directory!");
                            continue;
                        }


                        Writer bw = new BufferedWriter(
                                new OutputStreamWriter(
                                        new BufferedOutputStream(System.out)));

                        while (br.ready()) {
                            bw.write(br.readLine());
                            bw.write("\n");
                            bw.flush();
                        }

                    } else if (nextLine.size() == 3) {
                        Path path = Paths.get(nextLine.get(1));
                        BufferedReader br;
                        try {
                            br = new BufferedReader(
                                    new InputStreamReader(
                                            new BufferedInputStream(
                                                    Files.newInputStream(path)), nextLine.get(2)));
                        } catch (NoSuchFileException e) {
                            System.out.println("File not found in this directory!");
                            continue;
                        } catch (UnsupportedEncodingException e) {
                            System.out.println("Given encoding is not supported!");
                            continue;
                        }

                        Writer bw = new BufferedWriter(
                                new OutputStreamWriter(
                                        new BufferedOutputStream(System.out), nextLine.get(2)));

                        while (br.ready()) {
                            bw.write(br.readLine());
                            bw.write("\n");
                            bw.flush();
                        }

                    } else System.out.println("Number of arguments doesnt match for 'cat'!");
                    break;
                case "ls": {
                    if (nextLine.size() != 2) {
                        System.out.println("Command ls requires 1 additional argument - directory!");
                        continue;
                    }
                    File directory = new File(nextLine.get(1));
                    File[] children = directory.listFiles();
                    if (children == null) {
                        System.out.println("No files or directories found!");
                        continue;
                    }
                    long size = 0;
                    for (File file : children) {
                        if (file.length() > size) size = file.length();
                    }

                    for (File file : children) {
                        if (file.isDirectory()) {
                            System.out.print("d");
                        } else System.out.print("-");
                        if (file.canRead()) {
                            System.out.print("r");
                        } else System.out.print("-");
                        if (file.canWrite()) {
                            System.out.print("w");
                        } else System.out.print("-");
                        if (file.canExecute()) {
                            System.out.print("x");
                        } else System.out.print("-");

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Path path = Paths.get(file.toString());
                        BasicFileAttributeView faView = Files.getFileAttributeView(
                                path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
                        );
                        BasicFileAttributes attributes = faView.readAttributes();
                        FileTime fileTime = attributes.creationTime();
                        String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));

                        String format = " %" + String.valueOf(size).length() + "s" + " " + "%s" + " " + "%s\n";
                        System.out.printf(format, file.length(), formattedDateTime, file.getName());
                    }
                    //NOT WORKING -R
                    break;
                }
                case "tree": {
                    if (nextLine.size() != 2) {
                        System.out.println("Command tree requires 1 additional argument - directory!");
                        continue;
                    }
                    File directory = new File(nextLine.get(1));
                    File[] children = directory.listFiles();
                    if (children == null) {
                        System.out.println("No files or directories found!");
                        continue;
                    }

                    for (File file : children) {
                        if (file.isDirectory()) {
                            System.out.println("  " + file.getName());
                        } else {
                            System.out.println(file.getName());
                        }
                    }
                    break;
                }
                case "copy": {
                    continue;
                }
                case "mkdir": {
                    if (nextLine.size() != 2) {
                        System.out.println("Command mkdir requires 1 additional argument - directory!");
                        continue;
                    }
                    boolean directory = new File(nextLine.get(1)).mkdirs();
                    break;
                }
                case "hexdump":
                    if (nextLine.size() != 2) {
                        System.out.println("Command hexdump requires 1 additional argument - file!");
                        continue;
                    }
                    break;
            }
    }
}
}
