/*
 * Copyright 1997-2022 Optimatika
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.ojalgo.equation;

import static org.ojalgo.function.constant.PrimitiveMath.*;

import java.util.ArrayList;
import java.util.List;

import org.ojalgo.array.ArrayR064;
import org.ojalgo.array.BasicArray;
import org.ojalgo.array.DenseArray;
import org.ojalgo.array.SparseArray;
import org.ojalgo.function.UnaryFunction;
import org.ojalgo.structure.Access1D;
import org.ojalgo.structure.Mutate1D;
import org.ojalgo.type.NumberDefinition;

public final class Equation implements Comparable<Equation>, Access1D<Double>, Mutate1D.Modifiable<Double> {

    public static Equation dense(final int pivot, final int cols) {
        return Equation.dense(pivot, cols, ArrayR064.FACTORY);
    }

    public static Equation dense(final int pivot, final int cols, final DenseArray.Factory<Double> factory) {
        return new Equation(factory.make(cols), pivot, ZERO);
    }

    public static List<Equation> denseSystem(final int rows, final int cols) {
        return Equation.denseSystem(rows, cols, ArrayR064.FACTORY);
    }

    public static List<Equation> denseSystem(final int rows, final int cols, final DenseArray.Factory<Double> factory) {

        List<Equation> system = new ArrayList<>(rows);

        for (int i = 0; i < rows; i++) {
            system.add(new Equation(factory.make(cols), i, ZERO));
        }

        return system;
    }

    public static Equation of(final double rhs, final int pivot, final double... body) {
        return new Equation(ArrayR064.wrap(body), pivot, rhs);
    }

    public static Equation sparse(final int pivot, final int cols) {
        return Equation.sparse(pivot, cols, ArrayR064.FACTORY);
    }

    public static Equation sparse(final int pivot, final int cols, final DenseArray.Factory<Double> factory) {
        return new Equation(SparseArray.factory(factory).limit(cols).make(), pivot, ZERO);
    }

    public static Equation sparse(final int pivot, final int cols, final DenseArray.Factory<Double> factory, final int numberOfNonzeros) {
        return new Equation(SparseArray.factory(factory).limit(cols).initial(numberOfNonzeros).make(), pivot, ZERO);
    }

    public static Equation sparse(final int pivot, final int cols, final int numberOfNonzeros) {
        return Equation.sparse(pivot, cols, ArrayR064.FACTORY, numberOfNonzeros);
    }

    public static List<Equation> sparseSystem(final int rows, final int cols) {
        return Equation.sparseSystem(rows, cols, ArrayR064.FACTORY);
    }

    public static List<Equation> sparseSystem(final int rows, final int cols, final DenseArray.Factory<Double> factory) {

        List<Equation> system = new ArrayList<>(rows);

        for (int i = 0; i < rows; i++) {
            system.add(new Equation(SparseArray.factory(factory).limit(cols).make(), i, ZERO));
        }

        return system;
    }

    public static List<Equation> sparseSystem(final int rows, final int cols, final DenseArray.Factory<Double> factory, final int numberOfNonzeros) {

        List<Equation> system = new ArrayList<>(rows);

        for (int i = 0; i < rows; i++) {
            system.add(new Equation(SparseArray.factory(factory).limit(cols).initial(numberOfNonzeros).make(), i, ZERO));
        }

        return system;
    }

    public static List<Equation> sparseSystem(final int rows, final int cols, final int numberOfNonzeros) {
        return Equation.sparseSystem(rows, cols, ArrayR064.FACTORY, numberOfNonzeros);
    }

    public static Equation wrap(final BasicArray<Double> body, final int pivot, final double rhs) {
        return new Equation(body, pivot, rhs);
    }

    /**
     * The row index of the original body matrix, [A].
     */
    public final int index;
    /**
     * The (nonzero) elements of this equation/row
     */
    private final BasicArray<Double> myBody;
    private double myPivot = ZERO;
    private double myRHS;

    /**
     * @deprecated v49 Use one of the factory methods instead
     */
    @Deprecated
    public Equation(final int row, final long numberOfColumns, final double rhs) {
        this(SparseArray.factory(ArrayR064.FACTORY).limit(numberOfColumns).make(), row, rhs);
    }

    Equation(final BasicArray<Double> body, final int pivot, final double rhs) {

        super();

        myBody = body;
        myRHS = rhs;

        index = pivot;
        myPivot = ZERO;
    }

    public void add(final long ind, final Comparable<?> addend) {
        this.add(ind, NumberDefinition.doubleValue(addend));
    }

    public void add(final long ind, final double addend) {
        myBody.add(ind, addend);
        if (ind == index) {
            myPivot = myBody.doubleValue(ind);
        }
    }

    /**
     * Will perform a (relaxed) GaussSeidel update.
     *
     * @param x The current solution (one element will be updated)
     * @param relaxation Typically 1.0 but could be anything (Most likely should be between 0.0 and 2.0).
     * @return The error in this equation
     */
    public <T extends Access1D<Double> & Mutate1D.Modifiable<Double>> double adjust(final T x, final double relaxation) {
        return this.calculate(x, myRHS, relaxation);
    }

    public int compareTo(final Equation other) {
        return Integer.compare(index, other.index);
    }

    public long count() {
        return myBody.count();
    }

    public double dot(final Access1D<?> vector) {
        return myBody.dot(vector);
    }

    public double doubleValue(final long ind) {
        return myBody.doubleValue(ind);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Equation)) {
            return false;
        }
        final Equation other = (Equation) obj;
        if (index != other.index) {
            return false;
        }
        return true;
    }

    public Double get(final long ind) {
        return myBody.get(ind);
    }

    public BasicArray<Double> getBody() {
        return myBody;
    }

    /**
     * @return The element at {@link #index}
     */
    public double getPivot() {
        return myPivot;
    }

    /**
     * @return The equation RHS
     */
    public double getRHS() {
        return myRHS;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        return prime * result + index;
    }

    public <T extends Access1D<Double> & Mutate1D.Modifiable<Double>> void initialise(final T x) {
        this.calculate(x, ZERO, ONE);
    }

    public void modifyOne(final long ind, final UnaryFunction<Double> modifier) {
        myBody.modifyOne(ind, modifier);
        if (ind == index) {
            myPivot = myBody.doubleValue(ind);
        }
    }

    public void set(final long ind, final Comparable<?> value) {
        this.set(ind, NumberDefinition.doubleValue(value));
    }

    public void set(final long ind, final double value) {
        myBody.set(ind, value);
        if (ind == index) {
            myPivot = value;
        }
    }

    public void setRHS(final double rhs) {
        myRHS = rhs;
    }

    @Override
    public String toString() {
        return index + ": " + myBody.toString() + " = " + myRHS;
    }

    private <T extends Access1D<Double> & Mutate1D.Modifiable<Double>> double calculate(final T x, final double rhs, final double relaxation) {

        double increment = rhs;

        double error = increment -= myBody.dot(x);

        increment *= relaxation;

        increment /= myPivot;

        x.add(index, increment);

        return error;
    }

}
