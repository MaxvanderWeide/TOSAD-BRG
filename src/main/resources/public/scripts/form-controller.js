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
        $(".new-rule-header").hide();
        $(".btn-update").show();
        $(".btn-save").hide();
        clearFormFields();
        getAllRules();
    });

    $(".new-rule").click(() => {
        $(".new-rule-wrapper").show();
        $(".action-button-box").hide();
        $(".back-define").show();
        $(".new-rule-header").show();
        $(".btn-save").show();
        clearFormFields();
    });

    $(".back-maintain").click(() => {
        showMenu();
    });

    $(".back-define").click(() => {
        showMenu();
    });

    $("#search-rule").on("keyup", function () {
        const value = $(this).val().toLowerCase();
        $("#table-body tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    $(".new-rule-wrapper .table-selection").change((item) => {
        fillTargetAttributes(item.target.value, false);
    });

    $(".new-rule-wrapper .type-selection").change((item) => {
        fillOperators(item.target.value);
        displayBlock(item.target.value);
        if ($._data($('.table-selection').get(0), "events").change.length < 3) {
            fillValuesTargetAttributes(item.target);
        }
        $(item.target).parent().parent().parent().find(".rule-values-wrapper").show();
    });

    $(".btn-save").click((item) => {
       if (!checkFieldsError()) {
           saveRule(item);
       }
    });

    $(".btn-delete").click((item) => {
        deleteRule(item);
    });

    $(".btn-update").click((item) => {
        updateRule(item);
    });


}

function checkFieldsError() {
        const type = $(".type-selection").val();
        $(".field-error").hide();
        $(".rule-values-error").text("Select / enter all values.");
        let error = false;
        let valuesError = false;
        $(".rule-field").each((index, item) => {
            if ($(item).val() === null) {
                $(item).parent().find(".field-error").show();
                error = true;
                return false;
            } else if ($(item).val().trim() === "") {
                $(item).parent().find(".field-error").show();
                error = true;
                return false;
            }
        });

        switch (type) {
            case "Attribute_Range":
                if (isNaN($("#custInput1").val()) || isNaN($("#custInput2").val())) {
                    $(".rule-values-error").text("The values have to be a number");
                    valuesError = true;
                } else if ($("#custInput1").val() > $("#custInput2").val()) {
                    $(".rule-values-error").text("Minimum value is higher than maximum value");
                    valuesError = true;
                }
            default:
                break;
        }

        if (valuesError) {
            error = true;
            $(".rule-values-error").show();
        }

        return error;
}

function showMenu() {
    $(".action-button-box").show();
    $(".new-rule-wrapper").hide();
    $(".search-rule-wrapper").hide();
    $(".back-maintain").hide();
    $(".back-define").hide();
    $(".btn-delete").hide();
    $(".btn-update").hide();
    $(".alert-success").hide();
    $(".alert-danger").hide();
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

function fillTargetAttributes(table, interEntityRuleType = false) {
    fetch("define/tables/" + table + "/attributes ", {
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
                const selectionTarget = interEntityRuleType ? $(".new-rule-wrapper #custInput2") : $(".new-rule-wrapper .attribute-selection");

                $(selectionTarget).empty();
                for (const index in response.Attributes) {
                    $(selectionTarget).append("<option value='" + index + ' - ' + response.Attributes[index] + "'>" + index + "</option>");
                }
            }
        });
}

