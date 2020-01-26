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
            "$(\".new-rule-wrapper\"].target).find(\".comparator-step\").append(custInput1);",
    },
    Attribute_List: {
        block:
            "const attributeInput = $(\"<input>\");" +
            "const addAttribute = $(\"<button>\", {class: \"btn-success col-md-3 mb-3\", text: \"add\"});" +
            "const br = $(\"<br>\");" +
            "const custInput1 = $(\"<ul>\", {id: \"custInput1\", class: \"attributes-list form-input col-md-6 mb-3\"});" +
            "$(addAttribute).click(() => {" +
            "   const li = $(\"<li>\", {text: $(attributeInput).val()});" +
            "    $(Types[\"Attribute_List\"].target).find(\".attributes-list\").append(li);" +
            "});" +
            "" +
            "$(\".new-rule-wrapper\"].target).find(\".comparator-step\").append(attributeInput, addAttribute, br, custInput1);",
    },
    Tuple_Compare: {
        block:
            "var custInput1 = document.createElement(\"SELECT\");" +
            "custInput1.setAttribute(\"id\", \"custInput1\");" +
            "custInput1.setAttribute(\"class\", \"form-input col-md-5 mb-3\");" +
            "var attributesSelection = document.getElementById(\"attributeSelection\");" +
            "custInput1.innerHTML = attributesSelection.innerHTML;" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput1);",
        reval:
            "new Array(" +
            "document.getElementById(\"custInput1\").value)"
    },
    InterEntity_Compare: {
        block:
            "var custInput1 = document.createElement(\"SELECT\");" +
            "custInput1.setAttribute(\"id\", \"custInput1\");" +
            "custInput1.setAttribute(\"class\", \"form-input col-md-5 mb-3\");" +
            "var tablesSelection = document.getElementById(\"tableSelection\");" +
            "custInput1.innerHTML = tablesSelection.innerHTML;" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput1);" +
            "var custInput2 = document.createElement(\"SELECT\");" +
            "custInput2.setAttribute(\"id\", \"custInput2\");" +
            "custInput2.setAttribute(\"class\", \"form-input col-md-5 mb-3\");" +
            "custInput1.addEventListener(\"change\", function() {" +
            "custInput2.innerHTML = \"\";" +
            "var xhttp = new XMLHttpRequest();" +
            "    xhttp.onreadystatechange = function () {" +
            "        if (this.readyState == 4 && this.status == 200) {" +
            "            var responseJSON = JSON.parse(this.responseText);" +
            "            for (var k in responseJSON.Attributes) {" +
            "                var option = document.createElement(\"option\");" +
            "                option.value = k + ' - ' + responseJSON.Attributes[k];" +
            "                option.text = k + ' - ' + responseJSON.Attributes[k];" +
            "                custInput2.add(option)" +
            "            }" +
            "        }" +
            "    };" +
            "xhttp.open(\"GET\", 'define/tables/' + custInput1.options[custInput1.selectedIndex].text + '/attributes', true);" +
            "xhttp.setRequestHeader('Authorization', sessionStorage.getItem(\"access_token\"));" +
            "xhttp.send();" +
            "});" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput2);",
        reval:
            "new Array(" +
            "document.getElementById(\"custInput1\").value," +
            "document.getElementById(\"custInput2\").value)"
    },
    Entity_Other: {
        block:
            "var custInput1 = document.createElement(\"SELECT\");" +
            "custInput1.setAttribute(\"id\", \"custInput1\");" +
            "custInput1.setAttribute(\"class\", \"form-input col-md-5 mb-3\");" +
            "var br = document.createElement(\"br\");" +
            "var custInput2 = document.createElement(\"TEXTAREA\");" +
            "custInput2.setAttribute(\"id\", \"custInput2\");" +
            "custInput2.setAttribute(\"class\", \"other-rule form-input col-md-5 mb-3\");" +
            "var attributesSelection = document.getElementById(\"attributeSelection\");" +
            "custInput1.innerHTML = attributesSelection.innerHTML;" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput1);" +
            "document.getElementById(\"comparatorStep\").appendChild(br);" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput2);" +
            "document.getElementById(\"tableSelection\").addEventListener(\"change\", function() {" +
            "   custInput1.innerHTML = attributesSelection.innerHTML;" +
            "});",
        reval:
            "new Array(" +
            "document.getElementById(\"custInput1\").value," +
            "document.getElementById(\"custInput2\").value)"
    },
    Tuple_Other: {
        block:
            "var custInput1 = document.createElement(\"SELECT\");" +
            "custInput1.setAttribute(\"id\", \"custInput1\");" +
            "custInput1.setAttribute(\"class\", \"form-input col-md-5 mb-3\");" +
            "var br = document.createElement(\"br\");" +
            "var custInput2 = document.createElement(\"TEXTAREA\");" +
            "custInput2.setAttribute(\"id\", \"custInput2\");" +
            "custInput2.setAttribute(\"class\", \"other-rule form-input col-md-5 mb-3\");" +
            "var attributesSelection = document.getElementById(\"attributeSelection\");" +
            "custInput1.innerHTML = attributesSelection.innerHTML;" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput1);" +
            "document.getElementById(\"comparatorStep\").appendChild(br);" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput2);",
        reval:
            "new Array(" +
            "document.getElementById(\"custInput1\").value," +
            "document.getElementById(\"custInput2\").value)"
    },
    Attribute_Other: {
        block:
            "var custInput1 = document.createElement(\"TEXTAREA\");" +
            "custInput1.setAttribute(\"id\", \"custInput1\");" +
            "custInput1.setAttribute(\"class\", \"other-rule form-input col-md-5 mb-3\");" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput1);",
        reval:
            "new Array(" +
            "document.getElementById(\"custInput1\").value)"
    }
};
