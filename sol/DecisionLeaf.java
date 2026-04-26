package sol;

import src.ITreeNode;
import src.Row;

/**
 * A class representing a leaf in the decision tree.
 */
public class DecisionLeaf implements ITreeNode {
    private String decision;

    /** Construct a DecisionLeaf given a string representation of the decision to return
     * @param decision string representation of the attribute to return
     */
    public DecisionLeaf(String decision) {
        this.decision = decision;
    }

    /**
     * Return the string representing the decision stored at this decision leaf
     */
    @Override
    public String getDecision(Row forDatum) { return this.decision; }
}
