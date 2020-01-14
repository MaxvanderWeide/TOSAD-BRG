package define.model.definition;

import define.model.physical.Attribute;
import define.model.physical.Table;
import define.model.rule.BusinessRuleType;

import java.util.List;

public class RuleDefinition {

    private BusinessRuleType type;
    private Attribute targetAttribute;
    private Operator operator;
    private Comparator comparator;
    private Table table;
    private Attribute compareAttribute;
    private List<String> values;

    public RuleDefinition(BusinessRuleType type, Attribute targetAttribute,
                          Operator operator, Comparator comparator,
                          Table table, Attribute compareAttribute,
                          List<String> values) {
        this.type = type;
        this.targetAttribute = targetAttribute;
        this.operator = operator;
        this.comparator = comparator;
        this.table = table;
        this.compareAttribute = compareAttribute;
        this.values = values;
    }


}
