public class Polynomial {
	double[] coefficients;
	Polynomial() {
		this.coefficients = new double[1];	
	}
	Polynomial(double[] coefficients) {
		this.coefficients = coefficients;
	}
    public Polynomial add(Polynomial p) {
        int len = Math.max(this.coefficients.length, p.coefficients.length);
        double[] new_coeffs = new double[len];
        for(int i = 0; i < len; i++) {
            if(i < this.coefficients.length) new_coeffs[i] += this.coefficients[i];
            if(i < p.coefficients.length) new_coeffs[i] += p.coefficients[i];
        }
        return new Polynomial(new_coeffs);
    }
    public double evaluate(double x) {
        double sum = 0;
        for(int i = 0; i < coefficients.length; i++) {
            sum += coefficients[i] * Math.pow(x, i);
        } 
        return sum;
    }

    boolean hasRoot(double x) {
        if(evaluate(x) == 0) return true;
        return false;
    }
}