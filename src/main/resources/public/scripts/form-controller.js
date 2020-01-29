$(document).ready(function () {
    loadFromStorage();
    fillTypes();
    startupEventListeners();
});

$(function () {
    $('[data-toggle="tooltip"]').tooltip()
});

function startupEventListeners() {
    $(".btn-connect").click(() => {
        createConnection();
    });

    $(".maintain-rule").click(() => {
        $(".existing-rules-wrapper").hide();
        $(".maintain-spinner").show();
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

    $(".new-rule-wrapper .type-selection").change((item) => {
        fillOperators(item.target.value);
        displayBlock(item.target.value);
        if ($._data($('.table-selection').get(0), "events").change.length < 3) {
            fillValuesTargetAttributes(item.target);
        }
        $(item.target).parent().parent().parent().find(".rule-values-wrapper").show();
        $(".rule-values-error").hide();
        $("#tableHelp").attr("data-original-title", $(item.target).find("option:selected").attr("title"));
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
        if (!checkFieldsError()) {
            updateRule(item);
        }
    });

    bindTableSelection();
}

function bindTableSelection() {
    $(".new-rule-wrapper .table-selection").change((item) => {
        fillTargetAttributes(item.target.value, false);
        $(".type-selection").removeAttr("disabled");
    });
}

function checkFieldsError() {
    const type = $(".type-selection").val();
    $(".field-error").hide();
    $(".rule-values-error").text("Select / enter all values.");
    let error = false;
    let valuesError = false;
    $(".rule-field").each((index, item) => {
        if ($(item).val() === null || $(item).val().trim() === "") {
            if ($(item).hasClass("value-input")) {
                $(".rule-values-error").show();
            } else {
                $(item).parent().find(".field-error").show();
            }
            error = true;
        }
    });

    switch (type) {
        case "Attribute_Range":
            if (isNaN($("#custInput1").val()) || isNaN($("#custInput2").val())) {
                $(".rule-values-error").text("The values have to be a number");
                valuesError = true;
            } else if (parseInt($("#custInput1").val()) > parseInt($("#custInput2").val())) {
                $(".rule-values-error").text("Minimum value is higher than maximum value");
                valuesError = true;
            }
            break;
        case "Tuple_Compare", "Attribute_List":
            if ($(".attributes-list").html() === "") {
                $(".rule-values-error").text("Enter a value to the list");
                valuesError = true;
            }
            break;
        case "InterEntity_Compare":
            if ($(".target-foreign-key").val() === null || $(".target-foreign-key").val().trim() === "" ||
                $(".other-table-selection").val() === null || $(".other-table-selection").val().trim() === "" ||
                $(".other-table-pk-selection").val() === null || $(".other-table-pk-selection").val().trim() === "" ||
                $(".other-attribute-selection").val() === null || $(".other-attribute-selection").val().trim() === ""
            ) {
                $(".rule-values-error").show();
            }
            break;
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
            if (response.status === 200) {
                showMenu();
                return response.text();
            } else if (response.status === 400) {
                let alertDanger = $('.alert-danger');
                $(alertDanger).html("Can't create connection due to unfulfilled data requirements.");
                $(alertDanger).show();
            } else if (response.status === 403) {
                alert("You can't be authenticated. Contact your technical administrator.");
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

function fillTypes() {
    fetch("define/types", {
        method: "GET",
        headers: {"Authorization": sessionStorage.getItem("access_token")}
    })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            } else if (response.status === 404) {
                let alertDanger = $('.alert-danger');
                $(alertDanger).html("No types were found. Contact your technical administrator.");
                $(alertDanger).show();
            }
        })
        .then(response => {
            if (response !== undefined) {
                for (const index in response.Types) {
                    $(".type-selection").append("<option title='" + response.Types[index] + "' value='" + index + "'>" + index + "</option>");
                }
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
            } else if (response.status === 404) {
                let alertDanger = $('.alert-danger');
                $(alertDanger).html("No tables were found. Contact your technical administrator.");
                $(alertDanger).show();
            } else if (response.status === 403) {
                alert("You can't be authenticated. Please authenticate again.");
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

function fillTargetAttributes(table, interEntityRuleType = false, operation, column) {
    fetch("define/tables/" + table + "/attributes ", {
        method: "GET",
        headers: {"Authorization": sessionStorage.getItem("access_token")}
    })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            } else if (response.status === 404) {
                let alertDanger = $('.alert-danger');
                $(alertDanger).html("No attributes were found. Contact your technical administrator.");
                $(alertDanger).show();
            } else if (response.status === 403) {
                alert("You can't be authenticated. Please authenticate again.");
            }
        })
        .then(response => {
            if (response !== undefined) {
                const selectionTarget = interEntityRuleType ? $(".new-rule-wrapper #custInput2") : $(".new-rule-wrapper .attribute-selection");

                $(selectionTarget).empty();
                for (const index in response.Attributes) {
                    const selected = operation === "maintain" && column === index ? "selected" : "";
                    $(selectionTarget).append("<option " + selected + " value='" + index + ' - ' + response.Attributes[index] + "'>" + index + "</option>");
                }
            }
        });
}

function fillOperators(type, operation = "define", operatorName) {
    fetch("define/types/" + type + "/operators", {
        method: "GET",
        headers: {"Authorization": sessionStorage.getItem("access_token")}
    })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            } else if (response.status === 404) {
                let alertDanger = $('.alert-danger');
                $(alertDanger).html("No operators were found. Contact your technical administrator.");
                $(alertDanger).show();
            }
        })
        .then(response => {
            if (response !== undefined) {
                const operatorSelection = $(".new-rule-wrapper .operator-selection");
                operatorSelection.empty();
                for (const index in response.Operators) {
                    const selected = operation === "maintain" && operatorName === response.Operators[index] ? "selected" : "";
                    $(operatorSelection).append("<option " + selected + " value='" + response.Operators[index] + "'>" + response.Operators[index] + "</option>");
                }
            }
        });
}

