import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewActionListener implements ActionListener {

    Notepad notepad;
    // Variables to store the status of word wrap and line wrap
    boolean wWrap, lWrap, sDetails;

    public ViewActionListener(Notepad notepad) {
        this.notepad = notepad;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        switch (event.getActionCommand()) {
            case "Word Wrap":
                wWrap = notepad.wordWrap.isSelected(); // Check if word wrap is selected
                if (wWrap)
                    notepad.textarea.setLineWrap(true); // Enable word wrap
                else
                    notepad.textarea.setLineWrap(false); // Disable word wrap
                break;

            case "Line Wrap":
                lWrap = notepad.lineWrap.isSelected(); // Check if line wrap is selected
                if (lWrap)
                    notepad.textarea.setWrapStyleWord(true); // Enable line wrap
                else
                    notepad.textarea.setWrapStyleWord(false); // Disable line wrap
                break;

            case "Show Details":
                sDetails = notepad.showDetails.isSelected(); // Check if line wrap is selected
                if (sDetails)
                    notepad.detail.setVisible(true); // Enable line wrap
                else
                    notepad.detail.setVisible(false); // Disable line wrap
                break;

            default:
                break;
        }
    }
}
