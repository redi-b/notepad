import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;

public class Notepad extends JFrame {

    // GUI components
    JTextArea textarea;
    JPanel detail;
    JLabel chars, words, line;
    JMenuBar menuBar;
    JMenu fileMenu, editMenu, viewMenu, formatMenu, helpMenu;
    JMenuItem newFile, open, save, saveAs, print, close;
    JMenuItem undo, redo, cut, copy, paste, delete, selectAll, timeDate;
    JCheckBoxMenuItem wordWrap, lineWrap, showDetails;
    JCheckBoxMenuItem bold, italic;
    JMenuItem formatFont;
    JMenu formatFontSize, formatColorFg, formatColorBg;
    JMenuItem about;

    UndoManager undoManager = new UndoManager(); // Manages undo/redo operations

    ButtonGroup fontGroup = new ButtonGroup();
    ButtonGroup fontSizeGroup = new ButtonGroup();
    ButtonGroup colorFgGroup = new ButtonGroup();
    ButtonGroup colorBgGroup = new ButtonGroup();

    int charCount, wordCount, lineNum = 1;

    String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    // Font size options
    int[] fontSizes = { 12, 14, 16, 18, 20, 24, 28, 36, 48, 56, 64, 72 };

    // Color options
    String[] colors = { "Black", "White", "Red", "Blue", "Green" };
    Color[] awtColors = { Color.BLACK, Color.WHITE, Color.RED, Color.BLUE, Color.GREEN };

    // Constructor to set up the Notepad UI
    public Notepad() {
        setTitle("Notepad - New");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create the menu bar and add menus
        menuBar = new JMenuBar();
        addMenus();
        addFileMenuItems();
        addEditMenuItems();
        addViewMenuItems();
        addFormatMenuItems();
        addHelpMenuItems();

        // Set up the text area
        textarea = new JTextArea();
        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);
        textarea.setForeground(Color.BLACK);
        textarea.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // Padding
        textarea.getDocument().addUndoableEditListener(undoManager); // Add undo support
        TaActionListener tal = new TaActionListener(this);
        textarea.addKeyListener(tal);
        textarea.addCaretListener(tal);

