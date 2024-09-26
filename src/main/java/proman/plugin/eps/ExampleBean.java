package proman.plugin.eps;

import proman.diagrams.DiagramType;
import proman.plugin.Attribute;

public class ExampleBean {

    @Attribute("foo")
    public DiagramType type;

    @Attribute("bar")
    public String some;

    @Override
    public String toString() {
        return "DiagramBean{" +
                "foo=" + type +
                ", bar='" + some + '\'' +
                '}';
    }
}
