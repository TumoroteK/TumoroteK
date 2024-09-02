package fr.aphp.tumorotek.manager.io.document;

public class LabelValue {
    private String label;
    private String value;
    private boolean labelInBold;
    private boolean valueInBold;


    public LabelValue(String label, String value, boolean labelInBold, boolean valueInBold) {
        this.label = label;
        this.value = value;
        this.labelInBold = labelInBold;
        this.valueInBold = valueInBold;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isLabelInBold() {
        return labelInBold;
    }

    public void setLabelInBold(boolean labelInBold) {
        this.labelInBold = labelInBold;
    }

    public boolean isValueInBold() {
        return valueInBold;
    }

    public void setValueInBold(boolean valueInBold) {
        this.valueInBold = valueInBold;
    }
}
