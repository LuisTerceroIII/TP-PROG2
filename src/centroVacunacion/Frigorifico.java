package centroVacunacion;
/*
 * IREP:
 * -25 <= temperaturaAlamacenamiento <= 5
 * 10.000 <= capacidadMaximaAlmacenamiento <= 50.000
 * 0 <= espaciosOcupados <= capacidadMaximaAlmacenamiento
 * */
public class Frigorifico {
	
	private int temperaturaAlamacenamiento;
	private int capacidadMaximaAlmacenamiento;
	private int espaciosOcupados;
	
	public Frigorifico(int temperaturaAlamacenamiento, int capacidadMaximaAlmacenamiento) {
		this.setTemperaturaAlamacenamiento(temperaturaAlamacenamiento);
		this.setCapacidadMaximaAlmacenamiento(capacidadMaximaAlmacenamiento);
		this.espaciosOcupados = 0;
	}
	//Retorna verdadero si hay espacio para almacenar
	public boolean espacioLibre() {
		return capacidadMaximaAlmacenamiento - espaciosOcupados > 0;
	}

	public void almacenar() {
		if(espacioLibre()) {
			espaciosOcupados++;
		} else {
			throw new RuntimeException("No hay espacio en el frigorifico");
		}
	}

	public void liberarUnEspacio() {
		if(espaciosOcupados <= 0 ) {
			throw new RuntimeException("Frigorifico vacio");
		} else {
			espaciosOcupados--;
		}
	}

	public int getTemperaturaAlamacenamiento() {
		return temperaturaAlamacenamiento;
	}

	public void setTemperaturaAlamacenamiento(int temperaturaAlamacenamiento) {
		if(temperaturaAlamacenamiento < -25 || temperaturaAlamacenamiento > 5) {
			throw new RuntimeException("Temperatura invalida, intente entre -25 y 5 grados");
		}
		this.temperaturaAlamacenamiento = temperaturaAlamacenamiento;
	}

	public int getCapacidadMaximaAlmacenamiento() {
		return capacidadMaximaAlmacenamiento;
	}

	public void setCapacidadMaximaAlmacenamiento(int capacidadMaximaAlmacenamiento) {
		if(capacidadMaximaAlmacenamiento > 50000) {
			throw new RuntimeException("Capacidad maxima no disponible ; min : 10.000, max: 50.000");
		}
		if(capacidadMaximaAlmacenamiento < 10000) {
			throw new RuntimeException("Capacidad maxima no disponible ; min : 10.000, max: 50.000");
		}
		this.capacidadMaximaAlmacenamiento = capacidadMaximaAlmacenamiento;
	}
	
	public int getEspaciosOcupados() {
		return espaciosOcupados;
	}

	@Override
	public String toString() {
		return "Frigorifico [ Temperatura de almacenamiento  : " + temperaturaAlamacenamiento+ " grados "
				+ ", Capacidad Maxima : " + capacidadMaximaAlmacenamiento + "]";
	}

}
