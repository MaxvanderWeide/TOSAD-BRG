$(document).ready(function () {
    loadFromStorage();
    fillTypes();
    $(".btn-connect").click(() => {
        createConnection();
        $(".db-info-wrapper").hide();
        $(".action-button-box").show();
    });

    $(".maintain-rule").click(() => {
        $(".search-rule-wrapper").show();
        $(".action-button-box").hide();
    });

    $(".new-rule").click(() => {
        $(".new-rule-wrapper").show();
        $(".action-button-box").hide();
    });
});

function eventListeners() {
    $(".table-selection").unbind("change");
    $(".table-selection").change((item) => {
        fillTargetAttributes(item.target);
    });

    $(".type-selection").change((item) => {
        fillOperators(item.target);
        displayBlock(item.target);
        FillValuesTargetAttributes(item.target)
        $(item.target).parent().parent().parent().find(".rule-values-wrapper").show();
    });

    $(".btn-save").click((item) => {
        saveRule(item);
    })
}

function loadFromStorage() {
    if (sessionStorage.getItem("values") != null) {
        const values = JSON.parse(sessionStorage.getItem("values"));
        $("#dbEngine").empty().append("<option value=" + values['engine'] + ">" + values['engine'] + "</option>");
        $("#dbInputHost").val(values['host']);
        $("#dbName").val(values['dbName']);
        $("#dbInputPort").val(values['port']);
        $("#dbInputService").val(values['service']);
        $("#dbInputUser").val(values['username']);
        $("#dbInputPassword").val(values['password']);

        createConnection();
    }
}

function createConnection() {
    let values = {};
    values["engine"] = $("#dbEngine").val();
    values["dbName"] = $("#dbName").val();
    values["host"] = $("#dbInputHost").val();
    values["port"] = $("#dbInputPort").val();
    values["service"] = $("#dbInputService").val();
    values["username"] = $("#dbInputUser").val();
    values["password"] = $("#dbInputPassword").val();
    values = JSON.stringify(values);

    fetch("auth/connection", {
        method: "POST",
        body: values
    })
        .then(response => {
            if (response.status !== 200) {
                alert('Could not authenticate and make a connection')
            } else {
                return response.text();
            }
        })
        .then(response => {
            if (response !== undefined) {
                sessionStorage.setItem("access_token", response);
                sessionStorage.setItem("values", values);
                fillTargetTables();
                getAllRuleNames();

                //getAllRules();
                // $(".new-rule-wrapper").show();
            }
        });
}

function fillTargetTables() {
    fetch("define/tables", {
        method: "GET",
        headers: {"Authorization": sessionStorage.getItem("access_token")}
    })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            }
        })
        .then(response => {
            if (response !== undefined) {
                $(".table-selection").empty();
                for (const index in response.Tables) {
                    const value = response.Tables[index];
                    $(".table-selection").append("<option value='" + value + "'>" + value + "</option>");
                }
            }
        });
}


function fillTypes() {
    fetch("define/types", {
        method: "GET",
        headers: {"Authorization": sessionStorage.getItem("access_token")}
    })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            }
        })
        .then(response => {
            if (response !== undefined) {
                for (const index in response.Types) {
                    $(".type-selection").append("<option value='" + index + "'>" + index + "</option>");
                }
            }
        });
}

function fillTargetAttributes(tableSelection, entityRuleType = false) {
    fetch("define/tables/" + $(tableSelection).val() + "/attributes ", {
        method: "GET",
        headers: {"Authorization": sessionStorage.getItem("access_token")}
    })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            }
        })
        .then(response => {
            if (response !== undefined) {
                const selection = entityRuleType ? $("#custInput1") : $(".attribute-selection");
                $(selection).empty();
                for (const index in response.Attributes) {
                    $(selection).append("<option value='" + response.Attributes[index] + "'>" + index + ' - ' + response.Attributes[index] + "</option>");
                }
            }
        });
}

