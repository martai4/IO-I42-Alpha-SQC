package pl.put.poznan.checker.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.put.poznan.checker.scenario.Scenario;
import pl.put.poznan.checker.scenario.Step;
import pl.put.poznan.checker.scenario.SubScenario;
import pl.put.poznan.checker.scenario.Visitor;

/**
 * Wizytator odpowiedzialny za przetworzenie {@link pl.put.poznan.checker.scenario.Scenario scenariusza} na {@link ScenarioTextifier#text tekst} .
 *
 * @author I42-Alpha
 * @version 1.0
 */

public class ScenarioTextifier implements Visitor {



    private static final Logger logger = LoggerFactory.getLogger(ScenarioTextifier.class);
    /**
     * @see ScenarioTextifier#getText()
     */
    private String text;
    private String stepPrefix;


    /**
     * Zwraca utworzony scenariusz w postaci {@link ScenarioTextifier#text tekstu}.
     * @return scenariusz w postaci {@link ScenarioTextifier#text tekstu}
     */


    public String getText() {
        return text;
    }

    public ScenarioTextifier(){
        this.text = new String();
        this.stepPrefix ="";
    }

    /**
     * Odwiedza wskazany {@link pl.put.poznan.checker.scenario.Scenario scenariusz}, dolacza do {@link ScenarioTextifier#text tekstu} informacje o {@link pl.put.poznan.checker.scenario.Scenario scenariuszu }
     * i odwiedza  {@link pl.put.poznan.checker.scenario.SubScenario podscenariusz} {@link pl.put.poznan.checker.scenario.Scenario#main glowny}.
     *
     * @param scenario {@link pl.put.poznan.checker.scenario.Scenario scenariusz} ktory chcemy zamienic na {@link ScenarioTextifier#text tekst}.
     * @return zwraca {@link ScenarioTextifier siebie}.
     */

    @Override
    public Visitor visit(Scenario scenario){
        String name = scenario.getName();
        logger.debug("Visitor visit(Scenario scenario) Odwiedzano scenariusz: {}",name);
        if(name!=null){
            logger.debug("Visitor visit(Scenario scenario) Zapisano tytul: {}",name);
            this.text = this.text.concat("Tytuł: " + name+"\n");
        }
        else logger.debug("Visitor visit(Scenario scenario) Tytul jest null");

        boolean firstActor=true;
        for(String actor: scenario.getActors()){
            if(!firstActor) this.text = this.text.concat(", ");
            else this.text = this.text.concat("Aktorzy: ");
            firstActor = false;
            this.text = this.text.concat(actor);
            logger.debug("Visitor visit(Scenario scenario) Zapisano aktora: {}",actor);
        }
        if(firstActor) logger.debug("Visitor visit(Scenario scenario) Brak aktorow");
        this.text = this.text.concat("\n");
        String systemActor = scenario.getSystemActor();
        if(systemActor!=null) {
            this.text=this.text.concat("Aktor Systemowy: " + systemActor + "\n");
            logger.debug("Visitor visit(Scenario scenario) Dodano aktora systemowego: {}",systemActor);
        } else logger.debug("Visitor visit(Scenario scenario) Brak aktora systemowego");
        this.text = this.text.concat("\n");
        if(scenario.getMain()!=null)scenario.getMain().acceptVisitor(this);
        logger.info("Visitor visit(Scenario scenario) Zapisano scenariusz <{}> w postaci tekstu :\n{}",name,this.text);
        return this;
    }

    /**
     * Odwiedza wskazany {@link pl.put.poznan.checker.scenario.SubScenario podscenariusz}, i odwiedza zawarte w nim {@link pl.put.poznan.checker.scenario.Step kroki}.
     *
     * @param subScenario odwiedzany {@link pl.put.poznan.checker.scenario.SubScenario podscenariusz}.
     * @return zwraca {@link ScenarioTextifier siebie}.
     */

    @Override
    public Visitor visit(SubScenario subScenario){
        logger.debug("Visitor visit(SubScenario subScenario) Odwiedzono podscenariusz");
        String stepPrefix = this.stepPrefix;
        for (Integer i = 1; i <= subScenario.getSteps().size(); i++)
        {
            this.stepPrefix = stepPrefix.concat(i.toString()+".");
            logger.debug("Visitor visit(SubScenario subScenario) Prefix {}", this.stepPrefix);
            subScenario.getSteps().get(i-1).acceptVisitor(this);
        }
        this.stepPrefix=stepPrefix;
        return this;
    }

    /**
     * Odwiedza wskazany {@link pl.put.poznan.checker.scenario.Step krok}, dodaje go do {@link ScenarioTextifier#text tekstu} i odwiedza zawarty w nim {@link pl.put.poznan.checker.scenario.Step#child podscenariusz} jesli taki istenie.
     *
     * @param step  odwiedzany {@link pl.put.poznan.checker.scenario.Step krok}.
     * @return zwraca {@link ScenarioTextifier siebie}.
     */

    @Override
    public Visitor visit(Step step){
        String name = step.getText();
        if(name !=null)
        {
            this.text = this.text.concat(this.stepPrefix + step.getText() + "\n");
            logger.debug("Visitor visit(Step step) Odwiedzono i dodano krok: {}", name);
        } else
            logger.debug("Visitor visit(Step step) Odwiedzony krok jest pusty");
        if(step.getChild()!=null)step.getChild().acceptVisitor(this);
        return this;
    }
}
