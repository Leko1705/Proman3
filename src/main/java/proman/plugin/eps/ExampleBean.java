package proman.plugin.eps;

import proman.diagrams.DiagramType;
import proman.plugin.Attribute;
import proman.plugin.Required;

public class ExampleBean {

    @Required
    @Attribute("foo")
    public DiagramType type;

    @Attribute("bar")
    public String some;

    @Override
    public String toString() {
        return "DiagramBean{" +
                "foo=" + type +
                ", bar=" + some +
                '}';
    }
}
