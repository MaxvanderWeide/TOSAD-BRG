loadRules();

function loadRules() {
    const values = {};
    values["typeCode"] = "ACMP";
    values["ruleType"] = "Compare";
    values["subType"] = "Attribute";
    values["table"] = "Producten";
    values["attribute"] = "prijs";
    values["operator"] = "LessThan";
    values["errorMessage"] = "Error!!!!!";
    values["errorCode"] = "-20000";
    values["status"] = "Generated";
    values["values"] = ["100", "100"];

    // console.log(values);

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4) {
            $(".rule-items-wrapper").show();
            $(".spinner-holder").hide();
            if(this.status == 200) {
                console.log('rules: ' + this.responseText);
            }
        }
    };
    xhttp.open("GET", '/generate/rules', true);
    xhttp.setRequestHeader('Authorization', sessionStorage.getItem("access_token"));
    xhttp.send();
}

function generateCode(element) {
    const ruleName = element.closest("div.rule-item").querySelector("div.rule-name").textContent;

    console.log(ruleName);
}