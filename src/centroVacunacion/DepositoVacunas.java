package centroVacunacion;

import centroVacunacion.vacunas.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
IREP:
	vacunasEnStock: vacunasEnStock != null
	vacunasReservadas: vacunasReservadas != null
	vacunasAplicadas: vacunasAplicadas != null
	vacunasVencidas: vacunasVencidas.get("Moderna") >= 0 && vacunasVencidas.get("Pfizer") >= 0
	frigorificoA: frigorificoA != null && frigorificoA.getTemperaturaAlamacenamiento() == 3
	frigorificoB: frigorificoB != null && frigorificoA.getTemperaturaAlamacenamiento() == -18

 * */
public class DepositoVacunas {
	
	 	private ArrayList<VacunaCovid19> vacunasEnStock;
	    private ArrayList<VacunaCovid19> vacunasReservadas;
	    private ArrayList<VacunaCovid19> vacunasAplicadas;
	    private HashMap<String,Integer> vacunasVencidas; // Cantidad de vacunas vencidas en este vacunatorio.
	    private Frigorifico frigorificoA;// 3 grados
	    private Frigorifico frigorificoB;// -18 grados
	    
	    public DepositoVacunas() {
	    	this.vacunasEnStock = new ArrayList<>();
	    	this.vacunasReservadas = new ArrayList<>();
	    	this.vacunasAplicadas = new ArrayList<>();
	    	this.frigorificoA = new Frigorifico(3, 50000);
            this.frigorificoB = new Frigorifico(-18, 50000);
	    	this.vacunasVencidas = new HashMap<>();
	    	vacunasVencidas.put("Moderna", 0);
	    	vacunasVencidas.put("Pfizer", 0);
	    }
		public void ingresarSputnikV(int cantidad) {
			agregarVacunaAStock(new SputnikV());
			almacenar("SPUTNIK");
		}
		public void ingresarSinopharm(int cantidad) {
			agregarVacunaAStock(new Sinopharm());
			almacenar("SINOPHARM");
		}
		public void ingresarPfizer(int cantidad, Fecha fechaIngreso) {
			agregarVacunaAStock(new Pfizer(fechaIngreso));
			almacenar("PFIZER");
		}
		public void ingresarModerna(int cantidad, Fecha fechaIngreso) {
			agregarVacunaAStock(new Moderna(fechaIngreso));
			almacenar("MODERNA");
		}
		public void ingresarAstrazeneca(int cantidad) {
		agregarVacunaAStock(new Astrazeneca());
		almacenar("ASTRAZENECA");
	}
	
	    private  void almacenar(String vacuna) {
	    	String nombreVacuna = vacuna.toUpperCase();
	    	switch (nombreVacuna) {
			case "ASTRAZENECA":
			case "SINOPHARM":
			case "SPUTNIK":
				this.frigorificoA.almacenar();
				break;
			case "MODERNA":
			case "PFIZER":
				this.frigorificoB.almacenar();
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
		
		public void agregarVacunaAStock(VacunaCovid19 vacuna) {
			vacunasEnStock.add(vacuna);
		}
		//Se mantiene privada porque no quiero se puedan elimnar vacunas desde fuera, solo cuando se eliminan por vencidas.
		private void eliminarVacunaEnStock(Iterator<VacunaCovid19> itVacunas) { itVacunas.remove(); }
		public void agregarVacunaAReservadas(VacunaCovid19 vacuna) {
			vacunasReservadas.add(vacuna);
		}
		public void eliminarVacunaReservada(VacunaCovid19 vacuna) {
			vacunasReservadas.remove(vacuna);
		}
		public void agregarVacunaAAplicadas(VacunaCovid19 vacuna) {
			vacunasAplicadas.add(vacuna);
		}
		//Se suma en 1 la cantidad de vacunas vencidas. Cuenta llevada por "vacunasVencidas"
		public void registrarVacunaVencida(VacunaCovid19 vacuna) {
		Integer cantVencidas = vacunasVencidas.get(vacuna.getNombre());
		cantVencidas++;
		vacunasVencidas.put(vacuna.getNombre(), cantVencidas);
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
		public Map<String, Integer> getVacunasVencidas() {
			return vacunasVencidas;
		}
		public Frigorifico getFrigorificoA() {
			return frigorificoA;
		}
		public Frigorifico getFrigorificoB() {
			return frigorificoB;
		}

		public Iterator<VacunaCovid19> getIteratorVacunasEnStock() {
			return getVacunasEnStock().iterator();
		}
		public Iterator<VacunaCovid19> getIteratorVacunasReservadas() {
			return getVacunasReservadas().iterator();
		}
		public Iterator<VacunaCovid19> getIteratorVacunasAplicadas() {
		return getVacunasAplicadas().iterator();
	}
		
		public int cantidadVacunasEnStock() {
			return vacunasEnStock.size();
		}
		public void verificarVencimientoVacunas() {
			Iterator<VacunaCovid19> itVacunas = getIteratorVacunasEnStock();
			while(itVacunas.hasNext()) {
				VacunaCovid19 vacuna = itVacunas.next();
				if(vacuna.estaVencida()) {
					eliminarVacunaEnStock(itVacunas);
					registrarVacunaVencida(vacuna);
				}
			}
		}

}
