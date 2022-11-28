package SearchModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class PageUtils { //class of mostly string or file/list operations. Meant to be a general helper class to clear up some boilerplate
    protected String[] getWords(File file) { //returns array of words in the <p> tags in a page
        int counter = 0;
        String[] lines = fileRead(file);
        ArrayList<String> output = new ArrayList<>();
        boolean tag = false; //denotes if the current line being read is the correct data

        for (String e : lines) {
            if (Objects.equals(e, "frequencies")) {
                tag = true;
            } else if (Objects.equals(e, "/frequencies")) {
                break;
            }
            if (tag) {
                if (counter % 2 == 1) {
                    output.add(e.replace("\n", ""));
                }
                counter += 1;
            }
        }
        String[] out = new String[output.size()];
        out = output.toArray(out);
        return out;
    }
    public static String[] splitPage(String url) { //takes the input of url and returns a more usable array than what readURL would give
        String page = "";
        try {
            page = WebRequester.readURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] out = page.split("\n");
        for (int i = 0; i < out.length; i++) {
            out[i] = out[i].replace("\n", "");
        }
        return out;
    }

    public static String[] fileRead (File file) { //takes in a file and returns an array of its content.
        ArrayList<String> content = new ArrayList<>();
        try (Scanner read = new Scanner(file)) {
            while (read.hasNextLine()) {
                content.add(read.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        }
        String[] output = new String[content.size()];
        output = content.toArray(output);
        return output;
    }
    public static String makeURLTitle (String url){ //auto generates a name for a file based off of a url. Unique URL should generate unique name.
        url = url.replaceAll("\\.", "Dot");
        url = url.replaceAll("/", "Slash");
        return url.replaceAll("[^a-zA-Z0-9]", " ") + ".txt";
    }
    public static void wipeData (File file) {
        File[] content = file.listFiles();
        for (File f: content != null ? content : new File[0]) {
            if (f.delete()) {
            } else {
            }
        }
    }
    public String stripTitle(String element) { //returns the title from an HTML header element.
        String[] header = element.split(">");
        String title = "";
        for (String s : header) {
            if (!Objects.equals(s.split("<")[0], "")) title = s.split("<")[0];
        }
        return title;
    }
    public static String processURL(String base, String link) { //Takes in a base url and a relative URL, determines if relative or absolute link, creates url.
        StringBuilder url = new StringBuilder();
        String process = base.split(">")[0];
        process = process.replace("\"", "");

        if (Objects.equals(process.split("./")[0], "")) {
            String[] urlArr = link.split("/");
            for (int i = 0; i < urlArr.length; i++) {
                urlArr[i] = urlArr[i] + "/";
            }
            urlArr[urlArr.length - 1] = process.replace("./", "");
            for (String e:urlArr) {
                url.append(e);
            }
            return url.toString();
        } else {
            return process;
        }
    }
}
