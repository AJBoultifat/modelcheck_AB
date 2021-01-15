package implementation;

import java.util.ArrayList;

import model.State;

public class CheckEUResult {
    private SearchContinuation searchContinuation;
    private ArrayList<State> witnessPath;

    public CheckEUResult(SearchContinuation searchContinuation, State lastStateInWitnessPath) {
        this.searchContinuation = searchContinuation;
        this.witnessPath = new ArrayList<>();
        this.witnessPath.add(lastStateInWitnessPath);
    }

    public SearchContinuation getSearchContinuation() {
        return searchContinuation;
    }

    public ArrayList<State> getWitnessPath() {
        return witnessPath;
    }

    public void prependWitnessPathWith(State state) {
        witnessPath.add(0, state);
    }
}
