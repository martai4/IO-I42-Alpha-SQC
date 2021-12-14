package pl.put.poznan.checker.scenario;

import java.util.List;

public class SubScenario {
    private Integer level;
    private Integer length;
    private List<Step> steps;

    public boolean Is_w2_start_w1(String w1, String w2,int start_pos=0){
        for (int i=0;i<w1.length();i++){
            if(w1.charAt(i) != w2.charAt(i+start_pos)){
                return false;
            }
        }
        return true;
    }

    public int HowManyDecisions(){
        int counter=0;
        String k1="IF";
        String k2="ELSE";
        String k3="FOR EACH";
        for (Step s : steps) {
            if(Is_w2_start_w1(k1, String.valueOf(s)) || Is_w2_start_w1(k2,String.valueOf(s)) || Is_w2_start_w1(k3,String.valueOf(s))){
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
            if(Is_w2_start_w1(k1, String.valueOf(s))){
                if (!CheckActors(actors,String.Valueof(s),k1.length()+1)) {
                    odpowiedz.add(s);
                }
            }
            else if(Is_w2_start_w1(k2,String.valueOf(s),k2.length()+1)) {
                if (!CheckActors(actors,String.Valueof(s),k2.length()+1)) {
                    odpowiedz.add(s);
                }
            }
            else if(Is_w2_start_w1(k3,String.valueOf(s),k3.length()+1)) {
                if (!CheckActors(actors,String.Valueof(s),k3.length()+1)) {
                    odpowiedz.add(s);
                }
            }
            else {
                if (!CheckActors(actors,String.Valueof(s))) {
                    odpowiedz.add(s);
                }
            }
        }
        return odpowiedz;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> stepsList) {
        this.steps = stepsList;
    }
}
