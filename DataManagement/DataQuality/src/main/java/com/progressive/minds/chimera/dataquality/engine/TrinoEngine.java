package com.progressive.minds.chimera.dataquality.engine;

// Use this when we are ready to implement the TrinoEngine

import com.progressive.minds.chimera.dataquality.controls.DataControls;
import org.apache.arrow.vector.VectorSchemaRoot;

import java.util.List;
// import io.trino.jdbc.TrinoConnection;

public class TrinoEngine implements Engine {
    // private TrinoConnection trinoConnection;

    // public TrinoEngine(TrinoConnection trinoConnection) {
    //     this.trinoConnection = trinoConnection;
    // }

    private void convertArrowToTrino(VectorSchemaRoot arrowDataFrame) {
        // Conversion logic from Arrow to Trino compatible format
    }

    @Override
    public void process(VectorSchemaRoot arrowDataFrame, List<DataControls> dataControlsList) {
        // Convert Arrow DataFrame to Trino compatible format
        // Process the DataFrame based on DQ rules
        // Implement your DQ rules processing logic here
    }
}