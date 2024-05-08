package FFT;

public class Complex {
	private final double re;
	private final double im;
	
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}
	public String toString() {
		if(im == 0) return re + "";
		if(re == 0) return im + "i";
		if(im < 0) return re + "-" + (-im) + "i";
		return re + "+" + im + "i";
	}
	public Complex plus(Complex b) {
		double re = this.re + b.getReal();
		double im = this.im + b.getImage();
		return new Complex(re, im);
	}
	public Complex minus(Complex b) {
		double re = this.re - b.getReal();
		double im = this.im - b.getImage();
		return new Complex(re, im);
	}
	public Complex times(Complex b) {
		double re = this.re*b.getReal() - (this.im*b.getImage());
		double im = this.re*b.getImage() + (this.im*b.getReal());
		return new Complex(re, im);
	}
	public Complex conjugate() {
		return new Complex(re, -im);
	}
	public Complex scale(double a) {
		return new Complex(a*re, a*im);
	}
	public double magnitude() {
		return Math.sqrt(Math.pow(re, 2) + Math.pow(im, 2));
	}
	public double getAngle() {
		return Math.atan(im/re);
	}
	public double getReal() {
		return this.re;
	}
	public double getImage() {
		return this.im;
	}
}
