package pl.put.poznan.checker.logic.visitor.base;

//todo: przerzucić do logiki
public interface VisitableElement {
    Visitor acceptVisitor(Visitor visitor);
}
