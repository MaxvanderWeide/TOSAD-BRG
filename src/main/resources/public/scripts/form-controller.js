$(document).ready(function () {
    loadFromStorage();
    fillTypes();
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
        var value = $(this).val().toLowerCase();
        $("#table-body tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    $(".back-define").click(() => {
        showMenu();
    });

    eventListeners();
});

function showMenu() {
    $(".action-button-box").show();
    $(".new-rule-wrapper").hide();
    $(".search-rule-wrapper").hide();
    $(".back-maintain").hide();
    $(".back-define").hide();
    $(".btn-delete").hide();
}

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
                getAllRuleNames();
            } else {
                $(".db-info-wrapper").show();
            }
            $(".spinner-holder").hide();
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
                    $(selection).append("<option value='" + index + ' - ' + response.Attributes[index] + "'>" + index + "</option>");
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

    let attributes = {};
    let attributesValues = {};
    const attributeValuesArray = [];
    let attributeItem = {};

    attributeItem["column"] = $(target).find(".attribute-selection").val().split("-")[0].trim();
    attributeItem["operatorName"] = $(target).find(".operator-selection").val();

    $(target).find("ul.attributes-list li").each((index, item) => {
        attributesValues = {};
        attributesValues["value"] = $(item).html().split("|")[3];
        attributesValues["valueType"] = $(item).html().split("|")[1].split("-")[1].trim();
        attributesValues["isLiteral"] = true;
        attributeValuesArray.push(attributesValues);
    });

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
    const tableSelection = $(type).parent().parent().parent(".rule-details-wrapper").find(".table-selection");
    $(type).parent().parent().parent(".rule-details-wrapper").find(".comparator-step").html("");
    console.log($(type).val());
    eval(Types[$(type).val()].block);
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

                $("table.table").append(tableBody);
            }
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
