package pl.put.poznan.checker.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Visitator responsible for turning {@link pl.put.poznan.checker.scenario.Scenario Scenario} into single string.
 *
 * @author I42-Alpha
 * @version 1.0
 */

public class ScenarioTextifier implements Visitor{



    private static final Logger logger = LoggerFactory.getLogger(ScenarioTextifier.class);

    private String text;
    private String stepPrefix;


    /**
     * @return String
     */


    public String getText() {
        return text;
    }

    public ScenarioTextifier(){
        this.text = new String();
        this.stepPrefix ="";
    }

    /**
     * Visits given {@link pl.put.poznan.checker.scenario.Scenario Scenario}, appends string and visits main {@link pl.put.poznan.checker.scenario.SubScenario SubScenario}.
     *
     * @param scenario {@link pl.put.poznan.checker.scenario.Scenario Scenario} to visit
     */

    @Override
    public Visitor visit(Scenario scenario){
        String name = scenario.getName();
        logger.debug("Visited Scenario {} by ScenarioTextifier",name);
        if(name!=null){
            logger.debug("Added Title :{}",name);
            this.text = this.text.concat("Tytuł: " + name+"\n");
        }
        else logger.debug("Title is null");

        boolean firstActor=true;
        this.text = this.text.concat("Aktorzy: ");
        for(String actor: scenario.getActors()){
            if(!firstActor) this.text = this.text.concat(", ");
            else this.text = this.text.concat("Aktorzy: ");
            firstActor = false;
            this.text = this.text.concat(actor);
            logger.debug("Added Actor :{}",actor);
        }
        if(firstActor) logger.debug("No Actors");
        this.text = this.text.concat("\n");
        String systemActor = scenario.getSystemActor();
        if(systemActor!=null) {
            this.text=this.text.concat("Aktor Systemowy: " + systemActor + "\n");
            logger.debug("Added Actor :{}",systemActor);
        } else logger.debug("No System Actor");
        this.text = this.text.concat("\n");
        if(scenario.getMain()!=null)scenario.getMain().acceptVisitor(this);
        logger.info("Created string containing scenario {}:\n{}",scenario.getName(),this.text);
        return this;
    }

    /**
     * Visits given {@link pl.put.poznan.checker.scenario.SubScenario SubScenario}, appends string and visits {@link pl.put.poznan.checker.scenario.Step Steps} in it.
     *
     * @param subScenario {@link pl.put.poznan.checker.scenario.SubScenario SubScenario} to visit
     */

    @Override
    public Visitor visit(SubScenario subScenario){
        logger.debug("Visited SubScenario by ScenarioTextifier");
        String stepPrefix = this.stepPrefix;
        logger.debug("");
        for (Integer i = 1; i <= subScenario.getSteps().size(); i++)
        {
            this.stepPrefix = stepPrefix.concat(i.toString()+".");
            logger.debug("Prefix {}", this.stepPrefix);
            subScenario.getSteps().get(i-1).acceptVisitor(this);
        }
        this.stepPrefix=stepPrefix;
        return this;
    }

    /**
     * Visits given {@link pl.put.poznan.checker.scenario.Step Step}, appends string and visits {@link pl.put.poznan.checker.scenario.SubScenario Subscenario} in given {@link pl.put.poznan.checker.scenario.Step Step} if there is any.
     *
     * @param step {@link pl.put.poznan.checker.scenario.Step Step} to visit
     */

    @Override
    public Visitor visit(Step step){
        String name = step.getText();
        if(name !=null)
        {
            this.text = this.text.concat(this.stepPrefix + step.getText() + "\n");
            logger.debug("Visited and added Step {}", name);
        } else
            logger.debug("Visited Step and text is null");
        if(step.getChild()!=null)step.getChild().acceptVisitor(this);
        return this;
    }
}
