import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FormatActionListener implements ActionListener {

    Notepad notepad;
    // Variables to hold font attributes
    String fontName;
    int fontSize, fontStyle;
    boolean isBold, isItalic;

    public FormatActionListener(Notepad notepad) {
        this.notepad = notepad;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // Get current font attributes from the text area
        fontName = notepad.textarea.getFont().getName();
        fontSize = notepad.textarea.getFont().getSize();
        fontStyle = notepad.textarea.getFont().getStyle();
        isBold = notepad.bold.getState(); // Check if bold is selected
        isItalic = notepad.italic.getState(); // Check if italic is selected

        String[] commands = event.getActionCommand().split("-");
        String action = commands[0],
                value = commands[1];
        // value = String.join(" ", Arrays.copyOfRange(commands, 1, commands.length));

        switch (action) {
            case "fstyle":
                if (value.equals("Bold") && isBold)
                    notepad.textarea.setFont(
                            new Font(fontName, fontStyle + Font.BOLD, fontSize)); // Add bold style
                if (value.equals("Bold") && !isBold)
                    notepad.textarea.setFont(
                            new Font(fontName, fontStyle - Font.BOLD, fontSize)); // Remove bold style
                if (value.equals("Italic") && isItalic)
                    notepad.textarea.setFont(
                            new Font(fontName, fontStyle + Font.ITALIC, fontSize)); // Add italic style
                if (value.equals("Italic") && !isItalic)
                    notepad.textarea.setFont(
                            new Font(fontName, fontStyle - Font.ITALIC, fontSize)); // Remove italic
                                                                                    // style
                break;

            case "ffam":
                String font = pickFont();
                if (font != null) {
                    notepad.textarea.setFont(
                            new Font(
                                    font, // Set new font name
                                    fontStyle, // Retain current style
                                    fontSize // Retain current size
                            ));
                }
                break;

            case "fsize":
                int size = Integer.parseInt(value);
                notepad.textarea.setFont(
                        new Font(
                                fontName, // Retain current font name
                                fontStyle, // Retain current style
                                size // Set new font size
                        ));
                break;

            case "fcolorfg":
                // Check if the value is a digit, indicating a pre-defined color
                if (Character.isDigit(value.charAt(0))) {
                    int index = Integer.parseInt(value);
                    // Set the foreground and caret color based on the pre-defined color
                    notepad.textarea.setForeground(notepad.awtColors[index]);
                    notepad.textarea.setCaretColor(notepad.awtColors[index]);
                } else {
                    // Open color chooser dialog for custom color selection
                    Color color = JColorChooser.showDialog(
                            notepad, "Select foreground color", notepad.textarea.getForeground());
                    if (color != null) {
                        // Set the selected color as the foreground and caret color
                        notepad.textarea.setForeground(color);
                        notepad.textarea.setCaretColor(color);
                        // Clear the selection in the foreground color group
                        notepad.colorFgGroup.clearSelection();
                    }
                }
                break;

            case "fcolorbg":
                if (Character.isDigit(value.charAt(0))) {
                    int index = Integer.parseInt(value);
                    notepad.textarea.setBackground(notepad.awtColors[index]);
                } else {
                    Color color = JColorChooser.showDialog(
                            notepad, "Select background color", notepad.textarea.getBackground());
                    if (color != null) {
                        notepad.textarea.setBackground(color);
                        notepad.colorBgGroup.clearSelection();
                    }
                }
                break;
        }
    }

    // Method to pick a font
    private String pickFont() {
        // Create a panel with a border layout
        JPanel panel = new JPanel(new BorderLayout(0, 4));

        // Create a list of available fonts
        JList<String> fontList = new JList<>(notepad.fonts);
        fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontList.setVisibleRowCount(12);

        // Add the font list to a scroll pane
        JScrollPane scrollPane = new JScrollPane(fontList);

        // Create a label to display a sample of the selected font
        JLabel sample = new JLabel("Sample Text");
        sample.setHorizontalAlignment(SwingConstants.CENTER);
        sample.setPreferredSize(new Dimension(sample.getPreferredSize().width, 32));
        sample.setOpaque(true);
        sample.setForeground(Color.BLACK);
        sample.setBackground(Color.LIGHT_GRAY);
        sample.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        // Add a list selection listener to update the sample text font
        fontList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                sample.setFont(new Font(fontList.getSelectedValue(), Font.PLAIN, 16));
            }
        });

        // Add the scroll pane and sample label to the panel
        panel.add(scrollPane);
        panel.add(sample, BorderLayout.SOUTH);

        // Set the initially selected font
        fontList.setSelectedValue(fontName, true);

        // Show a confirm dialog to allow the user to pick a font
        int option = JOptionPane.showConfirmDialog(
                notepad,
                panel,
                "Select a Font",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        // Return null if the user cancels the dialog
        if (option != JOptionPane.OK_OPTION)
            return null;

        // Return the selected font
        return fontList.getSelectedValue();
    }
}
