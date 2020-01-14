package generate;

public class BusinessRuleTrigger {
    private String triggerEvent;
    private String triggerCode;

    public BusinessRuleTrigger(String triggerEvent, String triggerCode) {
        this.triggerEvent = triggerEvent;
        this.triggerCode = triggerCode;
    }

    public String getTriggerEvent() {
        return this.triggerEvent;
    }

    public String getTriggerCode() {
        return this.triggerCode;
    }
}
