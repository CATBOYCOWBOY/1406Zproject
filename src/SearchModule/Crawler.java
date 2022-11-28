package SearchModule;

import java.io.*;
import java.util.*;

public class Crawler extends SearchData{
    String seed;
    Crawler (String initURL) {
        seed = initURL;
    }
    public void startCrawl () { //Similar setup to previous project, except IDF and PageRank functionality
        File file = new File("src/PageData"); //are nested classes
        File Data = new File("src/Data");
        wipeData(Data);
        wipeData(file);
        HashMap<String, Boolean> visited = new HashMap<>();

        String[] pageOutGoing = parsePage(seed);
        visited.put(seed, true);
        ArrayList<String> pagesQueue = new ArrayList<>(Arrays.asList(pageOutGoing));

        while (pagesQueue.size() > 0) {
            pageOutGoing = parsePage(pagesQueue.get(0));
            visited.put(pagesQueue.get(0), true);
            pagesQueue.remove(0);

            for (String e : pageOutGoing) {
                if (!visited.containsKey(e) && !pagesQueue.contains(e)) {
                    pagesQueue.add(e);
                }
            }
        }
        findIDF(file);
        findPageIncoming(file);
        PageRankFinder rank = new PageRankFinder(file, 0.0001);
        rank.findPagesRank();
    }
    private String[] parsePage (String url) {
        int wordCount = 0;
        String title;
        String[] contents = splitPage(url);
        HashMap <String, Integer> pageMap = new HashMap<>(); //measures TF for words in page
        ArrayList<String> pageOutGoing = new ArrayList<>(); //outgoing links
        ArrayList<String> pageContents = new ArrayList<>();

        pageContents.add("url");
        pageContents.add(url);

        for (String i : contents) {
            String[] arr = i.split("<");
            if (arr.length > 1) {
                if (Objects.equals(arr[1], "html>")) {
                    title = stripTitle(i);
                    pageContents.add("title");
                    pageContents.add(title);
                } else if ((Objects.equals(arr[1].split("=")[0], "a href"))) {
                    pageOutGoing.add(processURL(arr[1].split("=")[1], url));
                }
            } else {
                wordCount += 1;
                if (pageMap.containsKey(i)){
                    pageMap.put(i, pageMap.get(i) + 1);
                } else {
                    pageMap.put(i, 1);
                }
            }
        }
        pageContents.add("frequencies");
        for (String j : pageMap.keySet()) {
            pageContents.add(j);
            float termFreq = (float)pageMap.get(j) / (float)wordCount;
            pageContents.add(String.valueOf(termFreq));
        }
        pageContents.add("/frequencies"); // "/frequencies" denotes the end of the section in the text file where the tf value of each word is stored

        pageContents.add("pageOutgoing");
        pageContents.addAll(pageOutGoing);
        pageContents.add("/pageOutgoing");

        String[] store = new String[pageContents.size()];
        String[] output = new String[pageOutGoing.size()];
        output = pageOutGoing.toArray(output);
        store = pageContents.toArray(store);

        storePage(store, url);
        return output;
    }
    private void storePage (String[] page, String url) { //Destination folder Data is meant to be cleared before this method called //in progress
        String fileName = makeURLTitle(url);
        File file = new File(String.format("src/PageData/%s", fileName));

        try {
            FileWriter writer = new FileWriter(file);
            BufferedWriter writtenTo = new BufferedWriter(writer);
            for (String s : page) {
                writtenTo.write(s + "\n");
            }
            writtenTo.close();
            writer.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
    private void findIDF (File file) {
        HashMap<String, Integer> IDFs = new HashMap<>();
        File idfData = new File("src/Data/idfData.txt");
        File[] pages = file.listFiles();

        if (pages != null) {
            for (File e : pages) {
                String[] contents = getWords(e);
                for (String content : contents) {
                    if (IDFs.containsKey(content)) {
                        IDFs.put(content, IDFs.get(content) + 1);
                    } else {
                        IDFs.put(content, 1);
                    }
                }
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(idfData));
            assert pages != null;
            double numPages = pages.length;
            for (String f : IDFs.keySet()) {
                double currentWord = 1.0 + IDFs.get(f);
                Double IDF = Math.log(numPages / currentWord)/Math.log(2);
                writer.write(f + "\n");
                writer.write(IDF + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
    private void findPageIncoming(File file) {
        HashMap<String, File> urlToFile = new HashMap<>();
        HashMap<String, ArrayList<String>> pageIncoming = new HashMap<>();
        File[] pages = file.listFiles();

        assert pages != null;
        for (File e: pages) {
            urlToFile.put(getURL(e), e);
        }
        for (File e: pages) {
            List<String> pageOutgoing = getPageOutgoing(e);
            String url = getURL(e);
            for (String f: pageOutgoing) {
                ArrayList<String> currentPage;
                if (pageIncoming.containsKey(f) && !pageIncoming.get(f).contains(url)) {
                    currentPage = pageIncoming.get(f);
                    currentPage.add(url);
                    pageIncoming.replace(f, currentPage);
                } else if (!pageIncoming.containsKey(f)) {
                    currentPage = new ArrayList<>();
                    currentPage.add(url);
                    pageIncoming.put(f, currentPage);
                }
            }
        }
        for (String e: urlToFile.keySet()) {
            ArrayList<String> incoming = pageIncoming.get(e);
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(urlToFile.get(e), true));
                writer.write("pageIncoming\n");
                for (String f: incoming) {
                    writer.write(f + "\n");
                }
                writer.write("/pageIncoming\n");
                writer.close();
            } catch (IOException g) {
                g.getStackTrace();
            }
        }
    }

    private class PageRankFinder {
        File data; //folder in which page data is contained, not a specific file
        double threshold;

        PageRankFinder(File initData, double initThreshold) {
            data= initData;
            threshold = initThreshold;
        }
        public void findPagesRank() {
            File[] pages = data.listFiles();
            int indexCounter = 0;
            assert pages != null;
            double[][] pageMatrix = new double[1][pages.length];
            List<String> pageIndex = new ArrayList<>();

            for (File e : pages) {
                pageIndex.add(getURL(e)); //Set an index in the 1 x N matrix to a crawled page
                indexCounter += 1;
            }
            indexCounter = 0;

            double[][] adjacency = makeAdjacency(data, pageIndex);
            Arrays.fill(pageMatrix[0], 1.0 / pages.length);

            double[][] finalValues =  convergeVector(pageMatrix, adjacency, threshold);

            for (File e : pages) {
                try {
                    PrintWriter writer = new PrintWriter(new FileWriter(e, true));
                    writer.write("PageRank\n");
                    writer.write( finalValues[0][indexCounter] + "\n");
                    indexCounter += 1;
                    writer.close();
                } catch (IOException f) {
                    f.getStackTrace();
                }
            }
        }
        private double[][] makeAdjacency(File data, List<String> pageIndex) {
            int indexCounter = 0;
            double alpha = 0.1;
            File[] pages = data.listFiles();
            assert pages != null;
            double[][] adjacency = new double[pages.length][pages.length];
            HashMap<String, List<String>> pageLinks = new HashMap<>();

            for (File e: pages) {
                String url = getURL(e);
                pageLinks.put(url, getPageOutgoing(e)); // map of outgoing links for each page
                indexCounter += 1;
            }
            indexCounter = 0;

            for (String e: pageIndex) { //creating "base" matrix of 1 and 0 before processing
                int outCounter = 0;
                List<String> pageOut = pageLinks.get(e);
                for (String f : pageIndex) {
                    if (pageOut.contains(f)) {
                        adjacency[indexCounter][outCounter] = 1.0;
                    } else {
                        adjacency[indexCounter][outCounter] = 0.0;
                    }
                    outCounter += 1;
                }
                indexCounter += 1;
            }
            for (int i = 0; i < adjacency.length; i++) {
                int oneCounter = 0;
                for (double e : adjacency[i]) {
                    if (e == 1.0) {
                        oneCounter += 1;
                    }
                }
                if (oneCounter > 0) {
                    for (int j = 0; j < adjacency[i].length; j++) {
                        adjacency[i][j] = adjacency[i][j] / oneCounter;
                    }
                } else {
                    Arrays.fill(adjacency[i], 1.0 / (double) oneCounter);
                }
            }
            MathUtils.scalarMult(adjacency, 1.0 - alpha);

            for (int i = 0; i < adjacency.length; i++) {
                for (int j = 0; j < adjacency.length; j++) {
                    adjacency[i][j] = adjacency[i][j] + alpha / adjacency.length;
                }
            }
            return adjacency;
        }
        private double[][] convergeVector (double [][] vector, double[][] matrix, double threshold) {
            double[][] current = vector; //tracking two iterations of matrix multiplication to determine how much change between the two
            double[][] next = MathUtils.matMult(vector, matrix);

            while (MathUtils.euclidDist(current, next) > threshold) {
                current = next;
                next = MathUtils.matMult(current, matrix);
            }
            return next;
        }
    }
}

