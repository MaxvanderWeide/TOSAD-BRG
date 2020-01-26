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
    });

    $(".new-rule").click(() => {
        $(".new-rule-wrapper").show();
        $(".action-button-box").hide();
        $(".back-define").show();
    });

    $(".back-maintain").click(() => {
        showMenu();
    });

    $(document).ready(function () {
        $("#search-rule").on("keyup", function () {
            var value = $(this).val().toLowerCase();
            $("#table-body tr").filter(function () {
                $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });
    });

    $(".search-button").click(() => {
        //filter table

        //show form
        $(".new-rule-wrapper").show();
    });

    $(".back-define").click(() => {
        showMenu();
    });

    eventListeners();

});
$(document).ready(function(){
    $("#search-rule").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#table-body tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});

function showMenu() {
    $(".action-button-box").show();
    $(".db-info-wrapper").hide();
    $(".new-rule-wrapper").hide();
    $(".search-rule-wrapper").hide();
    $(".back-maintain").hide();
    $(".back-define").hide();
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
                sessionStorage.setItem("access_token", response);
                sessionStorage.setItem("values", values);
                fillTargetTables();
                getAllRuleNames();
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
                    $(tableBody).append("" +
                        "<tr>" +
                            "<td>" + response[index]['id'] + "</td>" +
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

function getRuleByName(target) {
}
