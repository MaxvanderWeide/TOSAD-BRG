$(document).ready(function () {
    loadFromStorage();
    fillTypes();
    startupEventListeners();
});

function startupEventListeners() {
    $(".btn-connect").click(() => {
        createConnection();
    });

    $(".maintain-rule").click(() => {
        $(".search-rule-wrapper").show();
        $(".action-button-box").hide();
        $(".back-maintain").show();
        $(".search-button").show();
        $(".btn-delete").show();
    });

    $(".new-rule").click(() => {
        $(".new-rule-wrapper").show();
        $(".action-button-box").hide();
        $(".back-define").show();
    });

    $(".back-maintain").click(() => {
        showMenu();
    });

    $("#search-rule").on("keyup", function () {
        const value = $(this).val().toLowerCase();
        $("#table-body tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    $(".back-define").click(() => {
        showMenu();
    });

    $(".new-rule-wrapper .table-selection").change((item) => {
        fillTargetAttributes(item.target, false, "define");
    });

    $(".new-rule-wrapper .type-selection").change((item) => {
        fillOperators(item.target, "define");
        displayBlock(item.target);
        fillValuesTargetAttributes(item.target);
        $(item.target).parent().parent().parent().find(".rule-values-wrapper").show();
    });

    $(".btn-save").click((item) => {
        saveRule(item);
    })
}

function showMenu() {
    $(".action-button-box").show();
    $(".new-rule-wrapper").hide();
    $(".search-rule-wrapper").hide();
    $(".back-maintain").hide();
    $(".back-define").hide();
    $(".btn-delete").hide();
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
    } else {
        $(".db-info-wrapper").show();
        $(".spinner-holder.initial-spinner").hide();
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
                showMenu();
                return response.text();
            }
        })
        .then(response => {
            if (response !== undefined) {
                showMenu();
                $(".db-info-wrapper").hide();
                sessionStorage.setItem("access_token", response);
                sessionStorage.setItem("values", values);
                fillTargetTables();
                getAllRules();
            } else {
                $(".db-info-wrapper").show();
            }
            $(".spinner-holder.initial-spinner").hide();
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

function fillTargetAttributes(tableSelection, interEntityRuleType = false, target) {
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
                    let selectionTarget = "";
                    if (interEntityRuleType) {
                        selectionTarget = target === "define" ? $(".new-rule-wrapper #custInput2") : $(".update-rule-wrapper #custInput2");
                    } else {
                        selectionTarget = target === "define" ? $(".new-rule-wrapper .attribute-selection") : $(".update-rule-wrapper .attributeselection");
                    }

                    console.log(selectionTarget);

                    $(selectionTarget).empty();
                    for (const index in response.Attributes) {
                        $(selectionTarget).append("<option value='" + index + ' - ' + response.Attributes[index] + "'>" + index + "</option>");
                    }
                }
            }
        )
    ;
}

function fillOperators(type, target) {
    fetch("define/types/" + $(type).val() + "/operators", {
        method: "GET",
        headers: {"Authorization": sessionStorage.getItem("access_token")}
    })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            } else if (response.status === 404) {
                return response.text();
            }
        })
        .then(response => {
            if (response !== undefined) {
                const operatorSelection = target === "define" ? $(".new-rule-wrapper .operator-selection") : $(".update-rule-wrapper .operator-selection");
                operatorSelection.empty();
                if (typeof response == 'string' || response instanceof String) {
                    $(operatorSelection).append("<option value='null'>No operators associated to rule type</option>");
                } else {
                    for (const index in response.Operators) {
                        $(operatorSelection).append("<option value='" + response.Operators[index] + "'>" + response.Operators[index] + "</option>");
                    }
                }
            }
        });
}

function checkTypeSelected(valuesArray, type) {
    return valuesArray.indexOf(type.trim()) > -1;
}

function setAttributeValues(value, type, isLiteral) {
    const attributeValues = {};

    attributeValues["value"] = value;
    attributeValues["valueType"] = type;
    attributeValues["isLiteral"] = isLiteral;

    return attributeValues;
}

