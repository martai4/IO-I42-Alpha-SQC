package pl.put.poznan.checker.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.put.poznan.checker.logic.visitor.base.VisitableElement;
import pl.put.poznan.checker.logic.visitor.base.Visitor;

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


    /**
     * Domyslny konstruktor SubScenario.
     */
    public SubScenario(){

    }

    public boolean Is_start_w1_w2(String w1, String w2,int start_pos){
        for (int i=0;i<w1.length();i++){
            if(w1.charAt(i) != w2.charAt(i+start_pos)){
                return false;
            }
        }
        return true;
    }


        public int HowManyDecisions(){
        int counter=0;
        for (Step s : steps) {
            if(s.IS_start_from_decision()){
                counter+=1;
            }
            if(s.getChild()!=null){
                counter+=s.getChild().HowManyDecisions();
            }
        }
        return counter;
    }

    public boolean CheckActors(List<String> actors,String step, int start) {
        for (String s : actors) {
            if( Is_start_w1_w2(s, step,start)){
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
        for (Step s : steps) {
            if (Is_start_w1_w2(k1, s.getText(), 0)) {
                if (!CheckActors(actors, s.getText(), k1.length() + 1)) {
                    odpowiedz.add(s.getText());
                }
            } else if (Is_start_w1_w2(k2, s.getText(), 0)) {
                if (!CheckActors(actors, s.getText(), k2.length() + 1)) {
                    odpowiedz.add(s.getText());
                }
            } else if (Is_start_w1_w2(k3, s.getText(), 0)) {
                if (!CheckActors(actors, s.getText(), k3.length() + 1)) {
                    odpowiedz.add(s.getText());
                }
            } else {
                if (!CheckActors(actors, s.getText(), 0)) {
                    odpowiedz.add(s.getText());
                }
            }
            if(s.getChild()!=null){
                List<String> odpowiedz_pom= new ArrayList<>();
                odpowiedz_pom=(s.getChild().ListNoActorsErrors(actors));
                for (String pom : odpowiedz_pom){
                    odpowiedz.add(pom);
                }
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
