$(document).ready(function () {
    eventListeners();
    loadFromStorage();
    fillTypes();
});

function eventListeners() {
    $(".btn-connect").click(() => {
        createConnection();
    });

    $("#tableSelection").unbind("change");
    $("#tableSelection").change((item) => {
        fillTargetAttributes(item.target);
    });

    $("#typeSelection").change((item) => {
        fillOperators(item.target);
        displayBlock(item.target);
        FillValuesTargetAttributes(item.target)
    });

    $(".btn-save").click(() => {
        saveRule();
    })
}

function loadFromStorage() {
    if (sessionStorage.getItem("values") != null) {
        const values = JSON.parse(sessionStorage.getItem("values"));
        $("#dbEngine").append("<option value=" + values['engine'] + "> + values['engine'] + </option>");
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
                $(".rule-details-wrapper").css("display", "block");
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
                $("#tableSelection").empty();
                for (const index in response.Tables) {
                    const value = response.Tables[index];
                    $("#tableSelection").append("<option value='" + value + "'>" + value + "</option>");
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
                    $("#typeSelection").append("<option value='" + index + "'>" + index + "</option>");
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
                const selection = entityRuleType ? $("#custInput1") : $("#attributeSelection");
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
                $("#operatorSelection").empty();
                for (const index in response.Operators) {
                    $("#operatorSelection").append("<option value='" + response.Operators[index] + "'>" + response.Operators[index] + "</option>");
                }
            }
        });
}

function saveRule() {
    const selectedTable = $("#tableSelection").val();
    const selectedType = $("#typeSelection").val();

    const ruleValues = [];

    if (selectedType === "Attribute_List") {
        $("ul.attributes-list li").each((index, item) => {
            ruleValues.push($(item).html());
        });
    } else {
        if (["Tuple", "Entity"].indexOf(selectedType.split("_")[0].trim()) > -1) {
            ruleValues.push(selectedTable);
        }
        $("[id^=custInput]").each((index, item) => {
            ruleValues.push($(item).val());
        });
    }

    let values = {};
    values["ruleName"] = $("#ruleName").val();
    values["description"] = $("#ruleDescription").val();
    values["tableName"] = selectedTable;
    values["typeName"] = selectedType;
    values["targetAttribute"] = $("#attributeSelection").val();
    values["operatorName"] = $("#operatorSelection").val();
    values["errorMessage"] = $("#errorMessage").val();
    values["errorCode"] = $("#errorCode").val();
    values["values"] = ruleValues;
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
    $('#comparatorStep').html("");
    eval(Types[type.options[type.selectedIndex].text].block);
    $("#tableSelection").unbind("change");
    fillTargetAttributes($("#tableSelection"));
}

function FillValuesTargetAttributes(element) {
    const typeName = $(element).val();
    const typeNameSplit = typeName.split("_");
    if (typeNameSplit[0].trim() === "Entity" ||
        typeNameSplit[0].trim() === "Tuple") {
        $("#tableSelection").change((item) => {
            fillTargetAttributes(item.target, item.target);
        });
    }
}
