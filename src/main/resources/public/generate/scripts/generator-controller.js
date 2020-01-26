loadRules();

function loadRules() {
    fetch("/generate/rules", {
        method: "GET",
        headers: {"Authorization": sessionStorage.getItem("access_token")},
    })
        .then(response => {
            if (response.status === 200) {
                $(".rule-items-wrapper").show();
                $(".spinner-holder").hide();
                return response.json();
            }
        })
        .then(response => {
            if (response !== undefined) {
                for (const k in response) {
                    const item = response[k];
                    const values = JSON.parse(item["Values"]);
                    const itemWrapper = document.createElement("div");
                    itemWrapper.setAttribute("class", "rule-item");
                    const ruleWrapper = document.createElement("div");
                    ruleWrapper.setAttribute("class", "rule-wrapper");
                    const ruleInfoWrapper = document.createElement("div");
                    ruleInfoWrapper.setAttribute("class", "rule-info-wrapper");
                    const ruleLeftBlock = document.createElement("div");
                    ruleLeftBlock.setAttribute("class", "rule-info-left-block");
                    const ruleRightBlock = document.createElement("div");
                    ruleRightBlock.setAttribute("class", "rule-info-right-block");
                    const ruleBottomBlock = document.createElement("div");
                    ruleBottomBlock.setAttribute("class", "rule-info-bottom-block");
                    const generatedRuleWrapper = document.createElement("div");
                    generatedRuleWrapper.setAttribute("class", "generated-rule-wrapper");
                    const ul = document.createElement("ul");


                    for (const value of values) {
                        $(ul).append("<li>" + value + "</li>")
                    }

                    $(ruleLeftBlock).append("<label>Type code</label><input type='text' value='" + item["Operator"] + "'>");
                    $(ruleLeftBlock).append("<label>Type</label><input type='text' value='" + item["Type"] + "'>");
                    $(ruleLeftBlock).append("<label>Error code</label><input type='text' value='" + item["ErrorCode"] + "'>");
                    $(ruleLeftBlock).append("<label>Error message</label><input type='text' value='" + item["ErrorMessage"] + "'>");
                    $(ruleRightBlock).append("<label>Table</label><input type='text' value='" + item["Table"] + "'>");
                    $(ruleRightBlock).append("<label>Attribute</label><input type='text' value='" + item["Attribute"] + "'>");
                    $(ruleRightBlock).append("<label>Operator</label><input type='text' value='" + item["Operator"] + "'>");
                    $(ruleBottomBlock).append("<br/><label>Description</label>");
                    $(ruleBottomBlock).append("<textarea>" + item["Description"] + "</textarea>");
                    $(ruleBottomBlock).append("<br/><label>Values</label>");
                    $(ruleBottomBlock).append(ul);
                    $(ruleInfoWrapper).append(ruleLeftBlock, ruleRightBlock, ruleBottomBlock);
                    $(generatedRuleWrapper).append("<span class='generated-rule-title'>Generated rule</span>");
                    $(generatedRuleWrapper).append("<div class='generated-rule'>Click the \"Generate Code\" button to generate and insert the rule into your database</div>");
                    $(itemWrapper).append("<div class='rule-name'>" + k + "</div>");
                    $(ruleWrapper).append(ruleInfoWrapper, generatedRuleWrapper);
                    $(itemWrapper).append(ruleWrapper);
                    $(itemWrapper).append("<div class='rule-generator-wrapper'><button class='btn-generate-rule' data-id='" + item["ID"] + "'>Generate code</button></div>");

                    $("div.rule-items-wrapper").append(itemWrapper);
                }

                $("button.btn-generate-rule").click((item) => {
                    generateCode(item.target);
                });
            }
        });
}

function generateCode(element) {
    fetch("/generate/rules", {
        method: "POST",
        headers: {"Authorization": sessionStorage.getItem("access_token")},
        body: $(element).data("id")
    })
        .then(response => {
            if (response.status === 201) {
                return response.text();
            }
        })
        .then(response => {
            if (response !== undefined) {
                $(element).parent().parent().find("div.generated-rule").text(response);
            }
        });
}