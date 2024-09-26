package proman.view;

import proman.view.style.Styles;
import proman.view.style.TextStyle;

import javax.swing.*;
import java.util.EventListener;

public interface AnAction extends EventListener {

    default Icon getIcon(){
        return null;
    }

    String getText();

    default TextStyle getTextStyle(){
        return Styles.DEFAULT.getTextStyle();
    }

    void onAction();

}
