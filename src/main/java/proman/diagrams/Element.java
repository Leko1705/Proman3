package proman.diagrams;

import java.awt.*;

public interface Element {

    Component getView();

    Element copy();

}
