package application;
import java.awt.image.BufferedImage;

import FFT.*;
import java.awt.Color;
/* Implement 2DFFT for Image data */
public class ImageFFT {
	/* Define height ,width, original height, original width, image datas stored by matrix, BufferedImage datas */
	private int h;
	private int w;
	private int hOrigin;
	private int wOrigin;
	private Complex[][] originalImageMatrix;
	private Complex[][] imageFFT1Matrix;
	private Complex[][] imageFFT2Matrix;
	private BufferedImage originalImage;
	private BufferedImage imageFFT1M;
	private BufferedImage imageFFT1P;
	private BufferedImage imageFFT2M;
	private BufferedImage imageFFT2P;
	/* Filter matrix and processed matrix, BufferedImage datas*/
	private Complex[][] filter;
	private Complex[][] processedFFT2Matrix;
	private Complex[][] processedFFT1Matrix;
	private Complex[][] processedMatrix;
	private BufferedImage processedFFT2;
	private BufferedImage processedImage;
	private BufferedImage filterImage;
	
	/* Do not use this Constructor */
	public ImageFFT() {
		
	}
	/* Constructor initialized by initial image data */
	public ImageFFT(BufferedImage image) {
		/* Pass each property */
		originalImage = image;
		hOrigin = image.getHeight();
		wOrigin = image.getWidth();
		/* FFT only process data with length of 2^k, so normalize length */
		h = (int)Math.pow(2, get2Pow(hOrigin));
		w = (int)Math.pow(2, get2Pow(wOrigin));
		/* Make matrix data to a square */
		if(h > w) {
			w = h;
		}
		else {
			h = w;
		}
		/* Initialize all sizes of matrix and image datas*/
		init();
		/* 2DFFT only can process gray scale image, here use red scale to process */
		for(int i = 0; i < h; ++i) {
			for(int j = 0; j < w; ++j) {
				try {
					decodeRGB(new Color(image.getRGB(j, i)), i, j);
				}
				catch(Exception e) {
					decodeRGB(new Color(0), i, j);
				}
			}
		}
		/* 2DFFT */
		fft2d();
		imageTransform();
	}
	/* Initialize filter matrix, use omega0, omega1 for parameter, type for fileter type*/
	public void filterDesign(int omega0, int omega1, int type) {
		switch(type) {
		/* Low pass filter, implement by circle with value 1 pixel */
		case 1:
			for(int i = -h/2; i < h/2; ++i) {
				for(int j = -w/2; j < w/2; ++j) {
					if(Math.sqrt(Math.pow((double)i, 2.0) + Math.pow((double)j, 2.0)) < (h/2)*(1.0/omega0))
						filter[i+h/2][j+w/2] = new Complex(1, 0);
					else
						filter[i+h/2][j+w/2] = new Complex(0, 0);
				}
			}
			break;
		/* High pass filter, implement by donuts with value 1 pixel*/
		case 2:
			for(int i = -h/2; i < h/2; ++i) {
				for(int j = -w/2; j < w/2; ++j) {
					if(Math.sqrt(Math.pow((double)i, 2.0) + Math.pow((double)j, 2.0)) > (h/2)*(1.0/omega0))
						filter[i+h/2][j+w/2] = new Complex(1, 0);
					else
						filter[i+h/2][j+w/2] = new Complex(0, 0);
				}
			}
			break;
		/* Band pass filter */
		case 3:
			for(int i = -h/2; i < h/2; ++i) {
				for(int j = -w/2; j < w/2; ++j) {
					if(Math.sqrt(Math.pow((double)i, 2.0) + Math.pow((double)j, 2.0)) > (h/2)*(1.0/omega0) && 
							Math.sqrt(Math.pow((double)i, 2.0) + Math.pow((double)j, 2.0)) < (h/2)*(1.0/omega1))
						filter[i+h/2][j+w/2] = new Complex(1, 0);
					else
						filter[i+h/2][j+w/2] = new Complex(0, 0);
				}
			}
			break;
		/* Band reject filter*/
		case 4:
			for(int i = -h/2; i < h/2; ++i) {
				for(int j = -w/2; j < w/2; ++j) {
					if(Math.sqrt(Math.pow((double)i, 2.0) + Math.pow((double)j, 2.0)) > (h/2)*(1.0/omega0) && 
							Math.sqrt(Math.pow((double)i, 2.0) + Math.pow((double)j, 2.0)) < (h/2)*(1.0/omega1))
						filter[i+h/2][j+w/2] = new Complex(0, 0);
					else
						filter[i+h/2][j+w/2] = new Complex(1, 0);
				}
			}
			break;
		}
	}
	