        // Add the text area to a scroll pane
        JScrollPane taScrollPane = new JScrollPane(
                textarea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        taScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Set up the details area to display word count and line information
        // Create a new JPanel with centered flow layout and specified gaps
        detail = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        detail.setBackground(new Color(230, 230, 230)); // Set background to a light gray color
        detail.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY)); // Create a top border

        // Initialize JLabels for displaying character count, word count, and line
        // number
        chars = new JLabel("Characters: " + charCount);
        words = new JLabel("Words: " + wordCount);
        line = new JLabel("Line: " + lineNum);

        // Add the labels to the detail panel
        detail.add(chars);
        detail.add(words);
        detail.add(line);
        // Don't show the detail panel initially
        detail.setVisible(false);

        // Set the menu bar and add the scroll pane to the frame
        setLayout(new BorderLayout());
        setJMenuBar(menuBar);
        add(taScrollPane);
        add(detail, BorderLayout.SOUTH);

        setVisible(true); // Display the frame

        ActionListener incSize = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (textarea.getFont().getSize() < 72) {
                    textarea.setFont(textarea.getFont().deriveFont(textarea.getFont().getSize() + 2f));
                    setSelectedSize(textarea.getFont().getSize());
                }
            }

        };
        getRootPane().registerKeyboardAction(
                incSize,
                KeyStroke.getKeyStroke('=', InputEvent.CTRL_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(
                incSize,
                KeyStroke.getKeyStroke('=', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionListener decSize = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (textarea.getFont().getSize() > 12) {
                    textarea.setFont(textarea.getFont().deriveFont(textarea.getFont().getSize() - 2f));
                    setSelectedSize(textarea.getFont().getSize());
                }
            }

        };
        getRootPane().registerKeyboardAction(
                decSize,
                KeyStroke.getKeyStroke('-', InputEvent.CTRL_DOWN_MASK),
                JComponent.WHEN_FOCUSED);
    }

    private void setSelectedSize(int size) {
        for (Enumeration<AbstractButton> buttons = fontSizeGroup.getElements(); buttons
                .hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.getText().equals(String.valueOf(size))) {
                button.setSelected(true);
                return;
            }
        }

        fontSizeGroup.clearSelection();
    }

    public static void main(String[] args) {
        new Notepad();
    }

    // Method to add menus to the menu bar
    private void addMenus() {
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        menuBar.add(fileMenu);

        editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');
        menuBar.add(editMenu);

        viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V');
        menuBar.add(viewMenu);

        formatMenu = new JMenu("Format");
        formatMenu.setMnemonic('o');
        menuBar.add(formatMenu);

        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        menuBar.add(helpMenu);
    }

    // Method to add items to the File menu
    private void addFileMenuItems() {
        // Create an instance of FileActionListener, passing this Notepad instance
        FileActionListener fal = new FileActionListener(this);

        // Create and configure the "New" menu item
        newFile = new JMenuItem("New");
        newFile.addActionListener(fal); // Attach the action listener
        newFile.setActionCommand("New"); // Set the action command for identifying the action
        newFile.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK)); // Set keyboard shortcut
        fileMenu.add(newFile); // Add the menu item to the File menu

        open = new JMenuItem("Open");
        open.addActionListener(fal);
        open.setActionCommand("Open");
        open.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(open);

        save = new JMenuItem("Save");
        save.addActionListener(fal);
        save.setActionCommand("Save");
        save.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(save);

        saveAs = new JMenuItem("Save As");
        saveAs.addActionListener(fal);
        saveAs.setActionCommand("Save As");
        saveAs.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        fileMenu.add(saveAs);

        print = new JMenuItem("Print");
        print.addActionListener(fal);
        print.setActionCommand("Print");
        print.setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(print);

        fileMenu.addSeparator(); // Add a separator line

        close = new JMenuItem("Close");
        close.addActionListener(fal);
        close.setActionCommand("Close");
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
        // Set preferred size for the menu item. This will affect the whole menu
        close.setPreferredSize(new Dimension(150, close.getPreferredSize().height));
        fileMenu.add(close);
    }

    // Method to add items to the Edit menu
    private void addEditMenuItems() {
        EditActionListener eal = new EditActionListener(this);

        undo = new JMenuItem("Undo");
        undo.addActionListener(eal);
        undo.setActionCommand("Undo");
        undo.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
        editMenu.add(undo);

        redo = new JMenuItem("Redo");
        redo.addActionListener(eal);
        redo.setActionCommand("Redo");
        redo.setAccelerator(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK));
        editMenu.add(redo);

        editMenu.addSeparator(); // Add a separator line

        cut = new JMenuItem("Cut");
        cut.addActionListener(eal);
        cut.setActionCommand("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_DOWN_MASK));
        editMenu.add(cut);

        copy = new JMenuItem("Copy");
        copy.addActionListener(eal);
        copy.setActionCommand("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));
        editMenu.add(copy);

        paste = new JMenuItem("Paste");
        paste.addActionListener(eal);
        paste.setActionCommand("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_DOWN_MASK));
        editMenu.add(paste);

        delete = new JMenuItem("Delete");
        delete.addActionListener(eal);
        delete.setActionCommand("Delete");
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        editMenu.add(delete);

        editMenu.addSeparator(); // Add a separator line

        selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(eal);
        selectAll.setActionCommand("Select All");
        selectAll.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_DOWN_MASK));
        editMenu.add(selectAll);

        editMenu.addSeparator(); // Add a separator line

        timeDate = new JMenuItem("Time/Date");
        timeDate.addActionListener(eal);
        timeDate.setActionCommand("Time/Date");
        timeDate.setAccelerator(KeyStroke.getKeyStroke("F5"));
        timeDate.setPreferredSize(new Dimension(125, timeDate.getPreferredSize().height));
        editMenu.add(timeDate);
    }

    // Method to add items to the View menu
    private void addViewMenuItems() {
        ViewActionListener val = new ViewActionListener(this);

        wordWrap = new JCheckBoxMenuItem("Word Wrap", true);
        wordWrap.addActionListener(val);
        wordWrap.setActionCommand("Word Wrap");
        wordWrap.setAccelerator(KeyStroke.getKeyStroke('W', InputEvent.CTRL_DOWN_MASK));
        viewMenu.add(wordWrap);

        lineWrap = new JCheckBoxMenuItem("Line Wrap", true);
        lineWrap.addActionListener(val);
        lineWrap.setActionCommand("Line Wrap");
        lineWrap.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.CTRL_DOWN_MASK));
        viewMenu.add(lineWrap);

        showDetails = new JCheckBoxMenuItem("Show Details");
        showDetails.addActionListener(val);
        showDetails.setActionCommand("Show Details");
        showDetails.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_DOWN_MASK));
        showDetails.setPreferredSize(new Dimension(150, showDetails.getPreferredSize().height));
        viewMenu.add(showDetails);
    }

    // Method to add items to the Format menu
    private void addFormatMenuItems() {
        FormatActionListener fal = new FormatActionListener(this);

        bold = new JCheckBoxMenuItem("Bold");
        bold.addActionListener(fal);
        bold.setActionCommand("fstyle-Bold");
        bold.setAccelerator(KeyStroke.getKeyStroke('B', InputEvent.CTRL_DOWN_MASK));
        formatMenu.add(bold);

        italic = new JCheckBoxMenuItem("Italic");
        italic.addActionListener(fal);
        italic.setActionCommand("fstyle-Italic");
        italic.setAccelerator(KeyStroke.getKeyStroke('I', InputEvent.CTRL_DOWN_MASK));
        formatMenu.add(italic);

        formatMenu.addSeparator();

        formatFont = new JMenuItem("Font");
        formatFont.addActionListener(fal);
        formatFont.setActionCommand("ffam-change");
        formatMenu.add(formatFont);

        formatFontSize = new JMenu("Font Size");
        formatMenu.add(formatFontSize);

        // Adding font size options to the Format menu
        JRadioButtonMenuItem[] fontSizeItems = new JRadioButtonMenuItem[fontSizes.length];
        for (int i = 0; i < fontSizes.length; i++) {
            // Initialize each radio button with the font sizes and set the first item as
            // the default selected size
            fontSizeItems[i] = new JRadioButtonMenuItem(String.valueOf(fontSizes[i]), (i == 0));
            fontSizeItems[i].addActionListener(fal);
            fontSizeItems[i].setActionCommand("fsize-" + String.valueOf(fontSizes[i]));
            formatFontSize.add(fontSizeItems[i]);
            fontSizeGroup.add(fontSizeItems[i]);
        }

        formatMenu.addSeparator();

        // Create a menu for text color options
        formatColorFg = new JMenu("Text Color");
        formatMenu.add(formatColorFg);

        // Create an array of radio button menu items for predefined text colors
        JRadioButtonMenuItem[] colorFgItems = new JRadioButtonMenuItem[colors.length];
        for (int i = 0; i < colors.length; i++) {
            // Initialize each radio button with color name and set "Black" as the default
            // selected color
            colorFgItems[i] = new JRadioButtonMenuItem(colors[i], (colors[i] == "Black"));
            colorFgItems[i].addActionListener(fal);
            colorFgItems[i].setActionCommand("fcolorfg-" + String.valueOf(i));
            formatColorFg.add(colorFgItems[i]);
            colorFgGroup.add(colorFgItems[i]);
        }

        // Create a menu item for custom text color selection
        JMenuItem customFgColor = new JMenuItem("Custom");
        customFgColor.addActionListener(fal);
        customFgColor.setActionCommand("fcolorfg-Custom");
        customFgColor.setPreferredSize(new Dimension(125, customFgColor.getPreferredSize().height));
        formatColorFg.addSeparator();
        formatColorFg.add(customFgColor);

        // Create a menu for background color options
        formatColorBg = new JMenu("Background Color");
        formatMenu.add(formatColorBg);

        // Create an array of radio button menu items for predefined background colors
        JRadioButtonMenuItem[] colorBgItems = new JRadioButtonMenuItem[colors.length];
        for (int i = 0; i < colors.length; i++) {
            // Initialize each radio button with color name and set "White" as the default
            // selected color
            colorBgItems[i] = new JRadioButtonMenuItem(colors[i], (colors[i] == "White"));
            colorBgItems[i].addActionListener(fal);
            colorBgItems[i].setActionCommand("fcolorbg-" + String.valueOf(i));
            formatColorBg.add(colorBgItems[i]);
            colorBgGroup.add(colorBgItems[i]);
        }

        // Create a menu item for custom background color selection
        JMenuItem customBgColor = new JMenuItem("Custom");
        customBgColor.addActionListener(fal);
        customBgColor.setActionCommand("fcolorbg-Custom");
        customBgColor.setPreferredSize(new Dimension(125, customBgColor.getPreferredSize().height));
        formatColorBg.addSeparator();
        formatColorBg.add(customBgColor);

    }

    // Method to add items to the Help menu
    private void addHelpMenuItems() {
        HelpActionListener hal = new HelpActionListener(this);
        about = new JMenuItem("About");
        about.addActionListener(hal);
        about.setAccelerator(KeyStroke.getKeyStroke("F1"));
        about.setPreferredSize(new Dimension(100, about.getPreferredSize().height));
        helpMenu.add(about);
    }

}
