package fr.aphp.tumorotek.manager.io.document;

public class DocumentFooter {
    private String leftData;
    private String centerData;
    private String rightData;

    public DocumentFooter(String leftData, String centerData, String rightData) {
        this.leftData = leftData;
        this.centerData = centerData;
        this.rightData = rightData;
    }


    public String getLeftData() {
        return leftData;
    }

    public void setLeftData(String leftData) {
        this.leftData = leftData;
    }

    public String getCenterData() {
        return centerData;
    }

    public void setCenterData(String centerData) {
        this.centerData = centerData;
    }

    public String getRightData() {
        return rightData;
    }

    public void setRightData(String rightData) {
        this.rightData = rightData;
    }
}
