package archive;

import org.apache.spark.sql.catalyst.expressions.*;
import org.apache.spark.sql.catalyst.plans.logical.Aggregate;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;
import org.apache.spark.sql.catalyst.plans.logical.Project;
import scala.collection.JavaConverters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnLineageUtils {

    public static String processExpression(Expression expr) {
        if (expr instanceof AttributeReference) {
            AttributeReference attr = (AttributeReference) expr;
            return attr.name();
        } else if (expr instanceof Literal) {
            Literal literal = (Literal) expr;
            return literal.value().toString();
        } else {
            return expr.toString();
        }
    }

    public static String processSortOrder(Expression expr) {
        if (expr instanceof SortOrder) {
            SortOrder sortOrder = (SortOrder) expr;
            String column = processExpression(sortOrder.child());
            String direction = sortOrder.direction().toString();
            return column + " " + direction;
        } else {
            return expr.toString();
        }
    }

    public static String processCondition(Expression condition) {
        if (condition instanceof EqualTo) {
            EqualTo equalTo = (EqualTo) condition;
            return processExpression(equalTo.left()) + " = " + processExpression(equalTo.right());
        } else if (condition instanceof GreaterThan) {
            GreaterThan greaterThan = (GreaterThan) condition;
            return processExpression(greaterThan.left()) + " > " + processExpression(greaterThan.right());
        } else if (condition instanceof LessThan) {
            LessThan lessThan = (LessThan) condition;
            return processExpression(lessThan.left()) + " < " + processExpression(lessThan.right());
        } else if (condition instanceof And) {
            And andCondition = (And) condition;
            return "(" + processCondition(andCondition.left()) + " AND " + processCondition(andCondition.right()) + ")";
        } else if (condition instanceof Or) {
            Or orCondition = (Or) condition;
            return "(" + processCondition(orCondition.left()) + " OR " + processCondition(orCondition.right()) + ")";
        } else {
            // Default case for other types of expressions
            return condition.toString();
        }
    }
    /**
     * Extracts field lineage from the logical plan.
     */
    private static Map<String, FieldLineage> extractFieldLineage(LogicalPlan plan) {
        Map<String, FieldLineage> fieldLineageMap = new HashMap<>();
        traverseLogicalPlan(plan, fieldLineageMap);
        return fieldLineageMap;
    }
    /**
     * Class to hold field lineage details.
     */
    public static class FieldLineage {
        private final List<String> lineage = new ArrayList<>();

        public void addTransformation(String transformationType, List<String> inputFields) {
            lineage.add(transformationType + " -> Input Fields: " + inputFields);
        }

        public List<String> getLineage() {
            return lineage;
        }
    }

    /**
     * Extracts input fields from an expression.
     */
    public static List<String> extractInputFieldsFromExpression(Expression expression) {
        List<String> inputFields = new ArrayList<>();

        if (expression instanceof Attribute) {
            inputFields.add(((Attribute) expression).name());
        }

        // Explicit conversion for child expressions
        for (Expression child : JavaConverters.seqAsJavaList(expression.children())) {
            inputFields.addAll(extractInputFieldsFromExpression(child));
        }

        return inputFields;
    }

    /**
     * Recursively traverses the logical plan and extracts field lineage.
     */
    public static void traverseLogicalPlan(LogicalPlan plan, Map<String, FieldLineage> fieldLineageMap) {
        // Handle Project and Aggregate nodes (common for calculated fields)
        if (plan instanceof Project) {
            Project project = (Project) plan;

            // Iterate over project list (Project nodes)
            for (NamedExpression namedExpression : JavaConverters.seqAsJavaList(project.projectList())) {
                String outputField = namedExpression.name();
                List<String> inputFields = extractInputFieldsFromExpression((Expression) namedExpression);
                fieldLineageMap.computeIfAbsent(outputField, k -> new FieldLineage())
                        .addTransformation("Project", inputFields);
            }
        } else if (plan instanceof Aggregate) {
            Aggregate aggregate = (Aggregate) plan;

            // Handle aggregate expressions explicitly
            for (NamedExpression namedExpression : JavaConverters.seqAsJavaList(aggregate.aggregateExpressions())) {
                String outputField = namedExpression.name();
                List<String> inputFields = extractInputFieldsFromExpression((Expression) namedExpression);
                fieldLineageMap.computeIfAbsent(outputField, k -> new FieldLineage())
                        .addTransformation("Aggregate", inputFields);
            }
        }

        // Recursively process child plans
        for (LogicalPlan child : JavaConverters.seqAsJavaList(plan.children())) {
            traverseLogicalPlan(child, fieldLineageMap);
        }
    }

    public static String processGenerator(Generator generator) {
        // Process the generator based on its type
        if (generator instanceof Explode) {
            Explode explode = (Explode) generator;
            String child = processExpression(explode.child());
            return "Explode(" + child + ")";
        } else if (generator instanceof JsonTuple) {
            JsonTuple jsonTuple = (JsonTuple) generator;
            String child = processExpression(jsonTuple);
            return "JsonTuple(" + child + ")";
        } else if (generator instanceof Stack) {
            Stack stack = (Stack) generator;
            String child = processExpression(stack);
            return "Stack(" + child + ")";
        } else {
            // Default case for other types of generators
            return generator.toString();
        }
}
}
