package ctlChecker;

import static implementation.SearchContinuation.ABORT;
import static implementation.SearchContinuation.CONTINUE;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import ctlBooleanOperator.And;
import ctlBooleanOperator.Not;
import ctlBooleanOperator.Or;
import ctlFormula.Atom;
import ctlFormula.False;
import ctlFormula.Formula;
import ctlFormula.True;
import ctlOperator.EU;
import ctlOperator.EX;
import implementation.CheckAUResult;
import implementation.CheckEUResult;
import implementation.BDDsearchData;
import model.KripkeStructure;
import model.State;

public class CTLModelChecker {


    private KripkeStructure kripkeStructure;

    private Map<State, Map<Formula, Boolean>> labels = new HashMap<>();

    public CTLModelChecker(KripkeStructure kripkeStructure) {
        this.kripkeStructure = kripkeStructure;
    }

    public static boolean satisfies(KripkeStructure kripkeStructure, Formula formula) {
        return new CTLModelChecker(kripkeStructure).satisfies(formula);
    }

    public boolean satisfies(Formula formula) {
        kripkeStructure.validate();
      
        Formula formulaBase = formula.convertToCTLBase();
        
        return kripkeStructure.getInitialStates().stream().allMatch(initialState -> satisfies(initialState, formulaBase));
    }

    private boolean addLabel(State state, Formula formula, boolean value) {
        Map<Formula, Boolean> labelsForState = labels.computeIfAbsent(state, k -> new HashMap<>());

        labelsForState.put(formula, value);

        return value;
    }

    private boolean satisfies(State state, Formula formula) {
        return getLabel(state, formula).orElseGet(() -> computeLabel(state, formula));
    }

    private Optional<Boolean> getLabel(State state, Formula formula) {
        return Optional.ofNullable(labels.getOrDefault(state, new HashMap<>()).get(formula));
    }

    private boolean computeLabel(State state, Formula formula) {
        if (formula instanceof True) {
            return addLabel(state, formula, true);
        }

        if (formula instanceof False) {
            return addLabel(state, formula, false);
        }

        if (formula instanceof Atom) {
            return addLabel(state, formula, state.satisfies((Atom) formula));
        }

        if (formula instanceof Or) {
            for (Formula subFormula: ((Or) formula).getOperands()) {
                if (satisfies(state, subFormula)) {
                    return addLabel(state, formula, true);
                }
            }

            return addLabel(state, formula, false);
        }
        
        if (formula instanceof And) {
            for (Formula subFormula: ((And) formula).getOperands()) {
                if (!satisfies(state, subFormula)) {
                    return addLabel(state, formula, false);
                }
            }

            return addLabel(state, formula, true);
        }
        
        if (formula instanceof Not) {
            return addLabel(state, formula, !satisfies(state, ((Not) formula).getOperand()));
        }
      //delete
        if (formula instanceof EX) {
            Formula subFormula = ((EX) formula).getOperand();
            for (State successorState: kripkeStructure.getAllSuccessorStates(state)) {
                if (satisfies(successorState, subFormula)) {
                    return addLabel(state, formula, true);
                }
            }

            return addLabel(state, formula, false);
        }
        //delete
        if (formula instanceof EU) {
            CheckEUResult checkEUResult = checkEU(state, (EU) formula);
            boolean isFormulaSatisfied = getLabel(state, formula).get();
            if (isFormulaSatisfied) {
                
            } else {
               
            }
            return isFormulaSatisfied;
        }


        throw new IllegalArgumentException(formula.toString());
    }

    private CheckEUResult checkEU(State state, EU formula) {
        BDDsearchData dfsData = new BDDsearchData();
        return checkEU(state, formula, dfsData);
    }

    private CheckEUResult checkEU(State state, EU formula, BDDsearchData dfsData) {
        Formula op1 = formula.getOperand1();
        Formula op2 = formula.getOperand2();

        Optional<Boolean> label = getLabel(state, formula);
        if (label.isPresent()) {
            if (label.get()) {
                return new CheckEUResult(ABORT, state);
            } else {
                return new CheckEUResult(CONTINUE, null);
            }
        }

        if (satisfies(state, op2)) {
            addLabel(state, formula, true);
            return new CheckEUResult(ABORT, state);
        }

        if (!satisfies(state, op1)) {
            addLabel(state, formula, false);
            return new CheckEUResult(CONTINUE, null);
        }

        addLabel(state, formula, true);
        dfsData.visit(state);
        
        for (State successorState: kripkeStructure.getAllSuccessorStates(state)) {
          
            if (!dfsData.isVisited(successorState)) {
               
                CheckEUResult checkEUResult = checkEU(successorState, formula, dfsData);
                if (checkEUResult.getSearchContinuation() == ABORT) {
                    checkEUResult.prependWitnessPathWith(state);
                    return checkEUResult;
                }
                dfsData.setLowLink(state, Math.min(dfsData.getLowLink(state), dfsData.getLowLink(successorState)));
            } else {
               
                if (dfsData.isOnStack(successorState)) {
                    dfsData.setLowLink(state, Math.min(dfsData.getLowLink(state), dfsData.getDfs(successorState)));
                }
            }
        }

        if (dfsData.dfsEqualsLowLink(state)) {
          
            State stateFromStack;
            do {
                stateFromStack = dfsData.popFromStack();
                addLabel(stateFromStack, formula, false);
            } while (!state.equals(stateFromStack));
        }

        return new CheckEUResult(CONTINUE, null);
    }


    private CheckAUResult checkAU(State state, EX formula, BDDsearchData dfsData) {

        dfsData.visit(state);
      
        Optional<Boolean> label = getLabel(state, formula);
        if (label.isPresent()) {
            if (label.get()) {
                return new CheckAUResult(CONTINUE, null);
            } else {
                return new CheckAUResult(ABORT, state);
            }
        }

        addLabel(state, formula, false);
        for (State successorState : kripkeStructure.getAllSuccessorStates(state)) {
          
            if (!dfsData.isVisited(successorState)) {
                
                CheckAUResult checkAUResult = checkAU(successorState, formula, dfsData);
                if (checkAUResult.getSearchContinuation() == ABORT) {
                    checkAUResult.prependCounterExampleWith(state);
                    return checkAUResult;
                }
            } else {
               
                if (dfsData.isOnStack(successorState)) {
                   
                    return new CheckAUResult(ABORT, successorState);
                }
            }
        }

        dfsData.removeFromStack(state);
        addLabel(state, formula, true);
        return new CheckAUResult(CONTINUE, null);
    }

}
