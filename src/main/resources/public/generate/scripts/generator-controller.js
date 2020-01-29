$(document).ready(function () {
    getAllRules();
    searchTable();

    $(".generate").click(() => {
        generate();
    });

    $(".insert").click(() => {
        let triggers = sessionStorage.getItem("triggers");

        if(triggers !== undefined) {
            insertCode(triggers.replace(/(?:<br>)/g, "\n"));
        } else {
            let alertDanger = $('.alert-danger');
            $(alertDanger).html("There is no known trigger code available, try again after regenerating...");
            $(alertDanger).show();
        }
    });

    $(".sample-code-block").hide();
    $(".insert").hide();
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
                let alertDanger = $('.alert-danger');
                $(alertDanger).html("There are no rules defined yet! Go to Define & Maintain to define your first rule.");
                $(alertDanger).show();
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

                    sessionStorage.setItem("triggers", response.triggers);
                    showGeneratedRule(sessionStorage.getItem("triggers"));

                    $(".insert").show();
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
        ("<samp>"+response+"</samp>").replace(/(?:\n)/g, "<br>")
    );

    sample.show();
}

function insertCode(triggers) {
    const rules = {};
    rules["triggers"] = triggers;

    console.log(triggers);

    fetch("/generate/rules/insert", {
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
                let alertSuccess = $('.alert-success');
                $(alertSuccess).html("The generated triggers are inserted in the selected target database! :)");
                $(alertSuccess).show();
            }
        });
}