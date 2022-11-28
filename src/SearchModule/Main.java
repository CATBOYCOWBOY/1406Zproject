package SearchModule;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ProjectTesterImp projectTesterImp = new ProjectTesterImp();
        double[] vector2 = {0.031595, 0.0569419, 0.0744702, 0.031595};
        double[] vector1 = {0.13535, 0.08467, 0.15627, 0.13535};

        System.out.println(MathUtils.vectorMagnitude(vector1));
        System.out.println(MathUtils.vectorMagnitude(vector2));
        System.out.println(MathUtils.dotProduct(vector1, vector2));

        double SearchValueN0 = MathUtils.dotProduct(vector1, vector2) / (MathUtils.vectorMagnitude(vector1) * MathUtils.vectorMagnitude(vector2));
        double SearchValueWithRank = projectTesterImp.getPageRank("https://people.scs.carleton.ca/~davidmckenney/tinyfruits/N-0.html") * SearchValueN0;
        System.out.println(SearchValueN0);
        System.out.println(SearchValueWithRank);
        System.out.println("\n\n");

        System.out.println(projectTesterImp.getIDF("papaya"));

        System.out.println("\n\n");

        ArrayList<SearchResult> result = (ArrayList<SearchResult>) projectTesterImp.search("peach coconut apple apple papaya", true, 10);
        for (SearchResult e : result) {
            System.out.println(e.getTitle());
            System.out.println(e.getScore());
        }
    }
}