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
package org.ojalgo.matrix.store;

import java.util.Arrays;

import org.ojalgo.ProgrammingError;
import org.ojalgo.function.NullaryFunction;
import org.ojalgo.function.UnaryFunction;
import org.ojalgo.matrix.operation.MultiplyBoth;
import org.ojalgo.structure.Access1D;

abstract class Subregion2D<N extends Comparable<N>> implements TransformableRegion<N> {

    static final class ColumnsRegion<N extends Comparable<N>> extends Subregion2D<N> {

        private final TransformableRegion<N> myBase;
        private final int[] myColumns;

        /**
         * @param base
         * @param multiplier
         * @param columns
         */
        ColumnsRegion(final TransformableRegion<N> base, final TransformableRegion.FillByMultiplying<N> multiplier, final int... columns) {
            super(multiplier, base.countRows(), columns.length);
            myBase = base;
            myColumns = columns;
        }

        public void add(final long row, final long col, final Comparable<?> addend) {
            myBase.add(row, myColumns[(int) col], addend);
        }

        public void add(final long row, final long col, final double addend) {
            myBase.add(row, myColumns[(int) col], addend);
        }

        public long countColumns() {
            return myColumns.length;
        }

        public long countRows() {
            return myBase.countRows();
        }

        public double doubleValue(final long row, final long col) {
            return myBase.doubleValue(row, myColumns[(int) col]);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ColumnsRegion)) {
                return false;
            }
            ColumnsRegion other = (ColumnsRegion) obj;
            if (myBase == null) {
                if (other.myBase != null) {
                    return false;
                }
            } else if (!myBase.equals(other.myBase)) {
                return false;
            }
            if (!Arrays.equals(myColumns, other.myColumns)) {
                return false;
            }
            return true;
        }

        public void fillColumn(final long row, final long col, final Access1D<N> values) {
            myBase.fillColumn(row, myColumns[(int) col], values);
        }

        public void fillColumn(final long row, final long col, final N value) {
            myBase.fillColumn(row, myColumns[(int) col], value);
        }

        public void fillColumn(final long row, final long col, final NullaryFunction<?> supplier) {
            myBase.fillColumn(row, myColumns[(int) col], supplier);
        }

        public void fillOne(final long row, final long col, final Access1D<?> values, final long valueIndex) {
            myBase.fillOne(row, myColumns[(int) col], values, valueIndex);
        }

        public void fillOne(final long row, final long col, final N value) {
            myBase.fillOne(row, myColumns[(int) col], value);
        }

        public void fillOne(final long row, final long col, final NullaryFunction<?> supplier) {
            myBase.fillOne(row, myColumns[(int) col], supplier);
        }

        public N get(final long row, final long col) {
            return myBase.get(row, myColumns[(int) col]);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (myBase == null ? 0 : myBase.hashCode());
            result = prime * result + Arrays.hashCode(myColumns);
            return result;
        }

        public void modifyColumn(final long row, final long col, final UnaryFunction<N> modifier) {
            myBase.modifyColumn(row, myColumns[(int) col], modifier);
        }

        public void modifyOne(final long row, final long col, final UnaryFunction<N> modifier) {
            myBase.modifyOne(row, myColumns[(int) col], modifier);
        }

        public void set(final long row, final long col, final Comparable<?> value) {
            myBase.set(row, myColumns[(int) col], value);
        }

        public void set(final long row, final long col, final double value) {
            myBase.set(row, myColumns[(int) col], value);
        }

    }

    static final class LimitRegion<N extends Comparable<N>> extends Subregion2D<N> {

        private final TransformableRegion<N> myBase;
        private final int myRowLimit, myColumnLimit; // limits

        LimitRegion(final TransformableRegion<N> base, final TransformableRegion.FillByMultiplying<N> multiplier, final int rowLimit, final int columnLimit) {
            super(multiplier, rowLimit, columnLimit);
            myBase = base;
            myRowLimit = rowLimit;
            myColumnLimit = columnLimit;
        }

        public void add(final long row, final long col, final Comparable<?> addend) {
            myBase.add(row, col, addend);
        }

        public void add(final long row, final long col, final double addend) {
            myBase.add(row, col, addend);
        }

        public long countColumns() {
            return myColumnLimit;
        }

        public long countRows() {
            return myRowLimit;
        }

        public double doubleValue(final long row, final long col) {
            return myBase.doubleValue(row, col);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof LimitRegion)) {
                return false;
            }
            LimitRegion other = (LimitRegion) obj;
            if (myBase == null) {
                if (other.myBase != null) {
                    return false;
                }
            } else if (!myBase.equals(other.myBase)) {
                return false;
            }
            if (myColumnLimit != other.myColumnLimit || myRowLimit != other.myRowLimit) {
                return false;
            }
            return true;
        }

        public void fillOne(final long row, final long col, final Access1D<?> values, final long valueIndex) {
            myBase.fillOne(row, col, values, valueIndex);
        }

        public void fillOne(final long row, final long col, final N value) {
            myBase.fillOne(row, col, value);
        }

        public void fillOne(final long row, final long col, final NullaryFunction<?> supplier) {
            myBase.fillOne(row, col, supplier);
        }

        public N get(final long row, final long col) {
            return myBase.get(row, col);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (myBase == null ? 0 : myBase.hashCode());
            result = prime * result + myColumnLimit;
            result = prime * result + myRowLimit;
            return result;
        }

        public void modifyOne(final long row, final long col, final UnaryFunction<N> modifier) {
            myBase.modifyOne(row, col, modifier);
        }

        public void set(final long row, final long col, final Comparable<?> value) {
            myBase.set(row, col, value);
        }

        public void set(final long row, final long col, final double value) {
            myBase.set(row, col, value);
        }

    }

    static final class OffsetRegion<N extends Comparable<N>> extends Subregion2D<N> {

        private final TransformableRegion<N> myBase;
        private final int myRowOffset, myColumnOffset; // origin/offset

        OffsetRegion(final TransformableRegion<N> base, final TransformableRegion.FillByMultiplying<N> multiplier, final int rowOffset,
                final int columnOffset) {
            super(multiplier, base.countRows() - rowOffset, base.countColumns() - columnOffset);
            myBase = base;
            myRowOffset = rowOffset;
            myColumnOffset = columnOffset;
        }

        public void add(final long row, final long col, final Comparable<?> addend) {
            myBase.add(myRowOffset + row, myColumnOffset + col, addend);
        }

        public void add(final long row, final long col, final double addend) {
            myBase.add(myRowOffset + row, myColumnOffset + col, addend);
        }

        public long countColumns() {
            return myBase.countColumns() - myColumnOffset;
        }

        public long countRows() {
            return myBase.countRows() - myRowOffset;
        }

        public double doubleValue(final long row, final long col) {
            return myBase.doubleValue(myRowOffset + row, myColumnOffset + col);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof OffsetRegion)) {
                return false;
            }
            OffsetRegion other = (OffsetRegion) obj;
            if (myBase == null) {
                if (other.myBase != null) {
                    return false;
                }
            } else if (!myBase.equals(other.myBase)) {
                return false;
            }
            if (myColumnOffset != other.myColumnOffset || myRowOffset != other.myRowOffset) {
                return false;
            }
            return true;
        }

        @Override
        public void fillAll(final N value) {
            final long tmpCountColumns = myBase.countColumns();
            for (long j = myColumnOffset; j < tmpCountColumns; j++) {
                myBase.fillColumn(myRowOffset, j, value);
            }
        }

        @Override
        public void fillAll(final NullaryFunction<?> supplier) {
            final long tmpCountColumns = myBase.countColumns();
            for (long j = myColumnOffset; j < tmpCountColumns; j++) {
                myBase.fillColumn(myRowOffset, j, supplier);
            }
        }

        public void fillColumn(final long row, final long col, final N value) {
            myBase.fillColumn(myRowOffset + row, myColumnOffset + col, value);
        }

        public void fillColumn(final long row, final long col, final NullaryFunction<?> supplier) {
            myBase.fillColumn(myRowOffset + row, myColumnOffset + col, supplier);
        }

        public void fillDiagonal(final long row, final long col, final N value) {
            myBase.fillDiagonal(myRowOffset + row, myColumnOffset + col, value);
        }

        public void fillDiagonal(final long row, final long col, final NullaryFunction<?> supplier) {
            myBase.fillDiagonal(myRowOffset + row, myColumnOffset + col, supplier);
        }

        public void fillOne(final long row, final long col, final Access1D<?> values, final long valueIndex) {
            myBase.fillOne(myRowOffset + row, myColumnOffset + col, values, valueIndex);
        }

        public void fillOne(final long row, final long col, final N value) {
            myBase.fillOne(myRowOffset + row, myColumnOffset + col, value);
        }

        public void fillOne(final long row, final long col, final NullaryFunction<?> supplier) {
            myBase.fillOne(myRowOffset + row, myColumnOffset + col, supplier);
        }

        public void fillRow(final long row, final long col, final N value) {
            myBase.fillRow(myRowOffset + row, myColumnOffset + col, value);
        }

        public void fillRow(final long row, final long col, final NullaryFunction<?> supplier) {
            myBase.fillRow(myRowOffset + row, myColumnOffset + col, supplier);
        }

        public N get(final long row, final long col) {
            return myBase.get(myRowOffset + row, myColumnOffset + col);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (myBase == null ? 0 : myBase.hashCode());
            result = prime * result + myColumnOffset;
            result = prime * result + myRowOffset;
            return result;
        }

        public void modifyAll(final UnaryFunction<N> modifier) {
            for (long j = myColumnOffset; j < myBase.countColumns(); j++) {
                myBase.modifyColumn(myRowOffset, j, modifier);
            }
        }

        public void modifyColumn(final long row, final long col, final UnaryFunction<N> modifier) {
            myBase.modifyColumn(myRowOffset + row, myColumnOffset + col, modifier);
        }

        public void modifyDiagonal(final long row, final long col, final UnaryFunction<N> modifier) {
            myBase.modifyDiagonal(myRowOffset + row, myColumnOffset + col, modifier);
        }

        public void modifyOne(final long row, final long col, final UnaryFunction<N> modifier) {
            myBase.modifyOne(myRowOffset + row, myColumnOffset + col, modifier);
        }

        public void modifyRow(final long row, final long col, final UnaryFunction<N> modifier) {
            myBase.modifyRow(myRowOffset + row, myColumnOffset + col, modifier);
        }

        public void set(final long row, final long col, final Comparable<?> value) {
            myBase.set(myRowOffset + row, myColumnOffset + col, value);
        }

        public void set(final long row, final long col, final double value) {
            myBase.set(myRowOffset + row, myColumnOffset + col, value);
        }

    }

    static final class RowsRegion<N extends Comparable<N>> extends Subregion2D<N> {

        private final TransformableRegion<N> myBase;
        private final int[] myRows;

        RowsRegion(final TransformableRegion<N> base, final TransformableRegion.FillByMultiplying<N> multiplier, final int... rows) {
            super(multiplier, rows.length, base.countColumns());
            myBase = base;
            myRows = rows;
        }

        public void add(final long row, final long col, final Comparable<?> addend) {
            myBase.add(myRows[(int) row], col, addend);
        }

        public void add(final long row, final long col, final double addend) {
            myBase.add(myRows[(int) row], col, addend);
        }

        public long countColumns() {
            return myBase.countColumns();
        }

        public long countRows() {
            return myRows.length;
        }

        public double doubleValue(final long row, final long col) {
            return myBase.doubleValue(myRows[(int) row], col);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof RowsRegion)) {
                return false;
            }
            RowsRegion other = (RowsRegion) obj;
            if (myBase == null) {
                if (other.myBase != null) {
                    return false;
                }
            } else if (!myBase.equals(other.myBase)) {
                return false;
            }
            if (!Arrays.equals(myRows, other.myRows)) {
                return false;
            }
            return true;
        }

        public void fillOne(final long row, final long col, final Access1D<?> values, final long valueIndex) {
            myBase.fillOne(myRows[(int) row], col, values, valueIndex);
        }

        public void fillOne(final long row, final long col, final N value) {
            myBase.fillOne(myRows[(int) row], col, value);
        }

        public void fillOne(final long row, final long col, final NullaryFunction<?> supplier) {
            myBase.fillOne(myRows[(int) row], col, supplier);
        }

        public void fillRow(final long row, final long col, final Access1D<N> values) {
            myBase.fillRow(myRows[(int) row], col, values);
        }

        public void fillRow(final long row, final long col, final N value) {
            myBase.fillRow(myRows[(int) row], col, value);
        }

        public void fillRow(final long row, final long col, final NullaryFunction<?> supplier) {
            myBase.fillRow(myRows[(int) row], col, supplier);
        }

        public N get(final long row, final long col) {
            return myBase.get(myRows[(int) row], col);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (myBase == null ? 0 : myBase.hashCode());
            result = prime * result + Arrays.hashCode(myRows);
            return result;
        }

        public void modifyOne(final long row, final long col, final UnaryFunction<N> modifier) {
            myBase.modifyOne(myRows[(int) row], col, modifier);
        }

        public void modifyRow(final long row, final long col, final UnaryFunction<N> modifier) {
            myBase.modifyRow(myRows[(int) row], col, modifier);
        }

        public void set(final long row, final long col, final Comparable<?> value) {
            myBase.set(myRows[(int) row], col, value);
        }

        public void set(final long row, final long col, final double value) {
            myBase.set(myRows[(int) row], col, value);
        }

    }

    static final class TransposedRegion<N extends Comparable<N>> extends Subregion2D<N> {

        private final TransformableRegion<N> myBase;

        TransposedRegion(final TransformableRegion<N> base, final TransformableRegion.FillByMultiplying<N> multiplier) {
            super(multiplier, base.countColumns(), base.countRows());
            myBase = base;
        }

        public void add(final long row, final long col, final Comparable<?> addend) {
            myBase.add(col, row, addend);
        }

        public void add(final long row, final long col, final double addend) {
            myBase.add(col, row, addend);
        }

        public long countColumns() {
            return myBase.countRows();
        }

        public long countRows() {
            return myBase.countColumns();
        }

        public double doubleValue(final long row, final long col) {
            return myBase.doubleValue(col, row);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof TransposedRegion)) {
                return false;
            }
            TransposedRegion other = (TransposedRegion) obj;
            if (myBase == null) {
                if (other.myBase != null) {
                    return false;
                }
            } else if (!myBase.equals(other.myBase)) {
                return false;
            }
            return true;
        }

        public void fillColumn(final long row, final long col, final N value) {
            myBase.fillRow(col, row, value);
        }

        public void fillColumn(final long row, final long col, final NullaryFunction<?> supplier) {
            myBase.fillRow(col, row, supplier);
        }

        public void fillDiagonal(final long row, final long col, final N value) {
            myBase.fillDiagonal(col, row, value);
        }

        public void fillDiagonal(final long row, final long col, final NullaryFunction<?> supplier) {
            myBase.fillRow(col, row, supplier);
        }

        public void fillOne(final long row, final long col, final Access1D<?> values, final long valueIndex) {
            myBase.fillOne(col, row, values, valueIndex);
        }

        public void fillOne(final long row, final long col, final N value) {
            myBase.fillOne(col, row, value);
        }

        public void fillOne(final long row, final long col, final NullaryFunction<?> supplier) {
            myBase.fillOne(col, row, supplier);
        }

        public void fillRow(final long row, final long col, final N value) {
            myBase.fillDiagonal(col, row, value);
        }

        public void fillRow(final long row, final long col, final NullaryFunction<?> supplier) {
            myBase.fillDiagonal(col, row, supplier);
        }

        public N get(final long row, final long col) {
            return myBase.get(col, row);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (myBase == null ? 0 : myBase.hashCode());
            return result;
        }

        public void modifyColumn(final long row, final long col, final UnaryFunction<N> modifier) {
            myBase.modifyRow(col, row, modifier);
        }

        public void modifyDiagonal(final long row, final long col, final UnaryFunction<N> modifier) {
            myBase.modifyDiagonal(col, row, modifier);
        }

        public void modifyOne(final long row, final long col, final UnaryFunction<N> modifier) {
            myBase.modifyOne(col, row, modifier);
        }

        public void modifyRow(final long row, final long col, final UnaryFunction<N> modifier) {
            myBase.modifyColumn(col, row, modifier);
        }

        @Override
        public TransformableRegion<N> regionByTransposing() {
            return myBase;
        }

        public void set(final long row, final long col, final Comparable<?> value) {
            myBase.set(col, row, value);
        }

        public void set(final long row, final long col, final double value) {
            myBase.set(col, row, value);
        }

    }

    private final TransformableRegion.FillByMultiplying<N> myMultiplier;

    @SuppressWarnings("unused")
    private Subregion2D() {
        this(null, 0L, 0L);
    }

    @SuppressWarnings("unchecked")
    Subregion2D(final TransformableRegion.FillByMultiplying<N> multiplier, final long rows, final long columns) {

        super();

        if (multiplier instanceof MultiplyBoth.Primitive) {
            myMultiplier = (TransformableRegion.FillByMultiplying<N>) MultiplyBoth.newPrimitive64(Math.toIntExact(rows), Math.toIntExact(columns));
        } else if (multiplier instanceof MultiplyBoth.Generic) {
            myMultiplier = (TransformableRegion.FillByMultiplying<N>) MultiplyBoth.newGeneric(Math.toIntExact(rows), Math.toIntExact(columns));
        } else {
            myMultiplier = multiplier;
        }
    }

    public final void fillByMultiplying(final Access1D<N> left, final Access1D<N> right) {

        final int complexity = Math.toIntExact(left.count() / this.countRows());
        if (complexity != Math.toIntExact(right.count() / this.countColumns())) {
            ProgrammingError.throwForMultiplicationNotPossible();
        }

        myMultiplier.invoke(this, left, (int) (left.count() / this.countRows()), right);
    }

    public final TransformableRegion<N> regionByColumns(final int... columns) {
        return new Subregion2D.ColumnsRegion<>(this, myMultiplier, columns);
    }

    public final TransformableRegion<N> regionByLimits(final int rowLimit, final int columnLimit) {
        return new Subregion2D.LimitRegion<>(this, myMultiplier, rowLimit, columnLimit);
    }

    public final TransformableRegion<N> regionByOffsets(final int rowOffset, final int columnOffset) {
        return new Subregion2D.OffsetRegion<>(this, myMultiplier, rowOffset, columnOffset);
    }

    public final TransformableRegion<N> regionByRows(final int... rows) {
        return new Subregion2D.RowsRegion<>(this, myMultiplier, rows);
    }

    public TransformableRegion<N> regionByTransposing() {
        return new Subregion2D.TransposedRegion<>(this, myMultiplier);
    }

    @Override
    public String toString() {
        return Access1D.toString(this);
    }

}
