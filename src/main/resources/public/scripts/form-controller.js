function fillSelection() {\
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
    xhttp.open("GET", 'tables', true);
    xhttp.send();

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
    xhttp.open("GET", 'types', true);
    xhttp.send();
}

function fillTargetAttributes(tableSelection) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log('GET SUCCESS: ' + this.responseText);
            var responseJSON = JSON.parse(this.responseText);
            var selection = document.getElementById("attributeSelection");
            selection.options.length = 0;
            for (var k in responseJSON.Attributes) {
                var option = document.createElement("option");
                option.text = k + ' - ' + responseJSON.Attributes[k];
                selection.add(option)
            }
        }
    };
    xhttp.open("GET", 'tables/' + tableSelection.options[tableSelection.selectedIndex].text + '/attributes', true);
    xhttp.send();
}

function fillOperators(type) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log('GET SUCCESS: ' + this.responseText);
            var responseJSON = JSON.parse(this.responseText);
            var selection = document.getElementById("operatorSelection");
            selection.options.length = 0;
            for (var k in responseJSON.Operators) {
                var option = document.createElement("option");
                option.text = responseJSON.Operators[k];
                selection.add(option)
            }
        }
    };
    xhttp.open("GET", 'types/' + type.options[type.selectedIndex].text + '/operators', true);
    xhttp.send();
}
