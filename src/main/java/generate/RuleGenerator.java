package generate;

public class RuleGenerator {
    private BusinessRule businessRule;
    private String generatedCode;
    private String triggerName;

    public RuleGenerator(BusinessRule businessRule) {
        this.businessRule = businessRule;
    }

    private void generateTriggerName() {
        System.out.println("TriggerName is being generated...");

    }

    private void generateCode() {
        System.out.println("Code is being generated...");

    }

    public String getGeneratedCode() {
        return this.generatedCode;
    }
}
