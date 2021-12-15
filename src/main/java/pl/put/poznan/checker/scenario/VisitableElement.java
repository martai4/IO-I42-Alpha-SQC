package pl.put.poznan.checker.scenario;

public interface VisitableElement {
    Visitor acceptVisitor( Visitor visitor);
}
