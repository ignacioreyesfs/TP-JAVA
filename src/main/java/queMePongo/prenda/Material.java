package queMePongo.prenda;

public enum Material {
	ALGODON("Algodón"), 
	NAILON("Nailon"), 
	POLIESTER("Poliester"), 
	PLASTICO("Plástico"), 
	CUERO("Cuero"), 
	SUPPLEX("Supplex"), 
	SEDA("Seda");
	
	protected final String nombre;

	private Material(final String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return this.nombre;
	}
}
