package sol;

import org.junit.Assert;
import org.junit.Test;
import src.AttributeSelection;
import src.DecisionTreeCSVParser;
import src.ITreeNode;
import src.Row;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;

/**
 * A class to test basic decision tree functionality on a basic training dataset
 */
public class BasicDatasetTest {
    // IMPORTANT: for this filepath to work, make sure the project is open as the top-level directory in IntelliJ
    // (See the first yellow information box in the handout testing section for details)
    String trainingPath = "data/food.csv"; // TODO: replace with your own input file
    String targetAttribute = "eat?"; // TODO: replace with your own target attribute
    TreeGenerator testGenerator;
    Dataset training;

    /**
     * Constructs the decision tree for testing based on the input file and the target attribute.
     */
    @Before
    public void buildTreeForTest() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        this.training = new Dataset(attributeList, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        // builds a TreeGenerator object and generates a tree for "foodType"
        this.testGenerator = new TreeGenerator(this.training);
        // TODO: Uncomment this once you've implemented generateTree
        this.testGenerator.generateTree(this.training, this.targetAttribute);
    }

    /**
     * Tests the expected classification of the "tangerine" row is a fruit
     */
    @Test
    public void testClassification() {
        // makes a new (partial) Row representing the tangerine from the example
        // TODO: make your own rows based on your dataset
        Row cookie = new Row("test row (cookie)");
        cookie.setAttributeValue("protein", "low");
        cookie.setAttributeValue("sweet", "yes");
        // TODO: make your own assertions based on the expected classifications

        Assert.assertEquals("no", this.testGenerator.getDecision(cookie));
    }

    /**
     * Test for Dataset constructor, correct number of rows in food csv
     */
    @Test
    public void testDataset1() {
        Assert.assertEquals(5, this.training.size());
    }

    /**
     * Test for Dataset constructor, correct number of rows in cities csv
     */
    @Test
    public void testDataset2() {
        this.trainingPath = "data/cities.csv";
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        this.training = new Dataset(attributeList, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        Assert.assertEquals(5, this.training.size());
    }

    /**
     * Test whether the partitionByAttribute method works
     */
    @Test
    public void testPartitionByValue() {
        List<Dataset> partitionedByProtein = this.training.partitionByAttribute("protein");
        Assert.assertEquals(partitionedByProtein.stream().map((Dataset::size)).toList(), Arrays.asList(3, 2));
    }

    /**
     * Test whether the partitionByAttribute method works
     */
    @Test
    public void testPartitionByValue2() {
        List<Dataset> partitionedByProtein = this.training.partitionByAttribute("sweet");
        Assert.assertEquals(partitionedByProtein.stream().map((Dataset::size)).toList(), Arrays.asList(3, 2));
    }

    /**
     * Test whether the sameAttributeValue method works (true case)
     */
    @Test
    public void testSameAttributeValueTrue() {
        List<Dataset> partitionedByProtein = this.training.partitionByAttribute("protein");
        Assert.assertTrue(partitionedByProtein.getFirst().sameAttributeValue("eat?"));
    }

    /**
     * Test whether the sameAttributeValue method works (false case)
     */
    @Test
    public void testSameAttributeValue() {
        List<Dataset> partitionedByProtein = this.training.partitionByAttribute("sweet");
        Assert.assertFalse(partitionedByProtein.getFirst().sameAttributeValue("eat?"));
    }

    /**
     * Test whether the determineDefaultValue method works, sweet attribute
     */
    @Test
    public void testDetermineDefaultValue1() {
        String defaultRoot = this.training.determineDefaultValue("sweet");
        Assert.assertEquals("no", defaultRoot);
    }

    /**
     * Test whether the determineDefaultValue method works, protein attribute
     */
    @Test
    public void testDetermineDefaultValue2() {
        String defaultRoot = this.training.determineDefaultValue("protein");
        Assert.assertEquals("high", defaultRoot);
    }

    /**
     * Construct a simple decision tree for our food dataset to test the methods in DecisionLeaf and
     * AttributeNode
     */
    @Test
    public void testITreeNodes() {
        // create the decision tree
        DecisionLeaf lowProtein = new DecisionLeaf("no");
        DecisionLeaf highProtein = new DecisionLeaf("yes");
        HashMap<String, ITreeNode> proteinDecisions = new HashMap<>(2);
        proteinDecisions.put("high", highProtein);
        proteinDecisions.put("low", lowProtein);
        AttributeNode proteinSol = new AttributeNode("yes", "protein", proteinDecisions);

        // get decisions for our simple dataset
        Assert.assertEquals(this.training.getDataObjects().stream().map(proteinSol::getDecision).toList(),
                Arrays.asList("yes", "no", "no", "yes", "yes"));
    }
}
