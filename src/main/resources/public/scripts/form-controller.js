function fillTargetTables() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log('GET SUCCESS: ' + this.responseText);
            var responseJSON = JSON.parse(this.responseText);
            var selection = document.getElementById("tableSelection");
            for (var k in responseJSON.Tables) {
                var option = document.createElement("option");
                option.text = responseJSON.Tables[k];
                selection.add(option)
            }
        }
    };
    xhttp.open("GET", 'define/tables', true);
    xhttp.send();
}

function fillTypes() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log('GET SUCCESS: ' + this.responseText);
            var responseJSON = JSON.parse(this.responseText);
            var selection = document.getElementById("typeSelection");
            for (var k in responseJSON.Types) {
                var option = document.createElement("option");
                option.text = k;
                selection.add(option)
            }
        }
    };
    xhttp.open("GET", 'define/types', true);
    xhttp.send();
}

function fillTargetAttributes(tableSelection) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log('GET SUCCESS: ' + this.responseText);
            var responseJSON = JSON.parse(this.responseText);
            var selection = document.getElementById("attributeSelection");
            selection.options.length = 1;
            for (var k in responseJSON.Attributes) {
                var option = document.createElement("option");
                option.text = k + ' - ' + responseJSON.Attributes[k];
                selection.add(option)
            }
        }
    };
    xhttp.open("GET", 'define/tables/' + tableSelection.options[tableSelection.selectedIndex].text + '/attributes', true);
    xhttp.send();
}

function fillOperators(type) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log('GET SUCCESS: ' + this.responseText);
            var responseJSON = JSON.parse(this.responseText);
            var selection = document.getElementById("operatorSelection");
            selection.options.length = 1;
            for (var k in responseJSON.Operators) {
                var option = document.createElement("option");
                option.text = responseJSON.Operators[k];
                selection.add(option)
            }
        }
    };
    xhttp.open("GET", 'define/types/' + type.options[type.selectedIndex].text + '/operators', true);
    xhttp.send();
}

function fillComparators(operator, type) {

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log('GET SUCCESS: ' + this.responseText);
            var responseJSON = JSON.parse(this.responseText);
            var selection = document.getElementById("comparatorSelection");
            selection.options.length = 1;
            for (var k in responseJSON) {
                var option = document.createElement("option");
                option.setAttribute("reval", responseJSON[k].CodeReval);
                option.setAttribute("block", responseJSON[k].CodeBlock);
                option.text = k;
                selection.add(option)
            }
        }
    };
    xhttp.open("GET", 'define/types/' + type.options[type.selectedIndex].text + '/comparators', true);
    xhttp.send();
}

function evalCodeBlock(comparator) {
    eval(comparator.options[comparator.selectedIndex].getAttribute("block"))
}


function saveRule() {
    var selectedTable = document.getElementById("tableSelection");
    var selectedTableName = selectedTable.options[selectedTable.selectedIndex].value;

    var selectedType = document.getElementById("typeSelection");
    var selectedTypeName = selectedType.options[selectedType.selectedIndex].value;

    var selectedTargetAttribute = document.getElementById("attributeSelection");
    var selectedTargetAttributeName = selectedTargetAttribute.options[selectedTargetAttribute.selectedIndex].value;

    var selectedOperator = document.getElementById("operatorSelection");
    var selectedOperatorName = selectedOperator.options[selectedOperator.selectedIndex].value;

    var selectedComparator = document.getElementById("comparatorSelection");
    var selectedComparatorName = selectedComparator.options[selectedComparator.selectedIndex].value;

    var comparator = document.getElementById("comparatorSelection");

    var values = {};
    values["tableName"] = selectedTableName;
    values["typeName"] = selectedTypeName;
    values["targetAttribute"] = selectedTargetAttributeName;
    values["operatorName"] = selectedOperatorName;
    values["selectedComparatorName"] = selectedComparatorName;
    values["comparatorValues"] = eval(comparator.options[comparator.selectedIndex].getAttribute('reval'));
    values = JSON.stringify(values);

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log('POST SUCCESS: ' + this.responseText);
        }
    };
    xhttp.open("POST", 'define/rules', true);
    xhttp.send(values);
}
