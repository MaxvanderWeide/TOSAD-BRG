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
                console.log(response);

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

                showRuleOnClick();
                $("table.existing-rules-wrapper").append(tableBody);
                return "ok";
            } else {
                $("#table-body").html("No rules found")
            }
        }).then(response => {
        $(".spinner-holder.maintain-spinner").hide();

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
    console.log(ids);

    //generate the rules
        fetch("/generate/rules", {
            method: "POST",
            headers: {"Authorization": sessionStorage.getItem("access_token")},
            body: ids
        })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else if (response.status === 404) {
                    let alertDanger = $('.alert-danger');
                    alertDanger.val();
                    alertDanger.append("Contact your technical administrator.");
                    alertDanger.show();
                }
            })
            .then(response => {
                if (response !== undefined) {
                    // show generated items...

                    let alertDanger = $('.alert-success');
                    alertDanger.val();
                    alertDanger.append("the selected rules are generated! See the output below.");
                    alertDanger.show();
                }
            });

    //refresh table
    getAllRules();
}

function showRuleOnClick(id) {
    $('#checkbox').change(function() {
        if($(this).is(":checked")) {
            let ruleIds = [];

            let returnVal = confirm("Are you sure?");
            $(this).attr("checked", returnVal);

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
    });
}