package pl.put.poznan.checker.scenario;
import java.util.LinkedList;
public class SubLevelsVisitor implements Visitor {

    private Scenario toConvert;
    private int maxLevel;
    private int currentLevel;

    private final String newName;
    private SubScenario mainScenario;
    private SubScenario currentParent;
    private Scenario result;

    public SubLevelsVisitor(Scenario toConvert, int lvl, String name) throws Exception {
        this.toConvert = toConvert;
        this.newName = name;
        this.currentLevel = 0;
        this.result = null;
        if(lvl < 1){
            throw new Exception();
        }
        else{
            this.maxLevel = lvl;
        }
    }

    @Override
    public Visitor visit(Scenario toConvert) {
        this.mainScenario = new SubScenario();
        this.result = new Scenario(newName, toConvert.getActors(), toConvert.getSystemActor(), mainScenario);
        // wizytator wchodzi w tresc scenariusza
//        this.parents.push(mainScenario);
        currentParent = mainScenario;
        toConvert.getMain().acceptVisitor(this);

        return this;
    }

    @Override
    public Visitor visit(SubScenario subScenario) {
        SubScenario parentForThis = currentParent;
        for( Step step : subScenario.getSteps()){
//            this.parents.add(subScenario);
            step.acceptVisitor(this);
            currentParent = parentForThis;
        }
        return this;
    }

    @Override
    public Visitor visit(Step step) {
        this.currentLevel += 1;
//        if(this.currentLevel < this.maxLevel-1 || (this.currentLevel == this.maxLevel && step.getChild() == null)){
//            this.parents.getLast().getSteps().add(step);
//        }
        if(this.currentLevel < this.maxLevel){
            if(step.getChild()==null){
                currentParent.getSteps().add(step);
            }
            else{
                SubScenario newStepSubscenario = new SubScenario();
                SubScenario parentForThis = currentParent;
                currentParent = newStepSubscenario;
                step.getChild().acceptVisitor(this);
                currentParent = parentForThis;
                currentParent.getSteps().add(new Step(step.getText(), newStepSubscenario));
            }
        }
        else
        {
            currentParent.getSteps().add(new Step(step.getText(), null));
//            parents.getLast().getSteps().add(new Step(step.getText(), null));
        }
        this.currentLevel -=1;
        return this;
    }
    public Scenario getConverted(){
        return this.result;
    }
}
