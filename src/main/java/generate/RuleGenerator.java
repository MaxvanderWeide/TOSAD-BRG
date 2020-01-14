package generate;

public class RuleGenerator {
    private String generatedCode;
    private String triggerName;


    // TODO: change static values
    private String applicationName = "BRG";
    private String projectName = "VBMG";
    private String attribute = "product";
    private String ruleTypeCode = "arng";
    private String tableName = "products";
    private String tableAttribute = "productCode";


    public RuleGenerator() {
        generateTriggerName();
        System.out.println(this.triggerName);
    }


    private void generateTriggerName() {
        System.out.println("TriggerName is being generated...");
        this.triggerName = (String.format("%s_%s_%s_trigger_%s", this.applicationName, this.projectName, this.attribute.substring(0, 4), this.ruleTypeCode)).toUpperCase();
    }

    private void generateCode() {
        System.out.println("Code is being generated...");

    }

    public String getGeneratedCode() {
        return this.generatedCode;
    }
}
