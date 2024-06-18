import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class HelpActionListener implements ActionListener {

    Notepad notepad;

    public HelpActionListener(Notepad notepad) {
        this.notepad = notepad;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // List of group member names to display in the About dialog
        String[] names = {
                "Lisanegebriel Abay    UGR/5516/15",
                "Rediet Berhanu        UGR/6734/15",
                "Yealem Birhanu        UGR/9954/15",
                "Yohannes Tsegaye      UGR/5342/15",
                "Yonatan Million       UGR/6586/15",
        };

        // Create a modal dialog for the About window
        JDialog about = new JDialog(notepad, "About", true);
        about.setResizable(false); // Prevent resizing
        about.setAlwaysOnTop(true); // Keep the dialog always on top
        about.setMinimumSize(new Dimension(380, 150)); // Set minimum size
        about.setLocationRelativeTo(notepad); // Center the dialog relative to the notepad

        // Create a container panel with BorderLayout and add padding
        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Create and configure the title label
        JLabel title = new JLabel("Notepad made by: ");
        title.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        title.setForeground(Color.BLACK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // Create a panel for the names and set layout
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        for (String name : names) {
            JLabel label = new JLabel(name);
            label.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the label
            label.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
            label.setForeground(Color.BLACK);
            namePanel.add(label); // Add the label to the name panel
        }

        // Add the title and name panels to the container
        container.add(title, BorderLayout.NORTH);
        container.add(namePanel, BorderLayout.CENTER);
        about.add(container); // Add the container to the dialog
        about.pack(); // Adjust the dialog size to fit the content
        about.validate(); // Validate the dialog
        about.setVisible(true); // Make the dialog visible
        about.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // Set the close operation
    }
}
