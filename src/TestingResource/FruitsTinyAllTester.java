package TestingResource;
import SearchModule.ProjectTesterImp;
import SearchModule.ProjectTester;

public class FruitsTinyAllTester {
    public static void main(String[] args) throws Exception {
        ProjectTester tester = new ProjectTesterImp(); //Instantiate your own SearchModule.ProjectTester instance here
        tester.initialize();
        tester.crawl("https://people.scs.carleton.ca/~davidmckenney/tinyfruits/N-0.html");

        TestingResource.FruitsTinyOutgoingLinksTester.runTest(tester);
        TestingResource.FruitsTinyIncomingLinksTester.runTest(tester);
        TestingResource.FruitsTinyPageRanksTester.runTest(tester);
        TestingResource.FruitsTinyIDFTester.runTest(tester);
        TestingResource.FruitsTinyTFTester.runTest(tester);
        TestingResource.FruitsTinyTFIDFTester.runTest(tester);
        TestingResource.FruitsTinySearchTester.runTest(tester);
        System.out.println("Finished running all tests.");
    }
}
