package model;

public class VarBoolName {

	private String varName = "";
	private String varNamet = "";
	private String varNamef = "";
	
	private boolean varCondition;

	public VarBoolName(String varName, String varNamet, String varNamef) {
		this.varName = varName;
		this.varNamet = varNamet;
		this.varNamef = varNamef;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public String getVarNamet() {
		return varNamet;
	}

	public void setVarNamet(String varNamet) {
		this.varNamet = varNamet;
	}

	public String getVarNamef() {
		return varNamef;
	}

	public void setVarNamef(String varNamef) {
		this.varNamef = varNamef;
	}

	public boolean isVarCondition() {
		return varCondition;
	}

	public void VarCondition(boolean varCondition) {
		this.varCondition = varCondition;
	}

}
