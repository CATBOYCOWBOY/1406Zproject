package SearchModule;

public class Result implements SearchResult{
    private String title;
    private double score;

    Result (String initTitle, double initScore) {
        title = initTitle;
        score = initScore;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public double getScore() {
        return score;
    }
}