function fillOperators(type) {
    fetch("define/types/" + type + "/operators", {
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
                const operatorSelection = $(".new-rule-wrapper .operator-selection");
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

function setAttributeValues(value, type, index, doIndex = false) {
    const attributeValues = {};

    attributeValues["value"] = value;
    attributeValues["valueType"] = type;
    if (doIndex) {
        attributeValues["order"] = index;
    }

    return attributeValues;
}

function saveRule(element, method="insert") {
    const target = $(element.target).parent().parent();
    const selectedTable = $(target).find(".table-selection").val();
    const selectedType = $(target).find(".type-selection").val();

    let attributes = {};
    let attributeItems = [];
    const attributeValuesArray = [];

    if (checkTypeSelected(["Tuple_Other", "Entity_Other"], selectedType)) {
        $(target).find("ul.attributes-list li").each((index, item) => {
            attributeValuesArray.push(setAttributeValues($(item).html().split("|")[3], $(item).html().split("|")[1].split("-")[1].trim()));
        });
    } else if (checkTypeSelected(["Attribute_Range"], selectedType)) {
        const attributeItem = {};
        $("[id^=custInput]").each((index, item) => {
            attributeValuesArray.push(setAttributeValues($(item).val(), "NUMBER", index, true));
        });
        attributeItem["column"] = $(".attribute-selection").val().split("-")[0].trim();
        attributeItem["operatorName"] = $(target).find(".operator-selection").val();
        attributeItem["attributeValues"] = attributeValuesArray;
        attributeItems.push(attributeItem);
    } else if (checkTypeSelected(["Attribute_Compare", "Attribute_Other"], selectedType)) {
        const attributeItem = {};

        attributeValuesArray.push(setAttributeValues($("#custInput1").val(), "VARCHAR2"));
        attributeItem["column"] = $(".attribute-selection").val().split("-")[0].trim();
        attributeItem["operatorName"] = $(target).find(".operator-selection").val();
        attributeItem["attributeValues"] = attributeValuesArray;
        attributeItems.push(attributeItem);
    } else if (checkTypeSelected(["Attribute_List"], selectedType)) {
        const attributeItem = {};

        $(target).find("ul.attributes-list li").each((index, item) => {
            const type = isNaN($(item).html().trim()) ? "VARCHAR2" : "NUMBER";
            attributeValuesArray.push(setAttributeValues($(item).html().trim(), type));
        });

        attributeItem["column"] = $(".attribute-selection").val().split("-")[0].trim();
        attributeItem["operatorName"] = $(target).find(".operator-selection").val();
        attributeItem["attributeValues"] = attributeValuesArray;
        attributeItems.push(attributeItem);
    } else if (checkTypeSelected(["Tuple_Compare"], selectedType)) {
        $(target).find("ul.attributes-list li").each((index, item) => {
            const attributeItem = {};
            const type = $(item).html().split("|")[1].split("-")[1].trim();
            attributeItem["column"] = $(item).html().split("|")[1].split("-")[0].trim();
            attributeItem["operatorName"] = $(item).html().split("|")[2];
            attributeItem["attributeValues"] = setAttributeValues($(item).html().split("|")[3], type);
            attributeItems.push(attributeItem);
        });
    } else if (checkTypeSelected["InterEntity_Compare"], selectedType) {
        const attributeItem = {};
        attributeItem["targetTableFK"] = $(".target-foreign-key").val().split("-")[0].trim();
        attributeItem["otherTablePK"] = $(".other-table-pk-selection").val().split("-")[0].trim();
        attributeItem["column"] = $(".attribute-selection").val().split("-")[0].trim();
        attributeItem["otherTable"] = $(".other-table-selection").val().split("-")[0].trim();
        attributeItem["otherColumn"] = $(".other-attribute-selection ").val().split("-")[0].trim();
        attributeItem["operatorName"] = $(target).find(".operator-selection").val();
        attributeItems.push(attributeItem);
    }

    attributes = attributeItems;

    let values = {};
    values["ruleName"] = $(target).find(".rule-name").val();
    values["description"] = $(target).find(".rule-description").val();
    values["tableName"] = selectedTable;
    values["typeName"] = selectedType;
    values["errorMessage"] = $(target).find(".error-message").val();
    values["attributes"] = attributes;
    values = JSON.stringify(values);

    if(method === "insert") {
        fetch("define/rules", {
            method: "POST",
            headers: {"Authorization": sessionStorage.getItem("access_token")},
            body: values
        })
            .then(response => {
                if (response.status === 201) {
                    let alertSuccess = $('.alert-success');
                    alertSuccess.val();
                    alertSuccess.append("Your new BusinessRule was created!");
                    alertSuccess.show();
                } else if (response.status === 400) {
                    let alertDanger = $('.alert-danger');
                    alertDanger.val();
                    alertDanger.append("Your Business Rule was not created! You may want to recheck your input...");
                    alertDanger.show();
                }
            });
    } else if(method === "update") {
        let id = $('.rule-id').val();
        fetch("maintain/rules/"+id, {
            method: "PUT",
            headers: {"Authorization": sessionStorage.getItem("access_token")},
            body: values
        })
            .then(response => {
                if (response.status === 201) {
                    let alertSuccess = $('.alert-success');
                    alertSuccess.val();
                    alertSuccess.append("The rule was updated!");
                    alertSuccess.show();
                } else if (response.status === 400) {
                    let alertDanger = $('.alert-danger');
                    alertDanger.val();
                    alertDanger.append("Your Business Rule was not updated! You may want to recheck your input...");
                    alertDanger.show();
                }
            });
    }
}

function deleteRule(element) {
    let id = $('.rule-id').val();
    fetch("maintain/rules/" + id, {
        method: "DELETE",
        headers: {"Authorization": sessionStorage.getItem("access_token")},
    })
        .then(response => {
            if (response.status === 201) {
                let alertSuccess = $('.alert-success');
                alertSuccess.val();
                alertSuccess.append("The BusinessRule was deleted!");
                alertSuccess.show();
            } else if (response.status === 400) {
                let alertDanger = $('.alert-danger');
                alertDanger.val();
                alertDanger.append("The rule was not deleted...");
                alertDanger.show();
            }
        });
}

function updateRule(element) {
    // alert("Not implemented yet...")
    saveRule(element, "update");
}

function displayBlock(type) {
    $(".form-step-comparator").html("").append($("<div>", {class: "row comparator-step"}));
    eval(Types[type].block);
    $(".btn-save").unbind("click");
    $(".btn-save").click((item) => {
        if (!checkFieldsError()) {
            saveRule(item);
        }
    });
}

function fillValuesTargetAttributes(element) {
    const typeName = $(element).val();
    const typeNameSplit = typeName.split("_");
    if (typeNameSplit[0].trim() === "InterEntity") {
        $(".new-rule-wrapper .table-selection").change((item) => {
            fillTargetAttributes(item.target.value, true);
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
    clearFormFields();
    $(".new-rule-wrapper").show();

    getRuleById(id);
}

function getRuleById(target) {
    fetch("maintain/rules/" + target, {
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
                fillFormFields(response)
            }
        });
}

function fillFormFields(rule) {
    let table = rule.table;
    let type = rule.type.type;
    $(".rule-id").val(rule.id);
    $(".rule-name").val(rule.name);
    $(".rule-description").val(rule.description);
    $(".table-selection").val(table);
    $(".type-selection").val(type);

    fillTargetAttributes(table, false);
    fillOperators(type);
    displayBlock(type);
    fillFormValues(rule);
    $(".rule-values-wrapper").show();
    $(".attribute-selection").val(rule.attributes.column);
    $(".operator-selection").val(rule.attributes.operatorName);
    $(".error-message").val(rule.errorMessages);
    //TODO: Values tonen in define FE
    // $(".custInput1").val();
    // $(".custInput2").val();
}

function clearFormFields() {
    $(".rule-name").val('');
    $(".rule-description").val('');
    $(".table-selection").val('');
    $(".type-selection").val('');
    $(".attribute-selection").val('');
    $(".operator-selection").val('');
    $(".error-message").val('');
    $(".form-step-comparator").html("").append($("<div>", {class: "row comparator-step"}));
    $(".rule-values-wrapper").hide();
}

function fillFormValues(ruleData) {
    for (const attribute of ruleData.attributes) {
        switch (ruleData.type.type) {
            case "Attribute_List":
                for (const value of attribute.attributeValues) {
                    $(".attributes-list").append($("<li>", {text: value.value}));
                }
                break;
            case "Attribute_Range":
                $("#custInput1").val(attribute.attributeValues[0].value);
                $("#custInput2").val(attribute.attributeValues[1].value);
            case "Attribute_Compare":
                $("#custInput1").val(attribute.attributeValues[0].value);
            case "InterEntity_Compare":
            //TODO - select correct values
            default:
                break;
        }
    }
}