function fillOperators(type) {
    fetch("define/types/" + $(type).val() + "/operators", {
        method: "GET",
        headers: {"Authorization": sessionStorage.getItem("access_token")}
    })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            }
        })
        .then(response => {
            if (response !== undefined) {
                $(".operator-selection").empty();
                for (const index in response.Operators) {
                    $(".operator-selection").append("<option value='" + response.Operators[index] + "'>" + response.Operators[index] + "</option>");
                }
            }
        });
}

function saveRule(element) {
    const target = $(element.target).parent().parent();
    const selectedTable = $(target).find(".table-selection").val();
    const selectedType = $(target).find(".type-selection").val();
    const ruleOperation = $(target).hasClass("new-rule-wrapper") ? "insert" : "update";

    const ruleValues = [];

    if (selectedType === "Attribute_List") {
        $(target).find("ul.attributes-list li").each((index, item) => {
            ruleValues.push($(item).html());
        });
    } else {
        if (["Tuple", "Entity"].indexOf(selectedType.split("_")[0].trim()) > -1) {
            ruleValues.push(selectedTable);
        }
        $(target).find($("[id^=custInput]")).each((index, item) => {
            ruleValues.push($(item).val());
        });
    }

    let values = {};
    values["ruleName"] = $(target).find(".rule-name").val();
    values["description"] = $(target).find(".rule-description").val();
    values["tableName"] = selectedTable;
    values["typeName"] = selectedType;
    values["targetAttribute"] = $(target).find(".attribute-selection").val();
    values["operatorName"] = $(target).find(".operator-selection").val();
    values["errorMessage"] = $(target).find(".error-message").val();
    values["errorCode"] = $(target).find(".error-code").val();
    values["values"] = ruleValues;
    values["operation"] = ruleOperation;
    values = JSON.stringify(values);

    fetch("define/rules", {
        method: "POST",
        headers: {"Authorization": sessionStorage.getItem("access_token")},
        body: values
    })
        .then(response => {
            if (response.status === 201) {
                alert('Rule was created');
            } else if (response.status === 400) {
                alert('Rule was not created');
            }
        });
}

function displayBlock(type) {
    const tableSelection = $(type).parent().parent().parent(".rule-details-wrapper").find(".table-selection");
    $(type).parent().parent().parent(".rule-details-wrapper").find(".comparator-step").html("");
    eval(Types[type.options[type.selectedIndex].text].block);
    $(tableSelection).unbind("change");
    fillTargetAttributes(tableSelection);
}

function FillValuesTargetAttributes(element) {
    const typeName = $(element).val();
    const typeNameSplit = typeName.split("_");
    if (typeNameSplit[0].trim() === "Entity" ||
        typeNameSplit[0].trim() === "Tuple") {
        $(".table-selection").change((item) => {
            fillTargetAttributes(item.target, item.target);
        });
    }
}

