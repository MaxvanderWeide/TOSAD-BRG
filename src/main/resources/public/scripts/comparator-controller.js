var Types = {
    Attribute_Range: {
        block:
            "var custInput1 = document.createElement(\"INPUT\");" +
            "custInput1.setAttribute(\"type\", \"text\");" +
            "custInput1.setAttribute(\"id\", \"custInput1\");" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput1);" +
            "var custInput2 = document.createElement(\"INPUT\");" +
            "custInput2.setAttribute(\"type\", \"text\");" +
            "custInput2.setAttribute(\"id\", \"custInput2\");" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput2);",
        reval:
            "new Array(" +
            "document.getElementById(\"custInput1\").value" + "," +
            "document.getElementById(\"custInput2\").value)"
    },
    Attribute_Compare: {
        block:
            "var custInput1 = document.createElement(\"INPUT\");" +
            "custInput1.setAttribute(\"type\", \"text\");" +
            "custInput1.setAttribute(\"id\", \"custInput1\");" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput1);",
        reval:
            "new Array(" +
            "document.getElementById(\"custInput1\").value)"
    },
    Attribute_List: {
        block:
            "var attributeInput = document.createElement(\"INPUT\");" +
            "var addAttribute = document.createElement(\"BUTTON\");" +
            "var br = document.createElement(\"br\");" +
            "addAttribute.innerHTML = \"add\";" +
            "var custInput1 = document.createElement(\"ul\");" +
            "custInput1.setAttribute(\"id\", \"custInput1\");" +
            "custInput1.setAttribute(\"class\", \"attributes-list\");" +
            "custInput1.style.block = \"block\";" +
            "var attributeInput = document.createElement(\"INPUT\");" +
            "addAttribute.addEventListener(\"click\", function() {" +
            "   var li = document.createElement(\"li\");" +
            "   li.innerHTML = attributeInput.value;" +
            "   document.getElementsByClassName(\"attributes-list\")[0].appendChild(li);" +
            "});" +
            "document.getElementById(\"comparatorStep\").appendChild(attributeInput);" +
            "document.getElementById(\"comparatorStep\").appendChild(addAttribute);" +
            "document.getElementById(\"comparatorStep\").appendChild(br);" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput1);",
        reval:
            "var items = new Array();" +
            "for(const li of document.querySelectorAll(\"ul.attributes-list li\")) {" +
            "    items.push(li.textContent);" +
            "}" +
            "items"
    },
    Tuple_Compare: {
        block:
            "var custInput1 = document.createElement(\"SELECT\");" +
            "custInput1.setAttribute(\"id\", \"custInput1\");" +
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
            "var tablesSelection = document.getElementById(\"tableSelection\");" +
            "custInput1.innerHTML = tablesSelection.innerHTML;" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput1);" +
            "var custInput2 = document.createElement(\"SELECT\");" +
            "custInput2.setAttribute(\"id\", \"custInput2\");" +
            "custInput2.addEventListener(\"click\", function() {" +
            "var xhttp = new XMLHttpRequest();\n" +
            "    xhttp.onreadystatechange = function () {\n" +
            "        if (this.readyState == 4 && this.status == 200) {\n" +
            "            console.log('GET SUCCESS: ' + this.responseText);\n" +
            "            var responseJSON = JSON.parse(this.responseText);\n" +
            "            for (var k in responseJSON.Attributes) {\n" +
            "                var option = document.createElement(\"option\");\n" +
            "                option.text = k + ' - ' + responseJSON.Attributes[k];\n" +
            "                custInput2.add(option)\n" +
            "            }\n" +
            "        }\n" +
            "    };\n" +
            "    xhttp.open(\"GET\", 'define/tables/' + custInput1.options[custInput1.selectedIndex].text + '/attributes', true);\n" +
            "    xhttp.send();" +
            "});" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput2);",
        reval:
            "new Array(" +
            "document.getElementById(\"custInput1\").value)" + "," +
            "document.getElementById(\"custInput2\").value)"
    }
};