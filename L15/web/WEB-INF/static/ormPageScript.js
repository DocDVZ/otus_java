function newSimpleEntity() {
    var content = objectifyForm($("#newEntityForm").serializeArray());
    $.ajax({
            type: "POST",
            url: "/ormOperations",
            data: JSON.stringify(content),
            contentType: "application/json",
            dataType: "text",
            complete: function (xhr, textStatus) {
                writeResult(xhr.status + " - persist/update - " + xhr.responseText);
            }
        }
    )
}

function readSimpleEntity() {
    var content = $("#readInput").val();
    $.ajax({
            type: "GET",
            url: "/ormOperations",
            contentType: "application/json",
            dataType: "text",
            data: 'intField=' + content,
            complete: function (xhr, textStatus) {
                writeResult(xhr.status + " - read - " + xhr.responseText);
            }
        }
    )
}


function writeResult(e) {
    var results = document.getElementById("results");
    var p = document.createElement("p");
    p.innerHTML = e;
    results.appendChild(p);
}

function objectifyForm(formArray) {//serialize data function

    var returnArray = {};
    for (var i = 0; i < formArray.length; i++) {
        if (formArray[i]['value'] != null && formArray[i]['value'] != "") {
            returnArray[formArray[i]['name']] = formArray[i]['value'];
        }
    }
    return returnArray;
}