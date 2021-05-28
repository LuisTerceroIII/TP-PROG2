package centroVacunacion;

public class Frigorifico {
	
	private int temperaturaAlamacenamiento;
	private int capacidadMaximaAlmacenamiento;
	private int espaciosOcupados;
	
	public Frigorifico(int temperaturaAlamacenamiento, int capacidadMaximaAlmacenamiento) {
		this.temperaturaAlamacenamiento = temperaturaAlamacenamiento;
		this.capacidadMaximaAlmacenamiento = capacidadMaximaAlmacenamiento;
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
		this.temperaturaAlamacenamiento = temperaturaAlamacenamiento;
	}

	public int getCapacidadMaximaAlmacenamiento() {
		return capacidadMaximaAlmacenamiento;
	}

	public void setCapacidadMaximaAlmacenamiento(int capacidadMaximaAlmacenamiento) {
		this.capacidadMaximaAlmacenamiento = capacidadMaximaAlmacenamiento;
	}

	@Override
	public String toString() {
		return "Frigorifico [ Temperatura de almacenamiento  : " + temperaturaAlamacenamiento+ " grados "
				+ ", Capacidad Maxima : " + capacidadMaximaAlmacenamiento + "]";
	}

}
