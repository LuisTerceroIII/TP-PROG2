package centroVacunacion.vacunas;

import centroVacunacion.Fecha;
import centroVacunacion.VacunaCovid19;

public class Astrazeneca extends VacunaCovid19 {

        
	public Astrazeneca( Fecha fechaIngreso) {
		super(fechaIngreso);
		super.setName("AstraZeneca");
		super.setExclusivaMayores60(false);
		super.setStoreTemperature(3);
	}
    
    
}
