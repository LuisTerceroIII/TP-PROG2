package centroVacunacion;

import java.util.ArrayList;
/*
 * vacunasEnStock.size() + vacunasReservadas.size() == frigorificoA.getEspaciosOcupados() + frigorificoB.getEspaciosOcupados() 
 * 
 * */
public class DepositoVacunas {
	
	 	private ArrayList<VacunaCovid19> vacunasEnStock;
	    private ArrayList<VacunaCovid19> vacunasReservadas;
	    private ArrayList<VacunaCovid19> vacunasAplicadas;
	    private Frigorifico frigorificoA;// 3 grados
	    private Frigorifico frigorificoB;// -18 grados
	    
	    public DepositoVacunas(Frigorifico fA, Frigorifico fB) {
	    	this.vacunasEnStock = new ArrayList<>();
	    	this.vacunasReservadas = new ArrayList<>();
	    	this.vacunasAplicadas = new ArrayList<>();
	    	this.frigorificoA = fA;
            this.frigorificoB = fB;
	    }
	    
	    public void almacenar(String vacuna) {
	    	String nombreVacuna = vacuna.toUpperCase();
	    	switch (nombreVacuna) {
			case "ASTRAZENECA":
				this.frigorificoA.almacenar();
				break;
			case "MODERNA":
				this.frigorificoB.almacenar();
				break;
			case "PFIZER":
				this.frigorificoB.almacenar();
				break;
			case "SINOPHARM":
				this.frigorificoA.almacenar();
				break;
			case "SPUTNIK":
				this.frigorificoA.almacenar();
				break;
			default:
				throw new RuntimeException("Vacuna no se reconoce");
			}    
	    }

		//Libera un espacio en el frigorifico correspondiente
		public  void liberarUnEspacioFrigorifico(VacunaCovid19 vacuna) {
			if(vacuna.getTemperaturaAlmacenaje() == frigorificoA.getTemperaturaAlamacenamiento()) {
				frigorificoA.liberarUnEspacio();
			} else if(vacuna.getTemperaturaAlmacenaje() == frigorificoB.getTemperaturaAlamacenamiento()) {
				frigorificoB.liberarUnEspacio();
			}
		}
		public ArrayList<VacunaCovid19> getVacunasEnStock() {
			return vacunasEnStock;
		}

		public ArrayList<VacunaCovid19> getVacunasReservadas() {
			return vacunasReservadas;
		}

		public ArrayList<VacunaCovid19> getVacunasAplicadas() {
			return vacunasAplicadas;
		}

		public Frigorifico getFrigorifico3Grados() {
			return frigorificoA;
		}

		public Frigorifico getFrigorificoMenos18Grados() {
			return frigorificoB;
		}

}
