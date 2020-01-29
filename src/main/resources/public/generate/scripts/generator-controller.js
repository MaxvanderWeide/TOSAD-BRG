$(document).ready(function () {
    searchTable();
    startupEventListeners();
    loadFromStorage();
});

function startupEventListeners() {
    $(".generate").click(() => {
        generate();
    });

    $(".btn-connect").click(() => {
        createConnection();
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
}

function searchTable() {
    $("#search-rule").on("keyup", function () {
        const value = $(this).val().toLowerCase();
        $("#table-body tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
}

function createConnection() {
    let values = {};
    values["engine"] = $("#dbEngine").val();
    values["dbName"] = $("#dbName").val();
    values["host"] = $("#dbInputHost").val();
    values["port"] = $("#dbInputPort").val();
    values["service"] = $("#dbInputService").val();
    values["username"] = $("#dbInputUser").val();
    values["password"] = $("#dbInputPassword").val();
    values = JSON.stringify(values);

    fetch("/auth/connection", {
        method: "POST",
        body: values
    })
        .then(response => {
            if (response.status === 200) {
                return response.text();
            } else if (response.status === 400) {
                let alertDanger = $('.alert-danger');
                $(alertDanger).html("Can't create connection due to unfulfilled data requirements.");
                $(alertDanger).show();
            } else if (response.status === 403) {
                alert("You can't be authenticated. Contact your technical administrator.");
            }
        })
        .then(response => {
            if (response !== undefined) {
                $(".db-info-wrapper").hide();
                sessionStorage.setItem("access_token", response);
                sessionStorage.setItem("values", values);
                $('.alert-danger').hide();
                getAllRules();
                $(".search-rule-wrapper, .existing-rules-title").show();
            } else {
                $(".db-info-wrapper").show();
            }
            $(".spinner-holder.initial-spinner").hide();
        });
}

function loadFromStorage() {
    if (sessionStorage.getItem("values") != null) {
        const values = JSON.parse(sessionStorage.getItem("values"));
        $("#dbEngine").empty().append("<option value=" + values['engine'] + ">" + values['engine'] + "</option>");
        $("#dbInputHost").val(values['host']);
        $("#dbName").val(values['dbName']);
        $("#dbInputPort").val(values['port']);
        $("#dbInputService").val(values['service']);
        $("#dbInputUser").val(values['username']);
        $("#dbInputPassword").val(values['password']);

        createConnection();
    } else {
        $(".spinner-holder.initial-spinner").hide();
        $(".db-info-wrapper").show();
    }
}

function getAllRules() {
    $(".alert-danger").html("").hide();
    $(".alert-success").html("").hide();
    $(".existing-lead, #ruleTable, button.generate").hide();
    $(".existing-rules-spinner").show();
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
                        "<td>" + response[index]['type'] + "</td>" +
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
                $(".existing-rules-spinner").hide();
            }
        }).then(response => {
        $(".spinner-holder.initial-spinner, .existing-rules-spinner").hide();
        $("table.existing-rules-wrapper, button.generate").show();
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
        $(".sample-code-block").html("").hide();
        $('.alert-danger, .alert-success').hide();
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
                } else if (response.status === 404 || response.status === 500) {
                    $(".generate-spinner").hide();
                    let alertDanger = $('.alert-danger');
                    $(alertDanger).html("Some went wrong, contact your technical administrator.");
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
        $(".alert-success").html("").hide();
        $(".sample-code-block").html("").hide();
        $("button.insert").hide();
        $(".alert-danger").html("You haven't yet selected any rules to be created!").show();
        return;
    }
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
                $(alertDanger).html("Something went wrong, contact your technical administrator.");
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