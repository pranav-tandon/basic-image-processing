package logic.features;

import logic.core.NestedMatrix;

/**
 * This datatype represents the output of a spatial Discrete Fourier Transform,
 * and holds the amplitude and phase matrix obtained from the DFT.
 */
public class DFTOutput {
    private NestedMatrix amplitude;
    private NestedMatrix phase;

    /*
        Abstraction Function:
            Represents the output of the (spatial) DFT applied to an picture.
            amplitude is a matrix that represents the amplitude portion of the DFT output.
            phase is a matrix that represents the phase portion of the DFT output.

        Note:
            A DFT can usually be represented by a matrix of complex numbers but
            we use the amplitude & phase representation using two matrices.

        Representation Invariant:
            amplitude.breadth == phase.breadth
            amplitude.length == phase.breadth
     */

    /**
     * Create a new DFTOutput instance.
     *
     * @param _amplitude is not null
     * @param _phase is not null, and is equal in dimensions to _amplitude
     */
    public DFTOutput(double[][] _amplitude, double[][] _phase) {
        amplitude = new NestedMatrix(_amplitude);
        phase = new NestedMatrix(_phase);
        if (amplitude.columns != phase.columns || amplitude.rows != phase.rows) {
            throw new IllegalArgumentException(
                "amplitude and phase matrices should have the same dimensions"
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DFTOutput)) {
            return false;
        }
        DFTOutput other = (DFTOutput) o;
        return amplitude.equals(other.amplitude) && phase.equals(other.phase);
    }

    @Override
    public int hashCode() {
        return amplitude.hashCode();
    }
}
