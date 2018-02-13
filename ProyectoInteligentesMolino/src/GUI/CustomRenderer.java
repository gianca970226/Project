package GUI;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;


class CustomRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 6703872492730589499L;
    private static Integer original [][];
    
    public CustomRenderer(Integer matriz [][]){
        this.original=matriz;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        if (this.original[row][column]==1) {
            cellComponent.setBackground(Color.BLACK);
        } else if(this.original[row][column]==2){
            cellComponent.setBackground(Color.BLUE);
        }else{
            cellComponent.setBackground(Color.WHITE);
        }

        return cellComponent;
    }
}
