import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;

import com.progressive.minds.chimera.pipelineutils.SharedSparkSession;

public class testSparkJDBC {
    @Test
    public void SparkJDBCRead() {
        SparkSession spark = SharedSparkSession.getSparkSession();
        Dataset<Row> df =  spark.read()
                .format("jdbc")
                .option("url", "jdbc:postgresql://localhost:5432/chimera_db")
                .option("dbtable", "data_sources")
                //              .option("query", inSQLQuery)
                .option("user", "chimera")
                .option("password", "chimera123")
                .option("numPartitions", "4")
                .option("fetchSize", "5000")
                .option("driver", "org.postgresql.Driver")
        //        .options(extraOptions)
                .load();
        if(df.isEmpty()) {
            System.out.println("EmptyDataframe");
        }
        df.show();
    }
}
