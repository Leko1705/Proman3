package proman.view.style;

import java.awt.*;

class NoStyles implements Styles {

    @Override
    public LineStyle getLineStyle() {
        return new LineStyle() {
            @Override
            public int getThickness() {
                return 1;
            }

            @Override
            public Color getColor() {
                return Color.BLACK;
            }

            @Override
            public boolean isDashedLine() {
                return false;
            }
        };
    }

    @Override
    public ComponentStyle getComponentStyle() {
        return new ComponentStyle() {
            @Override
            public LineStyle getLineStyle() {
                return new LineStyle() {
                    @Override
                    public int getThickness() {
                        return 1;
                    }

                    @Override
                    public Color getColor() {
                        return Color.BLACK;
                    }

                    @Override
                    public boolean isDashedLine() {
                        return false;
                    }
                };
            }

            @Override
            public Color getBackgroundColor() {
                return Color.GRAY;
            }
        };
    }

    @Override
    public TextStyle getTextStyle() {
        return new TextStyle() {
            @Override
            public int getTextSize() {
                return 15;
            }

            @Override
            public boolean isBold() {
                return false;
            }

            @Override
            public boolean isItalic() {
                return false;
            }

            @Override
            public boolean isUnderline() {
                return false;
            }

            @Override
            public Color getTextColor() {
                return Color.BLACK;
            }

            @Override
            public Color getUnderlineColor() {
                return Color.BLACK;
            }
        };
    }
}
