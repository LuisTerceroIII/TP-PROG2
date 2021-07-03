package centroVacunacion;

/*
IREP:
	nombre: nombre.equals(“ASTRAZENECA”) || nombre.equals(“MODERNA”) ||
	 		nombre.equals(“PFIZER”) || nombre.equals(“SINOPHARM”) || nombre.equals(“SPUTNIK”)
	 		Puede ser minuscula o mayuscula.
	temperaturaAlmacenaje:  -18 <= temperaturaAlmacenaje <= 3 
	exclusivaMayores60 :  exclusivaMayores60 != null
 * */
public abstract class VacunaCovid19 {
    private String nombre;
    protected int temperaturaAlmacenaje;
    private boolean exclusivaMayores60;

	public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
    	if(nombre.equals("AstraZeneca") || nombre.equals("Moderna") || nombre.equals("Pfizer")
    		|| nombre.equals("Sinopharm") || nombre.equals("Sputnik") ) {
    		this.nombre = nombre;
    	} else throw new RuntimeException("Vacuna no valida!");
        
    }

    public int getTemperaturaAlmacenaje() {
        return temperaturaAlmacenaje;
    }

    public void setTemperaturaAlmacenaje(int temperaturaAlmacenaje) {
    	if(-18 <= temperaturaAlmacenaje && temperaturaAlmacenaje <= 3) {
    		this.temperaturaAlmacenaje = temperaturaAlmacenaje;
    	} else {
    		throw new RuntimeException("Temperatura de almacenaje no disponible");
    	}
        
    }
    
    public boolean isExclusivaMayores60() {
        return exclusivaMayores60;
    }

    public void setExclusivaMayores60(boolean exclusivaMayores60) {
        this.exclusivaMayores60 = exclusivaMayores60;
    }
    
	public boolean estaVencida() {
		return false;
	}


    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Vacuna Nombre : ").append(nombre)
    	.append(", Temperatura de almacenaje : ").append(temperaturaAlmacenaje);
        return sb.toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (exclusivaMayores60 ? 1231 : 1237);
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + temperaturaAlmacenaje;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VacunaCovid19 other = (VacunaCovid19) obj;
		if (exclusivaMayores60 != other.exclusivaMayores60)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (temperaturaAlmacenaje != other.temperaturaAlmacenaje)
			return false;
		return true;
	}

}
