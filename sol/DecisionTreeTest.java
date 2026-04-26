package sol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertFalse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import src.AttributeSelection;
import src.DecisionTreeCSVParser;
import src.ITreeNode;
import src.Row;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A class containing the tests for methods in the TreeGenerator and Dataset classes
 */
public class DecisionTreeTest {
    /*  TODO: Write more unit and system tests! Some basic guidelines that we will be looking for:
        1. Small unit tests on the Dataset class testing the IDataset methods (DONE)
        2. Small unit tests on the TreeGenerator class that test the ITreeGenerator methods (DONE)
        3. Tests on your own small dataset (expect 70% accuracy on testing data, 95% on training data) (Add more tests using food dataset)
        4. Test on the villains dataset (expect 70% accuracy on testing data, 95% on training data)
        5. Tests on the mushrooms dataset (expect 70% accuracy on testing data, 95% on training data)
        Feel free to write more unit tests for your own helper methods -- more details can be found in the handout! */

    String trainingPath = "data/food.csv";
    String targetAttribute = "eat?";
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
     * This test shows syntax for a basic assertEquals assertion -- can be deleted
     */
    @Test
    public void testAssertEqual() {
        assertEquals(2, 1 + 1);
    }

    /**
     * This test shows syntax for a basic assertTrue assertion -- can be deleted
     */
    @Test
    public void testAssertTrue() {
        assertTrue(true);
    }

    /**
     * This test shows syntax for a basic assertFalse assertion -- can be deleted
     */
    @Test
    public void testAssertFalse() {
        assertFalse(false);
    }

    /**
     * Tests the expected classification of if we will eat a cookie (low protein -> no)
     * (negative classification)
     */
    @Test
    public void testClassificationNegative() {
        // makes a new (partial) Row representing the tangerine from the example
        // TODO: make your own rows based on your dataset
        Row cookie = new Row("test row (cookie)");
        cookie.setAttributeValue("protein", "low");
        cookie.setAttributeValue("sweet", "yes");
        // TODO: make your own assertions based on the expected classifications

        Assert.assertEquals("no", this.testGenerator.getDecision(cookie));
    }

    /**
     * Tests the expected classification of if we will eat a steak (high protein -> yes)
     * (positive classification)
     */
    @Test
    public void testClassificationPositive() {
        // makes a new (partial) Row representing the tangerine from the example
        // TODO: make your own rows based on your dataset
        Row cookie = new Row("test row (steak)");
        cookie.setAttributeValue("protein", "high");
        cookie.setAttributeValue("sweet", "no");
        // TODO: make your own assertions based on the expected classifications

        Assert.assertEquals("yes", this.testGenerator.getDecision(cookie));
    }

    /**
     * Tests the expected classification of if we will eat a bread (medium protein -> yes*)
     * * indicates because of default value
     */
    @Test
    public void testClassificationDefault() {
        // makes a new (partial) Row representing the tangerine from the example
        // TODO: make your own rows based on your dataset
        Row cookie = new Row("test row (bread)");
        cookie.setAttributeValue("protein", "medium");
        cookie.setAttributeValue("sweet", "no");
        // TODO: make your own assertions based on the expected classifications

        Assert.assertEquals("yes", this.testGenerator.getDecision(cookie));
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
