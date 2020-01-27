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
            "const attributeInput = $(\"<input>\");" +
            "const addAttribute = $(\"<button>\", {class: \"btn-success col-md-3 mb-3\", text: \"add\"});" +
            "const br = $(\"<br>\");" +
            "const custInput1 = $(\"<ul>\", {id: \"custInput1\", class: \"attributes-list form-input col-md-6 mb-3\"});" +
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
            "const custInput1 = $(\"<div>\", {id: \"custInput1\", class: \"form-input col-md-5 mb-3\"});" +
            "custInput1.html($(\"#tableSelection\").html());" +
            "$(\".new-rule-wrapper\").find(\".comparator-step\").append(custInput1);" +
            "const custInput2 = $(\"<select>\", {id: \"custInput2\", class: \"form-input col-md-5 mb-3\"});" +
            "$(custInput1).change(() => {" +
            "    custInput2.html(\"\");" +
            "    fetch(\"define/tables/\" + $(custInput1).val() + \"/attributes\", {" +
            "        method: \"GET\"," +
            "        headers: {\"Authorization\": sessionStorage.getItem(\"access_token\")}" +
            "    })" +
            "        .then(response => {" +
            "            if (response.status === 200) {" +
            "                return response.json();" +
            "            }" +
            "        })" +
            "        .then(response => {" +
            "            if (response !== undefined) {" +
            "                for (var index in response.Attributes) {" +
            "                    const option = $(\"<option>\", {value: response.Attributes[index], text: response.Attributes[index]});" +
            "                    custInput2.add(option);" +
            "                }" +
            "            }" +
            "        });" +
            "});" +
            "$(\".new-rule-wrapper\").find(\".comparator-step\").append(custInput2)",
    },
    Entity_Other: {
        block:
            "const attributeInput = $(\"<input>\");" +
            "const addAttribute = $(\"<button>\", {class: \"btn-success col-md-3 mb-3\", text: \"add\"});" +
            "const br = $(\"<br>\");" +
            "const custInput1 = $(\"<ul>\", {id: \"custInput1\", class: \"attributes-list form-input col-md-6 mb-3\"});" +
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
            "const attributeInput = $(\"<input>\");" +
            "const addAttribute = $(\"<button>\", {class: \"btn-success col-md-3 mb-3\", text: \"add\"});" +
            "const br = $(\"<br>\");" +
            "const custInput1 = $(\"<ul>\", {id: \"custInput1\", class: \"attributes-list form-input col-md-6 mb-3\"});" +
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