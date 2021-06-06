package centroVacunacion;

import java.util.ArrayList;

public class Deposito {
	
	 	private ArrayList<VacunaCovid19> vacunasEnStock;
	    private ArrayList<VacunaCovid19> vacunasReservadas;
	    private ArrayList<VacunaCovid19> vacunasAplicadas;
	    private Frigorifico frigorificoA;// 3 grados
	    private Frigorifico frigorificoB;// -18 grados
	    
	    public Deposito() {
	    	this.vacunasEnStock = new ArrayList<>();
	    	this.vacunasReservadas = new ArrayList<>();
	    	this.vacunasAplicadas = new ArrayList<>();
	    	this.frigorificoA = new Frigorifico(3, 50000);
            this.frigorificoB = new Frigorifico(-18, 50000);
	    }
	    
	    public void almacenar(String vacuna) {
	    	String nombreVacuna = vacuna.toUpperCase();
	    	switch (nombreVacuna.toUpperCase()) {
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
			if(vacuna.getStoreTemperature() == 3) {
				frigorificoA.liberarUnEspacio();
			} else {
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
