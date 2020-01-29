$(document).ready(function () {
    getAllRules();
    searchTable();

    $(".generate").click(() => {
        generate();
    });

    $(".sample-code-block").hide();
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
                const tableBody = $("#table-body").html("");

                for (const index in response) {
                    let id = response[index]['id'];
                    $(tableBody).append("" +
                        "<tr>" +
                        "<td>" + id + "</td>" +
                        "<td>" + index + "</td>" +
                        "<td>" + response[index]['table'] + "</td>" +
                        "<td>" + response[index]['type']['type'] + "</td>" +
                        "<td data-id='"+ id +"'><input class='form-check-input' value='"+ id +"' type='checkbox' id='checkbox'></td>" +
                        "</tr>"
                    );
                }

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
    // $(".generate-spinner").show();
    let ids = [];
    let boxes = $('input[type=checkbox]');

    // get checked boxes
    boxes.each(function () {
        if (this.checked) {
            ids.push($(this).val());
        }
    });

    if(typeof ids !== 'undefined' && ids.length > 0) {
        $('.alert-danger').hide();
        $(".generate-spinner").show();
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
                    let alertSuccess = $('.alert-success');
                    $(alertSuccess).html("The selected rules are generated! See the output below.");
                    $(alertSuccess).show();
                    showGeneratedRule(response);
                }
            });
    } else {
        let alertDanger = $('.alert-danger');
        $(alertDanger).html("You haven't yet selected any rules to be created!");
        $(alertDanger).show();
    }

    //refresh table
    getAllRules();
}

function showGeneratedRule(response) {
    let sample = $(".sample-code-block");
    $(".generate-spinner").hide();

    sample.html(
        ("<samp>"+response.triggers+"</samp>").replace(/(?:\n)/g, "<br>")
    );

    sample.show();
}