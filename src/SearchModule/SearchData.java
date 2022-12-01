package SearchModule;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SearchData extends PageUtils { //Search methods by File object, not URL
    public SearchData () {}
    public List<String> getPageOutgoing(File file) {
        String[] lines = fileRead(file);
        List<String> output = new ArrayList<>();
        boolean tag = false;

        for (String e : lines) {
            if (Objects.equals(e, "pageOutgoing")) {
                tag = true;
            } else if (tag) {
                if (Objects.equals(e, "/pageOutgoing")) {
                    break;
                } else {
                    output.add(e);
                }
            }
        }
        return output;
    }

    public List<String> getPageIncoming(File file) {
        String[] lines = fileRead(file);
        List<String> output = new ArrayList<>();
        boolean tag = false;

        for (String e : lines) {
            if (Objects.equals(e, "pageIncoming")) {
                tag = true;
            } else if (tag) {
                if (Objects.equals(e, "/pageIncoming")) {
                    break;
                } else {
                    output.add(e);
                }
            }
        }
        return output;
    }
    public double getFileRank (File file) {
        String[] lines = fileRead(file);
        for (int i = 0; i < lines.length; i++) {
            if (Objects.equals(lines[i], "PageRank")) {
                return Double.parseDouble(lines[i+1]);
            }
        }
        return -1;
    }
    public String getURL (File file) {
        String[] lines = fileRead(file);
        String url = "";

        for (int i = 0; i < lines.length; i++) {
            if (Objects.equals(lines[i], "url")) {
                url = lines[i + 1];
                break;
            }
        }
        return url;
    }
    public String getTitle (File file) {
        String[] lines = fileRead(file);
        for (int i = 0; i< lines.length; i++) {
            if (Objects.equals(lines[i], "title")) {
                return lines[i + 1];
            }
        }
        return null;
    }
    public double getFileTf (File file, String word) {
        String[] lines = fileRead(file);

        for (int i = 0; i< lines.length; i++) {
            if (Objects.equals(word, lines[i])){
                return Double.parseDouble(lines[i+1]);
            }
        }
        return 0;
    }

    public double getFileIDF (String word) {
        File file = new File("src/Data/idfData.txt");
        String[] lines = fileRead(file);

        for (int i = 0; i < lines.length; i++) {
            if (Objects.equals(lines[i], word)) {
                return Double.parseDouble(lines[i + 1]);
            }
        }
        return 0;
    }

    protected double getFileTfIdf (File file, String word) {
        return Math.log(1 + getFileTf(file, word))/Math.log(2) * getFileIDF(word);
    }

    public List<SearchResult> searchTerms(String query, boolean boost, int X) { //returns unsorted list of best X pages
        HashMap<String, Integer> queryWords = getWords(query);
        double[] queryVector = convertQueryVector(queryWords, query.split(" ").length);
        List<SearchResult> bestResults = new ArrayList<>();
        File pages = new File("src/PageData");
        File[] allPages = pages.listFiles();
        assert allPages != null;

        for (File e : allPages) {
            double[] pageVector = convertPageVector(queryWords, e);
            double score = getCosineScore(queryVector, pageVector, e, boost);
            if (bestResults.size() < X) {
                bestResults.add(new Result(getTitle(e), getURL(e), score) {
                });
            } else {
                double worstScore = 2.0;
                int weakestResult = 0;
                for (int i = 0; i < bestResults.size(); i++) {
                    if (bestResults.get(i).getScore() < worstScore) {
                        worstScore = bestResults.get(i).getScore();
                        weakestResult = i;
                    }
                }
                if (score > worstScore) {
                    bestResults.set(weakestResult, new Result(getTitle(e), getURL(e), score) {
                    });
                }
            }
        }
        return bestResults;
    }

    private double[] convertQueryVector (HashMap<String, Integer> queryWords, int queryLength) {
        int i = 0;
        double[] queryVector = new double[queryWords.keySet().size()];
        for (String e : queryWords.keySet()) {
            double termTFLog = Math.log(1 + ((double) queryWords.get(e) / (double) queryLength)) / Math.log(2);
            queryVector[i] =  termTFLog * getFileIDF(e);
            i += 1;
        }
        return queryVector;
    }

    private double[] convertPageVector (HashMap<String, Integer> queryWords, File page) {
        double[] pageVector = new double[queryWords.keySet().size()];
        int i = 0;
        for (String e : queryWords.keySet()) {
            pageVector[i] = getFileTfIdf(page, e);
            i += 1;
        }
        return pageVector;
    }

    private double getCosineScore (double[] queryVector, double[] pageVector, File e, boolean boost) {
        double queryMagnitude = MathUtils.vectorMagnitude(queryVector);
        double pageMagnitude = MathUtils.vectorMagnitude(pageVector);
        if (queryMagnitude * pageMagnitude == 0) {
            return 0;
        }
        if (boost) {
            return (MathUtils.dotProduct(queryVector, pageVector) / (queryMagnitude * pageMagnitude)) * getFileRank(e);
        }
        return MathUtils.dotProduct(queryVector, pageVector) / (queryMagnitude * pageMagnitude);
    }

    private HashMap<String, Integer> getWords (String query) {
        String[] queryWords = query.split(" ");
        HashMap<String, Integer> countedWords = new HashMap<>();

        for (String e: queryWords) {
            if (countedWords.containsKey(e)) {
                countedWords.put(e, countedWords.get(e) + 1);
            } else {
                countedWords.put(e, 1);
            }
        }
        return countedWords;
    }
}
