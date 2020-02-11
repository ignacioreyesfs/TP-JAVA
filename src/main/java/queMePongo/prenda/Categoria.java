package queMePongo.prenda;

public enum Categoria {
	SUPERIOR{
		@Override
		public boolean admiteSuperposicion() {
		return true;
	}
},
	ACCESORIO,
	INFERIOR,	
	CALZADO,
	MANOS,
	CUELLO,
	CABEZA;
	
	public boolean admiteSuperposicion() {
		return false;
	}
}
