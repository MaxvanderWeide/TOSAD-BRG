$(document).ready(function () {
    getAllRules();
    searchTable();

    $(".generate").click(() => {
        generate();
    });
});

function searchTable() {
    $("#search-rule").on("keyup", function () {
        const value = $(this).val().toLowerCase();
        $("#table-body tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
}

function getAllRules() {
    fetch("rules", {
        method: "GET",
        headers: {"Authorization": sessionStorage.getItem("access_token")}
    })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            }
        })
        .then(response => {
            if (response !== undefined) {
                // response = response[0];
                const tableBody = $("#table-body").html("");

                for (const index in response) {
                    let id = response[index]['id'];
                    $(tableBody).append("" +
                        "<tr >" + /*onclick='clickTable(" + id + ")'*/
                        "<td>" + id + "</td>" +
                        "<td>" + index + "</td>" +
                        "<td>" + response[index]['table'] + "</td>" +
                        "<td>" + response[index]['type']['type'] + "</td>" +
                        "<td data-id='"+ id +"'><input class='form-check-input' value='"+ id +"' type='checkbox' id='checkbox'></td>" +
                        "</tr>"
                    );
                }

                // showRuleOnClick(); TODO - Weghalen??
                $("table.existing-rules-wrapper").append(tableBody);
                $("#ruleTable, .generate, .existing-lead").show();
                return "ok";
            } else {
                $("#table-body").html("No rules found")
            }
        }).then(response => {
        $(".spinner-holder.initial-spinner").hide();
        $("table.existing-rules-wrapper").show();
    });
}

function getRuleById(target) {
    fetch("maintain/rules/" + target, {
        method: "GET",
        headers: {"Authorization": sessionStorage.getItem("access_token")}
    })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            }
        })
        .then(response => {
            if (response !== undefined) {
                fillFormFields(response)
            }
        });
}

function generate() {
    $(".generate-spinner").show();
    let ids = [];
    let boxes = $('input[type=checkbox]');

    // get checked boxes
    boxes.each(function () {
        if (this.checked) {
            ids.push($(this).val());
        }
    });

    const rules = {};
    rules["rules"] = ids;

    //generate the rules
        fetch("/generate/rules", {
            method: "POST",
            headers: {"Authorization": sessionStorage.getItem("access_token")},
            body: JSON.stringify(rules)
        })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else if (response.status === 404) {
                    let alertDanger = $('.alert-danger');
                    $(alertDanger).html("Contact your technical administrator.");
                    $(alertDanger).show();
                }
            })
            .then(response => {
                if (response !== undefined) {
                    // show generated items...
                    console.log(response);
                    let alertSuccess = $('.alert-success');
                    $(alertSuccess).html("The selected rules are generated! See the output below.");
                    $(alertSuccess).show();
                    $(".rule-preview").html((response.triggers).replace(/(?:\n)/g, "<br>"));
                    $(".generate-spinner").show();
                }
            });

    //refresh table
    getAllRules();
}

function showGeneratedRule(response) {

}

function showRuleOnClick(id) {
    $('.rule-preview').append(
        "<div class=\"container trigger-example\">" +
        "        <div class=\"row\">" +
        "            <div class=\"col row\" style=\"padding-right: 0!important\">" +
        "                <div class=\"col inner\">col</div>" +
        "                <div class=\"col inner\">col</div>" +
        "            </div>" +
        "            <div class=\"col\" style=\"padding-right: 0!important\">" +
        "                <div class=\"col inner\">col</div>" +
        "                <div class=\"col inner\">col</div>" +
        "                <div class=\"col inner\">col</div>" +
        "                <div class=\"col inner\">col</div>" +
        "            </div>" +
        "            <div class=\"col\" style=\"padding-right: 0!important; padding-left: 0!important;\">" +
        "                <div class=\"col inner\">col</div>" +
        "                <div class=\"col inner\">col</div>" +
        "                <div class=\"col inner\">col</div>" +
        "                <div class=\"col inner\">col</div>" +
        "            </div>" +
        "            <div class=\"col text-center\">col</div>" +
        "        </div>" +
        "    </div>"
    );
}