	/* Filter applying, by multiplication image data and filter in frequecy domain */
	public void filterApply(int omega0, int omega1, int type) {
		filterDesign(omega0, omega1, type);
		for(int i = 0; i < h; ++i) {
			for(int j = 0; j < w; ++j) {
				processedFFT2Matrix[i][j] = imageFFT2Matrix[i][j].times(filter[i][j]);
				
			}
		}
		/* Inverse 2DFFT on image data*/
		ifft2d();
		inverseImageTransform();
	}
	
	
	
	/* log2(n) */
	public int get2Pow(int n) {
		return (int)Math.ceil(Math.log(n)/Math.log(2));
	}
	/* Initial all matrix and Image datas to adjustde size */
	public void init() {
		originalImageMatrix = new Complex[h][w];
		imageFFT1Matrix = new Complex[h][w];
		imageFFT2Matrix = new Complex[h][w];
		imageFFT1M = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		imageFFT1P = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		imageFFT2M = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		imageFFT2P = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		
		processedMatrix = new Complex[h][w];
		processedFFT1Matrix = new Complex[h][w];
		processedFFT2Matrix = new Complex[h][w];
		processedFFT2 = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		processedImage= new BufferedImage(wOrigin, hOrigin, BufferedImage.TYPE_BYTE_GRAY);
		
		
		filter = new Complex[h][w];
		filterImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		
	}
	/* Get red scale of image */
	public void decodeRGB(Color c, int i, int j) {
		originalImageMatrix[i][j] = new Complex(c.getRed(), 0);
	}
	
	/* 2DFFT: do FFT on row data then do FFT on column data */
	public void fft2d() {
		fft1();
		fft2();
	}
	/* FFT row data */
	public void fft1() {
		for(int i = 0; i < h; ++i) {
			imageFFT1Matrix[i] = FFT.fft(originalImageMatrix[i]);
		}
	}
	/* FFT column data */
	public void fft2() {
		Complex[][] Temp = transp(imageFFT1Matrix, h, w);
		for(int i = 0; i < h; ++i) {
			imageFFT2Matrix[i] = FFT.fft(Temp[i]);
		}
		imageFFT2Matrix = transp(imageFFT2Matrix, h, w);
		imageFFT2Matrix = FFT.fftshift(imageFFT2Matrix);
	}
	/* Inverse 2DFFT*/
	public void ifft2d() {
		ifft2();
		ifft1();
	}
	/* Inverse FFT row data */
	public void ifft1() {
		for(int i = 0; i < h; ++i) {
			processedMatrix[i] = FFT.ifft(processedFFT1Matrix[i]);
		}
	}
	/* Inverse FFT column data */
	public void ifft2() {
		processedFFT2Matrix = FFT.fftshift(processedFFT2Matrix);
		Complex[][] Temp = transp(processedFFT2Matrix, h, w);
		for(int i = 0; i < h; ++i) {
			processedFFT1Matrix[i] = FFT.ifft(Temp[i]);
		}
		processedFFT1Matrix = transp(processedFFT1Matrix, h, w);
		
	}
	/* Recreate a complex matrix of image matrix for analyzing */
	public Complex[][] transp(Complex[][] m, int h, int w) {
		Complex[][] c = new Complex[h][w];
		for(int i = 0; i < w; ++i) { 
			for(int j = 0; j < h; ++j) { 
				c[i][j] = new Complex(m[j][i].getReal(), m[j][i].getImage());
			}
		}
		return c;
	}
	public void imageTransform() {
		for(int i = 0; i < h; ++i) {
			for(int j = 0; j < w; ++j) {
				int r1m = bound((int)Math.round(18*Math.log(1+imageFFT1Matrix[i][j].magnitude())));
				int r1p = bound((int)Math.round(200*Math.log(1+imageFFT1Matrix[i][j].getAngle())));
				
				int r2m = bound((int)Math.round(18*Math.log(1+imageFFT2Matrix[i][j].magnitude())));
				int r2p = bound((int)Math.round(200*Math.log(1+imageFFT2Matrix[i][j].getAngle())));
				
				imageFFT1M.setRGB(j, i, new Color(r1m, r1m, r1m).getRGB());
				imageFFT1P.setRGB(j, i, new Color(r1p, r1p, r1p).getRGB());
				imageFFT2M.setRGB(j, i, new Color(r2m, r2m, r2m).getRGB());
				imageFFT2P.setRGB(j, i, new Color(r2p, r2p, r2p).getRGB());
			}
		}
	}
	
