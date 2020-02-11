package queMePongo.prenda;

public enum NivelDeAbrigo {
	BAJO{
		@Override
		public double ajustePorcentajeAbrigoExtra() {
			return 0.1;
		}
	},
	PERFECTO{
		@Override
		public double ajustePorcentajeAbrigoExtra() {
			return 0;
		}
	},
	ALTO{
		@Override
		public double ajustePorcentajeAbrigoExtra() {
			return -0.1;
		}
	};
		
	public abstract double ajustePorcentajeAbrigoExtra();
}
