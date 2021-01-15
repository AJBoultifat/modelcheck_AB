package model;

import java.util.ArrayList;

public class ModelBoolVariable {

	KripkeStructure kripkeStructure = new KripkeStructure();

	ArrayList<VarBoolName> varsNames = new ArrayList<VarBoolName>();
	ArrayList<Operation> operations = new ArrayList<Operation>();
	ArrayList<VarBoolName> opBufferBefore = new ArrayList<VarBoolName>();
	ArrayList<VarBoolName> opBufferAfter = new ArrayList<VarBoolName>();
	ArrayList<State> states = new ArrayList<State>();

	public VarBoolName getBDDVariable(String variableName) {
		VarBoolName varboolname = null;
		for (int i = 0; i < varsNames.size(); i++) {
			if (varsNames.get(i).getVarName().equals(variableName)) {
				return varsNames.get(i);
			}
		}
		return varboolname;
	}

	public void CreateVariable(String... variableNames) {
		for (String variableName : variableNames) {
			if (!CheckExistingVariable(variableName)) {
				String variableNameT = variableName + "t";
				String variableNameF = variableName + "f";
				VarBoolName varboolname = new VarBoolName(variableName, variableNameT, variableNameF);
				varsNames.add(varboolname);
			}
		}

	}

	public void CreateModel() {
		ArrayList<String> statesNames = new ArrayList<String>();
		for (int i = 0; i < varsNames.size(); i++) {
			for (int j = 0; j < varsNames.size(); j++) {
				statesNames.add(varsNames.get(j).getVarNamet());
			}
			
			//State s1 = new State("s1", "a");
		}
	}

	public boolean CheckExistingVariable(String variableName) {
		for (int i = 0; i < varsNames.size(); i++) {
			if (varsNames.get(i).getVarName().equals(variableName)) {
				return true;
			}
		}
		return false;
	}

	public int AND(String variable2, boolean boolVar2, String variable3, boolean boolVar3) {

		VarBoolName var = getBDDVariable(variable2);
		opBufferBefore.add(var);
		var.VarCondition(boolVar2);
		opBufferAfter.add(var);

		VarBoolName var2 = getBDDVariable(variable3);
		opBufferBefore.add(var);
		var.VarCondition(boolVar3);
		opBufferAfter.add(var);

		return 0;
	}

	public int AND(String variable2, boolean boolVar2, int operation) {
		return 2;
	}

	public int OR(String variable2, boolean boolVar2, String variable3, boolean boolVar3) {

		VarBoolName var = getBDDVariable(variable2);
		opBufferBefore.add(var);
		var.VarCondition(boolVar2);
		opBufferAfter.add(var);

		VarBoolName var2 = getBDDVariable(variable3);
		opBufferBefore.add(var);
		var.VarCondition(boolVar3);
		opBufferAfter.add(var);
		return 1;
	}

	public int OR(String variable2, boolean boolVar2, int operation) {
		return 3;
	}

	public int OP(int boolOp) {
		return -1;
	}

	public int OP(String variable2, boolean boolVar2, boolean boolvar2) {
		return -2;
	}

	public void condition(String variable1, boolean boolVar1, int operation) {

		VarBoolName var = getBDDVariable(variable1);
		opBufferBefore.add(var);
		var.VarCondition(boolVar1);
		opBufferAfter.add(var);

	}

}
