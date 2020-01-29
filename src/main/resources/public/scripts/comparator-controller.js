function Attribute_Range() {
    const custInput1 = $("<input>", {
        type: "text",
        id: "custInput1",
        class: "form-input col-md-5 mb-3 rule-field value-input"
    });
    const custInput2 = $("<input>", {
        type: "text",
        id: "custInput2",
        class: "form-input col-md-5 mb-3 rule-field value-input"
    });
    $(".new-rule-wrapper").find(".comparator-step").append(custInput1, custInput2);
}

function Attribute_Compare() {
    const custInput1 = $("<input>", {
        type: "text",
        id: "custInput1",
        class: "form-input col-md-5 mb-3 rule-field value-input"
    });
    $(".new-rule-wrapper").find(".comparator-step").append(custInput1);
}

function Attribute_List() {
    const attributeInput = $("<input>", {class: "form-input attr-lst-input"});
    const addAttribute = $("<button>", {class: "btn-success col-md-3 mb-3 attr-lst-btn", text: "add"});
    const br = $("<br>");
    const custInput1 = $("<ul>", {id: "custInput1", class: "attributes-list col-md-12 mb-3"});
    $(addAttribute).click(() => {
        const li = $("<li>", {text: $(attributeInput).val()});
        $(".new-rule-wrapper").find(".attributes-list").append(li);
        $(li).click((item) => {
            $(item.target).remove();
        });
    });
    $(".attributes-list li").click((item) => {
        $(item.target).remove();
    });
    $(".new-rule-wrapper").find(".comparator-step").append(attributeInput, addAttribute, br, custInput1);
}

function Tuple_Compare() {
    const attributeInput = $("<input>", {class: "form-input attr-lst-input"});
    const addAttribute = $("<button>", {class: "btn-success col-md-3 mb-3 attr-lst-btn", text: "add"});
    const br = $("<br>");
    const custInput1 = $("<ul>", {id: "custInput1", class: "attributes-list col-md-12 mb-3"});
    $(addAttribute).click(() => {
        if ($(".table-selection").val() !== null && $(".attribute-selection").val() !== null && $(".operator-selection").val() !== null && $(attributeInput).val().trim() !== "") {
            const li = $("<li>", {text: $(".table-selection").val() + " | " + $(".attribute-selection").val() + " | " + $(".operator-selection").val() + " | " + $(attributeInput).val()});
            $(".new-rule-wrapper").find(".attributes-list").append(li);
            $(li).click((item) => {
                $(item.target).remove();
            });
        }
    });
    $(".attributes-list li").click((item) => {
        $(item.target).remove();
    });
    $(".new-rule-wrapper").find(".comparator-step").append(attributeInput, addAttribute, br, custInput1);
}

