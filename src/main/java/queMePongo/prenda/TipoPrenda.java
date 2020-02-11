package queMePongo.prenda;

import queMePongo.usuario.Usuario;

public enum TipoPrenda {
	CAMISA(Categoria.SUPERIOR, "Camisa"){		
		@Override
		public boolean puedeSerDeMaterial(Material material) {
			return material == Material.SEDA;
		}
		@Override
		public boolean sePuedeUsarATemperatura(Usuario usuario, double temperatura) {
			return true;
		}
		@Override
		public boolean esPrendaSuperiorBasica() {
			return true;
		}
	},
	CAMPERA(Categoria.SUPERIOR, "Campera"){
		@Override
		public boolean puedeSerDeMaterial(Material material) {
			return material == Material.CUERO;
		}
		@Override
		public boolean sePuedeUsarATemperatura(Usuario usuario, double temperatura) {
			return temperatura <= 16 * usuario.getPorcentajeExtraSensibilidad(this.categoria);
		}
	},
	PANTALON(Categoria.INFERIOR, "Pantalón"){
		@Override
		public boolean puedeSerDeMaterial(Material material) {
			return material == Material.CUERO || material == Material.ALGODON;
		}

		@Override
		public boolean sePuedeUsarATemperatura(Usuario usuario, double temperatura) {
			return true;
		}
	},
	CALZATERMICA(Categoria.INFERIOR, "Calza térmica"){
		@Override
		public boolean puedeSerDeMaterial(Material material) {
			return material == Material.SUPPLEX;
		}
		
		@Override
		public boolean sePuedeUsarATemperatura(Usuario usuario, double temperatura) {
			return temperatura <= 10 * usuario.getPorcentajeExtraSensibilidad(this.categoria);
		}
	},
	SHORT(Categoria.INFERIOR, "Short"){
		@Override
		public boolean puedeSerDeMaterial(Material material) {
			return material == Material.POLIESTER;
		}
		
		@Override
		public boolean sePuedeUsarATemperatura(Usuario usuario, double temperatura) {
			return temperatura >= 20 * usuario.getPorcentajeExtraSensibilidad(this.categoria);
		}
	},
	ZAPATILLAS(Categoria.CALZADO,"Zapatillas"){
		@Override
		public boolean puedeSerDeMaterial(Material material) {
			return material == Material.CUERO;
		}
		
		@Override
		public boolean sePuedeUsarATemperatura(Usuario usuario, double temperatura) {
			return true;
		}
	},
	BOTAS(Categoria.CALZADO, "Botas"){
		@Override
		public boolean puedeSerDeMaterial(Material material) {
			return material == Material.CUERO;
		}
		
		@Override
		public boolean sePuedeUsarATemperatura(Usuario usuario, double temperatura) {
			return temperatura <= 15 * usuario.getPorcentajeExtraSensibilidad(this.categoria);
		}
	},
	ZAPATOS(Categoria.CALZADO, "Zapatos"){
		@Override
		public boolean puedeSerDeMaterial(Material material) {
			return material == Material.CUERO;
		}
		
		@Override
		public boolean sePuedeUsarATemperatura(Usuario usuario, double temperatura) {
			return true;
		}
	},
	PANUELO(Categoria.CUELLO, "Pañuelo"){
		@Override
		public boolean puedeSerDeMaterial(Material material) {
			return material == Material.NAILON;
		}

		@Override
		public boolean sePuedeUsarATemperatura(Usuario usuario, double temperatura) {
			return temperatura <= 20 * usuario.getPorcentajeExtraSensibilidad(this.categoria);
		}
	},
	GORRA(Categoria.CABEZA, "Gorra"){
		@Override
		public boolean puedeSerDeMaterial(Material material) {
			return material == Material.POLIESTER;
		}
		
		@Override
		public boolean sePuedeUsarATemperatura(Usuario usuario, double temperatura) {
			return true;
		}
	},
	REMERA(Categoria.SUPERIOR, "Remera"){
		@Override
		public boolean puedeSerDeMaterial(Material material) {
			return material == Material.ALGODON || material == Material.POLIESTER;
		}
		
		@Override
		public boolean sePuedeUsarATemperatura(Usuario usuario, double temperatura) {
			return true;
		}

		@Override
		public boolean esPrendaSuperiorBasica() {
			return true;
		}
	},
	LENTES(Categoria.ACCESORIO, "Lentes"){
		@Override
		public boolean puedeSerDeMaterial(Material material) {
			return material == Material.PLASTICO;
		}
		
		@Override
		public boolean sePuedeUsarATemperatura(Usuario usuario, double temperatura) {
			return true;
		}
	},
	GUANTES(Categoria.MANOS, "Guantes"){
		@Override
		public boolean puedeSerDeMaterial(Material material) {
			return material == Material.ALGODON;
		}
		@Override
		public boolean sePuedeUsarATemperatura(Usuario usuario, double temperatura) {
			return temperatura <= 15 * usuario.getPorcentajeExtraSensibilidad(this.categoria);
		}
	};
	
	protected final String nombre;

	protected final Categoria categoria;

	private TipoPrenda(final Categoria categoria, final String nombre) {
		this.categoria = categoria;
		this.nombre = nombre;
	}

	public boolean esPrendaSuperiorBasica() {
		return false;
	}

	public abstract boolean puedeSerDeMaterial(Material material);

	public Categoria getCategoria() {
		return this.categoria;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public abstract boolean sePuedeUsarATemperatura(Usuario usuario, double temperatura);
	
}
