package centroVacunacion.vacunas;

import centroVacunacion.VacunaCovid19;

/*
IREP:
	temperaturaAlmacenaje: temperaturaAlmacenaje == 3
	exclusivaMayores60 : exclusivaMayores60 == true
 * */
public class SputnikV extends VacunaCovid19 {

	public SputnikV() {
		super.setNombre("Sputnik");
		super.setExclusivaMayores60(true);
		setTemperaturaAlmacenaje(3);
	}
	@Override
	public void setTemperaturaAlmacenaje(int temperatura) {
		if(temperatura != 3) throw new RuntimeException("Sputnik V debe almacenarse a 3 grados");
		this.temperaturaAlmacenaje = temperatura;
	}
	
}