function InterEntity_Compare() {
    const fkLabel = $("<label>", {text: "Select the target table foreign key"});
    const fkSelect = $("<select>", {class: "target-foreign-key form-input"});
    const firstRowFirstCol = $("<div>", {class: "col-md-6 mb-3"});
    const firstRowSecondCol = $("<div>", {class: "col-md-6 mb-3"});

    const secondRow = $("<div>", {class: "row comparator-step-second"});
    const secondRowFirstCol = $("<div>", {class: "col-md-6 mb-3"});
    const secondRowSecondCol = $("<div>", {class: "col-md-6 mb-3"});
    const otherTableLabel = $("<label>", {text: "Other target table"});
    const otherTableSelection = $("<select>", {class: "other-table-selection form-input"});
    const otherAttributeLabel = $("<label>", {text: "Other target attribute"});
    const otherAttributeSelection = $("<select>", {class: "other-attribute-selection form-input"});
    const otherTablePkLabel = $("<label>", {text: "Other target table primary key attribute"});
    const otherTablePkSelection = $("<select>", {class: "other-table-pk-selection form-input"});

    function fillIEAttributes(target) {
        const tableSelection = target === "entity" ? $(".table-selection").val() : $(".other-table-selection").val();
        fetch("define/tables/" + tableSelection + "/attributes ", {
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
                    if (target === "entity") {
                        $(fkSelect).empty();
                    } else {
                        $(otherAttributeSelection).empty();
                        $(otherTablePkSelection).empty();
                    }
                    for (const index in response.Attributes) {
                        if (target === "entity") {
                            $(fkSelect).append($("<option>", {
                                value: index + ' - ' + response.Attributes[index],
                                text: index
                            }));
                        } else {
                            $(otherAttributeSelection).append($("<option>", {
                                value: index + ' - ' + response.Attributes[index],
                                text: index
                            }));
                            $(otherTablePkSelection).append($("<option>", {
                                value: index + ' - ' + response.Attributes[index],
                                text: index
                            }));
                        }
                    }
                    return "ok";
                }
            }).then(response => {
            if (response === "ok") {
                $(firstRowFirstCol).append(fkLabel, fkSelect);
                $(secondRowFirstCol).append(otherTablePkLabel, otherTablePkSelection);
                $(secondRow).append(secondRowFirstCol, secondRowSecondCol);
                $(".new-rule-wrapper").find(".comparator-step").prepend(firstRowFirstCol);
                $(".new-rule-wrapper").find(".form-step-comparator").append(secondRow);
            }
        });
    }

    fillIEAttributes("entity");

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
                $(otherTableSelection).append("<option>Select a table</option>");
                for (const index in response.Tables) {
                    const value = response.Tables[index];
                    $(otherTableSelection).append("<option value='" + value + "'>" + value + "</option>");
                }
                return "ok";
            }
        }).then(response => {
        if (response === "ok") {
            $(firstRowSecondCol).append(otherTableLabel, otherTableSelection);
            $(secondRowSecondCol).append(otherAttributeLabel, otherAttributeSelection);
            $(secondRow).append(secondRowSecondCol);
            $(".new-rule-wrapper").find(".comparator-step").append(firstRowSecondCol);
            $(".new-rule-wrapper").find(".form-step-comparator").append(secondRow);
            $(".other-table-selection").change(() => {
                fillIEAttributes("interentity");
            });
        }
    });

    $(".table-selection").change(() => {
        fillIEAttributes("entity");
    });
}

function Entity_Other() {
    const custInput1 = $("<textarea placeholder='declarations' class='form-input col-md-5 mb-3 rule-field value-input' name='inputlog'>", {
        type: "text",
        id: "custInput1"
    });
    $(".new-rule-wrapper").find(".comparator-step").append(custInput1);
    const custInput2 = $("<textarea placeholder='insertions' class='form-input col-md-5 mb-3 rule-field value-input' name='inputlog'>", {
        type: "text",
        id: "custInput2"
    });
    $(".new-rule-wrapper").find(".comparator-step").append(custInput2);
    const custInput3 = $("<input placeholder='statement' class='form-input col-md-5 mb-3 rule-field value-input' name='inputlog'>", {
        type: "text",
        id: "custInput3",
    });
    $(".new-rule-wrapper").find(".comparator-step").append(custInput3);
}

function Tuple_Other() {
    const attributeInput = $("<input>", {class: "form-input attr-lst-input"});
    const addAttribute = $("<button>", {class: "btn-success col-md-3 mb-3 attr-lst-btn", text: "add"});
    const br = $("<br>");
    const custInput1 = $("<ul>", {id: "custInput1", class: "attributes-list col-md-12 mb-3"});
    $(addAttribute).click(() => {
        const li = $("<li>", {text: $(".table-selection").val() + "|" + $(".attribute-selection").val() + "|" + $(".operator-selection").val() + "|" + $(attributeInput).val()});
        $(".new-rule-wrapper").find(".attributes-list").append(li);
    });
    $(".new-rule-wrapper").find(".comparator-step").append(attributeInput, addAttribute, br, custInput1);
}

function Attribute_Other() {
    const custInput1 = $("<textarea>", {id: "custInput1", class: "other-rule form-input col-md-5 mb-3"});
    $(".new-rule-wrapper").find(".comparator-step").append(custInput1);
}
