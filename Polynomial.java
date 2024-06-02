import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Polynomial {
	double[] coefficients;
	int[] exponents;
	Polynomial() {
		this.coefficients = new double[1];	
        this.exponents = new int[1];
	}
	Polynomial(double[] coefficients, int[] exponents) {
		this.coefficients = coefficients;
		this.exponents = exponents;
	}
    Polynomial(File file) throws Exception {

        BufferedReader input = new BufferedReader(new FileReader(file)); 
        String eq = input.readLine();
        
        int len = eq.split("\\+|\\-").length;
        this.coefficients = new double[len];
        this.exponents= new int[len];

        String[] split_plus = eq.split("\\+");

        int index = 0;
        for(String exp: split_plus) {
            String[] split_minus = exp.split("\\-");
            String[] coeff_exp_pair = split_minus[0].split("x");
            if(coeff_exp_pair.length == 2) {
                if(coeff_exp_pair[1].equals("")) this.exponents[index] = 1;
                else this.exponents[index] =  Integer.parseInt(coeff_exp_pair[1]);
            }
            this.coefficients[index++] = Double.parseDouble(coeff_exp_pair[0]);
            for(int i = 1; i < split_minus.length; i++) {
                coeff_exp_pair = split_minus[i].split("x");
                if(coeff_exp_pair.length == 2) {
                    if(coeff_exp_pair[1].equals("")) this.exponents[index] = 1;
                    else this.exponents[index] =  Integer.parseInt(coeff_exp_pair[1]);
                }
                this.coefficients[index++] = -Double.parseDouble(coeff_exp_pair[0]);
            }
        }
    }
    public void saveToFile(String file_name) throws IOException {
        FileWriter writer = new FileWriter(file_name);
        String out_str = "";
        int len = coefficients.length;
        if(coefficients[0] < 0) out_str += coefficients[0];
        else out_str += coefficients[0];
        if(exponents[0] != 0) out_str += "x" + exponents[0];
        for(int i = 1; i < len; i++) {
            if(coefficients[i] == 0) continue;
            else if(coefficients[i] < 0) out_str += coefficients[i];
            else out_str += "+" + coefficients[i];
            if(exponents[i] != 0) out_str += "x" + exponents[i];
        }
        writer.write(out_str);
        writer.close();

    }

    public Polynomial add(Polynomial p) {
        int[] exp_link = new int[p.coefficients.length]; // array for link exponents from p to exponent from this.polynoimal
        int new_len = coefficients.length + p.coefficients.length; 
        for(int i = 0; i < p.coefficients.length; i++) { // set up case for no links
            exp_link[i] = -1;
        }
        for(int i = 0; i < p.exponents.length; i++) { // linking exponents
            for(int j = 0; j < exponents.length; j++) {
                if(exponents[j] == p.exponents[i]) {
                    exp_link[i] = j;
                    new_len--;
                    break;
                }
            }
        }
        double[] new_coeffs = new double[new_len];
        int[] new_exp = new int[new_len];

        for(int i = 0; i < exponents.length; i++) { // copy first array to new
            new_coeffs[i] = coefficients[i];           
            new_exp[i] = exponents[i];
        }
        int num_new_exp = 0;
        for(int i = 0; i < p.exponents.length; i++) {
            if(exp_link[i] == -1) {
                new_exp[exponents.length + num_new_exp] = p.exponents[i]; 
                new_coeffs[exponents.length + num_new_exp] = p.coefficients[i];
                num_new_exp++;
            }
            else new_coeffs[exp_link[i]] += p.coefficients[i];
        }
        //get rid of zeroes
        int zero_coeff = 0;
        for(int i = 0; i < new_len; i++) { //get number of zero coeffs
            if(new_coeffs[i] == 0) zero_coeff++;
        }
        double[] no_zero_coeffs = new double[new_len - zero_coeff];
        int[] no_zero_exp = new int[new_len - zero_coeff];
        int i = 0;
        int j = 0;
        while(i < new_len - zero_coeff) {
            if(new_coeffs[j] == 0) {
                j++;
                continue;
            }
            no_zero_coeffs[i] = new_coeffs[j];
            no_zero_exp[i] = new_exp[j];
            j++;
            i++;
        }

        return new Polynomial(no_zero_coeffs, no_zero_exp);
    }
    public double evaluate(double x) {
        double sum = 0;
        for(int i = 0; i < coefficients.length; i++) {
            sum += coefficients[i] * Math.pow(x, exponents[i]);
        } 
        return sum;
    }
    public Polynomial multiply(Polynomial p) {
        Polynomial new_poly = new Polynomial();
        
        for(int i = 0; i < exponents.length; i++) {
            int[] temp_exp = new int[p.exponents.length];
            double[] temp_coeffs = new double[p.exponents.length];
            for(int j = 0; j < p.exponents.length; j++) {
                temp_exp[j] = exponents[i] + p.exponents[j];
                temp_coeffs[j] = coefficients[i] * p.coefficients[j];
            }
            new_poly = new_poly.add(new Polynomial(temp_coeffs, temp_exp));
        }
        return new_poly;
    }

    boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }
}