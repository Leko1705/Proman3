package proman.diagrams.graph.edges;

import proman.diagrams.Element;

public abstract class Edge {

    private final EdgeType<?> type;
    private final Element source;
    private final Element target;

    protected Edge(EdgeType<?> type,
                   Element source,
                   Element target) {
        this.type = type;
        this.source = source;
        this.target = target;
    }

    public EdgeType<?> getType(){
        return type;
    }

    public Element getSource(){
        return source;
    }

    public Element getDestination(){
        return target;
    }

    public abstract PathFactory getPathFactory();

    public HeadDefiner getSourceHeadPainter(){
        return null;
    }

    public HeadDefiner getDestinationHeadPainter(){
        return null;
    }


}
