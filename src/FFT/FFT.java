package FFT;

public class FFT {
	
	public static Complex[] fft(Complex[] p) {
		int n = p.length;
		
		if(n == 1) return new Complex[] {p[0]};
		
		if(Math.log(n) / Math.log(2) != (int)(Math.log(n) / Math.log(2))) 
			throw new IllegalArgumentException("n is not a power of 2");
		
		Complex[] even = new Complex[n/2];
		for(int k = 0; k < n/2; ++k) {
			even[k] = p[2*k];
		}
		Complex[] evenFFT = fft(even);
		
		Complex[] odd = new Complex[n/2];
		for(int k = 0; k < n/2; ++k) {
			odd[k] = p[2*k + 1];
		}
		Complex[] oddFFT = fft(odd);
		
		Complex[] y = new Complex[n];
		for(int k = 0; k < n/2; ++k) {
			double kth = 2*k*Math.PI / n;
			Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
			y[k] = evenFFT[k].plus(wk.times(oddFFT[k]));
			y[k + n/2] = evenFFT[k].minus(wk.times(oddFFT[k]));
		}
		return y;
	}
	public static Complex[] ifft(Complex[] p) {
		int n = p.length;
		Complex[] y = new Complex[n];
		for(int i = 0; i < n; ++i) {
			y[i] = p[i].conjugate();
		}
		y = fft(y);
		for(int i = 0; i < n; ++i) {
			y[i] = y[i].conjugate();
			y[i] = y[i].scale(1.0 / n);
		}
		return y;
	}
	public static Complex[][] fftshift(Complex[][] p) {
		int n = p.length;
		for(int i = 0; i < n/2; ++i) {
			for(int j = 0; j < n/2; ++j) {
				Complex temp = p[i][j];
				p[i][j] = p[i+n/2][j+n/2];
				p[i+n/2][j+n/2] = temp;
				temp = p[i][j+n/2];
				p[i][j+n/2] = p[i+n/2][j];
				p[i+n/2][j]=temp;
			}
		}
		return p;
	}
}