	/* For human visual, adjust pixels' value in a multipier scale, here also process the filter image */
	public void inverseImageTransform() {
		processedFFT2Matrix = FFT.fftshift(processedFFT2Matrix);
		for(int i = 0; i < h; ++i) {
			for(int j = 0; j < w; ++j) {
				int r2m = bound((int)Math.round(18*Math.log(1+processedFFT2Matrix[i][j].magnitude())));
				int fm = bound((int)(filter[i][j].magnitude())*255);

				processedFFT2.setRGB(j, i, new Color(r2m, r2m, r2m).getRGB());	
				filterImage.setRGB(j, i, new Color(fm, fm, fm).getRGB());	
			}
		}
		for(int i = 0; i < hOrigin; ++i) {
			for(int j = 0; j < wOrigin; ++j) {
				int rm = bound((int)Math.round(processedMatrix[i][j].magnitude()));
				processedImage.setRGB(j, i, new Color(rm, rm, rm).getRGB());
			}
		}
	}
	/* For human visual, adjust pixels' value in a multipier scale, here only process image data */
	public void inverseImageOnlyTrans() {
		/* Shifting first */
		processedFFT2Matrix = FFT.fftshift(processedFFT2Matrix);
		for(int i = 0; i < h; ++i) {
			for(int j = 0; j < w; ++j) {
				int r2m = bound((int)Math.round(18*Math.log(1+processedFFT2Matrix[i][j].magnitude())));

				processedFFT2.setRGB(j, i, new Color(r2m, r2m, r2m).getRGB());	
			}
		}
		/* Extract the magnitude value of each pixel */
		for(int i = 0; i < hOrigin; ++i) {
			for(int j = 0; j < wOrigin; ++j) {
				int rm = bound((int)Math.round(processedMatrix[i][j].magnitude()));
				processedImage.setRGB(j, i, new Color(rm, rm, rm).getRGB());
			}
		}
	}
	
	/* Testing, applying phase on different image, not be used */
	public void applyPhaseMix(double[][] angle) {
		for(int i = 0; i < h; ++i) {
			for(int j = 0; j < w; ++j) {
				processedFFT2Matrix[i][j] = new Complex(imageFFT2Matrix[i][j].magnitude()*Math.cos(angle[i][j]), 
						imageFFT2Matrix[i][j].magnitude()*Math.sin(angle[i][j]));
			}
		}
		ifft2d();
		inverseImageOnlyTrans();
	}
	/* Limit pixel scale in 255 */
	public int bound(int n) {
		if(n > 255)
			return 255;
		else if(n < 0)
			return 0;
		else
			return n;
	}
	/* Limit angle in pi */
	public int angleBound(int n) {
		return (int)Math.round(Math.abs(n%Math.PI));
	}

	/* Getter for Bufferimage data */
	public BufferedImage getImageFFT1Magnitude() {
		return imageFFT1M;
	}
	public BufferedImage getImageFFT2Magnitude() {
		return imageFFT2M;
	}
	
	public BufferedImage getImageFFT1Phase() {
		return imageFFT1P;
	}
	public BufferedImage getImageFFT2Phase() {
		return imageFFT2P;
	}
	public BufferedImage getOriginalImage() {
		return originalImage;
	}
	public BufferedImage getProcessedImage() {
		return processedImage;
	}
	public BufferedImage getProcessedFFT2() {
		return processedFFT2;
	}
	public BufferedImage getFilter() {
		return filterImage;
	}

	/* Get phase value of image matrix */
	public double[][] getPhase(){
		double[][] angle = new double[h][w];
		for(int i = 0; i < h; ++i) {
			for(int j = 0; j < w; ++j) {
				angle[i][j] = imageFFT2Matrix[i][j].getAngle();
			}
		}
		return angle;
	}
}
