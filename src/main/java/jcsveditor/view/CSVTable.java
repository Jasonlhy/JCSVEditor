package jcsveditor.view;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Created by jason on 3/6/2017.
 */

class TextAreaEditor extends DefaultCellEditor {
    protected JScrollPane scrollpane;
    protected JTextArea textarea;

    public TextAreaEditor() {
        super(new JCheckBox());
        scrollpane = new JScrollPane();
        textarea = new JTextArea();
        scrollpane.getViewport().add(textarea);
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        textarea.setText((String) value);

        return scrollpane;
    }

    public Object getCellEditorValue() {
        return textarea.getText();
    }
}


class TextAreaRenderer extends JScrollPane implements TableCellRenderer
{
    JTextArea textarea;

    public TextAreaRenderer() {
        textarea = new JTextArea();
        getViewport().add(textarea);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.setBorder(BorderFactory.createEmptyBorder());
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column)
    {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
            textarea.setForeground(table.getSelectionForeground());
            textarea.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
            textarea.setForeground(table.getForeground());
            textarea.setBackground(table.getBackground());
        }

        textarea.setText((String) value);
        textarea.setCaretPosition(0);

        return this;
    }
}

public class CSVTable extends JTable {
    public CSVTable(TableModel tableModel){
        super(tableModel);

        this.setDefaultRenderer(String.class, new TextAreaRenderer());
        this.setDefaultEditor(String.class, new TextAreaEditor());

        this.setRowHeight(40);
        this.setRowSelectionAllowed(true);
        this.setColumnSelectionAllowed(false);

        this.setDragEnabled(false);

        // the original font is Dialog which do not supports UTF-8
        // assume system sans_serif supports the file encoding such as UTF-8
        this.setFont(new Font("SANS_SERIF", Font.PLAIN, 12));

        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
}
