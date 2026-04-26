package sol;

import java.util.*;

import src.AttributeSelection;
import src.IDataset;
import src.Row;

/**
 * A class representing a training dataset for the decision tree
 */
public class Dataset implements IDataset {
    private List<String> attributeList;
    private List<Row> dataObjects;
    private AttributeSelection selectionType;

    /**
     * Constructor for a Dataset object using all parameters
     * @param attributeList - a list of attributes
     * @param dataObjects -  a list of rows
     * @param selectionType - an enum for which way to select attributes
     */
    public Dataset(List<String> attributeList, List<Row> dataObjects, AttributeSelection selectionType) {
        this.attributeList = attributeList;
        this.dataObjects = dataObjects;
        this.selectionType = selectionType;
    }

    /**
     * Getter method for the list of attributes
     * @return the list of attributes
     */
    @Override
    public List<String> getAttributeList() {
        return this.attributeList;
    }

    /**
     * Getter method for the list of data objects
     * @return the list of data objects
     */
    @Override
    public List<Row> getDataObjects() {
        return this.dataObjects;
    }

    /**
     * @param newRow the row to add
     * add a data element to the dataset
     */
    public void addRow(Row newRow) {
        this.dataObjects.add(newRow);
    }

    /**
     * Getter method for the order in which we'll return attributes
     * @return the AttributeSelection indicating the order in which we'll return attributes
     */
    @Override
    public AttributeSelection getSelectionType() {
        return this.selectionType;
    }

    /**
     * Return the number of data objects in the list
     */
    @Override
    public int size() {
        return this.dataObjects.size();
    }

    /**
     * @return the string representing the next attribute we should split on
     */
    public String getAttributeToSplitOn() {
        if (this.selectionType == AttributeSelection.ASCENDING_ALPHABETICAL) {
            return this.attributeList.stream().sorted().toList().get(0);
        }
        if (this.selectionType == AttributeSelection.DESCENDING_ALPHABETICAL) {
            return this.attributeList.stream().sorted().toList().get(this.attributeList.size() - 1);
        }
        if (this.selectionType == AttributeSelection.RANDOM) {
            Random randomGenerator = new Random();
            int randomIndex = randomGenerator.nextInt(this.attributeList.size());

            return this.attributeList.stream().toList().get(randomIndex);
        }

        throw new RuntimeException("Non-Exhaustive Selection Type");
    }

    /**
     * @param attributeName the attribute we want to partition the dataset by
     * @return a list of datasets partitioned by each datum's value for the specified attribute
     */
    public List<Dataset> partitionByAttribute(String attributeName) {
        // attribute values will be the keys, the partitioned datasets will be the values
        HashMap<String, Dataset> partitionedDatasets = new HashMap<>();

        // create a deep copy
        List<String> reducedAttributeList = new ArrayList<>(this.attributeList);
        reducedAttributeList.remove(attributeName);

        for (Row datum : this.dataObjects) {
            String datumAttributeValue = datum.getAttributeValue(attributeName);

            if(!partitionedDatasets.containsKey(datumAttributeValue))
                partitionedDatasets.put(datumAttributeValue,
                        new Dataset(reducedAttributeList, new ArrayList<Row>(), this.selectionType));

            partitionedDatasets.get(datumAttributeValue).addRow(datum);
        }

        // List of Datasets corresponding to each value of the attribute,
        // order depends on which attribute value showed up first in the rows
        return partitionedDatasets.values().stream().toList();
    }

    /**
     * @param attributeName the attribute to check whether all values in the dataset have the same value for
     * Return whether the data in the Dataset has the same value for a specified attribute
     * @return a boolean indicating whether all data in the dataset has the same value for a given attribute
     */
    public boolean sameAttributeValue(String attributeName) {
        String firstValue = this.dataObjects.getFirst().getAttributeValue(attributeName);

        for (Row row : this.dataObjects)
            if (!row.getAttributeValue(attributeName).equals(firstValue)) return false;


        return true;
    }

    /**
     * Determines the default value for a node in the decision tree
     * @param attributeName - the attribute of which we want teh default value
     * @return the default value of the attribute at the current node
     */
    public String determineDefaultValue(String attributeName) {
        HashMap<String, Integer> attributeValueFreq = new HashMap<>();

        for (Row datum : this.dataObjects) {
            String datumAttributeValue = datum.getAttributeValue(attributeName);

            if(!attributeValueFreq.containsKey(datumAttributeValue))
                attributeValueFreq.put(datumAttributeValue, 0);

            attributeValueFreq.put(datumAttributeValue,
                    attributeValueFreq.get(datumAttributeValue) + 1);
        }

        int maxOccurences = 0;
        String defaultAttributeVal = null;
        for (Map.Entry<String, Integer> attribute : attributeValueFreq.entrySet()) {
            if (attribute.getValue() > maxOccurences)  {
                maxOccurences = attribute.getValue();
                defaultAttributeVal = attribute.getKey();
            }
        }

        return defaultAttributeVal;
    }
}