function saveRule(element) {
    const target = $(element.target).parent().parent();
    const selectedTable = $(target).find(".table-selection").val();
    const selectedType = $(target).find(".type-selection").val();

    let attributes = {};
    const attributeValuesArray = [];
    let attributeItem = {};

    if (checkTypeSelected(["Tuple_Other", "Entity_Other"], selectedType)) {
        $(target).find("ul.attributes-list li").each((index, item) => {
            attributeValuesArray.push(setAttributeValues($(item).html().split("|")[3], $(item).html().split("|")[1].split("-")[1].trim(), true));
        });
    } else if (checkTypeSelected(["Attribute_Range"], selectedType)) {
        $("[id^=custInput]").each((index, item) => {
            attributeValuesArray.push(setAttributeValues($(item).val(), "NUMBER", true));
        });
    } else if (checkTypeSelected(["Attribute_Compare", "Attribute_Other"], selectedType)) {
        attributeValuesArray.push(setAttributeValues($("#custInput1").val(), "VARCHAR2", true));
    } else if (checkTypeSelected(["Attribute_List"], selectedType)) {
        $(target).find("ul.attributes-list li").each((index, item) => {
            const type = isNaN($(item).html().trim()) ? "VARCHAR2" : "NUMBER";
            attributeValuesArray.push(setAttributeValues($(item).html().trim(), type, true));
        });
    } else if (checkTypeSelected(["Tuple_Compare"], selectedType)) {
        $(target).find("ul.attributes-list li").each((index, item) => {
            const type = isNaN($(item).html().trim()) ? "VARCHAR2" : "NUMBER";
            attributeValuesArray.push(setAttributeValues($(item).html().trim(), type, true));
        });
    }

    attributeItem["column"] = $(".attribute-selection").val().split("-")[0].trim();
    attributeItem["operatorName"] = $(target).find(".operator-selection").val();
    attributeItem["attributeValues"] = attributeValuesArray;

    attributes = attributeItem;

    let values = {};
    values["ruleName"] = $(target).find(".rule-name").val();
    values["description"] = $(target).find(".rule-description").val();
    values["tableName"] = selectedTable;
    values["typeName"] = selectedType;
    values["errorMessage"] = $(target).find(".error-message").val();
    values["attributes"] = attributes;
    values = JSON.stringify(values);

    /*
    Dit is de wenselijke input naar de RuleController
    {
      "ruleName": "RuleName",
      "description": "Description",
      "tableName": "KLANTEN",
      "typeName": "Attribute_Compare",
      "errorMessage": "Error message",
      "attributes": [
        {
          "column": "PRIJS",
          "operatorName": "Equals",
          "attributeValues": [
            {
              "value": "10",
              "valueType": "NUMBER",
              "isLiteral": true
            }
          ]
        }
      ]
    }
     */

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
    $(type).parent().parent().parent(".rule-details-wrapper").find(".comparator-step").html("");
    eval(Types[$(type).val()].block);
}

function fillValuesTargetAttributes(element) {
    const typeName = $(element).val();
    const typeNameSplit = typeName.split("_");
    if (typeNameSplit[0].trim() === "InterEntity") {
        $(".new-rule-wrapper .table-selection").change((item) => {
            fillTargetAttributes(item.target, true, "define");
        });
    }
}

function getAllRules() {
    fetch("maintain/rules", {
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
                response = response[0];
                const tableBody = $("#table-body").html("");

                for (const index in response) {
                    let id = response[index]['id'];
                    $(tableBody).append("" +
                        "<tr onclick='clickTable(" + id + ")'>" +
                        "<td>" + id + "</td>" +
                        "<td>" + index + "</td>" +
                        "<td>" + response[index]['table'] + "</td>" +
                        "<td>" + response[index]['type'] + "</td>" +
                        "</tr>"
                    );
                }

                $("table.existing-rules-wrapper").append(tableBody);
                return "ok";
            } else {
                $("#table-body").html("No rules found")
            }
        }).then(response => {
        $(".spinner-holder.maintain-spinner").hide();

        $("table.existing-rules-wrapper").show();

    });
}

function clickTable(id) {
    $(".new-rule-wrapper").show();

    getRuleById(id);
}

function getRuleById(target) {
    fetch("define/rules/" + target, {
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
                //TODO: waardes in FE laden.
                console.log(response);
            }
        });
}
