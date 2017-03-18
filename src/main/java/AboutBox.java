import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AboutBox extends JDialog {
    public AboutBox(Frame owner) {
        super(owner, "JCSVEditor", true);

        JLabel lbl = new JLabel(new ImageIcon("../graph/icon.png"));
        JPanel p = new JPanel();
        Border b1 = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        Border b2 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        lbl.setBorder(BorderFactory.createCompoundBorder(b1, b2));
        p.add(lbl);
        getContentPane().add(p, BorderLayout.WEST);

        String message = "JCSVEditor\n"
                + "Copyright (c) Jason Liu, 2012";
        JTextArea txt = new JTextArea(message);
        txt.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        txt.setFont(new Font("Helvetica", Font.BOLD, 12));
        txt.setEditable(false);
        txt.setBackground(getBackground());
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(txt);

        message = "This program can edit,save\n" + " txt file and csv file";
        txt = new JTextArea(message);
        txt.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        txt.setFont(new Font("Arial", Font.PLAIN, 12));
        txt.setEditable(false);
        txt.setBackground(getBackground());
        p.add(txt);

        getContentPane().add(p, BorderLayout.CENTER);

        JButton btOK = new JButton("OK");
        ActionListener lst = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        };
        btOK.addActionListener(lst);
        p = new JPanel();
        p.add(btOK);
        getContentPane().add(p, BorderLayout.SOUTH);

        pack();
        setResizable(false);
    }

}
