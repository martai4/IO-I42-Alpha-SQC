package pl.put.poznan.checker.logic.visitor.base;

public interface VisitableElement {
    Visitor acceptVisitor(Visitor visitor);
}
