package sol;

import java.util.List;
import src.ITreeNode;
import java.util.HashMap;
import java.util.Map;

import src.Row;

/**
 * A class representing an inner node in the decision tree.
 */
// TODO: Uncomment this once you've implemented the methods in the ITreeNode interface!
public class AttributeNode implements ITreeNode {
    private String defaultValue;
    private String attribute;
    private HashMap<String, ITreeNode> children;

    /**
     * Construct an AttributeNode given a default value, current attribute, and children
     * @param attribute the current attribute
     * @param children the children
     */
    public AttributeNode(String defaultValue, String attribute,
                         HashMap<String, ITreeNode> children) {
        this.defaultValue = defaultValue;
        this.attribute = attribute;
        this.children = children;
    }

    /** Return the attribute value this DecisionLeaf corresponds to
     * @return the string representation of the attribute value this DecisionLeaf corresponds to
     */
    public String getAttribute() {
        return this.attribute;
    }

    /**
     * Add a child to the current AttributeNode
     * @param attributeValue the attribute value that the child corresponds to
     * @param child a pointer to the child ITreeNode
     */
    public void addChild(String attributeValue, ITreeNode child) {
        this.children.put(attributeValue, child);
    }

    /**
     * Return the decision for a piece of data
     * @param forDatum the data to return the decision for
     * @return a string representation of the decision
     */
    @Override
    public String getDecision(Row forDatum) {
        if (forDatum.getAttributes().isEmpty()) return this.defaultValue;

        for (Map.Entry<String, ITreeNode> child : this.children.entrySet()) {
            if(child.getKey().equals(forDatum.getAttributeValue(this.attribute))) {
                return child.getValue().getDecision(forDatum);
            }
        }

        return this.defaultValue;
    }
}
