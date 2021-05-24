package centroVacunacion;

public abstract class VacunaCovid19 {
    private String name;
    private int storeTemperature;
    private boolean exclusivaMayores60;
    private Fecha fechaIngreso;
    
    public VacunaCovid19(Fecha fechaIngreso) {
		super();
		this.setFechaIngreso(fechaIngreso);
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStoreTemperature() {
        return storeTemperature;
    }

    public void setStoreTemperature(int storeTemperature) {
        this.storeTemperature = storeTemperature;
    }
    
	public Fecha getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Fecha fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

    public boolean isExclusivaMayores60() {
        return exclusivaMayores60;
    }

    public void setExclusivaMayores60(boolean exclusivaMayores60) {
        this.exclusivaMayores60 = exclusivaMayores60;
    }

    @Override
    public String toString() {
        return   "Vacuna : '" + name + '\'' +
                ", Temperatura de almacenaje : " + storeTemperature;
    }




}