function getAllRules() {
    fetch("define/rules", {
        method: "GET",
        headers: {"Authorization": sessionStorage.getItem("access_token")},
    })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            }
        }).then(response => {
        if (response !== undefined) {
            for (const index in response) {
                const item = response[index];

                const firstRow = $("<div>", {class: "row"});
                const secondRow = $("<div>", {class: "row"});
                const thirdRow = $("<div>", {class: "row"});

                const ruleItem = $("<div>", {class: "rule-item"});
                const nameWrapper = $("<div>", {class: "mb-3"});
                nameWrapper.append($("<label>", {text: "Rule name:"}));
                nameWrapper.append($("<input>", {type: "text", class: "form-input rule-name", value: index}));

                const descriptionWrapper = $("<div>", {class: "mb-3"});
                descriptionWrapper.append($("<label>", {text: "Description:"}));
                descriptionWrapper.append($("<textarea>", {
                    class: "rule-description form-input",
                    text: item["Description"]
                }));

                const tableSelectionWrapper = $("<div>", {class: "col-md-6 mb-3"});
                tableSelectionWrapper.append($("<label>", {text: "Select table:"}));
                const tableSelection = ($("<select>", {class: "table-selection form-input"}));
                tableSelection.append($("<option>", {
                    disabled: "disable",
                    selected: "selected",
                    text: "Select a table"
                }));
                tableSelectionWrapper.append(tableSelection);

                const typeSelectionWrapper = $("<div>", {class: "col-md-6 mb-3"});
                typeSelectionWrapper.append($("<label>", {text: "Select rule type:"}));
                const typeSelection = ($("<select>", {class: "type-selection form-input"}));
                typeSelection.append($("<option>", {disabled: "disable", selected: "selected", text: "Select a type"}));
                typeSelectionWrapper.append(typeSelection);

                const attributeSelectionWrapper = $("<div>", {class: "col-md-6 mb-3"});
                attributeSelectionWrapper.append($("<label>", {text: "Select target attribute:"}));
                const attributeSelection = ($("<select>", {class: "attribute-selection form-input"}));
                attributeSelection.append($("<option>", {
                    disabled: "disable",
                    selected: "selected",
                    text: "Select an attribute"
                }));
                attributeSelectionWrapper.append(attributeSelection);

                const operatorSelectionWrapper = $("<div>", {class: "col-md-6 mb-3"});
                operatorSelectionWrapper.append($("<label>", {text: "Select an operator:"}));
                const operatorSelection = ($("<select>", {class: "operator-selection form-input"}));
                operatorSelection.append($("<option>", {
                    disabled: "disable",
                    selected: "selected",
                    text: "Select an operator"
                }));
                operatorSelectionWrapper.append(operatorSelection);

                const errorMessageWrapper = $("<div>", {class: "col-md-6 mb-3"});
                errorMessageWrapper.append($("<label>", {text: "Define the error message:"}));
                errorMessageWrapper.append($("<textarea>", {
                    class: "error-message form-input",
                    text: item["ErrorMessage"]
                }));

                const errorCodeWrapper = $("<div>", {class: "col-md-6 mb-3"});
                errorCodeWrapper.append($("<label>", {text: "Error code:"}));
                errorCodeWrapper.append($("<input>", {
                    type: "number",
                    class: "error-code form-input",
                    placeholder: "-20080",
                    value: item["ErrorCode"]
                }));

                const ruleValuesWrapper = $("<div>", {class: "mb-3 rule-values-wrapper"});
                ruleValuesWrapper.append($("<h3>", {text: "Rule values:"}));
                const formStepComparator = ($("<div>", {class: "form-step-comparator"}));
                formStepComparator.append($("<div>", {class: "row comparator-step:"}));
                ruleValuesWrapper.append(formStepComparator);

                const buttonWrapper = $("<div>", {class: "text-center"});
                buttonWrapper.append($("<button>", {
                    class: "save-rule btn btn-primary btn-lg btn-block btn-save",
                    text: "Save rule"
                }))

                $(firstRow).append(tableSelectionWrapper, typeSelectionWrapper);
                $(secondRow).append(attributeSelectionWrapper, operatorSelectionWrapper);
                $(thirdRow).append(errorMessageWrapper, errorCodeWrapper);

                $(ruleItem).append(nameWrapper, descriptionWrapper, firstRow, secondRow, thirdRow, ruleValuesWrapper, buttonWrapper);

                $(".rules-list-wrapper").append(ruleItem);
            }
            return "ok";
        }
    }).then(response => {
        if (response === "ok") {
            $(".rules-list-wrapper").show();
            eventListeners();
        }
    });
}

function getAllRuleNames() {
    fetch("define/rules/names", {
        method: "GET",
        headers: {"Authorization": sessionStorage.getItem("access_token")}
    })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            }
        })
        .then(response => {
            if (response !== undefined) {
                let tableBody = document.getElementById('table-body');
                tableBody.innerHTML = null;

                let i = 0;
                response.forEach(addRow, i);

                function addRow(row, i) {
                    tableBody.innerHTML +=
                        "<tr>" +
                        "<th scope='row'>" + i + "</th>" +
                        "<td>" + row["name"] + "</td>" +
                        "<td>" + row["table"] + "</td>" +
                        "<td>" + row["other"] + "</td>" +
                        "</tr>";
                }
                console.log(response);
            }
        });
}

function getRuleByName(target) {

}
