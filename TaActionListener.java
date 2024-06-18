import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

class TaActionListener implements KeyListener, CaretListener {

    Notepad notepad;
    // Variables to store word count, character count, and line number
    int wCount, cCount, lineNum;

    public TaActionListener(Notepad notepad) {
        this.notepad = notepad;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        // No action needed on key press
    }

    @Override
    public void keyReleased(KeyEvent event) {
        // Update character count
        cCount = notepad.textarea.getText().length();
        if (notepad.textarea.getText().strip().isEmpty())
            // Don't count empty space as a word
            wCount = 0;
        else
            // Update word count (strip to remove leading/trailing spaces and split to count
            // words)
            wCount = notepad.textarea.getText().strip().split(" ").length;
        // Update the character count label
        notepad.chars.setText("Characters: " + cCount);
        // Update the word count label
        notepad.words.setText("Words: " + wCount);
    }

    @Override
    public void keyTyped(KeyEvent event) {
        // No action needed on key typed
    }

    @Override
    public void caretUpdate(CaretEvent arg0) {
        // Get the current caret position
        int caretPos = notepad.textarea.getCaretPosition();
        try {
            // Calculate the current line number based on the caret position
            lineNum = notepad.textarea.getLineOfOffset(caretPos) + 1;
        } catch (BadLocationException e) {
            System.err.println("Error while counting line number!");
        }
        // Update the line number label
        notepad.line.setText("Line: " + lineNum);
    }
}
