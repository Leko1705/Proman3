package proman.view.style;

public interface Styles {

    Styles DEFAULT = new NoStyles();


    LineStyle getLineStyle();

    ComponentStyle getComponentStyle();

    TextStyle getTextStyle();

}
