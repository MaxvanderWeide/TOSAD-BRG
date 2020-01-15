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
    try {
        document.getElementsByClassName('form-step-operator')[0].removeChild(document.getElementById('operatorSelection'));
        document.getElementsByClassName('form-step-operator')[0].removeChild(document.getElementById('operatorSelectionLabel'));
    } catch (e) {
        console.log('No operator selected in previous try');
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
            var selectionLabel = document.createElement("LABEL");
            selectionLabel.setAttribute('for', 'operatorSelection');
            selectionLabel.setAttribute('id', 'operatorSelectionLabel');
            selectionLabel.innerHTML = 'Operator: ';
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
