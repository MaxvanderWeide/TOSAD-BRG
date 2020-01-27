loadRules();

function loadRules() {
    fetch("/generate/rules", {
        method: "GET",
        headers: {"Authorization": sessionStorage.getItem("access_token")},
    })
        .then(response => {
            if (response.status === 200) {
                $(".rule-items-wrapper").show();
                $(".spinner-holder").hide();
                return response.json();
            } else {
                alert("Something went wrong.");
                return null;
            }
        })
        .then(response => {
            if (response !== undefined && response !== null) {
                for (const k in response) {
                    const item = response[k];
                    $('#ruleTable').find('tbody:last').append('<tr data-toggle="modal" data-id="' + item["id"] + '" data-target="#orderModal">' +
                        '<td>' + item["name"] + '</td>' +
                        '<td>' + item["table"] + '</td>' +
                        '<td>' + item["type"]["type"] + '</td>' +
                        '</tr>');
                }
            }
        });
}

// function generateCode(element) {
//     fetch("/generate/rules", {
//         method: "POST",
//         headers: {"Authorization": sessionStorage.getItem("access_token")},
//         body: $(element).data("id")
//     })
//         .then(response => {
//             if (response.status === 201) {
//                 return response.text();
//             }
//         })
//         .then(response => {
//             if (response !== undefined) {
//                 $(element).parent().parent().find("div.generated-rule").text(response);
//             }
//         });
// }
