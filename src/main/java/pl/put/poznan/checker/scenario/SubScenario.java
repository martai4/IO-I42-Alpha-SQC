package pl.put.poznan.checker.scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Kawałek scenariusza, który zostaje wywoływany jako część wybranego kroku
 *
 * @author I42-Alpha
 * @version 1.0
 */
public class SubScenario {

    private final List<Step> steps = new ArrayList<>();
    //todo: uzgodnić, czy te gettery i settery są potrzebne, gdy mamy dostęp do listy za pomocą innych metod, których
    //todo: zaletą jest (będzie) obsługa błędów
    /*
    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> stepsList) {
        this.steps = stepsList;
    }
    */

    //todo: sprawdzić błędy (próba dostania się do elementu poza zakresem)
    /**
     * Zwraca wybrany krok podscenariusza
     * @param index indeks kroku
     * @return krok
     */
    public Step getStep(Integer index) {
        return steps.get(index);
    }
    //todo: sprawdzić błędy (próba dostania się do elementu poza zakresem)
    /**
     * Nadpisuje wybrany krok scenariusza
     * @param index indeks kroku, który zostanie nadpisany
     * @param step nowy krok
     */
    public void setStep(Integer index, Step step) {
        this.steps.set(index, step);
    }
    /**
     * Dodaje krok na koniec listy kroków
     * @param step nowy krok
     */
    public void addStep(Step step) {
        this.steps.add(step);
    }
    /**
     * Dodaje krok na koniec listy kroków, tworząc go z podanego tekstu
     * @param stepText tekst nowego kroku
     */
    public void addStep(String stepText) {
        this.steps.add(new Step(stepText));
    }
    /**
     * Zwraca rozmiar listy kroków
     * @return rozmiar
     */
    public Integer getLength() {
        return steps.size();
    }
}
