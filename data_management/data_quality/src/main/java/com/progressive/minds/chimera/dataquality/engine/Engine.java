package com.progressive.minds.chimera.dataquality.engine;

import com.progressive.minds.chimera.dataquality.controls.DataControls;
import org.apache.arrow.vector.VectorSchemaRoot;

import java.util.List;

public interface Engine {
    void process(VectorSchemaRoot arrowDataFrame, List<DataControls> dataControlsList);
}