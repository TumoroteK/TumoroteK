package fr.aphp.tumorotek.model.coeur.patient;

public class DuoPatients extends Object{

	
	private Patient p1;
	private Patient p2;
	
	public DuoPatients(){
	}
	
	public DuoPatients(Patient pA, Patient pB){
		this.p1=pA;
		this.p2=pB;
	}
	
	
	
	
	public Patient getP1() {
		return p1;
	}
	public Patient getP2() {
		return p2;
	}
	public void setP1(Patient p1) {
		this.p1 = p1;
	}
	public void setP2(Patient p2) {
		this.p2 = p2;
	}
	
	
	
	
	
}
