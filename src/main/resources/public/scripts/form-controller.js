function fillTargetTables() {
    var dbEngine = document.getElementById("dbEngine");
    var dbEngineName = dbEngine.options[dbEngine.selectedIndex].value;
    var dbHost = document.getElementById("dbInputHost").value;
    var dbName = document.getElementById("dbName").value;
    var dbPort = document.getElementById("dbInputPort").value;
    var dbService = document.getElementById("dbInputService").value;
    var dbUser = document.getElementById("dbInputUser").value;
    var dbPassword = document.getElementById("dbInputPassword").value;

    var values = {};
    values["engine"] = dbEngineName;
    values["dbName"] = dbName;
    values["host"] = dbHost;
    values["port"] = dbPort;
    values["service"] = dbService;
    values["username"] = dbUser;
    values["password"] = dbPassword;
    values = JSON.stringify(values);

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("Connected!");
            document.getElementById("db-info-wrapper").style.display = "none";
            document.getElementById("disconnectBtn").style.display = "inline-block";
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
    xhttp.open("POST", 'define/tables', false);
    xhttp.send(values);

    fillTypes();
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
    xhttp.open("GET", 'define/types', false);
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
    values["comparatorValues"] = getReval(selectedTypeName);
    values = JSON.stringify(values);

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status == 201) {
                console.log('POST SUCCESS: ' + this.responseText);
                alert('Rule was created');
            } else if (this.status == 400) {
                console.log('POST SUCCESS: ' + this.responseText);
                alert('Rule was not created');
            }
        }

    };
    xhttp.open("POST", 'define/rules', true);
    xhttp.send(values);
}

function displayBlock(type) {
    document.getElementById('comparatorStep').innerHTML = "";
    eval(Types[type.options[type.selectedIndex].text].block);
}

function getReval(type) {
    return eval(Types[type].reval);
}

function disconnect() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            // console.log('GET SUCCESS: ' + this.responseText);
            // document.getElementById("disconnectBtn").style.display = "none";
            // document.getElementById("db-info-wrapper").style.display = "block";
            window.location.reload();
        }
    };
    xhttp.open("GET", 'define/disconnect', false);
    xhttp.send();
}