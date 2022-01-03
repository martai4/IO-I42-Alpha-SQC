package pl.put.poznan.checker.logic.visitor.base;
import pl.put.poznan.checker.scenario.Scenario;

/**
 * {@link Visitor Wizytator} powienien posiadać osobną <b>klasę akceptacyjną</b>.
 * Dzięki temu możemy zastosować poprawny <i>Wizytator</i> na <b>obiektach</b> klas i ich podklas, które są <b>polimorficzne</b>.
 *
 * @author I42-Alpha
 * @version 2.0
 */
public interface VisitableElement
{
    /**
     * Przyjmuje {@link Visitor Wizytatora} i wywołuje jego funkcję {@link Visitor#visit(Scenario) <code>visit</code>}.
     * @param visitor <i>Wizytator</i>, który ma odwiedzić <code>VisitableElement</code>
     * @return Zwraca <i>Wizytatora</i>, który przemierzył przez <code>VisitableElement</code>.
     */
    Visitor acceptVisitor(Visitor visitor);
}
