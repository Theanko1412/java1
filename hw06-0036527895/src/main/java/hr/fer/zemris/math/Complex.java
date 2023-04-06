package hr.fer.zemris.math;

import java.util.List;

public class Complex {

    private double re;
    private double im;

    public static final Complex ZERO = new Complex(0,0);
    public static final Complex ONE = new Complex(1,0);
    public static final Complex ONE_NEG = new Complex(-1,0);
    public static final Complex IM = new Complex(0,1);
    public static final Complex IM_NEG = new Complex(0,-1);

    public Complex() {}

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    // returns module of complex number
    public double module() {
        return Math.sqrt((re*re)+(im*im));
    }

    // returns this*c
    public Complex multiply(Complex c) {
        return new Complex((re*c.re)-(im*c.im), (re*c.im)+(im*c.re));
    }

    // returns this/c
    public Complex divide(Complex c) {
        return new Complex((re*c.re + im*c.im)/(c.re*c.re + c.im*c.im), (im*c.re - re*c.im)/(c.re*c.re + c.im*c.im));
    }

    // returns this+c
    public Complex add(Complex c) {
        return new Complex(re+c.re, im+c.im);
    }

    // returns this-c
    public Complex sub(Complex c) {
        return new Complex(re-c.re, im-c.im);
    }

    // returns -this
    public Complex negate() {
        return new Complex(-re, -im);
    }

    // returns this^n, n is non-negative integer
    public Complex power(int n) {
        Complex start = new Complex(re, im);
        Complex returned = new Complex(re, im);
        for(int i = 0; i < n-1; i++) {
            returned = returned.multiply(start);
        }
        return returned;
    }

    // returns n-th root of this, n is positive integer
    public List<Complex> root(int n) {
        return null;
    }

    @Override
    public String toString() {
        return re + "+" + im + "i";
    }
}
