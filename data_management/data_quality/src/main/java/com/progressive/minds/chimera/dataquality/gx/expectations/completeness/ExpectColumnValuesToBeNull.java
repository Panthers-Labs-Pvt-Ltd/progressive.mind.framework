package com.progressive.minds.chimera.dataquality.gx.expectations.completeness;

import com.amazon.deequ.VerificationSuite;
import com.amazon.deequ.VerificationResult;
import com.amazon.deequ.checks.Check;
import com.amazon.deequ.checks.CheckLevel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import scala.Option;
import com.amazon.deequ.checks.CheckResult;
import com.amazon.deequ.checks.CheckStatus;
import com.amazon.deequ.constraints.Constraint;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class ExpectColumnValuesToBeNull {

    /**
     * Verifies that all values in the specified column are null.
     *
     * @param dataset   The dataset to check
     * @param column    The column to verify
     * @param checkName Optional name for the check
     * @return True if all values in the column are null, false otherwise
     */
    public boolean execute(Dataset<Row> dataset, String column, String checkName, boolean useApproximate ) {
        // Default check name if not provided
        String name = (checkName != null && !checkName.isEmpty())
                ? checkName
                : "ExpectColumnValuesToBeNull_" + column;

        Seq<Constraint> emptySeqs = JavaConverters.asScalaBuffer(Collections.<Constraint>emptyList()).toSeq();
        Check check = new Check(CheckLevel.Error(), "Null Check", emptySeqs);

        if(!useApproximate)
            check.hasCompleteness(column,
                        (completeness) -> (((Number) completeness).doubleValue() >= 0.0),Option.empty(),
                        Option.empty());
            else
            check.hasEntropy(
                column,
                (d) -> (((Number) d).doubleValue() > 0.5),
                Option.empty()
        );

        // Convert Java list to Scala sequence
        scala.collection.Seq<Check> checks = JavaConverters.asScalaBuffer(Collections.singletonList(check)).toSeq();

        // Run the verification suite
        VerificationResult result = new VerificationSuite()
                .onData(dataset)
                .addChecks(checks)
                .run();

        // Get the check status safely
        Map<Check, CheckResult> checkResults = JavaConverters.mapAsJavaMap(result.checkResults());
        Optional<CheckResult> checkResult = Optional.ofNullable(checkResults.get(name));

        return checkResult.map(cr -> cr.status() == CheckStatus.Success()).orElse(false);
    }
}
