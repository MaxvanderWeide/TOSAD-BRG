var Types = {
    Attribute_Range: {
        block:
            "const custInput1 = $(\"<input>\", {type: \"text\", id: \"custInput1\", class:\"form-input col-md-5 mb-3\"});" +
            "const custInput2 = $(\"<input>\", {type: \"text\", id: \"custInput2\", class:\"form-input col-md-5 mb-3\"});" +
            "$(\".new-rule-wrapper\").find(\".comparator-step\").append(custInput1, custInput2);",
    },
    Attribute_Compare: {
        block:
            "const custInput1 = $(\"<input>\", {type: \"text\", id: \"custInput1\", class:\"form-input col-md-5 mb-3\"});" +
            "$(\".new-rule-wrapper\").find(\".comparator-step\").append(custInput1);",
    },
    Attribute_List: {
        block:
            "const attributeInput = $(\"<input>\", {class: \"form-input attr-lst-input\"});" +
            "const addAttribute = $(\"<button>\", {class: \"btn-success col-md-3 mb-3 attr-lst-btn\", text: \"add\"});" +
            "const br = $(\"<br>\");" +
            "const custInput1 = $(\"<ul>\", {id: \"custInput1\", class: \"attributes-list col-md-6 mb-3\"});" +
            "$(addAttribute).click(() => {" +
            "   const li = $(\"<li>\", {text: $(attributeInput).val()});" +
            "    $(\".new-rule-wrapper\").find(\".attributes-list\").append(li);" +
            "});" +
            "" +
            "$(\".new-rule-wrapper\").find(\".comparator-step\").append(attributeInput, addAttribute, br, custInput1);",
    },
    Tuple_Compare: {
        block:
            "const attributeInput = $(\"<input>\");" +
            "const addAttribute = $(\"<button>\", {class: \"btn-success col-md-3 mb-3\", text: \"add\"});" +
            "const br = $(\"<br>\");" +
            "const custInput1 = $(\"<ul>\", {id: \"custInput1\", class: \"attributes-list form-input col-md-6 mb-3\"});" +
            "$(addAttribute).click(() => {" +
            "   const li = $(\"<li>\", {text: $(attributeInput).val()});" +
            "    $(\".new-rule-wrapper\").find(\".attributes-list\").append(li);" +
            "});" +
            "$(\".new-rule-wrapper\").find(\".comparator-step\").append(attributeInput, addAttribute, br, custInput1);"
    },
    InterEntity_Compare: {
        block:
            "const pkLabel = $(\"<label>\", {text: \"Select the target table primary key\"});\n" +
            "const fkLabel = $(\"<label>\", {text: \"Select the target table foreign key\"});\n" +
            "const pkSelect = $(\"<select>\", {class: \"target-primary-key form-input\"});\n" +
            "const fkSelect = $(\"<select>\", {class: \"target-foreign-key form-input\"});\n" +
            "const firstRowFirstCol = $(\"<div>\", {class: \"col-md-6 mb-3\"});\n" +
            "const firstRowSecondCol = $(\"<div>\", {class: \"col-md-6 mb-3\"});\n" +
            "\n" +
            "const secondRow = $(\"<div>\", {class: \"row comparator-step-second\"});\n" +
            "const secondRowFirstCol = $(\"<div>\", {class: \"col-md-6 mb-3\"});\n" +
            "const secondRowSecondCol = $(\"<div>\", {class: \"col-md-6 mb-3\"});\n" +
            "const otherTableLabel = $(\"<label>\", {text: \"Other target table\"});\n" +
            "const otherAttributeLabel = $(\"<label>\", {text: \"Other target attribute\"});\n" +
            "const otherTableSelection = $(\"<select>\", {class: \"other-table-selection form-input\"});\n" +
            "const otherAttributeSelection = $(\"<select>\", {class: \"other-attribute-selection form-input\"});\n" +
            "\n" +
            "function fillIEAttributes(target) {\n" +
            "    const tableSelection = target === \"entity\" ? $(\".table-selection\").val() : $(\".other-table-selection\").val();\n" +
            "    fetch(\"define/tables/\" + tableSelection + \"/attributes \", {\n" +
            "        method: \"GET\",\n" +
            "        headers: {\"Authorization\": sessionStorage.getItem(\"access_token\")}\n" +
            "    })\n" +
            "        .then(response => {\n" +
            "            if (response.status === 200) {\n" +
            "                return response.json();\n" +
            "            }\n" +
            "        })\n" +
            "        .then(response => {\n" +
            "            if (response !== undefined) {\n" +
            "                if (target === \"entity\") {" +
            "                    $(fkSelect).empty();\n" +
            "                    $(pkSelect).empty();\n" +
            "                } else {" +
            "                    $(otherAttributeSelection).empty();" +
            "                }" +
            "                for (const index in response.Attributes) {\n" +
            "                    if (target === \"entity\") {\n" +
            "                        $(fkSelect).append($(\"<option>\", {value: index + ' - ' + response.Attributes[index], text: index}));\n" +
            "                        $(pkSelect).append($(\"<option>\", {value: index + ' - ' + response.Attributes[index], text: index}));\n" +
            "                    } else {\n" +
            "                        $(otherAttributeSelection).append($(\"<option>\", {value: index + ' - ' + response.Attributes[index], text: index}));\n" +
            "                    }\n" +
            "                }\n" +
            "                return \"ok\";\n" +
            "            }\n" +
            "        }).then(response => {\n" +
            "        if (response === \"ok\") {\n" +
            "            $(firstRowFirstCol).append(pkLabel, pkSelect);\n" +
            "            $(firstRowSecondCol).append(fkLabel, fkSelect);\n" +
            "            $(\".new-rule-wrapper\").find(\".comparator-step\").append(firstRowFirstCol, firstRowSecondCol);\n" +
            "        }\n" +
            "    });\n" +
            "}\n" +
            "\n" +
            "fillIEAttributes(\"entity\");\n" +
            "\n" +
            "fetch(\"define/tables\", {\n" +
            "    method: \"GET\",\n" +
            "    headers: {\"Authorization\": sessionStorage.getItem(\"access_token\")}\n" +
            "})\n" +
            "    .then(response => {\n" +
            "        if (response.status === 200) {\n" +
            "            return response.json();\n" +
            "        }\n" +
            "    })\n" +
            "    .then(response => {\n" +
            "        if (response !== undefined) {\n" +
            "            $(otherTableSelection).append(\"<option>Select a table</option>\");\n" +
            "            for (const index in response.Tables) {\n" +
            "                const value = response.Tables[index];\n" +
            "                $(otherTableSelection).append(\"<option value='\" + value + \"'>\" + value + \"</option>\");\n" +
            "            }\n" +
            "            return \"ok\";\n" +
            "        }\n" +
            "    }).then(response => {\n" +
            "    if (response === \"ok\") {\n" +
            "        $(secondRowFirstCol).append(otherTableLabel, otherTableSelection);\n" +
            "        $(secondRowSecondCol).append(otherAttributeLabel, otherAttributeSelection);\n" +
            "        $(secondRow).append(secondRowFirstCol, secondRowSecondCol);\n" +
            "        $(\".new-rule-wrapper\").find(\".form-step-comparator\").append(secondRow);\n" +
            "\n" +
            "        $(\".other-table-selection\").change(() => {\n" +
            "           fillIEAttributes(\"interentity\");\n" +
            "        });\n" +
            "    }\n" +
            "});\n" +
            "\n" +
            "$(\".table-selection\").change(() => {\n" +
            "    fillIEAttributes(\"entity\");\n" +
            "});"
    },
    Entity_Other: {
        block:
            "const attributeInput = $(\"<input>\", {class: \"form-input attr-lst-input\"});" +
            "const addAttribute = $(\"<button>\", {class: \"btn-success col-md-3 mb-3 attr-lst-btn\", text: \"add\"});" +
            "const br = $(\"<br>\");" +
            "const custInput1 = $(\"<ul>\", {id: \"custInput1\", class: \"attributes-list col-md-6 mb-3\"});" +
            "let count = 1;" +
            "$(addAttribute).click(() => {" +
            "   const li = $(\"<li>\", {text: $(\".table-selection\").val() + \"|\" + $(\".attribute-selection\").val() + \"|\" + $(\".operator-selection\").val() + \"|\" +  $(attributeInput).val()});" +
            "    $(li).attr(\"data-number\", count);" +
            "$(\".new-rule-wrapper\").find(\".attributes-list\").append(li);" +
            "count++;" +
            "});" +
            "$(\".new-rule-wrapper\").find(\".comparator-step\").append(attributeInput, addAttribute, br, custInput1);",
    },
    Tuple_Other: {
        block:
            "const attributeInput = $(\"<input>\", {class: \"form-input attr-lst-input\"});" +
            "const addAttribute = $(\"<button>\", {class: \"btn-success col-md-3 mb-3 attr-lst-btn\", text: \"add\"});" +
            "const br = $(\"<br>\");" +
            "const custInput1 = $(\"<ul>\", {id: \"custInput1\", class: \"attributes-list col-md-6 mb-3\"});" +
            "$(addAttribute).click(() => {" +
            "   const li = $(\"<li>\", {text: $(\".table-selection\").val() + \"|\" + $(\".attribute-selection\").val() + \"|\" + $(\".operator-selection\").val() + \"|\" +  $(attributeInput).val()});" +
            "    $(\".new-rule-wrapper\").find(\".attributes-list\").append(li);" +
            "});" +
            "$(\".new-rule-wrapper\").find(\".comparator-step\").append(attributeInput, addAttribute, br, custInput1);",
    },
    Attribute_Other: {
        block:
            "const custInput1 = $(\"<textarea>\", {id: \"custInput1\", class: \"other-rule form-input col-md-5 mb-3\"});" +
            "$(\".new-rule-wrapper\").find(\".comparator-step\").append(custInput1);"
    }
};