function checkTypeSelected(valuesArray, type) {
    return valuesArray.indexOf(type.trim()) > -1;
}

function setAttributeValues(value, type, index, doIndex = false, operation = "insert", id) {
    const attributeValues = {};

    attributeValues["value"] = value;
    attributeValues["valueType"] = type;
    if (doIndex) {
        attributeValues["order"] = index;
    }
    if (operation === "update") {
        attributeValues["id"] = id;
    }

    return attributeValues;
}

function saveRule(element, method = "insert") {
    $(".new-rule-spinner").show();
    $(".alert-success").html("");
    $(".alert-success").hide();
    const target = $(element.target).parent().parent();
    const selectedTable = $(target).find(".table-selection").val();
    const selectedType = $(target).find(".type-selection").val();

    let attributes = {};
    let attributeItems = [];
    const attributeValuesArray = [];

    if (checkTypeSelected(["Tuple_Other"], selectedType)) {
        $(target).find("ul.attributes-list li").each((index, item) => {
            attributeValuesArray.push(setAttributeValues($(item).html().split("|")[3].trim(), $(item).html().split("|")[1].split("-")[1].trim()));
        });
    } else if (checkTypeSelected(["Entity_Other"], selectedType)) {
        const attributeItem = {};

        if (method === "insert") {
            attributeValuesArray.push(setAttributeValues($("#custInput1").val(), "RAW"));
        } else if (method === "update") {
            attributeValuesArray.push(setAttributeValues($("#custInput1").val(), "RAW", 0, false, "update", $("#custInput1").data("value-id")));
            attributeItem["id"] = $(".attribute-id").val();
        }

        attributeItem["column"] = $(".attribute-selection").val().split("-")[0].trim();
        attributeItem["operatorName"] = $(target).find(".operator-selection").val();
        attributeItem["attributeValues"] = attributeValuesArray;
        attributeItems.push(attributeItem);
    } else if (checkTypeSelected(["Attribute_Range"], selectedType)) {
        const attributeItem = {};
        $("[id^=custInput]").each((index, item) => {
            if (method === "insert") {
                attributeValuesArray.push(setAttributeValues($(item).val(), "NUMBER", index, true));
            } else if (method === "update") {
                attributeValuesArray.push(setAttributeValues($(item).val(), "NUMBER", index, true, "update", $(item).data("value-id")));
                attributeItem["id"] = $(".attribute-id").val();
            }
        });
        attributeItem["column"] = $(".attribute-selection").val().split("-")[0].trim();
        attributeItem["operatorName"] = $(target).find(".operator-selection").val();
        attributeItem["attributeValues"] = attributeValuesArray;
        attributeItems.push(attributeItem);
    } else if (checkTypeSelected(["Attribute_Compare", "Attribute_Other"], selectedType)) {
        const attributeItem = {};

        if (method === "insert") {
            attributeValuesArray.push(setAttributeValues($("#custInput1").val(), "VARCHAR2"));
        } else if (method === "update") {
            attributeValuesArray.push(setAttributeValues($("#custInput1").val(), "VARCHAR2", 0, false, "update", $("#custInput1").data("value-id")));
            attributeItem["id"] = $(".attribute-id").val();
        }

        attributeItem["column"] = $(".attribute-selection").val().split("-")[0].trim();
        attributeItem["operatorName"] = $(target).find(".operator-selection").val();
        attributeItem["attributeValues"] = attributeValuesArray;
        attributeItems.push(attributeItem);
    } else if (checkTypeSelected(["Attribute_List"], selectedType)) {
        const attributeItem = {};

        $(target).find("ul.attributes-list li").each((index, item) => {
            const type = isNaN($(item).html().trim()) ? "VARCHAR2" : "NUMBER";
            if (method === "insert") {
                attributeValuesArray.push(setAttributeValues($(item).html().trim(), type));
            } else if (method === "update") {
                attributeValuesArray.push(setAttributeValues($(item).html().trim(), type, 0, false, "update", $(item).data("value-id")));
                attributeItem["id"] = $(".attribute-id").val();
            }
        });

        attributeItem["column"] = $(".attribute-selection").val().split("-")[0].trim();
        attributeItem["operatorName"] = $(target).find(".operator-selection").val();
        attributeItem["attributeValues"] = attributeValuesArray;
        attributeItems.push(attributeItem);
    } else if (checkTypeSelected(["Tuple_Compare"], selectedType)) {
        const attributeItem = {};
        $(target).find("ul.attributes-list li").each((index, item) => {
            const type = $(item).html().split("|")[1].split("-")[1].trim();
            if (method === "insert") {
                attributeValuesArray.push(setAttributeValues($(item).html().split("|")[3].trim(), type));
            } else if (method === "update") {
                attributeValuesArray.push(setAttributeValues($(item).html().split("|")[3].trim(), type, 0, false, "update", $(item).data("value-id")));
                attributeItem["id"] = $(".attribute-id").val();
            }

            attributeItem["column"] = $(item).html().split("|")[1].split("-")[0].trim();
            attributeItem["operatorName"] = $(item).html().split("|")[2].trim();
        });
        attributeItem["attributeValues"] = attributeValuesArray;
        attributeItems.push(attributeItem);
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

    if (method === "insert") {
        fetch("define/rules", {
            method: "POST",
            headers: {"Authorization": sessionStorage.getItem("access_token")},
            body: values
        })
            .then(response => {
                if (response.status === 201) {
                    let alertSuccess = $('.alert-success');
                    $(alertSuccess).html("Your new BusinessRule was created!");
                    $(alertSuccess).show();
                    $(".new-rule-spinner").hide();
                    getAllRules();
                } else if (response.status === 400) {
                    let alertDanger = $('.alert-danger');
                    $(alertDanger).html("Your Business Rule was not created. You may want to check the input again.");
                    $(alertDanger).show();
                } else if (response.status === 403) {
                    alert("You can't be authenticated. Please authenticate again.");
                }
            });
    } else if (method === "update") {
        let id = $('.rule-id').val();
        fetch("maintain/rules/" + id, {
            method: "PUT",
            headers: {"Authorization": sessionStorage.getItem("access_token")},
            body: values
        })
            .then(response => {
                if (response.status === 201) {
                    let alertSuccess = $('.alert-success');
                    $(alertSuccess).html("The rule was updated!");
                    $(alertSuccess).show();
                    $(".new-rule-spinner").hide();
                    getAllRules();
                } else if (response.status === 400) {
                    let alertDanger = $('.alert-danger');
                    $(alertDanger).html("Your Business Rule was not updated. You may want to check the input again.");
                    $(alertDanger).show();
                } else if (response.status === 403) {
                    alert("You can't be authenticated. Please authenticate again.");
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
            if (response.status === 200) {
                let alertSuccess = $('.alert-success');
                $(alertSuccess).html("The BusinessRule was deleted!");
                $(alertSuccess).show();
                clearFormFields();
                $(".new-rule-wrapper").hide();
                getAllRules();
            } else if (response.status === 400) {
                let alertDanger = $('.alert-danger');
                $(alertDanger).html("The rule was not deleted.");
                $(alertDanger).show();
            } else if (response.status === 403) {
                alert("You can't be authenticated. Please authenticate again.");
            } else if (response.status === 404) {
                let alertDanger = $('.alert-danger');
                $(alertDanger).html("The rule was not found. Maybe it was already deleted?");
                $(alertDanger).show();
            }
        });
}

function updateRule(element) {
    saveRule(element, "update");
}

function displayBlock(type) {
    $(".form-step-comparator").html("").append($("<div>", {class: "row comparator-step"}));
    $(".new-rule-wrapper .table-selection").unbind("change");
    bindTableSelection();
    window[type]();
    $(".btn-save, .btn-update").unbind("click");
    $(".btn-save").click((item) => {
        if (!checkFieldsError()) {
            saveRule(item);
        }
    });
    $(".btn-update").click((item) => {
        if (!checkFieldsError()) {
            updateRule(item);
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
            } else if (response.status === 403) {
                alert("You can't be authenticated. Please authenticate again.");
            } else if (response.status === 404) {
                let alertDanger = $('.alert-danger');
                $(alertDanger).html("No Rules Were Found.");
                $(alertDanger).show();
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
        $(".maintain-spinner").hide();

        $("table.existing-rules-wrapper").show();

    });
}

function clickTable(id) {
    $(".new-rule-wrapper").hide();
    $(".maintain-rule-spinner").show();
    clearFormFields();

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
            } else if (response.status === 403) {
                alert("You can't be authenticated. Please authenticate again.");
            } else if (response.status === 404) {
                let alertDanger = $('.alert-danger');
                $(alertDanger).html("The specified rule was not found. Maybe it was deleted?");
                $(alertDanger).show();
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
    $(".attribute-id").val(rule.attributes[0].id);
    $(".rule-name").val(rule.name);
    $(".rule-description").val(rule.description);
    $(".table-selection").val(table);
    $(".type-selection").val(type);

    fillTargetAttributes(table, false, "maintain", rule.attributes[0].column);
    fillOperators(type, "maintain", rule.attributes[0].operatorName);
    displayBlock(type);
    fillFormValues(rule);
    $(".rule-values-wrapper").show();
    $(".error-message").val(rule.errorMessages);
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
    $(".field-error").hide();
    $(".type-selection").attr("disabled", "disabled");
}

function fillFormValues(ruleData) {
    for (const attribute of ruleData.attributes) {
        $(".type-selection").removeAttr("disabled");
        switch (ruleData.type.type) {
            case "Attribute_List":
                for (const value of attribute.attributeValues) {
                    const li = $("<li>", {text: value.value});
                    $(li).attr("data-value-id", value.id);
                    $(li).click((item) => {
                       $(item.target).remove();
                    });
                    $(".attributes-list").append(li);
                }
                break;
            case "Attribute_Range":
                $("#custInput1").val(attribute.attributeValues[0].value);
                $("#custInput1").attr("data-value-id", attribute.attributeValues[0].id);
                $("#custInput2").val(attribute.attributeValues[1].value);
                $("#custInput2").attr("data-value-id", attribute.attributeValues[1].id);
                break;
            case "Attribute_Compare":
                $("#custInput1").val(attribute.attributeValues[0].value);
                $("#custInput1").val(attribute.attributeValues[0].value).attr("data-value-id", attribute.attributeValues[0].id);
                break;
            case "Tuple_Compare":
                for (const value of attribute.attributeValues) {
                    const li = $("<li>", {text: ruleData.table + " | " + attribute.column + " - " + value.valueType + " | " + attribute.operatorName + " | "  +value.value});
                    $(li).attr("data-value-id", value.id);
                    $(li).click((item) => {
                        $(item.target).remove();
                    });
                    $(".attributes-list").append(li);
                }
                break;
            case "InterEntity_Compare":
            //TODO - select correct values
            default:
                break;
        }
    }
    $(".maintain-rule-spinner").hide();
    $(".new-rule-wrapper").show();

}
