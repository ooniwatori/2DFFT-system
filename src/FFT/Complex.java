package FFT;
/* Define complex number */
public class Complex {
	/* real part, imaginary part */
	private final double re;
	private final double im;
	/* Constructor */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}
	/* For test, show complex number in String */
	public String toString() {
		if(im == 0) return re + "";
		if(re == 0) return im + "i";
		if(im < 0) return re + "-" + (-im) + "i";
		return re + "+" + im + "i";
	}
	/* Define Addition */
	public Complex plus(Complex b) {
		double re = this.re + b.getReal();
		double im = this.im + b.getImage();
		return new Complex(re, im);
	}
	/* Define Substraction */
	public Complex minus(Complex b) {
		double re = this.re - b.getReal();
		double im = this.im - b.getImage();
		return new Complex(re, im);
	}
	/* Define Mutiplication*/
	public Complex times(Complex b) {
		double re = this.re*b.getReal() - (this.im*b.getImage());
		double im = this.re*b.getImage() + (this.im*b.getReal());
		return new Complex(re, im);
	}
	/* Define Conjugate */
	public Complex conjugate() {
		return new Complex(re, -im);
	}
	/* Define Scaling multiplication */
	public Complex scale(double a) {
		return new Complex(a*re, a*im);
	}
	/* Magnitude of complex number */
	public double magnitude() {
		return Math.sqrt(Math.pow(re, 2) + Math.pow(im, 2));
	}
	/* Angle of complex number*/
	public double getAngle() {
		return Math.atan(im/re);
	}
	/* Get real part */
	public double getReal() {
		return this.re;
	}
	/* Get imaginary part */
	public double getImage() {
		return this.im;
	}
}
