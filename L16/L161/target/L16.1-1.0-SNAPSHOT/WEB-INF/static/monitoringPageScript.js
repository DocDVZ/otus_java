function buttonClick() {
    $.ajax({
        type: "GET",
        url: "/ajax/beans",
        success: function(result) {
            var acc = document.getElementById("accordion");
            var resultObject = result;
            resultObject.forEach(function (item, i, arr) {
                var label = document.createElement("label");
                label.setAttribute("class", "expandable");
                acc.appendChild(label);
                var h3 = document.createElement("h3");
                h3.innerHTML = item.name;
                var chckbox = document.createElement("input");
                chckbox.setAttribute("type", "checkbox");
                label.appendChild(h3);
                label.appendChild(chckbox);
                var tablediv = document.createElement("div");
                tablediv.setAttribute("class", "content");
                var tblHeaders = document.createElement("div");
                tblHeaders.setAttribute("class", "line");
                var nametd = document.createElement("div");
                nametd.setAttribute("class", "cell width-25")
                nametd.innerHTML = "Name";
                var typetd = document.createElement("div");
                typetd.setAttribute("class", "cell width-25")
                typetd.innerHTML = "Type";
                var valuetd = document.createElement("div");
                valuetd.setAttribute("class", "cell width-50")
                valuetd.innerHTML = "Value";
                tblHeaders.appendChild(nametd);
                tblHeaders.appendChild(typetd);
                tblHeaders.appendChild(valuetd);
                tablediv.appendChild(tblHeaders);
                item.attributes.forEach(function (attr, k, attrlist) {
                    var row = document.createElement("div");
                    row.setAttribute("class", "line");
                    var attrName = document.createElement("div");
                    attrName.innerHTML = attr.name;
                    attrName.setAttribute("class", "cell width-25")
                    var attrType = document.createElement("div");
                    attrType.innerHTML = attr.type;
                    attrType.setAttribute("class", "cell width-25")
                    var attrValue = document.createElement("div");
                    attrValue.innerHTML = attr.value;
                    attrValue.setAttribute("class", "cell width-50")
                    row.appendChild(attrName);
                    row.appendChild(attrType);
                    row.appendChild(attrValue);
                    tablediv.appendChild(row);
                })
                label.appendChild(tablediv);
            })
        },
        error: function(result) {
            alert('error');
        }
    })
}
