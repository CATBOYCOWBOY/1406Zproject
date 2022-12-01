package SearchModule;

public class Result implements SearchResult{
    private String title;
    private String url;
    private double score;

    Result (String initTitle, String initUrl, double initScore) {
        url = initUrl;
        title = initTitle;
        score = initScore;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getUrl() {return url; }

    @Override
    public double getScore() {
        return score;
    }
}
