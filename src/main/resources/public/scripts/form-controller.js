function loadFromStorage() {
    if (sessionStorage.getItem("values") != null) {
        const values = JSON.parse(sessionStorage.getItem("values"));
        document.getElementById("dbEngine").options[dbEngine.selectedIndex].value = values['engine'];
        document.getElementById("dbInputHost").value = values['host'];
        document.getElementById("dbName").value = values['dbName'];
        document.getElementById("dbInputPort").value = values['port'];
        document.getElementById("dbInputService").value = values['service'];
        document.getElementById("dbInputUser").value = values['username'];
        document.getElementById("dbInputPassword").value = values['password'];
    }
}

function createConnection() {
    const dbEngine = document.getElementById("dbEngine");
    const dbEngineName = dbEngine.options[dbEngine.selectedIndex].value;
    const dbHost = document.getElementById("dbInputHost").value;
    const dbName = document.getElementById("dbName").value;
    const dbPort = document.getElementById("dbInputPort").value;
    const dbService = document.getElementById("dbInputService").value;
    const dbUser = document.getElementById("dbInputUser").value;
    const dbPassword = document.getElementById("dbInputPassword").value;

    let values = {};
    values["engine"] = dbEngineName;
    values["dbName"] = dbName;
    values["host"] = dbHost;
    values["port"] = dbPort;
    values["service"] = dbService;
    values["username"] = dbUser;
    values["password"] = dbPassword;
    values = JSON.stringify(values);

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status == 200) {
                sessionStorage.setItem("access_token", this.responseText);
                sessionStorage.setItem("values", values);
                fillTargetTables();
                document.getElementsByClassName("rule-details-wrapper")[0].style.display = "block";
                return;
            }
            alert('Could not authenticate and make a connection')
        }

    };
    xhttp.open("POST", 'auth/connection', true);
    xhttp.send(values);
}

function fillTargetTables() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("Connected!");
            console.log('GET SUCCESS: ' + this.responseText);
            const responseJSON = JSON.parse(this.responseText);
            const selection = document.getElementById("tableSelection");
            for (const k in responseJSON.Tables) {
                const option = document.createElement("option");
                option.text = responseJSON.Tables[k];
                selection.add(option)
            }
        }
    };
    xhttp.open("GET", 'define/tables', true);
    xhttp.setRequestHeader('Authorization', sessionStorage.getItem("access_token"));
    xhttp.send();
}


function fillTypes() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log('GET SUCCESS: ' + this.responseText);
            const responseJSON = JSON.parse(this.responseText);
            const selection = document.getElementById("typeSelection");
            for (const k in responseJSON.Types) {
                const option = document.createElement("option");
                option.text = k;
                selection.add(option)
            }
        }
    };
    xhttp.open("GET", 'define/types', true);
    xhttp.send();
}

function fillTargetAttributes(tableSelection) {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log('GET SUCCESS: ' + this.responseText);
            const responseJSON = JSON.parse(this.responseText);
            const selection = document.getElementById("attributeSelection");
            selection.options.length = 1;
            for (const k in responseJSON.Attributes) {
                const option = document.createElement("option");
                option.text = k + ' - ' + responseJSON.Attributes[k];
                selection.add(option)
            }
        }
    };
    xhttp.open("GET", 'define/tables/' + tableSelection.options[tableSelection.selectedIndex].text + '/attributes', true);
    xhttp.setRequestHeader('Authorization', sessionStorage.getItem("access_token"));
    xhttp.send();
}

function fillOperators(type) {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log('GET SUCCESS: ' + this.responseText);
            const responseJSON = JSON.parse(this.responseText);
            const selection = document.getElementById("operatorSelection");
            selection.options.length = 1;
            for (const k in responseJSON.Operators) {
                const option = document.createElement("option");
                option.text = responseJSON.Operators[k];
                selection.add(option)
            }
        }
    };
    xhttp.open("GET", 'define/types/' + type.options[type.selectedIndex].text + '/operators', true);
    xhttp.send();
}

function saveRule() {
    const selectedTable = document.getElementById("tableSelection");
    const selectedTableName = selectedTable.options[selectedTable.selectedIndex].value;

    const selectedType = document.getElementById("typeSelection");
    const selectedTypeName = selectedType.options[selectedType.selectedIndex].value;

    const selectedTargetAttribute = document.getElementById("attributeSelection");
    const selectedTargetAttributeName = selectedTargetAttribute.options[selectedTargetAttribute.selectedIndex].value;

    const selectedOperator = document.getElementById("operatorSelection");
    const selectedOperatorName = selectedOperator.options[selectedOperator.selectedIndex].value;

    const ruleValues = [];

    for(const li of document.querySelectorAll("ul.attributes-list li")) {
        ruleValues.push(li.textContent);
    }

    let values = {};
    values["ruleName"] = document.getElementById("ruleName").value;
    values["tableName"] = selectedTableName;
    values["typeName"] = selectedTypeName;
    values["targetAttribute"] = selectedTargetAttributeName;
    values["operatorName"] = selectedOperatorName;
    values["comparatorValues"] = getReval(selectedTypeName);
    values["errorMessage"] = document.getElementById("errorMessage").value;
    values["errorCode"] = document.getElementById("errorCode").value;
    values["values"] = ruleValues;
    values = JSON.stringify(values);

    const xhttp = new XMLHttpRequest();
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
    xhttp.setRequestHeader('Authorization', sessionStorage.getItem("access_token"));
    xhttp.send(values);
}

function displayBlock(type) {
    document.getElementById('comparatorStep').innerHTML = "";
    eval(Types[type.options[type.selectedIndex].text].block);
}

function getReval(type) {
    return eval(Types[type].reval);
}
