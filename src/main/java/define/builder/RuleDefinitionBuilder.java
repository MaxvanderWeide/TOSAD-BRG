package define.builder;

import model.definition.Comparator;
import model.definition.Operator;
import model.definition.RuleDefinition;
import model.physical.Attribute;
import model.physical.Table;
import model.rule.BusinessRuleType;

import java.util.List;

public class RuleDefinitionBuilder {

    private BusinessRuleType type;
    private Attribute targetAttribute;
    private Operator operator;
    private Comparator comparator;
    private Table table;
    private Attribute compareAttribute;
    private List<String> values;

    public RuleDefinitionBuilder setType(BusinessRuleType type) {
        this.type = type;
        return this;
    }

    public RuleDefinitionBuilder setAttribute(Attribute attribute) {
        this.targetAttribute = attribute;
        return this;
    }

    public RuleDefinitionBuilder setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    public RuleDefinitionBuilder setComparator(Comparator comparator) {
        this.comparator = comparator;
        return this;
    }

    public RuleDefinitionBuilder setTable(Table table) {
        this.table = table;
        return this;
    }

    public RuleDefinitionBuilder setValues(Attribute attribute, List<String> values) {
        this.compareAttribute = attribute;
        this.values = values;
        return this;
    }

    public RuleDefinitionBuilder setValues(List<String> values) {
        this.values = values;
        return this;
    }

    public RuleDefinition build() {
        return new RuleDefinition(type, targetAttribute, operator, comparator, table, compareAttribute, values);
    }
}
