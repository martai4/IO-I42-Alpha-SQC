package pl.put.poznan.checker.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Kawalek {@link pl.put.poznan.checker.scenario.Scenario scenariusza}, ktory zostaje wywolywany jako czesc przetwarzania
 * pojedynczego {@link pl.put.poznan.checker.scenario.Step kroku}. Wyjatkiem jest podscenariusz glowny scenariusza, od
 * ktorego zaczyna sie przetwarzanie.
 *
 * @author I42-Alpha
 * @version 1.1
 */
public class SubScenario implements VisitableElement
{
    private static final Logger logger = LoggerFactory.getLogger(SubScenario.class);
    private List<Step> steps = new ArrayList<>();

    public boolean Is_w2_start_w1(String w1, String w2,int start_pos){
        for (int i=0;i<w1.length();i++){
            if(w1.charAt(i) != w2.charAt(i+start_pos)){
                return false;
            }
        }
        return true;
    }

    /**
     * Domyslny konstruktor SubScenario.
     */
    public SubScenario(){

    }

    public int HowManyDecisions(){
        int counter=0;
        String k1="IF";
        String k2="ELSE";
        String k3="FOR EACH";
        for (Step s : steps) {
            if(Is_w2_start_w1(k1, String.valueOf(s),0) || Is_w2_start_w1(k2,String.valueOf(s),0) || Is_w2_start_w1(k3,String.valueOf(s),0)){
                counter=counter+1;
            }
        }
        return counter;
    }

    public boolean CheckActors(List<String> actors,String step, int start) {
        for (String s : actors) {
            if(Is_w2_start_w1(s, step,start)){
                return true;
            }
        }
        return false;
    }

    public List<String> ListNoActorsErrors(List<String> actors){
        List<String> odpowiedz=new ArrayList<>();
        String k1="IF";
        String k2="ELSE";
        String k3="FOR EACH";
        for (Step s : steps)
            if(Is_w2_start_w1(k1, String.valueOf(s),0)){
                if (!CheckActors(actors, String.valueOf(s),k1.length()+1)) {
                    odpowiedz.add(String.valueOf(s));
                }
            }
            else if(Is_w2_start_w1(k2,String.valueOf(s),0)) {
                if (!CheckActors(actors,String.valueOf(s),k2.length()+1)) {
                    odpowiedz.add(String.valueOf(s));
                }
            }
            else if(Is_w2_start_w1(k3,String.valueOf(s),0)) {
                if (!CheckActors(actors,String.valueOf(s),k3.length()+1)) {
                    odpowiedz.add(String.valueOf(s));
                }
            }
            else {
                if (!CheckActors(actors,String.valueOf(s),0)) {
                    odpowiedz.add(String.valueOf(s));
                }
            }
            return odpowiedz;
        }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> stepsList) {
        this.steps = stepsList;
    }

    /**
     * Zwraca wybrany {@link pl.put.poznan.checker.scenario.Step krok} podscenariusza.
     * @param index indeks kroku
     * @return Krok z listy wszystkich krokow podscenariusza.
     */
    public Step getStep(Integer index) {
        if (index >= getLength())
        {
            logger.warn("getStep(Integer) probowal zwrocic nieistniejacy element o indeksie " + index.toString());
            return null;
        }
        return steps.get(index);
    }
    /**
     * Nadpisuje wybrany {@link pl.put.poznan.checker.scenario.Step krok} podscenariusza.
     * @param index indeks kroku, ktory zostanie nadpisany
     * @param step nowy krok
     */
    public void setStep(Integer index, Step step) {
        if (index >= getLength())
        {
            logger.warn("setStep(Integer, Step) probowal nadpisac nieistniejacy element o indeksie "
                    + index.toString());
            return;
        }
        this.steps.set(index, step);
    }
    /**
     * Dodaje {@link pl.put.poznan.checker.scenario.Step krok} na koniec listy krokow.
     * @param step nowy krok
     */
    public void addStep(Step step) {
        this.steps.add(step);
    }
    /**
     * Dodaje {@link pl.put.poznan.checker.scenario.Step krok} na koniec listy krokow, tworzac go z podanego tekstu.
     * @param stepText tekst nowego kroku
     */
    public void addStep(String stepText) {
        this.steps.add(new Step(stepText));
    }
    /**
     * Zwraca rozmiar listy {@link pl.put.poznan.checker.scenario.Step krokow}.
     * @return Rozmiar listy.
     */
    public Integer getLength() {
        return steps.size();
    }

    @Override
    public Visitor acceptVisitor(Visitor visitor) {
        return visitor.visit(this);
    }
}
