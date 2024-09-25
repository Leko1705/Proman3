package proman.editing;

import proman.view.Tab;

import java.awt.*;

public interface Editor<T> {

    Tab getTab();

    Component createComponent(T editable);

}
