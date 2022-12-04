package SearchModule;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectTesterImp implements ProjectTester{ //implementation of search by URL, but base methods are in SearchModule.SearchData

    //SearchData object is able to return data on a page based on corresponding filename
    SearchData searcher;

    public ProjectTesterImp() { //Initializes a searcher object on creation, which can search for information based on file.
        searcher = new SearchData();
    }


    //getFileByURL method "converts" String format URL to File format file that searcher can read
    private File getFileByURL (String url) { //conversion of URL format to file format, returns null if no file exists
        return new File("src/PageData/" + PageUtils.makeURLTitle(url));
    }

    public void initialize() {
        File PageData = new File("src/PageData");
        File Data = new File("src/Data");
        PageUtils.wipeData(PageData);
        PageUtils.wipeData(Data);
    }

    //initializes a crawler object, wipe data from previous usage of method
    public void crawl(String seedURL) {
        Crawler crawler = new Crawler(seedURL);
        initialize();
        crawler.startCrawl();
    }


    public List<String> getOutgoingLinks(String url) {
        if (searcher.getPageOutgoing(getFileByURL(url)).size() > 0){
            return searcher.getPageOutgoing(getFileByURL(url));
        }
        return null;
    }


    public List<String> getIncomingLinks(String url) {
        if (searcher.getPageIncoming(getFileByURL(url)).size() > 0) {
            return searcher.getPageIncoming(getFileByURL(url));
        }
        return null;
    }


    public double getPageRank(String url) {return searcher.getFileRank(getFileByURL(url));}

    public double getIDF(String word) {return searcher.getFileIDF(word);}

    public double getTF(String url, String word) {
        return searcher.getFileTf(getFileByURL(url), word);
    }


    public double getTFIDF(String url, String word) {return searcher.getFileTfIdf(getFileByURL(url), word);}


    public List<SearchResult> search(String query, boolean boost, int X) {
        List<SearchResult> results = searcher.searchTerms(query, boost, X);
        results.sort(this::compareResults);
        return results;
    }

    private int compareResults (SearchResult o1, SearchResult o2) {
        double roundedSelfScore = (double)Math.round(o1.getScore() * 1000)/1000;
        double roundedCompareScore = (double)Math.round(o2.getScore() * 1000)/1000;

        if (roundedCompareScore == roundedSelfScore) {
            return o1.getTitle().compareTo(o2.getTitle());
        } else if (roundedSelfScore > roundedCompareScore) {
            return -1;
        }
        return 1;
    }
}
