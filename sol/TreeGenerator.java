package sol;

import src.ITreeGenerator;
import src.ITreeNode;
import src.Row;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;

/**
 * A class that implements the ITreeGenerator interface used to generate a decision tree
 */
// TODO: Uncomment this once you've implemented the methods in the ITreeGenerator interface!
public class TreeGenerator implements ITreeGenerator<Dataset> {
    // TODO: document this field
    private ITreeNode root;
    private Dataset dataset;

    /**
     * Generate a TreeGenerator when not initially given a dataset
     */

    public TreeGenerator() {
        this.dataset = null;
    }

    /**
     * Construct a Tree Generator given a dataset
     * @param dataset the dataset we want to construct a tree for
     */
    public TreeGenerator(Dataset dataset) {
        this.dataset = dataset;
    }

    // TODO: Implement methods declared in ITreeGenerator interface!

    /**
     * Return the decision tree created for a set of training data a
     * @param trainingData the dataset we want to train our decision tree on
     * @param targetAttribute the attribute we want to train our prediction for
     * @return the decision tree for the dataset
     */
    public ITreeNode generateTreeHelper(Dataset trainingData, String targetAttribute) {
        List<String> attributeList = trainingData.getAttributeList();
        List<Dataset> partitionedData;
        HashMap<String, ITreeNode> children = new HashMap<>();
        String currentAttribute;

        // if we have an empty attribute list, calculate the default value
        if (attributeList.isEmpty())
            return new DecisionLeaf(trainingData.determineDefaultValue(targetAttribute));
        else {
            /*  if all the data in this dataset have the same value for the target attribute, we can
                return a decisionLeaf for that value */
            if (trainingData.sameAttributeValue(targetAttribute))
                return new DecisionLeaf(trainingData.getDataObjects().getFirst().getAttributeValue(targetAttribute));
            else {
                // get the current attribute we want to split on, and split the data
                currentAttribute = trainingData.getAttributeToSplitOn();
                partitionedData = trainingData.partitionByAttribute(currentAttribute);

                for (Dataset partitionedDataset : partitionedData) {
                    // make the recursive call
                    children.put(partitionedDataset.getDataObjects().getFirst().getAttributeValue(currentAttribute),
                            this.generateTreeHelper(partitionedDataset, targetAttribute));
                }

                return new AttributeNode(trainingData.determineDefaultValue(targetAttribute),
                        currentAttribute, children);
            }
        }
    }

    /**
     * Update the root attribute to point to the root of a decision tree constructed given
     * trainingData for the targetAttribute
     * @param trainingData the dataset we'll make a decision tree for
     * @param targetAttribute the attribute whose value we want to predict
     */
    @Override
    public void generateTree(Dataset trainingData, String targetAttribute) {
        // remove the targetAttribute from the attributeList
        if (this.dataset == null) this.dataset = trainingData;

        trainingData.getAttributeList().remove(targetAttribute);
        this.root = this.generateTreeHelper(trainingData, targetAttribute);
    }

    /**
     * Return the decision for an object
     * @param datum the data we want to get the decision on
     */
    @Override
    public String getDecision(Row datum) {
        return this.root.getDecision(datum);
    }
}
