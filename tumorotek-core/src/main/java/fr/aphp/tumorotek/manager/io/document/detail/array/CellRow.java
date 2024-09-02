package fr.aphp.tumorotek.manager.io.document.detail.array;

import java.util.ArrayList;
import java.util.List;

public class CellRow {
    private List<DataCell> listDataCell;

    public CellRow() {
        this.listDataCell = new ArrayList<>();
    }

    // Method to add a DataCell to the list
    public void addDataCell(DataCell dataCell) {
        this.listDataCell.add(dataCell);
    }

    // Getter for listDataCell (optional, but useful)
    public List<DataCell> getListDataCell() {
        return this.listDataCell;
    }


}
