package pl.put.poznan.checker.scenario;

public class ScenarioTextifier implements Visitor{

    /**
     * Wizytator tworzący tekst scenariusza
     *
     * @author I42-Alpha
     * @version 1.0
     */

    private Integer depth;
    private String text;
    private String stepPrefix;

    public String getText() {
        return text;
    }

    public ScenarioTextifier(){
        this.depth = 0;
        this.text = new String();
        this.stepPrefix ="";
    }

    @Override
    public Visitor visit(Scenario scenario){
        this.text = this.text.concat("Tytuł: " + scenario.getName()+"\n");
        Boolean firstActor=true;
        this.text = this.text.concat("Aktorzy: ");
        for(String actor: scenario.getActors()){
            if(!firstActor) this.text = this.text.concat(", ");
            firstActor = false;
            this.text = this.text.concat(actor);
        }
        this.text = this.text.concat("\n");
        if(scenario.getSystemActor()!=null) this.text=this.text.concat("Aktor Systemowy: " + scenario.getSystemActor() + "\n");
        this.text = this.text.concat("\n");
        if(scenario.getMain()!=null)scenario.getMain().acceptVisitor(this);
        return this;
    }

    @Override
    public Visitor visit(SubScenario subScenario){
        String stepPrefix = this.stepPrefix;

        for (Integer i = 1; i <= subScenario.getSteps().size(); i++)
        {
            this.stepPrefix = stepPrefix.concat(i.toString()+".");
            subScenario.getSteps().get(i-1).acceptVisitor(this);
        }
        this.stepPrefix=stepPrefix;
        return this;
    }

    @Override
    public Visitor visit(Step step){
        this.text = this.text.concat(this.stepPrefix + step.getText() + "\n");
        if(step.getChild()!=null)step.getChild().acceptVisitor(this);
        return this;
    }
}
