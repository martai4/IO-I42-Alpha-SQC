package pl.put.poznan.checker.scenario;

//todo: przerzucić do logiki
public interface VisitableElement {
    Visitor acceptVisitor( Visitor visitor);
}
