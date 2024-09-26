package proman.diagrams.graph.nodes;

import proman.view.style.Styles;
import proman.view.style.TextStyle;
import proman.utils.SwingUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class TextNode implements Node {

    public static final NodeType<?> TYPE = new Type();


    private final View view;


    public TextNode() {
        this("text");
    }

    public TextNode(String initialText) {
        this.view = new View(initialText);
    }

    @Override
    public NodeType<?> getType() {
        return TYPE;
    }

    @Override
    public Component getView() {
        return view;
    }

    public String getText() {
        return view.getText();
    }

    void setText(String text) {
        view.setText(text);
    }

    @Override
    public Node copy() {
        return new TextNode(getText());
    }


    private static class Type implements NodeType<TextNode> {

        @Override
        public String getName() {
            return "Text";
        }

        @Override
        public TextNode createNode() {
            return new TextNode();
        }

    }



    private class View extends JTextPane {


        public View(String text) {
            setText(text);

            Styles styles = TYPE.getStyle();

            TextStyle textStyle = styles.getTextStyle();

            SwingUtils.applyTextStyle(super.getStyledDocument(), textStyle);

            TextNode node = TextNode.this;

            setDisabledTextColor(Color.BLACK);
            setEnabled(false);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        setEnabled(true);
                        requestFocusInWindow();
                    }
                }
            });

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    setEnabled(false);
                }
            });

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        setEnabled(false);
                        KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner();
                    }
                }
            });

            ((AbstractDocument) getDocument()).setDocumentFilter(new DocumentFilter(){
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if (text.equals("\n")){
                        setEnabled(false);
                        return;
                    }
                    super.replace(fb, offset, length, text, attrs);
                }
            });

            addKeyListener(new KeyAdapter() {
                private final Set<Integer> pressedKeys = new HashSet<>();

                @Override public void keyPressed(KeyEvent e) {
                    pressedKeys.add(e.getKeyCode());
                    if (pressedKeys.contains(KeyEvent.VK_ENTER) && pressedKeys.contains(KeyEvent.VK_SHIFT)){
                        setText(getText() + "\n");
                    }
                }
                @Override public void keyReleased(KeyEvent e) {
                    pressedKeys.remove(e.getKeyCode());
                }
            });

            getStyledDocument().addDocumentListener(new DocumentListener(){
                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateSize(getText());
                }
                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateSize(getText());
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateSize(getText());
                }

            });

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()){
                        node.getView();
                        // TODO auto remove
                    }
                }
            });

            setOpaque(false);
            updateSize(text);
        }

        private void updateSize(String text){
            JLabel label = new JLabel(text);
            FontMetrics fm = label.getFontMetrics(label.getFont());
            int width = fm.stringWidth(text);
            int height = fm.getHeight() * text.split("\n").length;

            setSize(width, height);
        }
    }

}
