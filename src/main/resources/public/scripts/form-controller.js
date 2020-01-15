function fillSelection() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
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

function getOperatorsWithType() {
    // TODO - Change to existing element instead of creating a new one every time.
    try {
        document.getElementsByClassName('form-step-operator')[0].removeChild(document.getElementById('operatorSelection'));
        document.getElementsByClassName('form-step-operator')[0].removeChild(document.getElementById('operatorSelectionLabel'));
    } catch (e) {
        console.info('No operator selected in previous try');
    }
    var e = document.getElementById('typeSelection');
    var text = e.options[e.selectedIndex].text;

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            console.log('GET SUCCESS: ' + this.responseText);
            var responseJSON = JSON.parse(this.responseText);
            var selection = document.createElement("SELECT");
            selection.setAttribute('id', 'operatorSelection');
            selection.setAttribute('class', 'form-input');
            selection.onchange = getComparatorsWithTypeAndOperator;
            var selectionLabel = document.createElement("LABEL");
            selectionLabel.setAttribute('for', 'operatorSelection');
            selectionLabel.setAttribute('id', 'operatorSelectionLabel');
            selectionLabel.innerHTML = 'Operator: ';
            var disabledOption = document.createElement("option");
            disabledOption.text = 'Select an operator';
            disabledOption.disabled = true;
            disabledOption.selected = true;
            selection.add(disabledOption);
            for (var k in responseJSON.Operators) {
                var option = document.createElement("option");
                option.text = responseJSON.Operators[k];
                selection.add(option)
            }
            document.getElementsByClassName('form-step-operator')[0].appendChild(selectionLabel);
            document.getElementsByClassName('form-step-operator')[0].appendChild(selection);
        }
    };
    xhttp.open("GET", 'types/operators/' + text, true);
    xhttp.send();
}

function getComparatorsWithTypeAndOperator() {
    try {
        document.getElementsByClassName('form-step-comparator')[0].removeChild(document.getElementById('comparatorSelection'));
        document.getElementsByClassName('form-step-comparator')[0].removeChild(document.getElementById('comparatorSelection'));
    } catch (e) {
        console.info('No comparator selected in previous try');
    }
    var e = document.getElementById('typeSelection');
    var text = e.options[e.selectedIndex].text;
    var t = document.getElementById('operatorSelection');
    var textt = t.options[t.selectedIndex].text;

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            console.log('GET SUCCESS: ' + this.responseText);
            var responseJSON = JSON.parse(this.responseText);
            var selection = document.createElement("SELECT");
            selection.setAttribute('id', 'comparatorSelection');
            selection.setAttribute('class', 'form-input');
            var selectionLabel = document.createElement("LABEL");
            selectionLabel.setAttribute('for', 'comparatorSelection');
            selectionLabel.setAttribute('id', 'comparatorSelectionLabel');
            selectionLabel.innerHTML = 'Comparator: ';
            var disabledOption = document.createElement("option");
            disabledOption.text = 'Select a comparator';
            disabledOption.disabled = true;
            disabledOption.selected = true;
            selection.add(disabledOption);
            for (var k in responseJSON.Comparators) {
                var option = document.createElement("option");
                option.text = responseJSON.Comparators[k];
                selection.add(option)
            }
            document.getElementsByClassName('form-step-comparator')[0].appendChild(selectionLabel);
            document.getElementsByClassName('form-step-comparator')[0].appendChild(selection);
        }
    };
    xhttp.open("GET", 'types/operators/' + text + '/comparators/' + textt, true);
    xhttp.send();
}
