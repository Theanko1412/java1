package hr.fer.zemris.math;

public class ComplexRootedPolynomial {

    Complex constant;
    Complex[] roots;

    // constructor
    public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
        this.constant = constant;
        this.roots = roots;
    }

    // computes polynomial value at given point z
    public Complex apply(Complex z) {
        Complex result = constant;
        for(int i = 0; i < roots.length-1; i++) {
            result = result.multiply(z.sub(roots[i]));
        }
        return result;
    }

/*    // converts this representation to ComplexPolynomial type
    public ComplexPolynomial toComplexPolynom() {

    }

    @Override
    public String toString() {

    }

    // finds index of closest root for given complex number z that is within
    // treshold; if there is no such root, returns -1
    // first root has index 0, second index 1, etc
    public int indexOfClosestRootFor(Complex z, double treshold) {

    }*/
}
